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

import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;

import com.ericsson.cifwk.taf.GenericOperator;
import com.ericsson.cifwk.taf.TestExecutionHelper;
import com.ericsson.cifwk.taf.annotations.Context;
import com.ericsson.cifwk.taf.data.Host;
import com.ericsson.oss.tafTests.data.CMRestToolDataProvider;
import com.ericsson.oss.tafTests.operators.rest.CMRestToolRestOperator;
/*
 * Generic Operator is a quite abstract contact point from the Test Case to "operate" on System Under Test -
 * execute some business actions on it
 * This is done via routing the action to particular Contextual Operator.
 * The more important goal of this class is to create expected results, so it can be compared with actual results delivered from
 * layers below 
 *
 * Architecture of using Generic Operator and Contextual Operators addresses the fact, that many services will be available in
 * different contexts (i.e. via UI and REST or via API calls and REST calls, or all different layers)
 * 
 * If service is not exposed in multiple contexts (e.g. only available in REST context), it is rational to incorporate expected and actual results in one class
 * 	- e.g. you might have RestToolExampleOperator class only instead of having a RestToolExampleOperator class plus a RestToolExampleAPiOperator class
 * 	- if this is the case then you will need to implement  the GenericOperator and the relevant ContextualOperator interfaces 
 * 			e.g. " ......  implements GenericOperator, RestOperator"
 *
 * Please note for services delivering a lot of functionality it is wise to make the class more maintainable using Contextual Operator and
 * even helpers
 *  
  */
public class CMRestToolOperator implements GenericOperator, CMRestOperatorActions{

	private static final Logger logger = Logger.getLogger(CMRestToolOperator.class);
	
	/*
	 * For a purpose of easily handling multiple contexts, it is good to have a list of contextual operators
	 * so routing to proper context can be done easily
	 * If there is only one context exposed by tested service, it is good enough to instantiate particular object
	 *  
	 */
	private final Map<String,CMRestOperatorActions> contextualOperators = new HashMap<String,CMRestOperatorActions>();
	
	/*
	 * Initialization of contextual operators
	 */
	public CMRestToolOperator() {
		contextualOperators.put(Context.REST,new CMRestToolRestOperator());
	}
	
	/*
	 * Routing to particular operator can be done using TestExecutionHelper - it returns current context of current method and in current thread 
	 */
	public boolean executeAuthorisedCall (final Host restServer,final  String userName, final String password){
		return contextualOperators.get(TestExecutionHelper.getCurrentContext()).executeAuthorisedCall(restServer, userName,password);
	}
	
	

	/*
	 * Data Provider and Generic Operator are on the same level in TAF architecture 
	 * so they can communicate. In most cases it is impossible to determine expected results without
	 * communicating with Data Provider
	 */
	private final CMRestToolDataProvider dataProvider = new CMRestToolDataProvider();
		
	/*
	 * Expected results for authorised call are directly connected to credentials used for making the call
	 * Data Provider can get information is currently processed credentials are valid
	 * In this example no additional processing is needed, but for clear documentation it is being processed
	 */
/*	public boolean expectedResultOfAuthorisedCall(String userName,	String password) {
		final boolean validCredentialUsed = dataProvider.areValidCredentials(userName,password);
		if (validCredentialUsed){
			logger.info("Processing valid credentials, so call should be succesfull");
			return true;
		} else {
			logger.info("Processing invalid credentials, so call should be unsuccesfull");
			return false;
		}
	}*/
	public boolean expectedResultOfAuthorisedCall(String fdn) {
		final boolean validCredentialUsed = dataProvider.areValidCredentials(fdn);
		if (validCredentialUsed){
			logger.info("Processing valid credentials, so call should be succesfull");
			return true;
		} else {
			logger.info("Processing invalid credentials, so call should be unsuccesfull");
			return false;
		}
	}


}
