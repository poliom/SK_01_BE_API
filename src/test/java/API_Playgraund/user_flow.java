package API_Playgraund;

import io.restassured.response.Response;
import org.testng.annotations.Test;

import java.util.Map;

public class user_flow {
    @Test
    public void regUser() {

        String body = "{\n" +
                "  \"email\": \"test_empty_headers@test.com\",\n" +
                "  \"password\": \"User123!\",\n" +
                "  \"full_name\": \"vidko videv\"\n" +
                "}";

        Response response =
                RestUtils.responseRestAssure(
                        "https://testing-ap-iground-dnd1r1gih-vidko-videvs-projects.vercel.app/api",
                        "/auth/register",
                        "POST",
                        Map.of(),
                        body);

        System.out.println("Admin Flow Test:");
        System.out.println("Status Code: " + response.getStatusCode());
        System.out.println("Response: " + response.asString());

        RestUtils.responseRestAssureValidation(response, 409);
    }
}
