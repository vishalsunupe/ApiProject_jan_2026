package apiTest;

import static io.restassured.RestAssured.given;

import java.io.IOException;
import java.util.Map;

import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import apibuilder.AccountPayload;
import apiconfig.APIEndpoints;
import apiconfig.RequestHeaders;
import apivalidation.APIValidation;
import baseTest.BaseTest;
import io.restassured.response.Response;
import utils.JsonDataUtil;

public class AccountApiTest extends BaseTest {

    AccountPayload reqPayLoad = new AccountPayload();
    RequestHeaders reqHeader = new RequestHeaders();

    @Test(dataProvider = "accountJsonData")
    public void testCreateNewAccount(Map<String, Object> payload) {

        logger.info("===== CREATE ACCOUNT API TEST STARTED =====");
        logger.info("Request Payload: {}", payload);

        Response response =
                given()
                        .headers(reqHeader.requestHeaders())
                        .body(payload)
                .when()
                        .post(APIEndpoints.apiEndpoints.CREATE_ACCOUNT)
                .then()
                        .extract()
                        .response();

        logger.info("Response received successfully");
        APIValidation.logResponse(response);

        logger.info("Validating status code");
        APIValidation.validateStatuscode(response, 201);

        logger.info("Validating response fields");

        Assert.assertNotNull(
                response.jsonPath().get("accountId"),
                "Account ID should not be null"
        );

        Assert.assertEquals(
                response.jsonPath().getInt("customerId"),
                payload.get("customerId"),
                "Customer ID mismatch"
        );

        Assert.assertEquals(
                response.jsonPath().getString("accountType"),
                payload.get("accountType"),
                "Account type mismatch"
        );

        Assert.assertTrue(
                response.jsonPath().getDouble("balance")
                        >= (Double) payload.get("initialDeposit"),
                "Balance should be >= initial deposit"
        );

        logger.info("===== CREATE ACCOUNT API TEST PASSED =====");
    }

    @DataProvider(name = "accountJsonData")
    public Object[][] getCustomerData() throws IOException {

        logger.info("Loading account creation test data from JSON file");

        String jsonPath = "src/test/resources/TestData/accountCreationData.json";

        return JsonDataUtil.getTestData(jsonPath);
       
    }

    //@Test
    public void getAccountById_validId() {

        int accountNo = 10;

        logger.info("===== GET ACCOUNT BY ID TEST STARTED =====");
        logger.info("Account Number: {}", accountNo);

        Response response =
                given()
                        .headers(reqHeader.requestHeaders())
                        .pathParam("accountNo", accountNo)
                .when()
                        .get(APIEndpoints.apiEndpoints.GET_ACCOUNT_BY_ACCOUNT_NO)
                .then()
                        .extract()
                        .response();

        APIValidation.logResponse(response);

        logger.info("Validating status code");
        APIValidation.validateStatuscode(response, 200);

        logger.info("Validating response fields");

        Assert.assertTrue(
                response.jsonPath().getInt("customerId") > 0,
                "Customer ID should be positive"
        );

        Assert.assertTrue(
                response.jsonPath().getDouble("balance") >= 0,
                "Balance should be zero or positive"
        );

        Assert.assertTrue(
                response.jsonPath().getString("accountType")
                        .matches("SAVINGS|CURRENT"),
                "Account type should be SAVINGS or CURRENT"
        );

        Assert.assertEquals(
                response.getHeader("Content-Type"),
                "application/json",
                "Invalid content type"
        );

        Assert.assertEquals(
                response.jsonPath().getInt("accountId"),
                accountNo
        );

        Assert.assertTrue(response.time() < 3000);

        logger.info("===== GET ACCOUNT BY ID TEST PASSED =====");
    }
}
