package com.ericsson.oss.services.cm.cmparser.test.builders;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.ericsson.oss.services.cm.cmshared.dto.AttributeSpecificationContainer;
import com.ericsson.oss.services.cm.cmshared.dto.CmObjectSpecification;

public class CmObjectSpecificationBuilder {

    private String name;
    private String type;

    private AttributeSpecificationContainer attributeSpecificationContainer;
    private String namespace;
    private String namespaceVersion;
    private List<CmObjectSpecification> childCmObjectSpecifications;

    public CmObjectSpecificationBuilder() {
        childCmObjectSpecifications = new ArrayList<>();
        type = "ENodeBFunction";
    }

    public CmObjectSpecificationBuilder withType(final String type) {
        this.type = type;
        return this;
    }

    public CmObjectSpecificationBuilder withName(final String name) {
        this.name = name;
        return this;
    }

    public CmObjectSpecificationBuilder withNamespace(final String namespace) {
        this.namespace = namespace;
        return this;
    }

    public CmObjectSpecificationBuilder withNamespaceVersion(final String namespaceVersion) {
        this.namespaceVersion = namespaceVersion;
        return this;
    }

    public CmObjectSpecificationBuilder withChildCmObjectSpecifications(final CmObjectSpecification... childCmObjectSpecifications) {
        this.childCmObjectSpecifications = Arrays.asList(childCmObjectSpecifications);
        return this;
    }

    public CmObjectSpecificationBuilder withChildCmObjectSpecifications(final List<CmObjectSpecification> childCmObjectSpecifications) {
        this.childCmObjectSpecifications = childCmObjectSpecifications;
        return this;
    }

    public CmObjectSpecificationBuilder withAttributeSpecificationContainer(final AttributeSpecificationContainer attributeSpecifications) {
        this.attributeSpecificationContainer = attributeSpecifications;
        return this;
    }

    public CmObjectSpecification build() {
        final CmObjectSpecification cmObjectSpecification = new CmObjectSpecification();
        cmObjectSpecification.setType(type);
        cmObjectSpecification.setAttributeSpecificationContainer(attributeSpecificationContainer);
        cmObjectSpecification.setName(name);
        cmObjectSpecification.setNamespace(namespace);
        cmObjectSpecification.setNamespaceVersion(namespaceVersion);
        cmObjectSpecification.setChildCmObjectSpecifications(childCmObjectSpecifications);
        return cmObjectSpecification;
    }
}
