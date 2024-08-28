package com.ericsson.oss.services.cm.writer.test;

import static com.ericsson.oss.services.cm.writer.test.DpsTestMo.CPP_NAMESPACE;
import static com.ericsson.oss.services.cm.writer.test.DpsTestMo.CPP_NAMESPACE_VERSION;
import static com.ericsson.oss.services.cm.writer.test.DpsTestMo.TOP_NAMESPACE;
import static com.ericsson.oss.services.cm.writer.test.DpsTestMo.TOP_NAMESPACE_VERSION;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.ejb.EJB;
import javax.ejb.Stateless;

import org.jboss.arquillian.junit.Arquillian;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.ericsson.oss.services.cm.cmshared.dto.ActionSpecification;
import com.ericsson.oss.services.cm.cmshared.dto.AttributeSpecification;
import com.ericsson.oss.services.cm.cmshared.dto.CmObject;
import com.ericsson.oss.services.cm.cmshared.dto.CmObjectSpecification;
import com.ericsson.oss.services.cm.cmshared.dto.CmResponse;
import com.ericsson.oss.services.cm.cmshared.dto.StringifiedAttributeSpecifications;
import com.ericsson.oss.services.cm.cmshared.dto.search.CmMatchCondition;
import com.ericsson.oss.services.cm.cmshared.dto.search.CmSearchCriteria;
import com.ericsson.oss.services.cm.cmshared.dto.search.CmSearchScope;
import com.ericsson.oss.services.cm.cmshared.dto.search.CmSearchScope.ScopeType;
import com.ericsson.oss.services.cm.cmtools.dto.builders.CmObjectSpecificationBuilder;
import com.ericsson.oss.services.cm.cmtools.dto.builders.CmSearchScopeBuilder;
import com.ericsson.oss.services.cm.cmtools.error.ErrorHandler;

@RunWith(Arquillian.class)
@Stateless
public class CmWriterIT extends CmWriterTestBase {

    @EJB
    ArquillianProxy arquillianProxyBean;

    protected static final String GENERATION_COUNTER_ATTRIBUTE = "generationCounter";

    protected static final String MANAGED_ELEMENT_RDN = "ManagedElement=" + DEFAULT_MO_ID;
    protected static final String ENODEB_FUNCTION_RDN = "ENodeBFunction=" + DEFAULT_MO_ID;
    protected static final String SECTOR_EQUIPMENT_FUNCTION_RDN = "SectorEquipmentFunction=" + DEFAULT_MO_ID;
    protected static final String EUTRANCELLFDD_RDN1 = "EUtranCellFDD=" + EUCFDD1_ID;

    protected static final String MECONTEXT_FDN1 = "MeContext=" + NODE_NAME1;
    protected static final String MANAGED_ELEMENT_FDN1 = MECONTEXT_FDN1 + "," + MANAGED_ELEMENT_RDN;
    protected static final String ENODEB_FUNCTION_FDN1 = MECONTEXT_FDN1 + "," + MANAGED_ELEMENT_RDN + "," + ENODEB_FUNCTION_RDN;
    protected static final String SECTOR_EQUIPMENT_FUNCTION_FDN1 = MANAGED_ELEMENT_FDN1 + "," + SECTOR_EQUIPMENT_FUNCTION_RDN;
    protected static final String EUTRANCELLFDD_FDN1 = ENODEB_FUNCTION_FDN1 + "," + EUTRANCELLFDD_RDN1;

    protected static final String MECONTEXT_FDN2 = "MeContext=" + NODE_NAME2;
    protected static final String MANAGED_ELEMENT_FDN2 = MECONTEXT_FDN2 + "," + MANAGED_ELEMENT_RDN;
    protected static final String ENODEB_FUNCTION_FDN2 = MECONTEXT_FDN2 + "," + MANAGED_ELEMENT_RDN + "," + ENODEB_FUNCTION_RDN;
    protected static final String SECTOR_EQUIPMENT_FUNCTION_FDN2 = MECONTEXT_FDN2 + "," + SECTOR_EQUIPMENT_FUNCTION_RDN;

    protected static final String ENODEB_FUNCTION_ID_ATTRIBUTE = ENodeBFunction.MANDATORY_ATTRIBUTE_ENODEBFUNCTIONID + "==" + DEFAULT_MO_ID;
    protected static final String ENODEB_FUNCTION_USERLABEL_ATTRIBUTE = ENodeBFunction.MANDATORY_ATTRIBUTE_ENODEBFUNCTIONID + "==" + DEFAULT_MO_ID;

    protected static final String SECTOR_FUNCTION_ID_ATTRIBUTE = EUtranCellFdd.MANDATORY_ATTRIBUTE_EUTRANCELLFDDID + "==" + EUCFDD1_ID;
    protected static final String SECTOR_FUNCTION_REF_ATTRIBUTE = EUtranCellFdd.MANDATORY_ATTRIBUTE_SECTOR_FUNCTION_REF + "=="
            + SECTOR_EQUIPMENT_FUNCTION_FDN1;

    /*
     * C R E A T E - T E S T S
     */

    @Test
    public void canCreateNodeRootMib() {
        logger.debug("@Test: canCreateNodeRootMib");
        final String nodeRootFdnString = "MeContext=ERBS99";
        final String generationCounterValue = "975";
        final StringifiedAttributeSpecifications attributes = new StringifiedAttributeSpecifications();
        addAttributeSpecification(attributes, "userLabel", "test user label");
        addAttributeSpecification(attributes, GENERATION_COUNTER_ATTRIBUTE, generationCounterValue);
        addAttributeSpecification(attributes, "MeContextId", "1");
        addAttributeSpecification(attributes, "neType", "ENODEB");
        final CmObjectSpecification cmObjectSpecification = new CmObjectSpecificationBuilder().withNamespace(TOP_NAMESPACE)
                .withNamespaceVersion(TOP_NAMESPACE_VERSION).withType("MeContext").withName("ERBS99").withAttributeSpecifications(attributes).build();
        final CmResponse cmResponse = arquillianProxyBean.createNodeRootMib(cmObjectSpecification);
        final CmObject cmObject = assertCmResponse(cmResponse);
        assertMoMetaData(cmObject, TOP_NAMESPACE, TOP_NAMESPACE_VERSION, nodeRootFdnString);
        assertAttribute(cmObject, GENERATION_COUNTER_ATTRIBUTE, generationCounterValue, Long.class);
    }

    @Test
    public void canCreateCppMoUnderExistingMeContext() {
        logger.debug("@Test: canCreateCppMoUnderExistingMeContext");
        final StringifiedAttributeSpecifications attributes = new StringifiedAttributeSpecifications();
        addAttributeSpecification(attributes, ManagedElement.MANDATORY_ATTRIBUTE_MANAGEDELEMENTID, DEFAULT_MO_ID);
        final CmObjectSpecification cmObjectSpecification = new CmObjectSpecificationBuilder().withNamespace(CPP_NAMESPACE)
                .withNamespaceVersion(CPP_NAMESPACE_VERSION).withType("ManagedElement").withName("99").withAttributeSpecifications(attributes)
                .build();

        final CmResponse cmResponse = arquillianProxyBean.createMibRoot(meContextAsCmObject.getFdn(), cmObjectSpecification);
        final CmObject cmObject = assertCmResponse(cmResponse);
        final String expectedFdn = MECONTEXT_FDN1 + ",ManagedElement=99";

        assertMoMetaData(cmObject, CPP_NAMESPACE, CPP_NAMESPACE_VERSION, expectedFdn);
        assertAttribute(cmObject, ManagedElement.MANDATORY_ATTRIBUTE_MANAGEDELEMENTID, DEFAULT_MO_ID, String.class);
    }

    @Test
    public void canCreateCppMoUnderExistingMeContextWithSequenceAttributes() {
        logger.debug("@Test: canCreateCppMoUnderExistingMeContextWithSequenceAttributes");
        final StringifiedAttributeSpecifications attributes = new StringifiedAttributeSpecifications();
        final StringifiedAttributeSpecifications healthCheckAttributes = new StringifiedAttributeSpecifications();

        addAttributeSpecification(healthCheckAttributes, "time", "12:00"); //basic string attribute
        addAttributeSpecification(healthCheckAttributes, "weekday", "THURSDAY"); //enum attribute

        final List<StringifiedAttributeSpecifications> sequence = new ArrayList<>();
        sequence.add(healthCheckAttributes);
        addAttributeSpecification(attributes, ManagedElement.MANDATORY_ATTRIBUTE_MANAGEDELEMENTID, DEFAULT_MO_ID);
        addAttributeSpecification(attributes, "healthCheckSchedule", sequence);

        final CmObjectSpecification cmObjectSpecification = new CmObjectSpecificationBuilder().withNamespace(CPP_NAMESPACE)
                .withNamespaceVersion(CPP_NAMESPACE_VERSION).withType("ManagedElement").withName("99").withAttributeSpecifications(attributes)
                .build();

        final CmResponse cmResponse = arquillianProxyBean.createMibRoot(meContextAsCmObject.getFdn(), cmObjectSpecification);

        final CmObject cmObject = assertCmResponse(cmResponse);
        final String expectedFdn = MECONTEXT_FDN1 + ",ManagedElement=99";

        assertMoMetaData(cmObject, CPP_NAMESPACE, CPP_NAMESPACE_VERSION, expectedFdn);

    }

    @Test
    public void canCreateENodeBFunctionUnderExistingManageElementWithComplexAttributes() {
        logger.debug("@Test: canCreateENodeBFunctionUnderExistingManageElementWithComplexAttributes");
        final StringifiedAttributeSpecifications attributes = new StringifiedAttributeSpecifications();
        addAttributeSpecification(attributes, ENodeBFunction.MANDATORY_ATTRIBUTE_ENODEBFUNCTIONID, DEFAULT_MO_ID);
        final String complexAttributeName = "eNodeBPlmnId";
        final StringifiedAttributeSpecifications complexAttribute = new StringifiedAttributeSpecifications();
        addAttributeSpecification(complexAttribute, "mcc", "1");
        addAttributeSpecification(complexAttribute, "mnc", "123");
        addAttributeSpecification(complexAttribute, "mncLength", "2");
        addAttributeSpecification(attributes, complexAttributeName, complexAttribute);
        final CmObjectSpecification cmObjectSpecification = new CmObjectSpecificationBuilder().withNamespace(ENodeBFunction.ERBS_NAMESPACE)
                .withNamespaceVersion(ENodeBFunction.ERBS_NAMESPACE_VERSION).withType("ENodeBFunction").withName("99")
                .withAttributeSpecifications(attributes).build();

        final CmResponse response = arquillianProxyBean.createMibRoot(managedElementAsCmObject.getFdn(), cmObjectSpecification);
        assertNotNull("Response is null?!", response);
        final CmObject cmObject = assertCmResponse(response);
        final String fdnString = MANAGED_ELEMENT_FDN1 + ",ENodeBFunction=99";
        assertMoMetaData(cmObject, ENodeBFunction.ERBS_NAMESPACE, ENodeBFunction.ERBS_NAMESPACE_VERSION, fdnString);
        assertAttribute(cmObject, ENodeBFunction.MANDATORY_ATTRIBUTE_ENODEBFUNCTIONID, DEFAULT_MO_ID, String.class);
        final String complexAttributeAsString = cmObject.getAttributes().get(complexAttributeName).toString();
        assertTrue("complexAttributeAsString should have contained 'mnc=123' but was " + complexAttributeAsString,
                complexAttributeAsString.contains("mnc=123"));
    }

    @Test
    public void canCreateENodeBFunctionUnderExistingManageElementWithSequenceAttributes() {
        logger.debug("@Test: canCreateENodeBFunctionUnderExistingManageElementWithSequenceAttributes");
        final StringifiedAttributeSpecifications attributes = new StringifiedAttributeSpecifications();
        final StringifiedAttributeSpecifications sequenceAttributes = new StringifiedAttributeSpecifications();

        addAttributeSpecification(sequenceAttributes, "enbId", "1");
        addAttributeSpecification(sequenceAttributes, "mcc", "1");
        addAttributeSpecification(sequenceAttributes, "mnc", "123");
        addAttributeSpecification(sequenceAttributes, "mncLength", "2");

        final List<StringifiedAttributeSpecifications> sequence = new ArrayList<>();
        sequence.add(sequenceAttributes);
        addAttributeSpecification(attributes, ENodeBFunction.MANDATORY_ATTRIBUTE_ENODEBFUNCTIONID, DEFAULT_MO_ID);
        addAttributeSpecification(attributes, "x2WhiteList", sequence);

        final CmObjectSpecification cmObjectSpecification = new CmObjectSpecificationBuilder().withNamespace(ENodeBFunction.ERBS_NAMESPACE)
                .withNamespaceVersion(ENodeBFunction.ERBS_NAMESPACE_VERSION).withType("ENodeBFunction").withName("1001")
                .withAttributeSpecifications(attributes).build();

        final CmResponse cmResponse = arquillianProxyBean.createMibRoot(managedElementAsCmObject.getFdn(), cmObjectSpecification);

        final CmObject cmObject = assertCmResponse(cmResponse);

        final String fdnString = MANAGED_ELEMENT_FDN1 + ",ENodeBFunction=1001";
        assertMoMetaData(cmObject, ENodeBFunction.ERBS_NAMESPACE, ENodeBFunction.ERBS_NAMESPACE_VERSION, fdnString);

    }

    @Test
    public void canCreateENodeBFunctionWithEnum() {
        logger.debug("@Test: canCreateENodeBFunctionWithEnum");
        final StringifiedAttributeSpecifications attributes = new StringifiedAttributeSpecifications();
        addAttributeSpecification(attributes, ENodeBFunction.MANDATORY_ATTRIBUTE_ENODEBFUNCTIONID, DEFAULT_MO_ID);
        final String ERBS_ATTRIBUTE_ENUM = "dnsLookupOnTai";
        final String ERBS_ATTRIBUTE_ENUM_VALUE = "OFF";
        addAttributeSpecification(attributes, ERBS_ATTRIBUTE_ENUM, ERBS_ATTRIBUTE_ENUM_VALUE);
        final CmObjectSpecification cmObjectSpecification = new CmObjectSpecificationBuilder().withNamespace(ENodeBFunction.ERBS_NAMESPACE)
                .withNamespaceVersion(ENodeBFunction.ERBS_NAMESPACE_VERSION).withType("ENodeBFunction").withName("99")
                .withAttributeSpecifications(attributes).build();

        final CmResponse response = arquillianProxyBean.createMibRoot(managedElementAsCmObject.getFdn(), cmObjectSpecification);
        assertNotNull("Response is null?!", response);
        final CmObject cmObject = assertCmResponse(response);
        final String fdnString = MANAGED_ELEMENT_FDN1 + ",ENodeBFunction=99";
        assertMoMetaData(cmObject, ENodeBFunction.ERBS_NAMESPACE, ENodeBFunction.ERBS_NAMESPACE_VERSION, fdnString);
        assertAttribute(cmObject, ERBS_ATTRIBUTE_ENUM, ERBS_ATTRIBUTE_ENUM_VALUE, String.class);
    }

    @Test
    public void createENodeBFunctionWithEnumReturnsExecutionError() {
        logger.debug("@Test: createENodeBFunctionWithEnumReturnsExecutionError");
        final StringifiedAttributeSpecifications attributes = new StringifiedAttributeSpecifications();
        addAttributeSpecification(attributes, ENodeBFunction.MANDATORY_ATTRIBUTE_ENODEBFUNCTIONID, DEFAULT_MO_ID);
        final String ERBS_ATTRIBUTE_ENUM = "dnsLookupOnTai";
        final String invalidEnumValue = "Invalid Value";
        addAttributeSpecification(attributes, ERBS_ATTRIBUTE_ENUM, invalidEnumValue);
        final CmObjectSpecification cmObjectSpecification = new CmObjectSpecificationBuilder().withNamespace(ENodeBFunction.ERBS_NAMESPACE)
                .withNamespaceVersion(ENodeBFunction.ERBS_NAMESPACE_VERSION).withType("ENodeBFunction").withName("99")
                .withAttributeSpecifications(attributes).build();

        final CmResponse response = arquillianProxyBean.createMibRoot(managedElementAsCmObject.getFdn(), cmObjectSpecification);
        assertNotNull("Response is null?!", response);
        assertEquals(-1, response.getStatusCode());
        assertTrue(response.getStatusMessage().contains(ERBS_ATTRIBUTE_ENUM));
    }

    @Test
    public void createENodeBFunctionWithComplexAttributesWithButMissingAttributeFailsCorrectly() {
        logger.debug("@Test: createENodeBFunctionWithComplexAttributesWithButMissingAttributeFailsCorrectly");
        final StringifiedAttributeSpecifications attributes = new StringifiedAttributeSpecifications();
        addAttributeSpecification(attributes, ENodeBFunction.MANDATORY_ATTRIBUTE_ENODEBFUNCTIONID, DEFAULT_MO_ID);
        final String complexAttributeName = "eNodeBPlmnId";
        final StringifiedAttributeSpecifications complexAttribute = new StringifiedAttributeSpecifications();
        addAttributeSpecification(complexAttribute, "mcc", "1");
        addAttributeSpecification(complexAttribute, "mnc", "123");
        addAttributeSpecification(attributes, complexAttributeName, complexAttribute);
        final CmObjectSpecification cmObjectSpecification = new CmObjectSpecificationBuilder().withNamespace(ENodeBFunction.ERBS_NAMESPACE)
                .withNamespaceVersion(ENodeBFunction.ERBS_NAMESPACE_VERSION).withType("ENodeBFunction").withName("100")
                .withAttributeSpecifications(attributes).build();

        final CmResponse response = arquillianProxyBean.createMibRoot(managedElementAsCmObject.getFdn(), cmObjectSpecification);
        assertNegativeResponse(response, "COMPLEX_REF", "eNodeBPlmnId", "the following mandatory attributes [mncLength] are required");
    }

    @Test
    public void canCreateManagedObject() {
        logger.debug("@Test: canCreateManagedObject");
        // This is the first 'normal' (NON-MIB Root) ManagedObject
        final StringifiedAttributeSpecifications attributes = new StringifiedAttributeSpecifications();
        addAttributeSpecification(attributes, "GeraNetworkId", "1");
        final CmObjectSpecification cmObjectSpecification = new CmObjectSpecificationBuilder().withType("GeraNetwork").withName("1")
                .withAttributeSpecifications(attributes).build();

        final CmResponse cmResponse = arquillianProxyBean.createManagedObject(eNodeBFunctionAsCmObject.getFdn(), cmObjectSpecification);

        final CmObject cmObject = assertCmResponse(cmResponse);
        final String fdnString = ENODEB_FUNCTION_FDN1 + ",GeraNetwork=1";

        assertMoMetaData(cmObject, ENodeBFunction.ERBS_NAMESPACE, ENodeBFunction.ERBS_NAMESPACE_VERSION, fdnString);
        assertAttribute(cmObject, "GeraNetworkId", "1", String.class);
    }

    /*
     * N E G A T I V E - T E S T S
     */
    @Test
    public void createNodeRootMibWithBadAttributesReturnsError() {
        logger.debug("@Test: createNodeRootMibWithBadAttributesReturnsError");
        final StringifiedAttributeSpecifications badMoAttributes = new StringifiedAttributeSpecifications();
        final String invalidValue = "not_a_long";
        addAttributeSpecification(badMoAttributes, GENERATION_COUNTER_ATTRIBUTE, invalidValue);
        final CmObjectSpecification cmObjectSpecification = new CmObjectSpecificationBuilder().withNamespace(TOP_NAMESPACE)
                .withNamespaceVersion(TOP_NAMESPACE_VERSION).withType("MeContext").withName("99").withAttributeSpecifications(badMoAttributes)
                .build();

        final CmResponse cmResponse = arquillianProxyBean.createNodeRootMib(cmObjectSpecification);
        assertNegativeResponse(cmResponse, invalidValue, "incorrect", "LONG", "not_a_long");
    }

    @Test
    public void createMibRootWithNonExistingParentReturnsError() {
        logger.debug("@Test: createMibRootWithNonExistingParentReturnsError");
        final StringifiedAttributeSpecifications attributes = new StringifiedAttributeSpecifications();
        addAttributeSpecification(attributes, "ManagedElementId", "ManagedElement_test_id");
        final CmObjectSpecification cmObjectSpecification = new CmObjectSpecificationBuilder().withNamespace(CPP_NAMESPACE)
                .withNamespaceVersion(CPP_NAMESPACE_VERSION).withType("ManagedElement").withName("99").withAttributeSpecifications(attributes)
                .build();

        final String missingFdn = "MeContext=fake";
        final CmResponse cmResponse = arquillianProxyBean.createMibRoot(missingFdn, cmObjectSpecification);
        assertNegativeResponse(cmResponse, missingFdn);
    }

    /*
     * S E T - T E S T S
     */

    //ethomit find out why test fails ( eeitsik: seems to fail intermittently, maybe related to database delete issue?)
    @Test
    public void setSeveralAttributesOnGivenMoReturnsOnlyTheSetAttributes() {
        logger.debug("@Test: setSeveralAttributesOnGivenMoReturnsOnlyTheSetAttributes");
        final StringifiedAttributeSpecifications newAttributes = new StringifiedAttributeSpecifications();
        final String generationCounterValue = "347356";
        addAttributeSpecification(newAttributes, "userLabel", "Yet another name");
        addAttributeSpecification(newAttributes, GENERATION_COUNTER_ATTRIBUTE, generationCounterValue);
        final CmResponse cmResponse = arquillianProxyBean.setManagedObjectAttributes(MECONTEXT_FDN1, newAttributes);
        final CmObject newCmObject = assertCmResponse(cmResponse);
        // Note how this test verifies 2 different datatypes: Long and String
        assertAttribute(newCmObject, GENERATION_COUNTER_ATTRIBUTE, generationCounterValue, Long.class);
        assertAttribute(newCmObject, "userLabel", "Yet another name", String.class);
        // Verify that only teh set attributes are returned
        final Map<String, Object> resultAttributes = newCmObject.getAttributes();
        assertEquals(newAttributes.size(), resultAttributes.size());
        assertThat(newAttributes.getAttributeNames(), equalTo(resultAttributes.keySet()));

    }

    @Test
    public void canSetENodeBFunctionWithEnum() {
        logger.debug("@Test: canSetENodeBFunctionWithEnum");
        final StringifiedAttributeSpecifications attributes = new StringifiedAttributeSpecifications();
        addAttributeSpecification(attributes, ENodeBFunction.MANDATORY_ATTRIBUTE_ENODEBFUNCTIONID, DEFAULT_MO_ID);
        final String erbsAttributeEnum = "dnsLookupOnTai";
        final String erbsAttributeEnumValue = "ON";
        addAttributeSpecification(attributes, erbsAttributeEnum, erbsAttributeEnumValue);
        final CmResponse cmResponse = arquillianProxyBean.setManagedObjectAttributes(ENODEB_FUNCTION_FDN1, attributes);
        final CmObject newCmObject = assertCmResponse(cmResponse);
        assertAttribute(newCmObject, erbsAttributeEnum, erbsAttributeEnumValue, String.class);
    }

    @Test
    public void canSetSimpleAttributeOnCertainNodesUsingSearchCriteria() {
        logger.debug("@Test: canSetSimpleAttributeOnCertainNodesUsingSearchCriteria");
        final CmSearchCriteria cmSearchCriteria = createSearchCriteria(ScopeType.NODE_NAME, "E", "ENodeBFunction", CmMatchCondition.STARTS_WITH, null);
        // Define what we want to change e.g an attribute specification
        final StringifiedAttributeSpecifications setterAttributeSpecifications = new StringifiedAttributeSpecifications();
        final String newValue = "some new value";
        addAttributeSpecification(setterAttributeSpecifications, ENodeBFunction.MANDATORY_ATTRIBUTE_ENODEBFUNCTIONID, newValue);
        final CmResponse cmResponse = arquillianProxyBean.setManagedObjectsAttributes(cmSearchCriteria, setterAttributeSpecifications);
        for (final CmObject newCmObject : cmResponse.getCmObjects()) {
            // assert the value was set
            assertAttribute(newCmObject, ENodeBFunction.MANDATORY_ATTRIBUTE_ENODEBFUNCTIONID, newValue, String.class);
            final Map<String, Object> resultAttributes = newCmObject.getAttributes();
            // assert that the attribute(s) that were returned are the same as the one(s) we set
            assertEquals(setterAttributeSpecifications.size(), resultAttributes.size());
            assertThat(setterAttributeSpecifications.getAttributeNames(), equalTo(resultAttributes.keySet()));
        }
    }

    @Test
    public void canSetMultipleAttributesOnCertainNodesUsingSearchCriteria() {
        logger.debug("@Test: canSetMultipleAttributesOnCertainNodesUsingSearchCriteria");
        final CmSearchCriteria cmSearchCriteria = createSearchCriteria(ScopeType.NODE_NAME, "E", "ENodeBFunction", CmMatchCondition.STARTS_WITH, null);
        // Define what we want to change e.g an attribute specification
        final StringifiedAttributeSpecifications setterAttributeSpecifications = new StringifiedAttributeSpecifications();
        final StringifiedAttributeSpecifications setterSequencedComplexAttributeSpecification = new StringifiedAttributeSpecifications();
        final StringifiedAttributeSpecifications setterComplexAttributeSpecification = new StringifiedAttributeSpecifications();
        final String sequencedComplexAttributeName = "x2BlackList";
        final String complexAttributeName = "eNodeBPlmnId";
        final String enumAttributeName = "dnsLookupOnTai";
        final String enumAttributeValue = "ON";

        addAttributeSpecification(setterSequencedComplexAttributeSpecification, "enbId", "1");
        addAttributeSpecification(setterSequencedComplexAttributeSpecification, "mcc", "1");
        addAttributeSpecification(setterSequencedComplexAttributeSpecification, "mnc", "123");
        addAttributeSpecification(setterSequencedComplexAttributeSpecification, "mncLength", "2");

        addAttributeSpecification(setterComplexAttributeSpecification, "mcc", "1");
        addAttributeSpecification(setterComplexAttributeSpecification, "mnc", "123");
        addAttributeSpecification(setterComplexAttributeSpecification, "mncLength", "2");

        addAttributeSpecification(setterAttributeSpecifications, ENodeBFunction.MANDATORY_ATTRIBUTE_ENODEBFUNCTIONID, DEFAULT_MO_ID);
        addAttributeSpecification(setterAttributeSpecifications, sequencedComplexAttributeName, setterSequencedComplexAttributeSpecification);
        addAttributeSpecification(setterAttributeSpecifications, complexAttributeName, setterComplexAttributeSpecification);
        addAttributeSpecification(setterAttributeSpecifications, enumAttributeName, enumAttributeValue);

        final CmResponse cmResponse = arquillianProxyBean.setManagedObjectsAttributes(cmSearchCriteria, setterAttributeSpecifications);
        for (final CmObject newCmObject : cmResponse.getCmObjects()) {
            // assert the values were set
            assertAttribute(newCmObject, ENodeBFunction.MANDATORY_ATTRIBUTE_ENODEBFUNCTIONID, DEFAULT_MO_ID, String.class);
            final String complexAttributeAsString = newCmObject.getAttributes().get(complexAttributeName).toString();
            assertTrue("complexAttributeAsString should have contained 'mnc=123' but was " + complexAttributeAsString,
                    complexAttributeAsString.contains("mnc=123"));
            assertAttribute(newCmObject, enumAttributeName, enumAttributeValue, String.class);
            final Map<String, Object> resultAttributes = newCmObject.getAttributes();
            // assert that the attribute(s) that were return are the same as the one(s) we set
            assertEquals(setterAttributeSpecifications.size(), resultAttributes.size());
            assertThat(setterAttributeSpecifications.getAttributeNames(), equalTo(resultAttributes.keySet()));
        }

    }

    /*
     * S E T - N E G A T I V E - T E S T S
     */

    @Test
    public void setManagedObjectAttributesReturnsFailureWhenGivenMoDoesNotExist() {
        logger.debug("@Test: setManagedObjectAttributesReturnsFailureWhenGivenMoDoesNotExist");
        final String nodeRootFdnString = "MeContext=NotExistsingNode";
        // No attributes required for this test as code will first verify existence of MO
        final CmResponse cmResponse = arquillianProxyBean.setManagedObjectAttributes(nodeRootFdnString, null);
        assertEquals(-1, cmResponse.getStatusCode());
        assertNegativeResponse(cmResponse, nodeRootFdnString);
    }

    @Test
    public void setManagedObjectAttributesReturnsFailureWhenGivenAttributeDoesNotExistAndMessageContainsAttributeName() {
        logger.debug("@Test: setManagedObjectAttributesReturnsFailureWhenGivenAttributeDoesNotExistAndMessageContainsAttributeName");
        final StringifiedAttributeSpecifications attributes = new StringifiedAttributeSpecifications();
        addAttributeSpecification(attributes, "NoExistAttribute", null);
        final CmResponse cmResponse = arquillianProxyBean.setManagedObjectAttributes(MECONTEXT_FDN1, attributes);
        // TODO ETHOMIT should become a EXECUTION ERROR instead
        assertNegativeResponse(cmResponse, ErrorHandler.UNEXPECTED_ERROR, "NoExistAttribute");
    }

    @Test
    public void setManagedObjectAttributesReturnsFailureWhenGivenAttributeValueIsInvalid() {
        logger.debug("@Test: setManagedObjectAttributesReturnsFailureWhenGivenAttributeValueIsInvalid");
        final StringifiedAttributeSpecifications attributes = new StringifiedAttributeSpecifications();
        final String invalidValue = "invalid value for long";
        addAttributeSpecification(attributes, GENERATION_COUNTER_ATTRIBUTE, invalidValue);
        final CmResponse cmResponse = arquillianProxyBean.setManagedObjectAttributes(MECONTEXT_FDN1, attributes);
        assertNegativeResponse(cmResponse, GENERATION_COUNTER_ATTRIBUTE);
    }

    @Test
    public void setManagedObjectAttributesReturnsFailureWhenOneGivenAttributeDoesNotExist() {
        logger.debug("@Test: setManagedObjectAttributesReturnsFailureWhenOneGivenAttributeDoesNotExist");
        StringifiedAttributeSpecifications newAttributes = new StringifiedAttributeSpecifications();
        final String generationCounterValue = "347359";
        addAttributeSpecification(newAttributes, "invalid_name", "Yet another name");
        addAttributeSpecification(newAttributes, GENERATION_COUNTER_ATTRIBUTE, generationCounterValue);
        CmResponse cmResponse = arquillianProxyBean.setManagedObjectAttributes(MECONTEXT_FDN1, newAttributes);
        // TODO ETHOMIT should become a EXECUTION ERROR instead
        assertNegativeResponse(cmResponse, ErrorHandler.UNEXPECTED_ERROR, "invalid_name");
        //check GENERATION_COUNTER did not get set because of the bad attribute without using cm-reader
        newAttributes = new StringifiedAttributeSpecifications();
        addAttributeSpecification(newAttributes, "userLabel", "Yet another name");
        cmResponse = arquillianProxyBean.setManagedObjectAttributes(MECONTEXT_FDN1, newAttributes);
        final CmObject newCmObject = assertCmResponse(cmResponse);
        assertNull(newCmObject.getAttributes().get(GENERATION_COUNTER_ATTRIBUTE));
    }

    @Test
    public void setManagedObjectAttributesReturnsFailureWhenOneGivenAttributeValueIsInvalid() {
        logger.debug("@Test: setManagedObjectAttributesReturnsFailureWhenOneGivenAttributeValueIsInvalid");
        StringifiedAttributeSpecifications attributes = new StringifiedAttributeSpecifications();
        final String invalidValue = "invalid value for long";
        addAttributeSpecification(attributes, "userLabel", "Yet another name");
        addAttributeSpecification(attributes, GENERATION_COUNTER_ATTRIBUTE, invalidValue);
        CmResponse cmResponse = arquillianProxyBean.setManagedObjectAttributes(MECONTEXT_FDN1, attributes);
        assertNegativeResponse(cmResponse, GENERATION_COUNTER_ATTRIBUTE);
        //check userLabel did not get set because of the bad attribute without using cm-reader
        attributes = new StringifiedAttributeSpecifications();
        addAttributeSpecification(attributes, GENERATION_COUNTER_ATTRIBUTE, "347356");
        cmResponse = arquillianProxyBean.setManagedObjectAttributes(MECONTEXT_FDN1, attributes);
        final CmObject newCmObject = assertCmResponse(cmResponse);
        assertNull(newCmObject.getAttributes().get("userLabel"));
    }

    @Test
    public void setManagedObjectEnumAttributeReturnsFailureWhenGivenEnumAttributeIsInvalid() {
        logger.debug("@Test: setManagedObjectEnumAttributeReturnsFailureWhenGivenEnumAttributeIsInvalid");
        final StringifiedAttributeSpecifications attributes = new StringifiedAttributeSpecifications();
        final String erbsAttributeEnum = "dnsLookupOnTai";
        final String erbsAttributeEnumInvalidValue = "some invalid value";
        addAttributeSpecification(attributes, erbsAttributeEnum, erbsAttributeEnumInvalidValue);
        final CmResponse cmResponse = arquillianProxyBean.setManagedObjectAttributes(ENODEB_FUNCTION_FDN1, attributes);
        assertNegativeResponse(cmResponse, erbsAttributeEnum);
    }

    @Test
    public void setManagedObjectsAttributesReturnsFailureWhenGivenBadAttributeName() {
        logger.debug("@Test: setManagedObjectsAttributesReturnsFailureWhenGivenBadAttributeName");
        final CmSearchCriteria cmSearchCriteria = createSearchCriteria(ScopeType.NODE_NAME, "E", "ENodeBFunction", CmMatchCondition.STARTS_WITH, null);
        final StringifiedAttributeSpecifications attributes = new StringifiedAttributeSpecifications();
        addAttributeSpecification(attributes, GENERATION_COUNTER_ATTRIBUTE + "BAD", "133");
        final CmResponse cmResponse = arquillianProxyBean.setManagedObjectsAttributes(cmSearchCriteria, attributes);
        assertNegativeResponse(cmResponse, ErrorHandler.UNEXPECTED_ERROR, GENERATION_COUNTER_ATTRIBUTE + "BAD");
    }

    /*
     * D E L E T E - T E S T S
     */

    @Test
    public void canDeleteEUtranCellFDDByFDN() {
        logger.debug("@Test: canDeleteEUtranCellFDD");
        final CmResponse cmResponse = arquillianProxyBean.deleteManagedObject(EUTRANCELLFDD_FDN1);
        assertEquals(1, cmResponse.getStatusCode());
    }

    /*
     * D E L E T E - N E G A T I V E - T E S T S
     */
    @Test
    public void deleteMoReturnsFailureWhenGivenMoDoesNotExist() {
        logger.debug("@Test: deleteMoReturnsFailureWhenGivenMoDoesNotExist");
        final String nodeRootFdnString = "MeContext=NotExistsingNode";
        final CmResponse cmResponse = arquillianProxyBean.deleteManagedObject(nodeRootFdnString);
        assertEquals(-1, cmResponse.getStatusCode());
        assertNegativeResponse(cmResponse, "The supplied FDN " + nodeRootFdnString + " does not exist in the database");
    }

    /*
     * D E L E T E - W I T H - D E S C E N D A N T S - T E S T S
     */
    @Test
    public void canDeleteMoTreeByFdnUseAllParameter() {
        logger.debug("@Test: canDeleteMoTreeByFdnUseAllParameter");
        final CmResponse cmResponse = arquillianProxyBean.deleteManagedObjectWithDescendants(MECONTEXT_FDN1);
        assertEquals(5, cmResponse.getStatusCode());
    }

    @Test
    public void canNotDeleteMoHasChildWhenNotUseAllParameter() {
        logger.debug("@Test: canNotDeleteMoHasChildWhenNotUseAllParemeter");
        final CmResponse cmResponse = arquillianProxyBean.deleteManagedObject(MECONTEXT_FDN1);
        assertEquals(-1, cmResponse.getStatusCode());
        assertNegativeResponse(cmResponse, "Cannot delete " + NODE_NAME1, " because it has 4 descendant(s)");
    }

    @Test
    public void deleteMoTreeReturnsFailureWhenGivenMoDoesNotExist() {
        logger.debug("@Test: deleteMoTreeReturnsFailureWhenGivenMoDoesNotExist");
        final String nodeRootFdnString = "MeContext=NotExistsingNode";
        final CmResponse cmResponse = arquillianProxyBean.deleteManagedObject(nodeRootFdnString);
        assertEquals(-1, cmResponse.getStatusCode());
        assertNegativeResponse(cmResponse, "The supplied FDN " + nodeRootFdnString + " does not exist in the database");
    }

    /*
     * D E L E T E - R E T U R N - F D N S
     */

    @Test
    public void canDeletePersistenceObjectsUsingSearchCriteriaIncludesMultipleAttributes() {
        final String[] attributes = { SECTOR_FUNCTION_ID_ATTRIBUTE, SECTOR_FUNCTION_REF_ATTRIBUTE };
        final CmSearchCriteria cmSearchCriteria = createSearchCriteria(ScopeType.UNSPECIFIED, null, "EUtranCellFDD", CmMatchCondition.EQUALS,
                attributes);
        final CmResponse cmResponse = arquillianProxyBean.deletePersistenceObjects(cmSearchCriteria);
        assertEquals(1, cmResponse.getStatusCode());
        assertTrue(cmResponse.getStatusMessage().contains(EUTRANCELLFDD_FDN1));
    }

    @Test
    public void canNotDeletePersistenceObjectsWhenHasChildUsingSearchCriteria() {
        final String[] attributes = { ENODEB_FUNCTION_ID_ATTRIBUTE, ENODEB_FUNCTION_USERLABEL_ATTRIBUTE };
        final CmSearchCriteria cmSearchCriteria = createSearchCriteria(ScopeType.UNSPECIFIED, null, "ENodeBFunction", CmMatchCondition.EQUALS,
                attributes);
        final CmResponse cmResponse = arquillianProxyBean.deletePersistenceObjects(cmSearchCriteria);
        assertNegativeResponse(cmResponse, DEFAULT_MO_ID, "descendant");
    }

    /*
     * A C T I O N - M E T H O D S
     */
    @Test
    public void canPerformActionWithNoAttributes() {
        logger.debug("@Test: canPerformActionWithNoAttributes");
        final ActionSpecification actionSpecification = new ActionSpecification("deleteAllSlots");
        final CmResponse cmResponse = arquillianProxyBean.performAction(ENODEB_FUNCTION_FDN2, actionSpecification);
        assertEquals(1, cmResponse.getStatusCode());
        assertEquals(1, cmResponse.getCmObjects().size());
        final CmObject actualCmObject = cmResponse.getCmObjects().iterator().next();
        assertMoMetaData(actualCmObject, ENodeBFunction.ERBS_NAMESPACE, ENodeBFunction.ERBS_NAMESPACE_VERSION, ENODEB_FUNCTION_FDN2);
        assertEquals("1 instance(s)", cmResponse.getStatusMessage());
        //ensure that no attributes are being returned
        assertTrue(cmResponse.getCmObjects().iterator().next().getAttributes().isEmpty());

    }

    @Test
    public void canPerformMultipleActionsOneNodeUsingSearchCriteria() {
        logger.debug("@Test: canPerformMultipleActionsOneNodeUsingSearchCriteria");
        final CmSearchCriteria cmSearchCriteria = createSearchCriteria(ScopeType.NODE_NAME, NODE_NAME1, "ENodeBFunction", CmMatchCondition.EQUALS,
                null);
        final ActionSpecification actionSpecification = new ActionSpecification("deleteAllSlots");

        final CmResponse cmResponse = arquillianProxyBean.performAction(cmSearchCriteria, actionSpecification);

        for (final CmObject cmObject : cmResponse.getCmObjects()) {
            assertTrue(cmResponse.getStatusCode() > 0); //indicates success and response was populated (statusCode cant be > 1)
            assertEquals(cmResponse.getStatusCode(), cmResponse.getCmObjects().size());
            assertEquals("ENodeBFunction", cmObject.getType());

            assertEquals(ENodeBFunction.ERBS_NAMESPACE, cmObject.getNamespace());
            assertEquals(ENodeBFunction.ERBS_NAMESPACE_VERSION, cmObject.getNamespaceVersion());
        }

    }

    /*
     * P R I V A T E - M E T H O D S
     */

    private CmSearchCriteria createSearchCriteria(final ScopeType scopeType, final String id, final String type,
            final CmMatchCondition cmMatchCondition, final String... attributes) {
        final String ids[] = { id };
        return createSearchCriteriaMultiNodeIdScope(scopeType, ids, type, cmMatchCondition, attributes);
    }

    private CmSearchCriteria createSearchCriteriaMultiNodeIdScope(final ScopeType scopeType, final String[] ids, final String type,
            final CmMatchCondition cmMatchCondition, final String... attributes) {
        final CmSearchCriteria cmSearchCriteria = new CmSearchCriteria();
        final CmObjectSpecification cmObjectSpecification = new CmObjectSpecification();
        final List<CmSearchScope> scopeList = new ArrayList<>(ids.length);
        final CmSearchScopeBuilder cmSearchScopeBuilder = new CmSearchScopeBuilder();
        for (final String id : ids) {
            cmSearchScopeBuilder.withScopeType(scopeType).withValue(id).withMatchCondition(cmMatchCondition);
            scopeList.add(cmSearchScopeBuilder.build());
        }
        cmSearchCriteria.setCmSearchScopes(scopeList);
        cmObjectSpecification.setNamespace(ENodeBFunction.ERBS_NAMESPACE);
        cmObjectSpecification.setType(type);
        cmObjectSpecification.setNamespaceVersion(ENodeBFunction.ERBS_NAMESPACE_VERSION);
        if (attributes != null) {
            final StringifiedAttributeSpecifications stringifiedAttributeSpecifications = new StringifiedAttributeSpecifications();
            for (final String attribute : attributes) {
                final AttributeSpecification attributeSpecification = attributeString2specificationForTest(attribute);
                stringifiedAttributeSpecifications.addAttributeSpecification(attributeSpecification);
            }
            cmObjectSpecification.setAttributeSpecifications(stringifiedAttributeSpecifications);
        }
        cmSearchCriteria.setSingleCmObjectSpecification(cmObjectSpecification);
        return cmSearchCriteria;
    }

    private AttributeSpecification attributeString2specificationForTest(final String attribute) {
        final AttributeSpecification attributeSpecification = new AttributeSpecification();
        if (attribute.contains("==")) {
            final String[] tokens = attribute.split("==");
            final String name = tokens[0];
            String value = tokens[1];
            attributeSpecification.setCmMatchCondition(CmMatchCondition.EQUALS);
            value = determineAttributeSpecificationCmMatchConditon(attributeSpecification, value);
            attributeSpecification.setName(name);
            attributeSpecification.setValue(value);
        } else {
            attributeSpecification.setName(attribute);
        }
        return attributeSpecification;
    }

    private String determineAttributeSpecificationCmMatchConditon(final AttributeSpecification attributeSpecification, String value) {
        if (value.toString().equals("*")) {
            attributeSpecification.setCmMatchCondition(CmMatchCondition.NO_MATCH_REQUIRED);
            value = null;
        } else if (value.toString().endsWith("*") && value.toString().startsWith("*")) {
            attributeSpecification.setCmMatchCondition(CmMatchCondition.CONTAINS);
            value = value.substring(1, value.length() - 1);
        } else if (value.toString().endsWith("*")) {
            attributeSpecification.setCmMatchCondition(CmMatchCondition.STARTS_WITH);
            value = value.substring(0, value.length() - 1);
        } else if (value.toString().startsWith("*")) {
            attributeSpecification.setCmMatchCondition(CmMatchCondition.ENDS_WITH);
            value = value.substring(1);
        }
        return value;
    }

    private void addAttributeSpecification(final StringifiedAttributeSpecifications attributes, final String name, final Object value) {
        final AttributeSpecification attributeSpecification = new AttributeSpecification();
        attributeSpecification.setName(name);
        attributeSpecification.setValue(value);
        attributes.addAttributeSpecification(attributeSpecification);
    }

    private CmObject assertCmResponse(final CmResponse cmResponse) {
        assertEquals("Statuscode not 1, status message:" + cmResponse.getStatusMessage(), 1, cmResponse.getStatusCode());
        final CmObject cmObject = cmResponse.getCmObjects().iterator().next();
        return cmObject;
    }

    private void assertMoMetaData(final CmObject cmObject, final String namespace, final String version, final String fdnString) {
        assertEquals(namespace, cmObject.getNamespace());
        assertEquals(fdnString, cmObject.getFdn());
        assertEquals(version, cmObject.getNamespaceVersion());
    }

    private void assertAttribute(final CmObject cmObject, final String attributeName, final String expectedValue, final Class expectedClass) {
        final Object returnedValue = cmObject.getAttributes().get(attributeName);
        assertEquals(expectedValue, returnedValue.toString());
        assertEquals(expectedClass, returnedValue.getClass());
    }

    private void assertNegativeResponse(final CmResponse cmResponse, final String... partsOfErrorMessage) {
        assertNegativeResponse(cmResponse, ErrorHandler.EXECUTION_ERROR, partsOfErrorMessage);
    }

    private void assertNegativeResponse(final CmResponse cmResponse, final int errorCode, final String... partsOfErrorMessage) {
        for (final String part : partsOfErrorMessage) {
            assertTrue("expected to find '" + part + "' in error message: \"" + cmResponse.getStatusMessage() + "\"", cmResponse.getStatusMessage()
                    .contains(part));
        }
        assertEquals(errorCode, cmResponse.getStatusCode());
    }

}
