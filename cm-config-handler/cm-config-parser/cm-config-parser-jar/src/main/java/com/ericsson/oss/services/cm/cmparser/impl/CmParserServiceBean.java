/*------------------------------------------------------------------------------
 *******************************************************************************
 * COPYRIGHT Ericsson 2013
 *
 * The copyright to the computer program(s) herein is the property of
 * Ericsson Inc. The programs may be used and/or copied only with written
 * permission from Ericsson Inc. or in accordance with the terms and
 * conditions stipulated in the agreement/contract under which the
 * program(s) have been supplied.
 *******************************************************************************
 *----------------------------------------------------------------------------*/
package com.ericsson.oss.services.cm.cmparser.impl;

import javax.enterprise.inject.Default;

import org.antlr.v4.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.ParseTreeWalker;

import com.ericsson.oss.services.cm.cmparser.CmCommandLexer;
import com.ericsson.oss.services.cm.cmparser.CmCommandParser;
import com.ericsson.oss.services.cm.cmparser.api.CMParserService;
import com.ericsson.oss.services.cm.cmparser.dto.ParserResult;
import com.ericsson.oss.services.cm.cmparser.dto.ParserResult.CommandType;

/**
 * {@link CMParserService} implementation.
 * 
 * 
 */
@Default
public class CmParserServiceBean implements CMParserService {

    private static final String CANNOT_PARSE_COMMAND = "Invalid Command Syntax";
    private static final int DEFAULT_PARSER_ERROR = -3;

    @Override
    public ParserResult parseCommand(final String command) {

        final ANTLRInputStream antlrInputStream = new ANTLRInputStream(command);
        final CmCommandLexer lexer = new CmCommandLexer(antlrInputStream);
        final CommonTokenStream tokens = new CommonTokenStream(lexer);
        final CmCommandParser cmCommandParser = new CmCommandParser(tokens);

        // Must remove default ANTLR error listener
        cmCommandParser.removeErrorListeners();

        final ParseTree tree = cmCommandParser.command();

        if (cmCommandParser.getNumberOfSyntaxErrors() > 0) {
            // Return error if any syntax issue
            // Note for future: use Antlr bail out strategy for errors
            return createParserResultForError();
        }

        final ParseTreeWalker walker = new ParseTreeWalker();
        final CmCommandWalker cmCommandWalker = new CmCommandWalker();

        walker.walk(cmCommandWalker, tree);

        return cmCommandWalker.getParserResult();

    }

    private ParserResult createParserResultForError() {
        final ParserResult parserResult = new ParserResult();
        parserResult.setCommandType(CommandType.PARSER_ERROR);
        parserResult.setStatusCode(DEFAULT_PARSER_ERROR);
        parserResult.setStatusMessage(CANNOT_PARSE_COMMAND);
        return parserResult;
    }
}
