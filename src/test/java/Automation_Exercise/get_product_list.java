package Automation_Exercise;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.testng.annotations.Test;

public class get_product_list {

    @Test
    public void GetProductsList() {
        RestAssured.baseURI = "https://automationexercise.com";

        Response response =
                RestAssured
                        .given()
                        .when()
                        .get("/api/productsList");

        System.out.println("Status code: " + response.getStatusCode());
        System.out.println("Response body: " + response.getBody().asString());
    }
    @Test
    public void GetProductsListBrand(){
        RestAssured.baseURI = "https://automationexercise.com";

        Response response =
                RestAssured
                        .given()
                        .when()
                        .get("/api/brandsList");

        System.out.println("Status code: " + response.getStatusCode());
        System.out.println("Response body: " + response.getBody().asString());
        String brand = response.jsonPath().getString("brands[0].brand");
        System.out.println("Brand of the first product: " + brand);
    }
}
