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
package com.ericsson.oss.services.cm.cmconfig.service.exception.handling;

import com.ericsson.oss.services.cm.cmshared.dto.AbstractCmResponse;

/**
 * Exception handling in the cm-config-service.
 */
public interface CmConfigServiceExceptionHandler {

    /**
     * Handle all exceptions other than {@link ValidationException}.
     *
     * @param cmResponse
     *            cmResponse
     * @param t
     *            The {@code Throwable} class
     * @param suggestedSolution
     *            suggested solution
     * @param statusCode
     *            status code.
     */
    void handleError(final AbstractCmResponse cmResponse, final Throwable t, final String suggestedSolution, final int statusCode);

    /**
     * Handle {@link ValidationException}.
     *
     * @param cmResponse
     *            cmResponse
     * @param statusMessage
     *            status message
     * @param suggestedSolution
     *            suggested solution
     * @param exceptionOriginatedClass
     *            class where this exception occurred.
     * @param statusCode
     *            status code.
     */
    void handleValidationException(final AbstractCmResponse cmResponse, final String statusMessage, final String suggestedSolution,
            final String exceptionOriginatedClass, final int statusCode);
}
