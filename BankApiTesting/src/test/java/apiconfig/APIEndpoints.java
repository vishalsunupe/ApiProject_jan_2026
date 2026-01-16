package apiconfig;

public class APIEndpoints {

    public static class apiEndpoints {

        /* ================= AUTH ================= */

        public static final String LOGIN          = "/api/auth/login";
        public static final String REGISTER       = "/api/auth/register";


        /* ================= CUSTOMERS ================= */

        // Create customer
        public static final String CREATE_CUSTOMER = "/api/customers";

        // Get all customers
        public static final String GET_ALL_CUSTOMERS = "/api/customers";

        // Get / Update / Delete single customer
        public static final String GET_CUSTOMER_BY_ID    = "/api/customers/{customerId}";
        public static final String UPDATE_CUSTOMER_BY_ID = "/api/customers/{customerId}";
        public static final String DELETE_CUSTOMER_BY_ID = "/api/customers/{customerId}";


        /* ================= ACCOUNTS ================= */

        // Create account
        public static final String CREATE_ACCOUNT = "/api/accounts";
        public static final String GET_ACCOUNT_BY_ACCOUNT_NO = "/api/accounts/{accountNo}";
        public static final String UPDATE_ACCOUNT_BY_ACCOUNT_NO = "/api/accounts/{accountNo}";
        public static final String DELETE_ACCOUNT_BY_ACCOUNT_NO = "/api/accounts/{accountNo}";


        /* ================= TRANSACTIONS ================= */

        // Deposit money
        public static final String DEPOSIT_MONEY = "/api/accounts/deposit";

        // Withdraw money
        public static final String WITHDRAW_MONEY = "/api/accounts/withdraw";

        // Transfer money
        public static final String TRANSFER_MONEY = "/api/accounts/transfer";

        // Get transactions for an account
        public static final String GET_TRANSACTIONS_BY_ACCOUNT_NO =
                "/api/accounts/{accountNo}/transactions";
    }
}
