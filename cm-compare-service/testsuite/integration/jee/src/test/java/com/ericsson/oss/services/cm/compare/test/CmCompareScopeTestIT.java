package com.ericsson.oss.services.cm.compare.test;

import static com.ericsson.oss.services.cm.compare.test.DpsTestMo.*;
import static org.junit.Assert.*;

import javax.ejb.EJB;
import javax.ejb.Stateless;

import org.jboss.arquillian.junit.Arquillian;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.ericsson.oss.services.cm.cmshared.dto.*;
import com.ericsson.oss.services.cm.cmshared.dto.search.*;
import com.ericsson.oss.services.cm.cmshared.dto.search.CmSearchScope.ScopeType;
import com.ericsson.oss.services.cm.testutil.builders.CmObjectSpecificationBuilder;


@RunWith(Arquillian.class)
@Stateless
public class CmCompareScopeTestIT extends CmCompareTestBase {

    @EJB
    ArquillianProxy arquillianProxyBean;
    
    protected static final String GENERATION_COUNTER_ATTRIBUTE = "generationCounter";
    private static final String CONFIG_SCOPE = "scopedConfig";

    
    @Test
    public void createConfig_withScope_configCreated() {
        logger.debug("@Test: createConfig_withScope_configCreated");
        final String nodeRootFdnString = "MeContext=ERBS99";
        final String generationCounterValue = "975";
        final StringifiedAttributeSpecifications attributes = new StringifiedAttributeSpecifications();
        addAttributeSpecification(attributes, "userLabel", "test user label");
        addAttributeSpecification(attributes, GENERATION_COUNTER_ATTRIBUTE, generationCounterValue);
        addAttributeSpecification(attributes, "MeContextId", "1");
        addAttributeSpecification(attributes, "neType", "ENODEB");
        final CmObjectSpecification cmObjectSpecification = new CmObjectSpecificationBuilder().withNamespace(TOP_NAMESPACE)
                .withNamespaceVersion(TOP_NAMESPACE_VERSION).withType("MeContext").withName("ERBS99").withAttributeSpecifications(attributes).build();
        
        final CmSearchScope scope = new CmSearchScope();
        scope.setScopeType(ScopeType.NODE_NAME);
        
        
        final CmSearchCriteria criteria = new CmSearchCriteria();
        criteria.setCmSearchScopes(scope);
        criteria.setCmObjectSpecifications(cmObjectSpecification);
        
        
        final CmResponse cmResponse = arquillianProxyBean.createConfig(CONFIG_SCOPE,criteria);
        final CmObject cmObject = assertCmResponse(cmResponse);
        assertMoMetaData(cmObject, TOP_NAMESPACE, TOP_NAMESPACE_VERSION, nodeRootFdnString);
        assertAttribute(cmObject, GENERATION_COUNTER_ATTRIBUTE, generationCounterValue, Long.class);
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
        
}
