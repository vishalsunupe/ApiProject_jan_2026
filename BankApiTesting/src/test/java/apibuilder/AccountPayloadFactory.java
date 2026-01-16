package apibuilder;

import java.util.HashMap;
import java.util.Map;

import utils.PropertyUtil;
import utils.RandomDataUtil;

public class AccountPayloadFactory {

    // ================= CREATE ACCOUNT PAYLOAD =================
    public static Map<String, Object> createValidAccountPayload() {

        Map<String, Object> payload = new HashMap<>();

        // customerId ONLY from properties
        payload.put(
                "customerId",
                (PropertyUtil.get("customer.id"))         //Integer.parseInt
        );

        // Randomized data using utility
        payload.put("accountType", RandomDataUtil.randomAccountType());
        payload.put("initialDeposit", RandomDataUtil.randomInitialDeposit());

        return payload;
    }

    // ================= UPDATE ACCOUNT PAYLOAD =================
    public static Map<String, Object> createUpdateAccountPayload() {

        Map<String, Object> payload = new HashMap<>();

        payload.put("accountType", RandomDataUtil.randomAccountType());

        return payload;
    }
}
