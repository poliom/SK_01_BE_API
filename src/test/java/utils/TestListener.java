package utils;

import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;
import io.cucumber.testng.PickleWrapper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;

public class TestListener implements ITestListener {

    private static final Logger failureLog = LogManager.getLogger("failures");

    private String resolveTestName(ITestResult result) {
        Object[] params = result.getParameters();
        if (params != null && params.length >= 2
                && params[0] instanceof PickleWrapper) {

            String scenarioName = ((PickleWrapper) params[0]).getPickle().getName();
            String featureName  = params[1].toString();
            return featureName + " — " + scenarioName;
        }
        return result.getTestClass().getName() + " — " + result.getMethod().getMethodName();
    }

    @Override
    public void onTestStart(ITestResult result) {
        String testName = resolveTestName(result);
        ExtentTest test = BaseTest.createTest(testName);
        TestContext.setTest(test);
        test.log(Status.INFO, "Test started: " + testName);
    }

    @Override
    public void onTestSuccess(ITestResult result) {
        ExtentTest test = TestContext.getTest();
        if (test != null) {
            test.pass("Test passed: " + resolveTestName(result));
        }
        TestContext.remove();
    }

    @Override
    public void onTestFailure(ITestResult result) {
        String testName = resolveTestName(result);
        Throwable cause = result.getThrowable();
        String errorMessage = cause != null ? cause.getMessage() : "Unknown failure";

        ExtentTest test = TestContext.getTest();
        if (test != null) {
            test.fail("Test failed: " + testName + " — " + errorMessage);
        }
        failureLog.error("FAIL [{}] {}", testName, errorMessage);
        TestContext.remove();
    }

    @Override
    public void onTestSkipped(ITestResult result) {
        ExtentTest test = TestContext.getTest();
        if (test != null) {
            test.skip("Test skipped: " + resolveTestName(result));
        }
        TestContext.remove();
    }

    @Override
    public void onStart(ITestContext context) {
        BaseTest.initExtent();
    }

    @Override
    public void onFinish(ITestContext context) {
        BaseTest.flushExtent();
    }
}
