/*------------------------------------------------------------------------------
 *******************************************************************************
 * COPYRIGHT Ericsson 2012
 *
 * The copyright to the computer program(s) herein is the property of
 * Ericsson Inc. The programs may be used and/or copied only with written
 * permission from Ericsson Inc. or in accordance with the terms and
 * conditions stipulated in the agreement/contract under which the
 * program(s) have been supplied.
 *******************************************************************************
 *----------------------------------------------------------------------------*/
package com.ericsson.oss.tafTests.operators.rest;

import java.util.List;

import org.apache.log4j.Logger;

import com.ericsson.cifwk.taf.RestOperator;
import com.ericsson.cifwk.taf.data.Host;
import com.ericsson.cifwk.taf.tal.rest.RestResponseCode;
import com.ericsson.cifwk.taf.tools.RestTool;
import com.ericsson.oss.tafTests.getters.rest.CMRestToolRestGetter;
//import com.ericsson.america.integrationtest.operators.RestOperatorExampleOperatorActions;
/*
 * This class is "operating" on SUT in REST context. To do it it uses RestTool
 */
public class CMRestToolRestOperator implements CMRestOperatorActions, RestOperator{

	private final static Logger logger = Logger.getLogger(CMRestToolRestOperator.class);

	private RestTool restTool;
	/*
	 * This is implementation of executeAuthorisedCall method in REST context
	 */
	@Override
	public boolean executeAuthorisedCall(Host restServer, String userName,String password) {
		restTool = new RestTool(restServer);
		restTool.setAuthenticationCredentails(restServer.getUser(), restServer.getPass());
	
		/*
		 * URI is externalised and isolated in GETTER class, so when it changes, it will required effort in one place only
		 */
		final List<String> calResponseContent = restTool.get(CMRestToolRestGetter.getAuthorisedCallUri());
		System.out.println("RestToolExampleRestGetter.getAuthorisedCallUri()  " + CMRestToolRestGetter.getAuthorisedCallUri());
		final List<RestResponseCode> callResponseCodes = restTool.getLastResponseCodes();
		printResponseTimes();
		/*
		 * Verification contains 2 steps: verifying if response code is as expected and verifying if content is as expected
		 */
		return verifyResponseCodes(callResponseCodes) && verifyResponseContent(calResponseContent);
	}

	/*
	 * This method is implementing verification of response codes 
	 */
	private boolean verifyResponseCodes(List<RestResponseCode> responseCodes){
		boolean result = true;
		for (RestResponseCode callResponseCode : responseCodes){
			/*
			 * Putting debug information is very helpful when test case is failing
			 */
			logger.debug("Processing response code " + callResponseCode);
			/*
			 * Expected content is externalised and isolated in GETTER class for maintenance purposes
			 */
			result = result && (callResponseCode == CMRestToolRestGetter.SUCCESFULL_CALL_CODE);
		}
		/*
		 * Because method is returning boolean, it is good practice to put actual result on the test report
		 */
		logger.info("Result of response code verification: " + result);
		return result;
	}
	/*
	 * Here the verification of content is done
	 */
	private boolean verifyResponseContent(List<String> responseContent){
		boolean result = true;
		for (String callResponseContent : responseContent){
			logger.debug("Processing response content " + callResponseContent);
			/*
			 * Expected content is externalised and isolated in GETTER class for maintenance purposes
			 */
			result = result && callResponseContent.contains(CMRestToolRestGetter.AUTHORISED_CALL_CONTENT);
		}
		/*
		 * Because method is returning boolean, it is good practice to put actual result on the test report
		 */
		logger.info("Result of response content verification: " + result);
		return result;
	}
	/*
	 * This method is an example of operating on response times programatically,
	 * The average response time is being logged by Tool Monitor if enabled
	 */
	private void printResponseTimes(){
		final List<Long> callResponseTimes = restTool.getLastExecutionTimes();
		for (Long time: callResponseTimes){
			logger.debug("Response time: " + time);
		}
		logger.debug("Average response time: " + restTool.getAverageExecutionTime());		
	}
	
	public List<String> getURIResponse(){
		return restTool.get(CMRestToolRestGetter.getAuthorisedCallUri());
	}
	
	
}