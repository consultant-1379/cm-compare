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
package com.ericsson.oss.services.cm.writer.test;

import java.util.HashMap;
import java.util.Map;

public class ManagedElement extends DpsTestMo {
    protected static final String MANDATORY_ATTRIBUTE_MANAGEDELEMENTID = "ManagedElementId";

    ManagedElement(final String id, final String nodeName) {
        super(id, nodeName);
        setType("ManagedElement");
        setNamespace(CPP_NAMESPACE);
        setVersion(CPP_NAMESPACE_VERSION);
        final Map<String, Object> attributes = new HashMap<String, Object>();
        attributes.put(MANDATORY_ATTRIBUTE_MANAGEDELEMENTID, id);
        setAttributes(attributes);
    }
}
