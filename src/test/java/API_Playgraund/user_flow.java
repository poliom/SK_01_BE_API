package API_Playgraund;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.restassured.response.Response;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.testng.Assert;
import org.testng.annotations.AfterTest;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import utils.BaseTest;
import utils.DatabaseUtils;
import utils.FileUtils;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static utils.FileUtils.writeResponseToFile;

public class user_flow extends BaseTest {

    private static final Logger log = LogManager.getLogger("requests");

    @Test
    public void regUser() {
        Response response =
                RestUtils.responseRestAssureFromFile(
                        "https://testing-ap-iground-dnd1r1gih-vidko-videvs-projects.vercel.app/api",
                        "/auth/register",
                        "POST",
                        Map.of(),
                        "requestBodies/registerUser.json");

        System.out.println("Admin Flow Test:");
        System.out.println("Status Code: " + response.getStatusCode());
        System.out.println("Response: " + response.asString());

        RestUtils.responseRestAssureValidation(response, 409);

        Assert.assertEquals(response.getStatusCode(), 409, "Expected status code 409 for duplicate registration");
    }

    @DataProvider(name = "usersFromFile")
    public Object[][] usersFromFile() throws Exception {
        String json = FileUtils.readFileAsString("testdata/users.json");
        ObjectMapper mapper = new ObjectMapper();
        List<Map<String, String>> users = mapper.readValue(json, List.class);

        Object[][] data = new Object[users.size()][3];
        for (int i = 0; i < users.size(); i++) {
            Map<String, String> user = users.get(i);
            String body = String.format(
                    "{\"email\": \"%s\", \"password\": \"%s\", \"full_name\": \"%s\"}",
                    user.get("email"), user.get("password"), user.get("username")
            );
            data[i][0] = body;
            data[i][1] = user.get("email");
            data[i][2] = user.get("username");
        }
        return data;
    }

    @Test(dataProvider = "usersFromFile")
    public void regUserFromFile(String body, String email, String fullName) throws Exception {
        Response response =
                RestUtils.responseRestAssure(
                        "https://testing-ap-iground-dnd1r1gih-vidko-videvs-projects.vercel.app/api",
                        "/auth/register",
                        "POST",
                        Map.of("Content-Type", "application/json", "accept", "application/json"),
                        body);

        Assert.assertEquals(response.getStatusCode(), 201, "Unexpected status code for email: " + email);

        log.info("Register response: {} for {}", response.getStatusCode(), email);

        DatabaseUtils db = new DatabaseUtils();
        db.connect();
        try {
            String sql = String.format(
                    "SELECT id, email, full_name, role, avatar_url, created_at, updated_at" +
                    " FROM api_ground.profiles" +
                    " WHERE email = '%s'", email);

            ResultSet rs = db.executeQuery(sql);

            Assert.assertTrue(rs.next(), "No DB row found for email: " + email);
            Assert.assertEquals(rs.getString("email"), email,
                    "DB email mismatch for user: " + email);
            Assert.assertEquals(rs.getString("full_name"), fullName,
                    "DB full_name mismatch for user: " + email);

            log.info("DB validation passed — email: {}, full_name: {}, role: {}, created_at: {}",
                    rs.getString("email"),
                    rs.getString("full_name"),
                    rs.getString("role"),
                    rs.getTimestamp("created_at"));

            rs.close();
        } finally {
            db.disconnect();
        }
    }

    @DataProvider(name = "usersFromDB")
    public Object[][] usersFromDB() throws Exception {
        List<Object[]> rows = new ArrayList<>();

        DatabaseUtils db = new DatabaseUtils();
        db.connect();
        try {
            ResultSet rs = db.executeQuery(
                    "SELECT id, email, full_name, role" +
                    " FROM api_ground.profiles" +
                    " WHERE role = 'USER'" +
                    " ORDER BY created_at DESC" +
                    " LIMIT 10");

            while (rs.next()) {
                rows.add(new Object[]{
                        rs.getString("id"),
                        rs.getString("email"),
                        rs.getString("full_name"),
                        rs.getString("role")
                });
            }
            rs.close();
        } finally {
            db.disconnect();
        }

        return rows.toArray(new Object[0][]);
    }

    @Test(dataProvider = "usersFromDB")
    public void verifyProfileFromDB(String id, String email, String fullName, String role) {
        log.info("Verifying profile — id: {}, email: {}, full_name: {}, role: {}", id, email, fullName, role);

        Assert.assertNotNull(id,       "Profile id should not be null");
        Assert.assertNotNull(email,    "Profile email should not be null");
        Assert.assertNotNull(fullName, "Profile full_name should not be null");
        Assert.assertEquals(role, "USER", "Expected role 'USER' but got: " + role);
        Assert.assertTrue(email.contains("@"), "Email should contain '@': " + email);
    }

    @AfterTest
    public void cleanupUsers() throws Exception {
        String json = FileUtils.readFileAsString("testdata/users.json");
        ObjectMapper mapper = new ObjectMapper();
        List<Map<String, String>> users = mapper.readValue(json, List.class);

        String emailList = users.stream()
                .map(u -> "'" + u.get("email") + "'")
                .collect(Collectors.joining(", "));

        String sql = "DELETE FROM api_ground.profiles WHERE email IN (" + emailList + ")";

        DatabaseUtils db = new DatabaseUtils();
        db.connect();
        try {
            int deleted = db.executeUpdate(sql);
            log.info("Cleanup: deleted {} test user(s) from api_ground.profiles", deleted);
        } finally {
            db.disconnect();
        }
    }

    @Test
    public void login_user() {
        Map<String, String> headers = Map.of(
                "Content-Type", "application/json",
                "accept", "application/json"
        );

        Response response =
                RestUtils.responseRestAssureFromFile(
                        "https://testing-ap-iground-dnd1r1gih-vidko-videvs-projects.vercel.app/api",
                        "/auth/login",
                        "POST",
                        headers,
                        "requestBodies/login.json");

        writeResponseToFile("user_login_response", response.asString());
        RestUtils.responseRestAssureValidation(response, 200);
    }
}
