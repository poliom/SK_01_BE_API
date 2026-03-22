package utils;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeSuite;

public class BaseTest {

    protected static ExtentReports extent;

    protected static synchronized ExtentTest createTest(String testName) {
        if (extent == null) {
            initExtent();
        }
        return extent.createTest(testName);
    }

    static synchronized void initExtent() {
        if (extent != null) return;
        ExtentSparkReporter spark = new ExtentSparkReporter("src/test/resources/test-output/ExtentReport.html");
        spark.config().setReportName("API Test Report");
        spark.config().setDocumentTitle("SK_01 Test Results");

        extent = new ExtentReports();
        extent.attachReporter(spark);
        extent.setSystemInfo("Environment", "QA");
        extent.setSystemInfo("Tester", "Automation");
    }

    static synchronized void flushExtent() {
        if (extent != null) {
            extent.flush();
        }
    }

    @BeforeSuite
    public void initReport() {
        initExtent();
    }

    @AfterSuite
    public void flushReport() {
        flushExtent();
    }
}
