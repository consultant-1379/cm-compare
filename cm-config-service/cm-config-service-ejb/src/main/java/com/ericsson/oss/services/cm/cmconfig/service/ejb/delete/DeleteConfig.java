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
package com.ericsson.oss.services.cm.cmconfig.service.ejb.delete;

import javax.ejb.Local;

import com.ericsson.oss.services.cm.cmshared.dto.CmResponse;

/**
 * Delete configuration.
 */
@Local
public interface DeleteConfig {

    /**
     * Deletes a configuration.
     *
     * @param configName
     *            name of the configuration which needs to be deleted
     * @return CmResponse
     */
    CmResponse deleteConfig(final String configName);

}
