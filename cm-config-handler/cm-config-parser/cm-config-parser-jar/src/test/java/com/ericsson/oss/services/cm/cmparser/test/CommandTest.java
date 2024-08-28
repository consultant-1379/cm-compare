package com.ericsson.oss.services.cm.cmparser.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.Before;

import com.ericsson.oss.services.cm.cmparser.api.CMParserService;
import com.ericsson.oss.services.cm.cmparser.dto.ParserResult;
import com.ericsson.oss.services.cm.cmparser.impl.CmParserServiceBean;

public class CommandTest {
    static final int PARSER_SUCCESS = 1;
    static final int PARSER_ERROR = -3;

    CMParserService cmParserService;

    @Before
    public void setupTest() {
        cmParserService = new CmParserServiceBean();
    }

    protected ParserResult parseCommand(final String commandString) {
        final ParserResult parserResult = cmParserService.parseCommand(commandString);
        assertNotNull(parserResult);
        return parserResult;

    }

    protected ParserResult parseCommandAndAssertResult(final String commandString, final int expectedStatusCode) {
        final ParserResult parserResult = parseCommand(commandString);
        assertEquals(expectedStatusCode, parserResult.getStatusCode());
        return parserResult;
    }
}
