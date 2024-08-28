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

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Matchers;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import com.ericsson.oss.itpf.sdk.recording.CommandPhase;
import com.ericsson.oss.itpf.sdk.recording.ErrorSeverity;
import com.ericsson.oss.itpf.sdk.recording.SystemRecorder;
import com.ericsson.oss.services.cm.cmshared.dto.AbstractCmResponse;

/**
 * unit test for {@link CmConfigServiceLogBean}.
 */
@SuppressWarnings("PMD.AvoidThrowingRawExceptionTypes")
@RunWith(MockitoJUnitRunner.class)
public class CmConfigServiceLogBeanTest {

    private static final String commandString = "createConfig";
    private static final String COMPONENT_NAME = "cm-config-service component";

    @Mock
    private SystemRecorder systemRecorder;

    @Mock
    private AbstractCmResponse cmResponse;

    @InjectMocks
    private CmConfigServiceLogBean objUnderTest;

    /**
     * logCmCommandTestWithStatusSuccess.
     */
    @Test
    public void logCmCommandTestWithStatusSuccess() {

        when(this.cmResponse.getStatusCode()).thenReturn(0);
        when(this.cmResponse.getStatusMessage()).thenReturn("status");

        this.objUnderTest.logCmCommand(commandString, this.cmResponse);

        final String expectedMessage = commandString + "([])";

        verify(this.systemRecorder).recordCommand(expectedMessage, CommandPhase.FINISHED_WITH_SUCCESS, COMPONENT_NAME, "status", "status");
    }

    @Test
    public void logCmCommandTestWithNulls() {

        when(this.cmResponse.getStatusCode()).thenReturn(0);
        when(this.cmResponse.getStatusMessage()).thenReturn("status");

        this.objUnderTest.logCmCommand(commandString, this.cmResponse, null, null);

        final String expectedMessage = commandString + "([null, null])";

        verify(this.systemRecorder).recordCommand(expectedMessage, CommandPhase.FINISHED_WITH_SUCCESS, COMPONENT_NAME, "status", "status");
    }

    /**
     * logCmCommandTestWithStatusError.
     */
    @Test
    public void logCmCommandTestWithStatusError() {

        when(this.cmResponse.getStatusCode()).thenReturn(-1);
        when(this.cmResponse.getStatusMessage()).thenReturn("status");

        this.objUnderTest.logCmCommand(commandString, this.cmResponse);

        final String expectedMessage = commandString + "([])";

        verify(this.systemRecorder).recordCommand(expectedMessage, CommandPhase.FINISHED_WITH_ERROR, COMPONENT_NAME, "status", "status");
    }

    /**
     * createErrorMessageFromApplicationError.
     */
    @Test
    public void createErrorMessageFromApplicationError() {

        this.objUnderTest.logErrorMessage(0, "errorMessage", "resource");

        verify(this.systemRecorder).recordError(Matchers.anyString(), Matchers.any(ErrorSeverity.class), Matchers.anyString(), Matchers.anyString(),
                Matchers.anyString());
    }

    /**
     * createErrorMessageFromApplicationException.
     */
    @SuppressWarnings("PMD.AvoidThrowingRawExceptionTypes")
    @Test
    public void createErrorMessageFromApplicationException() {

        final String resultMessage = this.objUnderTest.logAndCreateErrorMessageFromException(new Exception("exception message"));
        Assert.assertTrue("The message must contain the exception message", resultMessage.contains("exception message"));

        verify(this.systemRecorder).recordError(Matchers.anyString(), Matchers.any(ErrorSeverity.class), Matchers.anyString(), Matchers.anyString(),
                Matchers.anyString());
    }
}
