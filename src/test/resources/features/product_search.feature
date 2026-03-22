Feature: Get All Products

  Scenario Outline: Retrieve products with pagination and sorting
    Given the products endpoint is available
    When the user requests products with sort "<sort>", order "<order>", page <page> and size <size>
    Then the response status code should be 200
    And the response should contain at least 1 product

    Examples:
      | sort       | order | page | size |
      | created_at | desc  | 1    | 10   |
      | created_at | asc   | 1    | 5    |
      | created_at | desc  | 2    | 10   |
