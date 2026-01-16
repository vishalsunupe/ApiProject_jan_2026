package dbutils;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.Date;
import java.sql.Timestamp;

import org.testng.Assert;

public class DBValidationUtil {

    // ================= CORE =================
    public static String getSingleValue(Connection connection, String query, String columnName) {
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            if (!rs.next()) {
                Assert.fail("No record found for query: " + query);
            }
            return rs.getString(columnName);

        } catch (Exception e) {
            throw new RuntimeException("DB query failed", e);
        }
    }

    public static void validateEquals(Connection connection, String query,
                                      String columnName, String expectedValue) {
        String actualValue = getSingleValue(connection, query, columnName);
        Assert.assertEquals(actualValue, expectedValue,
                "DB value mismatch for column: " + columnName);
    }

    // ================= STRING =================
    public static void validateStringEquals(Connection connection, String query,
                                            String columnName, String expected) {
        String actual = getSingleValue(connection, query, columnName);
        Assert.assertEquals(actual, expected,
                "String mismatch for column: " + columnName);
    }

    public static void validateStringEqualsIgnoreCase(Connection connection, String query,
                                                      String columnName, String expected) {
        String actual = getSingleValue(connection, query, columnName);
        Assert.assertTrue(actual.equalsIgnoreCase(expected),
                "String (ignore case) mismatch for column: " + columnName);
    }

    // ================= LONG =================
    public static void validateLongEquals(Connection connection, String query,
                                          String columnName, long expected) {
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            if (!rs.next()) {
                Assert.fail("No record found for query: " + query);
            }

            long actual = rs.getLong(columnName);
            Assert.assertEquals(actual, expected,
                    "Long value mismatch for column: " + columnName);

        } catch (Exception e) {
            throw new RuntimeException("DB query failed", e);
        }
    }

    // ================= DECIMAL =================
    public static void validateDecimalEquals(Connection connection, String query,
                                             String columnName, BigDecimal expected) {
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            if (!rs.next()) {
                Assert.fail("No record found for query: " + query);
            }

            BigDecimal actual = rs.getBigDecimal(columnName);

            Assert.assertTrue(actual.compareTo(expected) == 0,
                    "Decimal mismatch for column: " + columnName +
                            " expected [" + expected + "] but found [" + actual + "]");

        } catch (Exception e) {
            throw new RuntimeException("DB query failed", e);
        }
    }

    // ================= DATE =================
    public static void validateDateEquals(Connection connection, String query,
                                          String columnName, Date expected) {
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            if (!rs.next()) {
                Assert.fail("No record found for query: " + query);
            }

            Date actual = rs.getDate(columnName);
            Assert.assertEquals(actual, expected,
                    "Date mismatch for column: " + columnName);

        } catch (Exception e) {
            throw new RuntimeException("DB query failed", e);
        }
    }

    // ================= NOT NULL =================
    public static void validateNotNull(Connection connection, String query,
                                       String columnName) {
        String value = getSingleValue(connection, query, columnName);
        Assert.assertNotNull(value,
                "Column should not be NULL: " + columnName);
    }

    public static void validateTimestampNotNull(Connection connection, String query,
                                                String columnName) {
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            if (!rs.next()) {
                Assert.fail("No record found for query: " + query);
            }

            Timestamp actual = rs.getTimestamp(columnName);
            Assert.assertNotNull(actual,
                    "Timestamp should not be NULL for column: " + columnName);

        } catch (Exception e) {
            throw new RuntimeException("DB query failed", e);
        }
    }
}
