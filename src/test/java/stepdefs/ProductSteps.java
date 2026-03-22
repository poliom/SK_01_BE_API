package stepdefs;

import API_Playgraund.RestUtils;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.When;
import io.restassured.response.Response;
import org.testng.Assert;

import java.util.Map;

public class ProductSteps {

    private static final String BASE_URL =
            "https://testing-ap-iground-dnd1r1gih-vidko-videvs-projects.vercel.app/api";

    @Given("the products endpoint is available")
    public void productsEndpointIsAvailable() {
        // endpoint availability is validated implicitly by the status code assertion
    }

    @When("the user requests products with sort {string}, order {string}, page {int} and size {int}")
    public void userRequestsProducts(String sort, String order, int page, int size) {
        String endpoint = String.format(
                "/products?sort=%s&order=%s&page=%d&size=%d", sort, order, page, size);

        Response response = RestUtils.responseRestAssure(
                BASE_URL,
                endpoint,
                "GET",
                Map.of("accept", "application/json"),
                "");

        ScenarioContext.setResponse(response);
    }

    @And("the response should contain at least 1 product")
    public void responseShouldContainAtLeast1Product() {
        Response response = ScenarioContext.getResponse();
        int count = response.jsonPath().getList("data").size();
        Assert.assertTrue(count >= 1,
                "Expected at least 1 product in response but got: " + count);
    }
}
