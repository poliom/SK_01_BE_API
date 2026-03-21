package Automation_Exercise;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.testng.annotations.Test;
import utils.BaseTest;

public class post_search extends BaseTest {

    private static final String BASE_URI = "https://automationexercise.com";

    @Test
    public void PostSearch() {
        Response response = RestAssured
                .given()
                .baseUri(BASE_URI)
                .when()
                .post("/api/searchProduct/jean");

        System.out.println("Status code: " + response.getStatusCode());
    }
}
