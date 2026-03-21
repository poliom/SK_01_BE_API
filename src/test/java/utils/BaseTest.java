package utils;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeSuite;

public class BaseTest {

    protected static ExtentReports extent;

    protected static synchronized ExtentTest createTest(String testName) {
        return extent.createTest(testName);
    }

    @BeforeSuite
    public void initReport() {
        ExtentSparkReporter spark = new ExtentSparkReporter("src/test/resources/test-output/ExtentReport.html");
        spark.config().setReportName("API Test Report");
        spark.config().setDocumentTitle("SK_01 Test Results");

        extent = new ExtentReports();
        extent.attachReporter(spark);
        extent.setSystemInfo("Environment", "QA");
        extent.setSystemInfo("Tester", "Automation");
    }

    @AfterSuite
    public void flushReport() {
        if (extent != null) {
            extent.flush();
        }
    }
}
