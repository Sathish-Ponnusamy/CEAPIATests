Feature: API Status

  Scenario: Check server status endpoint
    When I send GET request to status endpoint
    Then the response status code should be 200
    Then the response JSON should have "status" equal to "Server is running"