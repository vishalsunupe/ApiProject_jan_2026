package apiTest;

import static io.restassured.RestAssured.given;

import org.testng.annotations.Test;

import apibuilder.LoginPayload;
import apiconfig.APIEndpoints;
import apiconfig.RequestHeaders;
import apivalidation.APIValidation;
import baseTest.BaseTest;
import io.restassured.RestAssured;
import io.restassured.response.Response;

public class LoginApiTest extends BaseTest {
	LoginPayload reqPayLoad = new LoginPayload();

	RequestHeaders reqHeader = new RequestHeaders();


	@Test
	public void _test_Login_valid() {

		Response response = given()
				.body(reqPayLoad.loginPayload())
				.post(APIEndpoints.apiEndpoints.LOGIN)
				.then()
				.extract()
				.response();
		System.out.println(response.asPrettyString());
		
		APIValidation.validateStatuscode(response, 200);
		
	}
	


		
	}
 

