package dbutils;


import java.io.InputStream;
import java.util.Properties;

public final class DBConfig {

    private static final Properties DB_PROPERTIES = new Properties();

    static {
        try (InputStream input =
        DBConfig.class
                .getClassLoader()
                .getResourceAsStream("config/db.properties")) {

            if (input == null) {
                throw new RuntimeException("properties file not found in classpath");
            }

            DB_PROPERTIES.load(input);

        } catch (Exception e) {
            throw new RuntimeException("Failed to load database configuration", e);
        }
    }

    private DBConfig() {
        // Prevent instantiation
    }

    public static Properties getDBProperties() {
        return DB_PROPERTIES;
    }
}
