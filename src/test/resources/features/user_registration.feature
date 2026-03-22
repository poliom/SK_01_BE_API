Feature: User Registration

  Scenario: New user registers successfully
    Given a new user with username "testuser", email "testuser@mail.com" and password "Test1234!"
    When the user sends a POST request to "/register"
    Then the response status code should be 201

  Scenario: Duplicate user registration is rejected
    Given an already registered user with email "user_1773733782545@test.com"
    When the user sends a POST request to "/register"
    Then the response status code should be 408
