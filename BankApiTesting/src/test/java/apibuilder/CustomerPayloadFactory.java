package apibuilder;

import java.util.HashMap;
import java.util.Map;

import utils.RandomDataUtil;

public class CustomerPayloadFactory {

    // ================= CREATE CUSTOMER =================
    public static Map<String, Object> createCustomerPayload() {

        Map<String, Object> payload = new HashMap<>();

        payload.put("customerName", RandomDataUtil.randomIndianName());
        payload.put("gender", RandomDataUtil.randomGender());
        payload.put("dateOfBirth", RandomDataUtil.randomDOB());
        payload.put("address", RandomDataUtil.randomIndianAddress());
        payload.put("city", RandomDataUtil.randomIndianCity());
        payload.put("state", RandomDataUtil.randomIndianState());
        payload.put("pin", RandomDataUtil.randomIndianPin());
        payload.put("mobileNumber", RandomDataUtil.randomIndianMobile());
        payload.put("email", RandomDataUtil.randomEmail());
        payload.put("password", RandomDataUtil.randomPassword());

        return payload;
    }

    // ================= UPDATE CUSTOMER =================
    public static Map<String, Object> updateCustomerPayload() {

        Map<String, Object> payload = new HashMap<>();

        // Usually DOB & email are immutable → not updating
        payload.put("customerName", RandomDataUtil.randomIndianName());
        payload.put("gender", RandomDataUtil.randomGender());
        payload.put("dateOfBirth", RandomDataUtil.randomDOB());
        payload.put("address", RandomDataUtil.randomIndianAddress());
        payload.put("city", RandomDataUtil.randomIndianCity());
        payload.put("state", RandomDataUtil.randomIndianState());
        payload.put("pin", RandomDataUtil.randomIndianPin());
        payload.put("mobileNumber", RandomDataUtil.randomIndianMobile());
        payload.put("email", RandomDataUtil.randomEmail());
        payload.put("password", RandomDataUtil.randomPassword());

        return payload;
    }
}
