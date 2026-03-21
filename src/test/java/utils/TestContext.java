package utils;

import com.aventstack.extentreports.ExtentTest;

public class TestContext {

    private static final ThreadLocal<ExtentTest> currentTest = new ThreadLocal<>();

    public static void setTest(ExtentTest test) {
        currentTest.set(test);
    }

    public static ExtentTest getTest() {
        return currentTest.get();
    }

    public static void remove() {
        currentTest.remove();
    }
}
