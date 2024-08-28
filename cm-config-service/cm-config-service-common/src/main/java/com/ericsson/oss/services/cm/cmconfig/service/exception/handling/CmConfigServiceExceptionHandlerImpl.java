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

import javax.inject.Inject;

import com.ericsson.oss.services.cm.cmconfig.service.log.CmConfigServiceLog;
import com.ericsson.oss.services.cm.cmshared.dto.AbstractCmResponse;

/**
 * Default implementation of {@link CmConfigServiceExceptionHandler}.
 */
public class CmConfigServiceExceptionHandlerImpl implements CmConfigServiceExceptionHandler {

    @Inject
    private CmConfigServiceLog logger;

    @Override
    public void handleError(final AbstractCmResponse cmResponse, final Throwable t, final String suggestedSolution, final int statusCode) {
        final String exceptionMessage = this.logger.logAndCreateErrorMessageFromException(t);
        cmResponse.setStatusCode(statusCode);
        cmResponse.setStatusMessage(exceptionMessage);
        cmResponse.setSolution(suggestedSolution);
    }

    @Override
    public void handleValidationException(final AbstractCmResponse cmResponse, final String statusMessage, final String suggestedSolution,
            final String exceptionOriginatedClass, final int statusCode) {
        this.logger.logErrorMessage(statusCode, statusMessage, exceptionOriginatedClass);
        cmResponse.setStatusCode(statusCode);
        cmResponse.setStatusMessage(statusMessage);
        cmResponse.setSolution(suggestedSolution);
    }
}
