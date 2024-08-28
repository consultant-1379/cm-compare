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
package com.ericsson.oss.services.cm.cmconfig.command;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import com.ericsson.oss.services.cm.cmconfig.command.CmConfigCommandObject;
import com.ericsson.oss.services.cm.cmconfig.command.handlers.CommandHandler;
import com.ericsson.oss.services.cm.cmparser.dto.ParserResult;
import com.ericsson.oss.services.scriptengine.spi.dtos.CommandResponseDto;

@RunWith(MockitoJUnitRunner.class)
public class CmConfigCommandObjectTest {

    @Mock
    ParserResult parserResult;

    @Mock
    CommandHandler commandHandler;

    @Mock
    CommandResponseDto commandResponseDto;

    CmConfigCommandObject objUnderTest;

    @Before
    public void canCreateObjUnderTest() {
        objUnderTest = new CmConfigCommandObject();
        assertNotNull(objUnderTest);
    }

    @Test
    public void canSetAndgetCommand() {
        objUnderTest.setParserResult(parserResult);
        assertEquals(parserResult, objUnderTest.getParserResult());
    }

    @Test
    public void canSetAndgetCommandHandler() {
        objUnderTest.setCommandHandler(commandHandler);
        assertEquals(commandHandler, objUnderTest.getCommandHandler());
    }

    @Test
    public void executeForwardsToCommandHandlerWithCorrectData() {
        // Set-up the mock to return the desired type of parserResult
        objUnderTest.setParserResult(parserResult);
        objUnderTest.setCommandHandler(commandHandler);
        when(commandHandler.processCommand(parserResult)).thenReturn(commandResponseDto);
        // Execute the test
        assertEquals(commandResponseDto, objUnderTest.execute());
        // Verify that the correct method was called with the correct parameters
        verify(commandHandler).processCommand(parserResult);
    }

}
