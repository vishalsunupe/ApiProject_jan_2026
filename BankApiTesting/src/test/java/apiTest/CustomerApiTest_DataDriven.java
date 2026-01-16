package apiTest;

import static io.restassured.RestAssured.given;

import java.io.IOException;
import java.util.Map;

import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import apibuilder.CustomerPayloadFactory;
import apiconfig.APIEndpoints;
import apiconfig.RequestHeaders;
import apivalidation.APIValidation;
import baseTest.BaseTest;
import io.restassured.response.Response;
import utils.JsonDataUtil;
import utils.PropertyUtil;

public class CustomerApiTest_DataDriven extends BaseTest {

	private final CustomerPayloadFactory customerPayLoad = new CustomerPayloadFactory();
    RequestHeaders reqHeader = new RequestHeaders();

    @Test(dataProvider = "customerJsonData")
    public void CreateNewCustomerAPI(Map<String, Object> payload) {

        logger.info("===== CREATE CUSTOMER API TEST STARTED =====");
        logger.info("Request Payload: {}", payload);

        Response response =
                given()
                        .headers(reqHeader.requestHeaders())
                        .body(payload)
                .when()
                        .post(APIEndpoints.apiEndpoints.CREATE_CUSTOMER)
                .then()
                        .extract()
                        .response();

        logger.info("Response received successfully");

        APIValidation.logResponse(response);

        logger.info("Validating Status Code");
        APIValidation.validateStatuscode(response, 201);

        logger.info("Validating Response Schema");
        APIValidation.validateJsonSchema(
                response,
                "schemas/CustomerSchemas/create-customer-response-schema.json"
        );

        logger.info("Validating Headers, ContentType and Response Time");
        APIValidation.validateContentType(response, "application/json");
        APIValidation.validateResponseTime(response, 3000);
        APIValidation.validateHeader(response, "Transfer-Encoding", "chunked");
        APIValidation.validateHeader(response, "Keep-Alive", "timeout=60");

        logger.info("Validating response fields with request payload");

        APIValidation.validateFieldFromPayload(response, "customerName", payload, "customerName");
        APIValidation.validateFieldFromPayload(response, "gender", payload, "gender");
        APIValidation.validateFieldFromPayload(response, "dateOfBirth", payload, "dateOfBirth");
        APIValidation.validateFieldFromPayload(response, "address", payload, "address");
        APIValidation.validateFieldFromPayload(response, "city", payload, "city");
        APIValidation.validateFieldFromPayload(response, "state", payload, "state");
        APIValidation.validateFieldFromPayload(response, "pin", payload, "pin");
        APIValidation.validateFieldFromPayload(response, "mobileNumber", payload, "mobileNumber");
        APIValidation.validateFieldFromPayload(response, "email", payload, "email");

        logger.info("Validating auto-generated fields");

        Assert.assertNotNull(response.jsonPath().get("customerId"));
        Assert.assertNotNull(response.jsonPath().get("createdAt"));

        logger.info("===== CREATE CUSTOMER API TEST PASSED =====");
    }

    @DataProvider(name = "customerJsonData")
    public Object[][] getCustomerData() throws IOException {

        logger.info("Loading customer test data from Customer.json");

        String jsonPath = "src/test/resources/TestData/Customer.json";

        return JsonDataUtil.getTestData(jsonPath);
    }



    //@Test
    public void getCustomerById_validId() {

        int customerId = 104;
        logger.info("===== GET CUSTOMER BY ID TEST STARTED =====");
        logger.info("Customer ID: {}", customerId);

        Response response =
                given()
                        .headers(reqHeader.requestHeaders())
                        .pathParam("customerId", customerId)
                .when()
                        .get(APIEndpoints.apiEndpoints.GET_CUSTOMER_BY_ID)
                .then()
                        .extract()
                        .response();

        APIValidation.logResponse(response);
        APIValidation.validateStatuscode(response, 200);

        logger.info("Validating response fields for customer ID {}", customerId);

        APIValidation.validateFieldValue(response, "customerId", 104);
        APIValidation.validateFieldValue(response, "customerName", "Neha Mehta");
        APIValidation.validateFieldValue(response, "gender", "female");

        logger.info("===== GET CUSTOMER BY ID TEST PASSED =====");
    }

    //@Test
    public void UpdateCustomerAPI() {
    	   int customerId = Integer.parseInt(PropertyUtil.get("customer.id"));
           Map<String, Object> updatePayload = customerPayLoad.updateCustomerPayload();

        logger.info("===== UPDATE CUSTOMER API TEST STARTED =====");
        logger.info("Updating customer ID: {}", customerId);

        Response response =
                given()
                        .pathParam("customerId", customerId)
                        .headers(reqHeader.requestHeaders())
                        .body(updatePayload)
                .when()
                        .put(APIEndpoints.apiEndpoints.UPDATE_CUSTOMER_BY_ID)
                .then()
                        .extract()
                        .response();

        APIValidation.logResponse(response);
        APIValidation.validateStatuscode(response, 200);

        logger.info("Validating updated customer details");

        APIValidation.validateFieldValue(response, "customerId", 108);
        APIValidation.validateFieldValue(response, "customerName", "Vinod patil");

        logger.info("===== UPDATE CUSTOMER API TEST PASSED =====");
    }

    //@Test
    public void DeleteCustomerAPI() {

        int customerId = 103;
        logger.info("===== DELETE CUSTOMER API TEST STARTED =====");
        logger.info("Deleting customer ID: {}", customerId);

        Response response =
                given()
                        .pathParam("customerId", customerId)
                .when()
                        .delete(APIEndpoints.apiEndpoints.DELETE_CUSTOMER_BY_ID)
                .then()
                        .extract()
                        .response();

        APIValidation.logResponse(response);
        APIValidation.validateStatuscode(response, 204);

        logger.info("===== DELETE CUSTOMER API TEST PASSED =====");
    }
}
