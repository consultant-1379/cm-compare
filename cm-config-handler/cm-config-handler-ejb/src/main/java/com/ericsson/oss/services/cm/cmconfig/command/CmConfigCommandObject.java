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

import com.ericsson.oss.services.cm.cmconfig.command.handlers.CommandHandler;
import com.ericsson.oss.services.cm.cmparser.dto.ParserResult;
import com.ericsson.oss.services.scriptengine.spi.dtos.CommandResponseDto;

/**
 * 
 * The class create and execute a config command based on the
 * {@link ParserResult} obtained.
 * 
 */
public class CmConfigCommandObject {

    CommandHandler commandHandler;

    ParserResult parserResult;

    /**
     * 
     * @return CommandResponseDto
     */
    public CommandResponseDto execute() {
        return this.commandHandler.processCommand(this.parserResult);
    }

    /**
     * @return the commandHandler
     */
    public CommandHandler getCommandHandler() {
        return this.commandHandler;
    }

    /**
     * @param commandHandler
     *            the commandHandler to set
     */
    public void setCommandHandler(final CommandHandler commandHandler) {
        this.commandHandler = commandHandler;
    }

    /**
     * @return the parserResult
     */
    public ParserResult getParserResult() {
        return this.parserResult;
    }

    /**
     * @param parserResult
     *            the parserResult to set
     */
    public void setParserResult(final ParserResult parserResult) {
        this.parserResult = parserResult;
    }

}
