package com.ericsson.oss.services.cm.cmparser.test;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.ericsson.oss.services.cm.cmparser.dto.ParserResult;

public class ParserErrorTest extends CommandTest {

    @Test
    public void parsingBadCommadnReturnsParserErrorCodeAndMessage() {
        final ParserResult parserResult = parseCommand("some nonsense");
        final int standardParserErrorCode = PARSER_ERROR;
        assertEquals(standardParserErrorCode, parserResult.getStatusCode());
    }
}
