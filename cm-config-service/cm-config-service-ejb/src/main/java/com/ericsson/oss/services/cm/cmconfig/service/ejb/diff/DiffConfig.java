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
package com.ericsson.oss.services.cm.cmconfig.service.ejb.diff;

import javax.ejb.Local;

import com.ericsson.oss.services.cm.cmconfig.service.api.CmConfigDiffParams;
import com.ericsson.oss.services.cm.cmconfig.service.api.CmDiffResponse;
import com.ericsson.oss.services.cm.cmshared.dto.search.CmSearchCriteria;

/**
 * Diff of two given configurations.
 */
@Local
public interface DiffConfig {
    /**
     * Diff of a configuration with another configuration.
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
}
