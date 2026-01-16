package apibuilder;
import java.util.HashMap;
import java.util.Map;

import utils.PropertyUtil;
import utils.RandomDataUtil;

public class TransactionPayloadFactory {

    // ================= DEPOSIT =================
    public static Map<String, Object> depositPayload() {

        Map<String, Object> payload = new HashMap<>();

        payload.put("accountNo",PropertyUtil.get("account.no")); // FROM PROPERTIES
        payload.put("transactionType", "DEPOSIT");
        payload.put("amount", RandomDataUtil.randomInitialDeposit());
        payload.put("description",
                "Deposit Ref " + RandomDataUtil.uniqueReference().substring(0, 8));

        return payload;
    }

    // ================= WITHDRAW =================
    public static Map<String, Object> withdrawPayload() {

        Map<String, Object> payload = new HashMap<>();

        payload.put("accountNo",PropertyUtil.get("account.no")); // FROM PROPERTIES
        payload.put("transactionType", "WITHDRAW");
        payload.put("amount", 500.0);
        payload.put("description","Withdraw Ref " + RandomDataUtil.uniqueReference().substring(0, 8));

        return payload;
    }
    
   /* public static Map<String, Object> transferPayload(
            String fromAccount, String toAccount) {

        Map<String, Object> payload = new HashMap<>();

        payload.put("fromAccount", fromAccount);
        payload.put("toAccount", toAccount);
        payload.put("transactionType", "TRANSFER");
        payload.put("amount", 1000.0);
        payload.put("description",
                "Transfer Ref " + RandomDataUtil.uniqueReference().substring(0, 8));

        return payload;
}*/
}






