package apiTest;

import static io.restassured.RestAssured.given;

import java.sql.Connection;
import java.util.Map;

import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import apibuilder.CustomerPayloadFactory;
import apiconfig.APIEndpoints;
import apiconfig.RequestHeaders;
import apivalidation.APIValidation;
import baseTest.BaseTest;
import dbutils.DBConfig;
import dbutils.DBConnectionManager;
import dbutils.DBValidationUtil;
import io.restassured.response.Response;
import utils.PropertyUtil;




public class DatabaseValidatio_CustomerApiIT extends BaseTest {

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

    // ================= CREATE CUSTOMER =================
    @Test
    public void shouldCreateCustomer() {

        Map<String, Object> payload = CustomerPayloadFactory.createCustomerPayload();

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

        int customerId = response.jsonPath().getInt("customerId");
        Assert.assertTrue(customerId > 0);

        PropertyUtil.set("customer.id", String.valueOf(customerId));

        String query =
                "SELECT * FROM customers WHERE customer_id = " + customerId;

        DBValidationUtil.validateEquals(
                dbConnection, query, "customer_name", payload.get("customerName").toString());

        DBValidationUtil.validateEquals(
                dbConnection, query, "email", payload.get("email").toString());

        DBValidationUtil.validateEquals(
                dbConnection, query, "mobile_number", payload.get("mobileNumber").toString());
    }

    // ================= GET CUSTOMER =================
    @Test(dependsOnMethods = "shouldCreateCustomer")
    public void shouldGetCustomerById() {

        int customerId = Integer.parseInt(PropertyUtil.get("customer.id"));

        Response response =
                given()
                        .headers(requestHeaders.requestHeaders())
                        .pathParam("customerId", customerId)
                .when()
                        .get(APIEndpoints.apiEndpoints.GET_CUSTOMER_BY_ID)
                .then()
                        .extract()
                        .response();

        APIValidation.validateStatuscode(response, 200);

        String query =
                "SELECT customer_name, email FROM customers WHERE customer_id = " + customerId;

        DBValidationUtil.validateEquals(
                dbConnection, query, "customer_name",
                response.jsonPath().getString("customerName"));

        DBValidationUtil.validateEquals(
                dbConnection, query, "email",
                response.jsonPath().getString("email"));
    }

    // ================= UPDATE CUSTOMER =================
    @Test(dependsOnMethods = "shouldGetCustomerById")
    public void shouldUpdateCustomer() {

        int customerId = Integer.parseInt(PropertyUtil.get("customer.id"));
        Map<String, Object> updatePayload =CustomerPayloadFactory.updateCustomerPayload();

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

        APIValidation.validateStatuscode(response, 200);

        String query =
                "SELECT customer_name, city, mobile_number FROM customers WHERE customer_id = " + customerId;

        DBValidationUtil.validateEquals(
                dbConnection, query, "customer_name",
                updatePayload.get("customerName").toString());

        DBValidationUtil.validateEquals(
                dbConnection, query, "city",
                updatePayload.get("city").toString());

        DBValidationUtil.validateEquals(
                dbConnection, query, "mobile_number",
                updatePayload.get("mobileNumber").toString());
    }

    // ================= DELETE CUSTOMER =================
    @Test(dependsOnMethods = "shouldUpdateCustomer")
    public void shouldDeleteCustomer() {

        int customerId = Integer.parseInt(PropertyUtil.get("customer.id"));

        Response response =
                given()
                        .pathParam("customerId", customerId)
                .when()
                        .delete(APIEndpoints.apiEndpoints.DELETE_CUSTOMER_BY_ID)
                .then()
                        .extract()
                        .response();

        APIValidation.validateStatuscode(response, 204);

        String query =
                "SELECT COUNT(*) AS count FROM customers WHERE customer_id = " + customerId;

        String count =
                DBValidationUtil.getSingleValue(dbConnection, query, "count");

        Assert.assertEquals(count, "0");
    }
}
