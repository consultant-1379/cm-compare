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

import static com.ericsson.oss.services.cm.cmshared.dto.CmConstants.LIVE_CONFIGURATION;
import static com.ericsson.oss.services.cm.log.ErrorHandler.EXECUTION_ERROR;
import static com.ericsson.oss.services.cm.log.ErrorHandler.SUCCESS;

import org.jboss.arquillian.junit.Arquillian;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.ericsson.oss.services.cm.cmshared.dto.CmResponse;

/**
 * Arquillian tests for Create and Delete config.
 */
@Ignore
@RunWith(Arquillian.class)
public class CmConfigServiceCreateDeleteIT extends CmConfigServiceTestBase {

    @SuppressWarnings("PMD.SignatureDeclareThrowsException")
    @Override
    protected void setupTest() throws Exception {
        this.logger.info("No setup required for this tests");
    }

    /**
     * <li>Unit of Work: create valid config.
     * <li>State Under Test: valid config name supplied
     * <li>Expected Behavior: Status code > 0, message correct
     */
    @Test
    public void createConfig_configName_configCreated() {
        final String configName = "myConfig";
        final CmResponse response = this.cmConfigService.createConfig(configName);
        final CmResponse expectedResponse = new CmResponse();
        expectedResponse.setStatusCode(SUCCESS);
        expectedResponse.setStatusMessage("config successfully created");

        assertResponse(expectedResponse, response);
    }

    /**
     * <li>Unit of Work: create Live config.
     * <li>State Under Test: config named "Live" supplied
     * <li>Expected Behavior: Status code -1, message correct
     */
    @Test
    public void testCreateLive() {
        this.cmConfigService.createConfig(LIVE_CONFIGURATION);
        final CmResponse response = this.cmConfigService.createConfig(LIVE_CONFIGURATION);
        final CmResponse expectedResponse = new CmResponse();
        expectedResponse.setStatusCode(EXECUTION_ERROR);
        expectedResponse.setStatusMessage("already exists");

        assertResponse(expectedResponse, response);
    }

    /**
     * <li>Unit of Work: create "myConfigDuplicate" config twice.
     * <li>State Under Test: config has already been created
     * <li>Expected Behavior: Status code -1, message correct
     */
    @Test
    public void createConfig_duplicateConfigName_configNotCreated() {
        final String configName = "myConfigDuplicate";
        this.cmConfigService.createConfig(configName);
        final CmResponse response = this.cmConfigService.createConfig(configName);

        final CmResponse expectedResponse = new CmResponse();
        expectedResponse.setStatusCode(EXECUTION_ERROR);
        expectedResponse.setStatusMessage("already exists");

        assertResponse(expectedResponse, response);
    }

    /**
     * <li>Unit of Work: delete config that doesn't exist.
     * <li>State Under Test: config named "config-doesnt-exist" supplied
     * <li>Expected Behavior: Status code -1, message correct
     */
    @Test
    public void testDeleteNonexistingConfig() {
        final String configName = "config-doesnt-exist";
        final CmResponse response = this.cmConfigService.deleteConfig(configName);

        final CmResponse expectedResponse = new CmResponse();
        expectedResponse.setStatusCode(EXECUTION_ERROR);
        expectedResponse.setStatusMessage("Configuration does not exist");

        assertResponse(expectedResponse, response);
    }

    /**
     * <li>Unit of Work: delete Live config.
     * <li>State Under Test: config named "Live" supplied
     * <li>Expected Behavior: Status code -1, message correct
     */
    @Test
    public void testDeleteLiveConfig() {
        final CmResponse response = this.cmConfigService.deleteConfig(LIVE_CONFIGURATION);

        final CmResponse expectedResponse = new CmResponse();
        expectedResponse.setStatusCode(EXECUTION_ERROR);
        expectedResponse.setStatusMessage("Cannot DELETE Live configuration");

        assertResponse(expectedResponse, response);
    }
}
