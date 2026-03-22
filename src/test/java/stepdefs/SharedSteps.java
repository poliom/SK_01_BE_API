package stepdefs;

import io.cucumber.java.en.Then;
import io.restassured.response.Response;

public class SharedSteps {

    @Then("the response status code should be {int}")
    public void responseStatusCodeShouldBe(int statusCode) {
        Response response = ScenarioContext.getResponse();
        API_Playgraund.RestUtils.responseRestAssureValidation(response, statusCode);
    }
}
