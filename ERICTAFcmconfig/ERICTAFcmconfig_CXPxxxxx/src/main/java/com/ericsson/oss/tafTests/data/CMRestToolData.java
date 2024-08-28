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
package com.ericsson.oss.tafTests.data;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;

import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.testng.annotations.DataProvider;

import com.ericsson.oss.tafTests.data.CMRestToolDataProvider;
import com.ericsson.cifwk.taf.TestData;
import com.ericsson.cifwk.taf.data.Host;
import com.ericsson.oss.services.scriptengine.spi.dtos.AbstractDto;
import com.ericsson.oss.services.scriptengine.spi.dtos.CommandResponseDto;
import com.ericsson.oss.services.scriptengine.spi.dtos.ListDto;
import com.ericsson.oss.services.scriptengine.spi.dtos.NameValueDto;

/*
 * Goal of TestData class is to fetch data from Data Provider class and prepare for test method
 * It contains methods annotated with @DataProvider and name used in @Test annotation on test method
 */
/**
 * @author ERONKEO
 * 
 */
public class CMRestToolData implements TestData {

    public static final String expectedX2BlackList = "[{mcc=1, mnc=123, mncLength=2, enbId=1}, {mcc=2, mnc=258, mncLength=3, enbId=2}]\",\"name\":\"x2BlackList\"}";

    public static List<List<String>> getMeContextList = CMRestToolDataProvider.getDataFromCSV("CommandData.csv", "getMeContext");
    public static List<List<String>> getMeContextAndManagedElementList = CMRestToolDataProvider.getDataFromCSV("CommandData.csv",
            "getMeContextAndManagedElement");
    public static List<List<String>> createMeContextList = CMRestToolDataProvider.getDataFromCSV("CommandData.csv", "createMeContext");
    public static List<List<String>> createMeContextAndManagedElementList = CMRestToolDataProvider.getDataFromCSV("CommandData.csv",
            "createMeContextAndManagedElement");
    public static List<List<String>> createMeContextAndManagedElementWithInvalidParentList = CMRestToolDataProvider.getDataFromCSV("CommandData.csv",
            "createMeContextAndManagedElementWithInvalidParent");
    public static List<List<String>> getMeContextWhichDoesNotExistList = CMRestToolDataProvider.getDataFromCSV("CommandData.csv",
            "getMeContextWhichDoesNotExist");
    public static List<List<String>> invalidParentChildSituationList = CMRestToolDataProvider.getDataFromCSV("CommandData.csv",
            "invalidParentChildSituation");
    public static List<List<String>> createMeContextWith150CharacterLongNameList = CMRestToolDataProvider.getDataFromCSV("CommandData.csv",
            "createMeContextWith150CharacterLongName");
    public static List<List<String>> create_MeContext_And_Managed_Element_And_ENodeBFunctionList = CMRestToolDataProvider.getDataFromCSV(
            "CommandData.csv", "create_MeContext_And_Managed_Element_And_ENodeBFunction");
    /*
     * Following Test Cases added for KAOS User Stories to create a SectorEquipmentFunction and EUtranCellFDD
     */

    public static List<List<String>> create_MeContext_And_Managed_Element_And_ENodeBFunction_And_SectorEquipmentFunctionList = CMRestToolDataProvider
            .getDataFromCSV("CommandData.csv", "create_MeContext_And_Managed_Element_And_ENodeBFunction_And_SectorEquipmentFunction");
    public static List<List<String>> create_MeContext_And_Managed_Element_And_ENodeBFunction_And_SectorEquipmentFunction_MeContext_123abc456List = CMRestToolDataProvider
            .getDataFromCSV("CommandData.csv",
                    "create_MeContext_And_Managed_Element_And_ENodeBFunction_And_SectorEquipmentFunction_MeContext_123abc456");
    public static List<List<String>> createFirstEUtranCellFDDList = CMRestToolDataProvider.getDataFromCSV("CommandData.csv",
            "createFirstEUtranCellFDD");
    public static List<List<String>> createSecondEUtranCellFDDList = CMRestToolDataProvider.getDataFromCSV("CommandData.csv",
            "createSecondEUtranCellFDD");
    public static List<List<String>> createThirdEUtranCellFDDList = CMRestToolDataProvider.getDataFromCSV("CommandData.csv",
            "createThirdEUtranCellFDD");
    public static List<List<String>> searchWithAttributeFilterOnGreaterThanList = CMRestToolDataProvider.getDataFromCSV("CommandData.csv",
            "searchWithAttributeFilterOnGreaterThanReturnsCorrectFDNs");

    /*
     * End of Test Cases added for KAOS User Stories to create a SectorEquipmentFunction and EUtranCellFDD
     */
    public static List<List<String>> get_MeContext_And_ManagedElement_And_ENodeBFunction_Expecting_False_As_Result_List = CMRestToolDataProvider
            .getDataFromCSV("CommandData.csv", "get_MeContext_And_ManagedElement_And_ENodeBFunction_Expecting_False_As_Result");
    public static List<List<String>> createEnodeBFunctionChildMOList = CMRestToolDataProvider.getDataFromCSV("CommandData.csv",
            "createEnodeBFunctionChildMO");
    public static List<List<String>> create_EnodeBFunction_With_ChildMO_From_ManagedElement_Expecting_Fail_As_Result_List = CMRestToolDataProvider
            .getDataFromCSV("CommandData.csv", "create_EnodeBFunction_With_ChildMO_From_ManagedElement_Expecting_Fail_As_Result");
    public static List<List<String>> createENodeBFunctionWithComplexAttributesList = CMRestToolDataProvider.getDataFromCSV("CommandData.csv",
            "createENodeBFunctionWithComplexAttributes");
    public static List<List<String>> getStarENodeBFunctionList = CMRestToolDataProvider.getDataFromCSV("CommandData.csv", "getStarENodeBFunction");
    public static List<List<String>> getSpecificENodeBFunctionList = CMRestToolDataProvider.getDataFromCSV("CommandData.csv",
            "getSpecificENodeBFunction");
    public static List<List<String>> getENodeBFunctionNameFollowedByStarList = CMRestToolDataProvider.getDataFromCSV("CommandData.csv",
            "getENodeBFunctionNameFollowedByStar");
    public static List<List<String>> getENodeBFunctionNamePreceededByStarList = CMRestToolDataProvider.getDataFromCSV("CommandData.csv",
            "getENodeBFunctionNamePreceededByStar");
    public static List<List<String>> getENodeBFunctionDotStarList = CMRestToolDataProvider.getDataFromCSV("CommandData.csv",
            "getENodeBFunctionDotStar");
    public static List<List<String>> getENodeBFunctionDotAttributesList = CMRestToolDataProvider.getDataFromCSV("CommandData.csv",
            "getENodeBFunctionDotAttributes");
    public static List<List<String>> createASecondMeContextInstanceList = CMRestToolDataProvider.getDataFromCSV("CommandData.csv",
            "createASecondMeContextInstance");
    public static List<List<String>> createASecondManangedElementInstanceList = CMRestToolDataProvider.getDataFromCSV("CommandData.csv",
            "createASecondManangedElementInstance");
    public static List<List<String>> getENodeBFunctionDotAttributeEqualsValueVerifyingDnsLookupOnTaiList = CMRestToolDataProvider.getDataFromCSV(
            "CommandData.csv", "getENodeBFunctionDotAttributeEqualsValueVerifyingDnsLookupOnTai");
    public static List<List<String>> getENodeBFunctionDotAttributeEqualsValueVerifyingDnsLookupTimerList = CMRestToolDataProvider.getDataFromCSV(
            "CommandData.csv", "getENodeBFunctionDotAttributeEqualsValueVerifyingDnsLookupTimer");
    public static List<List<String>> getENodeBFunctionDotAttributeEqualsValueVerifyingUserLabelList = CMRestToolDataProvider.getDataFromCSV(
            "CommandData.csv", "getENodeBFunctionDotAttributeEqualsValueVerifyingUserLabel");
    public static List<List<String>> getENodeBFunctionDotAttributeEqualsValueVerifyingUserLabelAndDnsLookupTimerCombinedList = CMRestToolDataProvider
            .getDataFromCSV("CommandData.csv", "getENodeBFunctionDotAttributeEqualsValueVerifyingUserLabelAndDnsLookupTimerCombined");
    public static List<List<String>> getStarTermPointToMmeDotStarList = CMRestToolDataProvider.getDataFromCSV("CommandData.csv",
            "getStarTermPointToMmeDotStar");
    public static List<List<String>> getStarExternalCdma2000CellDotStarList = CMRestToolDataProvider.getDataFromCSV("CommandData.csv",
            "getStarExternalCdma2000CellDotStar");
    public static List<List<String>> getQciProfilePredefinedDotStarList = CMRestToolDataProvider.getDataFromCSV("CommandData.csv",
            "getQciProfilePredefinedDotStar");
    public static List<List<String>> getIpSyncRefDotStarList = CMRestToolDataProvider.getDataFromCSV("CommandData.csv", "getIpSyncRefDotStar");
    public static List<List<String>> getStarMeContextDotStarList = CMRestToolDataProvider
            .getDataFromCSV("CommandData.csv", "getStarMeContextDotStar");
    public static List<List<String>> getStarManagedElementDotStarList = CMRestToolDataProvider.getDataFromCSV("CommandData.csv",
            "getStarManagedElementDotStar");
    public static List<List<String>> getStarENodeBFunctionDotStarList = CMRestToolDataProvider.getDataFromCSV("CommandData.csv",
            "getStarENodeBFunctionDotStar");
    public static List<List<String>> getASpecificMeContextDotStarList = CMRestToolDataProvider.getDataFromCSV("CommandData.csv",
            "getASpecificMeContextDotStar");
    public static List<List<String>> getLTE01ERBS1StarENodeBFunctionDotStarList = CMRestToolDataProvider.getDataFromCSV("CommandData.csv",
            "getLTE01ERBS1StarENodeBFunctionDotStar");
    public static List<List<String>> getLTE01ERBS3StarTermPointToMmeDotStarList = CMRestToolDataProvider.getDataFromCSV("CommandData.csv",
            "getLTE01ERBS3StarTermPointToMmeDotStar");
    public static List<List<String>> getLTE01ERBS42StarExternalCdma2000CellDotStarList = CMRestToolDataProvider.getDataFromCSV("CommandData.csv",
            "getLTE01ERBS42StarExternalCdma2000CellDotStar");
    public static List<List<String>> getLTE01ERBS429StarENodeBFunctionDotStarList = CMRestToolDataProvider.getDataFromCSV("CommandData.csv",
            "getLTE01ERBS429StarENodeBFunctionDotStar");
    public static List<List<String>> getLTE01ERBS42StarENodeBFunctionDotStarList = CMRestToolDataProvider.getDataFromCSV("CommandData.csv",
            "getLTE01ERBS42StarENodeBFunctionDotStar");
    public static List<List<String>> actionMeContextDeleteAllRepertoiresList = CMRestToolDataProvider.getDataFromCSV("CommandData.csv",
            "actionMeContextDeleteAllRepertoires");
    public static List<List<String>> actionMeContextDeleteAllSlotsList = CMRestToolDataProvider.getDataFromCSV("CommandData.csv",
            "actionMeContextDeleteAllSlots");
    public static List<List<String>> actionMeContextSyncsList = CMRestToolDataProvider.getDataFromCSV("CommandData.csv", "actionMeContextSyncs");
    public static List<List<String>> actionMeContextDeleteAllSlotsAllEnodebFuncEqEqEnodebFuncIdList = CMRestToolDataProvider.getDataFromCSV(
            "CommandData.csv", "actionMeContextDeleteAllSlotsAllEnodebFuncEqEqEnodebFuncId");
    public static List<List<String>> createThirdMeContextInstanceList = CMRestToolDataProvider.getDataFromCSV("CommandData.csv",
            "createThirdMeContextInstance");
    public static List<List<String>> createThirdManagedElementInstanceList = CMRestToolDataProvider.getDataFromCSV("CommandData.csv",
            "createThirdManagedElementInstance");
    public static List<List<String>> createSequenceAttributesList = CMRestToolDataProvider.getDataFromCSV("CommandData.csv",
            "createSequenceAttributes");
    public static List<List<String>> createFourthMeContextInstanceList = CMRestToolDataProvider.getDataFromCSV("CommandData.csv",
            "createFourthMeContextInstance");
    public static List<List<String>> createFourthManagedElementInstanceList = CMRestToolDataProvider.getDataFromCSV("CommandData.csv",
            "createFourthManagedElementInstance");
    public static List<List<String>> createAttributesOfTypeMORefList = CMRestToolDataProvider.getDataFromCSV("CommandData.csv",
            "createAttributesOfTypeMORef");
    public static List<List<String>> createFourthENodeBFunctionInstanceList = CMRestToolDataProvider.getDataFromCSV("CommandData.csv",
            "createFourthENodeBFunctionInstance");
    public static List<List<String>> getStarMeContextDotMeContextIdList = CMRestToolDataProvider.getDataFromCSV("CommandData.csv",
            "getStarMeContextDotMeContextId");
    public static List<List<String>> getStarENodeBFunctionNoNameSpaceList = CMRestToolDataProvider.getDataFromCSV("CommandData.csv",
            "getStarENodeBFunctionNoNameSpace");
    public static List<List<String>> getENodeBFunctionNameWithNamepsaceAndVersionList = CMRestToolDataProvider.getDataFromCSV("CommandData.csv",
            "getENodeBFunctionNameWithNamepsaceAndVersion");
    public static List<List<String>> getMultipleNodeIdsList = CMRestToolDataProvider.getDataFromCSV("CommandData.csv", "getMultipleNodeIds");
    /*
     * Following Test Cases added for Team America by EFIAHAN US TORF-8459
     */    
    public static List<List<String>> getStarENodeBFunctionTableFormatList = CMRestToolDataProvider.getDataFromCSV("CommandData.csv",
            "getStarENodeBFunctionTableFormat");
    
    /*
     * Following Test Cases added for Team America by EPATMAH US TORF-6889
     */

    public static List<List<String>> getOneOrMoreChildMOIsList = CMRestToolDataProvider.getDataFromCSV("CommandData.csv", "getOneOrMoreChildMOIs");
    /*
     * Following Test Cases added for Team KAOS by EEIRSH
     */

    /*
     * Following Test Cases added for KAOS User Story TORF-1365
     */
    public static List<List<String>> setMeContextGenerationCounterList = CMRestToolDataProvider.getDataFromCSV("CommandData.csv",
            "setMeContextGenerationCounter");
    public static List<List<String>> setDuplicateMeContextAttributeGenerationCounterToSameValueList = CMRestToolDataProvider.getDataFromCSV(
            "CommandData.csv", "setDuplicateMeContextAttributeGenerationCounterToSameValue");
    public static List<List<String>> setMeContextAttributeUserLabelList = CMRestToolDataProvider.getDataFromCSV("CommandData.csv",
            "setMeContextAttributeUserLabel");
    public static List<List<String>> setMeContextAttributeUserLabelToNullValueList = CMRestToolDataProvider.getDataFromCSV("CommandData.csv",
            "setMeContextAttributeUserLabelToNullValue");
    public static List<List<String>> setAttributednsLookupTimerOnENodeBFunctionList = CMRestToolDataProvider.getDataFromCSV("CommandData.csv",
            "setAttributednsLookupTimerOnENodeBFunction");

    /*
     * Following Test Cases added for KAOS User Story TORF-1354
     */
    public static List<List<String>> setMeContextAttributesList = CMRestToolDataProvider.getDataFromCSV("CommandData.csv",
            "setMeContextListOfAttributes");
    public static List<List<String>> setMeContextDuplicateListOfReorderedAttributesToDifferentValuesList = CMRestToolDataProvider.getDataFromCSV(
            "CommandData.csv", "setMeContextDuplicateListOfReorderedAttributesToDifferentValues");
    public static List<List<String>> setListOfAttributesOnENodeBFunctionList = CMRestToolDataProvider.getDataFromCSV("CommandData.csv",
            "setListOfAttributesOnENodeBFunction");

    /*
     * Following Test Cases added for KAOS User Story TORF-5305
     */
    public static List<List<String>> seteNodeBPlmnIdComplexAttributeList = CMRestToolDataProvider.getDataFromCSV("CommandData.csv",
            "seteNodeBPlmnIdComplexAttribute");
    public static List<List<String>> seteNodeBPlmnIdComplexAttributesToDifferentValuesList = CMRestToolDataProvider.getDataFromCSV("CommandData.csv",
            "seteNodeBPlmnIdComplexAttributesToDifferentValues");

    /*
     * Following Test Cases added for KAOS User Story TORF-6582
     */
    public static List<List<String>> setEnumAttributednsLookupOnTaiENodeBFunctionList = CMRestToolDataProvider.getDataFromCSV("CommandData.csv",
            "setEnumAttributednsLookupOnTaiONENodeBFunction");
    public static List<List<String>> setEnumAttributednsLookupOnTaiOFFENodeBFunctionList = CMRestToolDataProvider.getDataFromCSV("CommandData.csv",
            "setEnumAttributednsLookupOnTaiOFFENodeBFunction");
    public static List<List<String>> setEnumAttributednsLookupOnTaiONAgainENodeBFunctionList = CMRestToolDataProvider.getDataFromCSV(
            "CommandData.csv", "setEnumAttributednsLookupOnTaiONAgainENodeBFunction");
    public static List<List<String>> setEnumAttributennsfModeToSPLMNENodeBFunctionList = CMRestToolDataProvider.getDataFromCSV("CommandData.csv",
            "setEnumAttributennsfModeToSPLMNENodeBFunction");

    /*
     * Following Test Cases added for KAOS User Story TORF-690 and TORF-10209
     */
    public static List<List<String>> deleteMOWithChildMONoParmALLExpectingFailList = CMRestToolDataProvider.getDataFromCSV("CommandData.csv",
            "deleteMOWithChildMONoParmALLExpectingFail");
    public static List<List<String>> deleteMOWithChildMoWithParmALLList = CMRestToolDataProvider.getDataFromCSV("CommandData.csv",
            "deleteMOWithChildMOWithParmALL");
    public static List<List<String>> getMOAfterDeleteMOWithParmALLExpectingFailList = CMRestToolDataProvider.getDataFromCSV("CommandData.csv",
            "getMOAfterDeleteMOWithParmALLExpectingFail");
    public static List<List<String>> deleteMOList = CMRestToolDataProvider.getDataFromCSV("CommandData.csv", "deleteMO");
    public static List<List<String>> getMOAfterDeleteMOExpectingFailList = CMRestToolDataProvider.getDataFromCSV("CommandData.csv",
            "getMOAfterDeleteMOExpectingFail");
    public static List<List<String>> deleteNonExistentMOExpectingFailList = CMRestToolDataProvider.getDataFromCSV("CommandData.csv",
            "deleteNonExistentMOExpectingFail");
    public static List<List<String>> deleteMOWithOneChildMoWithParmALLList = CMRestToolDataProvider.getDataFromCSV("CommandData.csv",
            "deleteMOWithOneChildMOWithParmALL");
    public static List<List<String>> deleteMOWithNoChildMoWithParmALLList = CMRestToolDataProvider.getDataFromCSV("CommandData.csv",
            "deleteMOWithNoChildMOWithParmALL");
    public static List<List<String>> delete123abc456ALLList = CMRestToolDataProvider.getDataFromCSV("CommandData.csv", "delete123abc456ALL");
    public static List<List<String>> delete123456ALLList = CMRestToolDataProvider.getDataFromCSV("CommandData.csv", "delete123456ALL");
    public static List<List<String>> deleteMeContext3ALLList = CMRestToolDataProvider.getDataFromCSV("CommandData.csv", "deleteMeContext3ALL");
    public static List<List<String>> deleteMeContext4ALLList = CMRestToolDataProvider.getDataFromCSV("CommandData.csv", "deleteMeContext4ALL");

    /*
     * Following Test Cases added for KAOS User Story TORF-6924
     */
    public static List<List<String>> setAttributeOnAllNodesOfENodeBFunctionTypeList = CMRestToolDataProvider.getDataFromCSV("CommandData.csv",
            "setAttributeOnAllNodesOfENodeBFunctionType");
    public static List<List<String>> setAttributeOnAllNodesOfSpecifiedENodeBFunctionTypeList = CMRestToolDataProvider.getDataFromCSV(
            "CommandData.csv", "setAttributeOnAllNodesOfSpecifiedENodeBFunctionType");
    public static List<List<String>> setAttributeOnNodesOfENodeBFunctionTypeStartingWithStringList = CMRestToolDataProvider.getDataFromCSV(
            "CommandData.csv", "setAttributeOnNodesOfENodeBFunctionTypeStartingWithString");
    public static List<List<String>> setAttributeOnNodesOfENodeBFunctionTypeEndingWithStringList = CMRestToolDataProvider.getDataFromCSV(
            "CommandData.csv", "setAttributeOnNodesOfENodeBFunctionTypeEndingWithString");

    /*
     * Following Test Cases added for KAOS User Story TORF-7037
     */
    public static List<List<String>> setSequencedComplexDataForSpecifiedNodeList = CMRestToolDataProvider.getDataFromCSV("CommandData.csv",
            "setSequencedComplexDataForSpecifiedNode");

    /*
     * Following Test Cases added for KAOS User Story TORF-1355
     */
    public static List<List<String>> setSingleValueFALSEForNodeIDFilteredOnSingleAttributeValueCriteriaList = CMRestToolDataProvider.getDataFromCSV(
            "CommandData.csv", "setSingleValueFALSEForNodeIDFilteredOnSingleAttributeValueCriteria");
    public static List<List<String>> setSingleValueTRUEForNodeIDFilteredOnSingleAttributeValueCriteriaList = CMRestToolDataProvider.getDataFromCSV(
            "CommandData.csv", "setSingleValueTRUEForNodeIDFilteredOnSingleAttributeValueCriteria");
    public static List<List<String>> setMultipleValuesForNodeIDFilteredOnSingleAttributeValueCriteriaList = CMRestToolDataProvider.getDataFromCSV(
            "CommandData.csv", "setMultipleValuesForNodeIDFilteredOnSingleAttributeValueCriteria");
    public static List<List<String>> setSingleIntegerValueForNodeIDFilteredOnMultipleAttributeValueCriteriaList = CMRestToolDataProvider
            .getDataFromCSV("CommandData.csv", "setSingleIntegerValueForNodeIDFilteredOnMultipleAttributeValueCriteria");
    public static List<List<String>> setSingleEnumValueForNodeIDFilteredOnMultipleAttributeValueCriteriaList = CMRestToolDataProvider.getDataFromCSV(
            "CommandData.csv", "setSingleEnumValueForNodeIDFilteredOnMultipleAttributeValueCriteria");

    /*
     * Following Test Cases added for KAOS User Story TORF-1358
     */
    public static List<List<String>> setAttributetHODataFwdReorderingOnAllMOInstancesOfENodeBFunctionWithuserLabelOfNodeList = CMRestToolDataProvider
            .getDataFromCSV("CommandData.csv", "setAttributetHODataFwdReorderingOnAllMOInstancesOfENodeBFunctionWithuserLabelOfNode");

    /* End of Test Cases added for KAOS User Story TORF-1358 */

    /*
     * Following Test Cases added for KAOS User Stories TORF-1358/6924/1355/6964
     */
    public static List<List<String>> setAttributeuserLabelOnAllEUtranCellcontainAttributeFilterList = CMRestToolDataProvider.getDataFromCSV(
            "CommandData.csv", "setAttributeuserLabelOnAllEUtranCellcontainAttributeFilter");
    public static List<List<String>> setAttributePartialNodeFilteruserLabelOnEUtranCellFDDcontainsSpecifieduserLabelList = CMRestToolDataProvider
            .getDataFromCSV("CommandData.csv", "setAttributePartialNodeFilteruserLabelOnEUtranCellFDDcontainsSpecifieduserLabel");
    public static List<List<String>> setAttributeulInterferenceManagementActivePartialNodeFilterContainsEUtranCellFDDcontainsSpecifieduserLabelAndulInterferenceManagementActiveFalseList = CMRestToolDataProvider
            .getDataFromCSV("CommandData.csv",
                    "setAttributeulInterferenceManagementActivePartialNodeFilterContainsEUtranCellFDDcontainsSpecifieduserLabelAndulInterferenceManagementActiveFalse");
    public static List<List<String>> setAttributeulChannelBandwidthNodeStarContainsEUtranCellFDDcontainsuserLabelAndulInterferenceManagementActiveFalseList = CMRestToolDataProvider
            .getDataFromCSV("CommandData.csv",
                    "setAttributeulChannelBandwidthNodeStarContainsEUtranCellFDDcontainsuserLabelAndulInterferenceManagementActiveFalse");

    /* End of Test Cases added for KAOS User Stories TORF-1358/6924/1355/6964 */

    /*
     * Start of KAOS TORF-1350, TORF-1351, TORF-1352, TORF-1353
     */
    public static List<List<String>> deleteMOsWithCriteriaList = CMRestToolDataProvider.getDataFromCSV("CommandData.csv", "deleteMOsWithCriteria");
    public static List<List<String>> deleteMOsWithCriteriaHavingChildFailList = CMRestToolDataProvider.getDataFromCSV("CommandData.csv",
            "deleteMOsWithCriteriaHavingChildFail");
    public static List<List<String>> deleteMOsWithCriteriaHavingChildALLParamFailList = CMRestToolDataProvider.getDataFromCSV("CommandData.csv",
            "deleteMOsWithCriteriaHavingChildALLParamFail");

    @DataProvider(name = "deleteMOsWithCriteria")
    public static Object[][] deleteMOsWhichSatisfyCriteria() {
        final Object[][] result = populateTestDataList(deleteMOsWithCriteriaList);
        return result;
    }

    @DataProvider(name = "deleteMOsWithCriteriaHavingChildFail")
    public static Object[][] deleteMOsWithCriteriaHavingChildFail() {
        final Object[][] result = populateTestDataList(deleteMOsWithCriteriaHavingChildFailList);
        return result;
    }

    @DataProvider(name = "deleteMOsWithCriteriaHavingChildALLParamFail")
    public static Object[][] deleteMOsWithCriteriaHavingChildALLParamFail() {
        final Object[][] result = populateTestDataList(deleteMOsWithCriteriaHavingChildALLParamFailList);
        return result;
    }

    /*
     * End of KAOS TORF-1350, TORF-1351, TORF-1352, TORF-1353
     */

    /*
     * Start of KAOS TORF-1356
     */
    public static List<List<String>> setMultipleMOsWithFilteredOnRangeOfAttributeValuesList = CMRestToolDataProvider.getDataFromCSV(
            "CommandData.csv", "setMultipleMOsWithFilteredOnRangeOfAttributeValues");

    public static List<List<String>> setMultipleMOsWithFilteredOnRangeOfAttributeValuesWithUnsupportedComparatorFailList = CMRestToolDataProvider
            .getDataFromCSV("CommandData.csv", "setMultipleMOsWithFilteredOnRangeOfAttributeValuesWithUnsupportedComparatorFail");

    @DataProvider(name = "setMultipleMOsWithFilteredOnRangeOfAttributeValues")
    public static Object[][] setMultipleMOsWithFilteredOnRangeOfAttributeValues() {
        final Object[][] result = populateTestDataList(setMultipleMOsWithFilteredOnRangeOfAttributeValuesList);
        return result;
    }

    @DataProvider(name = "setMultipleMOsWithFilteredOnRangeOfAttributeValuesWithUnsupportedComparatorFail")
    public static Object[][] setMultipleMOsWithFilteredOnRangeOfAttributeValuesWithUnsupportedComparatorFail() {
        final Object[][] result = populateTestDataList(setMultipleMOsWithFilteredOnRangeOfAttributeValuesWithUnsupportedComparatorFailList);
        return result;
    }

    /*
     * End of KAOS TORF-1356
     */

    @DataProvider(name = "getMeContext")
    public static Object[][] getMeContext() {
        final Object[][] result = populateTestDataList(getMeContextList);
        return result;
    }

    @DataProvider(name = "getMeContextAndManagedElement")
    public static Object[][] getMeContextAndManagedElement() {
        final Object[][] result = populateTestDataList(getMeContextAndManagedElementList);
        return result;
    }

    @DataProvider(name = "createMeContext")
    public static Object[][] createMeContext() {
        final Object[][] result = populateTestDataList(createMeContextList);
        return result;
    }

    @DataProvider(name = "createMeContextAndManagedElement")
    public static Object[][] createMeContextAndManagedElement() {
        final Object[][] result = populateTestDataList(createMeContextAndManagedElementList);
        return result;
    }

    @DataProvider(name = "createMeContextAndManagedElementWithInvalidParent")
    public static Object[][] createMeContextAndManagedElementWithInvalidParent() {
        final Object[][] result = populateTestDataList(createMeContextAndManagedElementWithInvalidParentList);
        return result;
    }

    @DataProvider(name = "getMeContextWhichDoesNotExist")
    public static Object[][] getMeContextWhichDoesNotExist() {
        final Object[][] result = populateTestDataList(getMeContextWhichDoesNotExistList);
        return result;
    }

    @DataProvider(name = "invalidParentChildSituation")
    public static Object[][] invalidParentChildSituation() {
        final Object[][] result = populateTestDataList(invalidParentChildSituationList);
        return result;
    }

    @DataProvider(name = "createMeContextWith150CharacterLongName")
    public static Object[][] createMeContextWith150CharacterLongName() {
        final Object[][] result = populateTestDataList(createMeContextWith150CharacterLongNameList);
        return result;
    }

    @DataProvider(name = "create_MeContext_And_Managed_Element_And_ENodeBFunction")
    public static Object[][] create_MeContext_And_Managed_Element_And_ENodeBFunction() {
        final Object[][] result = populateTestDataList(create_MeContext_And_Managed_Element_And_ENodeBFunctionList);
        return result;
    }

    /*
     * Following Test Cases added for KAOS User Stories to create a SectorEquipmentFunction and EUtranCellFDD
     */

    @DataProvider(name = "create_MeContext_And_Managed_Element_And_ENodeBFunction_And_SectorEquipmentFunction")
    public static Object[][] create_MeContext_And_Managed_Element_And_ENodeBFunction_And_SectorEquipmentFunction() {
        final Object[][] result = populateTestDataList(create_MeContext_And_Managed_Element_And_ENodeBFunction_And_SectorEquipmentFunctionList);
        return result;
    }

    @DataProvider(name = "create_MeContext_And_Managed_Element_And_ENodeBFunction_And_SectorEquipmentFunction_MeContext_123abc456")
    public static Object[][] create_MeContext_And_Managed_Element_And_ENodeBFunction_And_SectorEquipmentFunction_MeContext_123abc456() {
        final Object[][] result = populateTestDataList(create_MeContext_And_Managed_Element_And_ENodeBFunction_And_SectorEquipmentFunction_MeContext_123abc456List);
        return result;
    }

    @DataProvider(name = "createFirstEUtranCellFDD")
    public static Object[][] createEUtranCellFDD() {
        final Object[][] result = populateTestDataList(createFirstEUtranCellFDDList);
        return result;
    }

    @DataProvider(name = "createSecondEUtranCellFDD")
    public static Object[][] createSecondEUtranCellFDD() {
        final Object[][] result = populateTestDataList(createSecondEUtranCellFDDList);
        return result;
    }

    @DataProvider(name = "createThirdEUtranCellFDD")
    public static Object[][] createThirdEUtranCellFDD() {
        final Object[][] result = populateTestDataList(createThirdEUtranCellFDDList);
        return result;
    }

    /*
     * Following Test Cases added for Team America by ETHOMIT
     * 
     * 
     * Following Test Cases added for Team America User Story TORF-8573
     */

    @DataProvider(name = "searchWithAttributeFilterOnGreaterThanReturnsCorrectFDNs")
    public static Object[][] searchWithAttributeFilterOnGreaterThanReturnsCorrectFDNs() {
        final Object[][] result = populateTestDataList(searchWithAttributeFilterOnGreaterThanList);
        return result;
    }

    /*
     * End of Test Cases added for KAOS User Stories to create a SectorEquipmentFunction and EUtranCellFDD
     */

    @DataProvider(name = "get_MeContext_And_ManagedElement_And_ENodeBFunction_Expecting_False_As_Result")
    public static Object[][] get_MeContext_And_ManagedElement_And_ENodeBFunction_Expecting_False_As_Result() {
        final Object[][] result = populateTestDataList(get_MeContext_And_ManagedElement_And_ENodeBFunction_Expecting_False_As_Result_List);
        return result;
    }

    @DataProvider(name = "createENodeBFunctionChildMO")
    public static Object[][] createEnodeBFunctionChildMO() {
        final Object[][] result = populateTestDataList(createEnodeBFunctionChildMOList);
        return result;
    }

    @DataProvider(name = "createENodeBFunctionWithComplexAttributes")
    public static Object[][] createENodeBFunctionWithComplexAttributes() {
        final Object[][] result = populateTestDataList(createENodeBFunctionWithComplexAttributesList);
        return result;
    }

    @DataProvider(name = "getStarENodeBFunction")
    public static Object[][] getStarENodeBFunction() {
        final Object[][] result = populateTestDataList(getStarENodeBFunctionList);
        return result;
    }

    @DataProvider(name = "getSpecificENodeBFunction")
    public static Object[][] getSpecificENodeBFunction() {
        final Object[][] result = populateTestDataList(getSpecificENodeBFunctionList);
        return result;
    }

    @DataProvider(name = "getENodeBFunctionNameFollowedByStar")
    public static Object[][] getENodeBFunctionNameFollowedByStar() {
        final Object[][] result = populateTestDataList(getENodeBFunctionNameFollowedByStarList);
        return result;
    }

    @DataProvider(name = "getENodeBFunctionNamePreceededByStar")
    public static Object[][] getENodeBFunctionNamePreceededByStar() {
        final Object[][] result = populateTestDataList(getENodeBFunctionNamePreceededByStarList);
        return result;
    }

    @DataProvider(name = "getENodeBFunctionDotStar")
    public static Object[][] getENodeBFunctionDotStar() {
        final Object[][] result = populateTestDataList(getENodeBFunctionDotStarList);
        return result;
    }

    @DataProvider(name = "getENodeBFunctionDotAttributes")
    public static Object[][] getENodeBFunctionDotAttributes() {
        final Object[][] result = populateTestDataList(getENodeBFunctionDotAttributesList);
        return result;
    }

    @DataProvider(name = "createASecondMeContextInstance")
    public static Object[][] createASecondMeContextInstance() {
        final Object[][] result = populateTestDataList(createASecondMeContextInstanceList);
        return result;
    }

    @DataProvider(name = "createASecondManangedElementInstance")
    public static Object[][] createASecondManangedElementInstance() {
        final Object[][] result = populateTestDataList(createASecondManangedElementInstanceList);
        return result;
    }

    @DataProvider(name = "getENodeBFunctionDotAttributeEqualsValueVerifyingDnsLookupOnTai")
    public static Object[][] getENodeBFunctionDotAttributeEqualsValueVerifyingDnsLookupOnTai() {
        final Object[][] result = populateTestDataList(getENodeBFunctionDotAttributeEqualsValueVerifyingDnsLookupOnTaiList);
        return result;
    }

    @DataProvider(name = "getENodeBFunctionDotAttributeEqualsValueVerifyingDnsLookupTimer")
    public static Object[][] getENodeBFunctionDotAttributeEqualsValueVerifyingDnsLookupTimer() {
        final Object[][] result = populateTestDataList(getENodeBFunctionDotAttributeEqualsValueVerifyingDnsLookupTimerList);
        return result;
    }

    @DataProvider(name = "getENodeBFunctionDotAttributeEqualsValueVerifyingUserLabel")
    public static Object[][] getENodeBFunctionDotAttributeEqualsValueVerifyingUserLabel() {
        final Object[][] result = populateTestDataList(getENodeBFunctionDotAttributeEqualsValueVerifyingUserLabelList);
        return result;
    }

    @DataProvider(name = "getENodeBFunctionDotAttributeEqualsValueVerifyingUserLabelAndDnsLookupTimerCombined")
    public static Object[][] getENodeBFunctionDotAttributeEqualsValueVerifyingUserLabelAndDnsLookupTimerCombined() {
        final Object[][] result = populateTestDataList(getENodeBFunctionDotAttributeEqualsValueVerifyingUserLabelAndDnsLookupTimerCombinedList);
        return result;
    }

    @DataProvider(name = "getStarTermPointToMmeDotStar")
    public static Object[][] getStarTermPointToMmeDotStar() {
        final Object[][] result = populateTestDataList(getStarTermPointToMmeDotStarList);
        return result;
    }

    @DataProvider(name = "getStarExternalCdma2000CellDotStar")
    public static Object[][] getStarExternalCdma2000CellDotStar() {
        final Object[][] result = populateTestDataList(getStarExternalCdma2000CellDotStarList);
        return result;
    }

    @DataProvider(name = "getQciProfilePredefinedDotStar")
    public static Object[][] getQciProfilePredefinedDotStar() {
        final Object[][] result = populateTestDataList(getQciProfilePredefinedDotStarList);
        return result;
    }

    @DataProvider(name = "getIpSyncRefDotStar")
    public static Object[][] getIpSyncRefDotStar() {
        final Object[][] result = populateTestDataList(getIpSyncRefDotStarList);
        return result;
    }

    @DataProvider(name = "getStarMeContextDotStar")
    public static Object[][] getStarMeContextDotStar() {
        final Object[][] result = populateTestDataList(getStarMeContextDotStarList);
        return result;
    }

    @DataProvider(name = "getStarManagedElementDotStar")
    public static Object[][] getStarManagedElementDotStar() {
        final Object[][] result = populateTestDataList(getStarManagedElementDotStarList);
        return result;
    }

    @DataProvider(name = "getStarENodeBFunctionDotStar")
    public static Object[][] getStarENodeBFunctionDotStar() {
        final Object[][] result = populateTestDataList(getStarENodeBFunctionDotStarList);
        return result;
    }

    @DataProvider(name = "getASpecificMeContextDotStar")
    public static Object[][] getASpecificMeContextDotStar() {
        final Object[][] result = populateTestDataList(getASpecificMeContextDotStarList);
        return result;
    }

    @DataProvider(name = "getLTE01ERBS1StarENodeBFunctionDotStar")
    public static Object[][] getLTE01ERBS1StarENodeBFunctionDotStar() {
        final Object[][] result = populateTestDataList(getLTE01ERBS1StarENodeBFunctionDotStarList);
        return result;
    }

    @DataProvider(name = "getLTE01ERBS3StarTermPointToMmeDotStar")
    public static Object[][] getLTE01ERBS3StarTermPointToMmeDotStar() {
        final Object[][] result = populateTestDataList(getLTE01ERBS3StarTermPointToMmeDotStarList);
        return result;
    }

    @DataProvider(name = "getLTE01ERBS42StarExternalCdma2000CellDotStar")
    public static Object[][] getLTE01ERBS42StarExternalCdma2000CellDotStar() {
        final Object[][] result = populateTestDataList(getLTE01ERBS42StarExternalCdma2000CellDotStarList);
        return result;
    }

    @DataProvider(name = "getLTE01ERBS429StarENodeBFunctionDotStar")
    public static Object[][] getLTE01ERBS429StarENodeBFunctionDotStar() {
        final Object[][] result = populateTestDataList(getLTE01ERBS429StarENodeBFunctionDotStarList);
        return result;
    }

    @DataProvider(name = "getLTE01ERBS42StarENodeBFunctionDotStar")
    public static Object[][] getLTE01ERBS42StarENodeBFunctionDotStar() {
        final Object[][] result = populateTestDataList(getLTE01ERBS42StarENodeBFunctionDotStarList);
        return result;
    }

    @DataProvider(name = "createThirdMeContextInstance")
    public static Object[][] createThirdMeContextInstance() {
        final Object[][] result = populateTestDataList(createThirdMeContextInstanceList);
        return result;
    }

    @DataProvider(name = "createThirdManagedElementInstance")
    public static Object[][] createThirdManagedElementInstance() {
        final Object[][] result = populateTestDataList(createThirdManagedElementInstanceList);
        return result;
    }

    @DataProvider(name = "createSequenceAttributes")
    public static Object[][] createSequenceAttributes() {
        final Object[][] result = populateTestDataList(createSequenceAttributesList);
        return result;
    }

    @DataProvider(name = "createFourthMeContextInstance")
    public static Object[][] createFourthMeContextInstance() {
        final Object[][] result = populateTestDataList(createFourthMeContextInstanceList);
        return result;
    }

    @DataProvider(name = "createFourthManagedElementInstance")
    public static Object[][] createFourthManagedElementInstance() {
        final Object[][] result = populateTestDataList(createFourthManagedElementInstanceList);
        return result;
    }

    @DataProvider(name = "createFourthENodeBFunctionInstance")
    public static Object[][] createFourthENodeBFunctionInstance() {
        final Object[][] result = populateTestDataList(createFourthENodeBFunctionInstanceList);
        return result;
    }

    @DataProvider(name = "createAttributesOfTypeMORef")
    public static Object[][] createAttributesOfTypeMORef() {
        final Object[][] result = populateTestDataList(createAttributesOfTypeMORefList);
        return result;
    }

    @DataProvider(name = "getStarMeContextDotMeContextId")
    public static Object[][] getStarMeContextDotMeContextId() {
        final Object[][] result = populateTestDataList(getStarMeContextDotMeContextIdList);
        return result;
    }

    @DataProvider(name = "getStarENodeBFunctionNoNameSpace")
    public static Object[][] getStarENodeBFunctionNoNameSpace() {
        final Object[][] result = populateTestDataList(getStarENodeBFunctionNoNameSpaceList);
        return result;
    }

    @DataProvider(name = "getENodeBFunctionNameWithNamepsaceAndVersion")
    public static Object[][] getENodeBFunctionNameWithNamepsaceAndVersion() {
        final Object[][] result = populateTestDataList(getENodeBFunctionNameWithNamepsaceAndVersionList);
        return result;
    }

    /*
     * Following Test Cases added for Team KAOS by EEIRSH
     */

    /*
     * Following Test Cases added for KAOS User Story TORF-1365
     */

    @DataProvider(name = "setMeContextGenerationCounter")
    public static Object[][] setMeContext_generationCounter() {
        final Object[][] result = populateTestDataList(setMeContextGenerationCounterList);
        return result;
    }

    @DataProvider(name = "setDuplicateMeContextAttributeGenerationCounterToSameValue")
    public static Object[][] setMeContext_sameAttributeValue() {
        final Object[][] result = populateTestDataList(setDuplicateMeContextAttributeGenerationCounterToSameValueList);
        return result;
    }

    @DataProvider(name = "setMeContextAttributeUserLabel")
    public static Object[][] setMeContextAttributeUserLabel() {
        final Object[][] result = populateTestDataList(setMeContextAttributeUserLabelList);
        return result;
    }

    @DataProvider(name = "setMeContextAttributeUserLabelToNullValue")
    public static Object[][] setMeContextAttributeUserLabelToNullValue() {
        final Object[][] result = populateTestDataList(setMeContextAttributeUserLabelToNullValueList);
        return result;
    }

    @DataProvider(name = "setAttributednsLookupTimerOnENodeBFunction")
    public static Object[][] setAttributednsLookupTimerOnENodeBFunction() {
        final Object[][] result = populateTestDataList(setAttributednsLookupTimerOnENodeBFunctionList);
        return result;
    }

    /*
     * Following Test Cases added for KAOS User Story TORF-1354
     */
    @DataProvider(name = "setMeContextListOfAttributes")
    public static Object[][] setMeContextListOfAttributes() {
        final Object[][] result = populateTestDataList(setMeContextAttributesList);
        return result;
    }

    @DataProvider(name = "setMeContextDuplicateListOfReorderedAttributesToDifferentValues")
    public static Object[][] setMeContextDuplicateListOfReorderedAttributesToDifferentValues() {
        final Object[][] result = populateTestDataList(setMeContextDuplicateListOfReorderedAttributesToDifferentValuesList);
        return result;
    }

    @DataProvider(name = "setListOfAttributesOnENodeBFunction")
    public static Object[][] setListOfAttributesOnENodeBFunction() {
        final Object[][] result = populateTestDataList(setListOfAttributesOnENodeBFunctionList);
        return result;
    }

    /*
     * Following Test Cases added for KAOS User Story TORF-5305
     */

    @DataProvider(name = "seteNodeBPlmnIdComplexAttribute")
    public static Object[][] seteNodeBPlmnIdComplexAttribute() {
        final Object[][] result = populateTestDataList(seteNodeBPlmnIdComplexAttributeList);
        return result;
    }

    @DataProvider(name = "seteNodeBPlmnIdComplexAttributeToDifferentValues")
    public static Object[][] seteNodeBPlmnIdComplexAttributeToDifferentValues() {
        final Object[][] result = populateTestDataList(seteNodeBPlmnIdComplexAttributesToDifferentValuesList);
        return result;
    }

    /*
     * Following Test Cases added for KAOS User Story TORF-6582
     */

    @DataProvider(name = "setEnumAttributednsLookupOnTaiONENodeBFunction")
    public static Object[][] setEnumAttributednsLookupOnTaiONENodeBFunction() {
        final Object[][] result = populateTestDataList(setEnumAttributednsLookupOnTaiENodeBFunctionList);
        return result;
    }

    @DataProvider(name = "setenumAttributednsLookupOnTaiOFFENodeBFunction")
    public static Object[][] setEnumAttributednsLookupOnTaiOFFENodeBFunction() {
        final Object[][] result = populateTestDataList(setEnumAttributednsLookupOnTaiOFFENodeBFunctionList);
        return result;
    }

    @DataProvider(name = "setEnumAttributednsLookupOnTaiONAgainENodeBFunction")
    public static Object[][] setenumAttributednsLookupOnTaiONAgainENodeBFunction() {
        final Object[][] result = populateTestDataList(setEnumAttributednsLookupOnTaiONAgainENodeBFunctionList);
        return result;
    }

    @DataProvider(name = "setEnumAttributennsfModeToSPLMNENodeBFunction")
    public static Object[][] setenumAttributennsfModeToSPLMNENodeBFunction() {
        final Object[][] result = populateTestDataList(setEnumAttributennsfModeToSPLMNENodeBFunctionList);
        return result;
    }

    /* End of Test Cases added for KAOS User Story TORF-6582 */

    /*
     * Following Test Cases added for KAOS User Story TORF-690 and TORF-10209
     */

    @DataProvider(name = "deleteMOWithChildMONoParmALLExpectingFail")
    public static Object[][] deleteMOWithChildMONoParmALLExpectingFail() {
        final Object[][] result = populateTestDataList(deleteMOWithChildMONoParmALLExpectingFailList);
        return result;
    }

    @DataProvider(name = "deleteMOWithChildMOWithParmALL")
    public static Object[][] deleteMOWithChildMOWithParmALL() {
        final Object[][] result = populateTestDataList(deleteMOWithChildMoWithParmALLList);
        return result;
    }

    @DataProvider(name = "getMOAfterDeleteMOWithParmALLExpectingFail")
    public static Object[][] getMOAfterDeleteMOWithParmALLExpectingFail() {
        final Object[][] result = populateTestDataList(getMOAfterDeleteMOWithParmALLExpectingFailList);
        return result;
    }

    @DataProvider(name = "deleteMO")
    public static Object[][] deleteMO() {
        final Object[][] result = populateTestDataList(deleteMOList);
        return result;
    }

    @DataProvider(name = "getMOAfterDeleteMOExpectingFail")
    public static Object[][] getMOAfterDeleteMOExpectingFail() {
        final Object[][] result = populateTestDataList(getMOAfterDeleteMOExpectingFailList);
        return result;
    }

    @DataProvider(name = "deleteNonExistentMOExpectingFail")
    public static Object[][] deleteNonExistentMOExpectingFail() {
        final Object[][] result = populateTestDataList(deleteNonExistentMOExpectingFailList);
        return result;
    }

    @DataProvider(name = "deleteMOWithOneChildMOWithParmALL")
    public static Object[][] deleteMOWithOneChildMOWithParmALL() {
        final Object[][] result = populateTestDataList(deleteMOWithOneChildMoWithParmALLList);
        return result;
    }

    @DataProvider(name = "deleteMOWithNoChildMOWithParmALL")
    public static Object[][] deleteMOWithNoChildMOWithParmALL() {
        final Object[][] result = populateTestDataList(deleteMOWithNoChildMoWithParmALLList);
        return result;
    }

    @DataProvider(name = "delete123abc456ALL")
    public static Object[][] delete123abc456ALL() {
        final Object[][] result = populateTestDataList(delete123abc456ALLList);
        return result;
    }

    @DataProvider(name = "delete123456ALL")
    public static Object[][] delete123456ALL() {
        final Object[][] result = populateTestDataList(delete123456ALLList);
        return result;
    }

    @DataProvider(name = "deleteMeContext3ALL")
    public static Object[][] deleteMeContext3ALL() {
        final Object[][] result = populateTestDataList(deleteMeContext3ALLList);
        return result;
    }

    @DataProvider(name = "deleteMeContext4ALL")
    public static Object[][] deleteMeContext4ALL() {
        final Object[][] result = populateTestDataList(deleteMeContext4ALLList);
        return result;
    }

    /* End of Test Cases added for KAOS User Story TORF-690 and TORF-10209 */

    /* Start of Test Cases added for KAOS User Story TORF-6924 */

    @DataProvider(name = "setAttributeOnAllNodesOfENodeBFunctionType")
    public static Object[][] setAttributeOnAllNodesOfENodeBFunctionType() {
        final Object[][] result = populateTestDataList(setAttributeOnAllNodesOfENodeBFunctionTypeList);
        return result;
    }

    @DataProvider(name = "setAttributeOnAllNodesOfSpecifiedENodeBFunctionType")
    public static Object[][] setAttributeOnAllNodesOfSpecifiedENodeBFunctionType() {
        final Object[][] result = populateTestDataList(setAttributeOnAllNodesOfSpecifiedENodeBFunctionTypeList);
        return result;
    }

    @DataProvider(name = "setAttributeOnNodesOfENodeBFunctionTypeStartingWithString")
    public static Object[][] setAttributeOnNodesOfENodeBFunctionTypeStartingWithString() {
        final Object[][] result = populateTestDataList(setAttributeOnNodesOfENodeBFunctionTypeStartingWithStringList);
        return result;
    }

    @DataProvider(name = "setAttributeOnNodesOfENodeBFunctionTypeEndingWithString")
    public static Object[][] setAttributeOnNodesOfENodeBFunctionTypeEndingWithString() {
        final Object[][] result = populateTestDataList(setAttributeOnNodesOfENodeBFunctionTypeEndingWithStringList);
        return result;
    }

    /* End of Test Cases added for KAOS User Story TORF-6924 */

    /* Start of Test Cases added for KAOS User Story TORF-7037 */
    @DataProvider(name = "setSequencedComplexDataForSpecifiedNode")
    public static Object[][] setSequencedComplexDataForSpecifiedNode() {
        final Object[][] result = populateTestDataList(setSequencedComplexDataForSpecifiedNodeList);
        return result;
    }

    /* End of Test Cases added for KAOS User Story TORF-7037 */

    /* Start of Test Cases added for KAOS User Story TORF-1355 */
    @DataProvider(name = "setSingleValueFALSEForNodeIDFilteredOnSingleAttributeValueCriteria")
    public static Object[][] setSingleValueFALSEForNodeIDFilteredOnSingleAttributeValueCriteria() {
        final Object[][] result = populateTestDataList(setSingleValueFALSEForNodeIDFilteredOnSingleAttributeValueCriteriaList);
        return result;
    }

    @DataProvider(name = "setSingleValueTRUEForNodeIDFilteredOnSingleAttributeValueCriteria")
    public static Object[][] setSingleValueTRUEForNodeIDFilteredOnSingleAttributeValueCriteria() {
        final Object[][] result = populateTestDataList(setSingleValueTRUEForNodeIDFilteredOnSingleAttributeValueCriteriaList);
        return result;
    }

    @DataProvider(name = "setMultipleValuesForNodeIDFilteredOnSingleAttributeValueCriteria")
    public static Object[][] setMultipleValuesForNodeIDFilteredOnSingleAttributeValueCriteria() {
        final Object[][] result = populateTestDataList(setMultipleValuesForNodeIDFilteredOnSingleAttributeValueCriteriaList);
        return result;
    }

    @DataProvider(name = "setSingleIntegerValueForNodeIDFilteredOnMultipleAttributeValueCriteria")
    public static Object[][] setSingleIntegerValueForNodeIDFilteredOnMultipleAttributeValueCriteria() {
        final Object[][] result = populateTestDataList(setSingleIntegerValueForNodeIDFilteredOnMultipleAttributeValueCriteriaList);
        return result;
    }

    @DataProvider(name = "setSingleEnumValueForNodeIDFilteredOnMultipleAttributeValueCriteria")
    public static Object[][] setSingleEnumValueForNodeIDFilteredOnMultipleAttributeValueCriteria() {
        final Object[][] result = populateTestDataList(setSingleEnumValueForNodeIDFilteredOnMultipleAttributeValueCriteriaList);
        return result;
    }

    /* End of Test Cases added for KAOS User Story TORF-1355 */

    /*
     * Following Test Cases added for KAOS User Story TORF-1358
     */
    @DataProvider(name = "setAttributetHODataFwdReorderingOnAllMOInstancesOfENodeBFunctionWithuserLabelOfNode")
    public static Object[][] setAttributetHODataFwdReorderingOnAllMOInstancesOfENodeBFunctionWithuserLabelOfNode() {
        final Object[][] result = populateTestDataList(setAttributetHODataFwdReorderingOnAllMOInstancesOfENodeBFunctionWithuserLabelOfNodeList);
        return result;
    }

    /* End of Test Cases added for KAOS User Story TORF-1358 */

    /*
     * Following Test Cases added for KAOS User Story TORF-1358/6924/1355/6964
     */

    @DataProvider(name = "setAttributeuserLabelOnAllEUtranCellcontainAttributeFilter")
    public static Object[][] setAttributeuserLabelOnAllEUtranCellcontainAttributeFilter() {
        final Object[][] result = populateTestDataList(setAttributeuserLabelOnAllEUtranCellcontainAttributeFilterList);
        return result;
    }

    @DataProvider(name = "setAttributePartialNodeFilteruserLabelOnEUtranCellFDDcontainsSpecifieduserLabel")
    public static Object[][] setAttributePartialNodeFilteruserLabelOnEUtranCellFDDcontainsSpecifieduserLabel() {
        final Object[][] result = populateTestDataList(setAttributePartialNodeFilteruserLabelOnEUtranCellFDDcontainsSpecifieduserLabelList);
        return result;
    }

    @DataProvider(name = "setAttributeulInterferenceManagementActivePartialNodeFilterContainsEUtranCellFDDcontainsSpecifieduserLabelAndulInterferenceManagementActiveFalse")
    public static Object[][] setAttributeulInterferenceManagementActivePartialNodeFilterContainsEUtranCellFDDcontainsSpecifieduserLabelAndulInterferenceManagementActiveFalse() {
        final Object[][] result = populateTestDataList(setAttributeulInterferenceManagementActivePartialNodeFilterContainsEUtranCellFDDcontainsSpecifieduserLabelAndulInterferenceManagementActiveFalseList);
        return result;
    }

    @DataProvider(name = "setAttributeulChannelBandwidthNodeStarContainsEUtranCellFDDcontainsuserLabelAndulInterferenceManagementActiveFalse")
    public static Object[][] setAttributeulChannelBandwidthNodeStarContainsEUtranCellFDDcontainsuserLabelAndulInterferenceManagementActiveFalse() {
        final Object[][] result = populateTestDataList(setAttributeulChannelBandwidthNodeStarContainsEUtranCellFDDcontainsuserLabelAndulInterferenceManagementActiveFalseList);
        return result;
    }

    /* End of Test Cases added for KAOS User Story TORF-1358/6924/1355/6964 */

    @DataProvider(name = "actionMeContextDeleteAllRepertoires")
    public static Object[][] actionMeContextDeleteAllRepertoires() {
        final Object[][] result = populateTestDataList(actionMeContextDeleteAllRepertoiresList);
        return result;
    }

    @DataProvider(name = "actionMeContextDeleteAllSlots")
    public static Object[][] actionMeContextDeleteAllSlots() {
        final Object[][] result = populateTestDataList(actionMeContextDeleteAllSlotsList);
        return result;
    }

    @DataProvider(name = "actionMeContextSyncs")
    public static Object[][] actionMeContextSyncs() {
        final Object[][] result = populateTestDataList(actionMeContextSyncsList);
        return result;
    }

    @DataProvider(name = "actionMeContextDeleteAllSlotsAllEnodebFuncEqEqEnodebFuncId")
    public static Object[][] actionMeContextDeleteAllSlotsAllEnodebFuncEqEqEnodebFuncId() {
        final Object[][] result = populateTestDataList(actionMeContextDeleteAllSlotsAllEnodebFuncEqEqEnodebFuncIdList);
        return result;
    }

    /*
     * @DataProvider(name = "getAllMeContextMeContextIdEqEq") public static Object[][] getAllMeContextMeContextIdEqEq() { Object[][] result =
     * populateTestDataList(getAllMeContextMeContextIdEqEqList); return result; }
     */

    /*
     * @DataProvider(name = "create_EnodeBFunction_With_ChildMO_From_ManagedElement_Expecting_Fail_As_Result" ) public static Object[][]
     * create_EnodeBFunction_With_ChildMO_From_ManagedElement_Expecting_Fail_As_Result () { Object[][] result = populateTestDataList(
     * create_EnodeBFunction_With_ChildMO_From_ManagedElement_Expecting_Fail_As_Result_List ); return result; }
     */

    @DataProvider(name = "checkCommandCanBeEnteredViaUIandRecievesResponse")
    public static Object[][] checkCommandCanBeEnteredViaUIandRecievesResponse() {
        final Object[][] result = getUIServerIpForTests();
        return result;
    }

    public static Object[][] getUIServerIpForTests() {
        //public static Object[][] populateTestDataList(List<List<String>> listName) {

        final Host uiHost = CMRestToolDataProvider.getUIHost();

        final Object[][] result = new Object[1][];
        final Object[] testMethodArguments = new Object[1];
        testMethodArguments[0] = uiHost;
        result[0] = testMethodArguments;
        return result;
    }

    public static Object[][] populateTestDataList(final List<List<String>> listName) {

        final Host cmHosts = CMRestToolDataProvider.getCmHost();

        final Object[][] result = new Object[listName.size()][];

        int idx = 0;
        for (final List<String> item : listName) {
            final Object[] testMethodArguments = new Object[3];
            testMethodArguments[0] = cmHosts;
            testMethodArguments[1] = item.get(CMRestToolDataProvider.COMMAND);
            testMethodArguments[2] = item.get(CMRestToolDataProvider.EXPECTED_RESULT);
            result[idx] = testMethodArguments;
            idx++;
        }
        return result;
    }

    public static ListDto convertToList(final AbstractDto abstractDto) {
        return (ListDto) abstractDto;
    }

    public static CommandResponseDto getcommandResponseDto(final String response) {
        final ObjectMapper mapper = new ObjectMapper();
        CommandResponseDto commandResponseDto = null;
        try {
            commandResponseDto = mapper.readValue(response, CommandResponseDto.class);
        } catch (final JsonParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (final JsonMappingException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (final IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return commandResponseDto;
    }

    public static HashMap<String, String> getActualResultAndPlaceInMap(final List<String> postResult) {
        final HashMap<String, String> actualResults = new HashMap<String, String>();
        final ObjectMapper mapper = new ObjectMapper();
        CommandResponseDto commandResponseDto = null;

        try {
            commandResponseDto = mapper.readValue(postResult.get(0), CommandResponseDto.class);
        } catch (final JsonMappingException e) {
            e.printStackTrace();
        } catch (final IOException e) {
            e.printStackTrace();
        }

        final ListDto listDto = convertToList(commandResponseDto.getCommandResultDto());

        final ListDto lDTO = convertToList(listDto.getDtos().get(0));

        for (int i = 0; i < lDTO.getDtos().size(); i++) {

            final NameValueDto nameValueDto = (NameValueDto) lDTO.getDtos().get(i);
            actualResults.put(nameValueDto.getName(), nameValueDto.getValue());
        }

        return actualResults;
    }

    public static HashMap<String, HashMap<String, String>> getActualResultAndPlaceInMapForMultipleMoResponse(final List<String> postResult) {
        final ObjectMapper mapper = new ObjectMapper();
        CommandResponseDto commandResponseDto = null;

        try {
            commandResponseDto = mapper.readValue(postResult.get(0), CommandResponseDto.class);

        } catch (final JsonMappingException e) {

            e.printStackTrace();
        } catch (final IOException e) {
            e.printStackTrace();
        }

        final ListDto listDto = convertToList(commandResponseDto.getCommandResultDto());

        final HashMap<String, HashMap<String, String>> dtoNames = new HashMap<String, HashMap<String, String>>();

        for (final AbstractDto dtoList : listDto.getDtos()) {
            final HashMap<String, String> attributes = new HashMap<String, String>();
            final ListDto singleMO = convertToList(dtoList);
            for (final Object dto : singleMO.getDtos()) {
                final NameValueDto nameValueDto = (NameValueDto) dto;
                attributes.put(nameValueDto.getName(), nameValueDto.getValue());
            }

            dtoNames.put(singleMO.getDtoName(), attributes);
        }

        return dtoNames;
    }

    public static final String FDN = "FDN";
    public static final String TYPE = "TYPE";
    public static final String NAME = "NAME";
    public static final String NAMESPACE = "NAMESPACE";
    public static final String NAMESPACEVERSION = "NAMESPACEVERSION";
    public static final String MANAGEDELEMENTID = "ManagedElementId";
    public static final String ENODEBFUNCTIONID = "ENodeBFunctionId";
    public static final String ENODEBPLMNID = "eNodeBPlmnId";

    public static final String MECONTEXT_NAME_PRIMARY = "123456";
    public static final String MECONTEXT_NAME_SECONDARY = "123abc456";

    public static final String MECONTEXT_TYPE = "MeContext";
    public static final String MECONTEXT_NAMESPACE = "OSS_TOP";
    public static final String MECONTEXT_NAMESPACEVERSION = "1.1.0";

    public static final String MANAGEDELEMENT_NAME_PRIMARY = "epatmah3";
    public static final String MANAGEDELEMENTTYPE = "ManagedElement";
    public static final String MANAGEDELEMENT_NAMESPACE = "CPP_NODE_MODEL";
    public static final String MANAGEDELEMENT_NAMESPACEVERSION = "3.12.0";
    public static final String MANAGEDELEMENTIDNAME = "epatmah";

    public static final String ENODEBFUNCTION_NAME_PRIMARY = "78910";
    public static final String ENODEBFUNCTION_NAME_SECONDARY = "2";

    // public static final String ENODEBFUNCTION_NAME_SECONDARY = "789102";
    public static final String ENODEBFUNCTIONTYPE = "ENodeBFunction";
    public static final String ENODEBFUNCTION_NAMESPACE = "ERBS_NODE_MODEL";
    public static final String ENODEBFUNCTION_NAMESPACEVERSION = "3.1.72";
    public static final String ENODEBFUNCTIONIDNAME = "epatmah";

    /*
     * Following Test Cases added for KAOS User Stories to create a SectorEquipmentFunction and EUtranCellFDD
     */
    public static final String SECTOR_EQUIPMENT_FUNCTION_1 = "1";

    public static final String EUTRANCELLFDD001 = "EUC001";
    public static final String EUTRANCELLFDD002 = "EUC002";
    public static final String EUTRANCELLFDD003 = "EUC003";

    /*
     * End of Test Cases added for KAOS User Stories to create a SectorEquipmentFunction and EUtranCellFDD
     */

    public static final String GERANETWORK_PRIMARY_NAME = "1";
    public static final String ENODEBPLMNIDNAMES = "{mcc=1, mnc=2, mncLength=2}";

    public static HashMap<String, String> getExpectedResultsMapForMeContextCommand() {
        final HashMap<String, String> expectedResults = new HashMap<String, String>();
        expectedResults.put(FDN, "MeContext=" + MECONTEXT_NAME_PRIMARY);
        expectedResults.put(TYPE, MECONTEXT_TYPE);
        expectedResults.put(NAME, MECONTEXT_NAME_PRIMARY);
        expectedResults.put(NAMESPACE, MECONTEXT_NAMESPACE);
        expectedResults.put(NAMESPACEVERSION, MECONTEXT_NAMESPACEVERSION);
        return expectedResults;
    }

    public static HashMap<String, String> getExpectedResultsMapForGetStarMeContextDotMeContextId() {
        final HashMap<String, String> expectedResults = new HashMap<String, String>();
        expectedResults.put(FDN, "MeContext=" + MECONTEXT_NAME_PRIMARY);
        expectedResults.put("MeContextId", "123456");
        return expectedResults;
    }

    public static HashMap<String, String> getExpectedResultsMapForSecondMeContextCommand() {
        final HashMap<String, String> expectedResults = new HashMap<String, String>();
        expectedResults.put(FDN, "MeContext=123abc456");
        return expectedResults;
    }

    public static HashMap<String, String> getExpectedResultsMapForThirdMeContextCommand() {
        final HashMap<String, String> expectedResults = new HashMap<String, String>();
        expectedResults.put(FDN, "MeContext=MeContext3");
        return expectedResults;
    }

    public static HashMap<String, String> getExpectedResultsMapForFourthMeContextCommand() {
        final HashMap<String, String> expectedResults = new HashMap<String, String>();
        expectedResults.put(FDN, "MeContext=MeContext4");
        return expectedResults;
    }

    public static HashMap<String, String> getExpectedResultsMapForSecondManagedElementCommand() {
        final HashMap<String, String> expectedResults = new HashMap<String, String>();
        expectedResults.put(FDN, "MeContext=123abc456,ManagedElement=epatmah3");
        return expectedResults;
    }

    public static HashMap<String, String> getExpectedResultsMapForThirdManagedElementCommand() {
        final HashMap<String, String> expectedResults = new HashMap<String, String>();
        expectedResults.put(FDN, "MeContext=MeContext3,ManagedElement=ManagedElement3");
        return expectedResults;
    }

    public static HashMap<String, String> getExpectedResultsMapForFourthManagedElementCommand() {
        final HashMap<String, String> expectedResults = new HashMap<String, String>();
        expectedResults.put(FDN, "MeContext=MeContext4,ManagedElement=ManagedElement4");
        return expectedResults;
    }

    public static HashMap<String, String> getExpectedResultsMapForFourthENodeBFunctionCommand() {
        final HashMap<String, String> expectedResults = new HashMap<String, String>();
        expectedResults.put(FDN, "MeContext=MeContext4,ManagedElement=ManagedElement4,ENodeBFunction=ENodeBFunction4");
        return expectedResults;
    }

    public static HashMap<String, String> getExpectedResultsMapForManagedElementCommand() {
        final HashMap<String, String> expectedResults = new HashMap<String, String>();
        expectedResults.put(FDN, "MeContext=" + MECONTEXT_NAME_PRIMARY + ",ManagedElement=" + MANAGEDELEMENT_NAME_PRIMARY);
        expectedResults.put(TYPE, MANAGEDELEMENTTYPE);
        expectedResults.put(NAME, MANAGEDELEMENT_NAME_PRIMARY);
        expectedResults.put(NAMESPACE, MANAGEDELEMENT_NAMESPACE);
        expectedResults.put(NAMESPACEVERSION, MANAGEDELEMENT_NAMESPACEVERSION);
        expectedResults.put(MANAGEDELEMENTID, MANAGEDELEMENTIDNAME);
        return expectedResults;
    }

    public static HashMap<String, String> getExpectedResultsMapForENodeBFunctionCommand() {
        final HashMap<String, String> expectedResults = new HashMap<String, String>();
        expectedResults.put(FDN, "MeContext=" + MECONTEXT_NAME_PRIMARY + ",ManagedElement=" + MANAGEDELEMENT_NAME_PRIMARY + ",ENodeBFunction="
                + ENODEBFUNCTION_NAME_PRIMARY);
        expectedResults.put(TYPE, ENODEBFUNCTIONTYPE);
        expectedResults.put(NAME, ENODEBFUNCTION_NAME_PRIMARY);
        expectedResults.put(NAMESPACE, ENODEBFUNCTION_NAMESPACE);
        expectedResults.put(NAMESPACEVERSION, ENODEBFUNCTION_NAMESPACEVERSION);
        expectedResults.put(ENODEBFUNCTIONID, ENODEBFUNCTIONIDNAME);
        return expectedResults;
    }

    /*
     * Following Test Cases added for KAOS User Stories to create a SectorEquipmentFunction and EUtranCellFDD
     */

    public static HashMap<String, String> getExpectedResultsMapForSectorEquipmentFunction() {
        final HashMap<String, String> expectedResults = new HashMap<String, String>();
        expectedResults.put(FDN, "MeContext=" + MECONTEXT_NAME_PRIMARY + ",ManagedElement=" + MANAGEDELEMENT_NAME_PRIMARY
                + ",SectorEquipmentFunction=" + SECTOR_EQUIPMENT_FUNCTION_1);
        expectedResults.put(SECTOR_EQUIPMENT_FUNCTION_1, SECTOR_EQUIPMENT_FUNCTION_1);
        return expectedResults;
    }

    public static HashMap<String, String> getExpectedResultsMapForSectorEquipmentFunction_2() {
        final HashMap<String, String> expectedResults = new HashMap<String, String>();
        expectedResults.put(FDN, "MeContext=" + MECONTEXT_NAME_SECONDARY + ",ManagedElement=" + MANAGEDELEMENT_NAME_PRIMARY
                + ",SectorEquipmentFunction=" + SECTOR_EQUIPMENT_FUNCTION_1);
        expectedResults.put(SECTOR_EQUIPMENT_FUNCTION_1, SECTOR_EQUIPMENT_FUNCTION_1);
        return expectedResults;
    }

    public static HashMap<String, String> getExpectedResultsMapForcreateFirstEUtranCellFDD() {
        final HashMap<String, String> expectedResults = new HashMap<String, String>();
        expectedResults.put(FDN, "MeContext=" + MECONTEXT_NAME_PRIMARY + ",ManagedElement=" + MANAGEDELEMENT_NAME_PRIMARY + ",ENodeBFunction="
                + ENODEBFUNCTION_NAME_PRIMARY + ",EUtranCellFDD=" + EUTRANCELLFDD001);
        return expectedResults;
    }

    public static HashMap<String, String> getExpectedResultsMapForcreateSecondEUtranCellFDD() {
        final HashMap<String, String> expectedResults = new HashMap<String, String>();
        expectedResults.put(FDN, "MeContext=" + MECONTEXT_NAME_PRIMARY + ",ManagedElement=" + MANAGEDELEMENT_NAME_PRIMARY + ",ENodeBFunction="
                + ENODEBFUNCTION_NAME_PRIMARY + ",EUtranCellFDD=" + EUTRANCELLFDD002);
        return expectedResults;
    }

    public static HashMap<String, String> getExpectedResultsMapForcreateThirdEUtranCellFDD() {
        final HashMap<String, String> expectedResults = new HashMap<String, String>();
        expectedResults.put(FDN, "MeContext=" + MECONTEXT_NAME_SECONDARY + ",ManagedElement=" + MANAGEDELEMENT_NAME_PRIMARY + ",ENodeBFunction="
                + ENODEBFUNCTION_NAME_SECONDARY + ",EUtranCellFDD=" + EUTRANCELLFDD003);
        return expectedResults;
    }

    public static String[] getExpectedResultsMapForSearchWithAttributeFilterOnGreaterThanReturnsCorrectFDNs() {
        final String[] expectedDtoNames = { "MeContext=123abc456,ManagedElement=epatmah3,ENodeBFunction=2,EUtranCellFDD=EUC003",
                "MeContext=123456,ManagedElement=epatmah3,ENodeBFunction=78910,EUtranCellFDD=EUC002",
                "MeContext=123456,ManagedElement=epatmah3,ENodeBFunction=78910,EUtranCellFDD=EUC001",
                "MeContext=123456,ManagedElement=epatmah3,ENodeBFunction=78910,EUtranCellFDD=1",
                "MeContext=123abc456,ManagedElement=epatmah3,ENodeBFunction=2,EUtranCellFDD=1" };
        return expectedDtoNames;
    }

    /*
     * End of Test Cases added for KAOS User Stories to create a SectorEquipmentFunction and EUtranCellFDD
     */

    public static HashMap<String, String> getExpectedResultsMapForENodeBFunctionChildMoCommand() {
        final HashMap<String, String> expectedResults = new HashMap<String, String>();
        expectedResults.put(FDN, "MeContext=" + MECONTEXT_NAME_PRIMARY + ",ManagedElement=" + MANAGEDELEMENT_NAME_PRIMARY + ",ENodeBFunction="
                + ENODEBFUNCTION_NAME_PRIMARY + ",GeraNetwork=" + GERANETWORK_PRIMARY_NAME);
        return expectedResults;
    }

    public static HashMap<String, String> getCreateENodeBFunctionWithComplexAttributes() {
        final HashMap<String, String> expectedResults = new HashMap<String, String>();
        expectedResults.put(FDN, "MeContext=123abc456,ManagedElement=epatmah3,ENodeBFunction=2");
        expectedResults.put(ENODEBPLMNID, ENODEBPLMNIDNAMES);
        return expectedResults;
    }

    public static String[] getExpectedResultForGetStarENodeBFunction() {
        final String[] expectedDtoNames = { "MeContext=123456,ManagedElement=epatmah3,ENodeBFunction=78910",
                "MeContext=123abc456,ManagedElement=epatmah3,ENodeBFunction=2" };
        return expectedDtoNames;
    }

    public static String[] getExpectedResultForGetSpecificENodeBFunction() {
        final String[] expectedDtoNames = { "MeContext=123456,ManagedElement=epatmah3,ENodeBFunction=78910" };
        return expectedDtoNames;
    }

    public static String[] getExpectedResultForGetENodeBFunctionNameFollowedByStar() {
        final String[] expectedDtoNames = { "MeContext=123456,ManagedElement=epatmah3,ENodeBFunction=78910",
                "MeContext=123abc456,ManagedElement=epatmah3,ENodeBFunction=2" };
        return expectedDtoNames;
    }

    public static String[] getExpectedResultForGetENodeBFunctionNameWithNamepsaceAndVersion() {
        final String[] expectedDtoNames = { "MeContext=123456,ManagedElement=epatmah3,ENodeBFunction=78910",
                "MeContext=123abc456,ManagedElement=epatmah3,ENodeBFunction=2" };
        return expectedDtoNames;
    }

    public static String[] getExpectedResultForGetENodeBFunctionNamePreceededByStar() {
        final String[] expectedDtoNames = { "MeContext=123456,ManagedElement=epatmah3,ENodeBFunction=78910",
                "MeContext=123abc456,ManagedElement=epatmah3,ENodeBFunction=2" };
        return expectedDtoNames;
    }

    public static String[] getExpectedResultForGetENodeBFunctionDotStar() {
        final String[] expectedDtoNames = { "MeContext=123456,ManagedElement=epatmah3,ENodeBFunction=78910" };
        return expectedDtoNames;
    }

    public static String[] getExpectedResultForGetMultipleMos() {
        final String[] expectedDtoNames = { "MeContext=123456,ManagedElement=epatmah3,ENodeBFunction=78910",
                "MeContext=123abc456,ManagedElement=epatmah3,ENodeBFunction=2" };
        return expectedDtoNames;
    }

    public static String[] getExpectedResultForGetMultipleChildMOIs() {
        final String[] expectedDtoNames = { "MeContext=123456,ManagedElement=epatmah3,ENodeBFunction=78910,EUtranCellFDD=1",
                "MeContext=123abc456,ManagedElement=epatmah3,ENodeBFunction=2,EUtranCellFDD=1" };
        return expectedDtoNames;
    }

    public static String[] getExpectedResultForENodeBFunctionDotAttributes() {
        final String[] expectedDtoNames = { "MeContext=123456,ManagedElement=epatmah3,ENodeBFunction=78910" };
        return expectedDtoNames;
    }

    public static String[] getExpectedResultCreateSequenceAttributes() {
        final String[] expectedDtoNames = { "MeContext=MeContext3,ManagedElement=ManagedElement3,ENodeBFunction=ENodeBFunction3" };
        return expectedDtoNames;
    }

    public static String[] getExpectedResultCreateAttributesOfTypeMoRef() {
        final String[] expectedDtoNames = { "MeContext=MeContext4,ManagedElement=ManagedElement4,ENodeBFunction=ENodeBFunction4" };
        return expectedDtoNames;
    }

    public static HashMap<String, HashMap<String, String>> getExpectedEnodeBFunctionIdsForGetENodeBFunctionDotStar() {
        final HashMap<String, HashMap<String, String>> expectedDtos = new HashMap<String, HashMap<String, String>>();
        final HashMap<String, String> expectedAttributes = new HashMap<String, String>();
        expectedAttributes.put("ENodeBFunctionId", "78910");
        expectedDtos.put("MeContext=123456,ManagedElement=epatmah3,ENodeBFunction=78910", expectedAttributes);
        return expectedDtos;
    }

    public static HashMap<String, HashMap<String, String>> getExpectedEnodeBFunctionIdsAndDscpLabelsForGetENodeBFunctionDotAttributes() {
        final HashMap<String, HashMap<String, String>> expectedDtos = new HashMap<String, HashMap<String, String>>();
        final HashMap<String, String> expectedAttributes = new HashMap<String, String>();
        expectedAttributes.put("ENodeBFunctionId", "78910");
        expectedAttributes.put("dscpLabel", "24");
        expectedDtos.put("MeContext=123456,ManagedElement=epatmah3,ENodeBFunction=78910", expectedAttributes);
        return expectedDtos;
    }

    public static HashMap<String, HashMap<String, String>> getExpectedDnsLookupOnTaiForGetENodeBFunctionDotAttributeEqualsValue() {
        final HashMap<String, HashMap<String, String>> expectedDtos = new HashMap<String, HashMap<String, String>>();
        final HashMap<String, String> expectedAttributes1 = new HashMap<String, String>();
        final HashMap<String, String> expectedAttributes2 = new HashMap<String, String>();
        expectedAttributes1.put("dnsLookupOnTai", "ON");
        expectedAttributes1.put("dnsLookupTimer", "0");
        expectedAttributes1.put("dscpLabel", "24");
        expectedAttributes2.put("dnsLookupOnTai", "ON");
        expectedAttributes2.put("dnsLookupTimer", "0");
        expectedAttributes2.put("dscpLabel", "24");
        expectedAttributes1.put("userLabel", "Ireland");
        expectedAttributes2.put("userLabel", "");
        expectedDtos.put("MeContext=123456,ManagedElement=epatmah3,ENodeBFunction=78910", expectedAttributes1);
        expectedDtos.put("MeContext=123abc456,ManagedElement=epatmah3,ENodeBFunction=2", expectedAttributes2);
        return expectedDtos;
    }

    public static HashMap<String, HashMap<String, String>> getExpectedResultsForCreateSequenceAttributesValue() {
        final HashMap<String, HashMap<String, String>> expectedDtos = new HashMap<String, HashMap<String, String>>();
        final HashMap<String, String> expectedAttributes = new HashMap<String, String>();
        expectedAttributes.put("x2BlackList", "[{mcc=1, mnc=123, mncLength=2, enbId=1}, {mcc=2, mnc=258, mncLength=3, enbId=2}]");
        expectedDtos.put("MeContext=MeContext3,ManagedElement=ManagedElement3,ENodeBFunction=ENodeBFunction3", expectedAttributes);
        return expectedDtos;
    }

    public static HashMap<String, HashMap<String, String>> getExpectedResultsForCreateAttributesOfTypeMoRef() {
        final HashMap<String, HashMap<String, String>> expectedDtos = new HashMap<String, HashMap<String, String>>();
        final HashMap<String, String> expectedAttributes = new HashMap<String, String>();
        expectedAttributes.put("x2BlackList",
                "(enbId=0,mcc=100,mnc=60,mncLength=2), (enbId=100,mcc=999,mnc=999,mncLength=3), (enbId=100000,mcc=500,mnc=10,mncLength=2)");
        expectedDtos.put("MeContext=MeContext3,ManagedElement=ManagedElement3,ENodeBFunction=ENodeBFunction3", expectedAttributes);
        return expectedDtos;
    }

    /*
     * Following Test Cases added for Team KAOS by EEIRSH
     */

    /*
     * Following Test Cases added for KAOS User Story TORF-1365
     */
    public static HashMap<String, String> getExpectedResultsMapForsetMeContextCommandGenerationCounter() {
        final HashMap<String, String> expectedAttributes = new HashMap<String, String>();
        expectedAttributes.put("generationCounter", "20");
        return expectedAttributes;
    }

    public static HashMap<String, String> getExpectedResultsMapForSetDuplicateMeContextAttributeGenerationCounterToSameValue() {
        final HashMap<String, String> expectedAttributes = new HashMap<String, String>();
        expectedAttributes.put("generationCounter", "20");
        return expectedAttributes;
    }

    public static HashMap<String, String> getExpectedResultsMapForSetMeContextAttributeUserLabel() {
        final HashMap<String, String> expectedAttributes = new HashMap<String, String>();
        expectedAttributes.put("userLabel", "test5");
        return expectedAttributes;
    }

    public static HashMap<String, String> getExpectedResultsMapForSetMeContextCommandAttributeUserLabelToNullValue() {
        final HashMap<String, String> expectedAttributes = new HashMap<String, String>();
        expectedAttributes.put("userLabel", "");
        return expectedAttributes;
    }

    public static HashMap<String, String> getExpectedResultsMapForSetCommandAttributednsLookupTimerOnENodeBFunction() {
        final HashMap<String, String> expectedAttributes = new HashMap<String, String>();
        expectedAttributes.put("dnsLookupTimer", "999");
        return expectedAttributes;
    }

    /*
     * Following Test Cases added for KAOS User Story TORF-1354
     */

    public static HashMap<String, String> getExpectedResultsMapForSetMeContextCommandListOfAttributes() {
        final HashMap<String, String> expectedAttributes = new HashMap<String, String>();
        expectedAttributes.put("userLabel", "test6");
        expectedAttributes.put("generationCounter", "26");
        expectedAttributes.put("mirrorSynchronizationStatus", "SYNCHRONIZED");
        return expectedAttributes;
    }

    public static HashMap<String, String> getExpectedResultsMapForSetMeContextCommandDuplicateListOfReorderedAttributesToDifferentValues() {
        final HashMap<String, String> expectedAttributes = new HashMap<String, String>();
        expectedAttributes.put("generationCounter", "27");
        expectedAttributes.put("mirrorSynchronizationStatus", "SYNCING");
        expectedAttributes.put("userLabel", "test7");
        return expectedAttributes;
    }

    public static HashMap<String, String> getExpectedResultsMapForSetCommandListOfAttributesOnENodeBFunction() {
        final HashMap<String, String> expectedAttributes = new HashMap<String, String>();
        expectedAttributes.put("dnsLookupTimer", "500");
        expectedAttributes.put("s1RetryTimer", "250");
        expectedAttributes.put("userLabel", "TORF-1354");
        return expectedAttributes;
    }

    /*
     * Following Test Cases added for KAOS User Story TORF-5305
     */

    public static HashMap<String, String> getExpectedResultsMapForSetCommandeNodeBPlmnIdComplexAttribute() {
        final HashMap<String, String> expectedAttributes = new HashMap<String, String>();
        expectedAttributes.put("eNodeBPlmnId", "{mcc=2, mnc=2, mncLength=3}");
        return expectedAttributes;
    }

    public static HashMap<String, String> getExpectedResultsMapForSetCommandeNodeBPlmnIdComplexAttributesToDifferentValues() {
        final HashMap<String, String> expectedAttributes = new HashMap<String, String>();
        expectedAttributes.put("eNodeBPlmnId", "{mcc=1, mnc=1, mncLength=2}");
        return expectedAttributes;
    }

    /*
     * Following Test Cases added for KAOS User Story TORF-6582
     */

    public static HashMap<String, String> getExpectedResultsMapForSetCommandEnumAttributednsLookupOnTaiONENodeBFunction() {
        final HashMap<String, String> expectedAttributes = new HashMap<String, String>();
        expectedAttributes.put("dnsLookupOnTai", "ON");
        return expectedAttributes;
    }

    public static HashMap<String, String> getExpectedResultsMapForSetCommandEnumAttributednsLookupOnTaiOFFENodeBFunction() {
        final HashMap<String, String> expectedAttributes = new HashMap<String, String>();
        expectedAttributes.put("dnsLookupOnTai", "OFF");
        return expectedAttributes;
    }

    public static HashMap<String, String> getExpectedResultsMapForSetCommandEnumAttributednsLookupOnTaiONAgainENodeBFunction() {
        final HashMap<String, String> expectedAttributes = new HashMap<String, String>();
        expectedAttributes.put("dnsLookupOnTai", "ON");
        return expectedAttributes;
    }

    public static HashMap<String, String> getExpectedResultsMapForSetCommandEnumAttributennsfModeToSPLMNENodeBFunction() {
        final HashMap<String, String> expectedAttributes = new HashMap<String, String>();
        expectedAttributes.put("nnsfMode", "SPLMN");
        return expectedAttributes;
    }

    public static String[] getExpectedResultAllMeContextMeContextIdEqEqAttributes() {
        final String[] expectedResultAllMeContexts = { "MeContext=123456" };
        return expectedResultAllMeContexts;
    }

    public static HashMap<String, HashMap<String, String>> getExpectedResultAllMeContextMeContextIdEqEq() {
        final HashMap<String, HashMap<String, String>> equalsAttribute = new HashMap<String, HashMap<String, String>>();
        final HashMap<String, String> expectedAttributes = new HashMap<String, String>();
        expectedAttributes.put("MeContextId", "123456");
        equalsAttribute.put("MeContext=123456", expectedAttributes);
        return equalsAttribute;
    }

    /*
     * Following Test Cases added for KAOS User Story TORF-6924
     */
    public static final String[] ENodeBNodeList = { "MeContext=123456,ManagedElement=epatmah3,ENodeBFunction=78910",
            "MeContext=123abc456,ManagedElement=epatmah3,ENodeBFunction=2", "MeContext=MeContext3,ManagedElement=ManagedElement3,ENodeBFunction=3",
            "MeContext=MeContext4,ManagedElement=ManagedElement4,ENodeBFunction=ENodeBFunction4",
            "MeContext=MeContext3,ManagedElement=ManagedElement3,ENodeBFunction=2" };

    public static HashMap<String, HashMap<String, String>> getExpectedResultsMapForSetAttributeOnAllNodesOfENodeBFunctionType() {
        // Define Data Types
        final HashMap<String, HashMap<String, String>> expectedDtos = new HashMap<String, HashMap<String, String>>();
        final HashMap<String, String> expectedAttributes1 = new HashMap<String, String>();
        final HashMap<String, String> expectedAttributes2 = new HashMap<String, String>();
        final HashMap<String, String> expectedAttributes3 = new HashMap<String, String>();
        final HashMap<String, String> expectedAttributes4 = new HashMap<String, String>();
        final HashMap<String, String> expectedAttributes5 = new HashMap<String, String>();
        final String UserLabel = "TORF6924_TAF01";

        // Populate attribute hash maps
        expectedAttributes1.put("FDN", ENodeBNodeList[0]);
        expectedAttributes1.put("userLabel", UserLabel);

        expectedAttributes2.put("FDN", ENodeBNodeList[1]);
        expectedAttributes2.put("userLabel", UserLabel);

        expectedAttributes3.put("FDN", ENodeBNodeList[2]);
        expectedAttributes3.put("userLabel", UserLabel);

        expectedAttributes4.put("FDN", ENodeBNodeList[3]);
        expectedAttributes4.put("userLabel", UserLabel);

        expectedAttributes5.put("FDN", ENodeBNodeList[4]);
        expectedAttributes5.put("userLabel", UserLabel);

        // Populate node hash maps
        expectedDtos.put(ENodeBNodeList[0], expectedAttributes1);
        expectedDtos.put(ENodeBNodeList[1], expectedAttributes2);
        expectedDtos.put(ENodeBNodeList[2], expectedAttributes3);
        expectedDtos.put(ENodeBNodeList[3], expectedAttributes4);
        expectedDtos.put(ENodeBNodeList[4], expectedAttributes5);

        return expectedDtos;
    }

    public static String[] getExpectedResultForSetAttributeOnAllNodesOfENodeBFunctionType() {
        final String[] expectedDtoNames = { ENodeBNodeList[0], ENodeBNodeList[1], ENodeBNodeList[2], ENodeBNodeList[3] };
        return expectedDtoNames;
    }

    public static HashMap<String, HashMap<String, String>> getExpectedResultsMapForSetAttributeOnAllNodesOfSpecifiedENodeBFunctionType() {
        // Define Data Types
        final HashMap<String, HashMap<String, String>> expectedDtos = new HashMap<String, HashMap<String, String>>();
        final HashMap<String, String> expectedAttributes1 = new HashMap<String, String>();

        // Populate attribute hash maps
        expectedAttributes1.put("FDN", ENodeBNodeList[0]);
        expectedAttributes1.put("userLabel", "TORF6924_TAF02");

        // Populate node hash maps
        expectedDtos.put(ENodeBNodeList[0], expectedAttributes1);

        return expectedDtos;
    }

    public static HashMap<String, HashMap<String, String>> getExpectedResultsMapForSetAttributeOnNodesOfENodeBFunctionTypeStartingWithString() {
        // Define Data Types
        final HashMap<String, HashMap<String, String>> expectedDtos = new HashMap<String, HashMap<String, String>>();
        final HashMap<String, String> expectedAttributes1 = new HashMap<String, String>();
        final HashMap<String, String> expectedAttributes2 = new HashMap<String, String>();
        final String UserLabel = "TORF6924_TAF03";

        // Populate attribute hash maps
        expectedAttributes1.put("FDN", ENodeBNodeList[2]);
        expectedAttributes1.put("userLabel", UserLabel);

        expectedAttributes2.put("FDN", ENodeBNodeList[3]);
        expectedAttributes2.put("userLabel", UserLabel);

        // Populate node hash maps
        expectedDtos.put(ENodeBNodeList[2], expectedAttributes1);
        expectedDtos.put(ENodeBNodeList[3], expectedAttributes2);

        return expectedDtos;
    }

    public static HashMap<String, HashMap<String, String>> getExpectedResultsMapForSetAttributeOnNodesOfENodeBFunctionTypeEndingWithString() {
        // Define Data Types
        final HashMap<String, HashMap<String, String>> expectedDtos = new HashMap<String, HashMap<String, String>>();
        final HashMap<String, String> expectedAttributes = new HashMap<String, String>();

        // Populate attribute hash maps
        expectedAttributes.put("FDN", ENodeBNodeList[3]);
        expectedAttributes.put("userLabel", "TORF6924_TAF04");

        // Populate node hash maps
        expectedDtos.put(ENodeBNodeList[3], expectedAttributes);

        return expectedDtos;
    }

    /*
     * Following Test Cases added for KAOS User Story TORF-1355
     */
    public static HashMap<String, String> getExpectedResultsMapForSetSingleValueFALSEForNodeIDFilteredOnSingleAttributeValueCriteria() {
        final HashMap<String, String> expectedAttributes = new HashMap<String, String>();
        expectedAttributes.put("x2IpAddrViaS1Active", "false");
        return expectedAttributes;
    }

    public static HashMap<String, String> getExpectedResultsMapForSetSingleValueTRUEForNodeIDFilteredOnSingleAttributeValueCriteria() {
        final HashMap<String, String> expectedAttributes = new HashMap<String, String>();
        expectedAttributes.put("x2IpAddrViaS1Active", "true");
        return expectedAttributes;
    }

    public static HashMap<String, String> getExpectedResultsMapForSetMultipleValuesForNodeIDFilteredOnSingleAttributeValueCriteria() {
        final HashMap<String, String> expectedAttributes = new HashMap<String, String>();
        expectedAttributes.put("x2IpAddrViaS1Active", "false");
        expectedAttributes.put("tHODataFwdReordering", "1000");
        return expectedAttributes;
    }

    public static HashMap<String, String> getExpectedResultsMapForSetSingleIntegerValueForNodeIDFilteredOnMultipleAttributeValueCriteria() {
        final HashMap<String, String> expectedAttributes = new HashMap<String, String>();
        expectedAttributes.put("tHODataFwdReordering", "2000");
        return expectedAttributes;
    }

    public static HashMap<String, String> getExpectedResultsMapForSetSingleEnumValueForNodeIDFilteredOnMultipleAttributeValueCriteria() {
        final HashMap<String, String> expectedAttributes = new HashMap<String, String>();
        expectedAttributes.put("x2IpAddrViaS1Active", "true");
        return expectedAttributes;
    }

    /*
     * Following Test Cases added for KAOS User Story TORF-1358
     */
    public static HashMap<String, HashMap<String, String>> getExpectedResultsMapForSetCommandSetAttributetHODataFwdReorderingOnAllMOInstancesOfENodeBFunctionWithuserLabelOfNode() {
        final HashMap<String, HashMap<String, String>> expectedResults = new HashMap<String, HashMap<String, String>>();
        final HashMap<String, String> expectedAttributes = new HashMap<String, String>();

        // Created Expected Attributes hashmap
        expectedAttributes.put("FDN", "MeContext=123456,ManagedElement=epatmah3,ENodeBFunction=78910");
        expectedAttributes.put("tHODataFwdReordering", "85");

        // Created Expected DTOs hashmap
        expectedResults.put("MeContext=123456,ManagedElement=epatmah3,ENodeBFunction=78910", expectedAttributes);

        return expectedResults;
    }

    /*
     * Following Test Cases added for Team America by EFIAHAN
     * 
     * 
     * Following Test Cases added for Team America User Story TORF-1348
     */

    @DataProvider(name = "getMultipleNodeIds")
    public static Object[][] getMultipleNodeIds() {
        final Object[][] result = populateTestDataList(getMultipleNodeIdsList);
        return result;
    }

    /*
     * Following Test Cases added for Team America by EFIAHAN
     * 
     * 
     * Following Test Cases added for Team America User Story TORF-6889
     */

    @DataProvider(name = "getOneOrMoreChildMOIs")
    public static Object[][] getOneOrMoreChildMOIs() {
        final Object[][] result = populateTestDataList(getOneOrMoreChildMOIsList);
        return result;
    }

    /*
     * Following Test Cases added for KAOS User Stories TORF-1358/6924/1355/6964
     */

    public static final String[] EUtranCellFDDList = { "MeContext=123456,ManagedElement=epatmah3,ENodeBFunction=78910,EUtranCellFDD=EUC001",
            "MeContext=123456,ManagedElement=epatmah3,ENodeBFunction=78910,EUtranCellFDD=EUC002",
            "MeContext=123abc456,ManagedElement=epatmah3,ENodeBFunction=2,EUtranCellFDD=EUC003" };

    public static final HashMap<String, HashMap<String, String>> getExpectedResultsMapForsetAttributeuserLabelOnAllEUtranCellcontainAttributeFilter() {
        // Define Data Types
        final HashMap<String, HashMap<String, String>> expectedDtos = new HashMap<String, HashMap<String, String>>();
        final HashMap<String, String> expectedAttributes1 = new HashMap<String, String>();
        final HashMap<String, String> expectedAttributes2 = new HashMap<String, String>();
        final HashMap<String, String> expectedAttributes3 = new HashMap<String, String>();
        final String UserLabel = "TORF_1358";

        // Populate attribute hash maps
        expectedAttributes1.put("FDN", EUtranCellFDDList[0]);
        expectedAttributes1.put("userLabel", UserLabel);

        expectedAttributes2.put("FDN", EUtranCellFDDList[1]);
        expectedAttributes2.put("userLabel", UserLabel);

        expectedAttributes3.put("FDN", EUtranCellFDDList[2]);
        expectedAttributes3.put("userLabel", UserLabel);

        // Populate node hash maps
        expectedDtos.put(EUtranCellFDDList[0], expectedAttributes1);
        expectedDtos.put(EUtranCellFDDList[1], expectedAttributes2);
        expectedDtos.put(EUtranCellFDDList[2], expectedAttributes3);

        return expectedDtos;
    }

    public static final HashMap<String, HashMap<String, String>> getExpectedResultsMapForsetAttributePartialNodeFilteruserLabelOnEUtranCellFDDcontainsSpecifieduserLabel() {
        // Define Data Types
        final HashMap<String, HashMap<String, String>> expectedDtos = new HashMap<String, HashMap<String, String>>();
        final HashMap<String, String> expectedAttributes3 = new HashMap<String, String>();

        final String UserLabel = "TORF_6964";

        // Populate attribute hash maps

        expectedAttributes3.put("FDN", EUtranCellFDDList[2]);
        expectedAttributes3.put("userLabel", UserLabel);

        // Populate node hash maps
        expectedDtos.put(EUtranCellFDDList[2], expectedAttributes3);

        return expectedDtos;
    }

    public static final HashMap<String, HashMap<String, String>> getExpectedResultsMapForsetAttributeulInterferenceManagementActivePartialNodeFilterContainsEUtranCellFDDcontainsSpecifieduserLabelAndulInterferenceManagementActiveFalse() {
        // Define Data Types
        final HashMap<String, HashMap<String, String>> expectedDtos = new HashMap<String, HashMap<String, String>>();
        final HashMap<String, String> expectedAttributes1 = new HashMap<String, String>();
        final HashMap<String, String> expectedAttributes2 = new HashMap<String, String>();

        final String ulInterferenceManagementActive = "true";

        // Populate attribute hash maps
        expectedAttributes1.put("FDN", EUtranCellFDDList[0]);
        expectedAttributes1.put("ulInterferenceManagementActive", ulInterferenceManagementActive);

        expectedAttributes2.put("FDN", EUtranCellFDDList[1]);
        expectedAttributes2.put("ulInterferenceManagementActive", ulInterferenceManagementActive);

        // Populate node hash maps
        expectedDtos.put(EUtranCellFDDList[0], expectedAttributes1);
        expectedDtos.put(EUtranCellFDDList[1], expectedAttributes2);

        return expectedDtos;
    }

    public static final HashMap<String, HashMap<String, String>> getExpectedResultsMapForsetAttributeulChannelBandwidthNodeStarContainsEUtranCellFDDcontainsuserLabelAndulInterferenceManagementActiveFalse() {
        // Define Data Types
        final HashMap<String, HashMap<String, String>> expectedDtos = new HashMap<String, HashMap<String, String>>();
        final HashMap<String, String> expectedAttributes3 = new HashMap<String, String>();

        final String ulChannelBandwidth = "20000";

        // Populate attribute hash maps

        expectedAttributes3.put("FDN", EUtranCellFDDList[2]);
        expectedAttributes3.put("ulChannelBandwidth", ulChannelBandwidth);

        // Populate node hash maps
        expectedDtos.put(EUtranCellFDDList[2], expectedAttributes3);

        return expectedDtos;
    }

    /* End of Test Cases added for KAOS User Stories TORF-1358/6924/1355/6964 */

    /*
     * KAOS User Story TORF-7037 - Build expected data output
     */
    public static HashMap<String, String> getExpectedResultsMapForSetSequencedComplexDataForSpecifiedNode() {
        final HashMap<String, String> expectedAttributes = new HashMap<String, String>();

        // Created Expected Attributes hash map
        expectedAttributes.put("FDN", "MeContext=123456,ManagedElement=epatmah3,ENodeBFunction=78910");
        expectedAttributes
                .put("x2BlackList",
                        "[{mcc=100, mnc=60, mncLength=2, enbId=0}, {mcc=999, mnc=999, mncLength=3, enbId=100}, {mcc=500, mnc=10, mncLength=2, enbId=100000}]");

        return expectedAttributes;
    }

    /* End of KAOS User Story TORF-7037 - Build expected data output */
}
