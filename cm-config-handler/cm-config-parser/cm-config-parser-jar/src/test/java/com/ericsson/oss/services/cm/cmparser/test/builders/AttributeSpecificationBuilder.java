package com.ericsson.oss.services.cm.cmparser.test.builders;

import com.ericsson.oss.services.cm.cmshared.dto.AttributeSpecification;
import com.ericsson.oss.services.cm.cmshared.dto.search.CmMatchCondition;

public class AttributeSpecificationBuilder {
    private String name;
    private Object value;
    private CmMatchCondition cmMatchCondition;

    public AttributeSpecificationBuilder() {
        name = "userLabel";
        value = "foo";
        cmMatchCondition = CmMatchCondition.EQUALS;
    }

    public AttributeSpecificationBuilder withName(final String name) {
        this.name = name;
        return this;
    }

    public AttributeSpecificationBuilder withValue(final Object value) {
        this.value = value;
        return this;
    }

    public AttributeSpecificationBuilder withCmMatchCondition(final CmMatchCondition cmMatchCondition) {
        this.cmMatchCondition = cmMatchCondition;
        return this;
    }

    public AttributeSpecification build() {
        final AttributeSpecification attributeSpecification = new AttributeSpecification();
        attributeSpecification.setName(name);
        attributeSpecification.setValue(value);
        attributeSpecification.setCmMatchCondition(cmMatchCondition);
        return attributeSpecification;
    }
}
