package baseTest;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.Listeners;
import org.testng.annotations.AfterClass;
import org.testng.annotations.AfterSuite;

import io.restassured.RestAssured;
import utils.ExtentTestListener;

@Listeners(ExtentTestListener.class)
public class BaseTest {

    protected static final Logger logger =
            LogManager.getLogger(BaseTest.class);

    @BeforeSuite
    public void setup() {

        logger.info("========== TEST EXECUTION STARTED ==========");

        RestAssured.baseURI = "http://localhost:9090";
        logger.info("Base URI set to: {}", RestAssured.baseURI);

        // Optional defaults
        RestAssured.enableLoggingOfRequestAndResponseIfValidationFails();
        logger.info("REST Assured request/response logging enabled on failure");
    }

    @AfterSuite
    public void tearDown() {

        logger.info("========== TEST EXECUTION FINISHED ==========");
    }
}
