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

public class EUtranCellFdd extends DpsTestMo {
    protected static final String MANDATORY_ATTRIBUTE_EUTRANCELLFDDID = "EUtranCellFDDId";
    protected static final String MANDATORY_ATTRIBUTE_CELL_ID = "cellId";
    protected static final long MANDATORY_ATTRIBUTE_CELL_ID_VALUE = 123;
    protected static final String MANDATORY_ATTRIBUTE_EARFCNDL = "earfcndl";
    protected static final long MANDATORY_ATTRIBUTE_EARFCNDL_VALUE = 17000;
    protected static final String MANDATORY_ATTRIBUTE_EARFCNUL = "earfcnul";
    protected static final long MANDATORY_ATTRIBUTE_EARFCNUL_VALUE = 18000;
    protected static final String MANDATORY_ATTRIBUTE_PHYSICAL_LAYER_CELL_IDGROUP = "physicalLayerCellIdGroup";
    protected static final long MANDATORY_ATTRIBUTE_VALUE = 153;
    protected static final String MANDATORY_ATTRIBUTE_PHYSICAL_LAYER_SUBCELL_ID = "physicalLayerSubCellId";
    protected static final long MANDATORY_ATTRIBUTE__PHYSICAL_LAYER_SUBCELL_ID_VALUE = 0;
    protected static final String MANDATORY_ATTRIBUTE_TAC = "tac";
    protected static final long MANDATORY_ATTRIBUTE_TAC_VALUE = 13634;
    protected static final String MANDATORY_ATTRIBUTE_SECTOR_FUNCTION_REF = "sectorFunctionRef";
    protected static final String SECTOR_FUNCTION_MO_RDN = "ManagedElement=1,SectorEquipmentFunction=1";

    EUtranCellFdd(final String id, final String nodeName) {
        super(id, nodeName);
        setType("EUtranCellFDD");
        setNamespace(ERBS_NAMESPACE);
        setVersion(ERBS_NAMESPACE_VERSION);
        populateAttributes(id, nodeName);
    }

    private void populateAttributes(final String id, final String nodeName) {
        final Map<String, Object> attributes = new HashMap<String, Object>();
        attributes.put(MANDATORY_ATTRIBUTE_EUTRANCELLFDDID, id);
        attributes.put(MANDATORY_ATTRIBUTE_CELL_ID, MANDATORY_ATTRIBUTE_CELL_ID_VALUE);
        attributes.put(MANDATORY_ATTRIBUTE_EARFCNDL, MANDATORY_ATTRIBUTE_EARFCNDL_VALUE);
        attributes.put(MANDATORY_ATTRIBUTE_EARFCNUL, MANDATORY_ATTRIBUTE_EARFCNUL_VALUE);
        attributes.put(MANDATORY_ATTRIBUTE_PHYSICAL_LAYER_CELL_IDGROUP, MANDATORY_ATTRIBUTE_VALUE);
        attributes.put(MANDATORY_ATTRIBUTE_PHYSICAL_LAYER_SUBCELL_ID, MANDATORY_ATTRIBUTE__PHYSICAL_LAYER_SUBCELL_ID_VALUE);
        attributes.put(MANDATORY_ATTRIBUTE_TAC, MANDATORY_ATTRIBUTE_TAC_VALUE);
        attributes.put(MANDATORY_ATTRIBUTE_SECTOR_FUNCTION_REF, "MeContext=" + nodeName + "," + SECTOR_FUNCTION_MO_RDN);
        setAttributes(attributes);
    }
}
