![CI](https://github.com/poliom/SK_01_BE_API/actions/workflows/ci.yml/badge.svg)

# SK_01 — API Test Automation

REST API test automation project using RestAssured, TestNG, Cucumber BDD, and PostgreSQL DB validation.

## Stack
- **RestAssured** — HTTP client for API testing
- **TestNG** — test runner with parallel execution
- **Cucumber** — BDD layer with Gherkin feature files
- **Log4j2** — timestamped logging to file and HTML report
- **ExtentReports** — HTML test report
- **PostgreSQL JDBC** — DB validation and teardown

## Run tests
```bash
mvn clean test
```

## Reports
- **Extent HTML report** — `src/test/resources/test-output/ExtentReport.html`
- **Cucumber HTML report** — `target/cucumber-reports/report.html`
- **Log file** — `src/test/resources/logs/test_<timestamp>.log`
