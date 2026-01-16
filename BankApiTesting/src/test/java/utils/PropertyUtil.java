package utils;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

public class PropertyUtil {

    private static final String FILE_PATH =
            "src/test/resources/config/testdata.properties";

    private static Properties properties = new Properties();

    // Load properties once
    private static void loadProperties() {
        try (FileInputStream fis = new FileInputStream(FILE_PATH)) {
            properties.load(fis);
        } catch (IOException e) {
            throw new RuntimeException("Failed to load properties file", e);
        }
    }

    // Save properties to file
    private static void saveProperties() {
        try (FileOutputStream fos = new FileOutputStream(FILE_PATH)) {
            properties.store(fos, "API Chaining Test Data");
        } catch (IOException e) {
            throw new RuntimeException("Failed to save properties file", e);
        }
    }

    // ===== COMMON SET METHOD =====
    public static synchronized void set(String key, String value) {
        loadProperties();
        properties.setProperty(key, value);
        saveProperties();
    }

    // ===== COMMON GET METHOD =====
    public static synchronized String get(String key) {
        loadProperties();
        return properties.getProperty(key);
    }

    // ===== OPTIONAL CLEANUP =====
    public static synchronized void clear(String key) {
        loadProperties();
        properties.remove(key);
        saveProperties();
    }
}
