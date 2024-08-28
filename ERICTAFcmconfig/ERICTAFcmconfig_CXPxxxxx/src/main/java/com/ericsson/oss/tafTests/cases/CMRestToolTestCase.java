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
package com.ericsson.oss.tafTests.cases;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.openqa.selenium.Keys;
import org.testng.annotations.Test;

import com.ericsson.cifwk.taf.TestCase;
import com.ericsson.cifwk.taf.TorTestCaseHelper;
import com.ericsson.cifwk.taf.annotations.Context;
import com.ericsson.cifwk.taf.annotations.VUsers;
import com.ericsson.cifwk.taf.data.Host;
import com.ericsson.cifwk.taf.tools.RestTool;
import com.ericsson.cifwk.taf.ui.Browser;
import com.ericsson.cifwk.taf.ui.BrowserTab;
import com.ericsson.cifwk.taf.ui.BrowserType;
import com.ericsson.cifwk.taf.ui.UI;
import com.ericsson.cifwk.taf.ui.core.SelectorType;
import com.ericsson.cifwk.taf.ui.core.UiComponent;
import com.ericsson.cifwk.taf.ui.sdk.ViewModel;
import com.ericsson.oss.tafTests.data.CMRestToolData;

/*
 * Comments added for the purpose of showing data and logic flow are marked as comment, not javadoc
 * Javadoc comments are part of the original code
 *
 * Test Case class is describing the sequence and scope of verification logic
 * It's content is mainly generated from AVS service
 * Added part should be as minimal as possible and should contain:
 * - interaction between test cases
 * - usage of test data
 * - verification and reporting logic
 *   
 *  Shape of this class determines output on test report.
 *
 * Please note superclass is TorTestCaseHelper that provides reporting utilities
 * and it is marked with TestCase interface used as a marker only  
 */

public class CMRestToolTestCase extends TorTestCaseHelper implements TestCase {

    /**
     * Verify running a get command for a non-existent MeContext returns a failure code.
     * 
     * @DESCRIPTION Verify running a get command for a non-existent MeContext returns a failure code.
     * @PRIORITY HIGH
     * @GROUP
     * @VUsers
     * @Context Rest
     */
    @VUsers(vusers = { 1 })
    @Context(context = { Context.REST })
    @Test(groups = { "Smoke", "GAT", "RV_WORKFLOW" }, dataProvider = "getMeContext", dataProviderClass = CMRestToolData.class)
    public void getMeContextExpectingFalseAsResult(final Host restServer, final String command, final String expectedResponse) {
        setTestCase("getMeContextExpectingFalseAsResult ", "Host Id is " + restServer.getIp());
        final List<String> postResponse = sendRequest(restServer, command);
        final String postResultString = postResponse.get(0);
        assertTrue("Expected a status code of -1 for test (getMeContextExpectingFalseAsResult)" + command,
                CMRestToolData.getcommandResponseDto(postResultString).getStatusCode() == -1);
    }

    /**
     * Verify running a create command for an MeContext returns an expected FDN.
     * 
     * @DESCRIPTION Verify running a create command for an MeContext returns an expected FDN.
     * @PRIORITY HIGH
     * @GROUP GAT
     * @VUsers
     * @Context Rest
     */

    @VUsers(vusers = { 1 })
    @Context(context = { Context.REST })
    @Test(groups = { "Smoke", "GAT", "RV_WORKFLOW" }, dataProvider = "createMeContext", dataProviderClass = CMRestToolData.class)
    public void createMeContext(final Host restServer, final String command, final String expectedResponse) {
        setTestCase("createMeContext ", "Host Id is " + restServer.getIp());
        final List<String> postResponse = sendRequest(restServer, command);
        final HashMap<String, String> actualResult = CMRestToolData.getActualResultAndPlaceInMap(postResponse);
        final HashMap<String, String> expectedResultMap = CMRestToolData.getExpectedResultsMapForMeContextCommand();
        assertTrue("Expected Response does not match for input of " + command, postResponse.get(0).contains(expectedResponse));
        assertTrue("FDN doesn't Match expected result", actualResult.get("FDN").equals(expectedResultMap.get("FDN")));
    }

    /**
     * Verify running a get command for a valid MeContext returns a valid FDN.
     * 
     * @DESCRIPTION Verify running a get command for a valid MeContext returns a valid FDN.
     * @PRIORITY HIGH
     * @GROUP
     * @VUsers
     * @Context Rest
     */

    @VUsers(vusers = { 1 })
    @Context(context = { Context.REST })
    @Test(groups = { "Smoke", "GAT", "RV_WORKFLOW" }, dataProvider = "getMeContext", dataProviderClass = CMRestToolData.class)
    public void getMeContextExpectingTrueAsResult(final Host restServer, final String command, final String expectedResponse) {
        setTestCase("getMeContextExpectingTrueAsResult ", "Host Id is " + restServer.getIp());
        final List<String> postResponse = sendRequest(restServer, command);
        final HashMap<String, String> actualResult = CMRestToolData.getActualResultAndPlaceInMap(postResponse);
        final HashMap<String, String> expectedResultMap = CMRestToolData.getExpectedResultsMapForMeContextCommand();
        assertTrue("FDN doesn't Match expected result", actualResult.get("FDN").equals(expectedResultMap.get("FDN")));
    }

    /**
     * Verify running a get command for a non-existent ManagedElement returns a failure code.
     * 
     * @DESCRIPTION Verify running a get command for a non-existent MeContext returns a failure code.
     * @PRIORITY HIGH
     * @GROUP
     * @VUsers
     * @Context Rest
     */
    @VUsers(vusers = { 1 })
    @Context(context = { Context.REST })
    @Test(groups = { "Smoke", "GAT", "RV_WORKFLOW" }, dataProvider = "getMeContextAndManagedElement", dataProviderClass = CMRestToolData.class)
    public void getMeContextAndManagedElementExpectingFalseAsResult(final Host restServer, final String command, final String expectedResponse) {
        setTestCase("getMeContextAndManagedElementExpectingFalseAsResult ", "Host Id is " + restServer.getIp());
        final List<String> postResponse = sendRequest(restServer, command);
        final String postResultString = postResponse.get(0);
        assertTrue("Expected a status code of -1 for test (getMeContextAndManagedElementExpectingFalseAsResult)" + command, CMRestToolData
                .getcommandResponseDto(postResultString).getStatusCode() == -1);
    }

    /**
     * Verify running a create command for an Managed Element returns an expected FDN.
     * 
     * @DESCRIPTION Verify running a create command for an Managed Element returns an expected FDN.
     * @PRIORITY HIGH
     * @GROUP GAT
     * @VUsers
     * @Context Rest
     */
    @VUsers(vusers = { 1 })
    @Context(context = { Context.REST })
    @Test(groups = { "Smoke", "GAT", "RV_WORKFLOW" }, dataProvider = "createMeContextAndManagedElement", dataProviderClass = CMRestToolData.class)
    public void createMeContextAndManagedElement(final Host restServer, final String command, final String expectedResponse) {
        setTestCase("createMeContextAndManagedElement ", "Host Id is " + restServer.getIp());
        final List<String> postResponse = sendRequest(restServer, command);
        final HashMap<String, String> actualResult = CMRestToolData.getActualResultAndPlaceInMap(postResponse);
        final HashMap<String, String> expectedResultMap = CMRestToolData.getExpectedResultsMapForManagedElementCommand();
        assertTrue("Expected Response does not match for input of " + command, postResponse.get(0).contains(expectedResponse));
        assertTrue("FDN doesn't Match expected result", actualResult.get("FDN").equals(expectedResultMap.get("FDN")));
    }

    /**
     * Verify running a get command for a valid ManagedElement returns a valid FDN.
     * 
     * @DESCRIPTION Verify running a get command for a valid ManagedElement returns a valid FDN.
     * @PRIORITY HIGH
     * @GROUP
     * @VUsers
     * @Context Rest
     */
    @VUsers(vusers = { 1 })
    @Context(context = { Context.REST })
    @Test(groups = { "Smoke", "GAT", "RV_WORKFLOW" }, dataProvider = "getMeContextAndManagedElement", dataProviderClass = CMRestToolData.class)
    public void getMeContextAndManagedElementExpectingTrueAsResult(final Host restServer, final String command, final String expectedResponse) {
        setTestCase("getMeContextAndManagedElementExpectingTrueAsResult ", "Host Id is " + restServer.getIp());
        final List<String> postResponse = sendRequest(restServer, command);
        final HashMap<String, String> actualResult = CMRestToolData.getActualResultAndPlaceInMap(postResponse);
        final HashMap<String, String> expectedResultMap = CMRestToolData.getExpectedResultsMapForManagedElementCommand();
        assertTrue("FDN doesn't Match expected result", actualResult.get("FDN").equals(expectedResultMap.get("FDN")));
    }

    /**
     * Verify running a get command for a non-existent ENodeBFunction returns a failure code.
     * 
     * @DESCRIPTION Verify running a get command for a non-existent ENodeBFunction returns a failure code.
     * @PRIORITY HIGH
     * @GROUP
     * @VUsers
     * @Context Rest
     */
    @VUsers(vusers = { 1 })
    @Context(context = { Context.REST })
    @Test(groups = { "Smoke", "GAT", "RV_WORKFLOW" }, dataProvider = "get_MeContext_And_ManagedElement_And_ENodeBFunction_Expecting_False_As_Result", dataProviderClass = CMRestToolData.class)
    public void get_MeContext_And_ManagedElement_And_ENodeBFunction_Expecting_False_As_Result(final Host restServer, final String command,
                                                                                              final String expectedResponse) {
        setTestCase("get_MeContext_And_ManagedElement_And_ENodeBFunction_Expecting_False_As_Result ", "Host Id is " + restServer.getIp());
        final List<String> postResponse = sendRequest(restServer, command);
        final String postResultString = postResponse.get(0);
        assertTrue("Expected a status code of -1 for test (getMeContextAndManagedElementExpectingFalseAsResult)" + command, CMRestToolData
                .getcommandResponseDto(postResultString).getStatusCode() == -1);
    }

    /**
     * Verify running a create command for an ENodeBFunction returns an expected FDN.
     * 
     * @DESCRIPTION Verify running a create command for an ENodeBFunction returns an expected FDN.
     * @PRIORITY HIGH
     * @GROUP GAT
     * @VUsers
     * @Context Rest
     */
    @VUsers(vusers = { 1 })
    @Context(context = { Context.REST })
    @Test(groups = { "Smoke", "GAT", "RV_WORKFLOW" }, dataProvider = "create_MeContext_And_Managed_Element_And_ENodeBFunction", dataProviderClass = CMRestToolData.class)
    public void create_MeContext_And_Managed_Element_And_ENodeBFunction(final Host restServer, final String command, final String expectedResponse) {
        setTestCase("create_MeContext_And_Managed_Element_And_ENodeBFunction ", "Host Id is " + restServer.getIp());
        final List<String> postResponse = sendRequest(restServer, command);
        final HashMap<String, String> actualResult = CMRestToolData.getActualResultAndPlaceInMap(postResponse);
        final HashMap<String, String> expectedResultMap = CMRestToolData.getExpectedResultsMapForENodeBFunctionCommand();
        assertTrue("Expected Response does not match for input of " + command, postResponse.get(0).contains(expectedResponse));
        assertTrue("FDN doesn't Match expected result", actualResult.get("FDN").equals(expectedResultMap.get("FDN")));
    }

    /*
     * Following Test Cases added for KAOS User Stories to create a SectorEquipmentFunction and EUtranCellFDD
     */

    /**
     * Verify running a create command for a SectorEquipmentFunction to be used by EUtranCellFDD returns an expected FDN.
     * 
     * @DESCRIPTION Verify running a create command for a SectorEquipmentFunction returns an expected FDN.
     * @PRIORITY HIGH
     * @GROUP GAT
     * @VUsers
     * @Context Rest
     */
    @VUsers(vusers = { 1 })
    @Context(context = { Context.REST })
    @Test(groups = { "Smoke", "GAT", "RV_WORKFLOW" }, dataProvider = "create_MeContext_And_Managed_Element_And_ENodeBFunction_And_SectorEquipmentFunction", dataProviderClass = CMRestToolData.class)
    public void create_MeContext_And_Managed_Element_And_ENodeBFunction_And_SectorEquipmentFunction(final Host restServer, final String command,
                                                                                                    final String expectedResponse) {
        setTestCase("create_MeContext_And_Managed_Element_And_ENodeBFunction_And_SectorEquipmentFunction ", "Host Id is " + restServer.getIp());
        final List<String> postResponse = sendRequest(restServer, command);

        final HashMap<String, String> actualResult = CMRestToolData.getActualResultAndPlaceInMap(postResponse);
        final HashMap<String, String> expectedResultMap = CMRestToolData.getExpectedResultsMapForSectorEquipmentFunction();
        assertTrue("Expected Response does not match for input of " + command, postResponse.get(0).contains(expectedResponse));
        assertTrue("FDN doesn't Match expected result", actualResult.get("FDN").equals(expectedResultMap.get("FDN")));
    }

    /**
     * Verify running a create command for a second SectorEquipmentFunction to be used by EUtranCellFDD returns an expected FDN.
     * 
     * @DESCRIPTION Verify running a create command for a SectorEquipmentFunction returns an expected FDN.
     * @PRIORITY HIGH
     * @GROUP GAT
     * @VUsers
     * @Context Rest
     */
    @VUsers(vusers = { 1 })
    @Context(context = { Context.REST })
    @Test(groups = { "Smoke", "GAT", "RV_WORKFLOW" }, dataProvider = "create_MeContext_And_Managed_Element_And_ENodeBFunction_And_SectorEquipmentFunction_MeContext_123abc456", dataProviderClass = CMRestToolData.class)
    public void create_MeContext_And_Managed_Element_And_ENodeBFunction_And_SectorEquipmentFunction_MeContext_123abc456(final Host restServer,
                                                                                                                        final String command,
                                                                                                                        final String expectedResponse) {
        setTestCase("create_MeContext_And_Managed_Element_And_ENodeBFunction_And_SectorEquipmentFunction_MeContext_123abc456 ", "Host Id is "
                + restServer.getIp());
        final List<String> postResponse = sendRequest(restServer, command);
        final HashMap<String, String> actualResult = CMRestToolData.getActualResultAndPlaceInMap(postResponse);
        final HashMap<String, String> expectedResultMap = CMRestToolData.getExpectedResultsMapForSectorEquipmentFunction_2();
        assertTrue("Expected Response does not match for input of " + command, postResponse.get(0).contains(expectedResponse));
        assertTrue("FDN doesn't Match expected result", actualResult.get("FDN").equals(expectedResultMap.get("FDN")));
    }

    /**
     * Verify running a create command for a EUtranCellFDD (EUC001)
     * 
     * @DESCRIPTION Verify running a create command for a EUtranCellFDD returns an expected FDN.
     * @PRIORITY HIGH
     * @GROUP GAT
     * @VUsers
     * @Context Rest
     */
    @VUsers(vusers = { 1 })
    @Context(context = { Context.REST })
    @Test(groups = { "Smoke", "GAT", "RV_WORKFLOW" }, dataProvider = "createFirstEUtranCellFDD", dataProviderClass = CMRestToolData.class)
    public void createFirstEUtranCellFDD(final Host restServer, final String command, final String expectedResponse) {

        setTestCase("createFirstEUtranCellFDD ", "Host Id is " + restServer.getIp());
        final List<String> postResponse = sendRequest(restServer, command);
        final HashMap<String, String> actualResult = CMRestToolData.getActualResultAndPlaceInMap(postResponse);
        final HashMap<String, String> expectedResultMap = CMRestToolData.getExpectedResultsMapForcreateFirstEUtranCellFDD();
        assertTrue("Expected Response does not match for input of " + command, postResponse.get(0).contains(expectedResponse));
        assertTrue("FDN does Match expected result", actualResult.get("FDN").equals(expectedResultMap.get("FDN")));
    }

    /**
     * Verify running a create command for a second EUtranCellFDD (EUC002)
     * 
     * @DESCRIPTION Verify running a create command for a EUtranCellFDD returns an expected FDN.
     * @PRIORITY HIGH
     * @GROUP GAT
     * @VUsers
     * @Context Rest
     */
    @VUsers(vusers = { 1 })
    @Context(context = { Context.REST })
    @Test(groups = { "Smoke", "GAT", "RV_WORKFLOW" }, dataProvider = "createSecondEUtranCellFDD", dataProviderClass = CMRestToolData.class)
    public void createSecondEUtranCellFDD(final Host restServer, final String command, final String expectedResponse) {

        setTestCase("createSecondEUtranCellFDD ", "Host Id is " + restServer.getIp());
        final List<String> postResponse = sendRequest(restServer, command);
        final HashMap<String, String> actualResult = CMRestToolData.getActualResultAndPlaceInMap(postResponse);
        final HashMap<String, String> expectedResultMap = CMRestToolData.getExpectedResultsMapForcreateSecondEUtranCellFDD();
        assertTrue("Expected Response does not match for input of " + command, postResponse.get(0).contains(expectedResponse));
        assertTrue("FDN does Match expected result", actualResult.get("FDN").equals(expectedResultMap.get("FDN")));
    }

    /**
     * Verify running a create command for a Third EUtranCellFDD (EUC003)
     * 
     * @DESCRIPTION Verify running a create command for a EUtranCellFDD returns an expected FDN.
     * @PRIORITY HIGH
     * @GROUP GAT
     * @VUsers
     * @Context Rest
     */
    @VUsers(vusers = { 1 })
    @Context(context = { Context.REST })
    @Test(groups = { "Smoke", "GAT", "RV_WORKFLOW" }, dataProvider = "createThirdEUtranCellFDD", dataProviderClass = CMRestToolData.class)
    public void createThirdEUtranCellFDD(final Host restServer, final String command, final String expectedResponse) {

        setTestCase("createThirdEUtranCellFDD ", "Host Id is " + restServer.getIp());
        final List<String> postResponse = sendRequest(restServer, command);
        final HashMap<String, String> actualResult = CMRestToolData.getActualResultAndPlaceInMap(postResponse);

        final HashMap<String, String> expectedResultMap = CMRestToolData.getExpectedResultsMapForcreateThirdEUtranCellFDD();
        assertTrue("Expected Response does not match for input of " + command, postResponse.get(0).contains(expectedResponse));
        assertTrue("FDN does Match expected result", actualResult.get("FDN").equals(expectedResultMap.get("FDN")));
    }

    /*
     * End of Test Cases added for KAOS User Stories to create a SectorEquipmentFunction and EUtranCellFDD
     */

    /**
     * TORF-13689 Verify running a get command for a EUtranCellFDD with attribute filtering on >
     * 
     * @DESCRIPTION Verify running a get command for a EUtranCellFDD with attribute filtering on > returns an expected FDNs.
     * @PRIORITY HIGH
     * @GROUP GAT
     * @VUsers
     * @Context Rest
     */
    @VUsers(vusers = { 1 })
    @Context(context = { Context.REST })
    @Test(groups = { "Smoke", "GAT", "RV_WORKFLOW" }, dataProvider = "searchWithAttributeFilterOnGreaterThanReturnsCorrectFDNs", dataProviderClass = CMRestToolData.class)
    public void searchWithAttributeFilterOnGreaterThanReturnsCorrectFDNs(final Host restServer, final String command, final String expectedResponse) {

        setTestCase("searchWithAttributeFilterOnGreaterThanReturnsCorrectFDNs ", "Host Id is " + restServer.getIp());
        final List<String> postResponse = sendRequest(restServer, command);
        final HashMap<String, HashMap<String, String>> actualResultMap = CMRestToolData
                .getActualResultAndPlaceInMapForMultipleMoResponse(postResponse);
        final String[] expectedResults = CMRestToolData.getExpectedResultsMapForSearchWithAttributeFilterOnGreaterThanReturnsCorrectFDNs();
        compareActualandExpectedResults(actualResultMap, expectedResults);

    }

    /**
     * Verify running a get command for a valid ENodeBFunction returns a valid FDN.
     * 
     * @DESCRIPTION Verify running a get command for a valid ENodeBFunction returns a valid FDN.
     * @PRIORITY HIGH
     * @GROUP GAT
     * @VUsers
     * @Context Rest
     */
    @VUsers(vusers = { 1 })
    @Context(context = { Context.REST })
    @Test(groups = { "Smoke", "GAT", "RV_WORKFLOW" }, dataProvider = "get_MeContext_And_ManagedElement_And_ENodeBFunction_Expecting_False_As_Result", dataProviderClass = CMRestToolData.class)
    public void get_MeContext_And_ManagedElement_And_ENodeBFunction_Expecting_True_As_Result(final Host restServer, final String command,
                                                                                             final String expectedResponse) {
        setTestCase("get_MeContext_And_ManagedElement_And_ENodeBFunction_Expecting_True_As_Result ", "Host Id is " + restServer.getIp());
        final List<String> postResponse = sendRequest(restServer, command);
        final HashMap<String, String> actualResult = CMRestToolData.getActualResultAndPlaceInMap(postResponse);
        final HashMap<String, String> expectedResultMap = CMRestToolData.getExpectedResultsMapForENodeBFunctionCommand();
        assertTrue("FDN doesn't Match expected result", actualResult.get("FDN").equals(expectedResultMap.get("FDN")));
    }

    /**
     * Creating a second MeContext instance as a root for further ManagedElement and ENodeBFunction.
     * 
     * @DESCRIPTION Creating a second MeContext instance as a root for further ManagedElement and ENodeBFunction. Also verifying FDN as a sanity
     *              check.
     * @PRIORITY HIGH
     * @GROUP
     * @VUsers
     * @Context Rest
     */

    @VUsers(vusers = { 1 })
    @Context(context = { Context.REST })
    @Test(groups = { "Smoke", "GAT", "RV_WORKFLOW" }, dataProvider = "createASecondMeContextInstance", dataProviderClass = CMRestToolData.class)
    public void createASecondMeContextInstance(final Host restServer, final String command, final String expectedResponse) {
        setTestCase("createASecondMeContextInstance ", "Host Id is " + restServer.getIp());
        final List<String> postResponse = sendRequest(restServer, command);
        final HashMap<String, String> actualResult = CMRestToolData.getActualResultAndPlaceInMap(postResponse);
        final HashMap<String, String> expectedResultMap = CMRestToolData.getExpectedResultsMapForSecondMeContextCommand();
        assertTrue("Expected Response does not match for input of " + command, postResponse.get(0).contains(expectedResponse));
        assertTrue("FDN doesn't Match expected result", actualResult.get("FDN").equals(expectedResultMap.get("FDN")));
    }

    /**
     * Creating a second ManagedElement instance as part of structure for ENodeBFunction.
     * 
     * @DESCRIPTION Creating a second ManagedElement instance as part of structure for ENodeBFunction. Also verifying FDN as a sanity check.
     * @PRIORITY HIGH
     * @GROUP
     * @VUsers
     * @Context Rest
     */

    @VUsers(vusers = { 1 })
    @Context(context = { Context.REST })
    @Test(groups = { "Smoke", "GAT", "RV_WORKFLOW" }, dataProvider = "createASecondManangedElementInstance", dataProviderClass = CMRestToolData.class)
    public void createASecondManangedElementInstance(final Host restServer, final String command, final String expectedResponse) {
        setTestCase("createASecondManangedElementInstance ", "Host Id is " + restServer.getIp());
        final List<String> postResponse = sendRequest(restServer, command);
        final HashMap<String, String> actualResult = CMRestToolData.getActualResultAndPlaceInMap(postResponse);
        final HashMap<String, String> expectedResultMap = CMRestToolData.getExpectedResultsMapForSecondManagedElementCommand();
        assertTrue("Expected Response does not match for input of " + command, postResponse.get(0).contains(expectedResponse));
        assertTrue("FDN doesn't Match expected result", actualResult.get("FDN").equals(expectedResultMap.get("FDN")));
    }

    /**
     * Verify that running a create command to try to create a duplicate MeContext returns a status code error.
     * 
     * @DESCRIPTION Verify that running a create command to try to create a duplicate MeContext returns a status code error.
     * @PRIORITY HIGH
     * @GROUP
     * @VUsers
     * @Context Rest
     */
    @VUsers(vusers = { 1 })
    @Context(context = { Context.REST })
    @Test(groups = { "Smoke", "GAT", "RV_WORKFLOW" }, dataProvider = "createMeContext", dataProviderClass = CMRestToolData.class)
    public void createDuplicateMeContext(final Host restServer, final String command, final String expectedResponse) {
        setTestCase("createDuplicateMeContext ", "Host Id is " + restServer.getIp());
        final List<String> postResponse = sendRequest(restServer, command);
        final String postResultString = postResponse.get(0);
        assertTrue("Expected a status code of -2 for test (createDuplicateMeContext)" + command,
                CMRestToolData.getcommandResponseDto(postResultString).getStatusCode() == -2);
        /*
         * assertTrue("Expected error as command is trying to create duplicate" + command,
         * CMRestToolData.getcommandResponseDto(postResultString).getStatusMessage (). equals(
         * "Unable to create root object of type: MeContext with name: 123456 as a matching object already exists" ));
         */
    }

    /**
     * Verify that running a create command to try to create a duplicate ManagedElement returns a status code error.
     * 
     * @DESCRIPTION Verify that running a create command to try to create a duplicate ManagedElement returns a status code error.
     * @PRIORITY HIGH
     * @GROUP
     * @VUsers
     * @Context Rest
     */
    @VUsers(vusers = { 1 })
    @Context(context = { Context.REST })
    @Test(groups = { "Smoke", "GAT", "RV_WORKFLOW" }, dataProvider = "createMeContextAndManagedElement", dataProviderClass = CMRestToolData.class)
    public void createDuplicateMeContextAndManagedElement(final Host restServer, final String command, final String expectedResponse) {
        setTestCase("createDuplicateMeContextAndManagedElement ", "Host Id is " + restServer.getIp());
        final List<String> postResponse = sendRequest(restServer, command);
        final String postResultString = postResponse.get(0);
        assertTrue("Expected a status code of -2 for test (createDuplicateMeContext)" + command,
                CMRestToolData.getcommandResponseDto(postResultString).getStatusCode() == -2);
        /*
         * assertTrue("Expected error as command is trying to create duplicate" + command,
         * CMRestToolData.getcommandResponseDto(postResultString).getStatusMessage (). equals(
         * "Child of type ManagedElement with name epatmah3 already exists under the parent MO MeContext=123456" ));
         */

    }

    /**
     * Verify that running a create command to try to create a duplicate ENodeBFunction returns a status code error.
     * 
     * @DESCRIPTION Verify that running a create command to try to create a duplicate ENodeBFunction returns a status code error.
     * @PRIORITY HIGH
     * @GROUP
     * @VUsers
     * @Context Rest
     */
    @VUsers(vusers = { 1 })
    @Context(context = { Context.REST })
    @Test(groups = { "Smoke", "GAT", "RV_WORKFLOW" }, dataProvider = "create_MeContext_And_Managed_Element_And_ENodeBFunction", dataProviderClass = CMRestToolData.class)
    public void create_Duplicate_MeContext_And_Managed_Element_And_ENodeBFunction(final Host restServer, final String command,
                                                                                  final String expectedResponse) {
        setTestCase("create_Duplicate_MeContext_And_Managed_Element_And_ENodeBFunction ", "Host Id is " + restServer.getIp());
        final List<String> postResponse = sendRequest(restServer, command);
        assertTrue("Expected a status code of -2 for test (createDuplicateMeContext)" + command,
                CMRestToolData.getcommandResponseDto(postResponse.get(0)).getStatusCode() == -2);
        /*
         * assertTrue("Expected error as command is trying to create duplicate" + command,
         * CMRestToolData.getcommandResponseDto(postResult.get(0)).getStatusMessage (). equals(
         * "Child of type ENodeBFunction with name 78910 already exists under the parent MO MeContext=123456,ManagedElement=epatmah3" ));
         */
    }

    /**
     * Verify that attempting to create a ManagedElement with an invalid root (MeContext) results in a status code error.
     * 
     * @DESCRIPTION Verify that attempting to create a ManagedElement with an invalid root (MeContext) results in a status code error.
     * @PRIORITY HIGH
     * @GROUP
     * @VUsers
     * @Context Rest
     */
    @VUsers(vusers = { 1 })
    @Context(context = { Context.REST })
    @Test(groups = { "Smoke", "GAT", "RV_WORKFLOW" }, dataProvider = "createMeContextAndManagedElementWithInvalidParent", dataProviderClass = CMRestToolData.class)
    public void createMeContextAndManagedElementWithInvalidParent(final Host restServer, final String command, final String expectedResponse) {
        setTestCase("createMeContextAndManagedElementWithInvalidParent ", "Host Id is " + restServer.getIp());
        final List<String> postResponse = sendRequest(restServer, command);
        final String postResultString = postResponse.get(0);
        assertTrue("Expected a status code of -1 for test (createMeContextAndManagedElementWithInvalidParent)" + command, CMRestToolData
                .getcommandResponseDto(postResultString).getStatusCode() == -1);
    }

    /**
     * Verify that attempting to get a specific MeContext which does not exist (while others do exist) results in a status code error.
     * 
     * @DESCRIPTION Verify that attempting to get a specific MeContext which does not exist (while others do exist) results in a status code error.
     * @PRIORITY HIGH
     * @GROUP
     * @VUsers
     * @Context Rest
     */
    @VUsers(vusers = { 1 })
    @Context(context = { Context.REST })
    @Test(groups = { "Smoke", "GAT", "RV_WORKFLOW" }, dataProvider = "getMeContextWhichDoesNotExist", dataProviderClass = CMRestToolData.class)
    public void getMeContextWhichDoesNotExist(final Host restServer, final String command, final String expectedResponse) {
        setTestCase("getMeContextWhichDoesNotExist ", "Host Id is " + restServer.getIp());
        final List<String> postResponse = sendRequest(restServer, command);
        final String postResultString = postResponse.get(0);
        assertTrue("Expected a status code of -1 for test (getMeContextWhichDoesNotExist)" + command,
                CMRestToolData.getcommandResponseDto(postResultString).getStatusCode() == -1);
    }

    /**
     * Verify JIRA service requires authentication for the calls
     * 
     * @throws IOException
     * @throws JsonMappingException
     * @throws JsonParseException
     * @throws JSONException
     * 
     * @DESCRIPTION Goal of the test case is to verify that JIRA service is preventing access when incorrect credentials are passed and allowing to
     *              get data if proper credentials are passed
     * @PRIORITY MEDIUM
     * @GROUP Smoke, GAT, RV_WORKFLOW
     * @VUsers 1,5
     * @Context Rest
     */
    /*
     * @VUsers(vusers = { 1 })
     * 
     * @Context(context = { Context.REST })
     * 
     * @Test(groups = { "Smoke", "GAT", "RV_WORKFLOW" }, dataProvider = "createENodeBFunctionChildMO", dataProviderClass = CMRestToolData.class)
     * public void createChildMOOfEnodeBFunction(Host restServer, String command, String expectedResponse) { //TODO figure out why this test case
     * won't run setTestCase( "get_MeContext_And_ManagedElement_And_ENodeBFunction_Expecting_True_As_Result " , "Host Id is " + restServer.getIp());
     * List<String> postResponse = sendRequest(restServer, command); HashMap<String, String> actualResult =
     * CMRestToolData.getActualResultAndPlaceInMap(postResponse); HashMap<String, String> expectedResultMap =
     * CMRestToolData.getExpectedResultsMapForENodeBFunctionChildMoCommand(); assertTrue("Expected Response does not match for input of " + command,
     * postResponse.get(0).contains(expectedResponse)); assertTrue("FDN doesn't Match expected result",
     * actualResult.get("FDN").equals(expectedResultMap.get("FDN"))); assertTrue("eNodeBPlmnId doesn't Match expected result", actualResult.get(
     * "eNodeBPlmnId").equals(expectedResultMap.get("eNodeBPlmnId"))); }
     */
    /**
     * Verify that it is possible to create an ENodeBFunction with Complex attributes (assumes existence of root MeContext and supporting
     * ManagedElement (existence of which is tested in earlier tests).
     * 
     * @DESCRIPTION Verify that it is possible to create an ENodeBFunction with Complex attributes (assumes existence of root MeContext and supporting
     *              ManagedElement (existence of which is tested in earlier tests).
     * @PRIORITY HIGH
     * @GROUP GAT
     * @VUsers
     * @Context Rest
     */

    @VUsers(vusers = { 1 })
    @Context(context = { Context.REST })
    @Test(groups = { "Smoke", "GAT", "RV_WORKFLOW" }, dataProvider = "createENodeBFunctionWithComplexAttributes", dataProviderClass = CMRestToolData.class)
    public void createENodeBFunctionWithComplexAttributes(final Host restServer, final String command, final String expectedResponse) {
        setTestCase("createENodeBFunctionWithComplexAttributes", "Host Id is " + restServer.getIp());
        final List<String> postResponse = sendRequest(restServer, command);
        final HashMap<String, String> actualResultMap = CMRestToolData.getActualResultAndPlaceInMap(postResponse);
        final HashMap<String, String> expectedResultMap = CMRestToolData.getCreateENodeBFunctionWithComplexAttributes();
        assertTrue("Expected Response does not match for input of " + command, postResponse.get(0).contains(expectedResponse));
        assertTrue("FDN doesn't Match expected result", actualResultMap.get("FDN").equals(expectedResultMap.get("FDN")));
        assertTrue("eNodeBPlmnId doesn't Match expected result", actualResultMap.get("eNodeBPlmnId").equals(expectedResultMap.get("eNodeBPlmnId")));

    }

    /**
     * Verify that it is possible to run a get command with a wildcard (star) returning all ENodeBFunction instances
     * 
     * @DESCRIPTION Verify that it is possible to run a get command with a wildcard (star) returning all ENodeBFunction instances
     * @PRIORITY HIGH
     * @GROUP GAT
     * @VUsers
     * @Context Rest
     */

    @VUsers(vusers = { 1 })
    @Context(context = { Context.REST })
    @Test(groups = { "Smoke", "GAT", "RV_WORKFLOW" }, dataProvider = "getStarENodeBFunction", dataProviderClass = CMRestToolData.class)
    public void getStarENodeBFunction(final Host restServer, final String command, final String expectedResponse) {
        setTestCase("getStarENodeBFunction ", "Host Id is " + restServer.getIp());
        final List<String> postResponse = sendRequest(restServer, command);
        final HashMap<String, HashMap<String, String>> actualResultMap = CMRestToolData
                .getActualResultAndPlaceInMapForMultipleMoResponse(postResponse);
        final String[] expectedResults = CMRestToolData.getExpectedResultForGetStarENodeBFunction();
        compareActualandExpectedResults(actualResultMap, expectedResults);
    }

    /**
     * Verify that it is possible to run a get command for a specific ENodeBFunction instance, when multiple instances exist
     * 
     * @DESCRIPTION Verify that it is possible to run a get command for a specific ENodeBFunction instance, when multiple instances exist
     * @PRIORITY HIGH
     * @GROUP GAT
     * @VUsers
     * @Context Rest
     */

    @VUsers(vusers = { 1 })
    @Context(context = { Context.REST })
    @Test(groups = { "Smoke", "GAT", "RV_WORKFLOW" }, dataProvider = "getSpecificENodeBFunction", dataProviderClass = CMRestToolData.class)
    public void getSpecificENodeBFunction(final Host restServer, final String command, final String expectedResponse) {
        setTestCase("getSpecificENodeBFunction ", "Host Id is " + restServer.getIp());
        final List<String> postResponse = sendRequest(restServer, command);
        final HashMap<String, HashMap<String, String>> actualResultMap = CMRestToolData
                .getActualResultAndPlaceInMapForMultipleMoResponse(postResponse);
        final String[] expectedResults = CMRestToolData.getExpectedResultForGetSpecificENodeBFunction();
        compareActualandExpectedResults(actualResultMap, expectedResults);
    }

    /**
     * Verify that it is possible to run a get command for a partial wildcard of ENodeBFunction instance (eg *ERBS) when multiple instances exist
     * 
     * @DESCRIPTION Verify that it is possible to run a get command for a partial wildcard of ENodeBFunction instance (eg *ERBS) when multiple
     *              instances exist
     * @PRIORITY HIGH
     * @GROUP GAT
     * @VUsers
     * @Context Rest
     */

    @VUsers(vusers = { 1 })
    @Context(context = { Context.REST })
    @Test(groups = { "Smoke", "GAT", "RV_WORKFLOW" }, dataProvider = "getENodeBFunctionNameFollowedByStar", dataProviderClass = CMRestToolData.class)
    public void getENodeBFunctionNameFollowedByStar(final Host restServer, final String command, final String expectedResponse) {
        setTestCase("getENodeBFunctionNameFollowedByStar ", "Host Id is " + restServer.getIp());
        final List<String> postResponse = sendRequest(restServer, command);
        final HashMap<String, HashMap<String, String>> actualResultMap = CMRestToolData
                .getActualResultAndPlaceInMapForMultipleMoResponse(postResponse);
        final String[] expectedResults = CMRestToolData.getExpectedResultForGetENodeBFunctionNameFollowedByStar();
        compareActualandExpectedResults(actualResultMap, expectedResults);
    }

    /**
     * Verify that it is possible to run a get command for a partial wildcard of ENodeBFunction instance (eg ERBS*) when multiple instances exist
     * 
     * @DESCRIPTION Verify that it is possible to run a get command for a partial wildcard of ENodeBFunction instance (eg ERBS*) when multiple
     *              instances exist
     * @PRIORITY HIGH
     * @GROUP GAT
     * @VUsers
     * @Context Rest
     */

    @VUsers(vusers = { 1 })
    @Context(context = { Context.REST })
    @Test(groups = { "Smoke", "GAT", "RV_WORKFLOW" }, dataProvider = "getENodeBFunctionNamePreceededByStar", dataProviderClass = CMRestToolData.class)
    public void getENodeBFunctionNamePreceededByStar(final Host restServer, final String command, final String expectedResponse) {
        setTestCase("getENodeBFunctionNamePreceededByStar ", "Host Id is " + restServer.getIp());
        final List<String> postResponse = sendRequest(restServer, command);
        final HashMap<String, HashMap<String, String>> actualResultMap = CMRestToolData
                .getActualResultAndPlaceInMapForMultipleMoResponse(postResponse);
        final String[] expectedResults = CMRestToolData.getExpectedResultForGetENodeBFunctionNamePreceededByStar();
        compareActualandExpectedResults(actualResultMap, expectedResults);
    }

    /**
     * Verify that it is possible to run a get command for an ENodeBFunction with a dot star (.*) wildcard after it to indicate all attributes should
     * be returned.
     * 
     * @DESCRIPTION Verify that it is possible to run a get command for an ENodeBFunction with a dot star (.*) wildcard after it to indicate all
     *              attributes should be returned.
     * @PRIORITY HIGH
     * @GROUP GAT
     * @VUsers
     * @Context Rest
     */

    @VUsers(vusers = { 1 })
    @Context(context = { Context.REST })
    @Test(groups = { "Smoke", "GAT", "RV_WORKFLOW" }, dataProvider = "getENodeBFunctionDotStar", dataProviderClass = CMRestToolData.class)
    public void getENodeBFunctionDotStar(final Host restServer, final String command, final String expectedResponse) {
        setTestCase("getStarENodeBFunction ", "Host Id is " + restServer.getIp());
        final List<String> postResponse = sendRequest(restServer, command);
        final HashMap<String, HashMap<String, String>> actualResultMap = CMRestToolData
                .getActualResultAndPlaceInMapForMultipleMoResponse(postResponse);
        final HashMap<String, HashMap<String, String>> expectedEnodeBFunctionResultMap = CMRestToolData
                .getExpectedEnodeBFunctionIdsForGetENodeBFunctionDotStar();
        final String[] expectedResults = CMRestToolData.getExpectedResultForGetENodeBFunctionDotStar();
        compareActualandExpectedResults(actualResultMap, expectedResults);
        compareEnodeBfunctionIds(actualResultMap, expectedResults, expectedEnodeBFunctionResultMap);
    }

    /**
     * Verify that it is possible to run a get command for an ENodeBFunction and specify which attributes should be returned
     * 
     * @DESCRIPTION Verify that it is possible to run a get command for an ENodeBFunction and specify which attributes should be returned
     * @PRIORITY HIGH
     * @GROUP GAT
     * @VUsers
     * @Context Rest
     */

    @VUsers(vusers = { 1 })
    @Context(context = { Context.REST })
    @Test(groups = { "Smoke", "GAT", "RV_WORKFLOW" }, dataProvider = "getENodeBFunctionDotAttributes", dataProviderClass = CMRestToolData.class)
    public void getENodeBFunctionDotAttributes(final Host restServer, final String command, final String expectedResponse) {
        setTestCase("getENodeBFunctionDotAttributes ", "Host Id is " + restServer.getIp());
        final List<String> postResponse = sendRequest(restServer, command);
        final HashMap<String, HashMap<String, String>> actualResultMap = CMRestToolData
                .getActualResultAndPlaceInMapForMultipleMoResponse(postResponse);
        final HashMap<String, HashMap<String, String>> expectedEnodeBFunctionAndDscpLabelsResultMap = CMRestToolData
                .getExpectedEnodeBFunctionIdsAndDscpLabelsForGetENodeBFunctionDotAttributes();
        final String[] expectedResults = CMRestToolData.getExpectedResultForENodeBFunctionDotAttributes();
        compareActualandExpectedResults(actualResultMap, expectedResults);
        compareSelectedAttributes(actualResultMap, expectedResults, expectedEnodeBFunctionAndDscpLabelsResultMap);
    }

    /**
     * Verify that it is possible to run a get command for an ENodeBFunction and specify which attributes should be returned when they match a
     * specific value
     * 
     * @DESCRIPTION Verify that it is possible to run a get command for an ENodeBFunction and specify which attributes should be returned when they
     *              match a specific value
     * @PRIORITY HIGH
     * @GROUP GAT
     * @VUsers
     * @Context Rest
     */

    @VUsers(vusers = { 1 })
    @Context(context = { Context.REST })
    @Test(groups = { "Smoke", "GAT", "RV_WORKFLOW" }, dataProvider = "getENodeBFunctionDotAttributeEqualsValueVerifyingUserLabelAndDnsLookupTimerCombined", dataProviderClass = CMRestToolData.class)
    public void getENodeBFunctionDotAttributeEqualsValueVerifyingUserLabelAndDnsLookupTimerCombined(final Host restServer, final String command,
                                                                                                    final String expectedResponse) {
        setTestCase("getENodeBFunctionDotAttributeEqualsValueVerifyingUserLabelAndDnsLookupTimerCombined ", "Host Id is " + restServer.getIp());
        final List<String> postResponse = sendRequest(restServer, command);
        final HashMap<String, HashMap<String, String>> actualResultMap = CMRestToolData
                .getActualResultAndPlaceInMapForMultipleMoResponse(postResponse);
        final HashMap<String, HashMap<String, String>> expectedDotAttributesEqualsResultMap = CMRestToolData
                .getExpectedDnsLookupOnTaiForGetENodeBFunctionDotAttributeEqualsValue();
        final String[] expectedResults = CMRestToolData.getExpectedResultForENodeBFunctionDotAttributes();
        compareActualandExpectedResults(actualResultMap, expectedResults);
        CompareAnySelectedAttributes(actualResultMap, expectedResults, expectedDotAttributesEqualsResultMap, "userLabel");
        CompareAnySelectedAttributes(actualResultMap, expectedResults, expectedDotAttributesEqualsResultMap, "dnsLookupTimer");
    }

    public List<String> sendRequest(final Host restServer, final String command) {
        final RestTool RT = new RestTool(restServer);
        final Map<String, String> formData = new HashMap<String, String>();
        formData.put("command", "cmedit " + command);
        final List<String> postResponse = RT.postForm("script-engine/services/commandRequest/", formData, false);
        assertTrue("No post result", postResponse.size() > 0);
        return postResponse;
    }

    public void compareActualandExpectedResults(final HashMap<String, HashMap<String, String>> actualResultMap, final String[] expectedResults) {
        for (final String expectedResult : expectedResults) {
            HashMap<String, String> individualMap = new HashMap<String, String>();
            individualMap = actualResultMap.get(expectedResult);
            assertTrue("No Contents in individual map", individualMap.size() > 0);
            assertTrue("FDN doesn't Match expected result", individualMap.get("FDN").equals(expectedResult));
        }
    }

    public void compareEnodeBfunctionIds(final HashMap<String, HashMap<String, String>> actualResultMap, final String[] expectedResults,
                                         final HashMap<String, HashMap<String, String>> ExpectedEnodeBFunctionResultMap) {
        for (final String expectedResult : expectedResults) {
            HashMap<String, String> individualMapActual = new HashMap<String, String>();
            individualMapActual = actualResultMap.get(expectedResult);
            HashMap<String, String> individualMapExpected = new HashMap<String, String>();
            individualMapExpected = ExpectedEnodeBFunctionResultMap.get(expectedResult);
            assertTrue("No Contents in individual map of Actual results", individualMapActual.size() > 0);
            assertTrue("No Contents in individual map of Expected results", individualMapExpected.size() > 0);
            assertTrue("FDN doesn't Match expected result",
                    individualMapActual.get("ENodeBFunctionId").equals(individualMapExpected.get("ENodeBFunctionId")));
        }
    }

    public void compareSelectedAttributes(final HashMap<String, HashMap<String, String>> actualResultMap, final String[] expectedResults,
                                          final HashMap<String, HashMap<String, String>> ExpectedEnodeBFunctionResultMap) {
        for (final String expectedResult : expectedResults) {
            HashMap<String, String> individualMapActual = new HashMap<String, String>();
            individualMapActual = actualResultMap.get(expectedResult);
            HashMap<String, String> individualMapExpected = new HashMap<String, String>();
            individualMapExpected = ExpectedEnodeBFunctionResultMap.get(expectedResult);
            assertTrue("No Contents in individual map of Actual results", individualMapActual.size() > 0);
            assertTrue("No Contents in individual map of Expected results", individualMapExpected.size() > 0);
            assertTrue("ENodeBFunctionId doesn't Match expected result",
                    individualMapActual.get("ENodeBFunctionId").equals(individualMapExpected.get("ENodeBFunctionId")));
            assertTrue("dscpLabel doesn't Match expected result", individualMapActual.get("dscpLabel").equals(individualMapExpected.get("dscpLabel")));
        }
    }

    public void CompareAnySelectedAttributes(final HashMap<String, HashMap<String, String>> actualResultMap, final String[] expectedResults,
                                             final HashMap<String, HashMap<String, String>> ExpectedEnodeBFunctionResultMap,
                                             final String attributeToCheck) {
        for (final String expectedResult : expectedResults) {
            HashMap<String, String> individualMapActual = new HashMap<String, String>();
            individualMapActual = actualResultMap.get(expectedResult);
            HashMap<String, String> individualMapExpected = new HashMap<String, String>();
            individualMapExpected = ExpectedEnodeBFunctionResultMap.get(expectedResult);

            assertTrue("No Contents in individual map of Actual results", individualMapActual.size() > 0);
            assertTrue("No Contents in individual map of Expected results", individualMapExpected.size() > 0);
            assertTrue("attributeToCheck doesn't Match expected result",
                    individualMapActual.get(attributeToCheck).equals(individualMapExpected.get(attributeToCheck)));

        }
    }

    /**
     * Verify in the UI that it is possible to run a command and have data returned, also verify that it is possible to go back through command
     * history (up arrow) and get original command.
     * 
     * @DESCRIPTION Verify in the UI that it is possible to run a command and have data returned, also verify that it is possible to go back through
     *              command history (up arrow) and get original command.
     * @PRIORITY HIGH
     * @GROUP
     * @VUsers
     * @Context Rest
     */
    @VUsers(vusers = { 1 })
    @Context(context = { Context.REST })
    @Test(groups = { "Smoke", "GAT", "RV_WORKFLOW" }, dataProvider = "checkCommandCanBeEnteredViaUIandRecievesResponse", dataProviderClass = CMRestToolData.class)
    public void checkCommandCanBeEnteredViaUIandRecievesResponse(final Host apacheServer) {
        final String MeContextName = "UIMeContext1";
        final String command = "cmedit create MeContext=" + MeContextName + " MeContextId=" + MeContextName
                + ", neType=ENODEB -namespace=OSS_TOP -version=1.1.0";
        final Browser firefoxBrowser = UI.newBrowser(BrowserType.FIREFOX);
        final BrowserTab BrowserTab = firefoxBrowser.open("http://" + apacheServer.getIp() + "/#cliapp");
        final ViewModel viewModel = BrowserTab.getGenericView();
        final UiComponent userNameTextBox = viewModel.getViewComponent(SelectorType.XPATH, ".//*[@id='loginUsername']", UiComponent.class);
        final UiComponent passwordTextBox = viewModel.getViewComponent(SelectorType.XPATH, ".//*[@id='loginPassword']", UiComponent.class);
        userNameTextBox.sendKeys("UserName");
        passwordTextBox.sendKeys("Password");
        final UiComponent submitButton = viewModel.getViewComponent(SelectorType.XPATH, ".//*[@id='submit']", UiComponent.class);
        submitButton.click();
        sleep(5);
        final UiComponent cliInput = viewModel.getViewComponent(SelectorType.XPATH, ".//*[@id='cliInput']", UiComponent.class);
        cliInput.sendKeys(command + Keys.RETURN);
        sleep(5);
        final UiComponent fdnInResponse = viewModel.getViewComponent(SelectorType.XPATH, ".//*[@class='eaCliApp-listWidget-list']//li[1]",
                UiComponent.class);
        sleep(5);
        assertTrue("FDN does not match expected output " + fdnInResponse.getText(),
                fdnInResponse.getText().contains("FDN : MeContext=" + MeContextName));
        sleep(5);
        cliInput.sendKeys(Keys.ARROW_UP);
        sleep(5);
        assertTrue("Up arrow not browsing through command history as expected", cliInput.getText().equals(command));
    }

    /**
     * Verify that it is possible to run a get command with no namespace returning all ENodeBFunction instances
     * 
     * @DESCRIPTION Verify that it is possible to run a get command with no namespace returning all ENodeBFunction instances
     * @PRIORITY HIGH
     * @GROUP GAT
     * @VUsers
     * @Context Rest
     */

    @VUsers(vusers = { 1 })
    @Context(context = { Context.REST })
    @Test(groups = { "Smoke", "GAT", "RV_WORKFLOW" }, dataProvider = "getStarENodeBFunctionNoNameSpace", dataProviderClass = CMRestToolData.class)
    public void getStarENodeBFunctionNoNameSpace(final Host restServer, final String command, final String expectedResponse) {
        setTestCase("getStarENodeBFunctionNoNameSpace ", "Host Id is " + restServer.getIp());
        final List<String> postResponse = sendRequest(restServer, command);
        final HashMap<String, HashMap<String, String>> actualResultMap = CMRestToolData
                .getActualResultAndPlaceInMapForMultipleMoResponse(postResponse);
        final String[] expectedResults = CMRestToolData.getExpectedResultForGetStarENodeBFunction();
        compareActualandExpectedResults(actualResultMap, expectedResults);
    }

    /*
     * Following Test Cases added for Team America by EFIAHAN
     * 
     * 
     * Following Test Cases added for Team America User Story TORF-8460
     */

    /**
     * Verify that it is possible to run a get command with namespace and version returning all ENodeBFunction instances
     * 
     * @DESCRIPTION Verify that it is possible to run a get command with no namespace and version returning all ENodeBFunction instances
     * @PRIORITY HIGH
     * @GROUP GAT
     * @VUsers
     * @Context Rest
     */

    @VUsers(vusers = { 1 })
    @Context(context = { Context.REST })
    @Test(groups = { "Smoke", "GAT", "RV_WORKFLOW" }, dataProvider = "getENodeBFunctionNameWithNamepsaceAndVersion", dataProviderClass = CMRestToolData.class)
    public void getENodeBFunctionNameWithNamepsaceAndVersion(final Host restServer, final String command, final String expectedResponse) {
        setTestCase("getENodeBFunctionNameWithNamepsaceAndVersion ", "Host Id is " + restServer.getIp());
        final List<String> postResponse = sendRequest(restServer, command);
        final HashMap<String, HashMap<String, String>> actualResultMap = CMRestToolData
                .getActualResultAndPlaceInMapForMultipleMoResponse(postResponse);
        final String[] expectedResults = CMRestToolData.getExpectedResultForGetENodeBFunctionNameWithNamepsaceAndVersion();
        compareActualandExpectedResults(actualResultMap, expectedResults);
    }

    /*
     * Following Test Cases added for Team America by EFIAHAN
     * 
     * 
     * Following Test Cases added for Team America User Story TORF-1348
     */

    /**
     * Verify that it is possible to run a get command for multiple nodes with no namespace/version that will return MOIs under multiple node ids
     * 
     * @DESCRIPTION Verify that it is possible to run a get command for multiple nodes with no namespace/version that will return MOIs under multiple
     *              node ids
     * @PRIORITY HIGH
     * @GROUP GAT
     * @VUsers
     * @Context Rest
     */

    @VUsers(vusers = { 1 })
    @Context(context = { Context.REST })
    @Test(groups = { "Smoke", "GAT", "RV_WORKFLOW" }, dataProvider = "getMultipleNodeIds", dataProviderClass = CMRestToolData.class)
    public void getMultipleNodeIds(final Host restServer, final String command, final String expectedResponse) {
        setTestCase("getMultipleNodeIds ", "Host Id is " + restServer.getIp());
        final List<String> postResponse = sendRequest(restServer, command);
        final String postResultString = postResponse.get(0);
        final String[] expectedResults = CMRestToolData.getExpectedResultForGetMultipleMos();
        for (final String expectedResult : expectedResults) {
            assertTrue("Response did not contain expected result " + expectedResult, postResultString.contains(expectedResult));
        }
    }

    /*
     * Following Test Case added for Team America by EPATMAH
     * 
     * 
     * Following Test Cases added for Team America User Story TORF-6889
     */

    @VUsers(vusers = { 1 })
    @Context(context = { Context.REST })
    @Test(groups = { "Smoke", "GAT", "RV_WORKFLOW" }, dataProvider = "getOneOrMoreChildMOIs", dataProviderClass = CMRestToolData.class)
    public void getOneOrMoreChildMOIs(final Host restServer, final String command, final String expectedResponse) {
        setTestCase("getOneOrMoreChildMOIs ", "Host Id is " + restServer.getIp());
        sendRequest(
                restServer,
                "create MeContext=123456,ManagedElement=epatmah3,ENodeBFunction=78910,EUtranCellFDD=1 EUtranCellFDDId=1,physicalLayerCellIdGroup=1, cellId=1, earfcndl=1, tac=65535, earfcnul=18000, physicalLayerSubCellId=1, sectorFunctionRef=\"MeContext=123456,ManagedElement=1\" -namespace=ERBS_NODE_MODEL -version=3.1.72");
        sendRequest(
                restServer,
                "create MeContext=123abc456,ManagedElement=epatmah3,ENodeBFunction=2,EUtranCellFDD=1 EUtranCellFDDId=1,physicalLayerCellIdGroup=1, cellId=1, earfcndl=1, tac=65535, earfcnul=18000, physicalLayerSubCellId=1, sectorFunctionRef=\"MeContext=123456,ManagedElement=1\" -namespace=ERBS_NODE_MODEL -version=3.1.72");
        final List<String> postResponse = sendRequest(restServer, command);
        final String postResultString = postResponse.get(0);
        final String[] expectedResults = CMRestToolData.getExpectedResultForGetMultipleChildMOIs();
        for (final String expectedResult : expectedResults) {
            assertTrue("Response did not contain expected result " + expectedResult, postResultString.contains(expectedResult));
        }
    }

    /*
     * Following Test Cases added for Team America by EFIAHAN
     * 
     * 
     * Following Test Cases added for Team America User Story TORF-8459
     */

    /**
     * Verify that it is possible to run a get command with a wildcard (star) returning all ENodeBFunction instances in a table format
     * 
     * @DESCRIPTION Verify that it is possible to run a get command with a wildcard (star) returning all ENodeBFunction instances in a table format
     * @PRIORITY HIGH
     * @GROUP GAT
     * @VUsers
     * @Context Rest
     */

    @VUsers(vusers = { 1 })
    @Context(context = { Context.REST })
    @Test(groups = { "Smoke", "GAT", "RV_WORKFLOW" }, dataProvider = "getStarENodeBFunction", dataProviderClass = CMRestToolData.class)
    public void getStarENodeBFunctionTableFormat(final Host restServer, final String command, final String expectedResponse) {
        setTestCase("getStarENodeBFunctionTableFormat ", "Host Id is " + restServer.getIp());
        final List<String> postResponse = sendRequest(restServer, command);
        final String response = postResponse.get(0);
        assertTrue("Unable to verify response was a table response", response.contains("Table Result"));
        //        final HashMap<String, HashMap<String, String>> actualResultMap = CMRestToolData
        //              .getActualResultAndPlaceInMapForMultipleMoResponse(postResponse);
        //    final String[] expectedResults = CMRestToolData.getExpectedResultForGetStarENodeBFunction();
        //  compareActualandExpectedResults(actualResultMap, expectedResults);
    }

    /*
     * Following Test Cases added for Team KAOS by EEIRSH
     * 
     * 
     * Following Test Cases added for KAOS User Story TORF-1365
     */

    /**
     * Verify SET command for setting a single Attribute value
     * 
     * @DESCRIPTION Goal of the test case is to verify that it is possible for the user to specify a SET command to set/modify a single attribute. In
     *              this test case the 'generationCounter' attribute is set.
     * @PRIORITY MEDIUM
     * @GROUP Smoke, GAT, RV_WORKFLOW
     * @VUsers 1,5
     * @Context Rest
     */

    @VUsers(vusers = { 1 })
    @Context(context = { Context.REST })
    @Test(groups = { "Smoke", "GAT", "RV_WORKFLOW" }, dataProvider = "setMeContextGenerationCounter", dataProviderClass = CMRestToolData.class)
    public void setMeContextGenerationCounter(final Host restServer, final String command, final String expectedResponse) {
        setTestCase("setMeContextGenerationCounter ", "Host Id is " + restServer.getIp());
        final List<String> postResponse = sendRequest(restServer, command);
        final HashMap<String, String> actualResult = CMRestToolData.getActualResultAndPlaceInMap(postResponse);
        final HashMap<String, String> expectedResultMap = CMRestToolData.getExpectedResultsMapForsetMeContextCommandGenerationCounter();
        assertTrue("Expected Response does not match for input of " + command, postResponse.get(0).contains(expectedResponse));
        assertTrue("Set Attribute for generationCounter does not match expected result",
                actualResult.get("generationCounter").equals(expectedResultMap.get("generationCounter")));
    }

    /**
     * Verify SET command for setting a single Attribute to the same value it currently contains
     * 
     * @DESCRIPTION Goal of the test case is to verify that it is possible for the user to specify a SET command to set/modify a single attribute to
     *              the same value it currently contains. In this test case the 'generationCounter' attribute is set.
     * @PRIORITY MEDIUM
     * @GROUP Smoke, GAT, RV_WORKFLOW
     * @VUsers 1,5
     * @Context Rest
     */
    @VUsers(vusers = { 1 })
    @Context(context = { Context.REST })
    @Test(groups = { "Smoke", "GAT", "RV_WORKFLOW" }, dataProvider = "setDuplicateMeContextAttributeGenerationCounterToSameValue", dataProviderClass = CMRestToolData.class)
    public void setDuplicateMeContextAttributeGenerationCounterToSameValue(final Host restServer, final String command, final String expectedResponse) {
        setTestCase("setDuplicateMeContextAttributeGenerationCounterToSameValue ", "Host Id is " + restServer.getIp());
        final List<String> postResponse = sendRequest(restServer, command);
        final HashMap<String, String> actualResult = CMRestToolData.getActualResultAndPlaceInMap(postResponse);
        final HashMap<String, String> expectedResultMap = CMRestToolData
                .getExpectedResultsMapForSetDuplicateMeContextAttributeGenerationCounterToSameValue();
        assertTrue("Expected Response does not match for input of " + command, postResponse.get(0).contains(expectedResponse));
        assertTrue("Set Attribute for generationCounter does not match expected result",
                actualResult.get("generationCounter").equals(expectedResultMap.get("generationCounter")));
    }

    /**
     * Verify SET command for setting a single Attribute value
     * 
     * @DESCRIPTION Goal of the test case is to verify that it is possible for the user to specify a SET command to set/modify a single attribute. In
     *              this test case the 'userLabel' attribute is set.
     * @PRIORITY MEDIUM
     * @GROUP Smoke, GAT, RV_WORKFLOW
     * @VUsers 1,5
     * @Context Rest
     */
    @VUsers(vusers = { 1 })
    @Context(context = { Context.REST })
    @Test(groups = { "Smoke", "GAT", "RV_WORKFLOW" }, dataProvider = "setMeContextAttributeUserLabel", dataProviderClass = CMRestToolData.class)
    public void setMeContextAttributeUserLabel(final Host restServer, final String command, final String expectedResponse) {
        setTestCase("setMeContextAttributeUserLabel ", "Host Id is " + restServer.getIp());
        final List<String> postResponse = sendRequest(restServer, command);
        final HashMap<String, String> actualResult = CMRestToolData.getActualResultAndPlaceInMap(postResponse);
        final HashMap<String, String> expectedResultMap = CMRestToolData.getExpectedResultsMapForSetMeContextAttributeUserLabel();
        assertTrue("Expected Response does not match for input of " + command, postResponse.get(0).contains(expectedResponse));
        assertTrue("Set Attribute userLabel result does not match expected result",
                actualResult.get("userLabel").equals(expectedResultMap.get("userLabel")));
    }

    /**
     * Verify SET command for setting a single Attribute value to a null string value
     * 
     * @DESCRIPTION Goal of the test case is to verify that it is possible for the user to specify a SET command to set/modify a single attribute. In
     *              this test case the 'userLabel' attribute is set to a null value.
     * @PRIORITY MEDIUM
     * @GROUP Smoke, GAT, RV_WORKFLOW
     * @VUsers 1,5
     * @Context Rest
     */
    @VUsers(vusers = { 1 })
    @Context(context = { Context.REST })
    @Test(groups = { "Smoke", "GAT", "RV_WORKFLOW" }, dataProvider = "setMeContextAttributeUserLabelToNullValue", dataProviderClass = CMRestToolData.class)
    public void setMeContextAttributeUserLabelToNullValue(final Host restServer, String command, final String expectedResponse) {
        // TODO to be investigated
        command = "set MeContext=123456 userLabel=\"\"";
        setTestCase("setMeContextAttributeUserLabelToNullValue ", "Host Id is " + restServer.getIp());
        final List<String> postResponse = sendRequest(restServer, command);
        final HashMap<String, String> actualResult = CMRestToolData.getActualResultAndPlaceInMap(postResponse);
        final HashMap<String, String> expectedResultMap = CMRestToolData.getExpectedResultsMapForSetMeContextCommandAttributeUserLabelToNullValue();
        assertTrue("Expected Response does not match for input of " + command, postResponse.get(0).contains(expectedResponse));

        assertTrue("Set Attribute userLabel result does not match expected result",
                actualResult.get("userLabel").equals(expectedResultMap.get("userLabel")));
    }

    /**
     * Verify SET command for setting a single Attribute value
     * 
     * @DESCRIPTION Goal of the test case is to verify that it is possible for the user to specify a SET command to set/modify a single attribute. In
     *              this test case the 'dnsLookupTimer' attribute is set.
     * @PRIORITY MEDIUM
     * @GROUP Smoke, GAT, RV_WORKFLOW
     * @VUsers 1,5
     * @Context Rest
     */

    @VUsers(vusers = { 1 })
    @Context(context = { Context.REST })
    @Test(groups = { "Smoke", "GAT", "RV_WORKFLOW" }, dataProvider = "setAttributednsLookupTimerOnENodeBFunction", dataProviderClass = CMRestToolData.class)
    public void setAttributednsLookupTimerOnENodeBFunction(final Host restServer, final String command, final String expectedResponse) {
        setTestCase("setAttributednsLookupTimerOnENodeBFunction ", "Host Id is " + restServer.getIp());
        final List<String> postResponse = sendRequest(restServer, command);
        final HashMap<String, String> actualResult = CMRestToolData.getActualResultAndPlaceInMap(postResponse);
        final HashMap<String, String> expectedResultMap = CMRestToolData.getExpectedResultsMapForSetCommandAttributednsLookupTimerOnENodeBFunction();
        assertTrue("Expected Response does not match for input of " + command, postResponse.get(0).contains(expectedResponse));
        assertTrue("Set Attribute dnsLookupTimer result does not match expected result",
                actualResult.get("dnsLookupTimer").equals(expectedResultMap.get("dnsLookupTimer")));
    }

    /*
     * Following Test Cases added for KAOS User Story TORF-1354
     */

    /**
     * Verify SET command for setting a list of Attributes
     * 
     * @DESCRIPTION Goal of the test case is to verify that it is possible for the user to specify a SET command to set/modify a list of attributes.
     * @PRIORITY MEDIUM
     * @GROUP Smoke, GAT, RV_WORKFLOW
     * @VUsers 1,5
     * @Context Rest
     */

    @VUsers(vusers = { 1 })
    @Context(context = { Context.REST })
    @Test(groups = { "Smoke", "GAT", "RV_WORKFLOW" }, dataProvider = "setMeContextListOfAttributes", dataProviderClass = CMRestToolData.class)
    public void setMeContextListOfAttributes(final Host restServer, final String command, final String expectedResponse) {
        setTestCase("setMeContextListOfAttributes ", "Host Id is " + restServer.getIp());
        final List<String> postResponse = sendRequest(restServer, command);
        final HashMap<String, String> actualResult = CMRestToolData.getActualResultAndPlaceInMap(postResponse);
        final HashMap<String, String> expectedResultMap = CMRestToolData.getExpectedResultsMapForSetMeContextCommandListOfAttributes();
        assertTrue("Expected Response does not match for input of " + command, postResponse.get(0).contains(expectedResponse));
        assertTrue("Set Attribute UserLabel result does not match expected result",
                actualResult.get("userLabel").equals(expectedResultMap.get("userLabel")));
        assertTrue("Set Attribute generationCounter result does not match expected result",
                actualResult.get("generationCounter").equals(expectedResultMap.get("generationCounter")));
        assertTrue("Set Attribute mirrorSynchronizationStatus result does not match expected result", actualResult.get("mirrorSynchronizationStatus")
                .equals(expectedResultMap.get("mirrorSynchronizationStatus")));

    }

    /**
     * Verify SET command for setting a list of Attributes to different values than previous test case. Attributes in different order.
     * 
     * @DESCRIPTION Goal of the test case is to verify that it is possible for the user to specify a SET command to set/modify a list of attributes.
     *              The attributes are set previously and order of attributes specified have been changed.
     * @PRIORITY MEDIUM
     * @GROUP Smoke, GAT, RV_WORKFLOW
     * @VUsers 1,5
     * @Context Rest
     */
    @VUsers(vusers = { 1 })
    @Context(context = { Context.REST })
    @Test(groups = { "Smoke", "GAT", "RV_WORKFLOW" }, dataProvider = "setMeContextDuplicateListOfReorderedAttributesToDifferentValues", dataProviderClass = CMRestToolData.class)
    public void setMeContextDuplicateListOfReorderedAttributesToDifferentValues(final Host restServer, final String command,
                                                                                final String expectedResponse) {
        setTestCase("setMeContextDuplicateListOfReorderedAttributesToDifferentValues ", "Host Id is " + restServer.getIp());
        final List<String> postResponse = sendRequest(restServer, command);
        final HashMap<String, String> actualResult = CMRestToolData.getActualResultAndPlaceInMap(postResponse);
        final HashMap<String, String> expectedResultMap = CMRestToolData
                .getExpectedResultsMapForSetMeContextCommandDuplicateListOfReorderedAttributesToDifferentValues();
        assertTrue("Expected Response does not match for input of " + command, postResponse.get(0).contains(expectedResponse));
        assertTrue("Set Attribute generationCounter result does not match expected result",
                actualResult.get("generationCounter").equals(expectedResultMap.get("generationCounter")));
        assertTrue("Set Attribute mirrorSynchronizationStatus result does not match expected result", actualResult.get("mirrorSynchronizationStatus")
                .equals(expectedResultMap.get("mirrorSynchronizationStatus")));
        assertTrue("Set Attribute UserLabel result does not match expected result",
                actualResult.get("userLabel").equals(expectedResultMap.get("userLabel")));
    }

    /**
     * Verify SET command for setting a list of Attributes of Child MO
     * 
     * @DESCRIPTION Goal of the test case is to verify that it is possible for the user to specify a SET command to set/modify a list of attributes on
     *              a Child MO
     * @PRIORITY MEDIUM
     * @GROUP Smoke, GAT, RV_WORKFLOW
     * @VUsers 1,5
     * @Context Rest
     */

    @VUsers(vusers = { 1 })
    @Context(context = { Context.REST })
    @Test(groups = { "Smoke", "GAT", "RV_WORKFLOW" }, dataProvider = "setListOfAttributesOnENodeBFunction", dataProviderClass = CMRestToolData.class)
    public void setListOfAttributesOnENodeBFunction(final Host restServer, final String command, final String expectedResponse) {
        setTestCase("setListOfAttributesOnENodeBFunction ", "Host Id is " + restServer.getIp());
        final List<String> postResponse = sendRequest(restServer, command);
        final HashMap<String, String> actualResult = CMRestToolData.getActualResultAndPlaceInMap(postResponse);
        final HashMap<String, String> expectedResultMap = CMRestToolData.getExpectedResultsMapForSetCommandListOfAttributesOnENodeBFunction();
        assertTrue("Expected Response does not match for input of " + command, postResponse.get(0).contains(expectedResponse));
        assertTrue("Set Attribute dnsLookupTimer result does not match expected result",
                actualResult.get("dnsLookupTimer").equals(expectedResultMap.get("dnsLookupTimer")));
        assertTrue("Set Attribute s1RetryTimer result does not match expected result",
                actualResult.get("s1RetryTimer").equals(expectedResultMap.get("s1RetryTimer")));
        assertTrue("Set Attribute UserLabel result does not match expected result",
                actualResult.get("userLabel").equals(expectedResultMap.get("userLabel")));
    }

    /*
     * Following Test Cases added for KAOS User Story TORF-5305
     */

    /**
     * Verify SET command for setting/modifying a Structured Complex Attribute
     * 
     * @DESCRIPTION Goal of the test case is to verify that it is possible for the user to specify a SET command to set/modify a Structured Complex
     *              Attribute
     * @PRIORITY MEDIUM
     * @GROUP Smoke, GAT, RV_WORKFLOW
     * @VUsers 1,5
     * @Context Rest
     */

    @VUsers(vusers = { 1 })
    @Context(context = { Context.REST })
    @Test(groups = { "Smoke", "GAT", "RV_WORKFLOW" }, dataProvider = "seteNodeBPlmnIdComplexAttribute", dataProviderClass = CMRestToolData.class)
    public void seteNodeBPlmnIdComplexAttribute(final Host restServer, final String command, final String expectedResponse) {
        setTestCase("seteNodeBPlmnIdComplexAttribute ", "Host Id is " + restServer.getIp());
        final List<String> postResponse = sendRequest(restServer, command);
        final HashMap<String, String> actualResult = CMRestToolData.getActualResultAndPlaceInMap(postResponse);
        final HashMap<String, String> expectedResultMap = CMRestToolData.getExpectedResultsMapForSetCommandeNodeBPlmnIdComplexAttribute();
        assertTrue("Expected Response does not match for input of " + command, postResponse.get(0).contains(expectedResponse));
        assertTrue("Set Complex Structured Attribute eNodeBPlmnId result does not match expected result",
                actualResult.get("eNodeBPlmnId").equals(expectedResultMap.get("eNodeBPlmnId")));
    }

    /**
     * Verify SET command for setting/modifying a Structured Complex Attribute from previously set values
     * 
     * @DESCRIPTION Goal of the test case is to verify that it is possible for the user to specify a SET command to set/modify a Structured Complex
     *              Attribute that has been already set in previous test case
     * @PRIORITY MEDIUM
     * @GROUP Smoke, GAT, RV_WORKFLOW
     * @VUsers 1,5
     * @Context Rest
     */
    @VUsers(vusers = { 1 })
    @Context(context = { Context.REST })
    @Test(groups = { "Smoke", "GAT", "RV_WORKFLOW" }, dataProvider = "seteNodeBPlmnIdComplexAttributeToDifferentValues", dataProviderClass = CMRestToolData.class)
    public void seteNodeBPlmnIdComplexAttributeToDifferentValues(final Host restServer, final String command, final String expectedResponse) {
        setTestCase("seteNodeBPlmnIdComplexAttributeToDifferentValues ", "Host Id is " + restServer.getIp());
        final List<String> postResponse = sendRequest(restServer, command);
        final HashMap<String, String> actualResult = CMRestToolData.getActualResultAndPlaceInMap(postResponse);
        final HashMap<String, String> expectedResultMap = CMRestToolData
                .getExpectedResultsMapForSetCommandeNodeBPlmnIdComplexAttributesToDifferentValues();
        assertTrue("Expected Response does not match for input of " + command, postResponse.get(0).contains(expectedResponse));
        assertTrue("Set Complex Structured Attribute eNodeBPlmnId result does not match expected result",
                actualResult.get("eNodeBPlmnId").equals(expectedResultMap.get("eNodeBPlmnId")));
    }

    /*
     * Following Test Cases added for KAOS User Story TORF-6582
     */

    /**
     * Verify SET command for setting/modifying an enumerated Attribute on an MO
     * 
     * @DESCRIPTION Goal of the test case is to verify that it is possible for the user to specify a SET command to set/modify an enumerated
     *              attribute. In this Test Cases the enumerated attribute,dnsLookupOnTai is set to value ON
     * @PRIORITY MEDIUM
     * @GROUP Smoke, GAT, RV_WORKFLOW
     * @VUsers 1,5
     * @Context Rest
     */

    @VUsers(vusers = { 1 })
    @Context(context = { Context.REST })
    @Test(groups = { "Smoke", "GAT", "RV_WORKFLOW" }, dataProvider = "setEnumAttributednsLookupOnTaiONENodeBFunction", dataProviderClass = CMRestToolData.class)
    public void setEnumAttributednsLookupOnTaiONENodeBFunction(final Host restServer, final String command, final String expectedResponse) {
        setTestCase("setEnumAttributednsLookupOnTaiONENodeBFunction ", "Host Id is " + restServer.getIp());
        final List<String> postResponse = sendRequest(restServer, command);
        final HashMap<String, String> actualResult = CMRestToolData.getActualResultAndPlaceInMap(postResponse);
        final HashMap<String, String> expectedResultMap = CMRestToolData
                .getExpectedResultsMapForSetCommandEnumAttributednsLookupOnTaiONENodeBFunction();
        assertTrue("Expected Response does not match for input of " + command, postResponse.get(0).contains(expectedResponse));
        assertTrue("Set Attribute dnsLookupOnTai result does not match expected result",
                actualResult.get("dnsLookupOnTai").equals(expectedResultMap.get("dnsLookupOnTai")));
    }

    /**
     * Verify SET command for setting/modifying an enumerated Attribute on an MO
     * 
     * @DESCRIPTION Goal of the test case is to verify that it is possible for the user to specify a SET command to set/modify an enumerated
     *              attribute. In this Test Cases the enumerated attribute, dnsLookupOnTai is set to value OFF from a previous value of ON
     * @PRIORITY MEDIUM
     * @GROUP Smoke, GAT, RV_WORKFLOW
     * @VUsers 1,5
     * @Context Rest
     */

    @VUsers(vusers = { 1 })
    @Context(context = { Context.REST })
    @Test(groups = { "Smoke", "GAT", "RV_WORKFLOW" }, dataProvider = "setEnumAttributednsLookupOnTaiOFFENodeBFunction", dataProviderClass = CMRestToolData.class)
    public void setEnumAttributednsLookupOnTaiOFFENodeBFunction(final Host restServer, final String command, final String expectedResponse) {
        setTestCase("setEnumAttributednsLookupOnTaiOFFENodeBFunction ", "Host Id is " + restServer.getIp());
        final List<String> postResponse = sendRequest(restServer, command);
        final HashMap<String, String> actualResult = CMRestToolData.getActualResultAndPlaceInMap(postResponse);
        final HashMap<String, String> expectedResultMap = CMRestToolData
                .getExpectedResultsMapForSetCommandEnumAttributednsLookupOnTaiOFFENodeBFunction();
        assertTrue("Expected Response does not match for input of " + command, postResponse.get(0).contains(expectedResponse));
        assertTrue("Set Attribute dnsLookupOnTai result does not match expected result",
                actualResult.get("dnsLookupOnTai").equals(expectedResultMap.get("dnsLookupOnTai")));
    }

    /**
     * Verify SET command for setting/modifying an enumerated Attribute on an MO
     * 
     * @DESCRIPTION Goal of the test case is to verify that it is possible for the user to specify a SET command to set/modify an enumerated
     *              attribute. In this Test Cases the enumerated attribute, dnsLookupOnTai is set to value ON from a previous value of OFF
     * @PRIORITY MEDIUM
     * @GROUP Smoke, GAT, RV_WORKFLOW
     * @VUsers 1,5
     * @Context Rest
     */
    @VUsers(vusers = { 1 })
    @Context(context = { Context.REST })
    @Test(groups = { "Smoke", "GAT", "RV_WORKFLOW" }, dataProvider = "setEnumAttributednsLookupOnTaiONAgainENodeBFunction", dataProviderClass = CMRestToolData.class)
    public void setEnumAttributednsLookupOnTaiONAgainENodeBFunction(final Host restServer, final String command, final String expectedResponse) {
        setTestCase("setEnumAttributednsLookupOnTaiONAgainENodeBFunction ", "Host Id is " + restServer.getIp());
        final List<String> postResponse = sendRequest(restServer, command);
        final HashMap<String, String> actualResult = CMRestToolData.getActualResultAndPlaceInMap(postResponse);
        final HashMap<String, String> expectedResultMap = CMRestToolData
                .getExpectedResultsMapForSetCommandEnumAttributednsLookupOnTaiONAgainENodeBFunction();
        assertTrue("Expected Response does not match for input of " + command, postResponse.get(0).contains(expectedResponse));
        assertTrue("Set Attribute dnsLookupOnTai result does not match expected result",
                actualResult.get("dnsLookupOnTai").equals(expectedResultMap.get("dnsLookupOnTai")));
    }

    /**
     * Verify SET command for setting/modifying an enumerated Attribute on an MO
     * 
     * @DESCRIPTION Goal of the test case is to verify that it is possible for the user to specify a SET command to set/modify an enumerated
     *              attribute. In this Test Cases the enumerated attribute is nnsfMode is set to value SPLMN
     * @PRIORITY MEDIUM
     * @GROUP Smoke, GAT, RV_WORKFLOW
     * @VUsers 1,5
     * @Context Rest
     */
    @VUsers(vusers = { 1 })
    @Context(context = { Context.REST })
    @Test(groups = { "Smoke", "GAT", "RV_WORKFLOW" }, dataProvider = "setEnumAttributennsfModeToSPLMNENodeBFunction", dataProviderClass = CMRestToolData.class)
    public void setEnumAttributennsfModeToSPLMNENodeBFunction(final Host restServer, final String command, final String expectedResponse) {
        setTestCase("setEnumAttributennsfModeToSPLMNENodeBFunction ", "Host Id is " + restServer.getIp());
        final List<String> postResponse = sendRequest(restServer, command);
        final HashMap<String, String> actualResult = CMRestToolData.getActualResultAndPlaceInMap(postResponse);
        final HashMap<String, String> expectedResultMap = CMRestToolData
                .getExpectedResultsMapForSetCommandEnumAttributennsfModeToSPLMNENodeBFunction();
        assertTrue("Expected Response does not match for input of " + command, postResponse.get(0).contains(expectedResponse));
        assertTrue("Set Attribute nnsfMode result does not match expected result",
                actualResult.get("nnsfMode").equals(expectedResultMap.get("nnsfMode")));
    }

    /*
     * START - KAOS User Story TORF-6924
     */

    /**
     * Verify SET command for setting/modifying an Attribute on all MOs of Type ENodeBFunction
     * 
     * @DESCRIPTION Goal of the test case is to verify that it is possible for the user to specify a SET command to set/modify an attribute for all
     *              nodes of a specific Type. In this Test Cases the attribute is userLabel is set to value TORF6924_TAF01
     * @PRIORITY MEDIUM
     * @GROUP Smoke, GAT, RV_WORKFLOW
     * @VUsers 1,5
     * @Context Rest
     */

    /*
     * COMMENTING OUT UNTIL CODE IMPLEMENTED - eronkeo [06-Jan-2014]
     * 
     * @VUsers(vusers = { 1 })
     * 
     * @Context(context = { Context.REST })
     * 
     * @Test(groups = { "Smoke", "GAT", "RV_WORKFLOW" }, dataProvider = "setAttributeOnAllNodesOfENodeBFunctionType", dataProviderClass =
     * CMRestToolData.class) public void setAttributeOnAllNodesOfSpecifiedType(final Host restServer, final String command, final HashMap
     * expectedResponse) { setTestCase("setAttributeOnAllNodesOfENodeBFunctionType ", "Host Id is " + restServer.getIp()); final List<String>
     * postResponse = sendRequest(restServer, command);
     * 
     * final HashMap<String, HashMap<String, String>> actualResultMap = CMRestToolData
     * .getActualResultAndPlaceInMapForMultipleMoResponse(postResponse);
     * 
     * final HashMap<String, HashMap<String, String>> expectedResultMap = CMRestToolData
     * .getExpectedResultsMapForSetAttributeOnAllNodesOfENodeBFunctionType();
     * 
     * // Verify the result for (final Entry<String, HashMap<String, String>> entry : actualResultMap.entrySet()) {
     * assertTrue("Expected SUCCESS for entry: \"" + entry.getValue() + "\"", expectedResultMap.containsValue(entry.getValue())); }
     * 
     * }
     */

    /**
     * Verify SET command for setting/modifying an Attribute on MOs of Type ENodeBFunction starting with a specified string
     * 
     * @DESCRIPTION Goal of the test case is to verify that it is possible for the user to specify a SET command to set/modify an attribute for all
     *              nodes of a specific Type starting with a specified string. In this Test Cases the attribute is userLabel is set to value
     *              TORF6924_TAF02
     * @PRIORITY MEDIUM
     * @GROUP Smoke, GAT, RV_WORKFLOW
     * @VUsers 1,5
     * @Context Rest
     */
    @VUsers(vusers = { 1 })
    @Context(context = { Context.REST })
    @Test(groups = { "Smoke", "GAT", "RV_WORKFLOW" }, dataProvider = "setAttributeOnAllNodesOfSpecifiedENodeBFunctionType", dataProviderClass = CMRestToolData.class)
    public void setAttributeOnAllNodesOfSpecifiedENodeBFunctionType(final Host restServer, final String command, final String expectedResponse) {
        setTestCase("setAttributeOnAllNodesOfSpecifiedENodeBFunctionType", "Host Id is " + restServer.getIp());

        // Post command
        final List<String> postResponse = sendRequest(restServer, command);

        // Get Actual Result hash map
        final HashMap<String, HashMap<String, String>> actualResultMap = CMRestToolData
                .getActualResultAndPlaceInMapForMultipleMoResponse(postResponse);

        // Get Expected Results hash map
        final HashMap<String, HashMap<String, String>> expectedResultMap = CMRestToolData
                .getExpectedResultsMapForSetAttributeOnAllNodesOfSpecifiedENodeBFunctionType();

        // Verify Actual Results map is equal to Expected Results map
        assertTrue("Actual Results [" + actualResultMap.values().toString() + "] do not match Expected Results ["
                + expectedResultMap.values().toString() + "]", actualResultMap.size() == expectedResultMap.size());

        // Verify the result
        for (final Entry<String, HashMap<String, String>> entry : actualResultMap.entrySet()) {
            assertTrue("Actual Value: \"" + entry.getValue() + "\"; Expected Value: \"" + expectedResultMap.get(entry.getKey()) + "\"",
                    expectedResultMap.containsValue(entry.getValue()));
        }
    }

    /**
     * Verify SET command for setting/modifying an Attribute on MOs of Type ENodeBFunction starting with a specified string
     * 
     * @DESCRIPTION Goal of the test case is to verify that it is possible for the user to specify a SET command to set/modify an attribute for all
     *              nodes of a specific Type starting with a specified string. In this Test Cases the attribute is userLabel is set to value
     *              TORF6924_TAF03
     * @PRIORITY MEDIUM
     * @GROUP Smoke, GAT, RV_WORKFLOW
     * @VUsers 1,5
     * @Context Rest
     */
    @VUsers(vusers = { 1 })
    @Context(context = { Context.REST })
    @Test(groups = { "Smoke", "GAT", "RV_WORKFLOW" }, dataProvider = "setAttributeOnNodesOfENodeBFunctionTypeStartingWithString", dataProviderClass = CMRestToolData.class)
    public void setAttributeOnAllNodesOfTypeENodeBStartingWithString(final Host restServer, final String command, final String expectedResponse) {
        setTestCase("setAttributeOnNodesOfENodeBFunctionTypeStartingWithString", "Host Id is " + restServer.getIp());

        // Post command
        final List<String> postResponse = sendRequest(restServer, command);

        // Get Actual Result hash map
        final HashMap<String, HashMap<String, String>> actualResultMap = CMRestToolData
                .getActualResultAndPlaceInMapForMultipleMoResponse(postResponse);

        // Get Expected Results hash map
        final HashMap<String, HashMap<String, String>> expectedResultMap = CMRestToolData
                .getExpectedResultsMapForSetAttributeOnNodesOfENodeBFunctionTypeStartingWithString();

        // Verify Actual Results map is equal to Expected Results map
        assertTrue("Actual Results [" + actualResultMap.values().toString() + "] do not match Expected Results ["
                + expectedResultMap.values().toString() + "]", actualResultMap.size() == expectedResultMap.size());

        // Verify the result
        for (final Entry<String, HashMap<String, String>> entry : actualResultMap.entrySet()) {
            assertTrue("Actual Value: \"" + entry.getValue() + "\"; Expected Value: \"" + expectedResultMap.get(entry.getKey()) + "\"",
                    expectedResultMap.containsValue(entry.getValue()));
        }
    }

    /**
     * Verify SET command for setting/modifying an Attribute on MOs of Type ENodeBFunction starting with a specified string
     * 
     * @DESCRIPTION Goal of the test case is to verify that it is possible for the user to specify a SET command to set/modify an attribute for all
     *              nodes of a specific Type starting with a specified string. In this Test Cases the attribute is userLabel is set to value
     *              TORF6924_TAF04
     * @PRIORITY MEDIUM
     * @GROUP Smoke, GAT, RV_WORKFLOW
     * @VUsers 1,5
     * @Context Rest
     */
    @VUsers(vusers = { 1 })
    @Context(context = { Context.REST })
    @Test(groups = { "Smoke", "GAT", "RV_WORKFLOW" }, dataProvider = "setAttributeOnNodesOfENodeBFunctionTypeEndingWithString", dataProviderClass = CMRestToolData.class)
    public void setAttributeOnAllNodesOfTypeENodeBEndingWithString(final Host restServer, final String command, final String expectedResponse) {
        setTestCase("setAttributeOnNodesOfENodeBFunctionTypeEndingWithString", "Host Id is " + restServer.getIp());

        // Post command
        final List<String> postResponse = sendRequest(restServer, command);

        // Get Actual Result hash map
        final HashMap<String, HashMap<String, String>> actualResultMap = CMRestToolData
                .getActualResultAndPlaceInMapForMultipleMoResponse(postResponse);

        // Get Expected Results hash map
        final HashMap<String, HashMap<String, String>> expectedResultMap = CMRestToolData
                .getExpectedResultsMapForSetAttributeOnNodesOfENodeBFunctionTypeEndingWithString();

        // Verify Actual Results map is equal to Expected Results map
        assertTrue("Actual Results [" + actualResultMap.values().toString() + "] do not match Expected Results ["
                + expectedResultMap.values().toString() + "]", actualResultMap.size() == expectedResultMap.size());

        // Verify the result
        for (final Entry<String, HashMap<String, String>> entry : actualResultMap.entrySet()) {
            assertTrue("Actual Value: \"" + entry.getValue() + "\"; Expected Value: \"" + expectedResultMap.get(entry.getKey()) + "\"",
                    expectedResultMap.containsValue(entry.getValue()));
        }
    }

    /*
     * START - KAOS User Story TORF-6964
     */

    /**
     * Verify SET command for setting/modifying an Attribute on an MO filtered on one specified criteria
     * 
     * @DESCRIPTION Goal of the test case is to verify that it is possible for the user to specify a SET command to set/modify an attribute for a node
     *              filtered on one specified criteria. In this Test Cases the attribute is x2IpAddrViaS1Active is set to value "false"
     * @PRIORITY MEDIUM
     * @GROUP Smoke, GAT, RV_WORKFLOW
     * @VUsers 1,5
     * @Context Rest
     */
    @VUsers(vusers = { 1 })
    @Context(context = { Context.REST })
    @Test(groups = { "Smoke", "GAT", "RV_WORKFLOW" }, dataProvider = "setSingleValueFALSEForNodeIDFilteredOnSingleAttributeValueCriteria", dataProviderClass = CMRestToolData.class)
    public void setSingleValueFALSEForNodeIDFilteredOnSingleAttributeValueCriteria(final Host restServer, final String command,
                                                                                   final String expectedResponse) {
        final String testAttribute = "x2IpAddrViaS1Active";

        setTestCase("setSingleValueFALSEForNodeIDFilteredOnSingleAttributeValueCriteria", "Host Id is " + restServer.getIp());
        final List<String> postResponse = sendRequest(restServer, command);

        final HashMap<String, String> actualResult = CMRestToolData.getActualResultAndPlaceInMap(postResponse);
        final HashMap<String, String> expectedResultMap = CMRestToolData
                .getExpectedResultsMapForSetSingleValueFALSEForNodeIDFilteredOnSingleAttributeValueCriteria();

        assertTrue("Expected Response does not match for input of " + command, postResponse.get(0).contains(expectedResponse));
        assertTrue("Set Attribute " + testAttribute + " result does not match expected result",
                actualResult.get(testAttribute).equals(expectedResultMap.get(testAttribute)));
    }

    /**
     * Verify SET command for setting/modifying an Attribute on an MO filtered on one specified criteria
     * 
     * @DESCRIPTION Goal of the test case is to verify that it is possible for the user to specify a SET command to set/modify an attribute for a node
     *              filtered on one specified criteria. In this Test Cases the attribute is x2IpAddrViaS1Active is set to value "true"
     * @PRIORITY MEDIUM
     * @GROUP Smoke, GAT, RV_WORKFLOW
     * @VUsers 1,5
     * @Context Rest
     */
    @VUsers(vusers = { 1 })
    @Context(context = { Context.REST })
    @Test(groups = { "Smoke", "GAT", "RV_WORKFLOW" }, dataProvider = "setSingleValueTRUEForNodeIDFilteredOnSingleAttributeValueCriteria", dataProviderClass = CMRestToolData.class)
    public void setSingleValueTRUEForNodeIDFilteredOnSingleAttributeValueCriteria(final Host restServer, final String command,
                                                                                  final String expectedResponse) {
        final String testAttribute = "x2IpAddrViaS1Active";

        setTestCase("setSingleValueTRUEForNodeIDFilteredOnSingleAttributeValueCriteria", "Host Id is " + restServer.getIp());
        final List<String> postResponse = sendRequest(restServer, command);

        final HashMap<String, String> actualResult = CMRestToolData.getActualResultAndPlaceInMap(postResponse);
        final HashMap<String, String> expectedResultMap = CMRestToolData
                .getExpectedResultsMapForSetSingleValueTRUEForNodeIDFilteredOnSingleAttributeValueCriteria();

        assertTrue("Expected Response does not match for input of " + command, postResponse.get(0).contains(expectedResponse));
        assertTrue("Set Attribute " + testAttribute + " result does not match expected result",
                actualResult.get(testAttribute).equals(expectedResultMap.get(testAttribute)));
    }

    /**
     * Verify SET command for setting/modifying multiple Attributes on an MO filtered on a specified criteria
     * 
     * @DESCRIPTION Goal of the test case is to verify that it is possible for the user to specify a SET command to set/modify multiple attributes for
     *              a node filtered on a specified criteria. In this Test Cases the attributes set are: x2IpAddrViaS1Active = "true";
     *              tHODataFwdReordering = "1000"
     * @PRIORITY MEDIUM
     * @GROUP Smoke, GAT, RV_WORKFLOW
     * @VUsers 1,5
     * @Context Rest
     */
    @VUsers(vusers = { 1 })
    @Context(context = { Context.REST })
    @Test(groups = { "Smoke", "GAT", "RV_WORKFLOW" }, dataProvider = "setMultipleValuesForNodeIDFilteredOnSingleAttributeValueCriteria", dataProviderClass = CMRestToolData.class)
    public void setMultipleValuesForNodeIDFilteredOnSingleAttributeValueCriteria(final Host restServer, final String command,
                                                                                 final String expectedResponse) {
        final String[] testAttributes = { "x2IpAddrViaS1Active", "tHODataFwdReordering" };

        setTestCase("setMultipleValuesForNodeIDFilteredOnSingleAttributeValueCriteria", "Host Id is " + restServer.getIp());
        final List<String> postResponse = sendRequest(restServer, command);

        final HashMap<String, String> actualResult = CMRestToolData.getActualResultAndPlaceInMap(postResponse);
        final HashMap<String, String> expectedResultMap = CMRestToolData
                .getExpectedResultsMapForSetMultipleValuesForNodeIDFilteredOnSingleAttributeValueCriteria();

        assertTrue("Expected Response does not match for input of " + command, postResponse.get(0).contains(expectedResponse));
        assertTrue("Set Attribute " + testAttributes[0] + " result does not match expected result",
                actualResult.get(testAttributes[0]).equals(expectedResultMap.get(testAttributes[0])));
        assertTrue("Set Attribute " + testAttributes[1] + " result does not match expected result",
                actualResult.get(testAttributes[1]).equals(expectedResultMap.get(testAttributes[1])));
    }

    /*
     * START - KAOS User Story TORF-1355
     */

    /**
     * Verify SET command for setting/modifying an Integer Attribute on an MO filtered on multiple specified criteria
     * 
     * @DESCRIPTION Goal of the test case is to verify that it is possible for the user to specify a SET command to set/modify an integer attribute
     *              for a node filtered on multiple specified criteria. In this Test Cases the attribute is tHODataFwdReordering is set to value
     *              "2000"
     * @PRIORITY MEDIUM
     * @GROUP Smoke, GAT, RV_WORKFLOW
     * @VUsers 1,5
     * @Context Rest
     */
    @VUsers(vusers = { 1 })
    @Context(context = { Context.REST })
    @Test(groups = { "Smoke", "GAT", "RV_WORKFLOW" }, dataProvider = "setSingleIntegerValueForNodeIDFilteredOnMultipleAttributeValueCriteria", dataProviderClass = CMRestToolData.class)
    public void setSingleIntegerValueForNodeIDFilteredOnMultipleAttributeValueCriteria(final Host restServer, final String command,
                                                                                       final String expectedResponse) {
        final String testAttribute = "tHODataFwdReordering";

        setTestCase("setSingleIntegerValueForNodeIDFilteredOnMultipleAttributeValueCriteria", "Host Id is " + restServer.getIp());
        final List<String> postResponse = sendRequest(restServer, command);

        final HashMap<String, String> actualResult = CMRestToolData.getActualResultAndPlaceInMap(postResponse);
        final HashMap<String, String> expectedResultMap = CMRestToolData
                .getExpectedResultsMapForSetSingleIntegerValueForNodeIDFilteredOnMultipleAttributeValueCriteria();

        assertTrue("Expected Response does not match for input of " + command, postResponse.get(0).contains(expectedResponse));
        assertTrue("Set Attribute " + testAttribute + " result does not match expected result",
                actualResult.get(testAttribute).equals(expectedResultMap.get(testAttribute)));
    }

    /**
     * Verify SET command for setting/modifying an enumerated Attribute on an MO filtered on multiple specified criteria
     * 
     * @DESCRIPTION Goal of the test case is to verify that it is possible for the user to specify a SET command to set/modify an enumerated attribute
     *              for a node filtered on multiple specified criteria. In this Test Cases the attribute is tHODataFwdReordering is set to value
     *              "2000"
     * @PRIORITY MEDIUM
     * @GROUP Smoke, GAT, RV_WORKFLOW
     * @VUsers 1,5
     * @Context Rest
     */
    @VUsers(vusers = { 1 })
    @Context(context = { Context.REST })
    @Test(groups = { "Smoke", "GAT", "RV_WORKFLOW" }, dataProvider = "setSingleEnumValueForNodeIDFilteredOnMultipleAttributeValueCriteria", dataProviderClass = CMRestToolData.class)
    public void setSingleEnumValueForNodeIDFilteredOnMultipleAttributeValueCriteria(final Host restServer, final String command,
                                                                                    final String expectedResponse) {
        final String testAttribute = "x2IpAddrViaS1Active";

        setTestCase("setSingleEnumValueForNodeIDFilteredOnMultipleAttributeValueCriteria", "Host Id is " + restServer.getIp());
        final List<String> postResponse = sendRequest(restServer, command);

        final HashMap<String, String> actualResult = CMRestToolData.getActualResultAndPlaceInMap(postResponse);
        final HashMap<String, String> expectedResultMap = CMRestToolData
                .getExpectedResultsMapForSetSingleEnumValueForNodeIDFilteredOnMultipleAttributeValueCriteria();

        assertTrue("Expected Response does not match for input of " + command, postResponse.get(0).contains(expectedResponse));
        assertTrue("Set Attribute " + testAttribute + " result does not match expected result",
                actualResult.get(testAttribute).equals(expectedResultMap.get(testAttribute)));
    }

    /*
     * Following Test Cases added for KAOS User Story TORF-1358
     */

    /**
     * Verify SET command for setting/modifying an Attribute for all specified MO Instances containing a specified Attribute on a specified NE
     * 
     * @DESCRIPTION Goal of the test case is to verify that it is possible for the user to specify a SET command to set/modify an attribute on all
     *              specified MO instances containing a specified Attribute of a Node.
     * @PRIORITY MEDIUM
     * @GROUP Smoke, GAT, RV_WORKFLOW
     * @VUsers 1,5
     * @Context Rest
     */

    @VUsers(vusers = { 1 })
    @Context(context = { Context.REST })
    @Test(groups = { "Smoke", "GAT", "RV_WORKFLOW" }, dataProvider = "setAttributetHODataFwdReorderingOnAllMOInstancesOfENodeBFunctionWithuserLabelOfNode", dataProviderClass = CMRestToolData.class)
    public void setAttributetHODataFwdReorderingOnAllMOInstancesOfENodeBFunctionWithuserLabelOfNode(final Host restServer, final String command,
                                                                                                    final String expectedResponse) {
        setTestCase("setAttributetHODataFwdReorderingOnAllMOInstancesOfENodeBFunctionWithuserLabelOfNode ", "Host Id is " + restServer.getIp());
        final List<String> postResponse = sendRequest(restServer, command);

        final HashMap<String, HashMap<String, String>> actualResultMap = CMRestToolData
                .getActualResultAndPlaceInMapForMultipleMoResponse(postResponse);
        final HashMap<String, HashMap<String, String>> expectedResultMap = CMRestToolData
                .getExpectedResultsMapForSetCommandSetAttributetHODataFwdReorderingOnAllMOInstancesOfENodeBFunctionWithuserLabelOfNode();

        // Verify Actual Results map is equal to Expected Results map
        assertTrue("Actual Results [" + actualResultMap.values().toString() + "] do not match Expected Results ["
                + expectedResultMap.values().toString() + "]", actualResultMap.size() == expectedResultMap.size());

        for (final Entry<String, HashMap<String, String>> entry : actualResultMap.entrySet()) {
            assertTrue("Actual Value: \"" + entry.getValue() + "\"; Expected Value: \"" + expectedResultMap.get(entry.getKey()) + "\"",
                    expectedResultMap.containsValue(entry.getValue()));
        }
    }

    /*
     * END - KAOS User Story TORF-1358
     */

    /*
     * Following Test Cases added for KAOS User Stories TORF-1358/6924/1355/6964
     */

    /**
     * Verify SET command for setting/modifying an Attribute userLabel on all MOs of Type EUtranCellFDD starting with a specified string
     * 
     * @DESCRIPTION Goal of the test case is to verify that it is possible for the user to specify a SET command to set/modify attribute userLabel for
     *              all nodes containing EUtranCellFDD containing a certain string in the userLabel. In this Test Cases the attribute userLabel is set
     *              to value TORF_1358 on all EUtranCellFDD
     * @PRIORITY MEDIUM
     * @GROUP Smoke, GAT, RV_WORKFLOW
     * @VUsers 1,5
     * @Context Rest
     */

    @VUsers(vusers = { 1 })
    @Context(context = { Context.REST })
    @Test(groups = { "Smoke", "GAT", "RV_WORKFLOW" }, dataProvider = "setAttributeuserLabelOnAllEUtranCellcontainAttributeFilter", dataProviderClass = CMRestToolData.class)
    public void setAttributeuserLabelOnAllEUtranCellcontainAttributeFilter(final Host restServer, final String command, final String expectedResponse) {
        setTestCase("setAttributeuserLabelOnAllEUtranCellcontainAttributeFilter", "Host Id is " + restServer.getIp());

        // Post command
        final List<String> postResponse = sendRequest(restServer, command);

        // Get Actual Result hash map
        final HashMap<String, HashMap<String, String>> actualResultMap = CMRestToolData
                .getActualResultAndPlaceInMapForMultipleMoResponse(postResponse);

        // Get Expected Results hash map
        final HashMap<String, HashMap<String, String>> expectedResultMap = CMRestToolData
                .getExpectedResultsMapForsetAttributeuserLabelOnAllEUtranCellcontainAttributeFilter();

        // Verify Actual Results map is equal to Expected Results map
        assertTrue("Actual Results [" + actualResultMap.values().toString() + "] do not match Expected Results ["
                + expectedResultMap.values().toString() + "]", actualResultMap.size() == expectedResultMap.size());

        // Verify the result
        for (final Entry<String, HashMap<String, String>> entry : actualResultMap.entrySet()) {
            assertTrue("Actual Value: \"" + entry.getValue() + "\"; Expected Value: \"" + expectedResultMap.get(entry.getKey()) + "\"",
                    expectedResultMap.containsValue(entry.getValue()));
        }
    }

    /**
     * Verify SET command for setting/modifying an Attribute userLabel on EUtranCellFDD using a partial Node Filter and single Attribute Filter
     * 
     * @DESCRIPTION Goal of the test case is to verify that it is possible for the user to specify a SET command to set/modify attribute userLabel on
     *              a EUtranCellFDD using a partial Node Filter and single attribute Filter In this Test Cases the attribute userLabel is set to value
     *              TORF_6964 on the filtered EUtranCellFDD returned.
     * @PRIORITY MEDIUM
     * @GROUP Smoke, GAT, RV_WORKFLOW
     * @VUsers 1,5
     * @Context Rest
     */

    @VUsers(vusers = { 1 })
    @Context(context = { Context.REST })
    @Test(groups = { "Smoke", "GAT", "RV_WORKFLOW" }, dataProvider = "setAttributePartialNodeFilteruserLabelOnEUtranCellFDDcontainsSpecifieduserLabel", dataProviderClass = CMRestToolData.class)
    public void setAttributePartialNodeFilteruserLabelOnEUtranCellFDDcontainsSpecifieduserLabel(final Host restServer, final String command,
                                                                                                final String expectedResponse) {
        setTestCase("setAttributePartialNodeFilteruserLabelOnEUtranCellFDDcontainsSpecifieduserLabel", "Host Id is " + restServer.getIp());

        // Post command
        final List<String> postResponse = sendRequest(restServer, command);

        // Get Actual Result hash map
        final HashMap<String, HashMap<String, String>> actualResultMap = CMRestToolData
                .getActualResultAndPlaceInMapForMultipleMoResponse(postResponse);

        System.out.println("actualResultMap == " + actualResultMap);

        // Get Expected Results hash map
        final HashMap<String, HashMap<String, String>> expectedResultMap = CMRestToolData
                .getExpectedResultsMapForsetAttributePartialNodeFilteruserLabelOnEUtranCellFDDcontainsSpecifieduserLabel();

        System.out.println("expectedResultMap == " + expectedResultMap);

        // Verify Actual Results map is equal to Expected Results map
        assertTrue("Actual Results [" + actualResultMap.values().toString() + "] do not match Expected Results ["
                + expectedResultMap.values().toString() + "]", actualResultMap.size() == expectedResultMap.size());

        // Verify the result
        for (final Entry<String, HashMap<String, String>> entry : actualResultMap.entrySet()) {
            assertTrue("Actual Value: \"" + entry.getValue() + "\"; Expected Value: \"" + expectedResultMap.get(entry.getKey()) + "\"",
                    expectedResultMap.containsValue(entry.getValue()));
        }
    }

    /**
     * Verify SET command for setting/modifying an filtered Attribute ulInterferenceManagementActive on EUtranCellFDD using a partial Node Filter and
     * multiple Attribute Filters
     * 
     * @DESCRIPTION Goal of the test case is to verify that it is possible for the user to specify a SET command to set/modify attribute
     *              ulInterferenceManagementActive on a EUtranCellFDD using a partial Node Filter and multiple attribute Filters containing attribute
     *              to be set in SET command. In this Test Cases the attribute ulInterferenceManagementActive is set to value true on the filtered
     *              EUtranCellFDD returned.
     * @PRIORITY MEDIUM
     * @GROUP Smoke, GAT, RV_WORKFLOW
     * @VUsers 1,5
     * @Context Rest
     */

    @VUsers(vusers = { 1 })
    @Context(context = { Context.REST })
    @Test(groups = { "Smoke", "GAT", "RV_WORKFLOW" }, dataProvider = "setAttributeulInterferenceManagementActivePartialNodeFilterContainsEUtranCellFDDcontainsSpecifieduserLabelAndulInterferenceManagementActiveFalse", dataProviderClass = CMRestToolData.class)
    public void setAttributeulInterferenceManagementActivePartialNodeFilterContainsEUtranCellFDDcontainsSpecifieduserLabelAndulInterferenceManagementActiveFalse(final Host restServer,
                                                                                                                                                                 final String command,
                                                                                                                                                                 final String expectedResponse) {
        setTestCase(
                "setAttributeulInterferenceManagementActivePartialNodeFilterContainsEUtranCellFDDcontainsSpecifieduserLabelAndulInterferenceManagementActiveFalse",
                "Host Id is " + restServer.getIp());

        // Post command
        final List<String> postResponse = sendRequest(restServer, command);

        // Get Actual Result hash map
        final HashMap<String, HashMap<String, String>> actualResultMap = CMRestToolData
                .getActualResultAndPlaceInMapForMultipleMoResponse(postResponse);

        System.out.println("actualResultMap == " + actualResultMap);

        // Get Expected Results hash map
        final HashMap<String, HashMap<String, String>> expectedResultMap = CMRestToolData
                .getExpectedResultsMapForsetAttributeulInterferenceManagementActivePartialNodeFilterContainsEUtranCellFDDcontainsSpecifieduserLabelAndulInterferenceManagementActiveFalse();

        System.out.println("expectedResultMap == " + expectedResultMap);

        // Verify Actual Results map is equal to Expected Results map
        assertTrue("Actual Results [" + actualResultMap.values().toString() + "] do not match Expected Results ["
                + expectedResultMap.values().toString() + "]", actualResultMap.size() == expectedResultMap.size());

        // Verify the result
        for (final Entry<String, HashMap<String, String>> entry : actualResultMap.entrySet()) {
            assertTrue("Actual Value: \"" + entry.getValue() + "\"; Expected Value: \"" + expectedResultMap.get(entry.getKey()) + "\"",
                    expectedResultMap.containsValue(entry.getValue()));

        }
    }

    /**
     * Verify SET command for setting/modifying a non filtered Attribute ulChannelBandwidth on EUtranCellFDD using a partial Node Filter and multiple
     * Attribute Filters
     * 
     * @DESCRIPTION Goal of the test case is to verify that it is possible for the user to specify a SET command to set/modify a non filtered
     *              attribute ulChannelBandwidth on a EUtranCellFDD using a partial Node Filter and multiple attribute Filter NOT containing attribute
     *              to be set. In this Test Cases the attribute ulChannelBandwidth is set to value 20000 on the filtered EUtranCellFDD returned.
     * @PRIORITY MEDIUM
     * @GROUP Smoke, GAT, RV_WORKFLOW
     * @VUsers 1,5
     * @Context Rest
     */

    @VUsers(vusers = { 1 })
    @Context(context = { Context.REST })
    @Test(groups = { "Smoke", "GAT", "RV_WORKFLOW" }, dataProvider = "setAttributeulChannelBandwidthNodeStarContainsEUtranCellFDDcontainsuserLabelAndulInterferenceManagementActiveFalse", dataProviderClass = CMRestToolData.class)
    public void setAttributeulChannelBandwidthNodeStarContainsEUtranCellFDDcontainsuserLabelAndulInterferenceManagementActiveFalse(final Host restServer,
                                                                                                                                   final String command,
                                                                                                                                   final String expectedResponse) {
        setTestCase("setAttributeulChannelBandwidthNodeStarContainsEUtranCellFDDcontainsuserLabelAndulInterferenceManagementActiveFalse",
                "Host Id is " + restServer.getIp());

        // Post command
        final List<String> postResponse = sendRequest(restServer, command);

        // Get Actual Result hash map
        final HashMap<String, HashMap<String, String>> actualResultMap = CMRestToolData
                .getActualResultAndPlaceInMapForMultipleMoResponse(postResponse);

        // Get Expected Results hash map
        final HashMap<String, HashMap<String, String>> expectedResultMap = CMRestToolData
                .getExpectedResultsMapForsetAttributeulChannelBandwidthNodeStarContainsEUtranCellFDDcontainsuserLabelAndulInterferenceManagementActiveFalse();

        // Verify Actual Results map is equal to Expected Results map
        assertTrue("Actual Results [" + actualResultMap.values().toString() + "] do not match Expected Results ["
                + expectedResultMap.values().toString() + "]", actualResultMap.size() == expectedResultMap.size());

        // Verify the result
        for (final Entry<String, HashMap<String, String>> entry : actualResultMap.entrySet()) {
            assertTrue("Actual Value: \"" + entry.getValue() + "\"; Expected Value: \"" + expectedResultMap.get(entry.getKey()) + "\"",
                    expectedResultMap.containsValue(entry.getValue()));

        }
    }

    /*
     * End of Test Cases added for KAOS User Story TORF-1358/6924/1355/6964
     */

    /*
     * Following Test Cases added for KAOS User Story TORF-7037
     */

    /**
     * Verify SET command for setting/modifying an Attribute for all specified MO Instances containing a specified Attribute on a specified NE
     * 
     * @DESCRIPTION Goal of the test case is to verify that it is possible for the user to specify a SET command to set/modify an attribute on all
     *              specified MO instances containing a specified Attribute of a Node.
     * @PRIORITY MEDIUM
     * @GROUP Smoke, GAT, RV_WORKFLOW
     * @VUsers 1,5
     * @Context Rest
     */

    @SuppressWarnings("unused")
    @VUsers(vusers = { 1 })
    @Context(context = { Context.REST })
    @Test(groups = { "Smoke", "GAT", "RV_WORKFLOW" }, dataProvider = "setSequencedComplexDataForSpecifiedNode", dataProviderClass = CMRestToolData.class)
    public void setSequencedComplexDataForSpecifiedNode(final Host restServer, final String command, final String expectedResponse) {
        setTestCase("setSequencedComplexDataForSpecifiedNode ", "Host Id is " + restServer.getIp());
        final List<String> postResponse = sendRequest(restServer, command);

        final HashMap<String, String> actualResultMap = CMRestToolData.getActualResultAndPlaceInMap(postResponse);
        final HashMap<String, String> expectedResultMap = CMRestToolData.getExpectedResultsMapForSetSequencedComplexDataForSpecifiedNode();

        // Verify Actual Results map is equal to Expected Results map
        assertTrue("Actual Results [" + actualResultMap.values().toString() + "] do not match Expected Results ["
                + expectedResultMap.values().toString() + "]", actualResultMap.toString().equals(expectedResultMap.toString()));

        for (final Entry<String, String> entry : actualResultMap.entrySet()) {
            assertTrue("Actual Value: \"" + entry.getValue() + "\"; Expected Value: \"" + expectedResultMap.get(entry.getKey()) + "\"",
                    expectedResultMap.containsValue(entry.getValue()));
        }
    }

    /*
     * END - KAOS User Story TORF-7037
     */

    //TODO Add this test case to the suite.xml file when US completed

    /**
     * Verify running an action command deletes All Repertoires results in a success.
     * 
     * @DESCRIPTION Verify running an action command deletes All Repertoires results in a success.
     * @PRIORITY HIGH
     * @GROUP
     * @VUsers
     * @Context Rest
     */
    @VUsers(vusers = { 1 })
    @Context(context = { Context.REST })
    @Test(groups = { "Smoke", "GAT", "RV_WORKFLOW" }, dataProvider = "actionMeContextDeleteAllRepertoires", dataProviderClass = CMRestToolData.class)
    public void actionMeContextDeleteAllRepertoires(final Host restServer, final String command, final String expectedResponse) {
        setTestCase("actionMeContextDeleteAllRepertoires ", "Host Id is " + restServer.getIp());
        final List<String> postResponse = sendRequest(restServer, command);
        final String postResultString = postResponse.get(0);
        assertTrue("Expected SUCCESS for test (actionMeContextDeleteAllRepertoires)" + command, postResultString.contains("SUCCESS"));
    }

    //TODO Add this test case to the suite.xml file when US completed
    /**
     * Verify running an action command deletes All Slots results in a success.
     * 
     * @DESCRIPTION Verify running an action command deletes All Slots results in a success.
     * @PRIORITY HIGH
     * @GROUP
     * @VUsers
     * @Context Rest
     */
    @VUsers(vusers = { 1 })
    @Context(context = { Context.REST })
    @Test(groups = { "Smoke", "GAT", "RV_WORKFLOW" }, dataProvider = "actionMeContextDeleteAllSlots", dataProviderClass = CMRestToolData.class)
    public void actionMeContextDeleteAllSlots(final Host restServer, final String command, final String expectedResponse) {
        setTestCase("actionMeContextDeleteAllSlots ", "Host Id is " + restServer.getIp());
        final List<String> postResponse = sendRequest(restServer, command);
        final String postResultString = postResponse.get(0);
        assertTrue("Expected SUCCESS for test (actionMeContextDeleteAllSlots)" + command, postResultString.contains("SUCCESS"));
    }

    //TODO Add this test case to the suite.xml file when US completed
    /**
     * Verify running an action command syncs and results in a success.
     * 
     * @DESCRIPTION Verify running an action command syncs and results in a success.
     * @PRIORITY HIGH
     * @GROUP
     * @VUsers
     * @Context Rest
     */
    @VUsers(vusers = { 1 })
    @Context(context = { Context.REST })
    @Test(groups = { "Smoke", "GAT", "RV_WORKFLOW" }, dataProvider = "actionMeContextSyncs", dataProviderClass = CMRestToolData.class)
    public void actionMeContextSyncs(final Host restServer, final String command, final String expectedResponse) {
        setTestCase("actionMeContextSyncs ", "Host Id is " + restServer.getIp());
        final List<String> postResponse = sendRequest(restServer, command);
        final String postResultString = postResponse.get(0);
        assertTrue("Expected SUCCESS for test (actionMeContextSyncs)" + command, postResultString.contains("SUCCESS"));
    }

    //TODO Add this test case to the suite.xml file when US completed
    /**
     * Verify running an action command deletes All Slots for * EnodeBFunction with EnodeBFunctionId== which results in a success.
     * 
     * @DESCRIPTION Verify running an action command deletes All Slots for * EnodeBFunction with EnodeBFunctionId== which results in a success.
     * @PRIORITY HIGH
     * @GROUP
     * @VUsers
     * @Context Rest
     */
    @VUsers(vusers = { 1 })
    @Context(context = { Context.REST })
    @Test(groups = { "Smoke", "GAT", "RV_WORKFLOW" }, dataProvider = "actionMeContextDeleteAllSlotsAllEnodebFuncEqEqEnodebFuncId", dataProviderClass = CMRestToolData.class)
    public void actionMeContextDeleteAllSlotsAllEnodebFuncEqEqEnodebFunc(final Host restServer, final String command, final String expectedResponse) {
        setTestCase("actionMeContextDeleteAllSlotsAllEnodebFuncEqEqEnodebFuncId ", "Host Id is " + restServer.getIp());
        final List<String> postResponse = sendRequest(restServer, command);
        final String postResultString = postResponse.get(0);
        assertTrue("Expected SUCCESS for test (actionMeContextDeleteAllSlotsAllEnodebFuncEqEqEnodebFuncId)" + command,
                postResultString.contains("SUCCESS"));
    }

    /**
     * Creating a Third MeContext instance to allow sequence attributes to be tested correctly (will require a create EnodeBFunction command)
     * 
     * @DESCRIPTION Creating a Third MeContext instance to allow sequence attributes to be tested correctly (will require a create EnodeBFunction
     *              command)
     * @PRIORITY HIGH
     * @GROUP
     * @VUsers
     * @Context Rest
     */
    @VUsers(vusers = { 1 })
    @Context(context = { Context.REST })
    @Test(groups = { "Smoke", "GAT", "RV_WORKFLOW" }, dataProvider = "createThirdMeContextInstance", dataProviderClass = CMRestToolData.class)
    public void createThirdMeContextInstance(final Host restServer, final String command, final String expectedResponse) {
        setTestCase("createThirdMeContextInstance ", "Host Id is " + restServer.getIp());
        final List<String> postResponse = sendRequest(restServer, command);
        final HashMap<String, String> actualResult = CMRestToolData.getActualResultAndPlaceInMap(postResponse);
        final HashMap<String, String> expectedResultMap = CMRestToolData.getExpectedResultsMapForThirdMeContextCommand();
        assertTrue("Expected Response does not match for input of " + command, postResponse.get(0).contains(expectedResponse));
        assertTrue("FDN doesn't Match expected result", actualResult.get("FDN").equals(expectedResultMap.get("FDN")));
    }

    /**
     * Creating a Third ManagedElement instance to allow sequence attributes to be tested correctly (will require a create EnodeBFunction command)
     * 
     * @DESCRIPTION Creating a Third ManagedElement instance to allow sequence attributes to be tested correctly (will require a create EnodeBFunction
     *              command)
     * @PRIORITY HIGH
     * @GROUP
     * @VUsers
     * @Context Rest
     */
    @VUsers(vusers = { 1 })
    @Context(context = { Context.REST })
    @Test(groups = { "Smoke", "GAT", "RV_WORKFLOW" }, dataProvider = "createThirdManagedElementInstance", dataProviderClass = CMRestToolData.class)
    public void createThirdManagedElementInstance(final Host restServer, final String command, final String expectedResponse) {
        setTestCase("createThirdManagedElementInstance ", "Host Id is " + restServer.getIp());
        final List<String> postResponse = sendRequest(restServer, command);
        final HashMap<String, String> actualResult = CMRestToolData.getActualResultAndPlaceInMap(postResponse);
        final HashMap<String, String> expectedResultMap = CMRestToolData.getExpectedResultsMapForThirdManagedElementCommand();
        assertTrue("Expected Response does not match for input of " + command, postResponse.get(0).contains(expectedResponse));
        assertTrue("FDN doesn't Match expected result", actualResult.get("FDN").equals(expectedResultMap.get("FDN")));
    }

    /**
     * Verify that it is possible to create Sequence Attributes
     * 
     * @DESCRIPTION Verify that it is possible to create Sequence Attributes
     * @PRIORITY HIGH
     * @GROUP GAT
     * @VUsers
     * @Context Rest
     */

    @VUsers(vusers = { 1 })
    @Context(context = { Context.REST })
    @Test(groups = { "Smoke", "GAT", "RV_WORKFLOW" }, dataProvider = "createSequenceAttributes", dataProviderClass = CMRestToolData.class)
    public void createSequenceAttributes(final Host restServer, final String command, final String expectedResponse) {
        setTestCase("createSequenceAttributes ", "Host Id is " + restServer.getIp());
        final List<String> postResponse = sendRequest(restServer, command);
        assertTrue("x2BlackList attribute has not been created as expected", postResponse.get(0).contains(CMRestToolData.expectedX2BlackList));
    }

    /**
     * Creating a Fourth MeContext instance to allow sequence attributes to be tested correctly (will require a create EnodeBFunction command)
     * 
     * @DESCRIPTION Creating a Third MeContext instance to allow sequence attributes to be tested correctly (will require a create EnodeBFunction
     *              command)
     * @PRIORITY HIGH
     * @GROUP
     * @VUsers
     * @Context Rest
     */
    @VUsers(vusers = { 1 })
    @Context(context = { Context.REST })
    @Test(groups = { "Smoke", "GAT", "RV_WORKFLOW" }, dataProvider = "createFourthMeContextInstance", dataProviderClass = CMRestToolData.class)
    public void createFourthMeContextInstance(final Host restServer, final String command, final String expectedResponse) {
        setTestCase("createFourthMeContextInstance ", "Host Id is " + restServer.getIp());
        final List<String> postResponse = sendRequest(restServer, command);
        final HashMap<String, String> actualResult = CMRestToolData.getActualResultAndPlaceInMap(postResponse);
        final HashMap<String, String> expectedResultMap = CMRestToolData.getExpectedResultsMapForFourthMeContextCommand();
        assertTrue("Expected Response does not match for input of " + command, postResponse.get(0).contains(expectedResponse));
        assertTrue("FDN doesn't Match expected result", actualResult.get("FDN").equals(expectedResultMap.get("FDN")));
    }

    /**
     * Creating a Fourth ManagedElement instance to allow sequence attributes to be tested correctly (will require a create EnodeBFunction command)
     * 
     * @DESCRIPTION Creating a Third ManagedElement instance to allow sequence attributes to be tested correctly (will require a create EnodeBFunction
     *              command)
     * @PRIORITY HIGH
     * @GROUP
     * @VUsers
     * @Context Rest
     */
    @VUsers(vusers = { 1 })
    @Context(context = { Context.REST })
    @Test(groups = { "Smoke", "GAT", "RV_WORKFLOW" }, dataProvider = "createFourthManagedElementInstance", dataProviderClass = CMRestToolData.class)
    public void createFourthManagedElementInstance(final Host restServer, final String command, final String expectedResponse) {
        setTestCase("createFourthManagedElementInstance ", "Host Id is " + restServer.getIp());
        final List<String> postResponse = sendRequest(restServer, command);
        final HashMap<String, String> actualResult = CMRestToolData.getActualResultAndPlaceInMap(postResponse);
        final HashMap<String, String> expectedResultMap = CMRestToolData.getExpectedResultsMapForFourthManagedElementCommand();
        assertTrue("Expected Response does not match for input of " + command, postResponse.get(0).contains(expectedResponse));
        assertTrue("FDN doesn't Match expected result", actualResult.get("FDN").equals(expectedResultMap.get("FDN")));
    }

    /**
     * Creating a Fourth ManagedElement instance to allow sequence attributes to be tested correctly (will require a create EnodeBFunction command)
     * 
     * @DESCRIPTION Creating a Third ManagedElement instance to allow sequence attributes to be tested correctly (will require a create EnodeBFunction
     *              command)
     * @PRIORITY HIGH
     * @GROUP
     * @VUsers
     * @Context Rest
     */
    @VUsers(vusers = { 1 })
    @Context(context = { Context.REST })
    @Test(groups = { "Smoke", "GAT", "RV_WORKFLOW" }, dataProvider = "createFourthENodeBFunctionInstance", dataProviderClass = CMRestToolData.class)
    public void createFourthENodeBFunctionInstance(final Host restServer, final String command, final String expectedResponse) {
        setTestCase("createFourthENodeBFunctionInstance ", "Host Id is " + restServer.getIp());
        final List<String> postResponse = sendRequest(restServer, command);
        final HashMap<String, String> actualResult = CMRestToolData.getActualResultAndPlaceInMap(postResponse);
        final HashMap<String, String> expectedResultMap = CMRestToolData.getExpectedResultsMapForFourthENodeBFunctionCommand();
        assertTrue("Expected Response does not match for input of " + command, postResponse.get(0).contains(expectedResponse));
        assertTrue("FDN doesn't Match expected result", actualResult.get("FDN").equals(expectedResultMap.get("FDN")));
    }

    /**
     * Verify that it is possible to create Attributes of Type MO Ref
     * 
     * @DESCRIPTION Verify that it is possible to create Attributes of Type MORef:
     * @PRIORITY HIGH
     * @GROUP GAT
     * @VUsers
     * @Context Rest
     */

    @VUsers(vusers = { 1 })
    @Context(context = { Context.REST })
    @Test(groups = { "Smoke", "GAT", "RV_WORKFLOW" }, dataProvider = "createAttributesOfTypeMORef", dataProviderClass = CMRestToolData.class)
    public void createAttributesOfTypeMORef(final Host restServer, String command, final String expectedResponse) {
        //TODO Solve issue around quotes "" in this command
        command = "create MeContext=MeContext4,ManagedElement=ManagedElement4,ENodeBFunction=ENodeBFunction4,QciTable=QciTable4 QciTableId=4, reservedBy=[\"a=1,b=2\"] -namespace=ERBS_NODE_MODEL -version=3.1.72";
        setTestCase("createAttributesOfTypeMORef ", "Host Id is " + restServer.getIp());
        final List<String> postResponse = sendRequest(restServer, command);
        assertTrue("", postResponse.get(0).contains("[a=1,b=2]\",\"name\":\"reservedBy"));
    }

    /**
     * Verify that it is possible to create Attributes of Type MO Ref
     * 
     * @DESCRIPTION Verify that it is possible to create Attributes of Type MORef:
     * @PRIORITY HIGH
     * @GROUP GAT
     * @VUsers
     * @Context Rest
     */

    @VUsers(vusers = { 1 })
    @Context(context = { Context.REST })
    @Test(groups = { "Smoke", "GAT", "RV_WORKFLOW" }, dataProvider = "getStarMeContextDotMeContextId", dataProviderClass = CMRestToolData.class)
    public void getStarMeContextDotMeContextId(final Host restServer, final String command, final String expectedResponse) {
        setTestCase("getStarMeContextDotMeContextId ", "Host Id is " + restServer.getIp());
        final List<String> postResponse = sendRequest(restServer, command);
        final HashMap<String, String> actualResult = CMRestToolData.getActualResultAndPlaceInMap(postResponse);
        final HashMap<String, String> expectedResultMap = CMRestToolData.getExpectedResultsMapForGetStarMeContextDotMeContextId();
        assertTrue("Expected Response does not match for input of " + command, postResponse.get(0).contains(expectedResponse));
        assertTrue("FDN doesn't Match expected result", actualResult.get("FDN").equals(expectedResultMap.get("FDN")));

    }

    /**
     * Verify that it is possible to run a get command for an MeContext and specify which attributes should be returned when they match a specific
     * value for all MeContexts
     * 
     * @DESCRIPTION Verify that it is possible to run a get command for an MeContext and specify which attributes should be returned when they match a
     *              specific value
     * @PRIORITY HIGH
     * @GROUP GAT
     * @VUsers
     * @Context Rest
     */

    @VUsers(vusers = { 1 })
    @Context(context = { Context.REST })
    @Test(groups = { "Smoke", "GAT", "RV_WORKFLOW" }, dataProvider = "getAllMeContextMeContextIdEqEq", dataProviderClass = CMRestToolData.class)
    public void getAllMeContextMeContextIdEqEq(final Host restServer, final String command, final String expectedResponse) {
        setTestCase("getAllMeContextMeContextIdEqEq ", "Host Id is " + restServer.getIp());
        final List<String> postResponse = sendRequest(restServer, command);
        final HashMap<String, HashMap<String, String>> actualResultMap = CMRestToolData
                .getActualResultAndPlaceInMapForMultipleMoResponse(postResponse);
        final HashMap<String, HashMap<String, String>> expectedDotAttributesEqualsResultMap = CMRestToolData
                .getExpectedResultAllMeContextMeContextIdEqEq();
        final String[] expectedResults = CMRestToolData.getExpectedResultAllMeContextMeContextIdEqEqAttributes();
        compareActualandExpectedResults(actualResultMap, expectedResults);
        CompareAnySelectedAttributes(actualResultMap, expectedResults, expectedDotAttributesEqualsResultMap, "MeContextId");
    }

    /*
     * Following Test Cases added for KAOS User Story TORF-690
     */

    /*
     * The following Test Case verifies KAOS User Story TORF-10209 and TORF-690
     */
    /**
     * Verify DELETE command for deleting an MO with Children returns a failure code when '-ALL' parameter not specified
     * 
     * @DESCRIPTION Goal of the test case is to verify that it is not possible for the user to specify a DELETE command to delete an MO that contains
     *              a Child MO / Number of Child MOs if the parameter '-ALL' is not specified
     * @PRIORITY MEDIUM
     * @GROUP Smoke, GAT, RV_WORKFLOW
     * @VUsers 1,5
     * @Context Rest
     */

    @VUsers(vusers = { 1 })
    @Context(context = { Context.REST })
    @Test(groups = { "Smoke", "GAT", "RV_WORKFLOW" }, dataProvider = "deleteMOWithChildMONoParmALLExpectingFail", dataProviderClass = CMRestToolData.class)
    public void deleteMOWithChildMONoParmALLExpectingFail(final Host restServer, final String command, final String expectedResponse) {
        setTestCase("deleteMOWithChildMONoParmALLExpectingFail ", "Host Id is " + restServer.getIp());
        final List<String> postResponse = sendRequest(restServer, command);
        final String postResultString = postResponse.get(0);
        assertTrue("Expected a status code of -1 for test (deleteMOWithChildMONoParmALLExpectingFail)" + command, CMRestToolData
                .getcommandResponseDto(postResultString).getStatusCode() == -1);
        assertTrue("Expected Result string not received for test (deleteMOWithChildMONoParmALLExpectingFail)" + command,
                postResultString.contains("Cannot delete") && postResultString.contains("because it has 6 descendant(s)"));

    }

    /**
     * Verify DELETE command with parameter '-ALL' for deleting an MO with Child MO / Number of Child MOs succeeds
     * 
     * @DESCRIPTION Goal of the test case is to verify that it is possible for the user to specify a DELETE command with parameter '-ALL' to delete an
     *              MO that contains a Child MO / Number of hierarchy Child MOs
     * @PRIORITY MEDIUM
     * @GROUP Smoke, GAT, RV_WORKFLOW
     * @VUsers 1,5
     * @Context Rest
     */

    @VUsers(vusers = { 1 })
    @Context(context = { Context.REST })
    @Test(groups = { "Smoke", "GAT", "RV_WORKFLOW" }, dataProvider = "deleteMOWithChildMOWithParmALL", dataProviderClass = CMRestToolData.class)
    public void deleteMOWithChildMOWithParmALL(final Host restServer, final String command, final String expectedResponse) {
        setTestCase("deleteMOWithChildMOWithParmALL ", "Host Id is " + restServer.getIp());
        final List<String> postResponse = sendRequest(restServer, command);
        final String postResultString = postResponse.get(0);
        assertTrue("Cleanup - Delete MO was not sucessful " + command, postResultString.contains("7 instance(s) have been deleted")
                && postResultString.contains("MeContext=123456"));
    }

    /**
     * Verify running a get command for a non-existent MO returns a failure code.
     * 
     * @DESCRIPTION Verify running a get command for a non-existent MO returns a failure code.
     * @PRIORITY HIGH
     * @GROUP
     * @VUsers
     * @Context Rest
     */
    @VUsers(vusers = { 1 })
    @Context(context = { Context.REST })
    @Test(groups = { "Smoke", "GAT", "RV_WORKFLOW" }, dataProvider = "getMOAfterDeleteMOWithParmALLExpectingFail", dataProviderClass = CMRestToolData.class)
    public void getMOAfterDeleteMOWithParmALLExpectingFail(final Host restServer, final String command, final String expectedResponse) {
        setTestCase("getMOAfterDeleteMOWithParmALLExpectingFail ", "Host Id is " + restServer.getIp());
        final List<String> postResponse = sendRequest(restServer, command);
        final String postResultString = postResponse.get(0);
        assertTrue("Expected a status code of -1 for test (getMOAfterDeleteMOWithParmALLExpectingFail)" + command, CMRestToolData
                .getcommandResponseDto(postResultString).getStatusCode() == -1);
    }

    /**
     * Verify DELETE command for deleting an MO
     * 
     * @DESCRIPTION Goal of the test case is to verify that it is possible for the user to specify a DELETE command to delete an MO
     * @PRIORITY MEDIUM
     * @GROUP Smoke, GAT, RV_WORKFLOW
     * @VUsers 1,5
     * @Context Rest
     */

    @VUsers(vusers = { 1 })
    @Context(context = { Context.REST })
    @Test(groups = { "Smoke", "GAT", "RV_WORKFLOW" }, dataProvider = "deleteMO", dataProviderClass = CMRestToolData.class)
    public void deleteMO(final Host restServer, final String command, final String expectedResponse) {
        setTestCase("deleteMO ", "Host Id is " + restServer.getIp());
        final List<String> postResponse = sendRequest(restServer, command);
        final String postResultString = postResponse.get(0);
        assertTrue("Cleanup - Delete MO was not sucessful " + command, postResultString.contains("1 instance(s) have been deleted")
                && postResultString.contains("MeContext=123abc456,ManagedElement=epatmah3,ENodeBFunction=2,EUtranCellFDD=EUC003"));
    }

    /**
     * Verify running a get command for a previously deleted MO returns a failure code.
     * 
     * @DESCRIPTION Verify running a get command for a previously deleted MO returns a failure code.
     * @PRIORITY HIGH
     * @GROUP
     * @VUsers
     * @Context Rest
     */
    @VUsers(vusers = { 1 })
    @Context(context = { Context.REST })
    @Test(groups = { "Smoke", "GAT", "RV_WORKFLOW" }, dataProvider = "getMOAfterDeleteMOExpectingFail", dataProviderClass = CMRestToolData.class)
    public void getMOAfterDeleteMOExpectingFail(final Host restServer, final String command, final String expectedResponse) {
        setTestCase("getMOAfterDeleteMOExpectingFail ", "Host Id is " + restServer.getIp());
        final List<String> postResponse = sendRequest(restServer, command);

        final String postResultString = postResponse.get(0);
        assertTrue("Expected a status code of -1 for test (getMOAfterDeleteMOExpectingFail)" + command,
                CMRestToolData.getcommandResponseDto(postResultString).getStatusCode() == -1);

    }

    /**
     * Verify running a DELETE command for a non-existent MO fails
     * 
     * @DESCRIPTION Verify running a DELETE command for a non-existent MO returns a failure code.
     * @PRIORITY HIGH
     * @GROUP
     * @VUsers
     * @Context Rest
     */
    @VUsers(vusers = { 1 })
    @Context(context = { Context.REST })
    @Test(groups = { "Smoke", "GAT", "RV_WORKFLOW" }, dataProvider = "deleteNonExistentMOExpectingFail", dataProviderClass = CMRestToolData.class)
    public void deleteNonExistentMOExpectingFail(final Host restServer, final String command, final String expectedResponse) {
        setTestCase("deleteNonExistentMOExpectingFail ", "Host Id is " + restServer.getIp());
        final List<String> postResponse = sendRequest(restServer, command);

        final String postResultString = postResponse.get(0);
        assertTrue("Expected a status code of -1 for test (deleteNonExistentMO)" + command, CMRestToolData.getcommandResponseDto(postResultString)
                .getStatusCode() == -1);
    }

    /**
     * Verify DELETE command with parameter '-ALL' for deleting a Parent MO with one Child succeeds
     * 
     * 
     * @DESCRIPTION Goal of the test case is to verify that it is possible for the user to specify a DELETE command with parameter '-ALL' to delete an
     *              MO that contains a Child MO
     * @PRIORITY MEDIUM
     * @GROUP Smoke, GAT, RV_WORKFLOW
     * @VUsers 1,5
     * @Context Rest
     */

    @VUsers(vusers = { 1 })
    @Context(context = { Context.REST })
    @Test(groups = { "Smoke", "GAT", "RV_WORKFLOW" }, dataProvider = "deleteMOWithOneChildMOWithParmALL", dataProviderClass = CMRestToolData.class)
    public void deleteMOWithOneChildMOWithParmALL(final Host restServer, final String command, final String expectedResponse) {
        setTestCase("deleteMOWithOneChildMOWithParmALL ", "Host Id is " + restServer.getIp());
        final List<String> postResponse = sendRequest(restServer, command);
        final String postResultString = postResponse.get(0);
        assertTrue("Cleanup - Delete MO was not sucessful " + command, postResultString.contains("3 instance(s) have been deleted")
                && postResultString.contains("MeContext=MeContext4,ManagedElement=ManagedElement4"));
    }

    /**
     * Verify DELETE command with parameter '-ALL' for deleting an MO with no Child succeeds
     * 
     * 
     * @DESCRIPTION Goal of the test case is to verify that it is possible for the user to specify a DELETE command with parameter '-ALL' to delete an
     *              MO that contains no Child MO
     * @PRIORITY MEDIUM
     * @GROUP Smoke, GAT, RV_WORKFLOW
     * @VUsers 1,5
     * @Context Rest
     */

    @VUsers(vusers = { 1 })
    @Context(context = { Context.REST })
    @Test(groups = { "Smoke", "GAT", "RV_WORKFLOW" }, dataProvider = "deleteMOWithNoChildMOWithParmALL", dataProviderClass = CMRestToolData.class)
    public void deleteMOWithNoChildMOWithParmALL(final Host restServer, final String command, final String expectedResponse) {
        setTestCase("deleteMOWithNoChildMOWithParmALL ", "Host Id is " + restServer.getIp());
        final List<String> postResponse = sendRequest(restServer, command);
        final String postResultString = postResponse.get(0);
        assertTrue("Cleanup - Delete MO was not sucessful " + command, postResultString.contains("1 instance(s) have been deleted")
                && postResultString.contains("MeContext=MeContext3, ManagedElement=ManagedElement3, ENodeBFunction=3"));
    }

    /**
     * Cleanup - Delete All Test MOIs
     * 
     * 
     * @DESCRIPTION Goal of the test case is cleanup all Test MOIs
     * @PRIORITY MEDIUM
     * @GROUP Smoke, GAT, RV_WORKFLOW
     * @VUsers 1,5
     * @Context Rest
     */

    @VUsers(vusers = { 1 })
    @Context(context = { Context.REST })
    @Test(groups = { "Smoke", "GAT", "RV_WORKFLOW" }, dataProvider = "delete123abc456ALL", dataProviderClass = CMRestToolData.class)
    public void delete123abc456ALL(final Host restServer, final String command, final String expectedResponse) {
        setTestCase("delete123abc456ALL ", "Host Id is " + restServer.getIp());
        final List<String> postResponse = sendRequest(restServer, command);
        final String postResultString = postResponse.get(0);
        assertTrue("Cleanup - Delete MO was not sucessful " + command, postResultString.contains("5 instance(s) have been deleted")
                && postResultString.contains("MeContext=123abc456"));
    }

    /**
     * Cleanup - Delete MeContext=123456 & Children
     * 
     * 
     * @DESCRIPTION Goal of the test case is to cleanup MeContext=123456 & children
     * @PRIORITY MEDIUM
     * @GROUP Smoke, GAT, RV_WORKFLOW
     * @VUsers 1,5
     * @Context Rest
     */

    @VUsers(vusers = { 1 })
    @Context(context = { Context.REST })
    @Test(groups = { "Smoke", "GAT", "RV_WORKFLOW" }, dataProvider = "delete123456ALL", dataProviderClass = CMRestToolData.class)
    public void delete123456ALL(final Host restServer, final String command, final String expectedResponse) {
        setTestCase("delete123456ALL ", "Host Id is " + restServer.getIp());
        final List<String> postResponse = sendRequest(restServer, command);
        final String postResultString = postResponse.get(0);
        assertTrue("Cleanup - Delete MO was not sucessful " + command, postResultString.contains("3 instance(s) have been deleted")
                && postResultString.contains("MeContext=123456"));
    }

    /**
     * Cleanup - Delete All Test MOIs
     * 
     * 
     * @DESCRIPTION Goal of the test case is cleanup all Test MOIs
     * @PRIORITY MEDIUM
     * @GROUP Smoke, GAT, RV_WORKFLOW
     * @VUsers 1,5
     * @Context Rest
     */

    @VUsers(vusers = { 1 })
    @Context(context = { Context.REST })
    @Test(groups = { "Smoke", "GAT", "RV_WORKFLOW" }, dataProvider = "deleteMeContext3ALL", dataProviderClass = CMRestToolData.class)
    public void deleteMeContext3ALL(final Host restServer, final String command, final String expectedResponse) {
        setTestCase("deleteMeContext3ALL ", "Host Id is " + restServer.getIp());
        final List<String> postResponse = sendRequest(restServer, command);
        final String postResultString = postResponse.get(0);
        assertTrue("Cleanup - Delete MO was not sucessful " + command, postResultString.contains("2 instance(s) have been deleted")
                && postResultString.contains("MeContext=MeContext3"));
    }

    /**
     * Cleanup - Delete All Test MOIs
     * 
     * 
     * @DESCRIPTION Goal of the test case is cleanup all Test MOIs
     * @PRIORITY MEDIUM
     * @GROUP Smoke, GAT, RV_WORKFLOW
     * @VUsers 1,5
     * @Context Rest
     */

    @VUsers(vusers = { 1 })
    @Context(context = { Context.REST })
    @Test(groups = { "Smoke", "GAT", "RV_WORKFLOW" }, dataProvider = "deleteMeContext4ALL", dataProviderClass = CMRestToolData.class)
    public void deleteMeContext4ALL(final Host restServer, final String command, final String expectedResponse) {
        setTestCase("deleteMeContext4ALL ", "Host Id is " + restServer.getIp());
        final List<String> postResponse = sendRequest(restServer, command);
        final String postResultString = postResponse.get(0);
        assertTrue("Cleanup - Delete MO was not sucessful " + command, postResultString.contains("1 instance(s) have been deleted")
                && postResultString.contains("MeContext=MeContext4"));
    }

    /* End of Test Cases added for KAOS User Story TORF-690 */

    /*
     * Following Code added for KAOS TORF-1350, TORF-1351, TORF-1352, TORF-1353, TORF-1356
     */

    /*
     * data base setup method
     * 
     * should be called from the first test of a group of tests (e.g deleteMOsWithCriteria)
     */
    private void setupDatabase(final Host restServer) {

        //clean conflicting MOs (if any)
        cleanupDatabase(restServer);

        //create MeContext
        sendRequest(restServer,
                "create MeContext=KAOS001 MeContextId=1,neType=ENODEB, generationCounter=2, mirrorSynchronizationStatus=SYNCING -namespace=OSS_TOP -version=1.1.0");
        sendRequest(restServer,
                "create MeContext=KAOS002 MeContextId=1,neType=ENODEB, generationCounter=2, mirrorSynchronizationStatus=SYNCING -namespace=OSS_TOP -version=1.1.0");

        //create ManagedElement
        sendRequest(restServer, "create MeContext=KAOS001,ManagedElement=1 ManagedElementId=1 -namespace=CPP_NODE_MODEL -version=3.12.0");
        sendRequest(restServer, "create MeContext=KAOS002,ManagedElement=1 ManagedElementId=1 -namespace=CPP_NODE_MODEL -version=3.12.0");

        //create SectorEquipmentFunction
        sendRequest(restServer,
                "create MeContext=KAOS001,ManagedElement=1,SectorEquipmentFunction=1 SectorEquipmentFunctionId=1 -namespace=ERBS_NODE_MODEL -version=3.1.72");
        sendRequest(restServer,
                "create MeContext=KAOS002,ManagedElement=1,SectorEquipmentFunction=1 SectorEquipmentFunctionId=1 -namespace=ERBS_NODE_MODEL -version=3.1.72");

        //create ENodeBFunction
        sendRequest(
                restServer,
                "create MeContext=KAOS001,ManagedElement=1,ENodeBFunction=1 ENodeBFunctionId=1, userLabel=\"EQUALS\", dscpLabel=21 -namespace=ERBS_NODE_MODEL -version=3.1.72");
        sendRequest(
                restServer,
                "create MeContext=KAOS002,ManagedElement=1,ENodeBFunction=1 ENodeBFunctionId=1, userLabel=\"EQUALS\", dscpLabel=21 -namespace=ERBS_NODE_MODEL -version=3.1.72");

        //create EUtranCellFDD
        sendRequest(
                restServer,
                "create MeContext=KAOS001,ManagedElement=1,ENodeBFunction=1,EUtranCellFDD=1 EUtranCellFDDId=1,noOfPucchSrUsers=160, cellSubscriptionCapacity=72,physicalLayerCellIdGroup=1, cellId=1, earfcndl=1, tac=65535, earfcnul=18000, physicalLayerSubCellId=1, sectorFunctionRef=\"MeContext=KAOS001,ManagedElement=1,SectorEquipmentFunction=1\" -namespace=ERBS_NODE_MODEL -version=3.1.72");
        sendRequest(
                restServer,
                "create MeContext=KAOS002,ManagedElement=1,ENodeBFunction=1,EUtranCellFDD=1 EUtranCellFDDId=1,noOfPucchSrUsers=161, cellSubscriptionCapacity=71,physicalLayerCellIdGroup=1, cellId=1, earfcndl=1, tac=65535, earfcnul=18000, physicalLayerSubCellId=1, sectorFunctionRef=\"MeContext=KAOS002,ManagedElement=1,SectorEquipmentFunction=1\" -namespace=ERBS_NODE_MODEL -version=3.1.72");
    }

    /*
     * data base cleanup method
     * 
     * called from the last test of a group of tests (e.g deleteMOsWithCriteriaHavingChildFail )
     */
    private void cleanupDatabase(final Host restServer) {
        sendRequest(restServer, "delete MeContext=KAOS001 -ALL");
        sendRequest(restServer, "delete MeContext=KAOS002 -ALL");
    }

    @VUsers(vusers = { 1 })
    @Context(context = { Context.REST })
    @Test(groups = { "Smoke", "GAT", "RV_WORKFLOW" }, dataProvider = "deleteMOsWithCriteria", dataProviderClass = CMRestToolData.class)
    public void deleteMOsWithCriteria(final Host restServer, final String command, final String expectedResponse) {
        setTestCase("deleteMOsWithCriteria ", "Host Id is " + restServer.getIp());

        //setup data base
        setupDatabase(restServer);

        final List<String> postResponse = sendRequest(restServer, command);

        final String postResultString = postResponse.get(0);
        final String expectedString = "1 instance(s)";

        assertTrue("Expected statusMessage to contain" + " \" " + command, postResultString.contains(expectedString));

    }

    @VUsers(vusers = { 1 })
    @Context(context = { Context.REST })
    @Test(groups = { "Smoke", "GAT", "RV_WORKFLOW" }, dataProvider = "deleteMOsWithCriteriaHavingChildFail", dataProviderClass = CMRestToolData.class)
    public void deleteMOsWithCriteriaHavingChildFail(final Host restServer, final String command, final String expectedResponse) {
        setTestCase("deleteMOsWithCriteriaHavingChildFail ", "Host Id is " + restServer.getIp());
        final List<String> postResponse = sendRequest(restServer, command);
        final String postResultString = postResponse.get(0);
        final String expectedString = "Cannot delete 1 because it has 1 descendant(s)";

        assertTrue("Expected a status code of -1 for test (deleteMOsWithCriteriaHavingChildFail) " + command,
                CMRestToolData.getcommandResponseDto(postResultString).getStatusCode() == -1);
        assertTrue("Expected result string contains \"" + expectedString + "\"", CMRestToolData.getcommandResponseDto(postResultString)
                .getStatusMessage().contains(expectedString));
    }

    @VUsers(vusers = { 1 })
    @Context(context = { Context.REST })
    @Test(groups = { "Smoke", "GAT", "RV_WORKFLOW" }, dataProvider = "deleteMOsWithCriteriaHavingChildALLParamFail", dataProviderClass = CMRestToolData.class)
    public void deleteMOsWithCriteriaHavingChildALLParamFail(final Host restServer, final String command, final String expectedResponse) {
        setTestCase("deleteMOsWithCriteriaHavingChildALLParamFail ", "Host Id is " + restServer.getIp());
        final List<String> postResponse = sendRequest(restServer, command);
        final String postResultString = postResponse.get(0);
        final String expectedString = "Cannot delete 1 because it has 1 descendant(s)";

        //clean data base
        cleanupDatabase(restServer);

        assertTrue("Expected a status code of -1 for test (deleteMOsWithCriteriaHavingChildFail) " + command,
                CMRestToolData.getcommandResponseDto(postResultString).getStatusCode() == -1);
        assertTrue("Expected result string contains \"" + expectedString + "\"", CMRestToolData.getcommandResponseDto(postResultString)
                .getStatusMessage().contains(expectedString));

    }

    @VUsers(vusers = { 1 })
    @Context(context = { Context.REST })
    @Test(groups = { "Smoke", "GAT", "RV_WORKFLOW" }, dataProvider = "setMultipleMOsWithFilteredOnRangeOfAttributeValues", dataProviderClass = CMRestToolData.class)
    public void setMultipleMOsWithFilteredOnRangeOfAttributeValues(final Host restServer, final String command, final String expectedResponse) {
        setTestCase("setMultipleMOsWithFilteredOnRangeOfAttributeValues ", "Host Id is " + restServer.getIp());

        //setup data base
        setupDatabase(restServer);
        final List<String> postResponse = sendRequest(restServer, command);

        final String postResultString = postResponse.get(0);
        assertTrue("Expected statusMessage to contain" + " \"" + "2 instance(s) updated" + "\" " + command,
                postResultString.contains("2 instance(s) updated"));

    }

    @VUsers(vusers = { 1 })
    @Context(context = { Context.REST })
    @Test(groups = { "Smoke", "GAT", "RV_WORKFLOW" }, dataProvider = "setMultipleMOsWithFilteredOnRangeOfAttributeValuesWithUnsupportedComparatorFail", dataProviderClass = CMRestToolData.class)
    public void setMultipleMOsWithFilteredOnRangeOfAttributeValuesWithUnsupportedComparatorFail(final Host restServer, final String command,
                                                                                                final String expectedResponse) {
        setTestCase("setMultipleMOsWithFilteredOnRangeOfAttributeValuesWithUnsupportedComparatorFail ", "Host Id is " + restServer.getIp());
        final List<String> postResponse = sendRequest(restServer, command);

        //clean data base
        cleanupDatabase(restServer);

        final String postResultString = postResponse.get(0);
        assertTrue("Expected a status code of -3 for test (setMultipleMOsWithFilteredOnRangeOfAttributeValuesWithUnsupportedComparatorFail) "
                + command, CMRestToolData.getcommandResponseDto(postResultString).getStatusCode() == -3);
    }

    //TODO The following block of tests are specifically for Performance Testing purposes and are not intended to be part of the main suite as they use a specific data set.
    //------------------------------------------Performance Tests-------------------------------------------------------------
    //TODO Depending on results, at least during the initial benchmarking stage, it would probably be advisable for these tests to also be run manually.

    @VUsers(vusers = { 1 })
    @Context(context = { Context.REST })
    @Test(groups = { "Smoke", "GAT", "RV_WORKFLOW" }, dataProvider = "getASpecificMeContextDotStar", dataProviderClass = CMRestToolData.class)
    public void getASpecificMeContextDotStar(final Host restServer, final String command, final String expectedResponse) {
        setTestCase("getASpecificMeContextDotStar ", "Host Id is " + restServer.getIp());
        final List<String> postResponse = sendRequest(restServer, command);
        assertTrue("Response size is 0 ", postResponse.size() > 0);
        //assertTrue ("Reponse did not indicate a success", postResponse.get(0).endsWith("\"statusMessage\": \"Success\"}"));
    }

    @VUsers(vusers = { 1 })
    @Context(context = { Context.REST })
    @Test(groups = { "Smoke", "GAT", "RV_WORKFLOW" }, dataProvider = "getStarMeContextDotStar", dataProviderClass = CMRestToolData.class)
    public void getStarMeContextDotStar(final Host restServer, final String command, final String expectedResponse) {
        setTestCase("getStarMeContextDotStar ", "Host Id is " + restServer.getIp());
        final List<String> postResponse = sendRequest(restServer, command);
        assertTrue("Response size is 0 ", postResponse.size() > 0);
        //assertTrue ("Reponse did not indicate a success", postResponse.get(0).endsWith("\"statusMessage\": \"Success\"}"));
    }

    @VUsers(vusers = { 1 })
    @Context(context = { Context.REST })
    @Test(groups = { "Smoke", "GAT", "RV_WORKFLOW" }, dataProvider = "getStarManagedElementDotStar", dataProviderClass = CMRestToolData.class)
    public void getStarManagedElementDotStar(final Host restServer, final String command, final String expectedResponse) {
        setTestCase("getStarManagedElementDotStar ", "Host Id is " + restServer.getIp());
        final List<String> postResponse = sendRequest(restServer, command);
        assertTrue("Response size is 0 ", postResponse.size() > 0);
        //assertTrue ("Reponse did not indicate a success", postResponse.get(0).endsWith("\"statusMessage\": \"Success\"}"));
    }

    @VUsers(vusers = { 1 })
    @Context(context = { Context.REST })
    @Test(groups = { "Smoke", "GAT", "RV_WORKFLOW" }, dataProvider = "getStarENodeBFunctionDotStar", dataProviderClass = CMRestToolData.class)
    public void getStarENodeBFunctionDotStar(final Host restServer, final String command, final String expectedResponse) {
        setTestCase("getStarENodeBFunctionDotStar ", "Host Id is " + restServer.getIp());
        final List<String> postResponse = sendRequest(restServer, command);
        assertTrue("Response size is 0 ", postResponse.size() > 0);
        //assertTrue ("Reponse did not indicate a success", postResponse.get(0).endsWith("\"statusMessage\": \"Success\"}"));
    }

    @VUsers(vusers = { 1 })
    @Context(context = { Context.REST })
    @Test(groups = { "Smoke", "GAT", "RV_WORKFLOW" }, dataProvider = "getStarTermPointToMmeDotStar", dataProviderClass = CMRestToolData.class)
    public void getStarTermPointToMmeDotStar(final Host restServer, final String command, final String expectedResponse) {
        setTestCase("getStarTermPointToMmeDotStar ", "Host Id is " + restServer.getIp());
        final List<String> postResponse = sendRequest(restServer, command);
        assertTrue("Response size is 0 ", postResponse.size() > 0);
        //assertTrue ("Reponse did not indicate a success", postResponse.get(0).endsWith("\"statusMessage\": \"Success\"}"));
    }

    @VUsers(vusers = { 1 })
    @Context(context = { Context.REST })
    @Test(groups = { "Smoke", "GAT", "RV_WORKFLOW" }, dataProvider = "getStarExternalCdma2000CellDotStar", dataProviderClass = CMRestToolData.class)
    public void getStarExternalCdma2000CellDotStar(final Host restServer, final String command, final String expectedResponse) {
        setTestCase("getStarExternalCdma2000CellDotStar ", "Host Id is " + restServer.getIp());
        final List<String> postResponse = sendRequest(restServer, command);
        assertTrue("Response size is 0 ", postResponse.size() > 0);
        //assertTrue ("Reponse did not indicate a success", postResponse.get(0).endsWith("\"statusMessage\": \"Success\"}"));
    }

    @VUsers(vusers = { 1 })
    @Context(context = { Context.REST })
    @Test(groups = { "Smoke", "GAT", "RV_WORKFLOW" }, dataProvider = "getQciProfilePredefinedDotStar", dataProviderClass = CMRestToolData.class)
    public void getQciProfilePredefinedDotStar(final Host restServer, final String command, final String expectedResponse) {
        setTestCase("getQciProfilePredefinedDotStar ", "Host Id is " + restServer.getIp());
        final List<String> postResponse = sendRequest(restServer, command);
        assertTrue("Response size is 0 ", postResponse.size() > 0);
        //assertTrue ("Reponse did not indicate a success", postResponse.get(0).endsWith("\"statusMessage\": \"Success\"}"));
    }

    @VUsers(vusers = { 1 })
    @Context(context = { Context.REST })
    @Test(groups = { "Smoke", "GAT", "RV_WORKFLOW" }, dataProvider = "getIpSyncRefDotStar", dataProviderClass = CMRestToolData.class)
    public void getIpSyncRefDotStar(final Host restServer, final String command, final String expectedResponse) {
        setTestCase("getIpSyncRefDotStar ", "Host Id is " + restServer.getIp());
        final List<String> postResponse = sendRequest(restServer, command);
        assertTrue("Response size is 0 ", postResponse.size() > 0);
        //assertTrue ("Reponse did not indicate a success", postResponse.get(0).endsWith("\"statusMessage\": \"Success\"}"));
    }

    @VUsers(vusers = { 1 })
    @Context(context = { Context.REST })
    @Test(groups = { "Smoke", "GAT", "RV_WORKFLOW" }, dataProvider = "getLTE01ERBS1StarENodeBFunctionDotStar", dataProviderClass = CMRestToolData.class)
    public void getLTE01ERBS1StarENodeBFunctionDotStar(final Host restServer, final String command, final String expectedResponse) {
        setTestCase("getLTE01ERBS1StarENodeBFunctionDotStar ", "Host Id is " + restServer.getIp());
        final List<String> postResponse = sendRequest(restServer, command);
        assertTrue("Response size is 0 ", postResponse.size() > 0);
        //assertTrue ("Reponse did not indicate a success", postResponse.get(0).endsWith("\"statusMessage\": \"Success\"}"));
    }

    @VUsers(vusers = { 1 })
    @Context(context = { Context.REST })
    @Test(groups = { "Smoke", "GAT", "RV_WORKFLOW" }, dataProvider = "getLTE01ERBS42StarENodeBFunctionDotStar", dataProviderClass = CMRestToolData.class)
    public void getLTE01ERBS42StarENodeBFunctionDotStar(final Host restServer, final String command, final String expectedResponse) {
        setTestCase("getLTE01ERBS42StarENodeBFunctionDotStar ", "Host Id is " + restServer.getIp());
        final List<String> postResponse = sendRequest(restServer, command);
        assertTrue("Response size is 0 ", postResponse.size() > 0);
        //assertTrue ("Reponse did not indicate a success", postResponse.get(0).endsWith("\"statusMessage\": \"Success\"}"));
    }

    @VUsers(vusers = { 1 })
    @Context(context = { Context.REST })
    @Test(groups = { "Smoke", "GAT", "RV_WORKFLOW" }, dataProvider = "getLTE01ERBS429StarENodeBFunctionDotStar", dataProviderClass = CMRestToolData.class)
    public void getLTE01ERBS429StarENodeBFunctionDotStar(final Host restServer, final String command, final String expectedResponse) {
        setTestCase("getLTE01ERBS429StarENodeBFunctionDotStar ", "Host Id is " + restServer.getIp());
        final List<String> postResponse = sendRequest(restServer, command);
        assertTrue("Response size is 0 ", postResponse.size() > 0);
        //assertTrue ("Reponse did not indicate a success", postResponse.get(0).endsWith("\"statusMessage\": \"Success\"}"));
    }

    @VUsers(vusers = { 1 })
    @Context(context = { Context.REST })
    @Test(groups = { "Smoke", "GAT", "RV_WORKFLOW" }, dataProvider = "getLTE01ERBS42StarExternalCdma2000CellDotStar", dataProviderClass = CMRestToolData.class)
    public void getLTE01ERBS42StarExternalCdma2000CellDotStar(final Host restServer, final String command, final String expectedResponse) {
        setTestCase("getLTE01ERBS42StarExternalCdma2000CellDotStar ", "Host Id is " + restServer.getIp());
        final List<String> postResponse = sendRequest(restServer, command);
        assertTrue("Response size is 0 ", postResponse.size() > 0);
        //assertTrue ("Reponse did not indicate a success", postResponse.get(0).endsWith("\"statusMessage\": \"Success\"}"));
    }

    @VUsers(vusers = { 1 })
    @Context(context = { Context.REST })
    @Test(groups = { "Smoke", "GAT", "RV_WORKFLOW" }, dataProvider = "getLTE01ERBS3StarTermPointToMmeDotStar", dataProviderClass = CMRestToolData.class)
    public void getLTE01ERBS3StarTermPointToMmeDotStar(final Host restServer, final String command, final String expectedResponse) {
        setTestCase("getLTE01ERBS3StarTermPointToMmeDotStar ", "Host Id is " + restServer.getIp());
        final List<String> postResponse = sendRequest(restServer, command);
        assertTrue("Response size is 0 ", postResponse.size() > 0);
        //assertTrue ("Reponse did not indicate a success", postResponse.get(0).endsWith("\"statusMessage\": \"Success\"}"));
    }

    //---------------------------------------end of performance tests-------------------------------------------------

}
