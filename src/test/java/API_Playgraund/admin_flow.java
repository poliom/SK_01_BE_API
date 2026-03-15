package API_Playgraund;

import io.restassured.response.Response;
import org.testng.annotations.Test;

import java.util.Map;

import static io.restassured.RestAssured.given;


public class admin_flow {
    @Test
    public String admin_flow() {
        Map<String, String> headers = Map.of(
                "Content-Type", "application/json",
                "accept", "application/json"
        );
        String body = "{\n" +
                "  \"email\": \"user@test.com\",\n" +
                "  \"password\": \"User123!\"\n" +
                "}";

        Response response =
                RestUtils.responseRestAssure(
                        "https://testing-ap-iground-dnd1r1gih-vidko-videvs-projects.vercel.app/api",
                        "/auth/login",
                        "POST",
                        headers,
                        body);

        RestUtils.responseRestAssureValidation(response, 200);

        //System.out.println("Admin Flow Test:");
        //System.out.println("Status Code: " + response.getStatusCode());
        //System.out.println("Response: " + response.asString());
        return response.jsonPath().getString("access_token");
    }

    @Test
    public void admin_flow_2() {
        Map<String, String> headers = Map.of(
                "Content-Type", "application/json",
                "accept", "application/json"
        );
        String body = "{\n" +
                "  \"email\": \"user@test.com\",\n" +
                "  \"password\": \"User123!\"\n" +
                "}";
        Response response =given()
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
    public void UpdateProduct(){
        Map<String, String> headers = Map.of(
                "Content-Type", "application/json",
                "accept", "application/json",
                "Authorization","Bearer " + admin_flow()
        );
        String body = "{\n" +
                "  \"name\": \"Mechanical Keyboard X\",\n" +
                "  \"price\": 50.99,\n" +
                "  \"category\": \"electronics\",\n" +
                "  \"stock\": 8\n" +
                "}";
        String product_id = "10000000-0000-0000-0000-000000000003";
        Response response =
                RestUtils.responseRestAssure(
                        "https://testing-ap-iground-dnd1r1gih-vidko-videvs-projects.vercel.app/api",
                        "/products/" + product_id,
                        "PUT",
                        headers,
                        body);

        RestUtils.responseRestAssureValidation(response, 200);
        System.out.println("Admin Flow Test:");
        System.out.println("Status Code: " + response.getStatusCode());
        System.out.println("Response: " + response.asString());
    }
}
