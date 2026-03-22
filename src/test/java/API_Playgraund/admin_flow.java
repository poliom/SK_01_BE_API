package API_Playgraund;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.restassured.response.Response;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.testng.Assert;
import org.testng.annotations.AfterTest;
import org.testng.annotations.Test;
import utils.BaseTest;
import utils.DatabaseUtils;
import utils.FileUtils;

import java.sql.ResultSet;
import java.util.Map;

import static io.restassured.RestAssured.given;
import static utils.FileUtils.writeResponseToFile;


public class admin_flow extends BaseTest {

    private static final Logger log = LogManager.getLogger("requests");

    private static final String PRODUCT_ID = "10000000-0000-0000-0000-000000000003";

    @AfterTest
    public void revertProduct() throws Exception {
        String sql = String.format(
                "UPDATE api_ground.products" +
                " SET price = 49.99, stock = 25, updated_at = NOW()" +
                " WHERE id = '%s'", PRODUCT_ID);

        DatabaseUtils db = new DatabaseUtils();
        db.connect();
        try {
            int updated = db.executeUpdate(sql);
            log.info("Cleanup: reverted product {} — rows affected: {}", PRODUCT_ID, updated);
        } finally {
            db.disconnect();
        }
    }

    @Test
    public String admin_flow() {
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
                        "requestBodies/admin_login.json");

        RestUtils.responseRestAssureValidation(response, 200);
        writeResponseToFile("admin_login_response.json", response.asString());
        Assert.assertTrue(FileUtils.responseFileExists("admin_login_response.json"),
                "Response file was not created: admin_login_response.json");

        return response.jsonPath().getString("access_token");
    }

    @Test
    public void admin_flow_2() {
        Map<String, String> headers = Map.of(
                "Content-Type", "application/json",
                "accept", "application/json"
        );
        String body = FileUtils.readFileAsString("requestBodies/login.json");
        Response response = given()
                .baseUri("https://testing-ap-iground-dnd1r1gih-vidko-videvs-projects.vercel.app/api")
                .headers(headers)
                .when()
                .body(body)
                .post("/auth/login")
                .then()
                .statusCode(200)
                .extract().response();
        System.out.println("Admin Flow Test:");
        System.out.println("Status Code: " + response.getStatusCode());
        System.out.println("Response: " + response.asString());
    }

    @Test
    public void UpdateProduct() throws Exception {
        Map<String, String> headers = Map.of(
                "Content-Type", "application/json",
                "accept", "application/json",
                "Authorization", "Bearer " + admin_flow()
        );

        Response response =
                RestUtils.responseRestAssureFromFile(
                        "https://testing-ap-iground-dnd1r1gih-vidko-videvs-projects.vercel.app/api",
                        "/products/" + PRODUCT_ID,
                        "PUT",
                        headers,
                        "requestBodies/updateProduct.json");

        writeResponseToFile("response_update_product", response.asString());
        RestUtils.responseRestAssureValidation(response, 200);

        // Parse expected values from the same file sent to the API
        JsonNode expected = new ObjectMapper()
                .readTree(FileUtils.readFileAsString("requestBodies/updateProduct.json"));
        String expectedName  = expected.get("name").asText();
        double expectedPrice = expected.get("price").asDouble();
        int    expectedStock = expected.get("stock").asInt();

        // Verify the DB reflects what the API PUT persisted
        DatabaseUtils db = new DatabaseUtils();
        db.connect();
        try {
            String sql = String.format(
                    "SELECT id, name, price, stock, updated_at" +
                    " FROM api_ground.products" +
                    " WHERE id = '%s'", PRODUCT_ID);

            ResultSet rs = db.executeQuery(sql);

            Assert.assertTrue(rs.next(), "No DB row found for product id: " + PRODUCT_ID);
            Assert.assertEquals(rs.getString("name"), expectedName,
                    "DB name mismatch");
            Assert.assertEquals(rs.getDouble("price"), expectedPrice, 0.001,
                    "DB price mismatch");
            Assert.assertEquals(rs.getInt("stock"), expectedStock,
                    "DB stock mismatch");

            log.info("DB validation passed — name: {}, price: {}, stock: {}, updated_at: {}",
                    rs.getString("name"),
                    rs.getDouble("price"),
                    rs.getInt("stock"),
                    rs.getTimestamp("updated_at"));

            rs.close();
        } finally {
            db.disconnect();
        }
    }
}
