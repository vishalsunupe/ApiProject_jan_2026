package apiTest;

import static io.restassured.RestAssured.given;

import java.math.BigDecimal;
import java.sql.Connection;
import java.util.Map;

import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import apibuilder.AccountPayload;
import apiconfig.APIEndpoints;
import apiconfig.RequestHeaders;
import apivalidation.APIValidation;
import baseTest.BaseTest;
import dbutils.DBConfig;
import dbutils.DBConnectionManager;
import dbutils.DBValidationUtil;
import io.restassured.response.Response;
import utils.PropertyUtil;

public class DatabaseValidation_AccountApiIT extends BaseTest {

    private final AccountPayload accountPayload = new AccountPayload();
    private final RequestHeaders requestHeaders = new RequestHeaders();

    private Connection dbConnection;

    // ================= DB SETUP =================
    @BeforeClass
    public void setupDB() {
        dbConnection =
                DBConnectionManager.getConnection(DBConfig.getDBProperties());
        logger.info("Database connection established");
    }

    @AfterClass
    public void tearDownDB() {
        // DBConnectionManager.closeConnection(dbConnection);
        logger.info("Database connection closed");
    }

    // ================= CREATE ACCOUNT =================
    @Test
    public void shouldCreateAccount() {

        // ---------- API PAYLOAD ----------
        Map<String, Object> payload = accountPayload.createNewAccountPayload();

        // ---------- API CALL ----------
        Response response =
                given()
                        .headers(requestHeaders.requestHeaders())
                        .body(payload)
                .when()
                        .post(APIEndpoints.apiEndpoints.CREATE_ACCOUNT)
                .then()
                        .extract()
                        .response();

        // ---------- RESPONSE VALIDATION ----------
        APIValidation.logResponse(response);
        APIValidation.validateStatuscode(response, 201);
        APIValidation.validateResponseBodyNotNull(response);

        APIValidation.validateFieldExists(response, "accountId");
        APIValidation.validateFieldExists(response, "accountNo");
        APIValidation.validateFieldExists(response, "accountType");
        APIValidation.validateFieldExists(response, "status");
        APIValidation.validateFieldExists(response, "balance");

        APIValidation.validateNumericField(response, "accountId");
        APIValidation.validateFieldType(response, "accountNo", String.class);
        APIValidation.validateFieldType(response, "accountType", String.class);
        APIValidation.validateFieldType(response, "status", String.class);
        APIValidation.validateNumericField(response, "balance");

        APIValidation.validateFieldFromPayload(
                response,
                "accountType",
                payload,
                "accountType"
        );

        APIValidation.validateFieldValue(response, "status", "ACTIVE");

        // ---------- EXTRACT RESPONSE VALUES ----------
        int accountId = response.jsonPath().getInt("accountId");
        String accountNo = response.jsonPath().getString("accountNo");
        String accountType = response.jsonPath().getString("accountType");
        String status = response.jsonPath().getString("status");

        BigDecimal responseBalance =
                new BigDecimal(response.jsonPath().get("balance").toString());

        Assert.assertTrue(accountId > 0, "Account ID should be generated");
        Assert.assertNotNull(accountNo, "Account number should not be null");
        Assert.assertTrue(
                responseBalance.compareTo(BigDecimal.ZERO) >= 0,
                "Balance should be non-negative"
        );

        PropertyUtil.set("accountId.id", String.valueOf(accountId));

        // ---------- DB VALIDATION ----------
        String query =
                "SELECT customer_id, account_no, account_type, balance, status, created_date " +
                "FROM accounts WHERE account_id = " + accountId;

        DBValidationUtil.validateLongEquals(
                dbConnection,
                query,
                "customer_id",
                Long.parseLong(payload.get("customerId").toString())
        );

        DBValidationUtil.validateStringEquals(
                dbConnection,
                query,
                "account_no",
                accountNo
        );

        DBValidationUtil.validateStringEquals(
                dbConnection,
                query,
                "account_type",
                accountType
        );

        DBValidationUtil.validateDecimalEquals(
                dbConnection,
                query,
                "balance",
                responseBalance
        );

        DBValidationUtil.validateStringEquals(
                dbConnection,
                query,
                "status",
                status
        );

        DBValidationUtil.validateTimestampNotNull(
                dbConnection,
                query,
                "created_date"
        );
    }
}
