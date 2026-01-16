package utils;

import java.util.Random;
import java.util.UUID;

public class RandomDataUtil {

    private static final Random RANDOM = new Random();

    // ================= GENERIC =================

    public static String uniqueReference() {
        return UUID.randomUUID().toString().replace("-", "");
    }

    public static String randomEmail() {
        return "user_" + uniqueReference().substring(0, 6) + "@gmail.com";
    }

    public static String randomPassword() {
        return "Pwd@" + uniqueReference().substring(0, 5);
    }

    public static String randomGender() {
        return RANDOM.nextBoolean() ? "male" : "female";
    }

    public static String randomDOB() {
        int year = 1985 + RANDOM.nextInt(20);
        int month = 1 + RANDOM.nextInt(12);
        int day = 1 + RANDOM.nextInt(28);
        return String.format("%04d-%02d-%02d", year, month, day);
    }

    // ================= INDIAN CUSTOMER =================

    public static String randomIndianName() {
        String[] firstNames = {
                "Amit", "Rahul", "Vikas", "Suresh",
                "Priya", "Neha", "Anjali", "Kavita"
        };
        String[] lastNames = {
                "Sharma", "Patel", "Verma",
                "Gupta", "Singh", "Iyer"
        };

        return firstNames[RANDOM.nextInt(firstNames.length)] + " " +
               lastNames[RANDOM.nextInt(lastNames.length)];
    }

    public static String randomIndianAddress() {
        return "Flat " + (RANDOM.nextInt(200) + 1)
                + ", " + uniqueReference().substring(0, 6)
                + " Nagar";
    }

    public static String randomIndianCity() {
        String[] cities = {
                "Pune", "Mumbai", "Bangalore",
                "Hyderabad", "Chennai", "Delhi"
        };
        return cities[RANDOM.nextInt(cities.length)];
    }

    public static String randomIndianState() {
        String[] states = {
                "Maharashtra", "Karnataka",
                "Telangana", "Tamil Nadu", "Delhi"
        };
        return states[RANDOM.nextInt(states.length)];
    }

    public static String randomIndianPin() {
        return String.valueOf(100000 + RANDOM.nextInt(900000));
    }

    public static String randomIndianMobile() {
        return "9" + (long) (RANDOM.nextDouble() * 1_000_000_000L);
    }

    // ================= ACCOUNT =================

    public static int randomCustomerId() {
        return RANDOM.nextInt(1000) + 1;
    }

    public static String randomAccountType() {
        return RANDOM.nextBoolean() ? "SAVINGS" : "CURRENT";
    }

    public static double randomInitialDeposit() {
        return Math.round((1000 + RANDOM.nextDouble() * 9000) * 100.0) / 100.0;
    }
}
