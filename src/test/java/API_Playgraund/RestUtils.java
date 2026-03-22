package API_Playgraund;

import io.restassured.response.Response;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import utils.FileUtils;

import java.util.Map;

import static io.restassured.RestAssured.given;

public class RestUtils {

    private static final Logger requestLog = LogManager.getLogger("requests");
    private static final Logger responseLog = LogManager.getLogger("responses");

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
            case 409:
                response.then().statusCode(409);
                break;
            default:
                throw new IllegalArgumentException("Unsupported status code: " + statuscode);
        }
    }

    public static Response responseRestAssure(String baseURL, String endpoint, String Method, Map<String, String> headers, String body) {
        requestLog.info(">>> {} {}{}", Method.toUpperCase(), baseURL, endpoint);
        requestLog.info("Request body: {}", body);

        long start = System.currentTimeMillis();
        Response response;

        switch (Method.toUpperCase()) {
            case "GET":
                response = given()
                        .baseUri(baseURL)
                        .headers(headers)
                        .when()
                        .body(body)
                        .get(endpoint);
                break;
            case "POST":
                response = given()
                        .baseUri(baseURL)
                        .headers(headers)
                        .when()
                        .body(body)
                        .post(endpoint);
                break;
            case "PUT":
                response = given()
                        .baseUri(baseURL)
                        .headers(headers)
                        .when()
                        .body(body)
                        .put(endpoint);
                break;
            default:
                throw new IllegalArgumentException("Unsupported HTTP method: " + Method);
        }

        long elapsed = System.currentTimeMillis() - start;
        requestLog.info("<<< Status: {} | Time: {}ms", response.getStatusCode(), elapsed);
        responseLog.debug("Response body: {}", response.asString());

        return response;
    }

    public static Response responseRestAssureFromFile(String baseURL, String endpoint, String method, Map<String, String> headers, String filePath) {
        requestLog.info("Loading request body from file: {}", filePath);
        String body = FileUtils.readFileAsString(filePath);
        return responseRestAssure(baseURL, endpoint, method, headers, body);
    }
}
