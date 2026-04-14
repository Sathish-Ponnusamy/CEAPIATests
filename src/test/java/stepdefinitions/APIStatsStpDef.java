package stepdefinitions;

import io.cucumber.java.en.When;
import io.cucumber.java.en.Then;
import io.restassured.response.Response;
import static io.restassured.RestAssured.given;
import org.junit.jupiter.api.Assertions;
import utils.ConfigReader;

public class APIStatsStpDef {
    private Response response;

    @When("I send GET request to {string}")
    public void i_send_get_request_to(String url) {
        response = given()
                .when()
                .get(url)
                .then()
                .extract()
                .response();
    }

    @When("I send GET request to status endpoint")
    public void i_send_get_request_to_status_endpoint() {
        String url = ConfigReader.getStatusUrl();
        if (url == null || url.isEmpty()) {
            throw new IllegalStateException("status.url is not in config.properties");
        }
        i_send_get_request_to(url);
    }

    @Then("the response status code should be {int}")
    public void the_response_status_code_should_be(Integer expected) {
        Assertions.assertNotNull(response, "Response is null");
        Assertions.assertEquals(expected.intValue(), response.getStatusCode(), "Not expected status code");
    }

    @Then("the response JSON should have {string} equal to {string}")
    public void the_response_json_should_have_equal_to(String key, String expectedValue) {
        Assertions.assertNotNull(response, "Response is null");
        String actual = response.jsonPath().getString(key);
        Assertions.assertEquals(expectedValue, actual, "Not expected JSON value for " + key);
    }
}
