package API_Playgraund;

import io.restassured.response.Response;

import java.util.Map;

import static io.restassured.RestAssured.given;

public class RestUtils {
    public static void responseRestAssureValidation(Response response, int statuscode) {
        switch (statuscode) {
            case 200:
                response.then().statusCode(200);
                break;
            case 201:
                response.then().statusCode(201);
                break;
            case 400:
                response.then().statusCode(400);
                break;
            case 401:
                response.then().statusCode(401);
                break;
            case 403:
                response.then().statusCode(403);
                break;
            case 404:
                response.then().statusCode(404);
                break;
            default:
                throw new IllegalArgumentException("Unsupported status code: " + statuscode);
        }
    }
    public static Response responseRestAssure(String baseURL, String endpoint, String Method, Map<String, String> headers, String body) {
        switch (Method.toUpperCase()) {
            case "GET":
                return given()
                        .baseUri(baseURL)
                        .headers(headers)
                        .when()
                        .body(body)
                        .get(endpoint);
            case "POST":
                return given()
                        .baseUri(baseURL)
                        .headers(headers)
                        .when()
                        .body(body)
                        .post(endpoint);
            case "PUT":
                return given()
                        .baseUri(baseURL)
                        .headers(headers)
                        .when()
                        .body(body)
                        .put(endpoint);
            default:
                throw new IllegalArgumentException("Unsupported HTTP method: " + Method);
        }
    }
}
