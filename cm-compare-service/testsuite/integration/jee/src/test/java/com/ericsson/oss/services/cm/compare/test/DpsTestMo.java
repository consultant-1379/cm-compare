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
package com.ericsson.oss.services.cm.compare.test;

import java.util.Map;

public abstract class DpsTestMo {

    protected static final String TOP_NAMESPACE = "OSS_TOP";
    protected static final String TOP_NAMESPACE_VERSION = "1.0.1";
    protected static final String ERBS_NAMESPACE = "ERBS_NODE_MODEL";
    protected static final String ERBS_NAMESPACE_VERSION = "3.1.72";
    protected static final String CPP_NAMESPACE = "CPP_NODE_MODEL";
    protected static final String CPP_NAMESPACE_VERSION = "3.12.0";

    public String getNamespace() {
        return namespace;
    }

    public void setNamespace(final String namespace) {
        this.namespace = namespace;
    }

    public String getType() {
        return type;
    }

    public void setType(final String type) {
        this.type = type;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(final String version) {
        this.version = version;
    }

    public String getId() {
        return id;
    }

    public void setId(final String id) {
        this.id = id;
    }

    public Map<String, Object> getAttributes() {
        return attributes;
    }

    public void setAttributes(final Map<String, Object> attributes) {
        this.attributes = attributes;
    }

    public DpsTestMo(final String id, final String nodeName) {
        this.id = id;
        this.nodeName = nodeName;
    }

    public String getNodeName() {
        return nodeName;
    }

    public void setNodeName(final String nodeName) {
        this.nodeName = nodeName;
    }

    private String namespace;
    private String type;
    private String version;
    private String id;
    private String nodeName;
    private Map<String, Object> attributes;
}
