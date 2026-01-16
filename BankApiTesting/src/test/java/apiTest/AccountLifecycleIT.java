package apiTest;

import static io.restassured.RestAssured.given;

import java.math.BigDecimal;
import java.util.Map;

import org.testng.Assert;
import org.testng.annotations.Test;

import apibuilder.AccountPayloadFactory;
import apiconfig.APIEndpoints;
import apiconfig.RequestHeaders;
import apivalidation.APIValidation;
import baseTest.BaseTest;
import io.restassured.response.Response;
import utils.PropertyUtil;

public class AccountLifecycleIT extends BaseTest {

    private final RequestHeaders requestHeaders = new RequestHeaders();

    // ================= CREATE ACCOUNT =================
    @Test(priority = 1)
    public void shouldCreateAccount() {

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

      
        String accountNo = response.jsonPath().getString("accountNo");
        Assert.assertNotNull(accountNo, "Account number should be generated");

        PropertyUtil.set("account.no", accountNo);
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
        accountNo = response.jsonPath().getString("accountNo");
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


        logger.info("Account created with accountNo: {}", accountNo);
    }
    
    // ================= GET ACCOUNT =================
    @Test(priority = 2, dependsOnMethods = "shouldCreateAccount")
    public void shouldGetAccountByAccountNo() {

        logger.info("===== GET ACCOUNT API STARTED =====");

        String accountNo = PropertyUtil.get("account.no");

        Response response =
                given()
                        .headers(requestHeaders.requestHeaders())
                        .pathParam("accountNo", accountNo)
                .when()
                        .get(APIEndpoints.apiEndpoints.GET_ACCOUNT_BY_ACCOUNT_NO)
                .then()
                        .extract()
                        .response();

        APIValidation.logResponse(response);
        APIValidation.validateStatuscode(response, 200);

        Assert.assertEquals(
                response.jsonPath().getString("accountNo"),
                accountNo,
                "Account number mismatch"
        );

        logger.info("Account retrieved successfully");
    }

    // ================= UPDATE ACCOUNT =================
    @Test(priority = 3, dependsOnMethods = "shouldGetAccountByAccountNo")
    public void shouldUpdateAccount() {

        logger.info("===== UPDATE ACCOUNT API STARTED =====");

        String accountNo = PropertyUtil.get("account.no");

        Map<String, Object> updatePayload =
                AccountPayloadFactory.createUpdateAccountPayload();

        Response response =
                given()
                        .headers(requestHeaders.requestHeaders())
                        .pathParam("accountNo", accountNo)
                        .body(updatePayload)
                .when()
                        .put(APIEndpoints.apiEndpoints.UPDATE_ACCOUNT_BY_ACCOUNT_NO)
                .then()
                        .extract()
                        .response();

        APIValidation.logResponse(response);
        APIValidation.validateStatuscode(response, 200);

        Assert.assertEquals(
                response.jsonPath().getString("accountType"),
                updatePayload.get("accountType"),
                "Account type not updated"
        );

        logger.info("Account updated successfully");
    }

    // ================= DELETE ACCOUNT =================
    @Test(priority = 4, dependsOnMethods = "shouldUpdateAccount")
    public void shouldDeleteAccount() {

        logger.info("===== DELETE ACCOUNT API STARTED =====");

        String accountNo = PropertyUtil.get("account.no");

        Response response =
                given()
                        .headers(requestHeaders.requestHeaders())
                        .pathParam("accountNo", accountNo)
                .when()
                        .delete(APIEndpoints.apiEndpoints.DELETE_ACCOUNT_BY_ACCOUNT_NO)
                .then()
                        .extract()
                        .response();

        APIValidation.validateStatuscode(response, 204);

        logger.info("Account deleted successfully");
    }
}
