package Automation_Exercise;

import io.restassured.response.Response;
import org.testng.annotations.Test;
import utils.BaseTest;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

public class automationexerciseTest extends BaseTest {

    private static final String BASE_URI = "https://automationexercise.com";

    @Test
    public void getProductsList_shouldReturn200() {
        given()
            .baseUri(BASE_URI)
            .when()
                .get("/api/productsList")
            .then()
                .statusCode(200);
    }

    @Test(enabled = false)
    public void getProductsList_shouldReturnJsonWithProducts() {
        given()
            .baseUri(BASE_URI)
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
            .baseUri(BASE_URI)
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
            .baseUri(BASE_URI)
            .when()
                .get("/api/productsList")
            .then()
                .extract()
                .response();

        int productCount = response.jsonPath().getList("products").size();
        assert productCount > 0 : "Products list should not be empty, but got: " + productCount;
    }
}
