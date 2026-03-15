package Automation_Exercise;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

public class automationexerciseTest {

    @BeforeClass
    public void setUp() {
        RestAssured.baseURI = "https://automationexercise.com";
    }

    @Test
    public void getProductsList_shouldReturn200() {
        given()
            .when()
                .get("/api/productsList")
            .then()
                .statusCode(200);
    }

    @Test
    public void getProductsList_shouldReturnJsonWithProducts() {
        given()
            .when()
                .get("/api/productsList")
            .then()
                .statusCode(200)
                .body("responseCode", equalTo(200))
                .body("products", notNullValue())
                .body("products", not(empty()));
    }

    @Test
    public void getProductsList_shouldHaveRequiredProductFields() {
        given()
            .when()
                .get("/api/productsList")
            .then()
                .statusCode(200)
                .body("products[0].id", notNullValue())
                .body("products[0].name", notNullValue())
                .body("products[0].price", notNullValue())
                .body("products[0].brand", notNullValue())
                .body("products[0].category", notNullValue());
    }

    @Test
    public void getProductsList_responseShouldNotBeEmpty() {
        Response response = given()
            .when()
                .get("/api/productsList")
            .then()
                .extract()
                .response();

        int productCount = response.jsonPath().getList("products").size();
        assert productCount > 0 : "Products list should not be empty, but got: " + productCount;
    }
}
