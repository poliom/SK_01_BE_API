package utils;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeSuite;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

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

        String reportPath = RunContext.reportsDir() + "/ExtentReport.html";
        try {
            Files.createDirectories(Paths.get(RunContext.reportsDir()));
        } catch (IOException e) {
            throw new RuntimeException("Cannot create reports directory: " + RunContext.reportsDir(), e);
        }

        ExtentSparkReporter spark = new ExtentSparkReporter(reportPath);
        spark.config().setReportName("API Test Report");
        spark.config().setDocumentTitle("SK_01 Test Results");

        extent = new ExtentReports();
        extent.attachReporter(spark);
        extent.setSystemInfo("Environment", "QA");
        extent.setSystemInfo("Run ID", RunContext.RUN_DIR);
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
