package com.restassured.testcases;

import org.testng.Assert;
import org.testng.annotations.Test;

import com.restassured.reports.LogStatus;
import com.google.common.io.Files;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.response.Response;
import com.restassured.constants.Constants;

import static io.restassured.RestAssured.given;

import java.io.File;
import java.io.IOException;
import java.util.Hashtable;


public class RestCountriesTest extends BaseTest{



	/*
	 * There should be a test case matching this test name in RUNMANAGER and TESTDATA sheet
	 * If there are multiple data lines in TESTDATA sheet, it will treated as iteration
	 * Mark Yes in RUNMANAGER and TESTDATA to run this test case
	 * @author Amuthan Sakthivel
	 */
	
	
	@Test
	public void getCountryDetailsTest(Hashtable<String, String> data) throws IOException {
		/*
		 * Replacing the name parameter in the endpoint with the data from excel sheet.
		 * Data providers return a hastable and the column name is used as a key to get the value
		 */
		
		Response response=	given()
				.filter(new RequestLoggingFilter(captor)) //This line is mandatory to log the request details to extent report
				.log()
				.all()
				.get(Constants.BASEURL+Constants.COUNTRYDETAILSBYNAME_ENDPOINT.replace("{name}", data.get("countryName")));


		/*
		 * Writing the request and response to extent report
		 * These set of statements needs to repeated in all your tests to log the req and response in extent report.
		 * This can be still be created as and method and called once.
		 * 
		 */
		LogStatus.info("---- Request ---");
		formatAPIAndLogInReport(writer.toString());
		LogStatus.info("---- Response ---");
		formatAPIAndLogInReport(response.prettyPrint());
		
		//Asserting the country code in the response using jsonPath. 
		//Expected value is from TESTDATA sheet and column expectedCountryCapital
		Assert.assertEquals(response.jsonPath().get("[0].capital"), data.get("expectedCountryCapital"));
		
		//Writing the response to an log file
		Files.write(response.asByteArray(), 
				new File(Constants.RESPONSETXTPATH+data.get("TestCaseName")+data.get("countryName")+".txt"));
		
	}

}
