package Automation_Exercise;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.testng.annotations.Test;
import utils.BaseTest;

public class get_product_list extends BaseTest {

    private static final String BASE_URI = "https://automationexercise.com";

    @Test
    public void GetProductsList() {
        Response response = RestAssured
                .given()
                .baseUri(BASE_URI)
                .when()
                .get("/api/productsList");

        System.out.println("Status code: " + response.getStatusCode());
        System.out.println("Response body: " + response.getBody().asString());
    }

    @Test
    public void GetProductsListBrand() {
        Response response = RestAssured
                .given()
                .baseUri(BASE_URI)
                .when()
                .get("/api/brandsList");

        System.out.println("Status code: " + response.getStatusCode());
        System.out.println("Response body: " + response.getBody().asString());
        String brand = response.jsonPath().getString("brands[0].brand");
        System.out.println("Brand of the first product: " + brand);
    }
}
