package apiTest;

import static io.restassured.RestAssured.given;

import java.io.IOException;
import java.util.Map;

import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import apiconfig.APIEndpoints;
import apiconfig.RequestHeaders;
import apivalidation.APIValidation;
import baseTest.BaseTest;
import io.restassured.response.Response;
import utils.JsonDataUtil;

public class DepositMoneyApiTest extends BaseTest {
    RequestHeaders reqHeader = new RequestHeaders();

   @Test(dataProvider = "depositTransactionData")
    public void testDepositMoney_Datadriven(Map<String, Object> payload) {

        Response response = given()
                .headers(reqHeader.requestHeaders())
                .body(payload)
                .when()
                .post(APIEndpoints.apiEndpoints.DEPOSIT_MONEY)
                .then()
                .extract()
                .response();

        System.out.println(response.asPrettyString());

        // Status Code Validation
        APIValidation.validateStatuscode(response, 201);

        // Assertions

        Assert.assertNotNull(
                response.jsonPath().getInt("transactionId"),
                "Transaction ID should not be null"
        );

        Assert.assertEquals(
                response.jsonPath().getString("accountNo"),
                payload.get("accountNo"),
                "Account number mismatch"
        );

        Assert.assertEquals(
                response.jsonPath().getString("transactionType"),
                payload.get("transactionType"),
                "Transaction type mismatch"
        );

        Double expectedAmount = ((Number) payload.get("amount")).doubleValue();
        Double actualAmount = response.jsonPath().getDouble("amount");

        Assert.assertEquals(
                actualAmount,
                expectedAmount,
                "Amount mismatch"
        );

        Assert.assertEquals(
                response.jsonPath().getString("description"),
                payload.get("description"),
                "Description mismatch"
        );

       
        Assert.assertNotNull(
                response.jsonPath().getString("transactionDate"),
                "Transaction date should not be null"
        );

        Assert.assertTrue(
                response.jsonPath().getDouble("balanceAfterTransaction") > 0,
                "Balance after transaction should be greater than zero"
        );
    }

    @DataProvider(name = "depositTransactionData")
    public Object[][] getDepositTransactionData() throws IOException {

        return JsonDataUtil.getTestData(
                "C:\\restAPIProject7AMAug\\TestData\\depositTransactionData.json"
        );
    }
    
    /*
    @Test
    public void DepositMoneyApi() {

        Response response = given()
                .headers(reqHeader.requestHeaders())
                .body(payload.depositPayload())
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

        APIValidation.validateFieldValue(response, "accountNo", "ACC1767542216769");
        APIValidation.validateFieldValue(response, "transactionType", "DEPOSIT");
        APIValidation.validateFieldValue(response, "description", "Salary");
        APIValidation.validateDoubleValue(response, "balanceAfterTransaction", 72000.0);
        APIValidation.validateDoubleValue(response, "amount", 1000.0);
        
        APIValidation.validateFieldType(response, "transactionId", Integer.class);
        APIValidation.validateFieldType(response, "accountNo", String.class);
        APIValidation.validateFieldType(response, "transactionType", String.class);
        APIValidation.validateNumericField(response, "amount");
        APIValidation.validateNumericField(response, "balanceAfterTransaction");


        APIValidation.validateHeader(response, "Content-Type", "application/json");

        APIValidation.validateContains(response, "DEPOSIT");*/
    }

    
    
    
    
    
    
    
    
    
    
    
    
    
    
