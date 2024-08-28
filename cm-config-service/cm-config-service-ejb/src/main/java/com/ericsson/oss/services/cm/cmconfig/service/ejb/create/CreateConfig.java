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
package com.ericsson.oss.services.cm.cmconfig.service.ejb.create;

import javax.ejb.Local;

import com.ericsson.oss.services.cm.cmshared.dto.CmResponse;

/**
 * Create configuration.
 */
@Local
public interface CreateConfig {

    /**
     * Creates an empty configuration.
     *
     * @param configName
     *            name of the configuration.
     * @return CmResponse On success a CmResponse with status code &gt;= 0 will
     *         be returned, and on failure the status code will be &lt;0 .
     */
    CmResponse createConfig(final String configName);

}
