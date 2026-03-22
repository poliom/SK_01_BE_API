package utils;

import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;
import io.cucumber.testng.PickleWrapper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.ThreadContext;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class TestListener implements ITestListener {

    private static final Logger failureLog = LogManager.getLogger("failures");
    private static final DateTimeFormatter TIME_FMT = DateTimeFormatter.ofPattern("HHmmss");

    // -----------------------------------------------------------------------
    // Helpers
    // -----------------------------------------------------------------------

    /** Returns "Feature — Scenario name" for Cucumber, method name otherwise. */
    private String resolveDisplayName(ITestResult result) {
        Object[] params = result.getParameters();
        if (params != null && params.length >= 2 && params[0] instanceof PickleWrapper) {
            String scenario = ((PickleWrapper) params[0]).getPickle().getName();
            String feature  = params[1].toString();
            return feature + " — " + scenario;
        }
        return result.getTestClass().getName() + " — " + result.getMethod().getMethodName();
    }

    /** Returns only the scenario name (without feature prefix). */
    private String resolveScenarioName(ITestResult result) {
        Object[] params = result.getParameters();
        if (params != null && params.length >= 2 && params[0] instanceof PickleWrapper) {
            return ((PickleWrapper) params[0]).getPickle().getName();
        }
        return result.getMethod().getMethodName();
    }

    /**
     * Converts a scenario name into a safe filename token:
     * spaces/slashes → underscores, strips everything else non-alphanumeric,
     * collapses repeated underscores, caps at 60 characters.
     */
    private static String sanitise(String name) {
        return name.replaceAll("[^a-zA-Z0-9]+", "_")
                   .replaceAll("_+", "_")
                   .replaceAll("^_|_$", "")
                   .substring(0, Math.min(name.length(), 60));
    }

    // -----------------------------------------------------------------------
    // ITestListener callbacks
    // -----------------------------------------------------------------------

    @Override
    public void onTestStart(ITestResult result) {
        String displayName  = resolveDisplayName(result);
        String scenarioName = resolveScenarioName(result);

        // Build a file-safe identifier: sanitised name + HHmmss timestamp
        String timestamp    = LocalTime.now().format(TIME_FMT);
        String logFileName  = sanitise(scenarioName) + "_" + timestamp;

        // Store in ThreadContext so log4j2 RoutingAppender writes to the right file
        ThreadContext.put("testLogFile", logFileName);

        // Store for use by RestUtils (response file naming)
        TestContext.setScenarioLogName(logFileName);

        ExtentTest test = BaseTest.createTest(displayName);
        TestContext.setTest(test);
        test.log(Status.INFO, "Test started: " + displayName);
    }

    @Override
    public void onTestSuccess(ITestResult result) {
        ExtentTest test = TestContext.getTest();
        if (test != null) {
            test.pass("Test passed: " + resolveDisplayName(result));
        }
        clearContext();
    }

    @Override
    public void onTestFailure(ITestResult result) {
        String displayName   = resolveDisplayName(result);
        Throwable cause      = result.getThrowable();
        String errorMessage  = cause != null ? cause.getMessage() : "Unknown failure";

        ExtentTest test = TestContext.getTest();
        if (test != null) {
            test.fail("Test failed: " + displayName + " — " + errorMessage);
        }
        failureLog.error("FAIL [{}] {}", displayName, errorMessage);
        clearContext();
    }

    @Override
    public void onTestSkipped(ITestResult result) {
        ExtentTest test = TestContext.getTest();
        if (test != null) {
            test.skip("Test skipped: " + resolveDisplayName(result));
        }
        clearContext();
    }

    @Override
    public void onStart(ITestContext context) {
        BaseTest.initExtent();
    }

    @Override
    public void onFinish(ITestContext context) {
        BaseTest.flushExtent();
    }

    // -----------------------------------------------------------------------
    // Private
    // -----------------------------------------------------------------------

    private void clearContext() {
        ThreadContext.remove("testLogFile");
        TestContext.remove();
    }
}
