package com.ericsson.oss.tafTests.cases;

import org.testng.annotations.Test;

import com.ericsson.oss.tafTests.operators.CMOperator;
import com.ericsson.oss.tafTests.data.CMTestDataProvider;
import com.ericsson.cifwk.taf.*;
import com.ericsson.cifwk.taf.annotations.Context;
import com.ericsson.cifwk.taf.annotations.VUsers;
//import static org.testng.Assert.*;

public class CMTestCase extends TorTestCaseHelper implements TestCase {
		
		/**
		 * Verify that script engine performs language validation checks by passing it a bad command and verifying it returns an
		 * error message 
		 * @throws Exception 
		 * @DESCRIPTION Goal of the test case is to Pass a badly formatted get command to the executeCommand method of ScriptEngine
		 * and verify that is correctly handles the command (ie, it returns an error) 
		 * @PRIORITY HIGH
		 * @GROUP Acceptance 
		 * @VUsers 1,5
		 * @Context Rest
		*/
		@VUsers(vusers = {1,5})
		@Context(context={Context.REST})
		@Test(groups={"Acceptance"},dataProvider = "getExpectedResultForBadGetCommandFromCSV", dataProviderClass = CMTestDataProvider.class)
		public void languageValidationCheck(String badGetCommandFromCSV ,String expectedResultBadCommandFromCSV) throws Exception {			
			setTestStep("LanguageValidationCheck Pass bad get command to relevant ScriptEngine executeCommand method and verify that " +
					"bad commands are properly dealt with.");
			assertEquals(CMOperator.sendCommandtoScriptEngine(badGetCommandFromCSV), expectedResultBadCommandFromCSV);
		}	
		
		/**
		 * Verify that when provided with a valid command, script engine passes the command successfully to the CMEditor (verifying that 
		 * mapping of the get command to cm editor works correctly), which in turn queries the DPS. 
		 * @throws Exception 
		 * @DESCRIPTION This test verifies that script engine has passed the command successfully to the CMEditor and DPS by verifying
		 * the returned result from the DPS.
		 * @PRIORITY HIGH
		 * @GROUP Acceptance 
		 * @VUsers 1,5
		 * @Context Rest
		*/
		@VUsers(vusers = {1,5})
		@Context(context={Context.REST})
		@Test(groups={"Acceptance"},dataProvider = "getExpectedResultForGoodGetCommandFromCSV", dataProviderClass = CMTestDataProvider.class)
		public void verifyCommandPassedFromScriptEngineToCMEditorToDPSandReturns(String goodGetCommandFromCSV, String expectedResultGoodCommandFromCSV) throws Exception {			
			setTestStep("Verify that when provided with a valid command, script engine passes the command successfully to the CMEditor (verifying that " +
					"mapping of the get command to cm editor works correctly), which in turn queries the DPS.");
			System.out.println("Equality check " + CMOperator.sendCommandtoScriptEngine(goodGetCommandFromCSV) + " ----- " + expectedResultGoodCommandFromCSV);
			assertEquals(CMOperator.sendCommandtoScriptEngine(goodGetCommandFromCSV), expectedResultGoodCommandFromCSV);	
		}	
		
		/*TODO Please note below test case is a provisional test case for upcoming user story -- 
		http://jira-oss.lmera.ericsson.se/secure/RapidBoard.jspa?rapidView=714&view=planning&selectedIssue=TORF-694
		This test case is not complete as things stand*/
		/**
		 * Verify that get command for a specific ERBS returns expected result
		 * @throws Exception 
		 * @DESCRIPTION Verify that get command for a specific ERBS returns expected result
		 * @PRIORITY HIGH
		 * @GROUP Acceptance 
		 * @VUsers 1,5
		 * @Context Rest
		*/
		@VUsers(vusers = {1,5})
		@Context(context={Context.REST})
		@Test(groups={"Acceptance"},dataProvider = "getExpectedResultForSpecificERBSCommandFromCSV", dataProviderClass = CMTestDataProvider.class)
		public void verifyGetCommandForSpecificERBSReturnsExpectedResult(String getSpecificERBSCommand, String expectedResultFromGetCommandSpecificERBS) throws Exception {			
			setTestStep("Verify that when provided with a valid command, script engine passes the command successfully to the CMEditor (verifying that " +
					"mapping of the get command to cm editor works correctly), which in turn queries the DPS.");
			assertEquals(CMOperator.sendCommandtoScriptEngine(getSpecificERBSCommand), expectedResultFromGetCommandSpecificERBS);	
		}
}
