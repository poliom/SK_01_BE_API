package API_Playgraund;

import io.restassured.response.Response;

import java.util.Map;

import static io.restassured.RestAssured.given;

public class RestUtils {
    public static Response responseRestAssure(String baseURL, String endpoint, String Method, Map<String, String> headers, String body) {
        switch (Method.toUpperCase()) {
            case "GET":
                return given()
                        .baseUri(baseURL)
                        .headers(headers)
                        .when()
                        .body(body)
                        .get(endpoint)
                        .then()
                        .statusCode(200)
                        .extract().response();
            case "POST":
                return given()
                        .baseUri(baseURL)
                        .headers(headers)
                        .when()
                        .body(body)
                        .post(endpoint)
                        .then()
                        .statusCode(200)
                        .extract().response();
            case "PUT":
                return given()
                        .baseUri(baseURL)
                        .headers(headers)
                        .when()
                        .body(body)
                        .put(endpoint)
                        .then()
                        .statusCode(200)
                        .extract().response();
            default:
                throw new IllegalArgumentException("Unsupported HTTP method: " + Method);
        }
    }
}
