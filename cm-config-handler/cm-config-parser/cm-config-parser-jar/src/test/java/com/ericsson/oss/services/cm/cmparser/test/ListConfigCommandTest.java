package com.ericsson.oss.services.cm.cmparser.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

import com.ericsson.oss.services.cm.cmparser.dto.ParserResult;
import com.ericsson.oss.services.cm.cmparser.dto.ParserResult.CommandType;

public class ListConfigCommandTest extends CommandTest {

    @Test
    public void parseListCmdWithNoOptions() {
        final String command = "list";
        final ParserResult parserResult = parseCommand(command);
        assertParseResult(parserResult, false, false, null);
    }

    @Test
    public void parseListCmdWithAllOption() {
        final String command = "list -all";
        final ParserResult parserResult = parseCommand(command);
        assertParseResult(parserResult, true, false, null);
    }

    @Test
    public void parseListCmdWithOwnerOption() {
        final String command = "list -owner pepe";
        final ParserResult parserResult = parseCommand(command);
        assertParseResult(parserResult, false, true, "pepe");
    }

    private void assertParseResult(final ParserResult parserResult, final boolean expectedListAllOption, final boolean expectedListOwnerOption,
            final String expectedListOwnerName) {
        assertEquals("The CommandType must be equal", CommandType.LIST_CONFIG_COMMAND, parserResult.getCommandType());
        assertEquals("The StatusCode has to be SUCCESS", PARSER_SUCCESS, parserResult.getStatusCode());
        assertEquals("The List All option must to be equal", expectedListAllOption, parserResult.getListConfigOptions().isListConfigAllOption());
        assertEquals("The List Owner option must to be equal", expectedListOwnerOption, parserResult.getListConfigOptions().isListConfigOwnerOption());
        if (expectedListOwnerName != null) {
            assertEquals("The Config Owner name option must to be equal", expectedListOwnerName, parserResult.getListConfigOptions().getConfigOwner());
        } else {
            assertNull("The config owner must be null", expectedListOwnerName);
        }

    }
}