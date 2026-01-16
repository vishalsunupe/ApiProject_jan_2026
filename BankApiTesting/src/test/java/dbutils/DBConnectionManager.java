package dbutils;



import java.sql.Connection;
import java.sql.DriverManager;
import java.util.Properties;

public class DBConnectionManager {

    private static Connection connection;

    private DBConnectionManager() {}

    public static Connection getConnection(Properties dbProps) {
        try {
            if (connection == null || connection.isClosed()) {
                Class.forName(dbProps.getProperty("db.driver"));
                connection = DriverManager.getConnection(
                        dbProps.getProperty("db.url"),
                        dbProps.getProperty("db.username"),
                        dbProps.getProperty("db.password")
                );
            }
            return connection;
        } catch (Exception e) {
            throw new RuntimeException("DB Connection failed", e);
        }
    }
}
