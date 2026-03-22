package stepdefs;

import API_Playgraund.RestUtils;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.When;
import io.restassured.response.Response;
import org.testng.Assert;

import java.util.HashMap;
import java.util.Map;

public class AdminSteps {

    private static final String BASE_URL =
            "https://testing-ap-iground-dnd1r1gih-vidko-videvs-projects.vercel.app/api";

    @Given("the admin has valid credentials")
    public void adminHasValidCredentials() {
        // credentials are loaded from requestBodies/admin_login.json in the When step
    }

    @When("the admin sends a POST request to {string}")
    public void adminSendsPostRequest(String endpoint) {
        Map<String, String> headers = new HashMap<>();
        headers.put("Content-Type", "application/json");
        headers.put("accept", "application/json");

        Response response = RestUtils.responseRestAssureFromFile(
                BASE_URL, "/auth" + endpoint, "POST", headers,
                "requestBodies/admin_login.json");

        ScenarioContext.setResponse(response);
    }

    @And("the response should contain an access token")
    public void responseShouldContainAccessToken() {
        String token = ScenarioContext.getResponse().jsonPath().getString("access_token");
        Assert.assertNotNull(token, "Expected access_token in response but was null");
        Assert.assertFalse(token.isEmpty(), "Expected access_token to be non-empty");
    }
}
