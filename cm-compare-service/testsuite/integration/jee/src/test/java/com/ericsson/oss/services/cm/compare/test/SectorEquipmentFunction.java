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

public class SectorEquipmentFunction extends DpsTestMo {
    protected static final String MANDATORY_ATTRIBUTE_SECTOREQUIPMENTFUNCTIONID = "SectorEquipmentFunctionId";

    SectorEquipmentFunction(final String id, final String nodeName) {
        super(id, nodeName);
        setType("SectorEquipmentFunction");
        setNamespace(ERBS_NAMESPACE);
        setVersion(ERBS_NAMESPACE_VERSION);
        final Map<String, Object> attributes = new HashMap<String, Object>();
        attributes.put(MANDATORY_ATTRIBUTE_SECTOREQUIPMENTFUNCTIONID, id);
        setAttributes(attributes);
    }
}
