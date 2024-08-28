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
public class ExportServiceJob2IT extends CmConfigServiceTestBase {

    @Override
    @SuppressWarnings("PMD.SignatureDeclareThrowsException")
    protected void setupTest() throws Exception {
        return;
    }

    /**
     * In the test, we're just going to invoke the batch execution and wait for completion. To validate the test
     * expected behaviour we need to query the +javax.batch.runtime.Metric+ object available in the step execution.
     * The batch process itself will read and process 10 elements from numbers 1 to 10, but only write the odd
     * elements. Commits are executed after 3 elements are read.
     * 
     * @throws Exception
     *             an exception if the batch could not complete successfully.
     */
    @Test
    public void export_all_allNodesExported() throws InterruptedException {
        final Long exportId = this.cmConfigService.startExportJob("myJob2", new Properties());
        System.out.println("EXPORT JOB2 STARTED WITH ID " + exportId);

        final ExportStatus exportStatus = this.cmConfigService.processExportTillComplete(exportId);
        assertEquals(ExportStatus.COMPLETED, exportStatus);
    }

}
