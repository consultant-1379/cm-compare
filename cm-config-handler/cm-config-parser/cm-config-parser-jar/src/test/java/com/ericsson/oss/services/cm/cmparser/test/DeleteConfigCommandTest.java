package com.ericsson.oss.services.cm.cmparser.test;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.ericsson.oss.services.cm.cmparser.dto.ParserResult;
import com.ericsson.oss.services.cm.cmparser.dto.ParserResult.CommandType;

public class DeleteConfigCommandTest extends CommandTest {

    private static final String CONFIG_NAME = "myconfig";

    @Test
    public void parseDeleteCmd() {
        final String command = "delete " + CONFIG_NAME;
        final ParserResult parserResult = parseCommand(command);
        assertParseResult(parserResult);
    }

    private void assertParseResult(final ParserResult parserResult) {
        assertEquals("The CommandType must be equal", CommandType.DELETE_CONFIG_COMMAND, parserResult.getCommandType());
        assertEquals("The StatusCode has to be SUCCESS", PARSER_SUCCESS, parserResult.getStatusCode());
        assertEquals("The Config name must to be equal", CONFIG_NAME, parserResult.getConfigurationName());

    }
}