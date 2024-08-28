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

import static org.junit.Assert.assertTrue;

import javax.ejb.Stateless;

import org.jboss.arquillian.junit.Arquillian;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.ericsson.oss.services.cm.cmshared.dto.CmResponse;

/**
 * Arquillian test for list config.
 */
@RunWith(Arquillian.class)
@Stateless
public class CmConfigListIT extends CmConfigServiceTestBase {

    @Override
    @SuppressWarnings("PMD.SignatureDeclareThrowsException")
    protected void setupTest() throws Exception {
        this.testBuilder.createTestData(configs[0]);
        this.testBuilder.createTestData(configs[1]);
        this.testBuilder.createTestData(configs[2]);
    }

    /**
     * Tests that listConfig lists all existing configurations.
     */
    @Test
    public void listConfig_all_allconfigsListed() {
        this.logger.debug("@Test: listConfig_all_configsListed");
        this.cmConfigService.createConfig(configs[0]);
        this.cmConfigService.createConfig(configs[1]);
        final CmResponse cmResponse = this.cmConfigService.listConfig();
        final CmResponse expectedResponse = new CmResponse();
        expectedResponse.setStatusCode(3);

        assertResponse(expectedResponse, cmResponse);
        assertTrue("Configs in dps # :" + cmResponse.getCmObjects().size(), cmResponse.getCmObjects().size() == configs.length);
    }

}
