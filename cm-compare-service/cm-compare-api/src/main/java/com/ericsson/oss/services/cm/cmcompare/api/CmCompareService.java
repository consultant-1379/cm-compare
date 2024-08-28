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
package com.ericsson.oss.services.cm.cmcompare.api;

import javax.ejb.Remote;

import com.ericsson.oss.itpf.sdk.core.annotation.EService;
import com.ericsson.oss.services.cm.cmshared.dto.search.CmSearchCriteria;
import com.ericsson.oss.services.cm.cmshared.dto.CmResponse;

/**
 * Defines the operations on CM Configurations.
 */
@EService
@Remote
public interface CmCompareService {

    /**
     * Create an empty configuration.
     * 
     * @param configName
     *          name of the configuration.
     * @return CmResponse
     *          On success, returnCmResponse with status code &gt;= 0 will be returned
     *          If config already exists, it returns returnCmResponse with status code &lt;0 .
     */
    CmResponse createConfig(final String configName);

    /**
     * create a configuration based on the scope defined in {@link CmSearchCriteria}.
     * 
     * @param configName
     *          name of the configuration.
     * @param scope
     *          configuration scope.
     * @return CmResponse
     */
    CmResponse createConfig(final String configName, final CmSearchCriteria scope);

    /**
     * list all configurations.
     * 
     * @return CmResponse
     */
    CmResponse listConfig();

    /**
     * Update the config from another config.
     * 
     * @param toConfigName
     *          name of the destination configuration.
     * @param fromConfigName
     *          name of the source configuration.        
     * @param scope
     *          configuration scope.
     * @param params
     *          update params.
     * @return CmResponse
     */
    CmResponse updateConfig(final String toConfigName, final String fromConfigName, final CmSearchCriteria scope, final CmConfigUpdateParams params);

    /**
     * Diff of a config with another config.
     * 
     * @param toConfigName
     *          name of the diff from configuration.
     * @param fromConfigName
     *          name of the diff to configuration.
     * @param scope
     *          configuration scope.
     * @param params
     *          diff params.
     * @return CmResponse
     */
    CmResponse diffConfig(final String toConfigName, final String fromConfigName, final CmSearchCriteria scope, final CmConfigDiffParams params);

    /**
     * Link config.
     * 
     * @param overlayConfigName
     *          name of the overlay configuration.
     * @param baseConfigName
     *          name of the base configuration.
     * @return CmResponse
     */
    CmResponse linkConfig(final String overlayConfigName, final String baseConfigName);

    /**
     * Unlink config.
     * 
     * @param overlayConfigName
     *          name of the overlay configuration.
     * @return CmResponse
     */
    CmResponse unlinkConfig(final String overlayConfigName);

    /**
     * Lock a config.
     * 
     * @param configName
     *          name of the configuration to be locked.
     * @param reasonString
     *          reason for locking the configuration.        
     * @return CmResponse
     */
    CmResponse lockConfig(final String configName, final String reasonString);

    /**
     * Unlock a config.
     * 
     * @param configName
     *          name of the configuration to be unlocked.
     * @return CmResponse
     */
    CmResponse unlockConfig(final String configName);

    /**
     * Delete a config.
     * 
     * @param configName
     *          name of the configuration which needs to be deleted.
     * @return CmResponse
     */
    CmResponse deleteConfig(final String configName);

}
