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

import static com.ericsson.oss.services.cm.log.ErrorHandler.EXECUTION_ERROR;

import java.util.Arrays;

import javax.enterprise.inject.Default;
import javax.inject.Inject;

import com.ericsson.oss.itpf.sdk.recording.CommandPhase;
import com.ericsson.oss.itpf.sdk.recording.ErrorSeverity;
import com.ericsson.oss.itpf.sdk.recording.SystemRecorder;
import com.ericsson.oss.services.cm.cmshared.dto.AbstractCmResponse;

/**
 * {@code CmConfigHandlerLog} implementation for command recording.
 */
@Default
public class CmConfigServiceLogBean implements CmConfigServiceLog {

    /**
     * cm-config-service component.
     */
    private static final String COMPONENT_NAME = "cm-config-service component";
    private static final String EXCEPTION_MESSAGE = "Exception thrown in %s : %s";

    @Inject
    private SystemRecorder systemRecorder;

    @Override
    public void logCmCommand(final String commandString, final AbstractCmResponse cmResponse, final Object... commandParameters) {

        this.systemRecorder.recordCommand(generateCommandLog(commandString, commandParameters), getCommandPhase(cmResponse.getStatusCode()),
                COMPONENT_NAME, cmResponse.getStatusMessage(), cmResponse.getStatusMessage());
    }

    @Override
    public void logErrorMessage(final int errorCode, final String errorMessage, final String resource) {
        this.systemRecorder.recordError("error code: " + errorCode, ErrorSeverity.WARNING, COMPONENT_NAME, resource, errorMessage);
    }

    @Override
    public String logAndCreateErrorMessageFromException(final Throwable throwable) {
        this.systemRecorder.recordError("error code: " + EXECUTION_ERROR, ErrorSeverity.WARNING, COMPONENT_NAME, "", throwable.getMessage());
        return String.format(EXCEPTION_MESSAGE, COMPONENT_NAME, throwable.getMessage());
    }

    /**
     * Generates a string based on the command and its passed parameters to be used for logging purposes.
     *
     * @param commandString
     *            the command to be logged
     * @param commandParameters
     *            the parameters passed to that command
     * @return a string in the format of <code>commandString([commandParameter1,commandParameter2,...])</code>
     */
    protected String generateCommandLog(final String commandString, final Object... commandParameters) {
        final StringBuilder commandLog = new StringBuilder();
        commandLog.append(commandString);
        commandLog.append("(");
        commandLog.append(Arrays.toString(commandParameters));
        commandLog.append(")");

        return commandLog.toString();
    }

    private CommandPhase getCommandPhase(final int statusCode) {
        if (statusCode < 0) {
            return CommandPhase.FINISHED_WITH_ERROR;
        }
        return CommandPhase.FINISHED_WITH_SUCCESS;
    }

}
