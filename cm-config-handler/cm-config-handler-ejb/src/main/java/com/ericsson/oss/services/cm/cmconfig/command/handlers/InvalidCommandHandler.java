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

import javax.inject.Inject;

import com.ericsson.oss.services.cm.cmconfig.command.handlers.qualifiers.InvalidCommand;
import com.ericsson.oss.services.cm.cmparser.dto.ParserResult;
import com.ericsson.oss.services.cm.cmshared.dto.CmResponse;
import com.ericsson.oss.services.cm.log.ErrorHandlerImpl;

/**
 * 
 * Invalid command handler.
 * 
 */
@InvalidCommand
public class InvalidCommandHandler extends CommandHandler {

    @Inject
    ErrorHandlerImpl errorHandler;

    @Override
    public CmResponse getCmResponse(final ParserResult parserResult) {
        return handleParserError(parserResult);
    }

    private CmResponse handleParserError(final ParserResult parserResult) {
        final CmResponse cmResponse = new CmResponse();
        cmResponse.setStatusCode(parserResult.getStatusCode());
        cmResponse.setStatusMessage(parserResult.getStatusMessage());
        this.errorHandler.createErrorMessage(ErrorHandlerImpl.COMMAND_SYNTAX_ERROR_CODE, parserResult.getCommandType().toString(),
                parserResult.getCommandType());
        return cmResponse;
    }

}
