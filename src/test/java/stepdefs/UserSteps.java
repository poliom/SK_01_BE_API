package stepdefs;

import API_Playgraund.RestUtils;
import io.cucumber.java.After;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.When;
import io.restassured.response.Response;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import utils.DatabaseUtils;

import java.util.HashMap;
import java.util.Map;

public class UserSteps {

    private static final Logger log = LogManager.getLogger("requests");
    private static final String BASE_URL =
            "https://testing-ap-iground-dnd1r1gih-vidko-videvs-projects.vercel.app/api";

    private String requestBody;
    private String currentEmail;

    @Given("a new user with username {string}, email {string} and password {string}")
    public void newUser(String username, String email, String password) {
        currentEmail = email;
        requestBody = String.format(
                "{\"full_name\":\"%s\",\"email\":\"%s\",\"password\":\"%s\"}",
                username, email, password);
    }

    @Given("an already registered user with email {string}")
    public void alreadyRegisteredUser(String email) {
        requestBody = String.format(
                "{\"full_name\":\"existing\",\"email\":\"%s\",\"password\":\"Test1234!\"}", email);
    }

    @When("the user sends a POST request to {string}")
    public void userSendsPostRequest(String endpoint) {
        Map<String, String> headers = new HashMap<>();
        headers.put("Content-Type", "application/json");
        headers.put("accept", "application/json");

        Response response = RestUtils.responseRestAssure(
                BASE_URL, "/auth" + endpoint, "POST", headers, requestBody);

        ScenarioContext.setResponse(response);
    }

    @After
    public void teardown() throws Exception {
        if (currentEmail == null) return;

        DatabaseUtils db = new DatabaseUtils();
        db.connect();
        try {
            int deleted = db.executeUpdate(
                    "DELETE FROM api_ground.profiles WHERE email = '" + currentEmail + "'");
            log.info("Teardown: deleted {} profile(s) for email: {}", deleted, currentEmail);
        } finally {
            db.disconnect();
            currentEmail = null;
        }
    }
}
