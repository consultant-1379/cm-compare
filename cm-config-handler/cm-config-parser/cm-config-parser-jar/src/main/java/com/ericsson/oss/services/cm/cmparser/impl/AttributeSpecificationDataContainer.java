/*------------------------------------------------------------------------------
 *******************************************************************************
 * COPYRIGHT Ericsson 2013
 *
 * The copyright to the computer program(s) herein is the property of
 * Ericsson Inc. The programs may be used and/or copied only with written
 * permission from Ericsson Inc. or in accordance with the terms and
 * conditions stipulated in the agreement/contract under which the
 * program(s) have been supplied.
 *******************************************************************************
 *----------------------------------------------------------------------------*/
package com.ericsson.oss.services.cm.cmparser.impl;

import com.ericsson.oss.services.cm.cmshared.dto.AttributeSpecification;
import com.ericsson.oss.services.cm.cmshared.dto.AttributeSpecificationContainer;
import com.ericsson.oss.services.cm.cmshared.dto.StringifiedAttributeSpecifications;
import com.ericsson.oss.services.cm.cmshared.dto.search.CmMatchCondition;

/**
 * 
 * {@link DataContainer} implementation for Attribute specifications.
 * 
 */
public class AttributeSpecificationDataContainer implements DataContainer {

    private final AttributeSpecificationContainer attributeSpecificationContainer = new StringifiedAttributeSpecifications();

    @Override
    public void addValue(final String name, final Object value, final CmMatchCondition cmMatchCondition) {
        final AttributeSpecification attributeSpecification = new AttributeSpecification();
        attributeSpecification.setName(name);
        attributeSpecification.setValue(value);
        attributeSpecification.setCmMatchCondition(cmMatchCondition);
        this.attributeSpecificationContainer.addAttributeSpecification(attributeSpecification);
    }

    @Override
    public Object getContainer() {
        return this.attributeSpecificationContainer;
    }

}
