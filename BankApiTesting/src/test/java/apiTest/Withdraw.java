package apiTest;

import static io.restassured.RestAssured.given;

import java.io.IOException;
import java.util.Map;

import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import apibuilder.LoginPayload;
import apiconfig.APIEndpoints;
import apiconfig.RequestHeaders;
import apivalidation.APIValidation;
import io.restassured.response.Response;
import utils.JsonDataUtil;

public class Withdraw {

	RequestHeaders reqHeader = new RequestHeaders();


	@Test(dataProvider="FundTransferJsonData")
	public void fundTransferApi(Map<String, Object> payload) {

		Response response = given()
				.headers(reqHeader.requestHeaders())
				.body(payload)
				.post(APIEndpoints.apiEndpoints.TRANSFER_MONEY)
				.then()
				.extract()
				.response();
		
		
		APIValidation.validateStatuscode(response, 201);
		APIValidation.logResponse(response);
		
	}
	@DataProvider(name = "FundTransferJsonData")
	public Object[][] getCustomerData() throws IOException {

		return JsonDataUtil.getTestData(
				"C:\\restAPIProject7AMAug\\TestData\\transfer-requests.json"
				);
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
}
