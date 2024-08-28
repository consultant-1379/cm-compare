/*------------------------------------------------------------------------------
 *******************************************************************************
 * COPYRIGHT Ericsson 2012
 *
 * The copyright to the computer program(s) herein is the property of
 * Ericsson Inc. The programs may be used and/or copied only with written
 * permission from Ericsson Inc. or in accordance with the terms and
 * conditions stipulated in the agreement/contract under which the
 * program(s) have been supplied.
 *******************************************************************************
 *----------------------------------------------------------------------------*/
package com.ericsson.oss.services.cm.cmconfig.service.ejb;

import static org.junit.Assert.assertNotNull;

import org.junit.Before;
import org.junit.Test;

/**
 * Unit tests for CmConfigServiceEventHandlerBean.
 */
public class CmConfigServiceEventHandlerBeanTest {

    private CmConfigServiceEventHandlerBean objUnderTest;

    /**
     * create CmConfigServiceEventHandlerBean for tests.
     */
    @Before
    public void canCreateCmConfigServiceEventHandlerBean() {
        this.objUnderTest = new CmConfigServiceEventHandlerBean();
        assertNotNull(this.objUnderTest);
    }

    /**
     * startup Logs Its OwnInitiation.
     */
    @Test
    public void startupLogsItsOwnInitiation() {
        this.objUnderTest.startup();
    }

}
