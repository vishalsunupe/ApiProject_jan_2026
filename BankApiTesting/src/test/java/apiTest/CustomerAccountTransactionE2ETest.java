package apiTest;

import static io.restassured.RestAssured.given;
import java.math.BigDecimal;
import java.util.Map;
import org.testng.Assert;
import org.testng.annotations.Test;
import apibuilder.AccountPayloadFactory;
import apibuilder.CustomerPayloadFactory;
import apibuilder.TransactionPayloadFactory;
import apiconfig.APIEndpoints;
import apiconfig.RequestHeaders;
import apivalidation.APIValidation;
import baseTest.BaseTest;
import io.restassured.response.Response;
import utils.PropertyUtil;

@Test(invocationCount = 20)
public class CustomerAccountTransactionE2ETest extends BaseTest {

    private final RequestHeaders requestHeaders = new RequestHeaders();

    // ================= CREATE CUSTOMER =================
    @Test(priority = 1)
    public void createCustomer_shouldSucceed() {

        logger.info("===== CREATE CUSTOMER API TEST STARTED =====");

        Map<String, Object> payload =
                CustomerPayloadFactory.createCustomerPayload();

        Response response =
                given()
                        .headers(requestHeaders.requestHeaders())
                        .body(payload)
                .when()
                        .post(APIEndpoints.apiEndpoints.CREATE_CUSTOMER)
                .then()
                        .extract()
                        .response();

        APIValidation.logResponse(response);
        APIValidation.validateStatuscode(response, 201);
        APIValidation.validateResponseBodyNotNull(response);

        APIValidation.validateFieldExists(response, "customerId");
        APIValidation.validateFieldExists(response, "customerName");
        APIValidation.validateFieldExists(response, "email");

        APIValidation.validateFieldFromPayload(response, "customerName", payload, "customerName");
        APIValidation.validateFieldFromPayload(response, "gender", payload, "gender");
        APIValidation.validateFieldFromPayload(response, "email", payload, "email");

        APIValidation.validateNumericField(response, "customerId");

        int customerId = response.jsonPath().getInt("customerId");
        Assert.assertTrue(customerId > 0, "Customer ID should be generated");

        PropertyUtil.set("customer.id", String.valueOf(customerId));
        logger.info("Customer ID stored for chaining: {}", customerId);

        logger.info("===== CREATE CUSTOMER API TEST PASSED =====");
    }

    // ================= CREATE ACCOUNT =================
    @Test(priority = 2)
    public void createAccount_shouldSucceed() {

        logger.info("===== CREATE ACCOUNT API STARTED =====");

        Map<String, Object> payload =
                AccountPayloadFactory.createValidAccountPayload();

        Response response =
                given()
                        .headers(requestHeaders.requestHeaders())
                        .body(payload)
                .when()
                        .post(APIEndpoints.apiEndpoints.CREATE_ACCOUNT)
                .then()
                        .extract()
                        .response();

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

        int accountId = response.jsonPath().getInt("accountId");
        String accountNo = response.jsonPath().getString("accountNo");

        BigDecimal openingBalance =
                new BigDecimal(response.jsonPath().get("balance").toString());

        Assert.assertTrue(accountId > 0, "Account ID should be generated");
        Assert.assertNotNull(accountNo, "Account number should not be null");
        Assert.assertTrue(
                openingBalance.compareTo(BigDecimal.ZERO) >= 0,
                "Balance should be non-negative"
        );

        PropertyUtil.set("accountId.id", String.valueOf(accountId));
        PropertyUtil.set("account.no", accountNo);
        PropertyUtil.set("account.opening.balance", openingBalance.toString());

        logger.info("Account created with accountNo: {}", accountNo);
    }

    // ================= DEPOSIT MONEY =================
    @Test(priority = 3)
    public void depositMoney_shouldUpdateBalanceCorrectly() {

        Map<String, Object> payload =
                TransactionPayloadFactory.depositPayload();

        String expectedAccountNo =
                payload.get("accountNo").toString();

        String expectedTransactionType =
                payload.get("transactionType").toString();

        String expectedDescription =
                payload.get("description").toString();

        double depositAmount =
                ((Number) payload.get("amount")).doubleValue();

        BigDecimal openingBalance =
                new BigDecimal(PropertyUtil.get("account.opening.balance"));

        Response response =
                given()
                        .headers(requestHeaders.requestHeaders())
                        .body(payload)
                .when()
                        .post(APIEndpoints.apiEndpoints.DEPOSIT_MONEY)
                .then()
                        .extract()
                        .response();

        APIValidation.logResponse(response);

        APIValidation.validateStatuscode(response, 201);
        APIValidation.validateContentType(response, "application/json");
        APIValidation.validateResponseBodyNotNull(response);
        APIValidation.validateResponseTime(response, 2000);

        APIValidation.validateFieldExists(response, "transactionId");
        APIValidation.validateFieldExists(response, "transactionDate");
        APIValidation.validateFieldExists(response, "balanceAfterTransaction");

        APIValidation.validateFieldValue(response, "accountNo", expectedAccountNo);
        APIValidation.validateFieldValue(response, "transactionType", expectedTransactionType);
        APIValidation.validateFieldValue(response, "description", expectedDescription);
        APIValidation.validateDoubleValue(response, "amount", depositAmount);

        BigDecimal expectedBalanceAfterTransaction =
                openingBalance.add(BigDecimal.valueOf(depositAmount));

        BigDecimal actualBalanceAfterTransaction =
                new BigDecimal(
                        response.jsonPath().get("balanceAfterTransaction").toString()
                );

        Assert.assertEquals(
                actualBalanceAfterTransaction,
                expectedBalanceAfterTransaction,
                "Balance after deposit should be opening balance + deposit amount"
        );

        APIValidation.validateFieldType(response, "transactionId", Integer.class);
        APIValidation.validateFieldType(response, "accountNo", String.class);
        APIValidation.validateFieldType(response, "transactionType", String.class);

        APIValidation.validateNumericField(response, "amount");
        APIValidation.validateNumericField(response, "balanceAfterTransaction");

        APIValidation.validateHeader(response, "Content-Type", "application/json");
        APIValidation.validateContains(response, "DEPOSIT");
    }
}
