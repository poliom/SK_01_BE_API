package utils;

import com.aventstack.extentreports.ExtentTest;

public class TestContext {

    private static final ThreadLocal<ExtentTest> currentTest    = new ThreadLocal<>();
    private static final ThreadLocal<String>     scenarioLogName = new ThreadLocal<>();

    public static void setTest(ExtentTest test) { currentTest.set(test); }
    public static ExtentTest getTest()          { return currentTest.get(); }

    /** Sanitised scenario name + timestamp used for log and response file names. */
    public static void setScenarioLogName(String name) { scenarioLogName.set(name); }
    public static String getScenarioLogName()          { return scenarioLogName.get(); }

    public static void remove() {
        currentTest.remove();
        scenarioLogName.remove();
    }
}
