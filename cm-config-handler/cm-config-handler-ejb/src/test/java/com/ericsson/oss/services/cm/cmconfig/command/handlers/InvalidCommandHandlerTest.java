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
package com.ericsson.oss.services.cm.cmconfig.command.handlers;

import static org.junit.Assert.assertEquals;

import javax.inject.Inject;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import com.ericsson.oss.services.cm.cmconfig.command.handlers.InvalidCommandHandler;
import com.ericsson.oss.services.cm.cmparser.dto.ParserResult;
import com.ericsson.oss.services.cm.cmparser.dto.ParserResult.CommandType;
import com.ericsson.oss.services.cm.cmshared.dto.CmResponse;
import com.ericsson.oss.services.cm.log.ErrorHandlerImpl;

@RunWith(MockitoJUnitRunner.class)
public class InvalidCommandHandlerTest {

    @Mock
    @Inject
    ErrorHandlerImpl errorHandler;

    @InjectMocks
    InvalidCommandHandler objUnderTest = new InvalidCommandHandler();

    @Test
    public void getCmResponseReturnsCmResposneWithStatusDetailsfromTheParserError() {
        final String message = "some message";
        final ParserResult parserError = new ParserResult();
        parserError.setCommandType(CommandType.PARSER_ERROR);
        final int statusCode = -9;
        parserError.setStatusCode(statusCode);
        parserError.setStatusMessage(message);
        final CmResponse cmResponse = objUnderTest.getCmResponse(parserError);
        final int agreedParserErrorCode = statusCode; // Agreed code for parser all errors
        assertEquals(agreedParserErrorCode, cmResponse.getStatusCode());
        assertEquals(message, cmResponse.getStatusMessage());
    }

}
