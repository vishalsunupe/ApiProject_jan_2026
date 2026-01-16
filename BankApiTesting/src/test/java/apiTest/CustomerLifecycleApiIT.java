package apiTest;



import static io.restassured.RestAssured.given;

import java.util.Map;

import org.testng.Assert;
import org.testng.annotations.Test;

import apibuilder.CustomerPayloadFactory;
import apiconfig.APIEndpoints;
import apiconfig.RequestHeaders;
import apivalidation.APIValidation;
import baseTest.BaseTest;
import io.restassured.response.Response;
import utils.PropertyUtil;



public class CustomerLifecycleApiIT extends BaseTest {

    private final CustomerPayloadFactory customerPayLoad = new CustomerPayloadFactory();
    private final RequestHeaders requestHeaders = new RequestHeaders();

    // ================= CREATE CUSTOMER =================
    @Test(priority=1)
    public void shouldCreateCustomer() {

        logger.info("===== CREATE CUSTOMER API TEST STARTED =====");

        Map<String, Object> payload = customerPayLoad.createCustomerPayload();

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

        // ---- Field presence ----
        APIValidation.validateFieldExists(response, "customerId");
        APIValidation.validateFieldExists(response, "customerName");
        APIValidation.validateFieldExists(response, "email");

        // ---- Field value vs payload ----
        APIValidation.validateFieldFromPayload(response, "customerName", payload, "customerName");
        APIValidation.validateFieldFromPayload(response, "gender", payload, "gender");
        APIValidation.validateFieldFromPayload(response, "email", payload, "email");

        // ---- Type validation ----
        APIValidation.validateNumericField(response, "customerId");

        int customerId = response.jsonPath().getInt("customerId");
        Assert.assertTrue(customerId > 0, "Customer ID should be generated");

        PropertyUtil.set("customer.id", String.valueOf(customerId));
        logger.info("Customer ID stored for chaining: {}", customerId);

        logger.info("===== CREATE CUSTOMER API TEST PASSED =====");
    }

    // ================= GET CUSTOMER =================
    @Test(priority=2,dependsOnMethods = "shouldCreateCustomer")
    public void shouldGetCustomerById() {

        int customerId = Integer.parseInt(PropertyUtil.get("customer.id"));
        logger.info("Fetching customer with ID: {}", customerId);

        Response response =
                given()
                        .headers(requestHeaders.requestHeaders())
                        .pathParam("customerId", customerId)
                .when()
                        .get(APIEndpoints.apiEndpoints.GET_CUSTOMER_BY_ID)
                .then()
                        .extract()
                        .response();

        APIValidation.logResponse(response);
        APIValidation.validateStatuscode(response, 200);

        APIValidation.validateFieldValue(response, "customerId", customerId);
        APIValidation.validateFieldType(response, "customerName", String.class);
        APIValidation.validateFieldType(response, "email", String.class);

        logger.info("===== GET CUSTOMER API TEST PASSED =====");
    }

    // ================= UPDATE CUSTOMER =================
    @Test(priority=3,dependsOnMethods = "shouldGetCustomerById")
    public void shouldUpdateCustomer() {

        int customerId = Integer.parseInt(PropertyUtil.get("customer.id"));
        Map<String, Object> updatePayload = customerPayLoad.updateCustomerPayload();

        logger.info("Updating customer with ID: {}", customerId);

        Response response =
                given()
                        .headers(requestHeaders.requestHeaders())
                        .pathParam("customerId", customerId)
                        .body(updatePayload)
                .when()
                        .put(APIEndpoints.apiEndpoints.UPDATE_CUSTOMER_BY_ID)
                .then()
                        .extract()
                        .response();

        APIValidation.logResponse(response);
        APIValidation.validateStatuscode(response, 200);

        // ---- Ensure ID unchanged ----
        APIValidation.validateFieldValue(response, "customerId", customerId);

        // ---- Validate updated fields ----
        APIValidation.validateFieldFromPayload(response, "customerName", updatePayload, "customerName");
        APIValidation.validateFieldFromPayload(response, "city", updatePayload, "city");
        APIValidation.validateFieldFromPayload(response, "mobileNumber", updatePayload, "mobileNumber");

        logger.info("===== UPDATE CUSTOMER API TEST PASSED =====");
    }

    // ================= DELETE CUSTOMER =================
    @Test(priority=4,dependsOnMethods = "shouldUpdateCustomer")
    public void shouldDeleteCustomer() {

        int customerId = Integer.parseInt(PropertyUtil.get("customer.id"));
        logger.info("Deleting customer with ID: {}", customerId);

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
