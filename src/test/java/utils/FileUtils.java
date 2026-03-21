package utils;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class FileUtils {

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

    public static void writeResponseToFile(String fileName, String content) {
        try {
            Path responsesDir = Paths.get("src/test/resources/responses");
            Files.createDirectories(responsesDir);
            Path file = responsesDir.resolve(fileName);
            Files.writeString(file, content, StandardCharsets.UTF_8);
        } catch (IOException e) {
            throw new RuntimeException("Failed to write response to file: " + fileName, e);
        }
    }

    public static boolean responseFileExists(String fileName) {
        Path file = Paths.get("src/test/resources/responses").resolve(fileName);
        return Files.exists(file);
    }
}
