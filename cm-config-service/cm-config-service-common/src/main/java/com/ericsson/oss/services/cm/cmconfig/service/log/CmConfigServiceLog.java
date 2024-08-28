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
package com.ericsson.oss.services.cm.cmconfig.service.log;

import com.ericsson.oss.services.cm.cmshared.dto.AbstractCmResponse;

/**
 * Command logging.
 */
public interface CmConfigServiceLog {
    /**
     * Records the command and its status.
     *
     * @param commandString
     *            string representation of the command executed
     * @param cmResponse
     *            status result of the command
     * @param commandParameters
     *            parameters to the command executed
     */
    void logCmCommand(String commandString, AbstractCmResponse cmResponse, Object... commandParameters);

    /**
     * Records an error.
     *
     * @param errorCode
     *            error code
     * @param errorMessage
     *            error message to record
     * @param resource
     *            resource to record
     */
    void logErrorMessage(final int errorCode, final String errorMessage, final String resource);

    /**
     * Records an error from an exception.
     *
     * @param throwable
     *            throwable with the information to record
     * @return the error message string
     */
    String logAndCreateErrorMessageFromException(Throwable throwable);
}
