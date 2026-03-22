Feature: Admin Authentication

  Scenario: Admin logs in with valid credentials and receives a token
    Given the admin has valid credentials
    When the admin sends a POST request to "/login"
    Then the response status code should be 200
    And the response should contain an access token
