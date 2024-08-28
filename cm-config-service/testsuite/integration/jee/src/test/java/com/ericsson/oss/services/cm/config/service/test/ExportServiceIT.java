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

package com.ericsson.oss.services.cm.config.service.test;

import static org.junit.Assert.assertEquals;

import java.util.Properties;

import javax.ejb.Stateless;

import org.jboss.arquillian.junit.Arquillian;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.ericsson.oss.services.cm.cmconfig.service.api.ExportStatus;

/**
 * Arquillian test for list config.
 */
@RunWith(Arquillian.class)
@Stateless
public class ExportServiceIT extends CmConfigServiceTestBase {

    @Override
    @SuppressWarnings("PMD.SignatureDeclareThrowsException")
    protected void setupTest() throws Exception {
        return;
    }

    /**
     * Tests that listConfig lists all existing configurations.
     * 
     * @throws InterruptedException
     */
    @Test
    public void export_all_allNodesExported() throws InterruptedException {
        this.logger.debug("@Test: export_all_allNodesExported");
        final Long exportId = this.cmConfigService.startExportJob("myJob", new Properties());
        System.out.println("EXPORT JOB STARTED WITH ID " + exportId);

        final ExportStatus exportStatus = this.cmConfigService.exportJobStatus(exportId);
        BatchTestHelper.keepTestAlive(exportStatus);

        // <1> Job should be completed.
        assertEquals(ExportStatus.COMPLETED, exportStatus);

    }

}
