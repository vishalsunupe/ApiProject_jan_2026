package apivalidation;

import org.testng.Assert;

import io.restassured.response.Response;

import java.util.List;
import java.util.Map;

import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;

public class APIValidation {

	/* =======================
       STATUS CODE
       ======================= */
	public static void validateStatuscode(Response response, int expectedCode) {
		Assert.assertEquals(response.getStatusCode(), expectedCode,
				"Status code mismatch!");
	}

	/* =======================
       JSON SCHEMA
       ======================= */
	public static void validateJsonSchema(Response response, String schemaFileName) {
		response.then()
		.assertThat()
		.body(matchesJsonSchemaInClasspath(schemaFileName));
	}

	/* =======================
       RESPONSE TIME
       ======================= */
	public static void validateResponseTime(Response response, long maxTimeInMs) {
		Assert.assertTrue(response.getTime() <= maxTimeInMs,
				"Response time exceeded! Actual: " + response.getTime());
	}

	/* =======================
       CONTENT TYPE
       ======================= */
	public static void validateContentType(Response response, String expectedType) {
		Assert.assertEquals(response.getContentType(), expectedType,
				"Content-Type mismatch!");
	}

	/* =======================
       RESPONSE BODY
       ======================= */
	public static void validateResponseBodyNotNull(Response response) {
		Assert.assertNotNull(response.getBody(), "Response body is NULL");
		Assert.assertFalse(response.getBody().asString().isEmpty(),
				"Response body is EMPTY");
	}

	/* =======================
       FIELD PRESENCE
       ======================= */
	public static void validateFieldExists(Response response, String jsonPath) {
		Assert.assertNotNull(response.jsonPath().get(jsonPath),
				"Missing field: " + jsonPath);
	}

	/* =======================
       FIELD VALUE
       ======================= */
	public static void validateFieldValue(Response response, String jsonPath, Object expectedValue) {
		Assert.assertEquals(response.jsonPath().get(jsonPath), expectedValue,
				"Value mismatch for field: " + jsonPath);
	}

	/* =======================
       FIELD TYPE
       ======================= */
	public static void validateFieldType(Response response, String jsonPath, Class<?> expectedType) {
		Object value = response.jsonPath().get(jsonPath);
		Assert.assertTrue(expectedType.isInstance(value),
				"Invalid type for field: " + jsonPath);
	}

	public static void validateFieldFromPayload(
			Response response,
			String jsonPath,
			Map<String, Object> payload,
			String payloadKey
			) {
		Assert.assertEquals(
				response.jsonPath().get(jsonPath),
				payload.get(payloadKey),
				"Mismatch for field: " + jsonPath
				);
	}
	/* =======================
    DOUBLE VALUE (SAFE)
    ======================= */
	public static void validateDoubleValue(
			Response response,
			String jsonPath,
			double expectedValue
			) {
		double actualValue = response.jsonPath().getDouble(jsonPath);

		Assert.assertEquals(
				actualValue,
				expectedValue,
				0.001,   // delta
				"Value mismatch for field: " + jsonPath
				);
	}
	/* =======================
	   NUMERIC FIELD TYPE
	   ======================= */
	public static void validateNumericField(Response response, String jsonPath) {
	    Object value = response.jsonPath().get(jsonPath);

	    Assert.assertTrue(
	            value instanceof Number,
	            "Invalid numeric type for field: " + jsonPath
	    );
	}




	/* =======================
       ARRAY SIZE
       ======================= */
	public static void validateArraySize(Response response, String jsonPath, int expectedSize) {
		List<?> list = response.jsonPath().getList(jsonPath);
		Assert.assertEquals(list.size(), expectedSize,
				"Array size mismatch!");
	}

	/* =======================
       PARTIAL TEXT MATCH
       ======================= */
	public static void validateContains(Response response, String expectedText) {
		Assert.assertTrue(response.asString().contains(expectedText),
				"Expected text not found in response!");
	}

	/* =======================
       HEADER VALIDATION
       ======================= */
	public static void validateHeader(Response response, String headerName, String expectedValue) {
		Assert.assertEquals(response.getHeader(headerName), expectedValue,
				"Header mismatch: " + headerName);
	}

	/* =======================
       ERROR RESPONSE
       ======================= */
	public static void validateErrorResponse(Response response, int expectedCode, String expectedMessage) {
		validateStatuscode(response, expectedCode);
		Assert.assertEquals(response.jsonPath().getString("error.message"),
				expectedMessage);
	}

	/* =======================
       AUTH TOKEN
       ======================= */
	public static void validateTokenNotNull(Response response) {
		Assert.assertNotNull(response.jsonPath().get("token"),
				"Token is missing!");
	}

	/* =======================
       LOG RESPONSE
       ======================= */
	public static void logResponse(Response response) {
		response.then().log().all();
	}
}
