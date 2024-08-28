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
package com.ericsson.oss.services.cm.cmconfig.service.api;

import java.util.Properties;

import javax.ejb.Remote;

import com.ericsson.oss.itpf.sdk.core.annotation.EService;
import com.ericsson.oss.services.cm.cmshared.dto.CmResponse;
import com.ericsson.oss.services.cm.cmshared.dto.search.CmSearchCriteria;

/**
 * Service for CM Configuration operations.
 */
@EService
@Remote
public interface CmConfigService {

    /**
     * Creates an empty configuration.
     * 
     * @param configName
     *            name of the configuration.
     * @return CmResponse On success a CmResponse with status code &gt;= 0 will
     *         be returned, and on failure the status code will be &lt;0 .
     */
    CmResponse createConfig(final String configName);

    /**
     * Lists all configurations.
     * 
     * @return CmResponse
     */
    CmResponse listConfig();

    /**
     * Diffs a configuration with another configuration.
     * 
     * @param targetConfigName
     *            name of the diff <em>target</em> configuration
     * @param referenceConfigName
     *            name of the diff <em>reference</em> configuration
     * @param scope
     *            configuration scope
     * @param params
     *            diff parameters
     * @return CmDiffResponse
     */
    CmDiffResponse diffConfig(final String targetConfigName, final String referenceConfigName, final CmSearchCriteria scope,
            final CmConfigDiffParams params);

    /**
     * Deletes a configuration.
     * 
     * @param configName
     *            name of the configuration which needs to be deleted
     * @return CmResponse
     */
    CmResponse deleteConfig(final String configName);

    /**
     * Copies specified nodes from a source to target configuration.
     * 
     * @param sourceConfigName
     *            name of the source configuration
     * @param targetConfigName
     *            name of the target configuration
     * @param nodesScope
     *            node scope; the search criteria which specifies the nodes to
     *            copy.
     * @param copyFlags
     *            flags which specify the behavior of the copy.
     * @return CmResponse
     */
    CmCopyResponse copyNodes(String sourceConfigName, String targetConfigName, CmSearchCriteria nodesScope, CmCopyNodesFlags copyFlags);

    Long startExportJob(String jobName, Properties properties);

    ExportStatus exportJobStatus(Long exportJobID);

    ExportStatus processExportTillComplete(Long exportJobID);

}
