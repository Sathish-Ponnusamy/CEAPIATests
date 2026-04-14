**Automation Framework: Selenium & RestAssured**

**A. Software Prerequisites**

1. **Java JDK 17**: Ensure JAVA_HOME is set in your environment variables.

2. **Maven**: Ensure the mvn command is accessible from your terminal/command prompt.

**B. Automation Framework Components**

The framework is built with the following integrations:

1. **Language**: Java

2. **BDD Framework**: Cucumber with Gherkin syntax

3. **API Testing**: RestAssured

**Reporting**: Extent Reports (HTML format)

**C. How to Run**
Follow these steps to execute the test suite:

1. Open your terminal or command prompt.

2. Navigate to the project root directory.

3. Run the following command to perform a clean build and execute all tests:

    ***mvn clean install***

**D. Test Reports**

Upon completion of the test execution, the Extent HTML report will be generated. 
1. You can find the report file at:
***target/ExtentReports/ExtentReport.html***