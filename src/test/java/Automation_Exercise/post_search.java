package Automation_Exercise;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.testng.annotations.Test;

public class post_search {
    @Test
    public void PostSearch() {
        RestAssured.baseURI = "https://automationexercise.com";
        Response response = RestAssured.post("/api/searchProduct/jean");
        System.out.println("Status code: " + response.getStatusCode());
    }
}
