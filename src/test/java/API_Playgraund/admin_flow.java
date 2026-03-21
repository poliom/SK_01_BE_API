package API_Playgraund;

import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.Test;
import utils.BaseTest;
import utils.FileUtils;

import java.util.Map;

import static io.restassured.RestAssured.given;
import static utils.FileUtils.writeResponseToFile;


public class admin_flow extends BaseTest {
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
    public void UpdateProduct() {
        Map<String, String> headers = Map.of(
                "Content-Type", "application/json",
                "accept", "application/json",
                "Authorization", "Bearer " + admin_flow()
        );
        String product_id = "10000000-0000-0000-0000-000000000003";
        Response response =
                RestUtils.responseRestAssureFromFile(
                        "https://testing-ap-iground-dnd1r1gih-vidko-videvs-projects.vercel.app/api",
                        "/products/" + product_id,
                        "PUT",
                        headers,
                        "requestBodies/updateProduct.json");

        writeResponseToFile("response_update_product", response.asString());

        RestUtils.responseRestAssureValidation(response, 200);
        System.out.println("Admin Flow Test:");
        System.out.println("Status Code: " + response.getStatusCode());
        System.out.println("Response: " + response.asString());
    }
}
