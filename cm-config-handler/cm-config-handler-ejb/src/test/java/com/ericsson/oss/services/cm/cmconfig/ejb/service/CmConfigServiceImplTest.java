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
package com.ericsson.oss.services.cm.cmconfig.ejb.service;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import javax.inject.Inject;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import com.ericsson.oss.services.cm.cmconfig.command.CmConfigCommandFactory;
import com.ericsson.oss.services.cm.cmconfig.command.CmConfigCommandObject;
import com.ericsson.oss.services.cm.log.ErrorHandlerImpl;
import com.ericsson.oss.services.scriptengine.spi.dtos.Command;
import com.ericsson.oss.services.scriptengine.spi.dtos.CommandResponseDto;

@RunWith(MockitoJUnitRunner.class)
public class CmConfigServiceImplTest {

    @Mock
    CmConfigCommandFactory commandFactory;

    @Mock
    CmConfigCommandObject cmCommandObject;

    @Mock
    CommandResponseDto commandResponseDto;

    @Mock
    Command command;

    @Mock
    @Inject
    ErrorHandlerImpl errorHandlerImpl;

    @InjectMocks
    // this will do all the magic!! Mockito will inject your mocked session
    // façade into the appropriate field … so no need for a @Before method
    CmConfigurationServiceImpl objUnderTest;

    @Test
    public void executeValidCommandReturnsTheResponseFromTheCmCommand() {
        when(commandFactory.createCommandModel(command.getCommand())).thenReturn(cmCommandObject);
        when(cmCommandObject.execute()).thenReturn(commandResponseDto);
        final CommandResponseDto result = objUnderTest.execute(command);
        assertEquals(commandResponseDto, result);
        // For best testing, verify the method was called (just once)
        verify(commandFactory).createCommandModel(command.getCommand());
    }

    @Test
    public void exceptionDuringExecuteIsCaughtAndResponse() {
        final String exceptionMessage = "exception message";
        final Exception someException = new RuntimeException(exceptionMessage);
        when(commandFactory.createCommandModel(command.getCommand())).thenReturn(cmCommandObject);
        when(cmCommandObject.execute()).thenThrow(someException);
        final CommandResponseDto result = objUnderTest.execute(command);
        // Check status
        assertEquals(CommandResponseDto.UNEXPECTED_ERROR, result.getStatusCode());
        // For best testing, verify the method was called (just once)
        verify(commandFactory).createCommandModel(command.getCommand());
    }
}
