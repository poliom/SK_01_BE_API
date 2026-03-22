package utils;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class FileUtils {

    /** Reads a file from the test classpath (src/test/resources). */
    public static String readFileAsString(String filePath) {
        try (InputStream is = FileUtils.class.getClassLoader().getResourceAsStream(filePath)) {
            if (is == null) {
                throw new RuntimeException("File not found in resources: " + filePath);
            }
            return new String(is.readAllBytes(), StandardCharsets.UTF_8);
        } catch (IOException e) {
            throw new RuntimeException("Failed to read file: " + filePath, e);
        }
    }

    /**
     * Saves a response body to the run's responses directory.
     *
     * @param fileName file name including extension, e.g. "New_user_registers_143022_001.json"
     * @param content  raw response body
     */
    public static void writeResponseToFile(String fileName, String content) {
        try {
            Path dir = Paths.get(RunContext.responsesDir());
            Files.createDirectories(dir);
            Files.writeString(dir.resolve(fileName), content, StandardCharsets.UTF_8);
        } catch (IOException e) {
            throw new RuntimeException("Failed to write response file: " + fileName, e);
        }
    }

    /** Returns true if a response file with the given name exists in the run's responses directory. */
    public static boolean responseFileExists(String fileName) {
        return Files.exists(Paths.get(RunContext.responsesDir()).resolve(fileName));
    }
}
