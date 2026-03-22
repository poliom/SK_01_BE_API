package utils;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Single source of truth for the current test-run artifact directory.
 * When launched via Maven, the run.dir system property is set by Surefire
 * (see pom.xml argLine). When running directly from an IDE the class falls
 * back to a timestamp generated at load time.
 */
public final class RunContext {

    private static final DateTimeFormatter FMT =
            DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss");

    /** Root directory for all artifacts produced by this run. */
    public static final String RUN_DIR = System.getProperty(
            "run.dir",
            "src/test/resources/test-artifacts/run_" + LocalDateTime.now().format(FMT));

    public static String logsDir()      { return RUN_DIR + "/logs";      }
    public static String reportsDir()   { return RUN_DIR + "/reports";   }
    public static String responsesDir() { return RUN_DIR + "/responses"; }

    private RunContext() {}
}
