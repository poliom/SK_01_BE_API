package API_Playgraund;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.restassured.response.Response;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import utils.BaseTest;
import utils.FileUtils;

import java.util.List;
import java.util.Map;

import static utils.FileUtils.writeResponseToFile;

public class user_flow extends BaseTest {

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
    }

    @DataProvider(name = "usersFromFile")
    public Object[][] usersFromFile() throws Exception {
        String json = FileUtils.readFileAsString("testdata/users.json");
        ObjectMapper mapper = new ObjectMapper();
        List<Map<String, String>> users = mapper.readValue(json, List.class);

        Object[][] data = new Object[users.size()][1];
        for (int i = 0; i < users.size(); i++) {
            Map<String, String> user = users.get(i);
            String body = String.format(
                    "{\"email\": \"%s\", \"password\": \"%s\", \"full_name\": \"%s\"}",
                    user.get("email"), user.get("password"), user.get("username")
            );
            data[i][0] = body;
        }
        return data;
    }

    @Test(dataProvider = "usersFromFile")
    public void regUserFromFile(String body) {
        Response response =
                RestUtils.responseRestAssure(
                        "https://testing-ap-iground-dnd1r1gih-vidko-videvs-projects.vercel.app/api",
                        "/auth/register",
                        "POST",
                        Map.of("Content-Type", "application/json", "accept", "application/json"),
                        body);

        System.out.println("Register User Test:");
        System.out.println("Status Code: " + response.getStatusCode());
        System.out.println("Response: " + response.asString());
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
