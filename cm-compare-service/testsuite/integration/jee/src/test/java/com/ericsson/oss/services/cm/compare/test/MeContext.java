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

import java.util.HashMap;
import java.util.Map;

class MeContext extends DpsTestMo {
    protected static final String MANDATORY_ATTRIBUTE_MECONTEXTID = "MeContextId";
    protected static final String MANDATORY_ATTRIBUTE_NETYPE = "neType";
    protected static final String MANDATORY_ATTRIBUTE_VALUE_NETYPE = "ENODEB";

    MeContext(final String id, final String nodeName) {
        super(id, nodeName);
        setType("MeContext");
        setNamespace(TOP_NAMESPACE);
        setVersion(TOP_NAMESPACE_VERSION);
        final Map<String, Object> attributes = new HashMap<String, Object>();
        attributes.put(MANDATORY_ATTRIBUTE_MECONTEXTID, id);
        attributes.put(MANDATORY_ATTRIBUTE_NETYPE, MANDATORY_ATTRIBUTE_VALUE_NETYPE);
        setAttributes(attributes);
    }
}
