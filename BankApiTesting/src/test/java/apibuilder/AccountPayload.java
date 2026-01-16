package apibuilder;

import java.util.HashMap;
import java.util.Map;

public class AccountPayload {

    public Map<String, Object> createNewAccountPayload() {

        Map<String, Object> payload = new HashMap<>();

        payload.put("customerId", 127);
        payload.put("accountType", "SAVINGS");
        payload.put("initialDeposit", 20000.0);

        return payload;
    }
}
