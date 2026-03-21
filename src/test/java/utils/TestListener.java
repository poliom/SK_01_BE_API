package utils;

import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;

public class TestListener implements ITestListener {

    private static final Logger failureLog = LogManager.getLogger("failures");

    @Override
    public void onTestStart(ITestResult result) {
        String testName = result.getTestClass().getName() + " — " + result.getMethod().getMethodName();
        ExtentTest test = BaseTest.createTest(testName);
        TestContext.setTest(test);
        test.log(Status.INFO, "Test started: " + testName);
    }

    @Override
    public void onTestSuccess(ITestResult result) {
        ExtentTest test = TestContext.getTest();
        if (test != null) {
            test.pass("Test passed: " + result.getMethod().getMethodName());
        }
        TestContext.remove();
    }

    @Override
    public void onTestFailure(ITestResult result) {
        String methodName = result.getMethod().getMethodName();
        Throwable cause = result.getThrowable();
        String errorMessage = cause != null ? cause.getMessage() : "Unknown failure";

        ExtentTest test = TestContext.getTest();
        if (test != null) {
            test.fail("Test failed: " + methodName + " — " + errorMessage);
        }
        failureLog.error("FAIL [{}] {}", methodName, errorMessage);
        TestContext.remove();
    }

    @Override
    public void onTestSkipped(ITestResult result) {
        ExtentTest test = TestContext.getTest();
        if (test != null) {
            test.skip("Test skipped: " + result.getMethod().getMethodName());
        }
        TestContext.remove();
    }

    @Override
    public void onStart(ITestContext context) {}

    @Override
    public void onFinish(ITestContext context) {}
}
