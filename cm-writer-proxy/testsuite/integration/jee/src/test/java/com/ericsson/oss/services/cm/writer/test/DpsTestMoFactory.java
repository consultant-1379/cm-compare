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

public class DpsTestMoFactory {
    public static DpsTestMo createTestMO(final DpsTestMoType testMOType, final String id, final String nodeName) {
        DpsTestMo testMO = null;
        switch (testMOType) {
            case MECONTEXT:
                testMO = new MeContext(id, nodeName);
                break;
            case MANAGED_ELEMENT:
                testMO = new ManagedElement(id, nodeName);
                break;
            case ENODEB_FUNCTION:
                testMO = new ENodeBFunction(id, nodeName);
                break;
            case SECTOR_EQUIPMENT_FUNCTION:
                testMO = new SectorEquipmentFunction(id, nodeName);
                break;
            case EUTRAN_CELL_FDD:
                testMO = new EUtranCellFdd(id, nodeName);
                break;
            default:
                break;
        }
        return testMO;
    }
}
