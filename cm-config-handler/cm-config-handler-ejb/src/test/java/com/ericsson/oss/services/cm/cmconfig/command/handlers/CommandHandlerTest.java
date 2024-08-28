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

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.contains;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;

import com.ericsson.oss.services.cm.cmparser.dto.ParserResult;
import com.ericsson.oss.services.cm.cmshared.dto.CmObject;
import com.ericsson.oss.services.cm.cmshared.dto.CmResponse;
import com.ericsson.oss.services.scriptengine.spi.dtos.AbstractDto;
import com.ericsson.oss.services.scriptengine.spi.dtos.CommandResponseDto;
import com.ericsson.oss.services.scriptengine.spi.dtos.ListDto;
import com.ericsson.oss.services.scriptengine.spi.dtos.OrderedTableDto;

public class CommandHandlerTest {

    private static final String FDN_ATTRIBUTE = "FDN";
    final String FDN_VALUE = "some fdn";

    CommandHandler objUnderTest = new TestCommandHandler();
    final String ATTRIBUTE_NAME_1 = "id";
    final String ATTRIBUTE_NAME_2 = "label";
    final int ATTRIBUTE_VALUE_1 = 123;

    final String ATTRIBUTE_VALUE_2 = "some label";

    @Test
    public void processCommandReturnsTheCorrectMoAsAListDto() {
        // Set-up mock responses
        final CommandResponseDto result = objUnderTest.processCommand(new ParserResult());
        // verify the results
        assertEquals("1 CmObject in result so statusCode should be 1", 1, result.getStatusCode());
        // TODO eeitsik this test is to involved in the result formatter, this
        // should be mocked instead and have its own unit tests!
        // Expect a ListDto
        assertEquals(ListDto.class, result.getCommandResultDto().getClass());

        final ListDto listDto = (ListDto) result.getCommandResultDto();
        final List<AbstractDto> mos = listDto.getDtos();
        assertEquals(1, mos.size());
        final ListDto firstMo = (ListDto) mos.iterator().next();
        final List<AbstractDto> attributes = firstMo.getDtos();
        assertEquals(FDN_VALUE, firstMo.getDtoName());
        final String expectedNameValuePairForFdn = "FDN=" + FDN_VALUE;
        assertTrue(attributes.toString().contains(expectedNameValuePairForFdn));
        final String expectedIdValue = "123";
        assertTrue(attributes.toString().contains(expectedIdValue));
    }

    @Test
    public void processCommandReturnsTheCorrectMoAsATableDto() {
        final ParserResult parserResult = new ParserResult();
        parserResult.setOutputTable(true);
        final CommandResponseDto result = objUnderTest.processCommand(parserResult);
        // verify the results
        assertEquals("1 CmObject in result so statusCode should be 1", 1, result.getStatusCode());
        assertEquals(OrderedTableDto.class, ((ListDto) result.getCommandResultDto()).getDtos().get(0).getClass());

        final OrderedTableDto tableDto = (OrderedTableDto) ((ListDto) result.getCommandResultDto()).getDtos().get(0);
        final List<String> columnList = tableDto.getColumnList();
        assertEquals(3, columnList.size());
        assertThat(columnList, contains(FDN_ATTRIBUTE, ATTRIBUTE_NAME_1, ATTRIBUTE_NAME_2));

        final List<List<String>> rowList = tableDto.getRows();
        for (final List<String> row : rowList) {
            assertThat(row, contains(FDN_VALUE, "" + ATTRIBUTE_VALUE_1, ATTRIBUTE_VALUE_2));
        }
    }

    class TestCommandHandler extends CommandHandler {

        @Override
        CmResponse getCmResponse(final ParserResult command) {

            final CmResponse cmResponse = new CmResponse();
            final CmObject cmObject = new CmObject();
            cmObject.setFdn(FDN_VALUE);
            final Map<String, Object> attributes = new HashMap<>();
            attributes.put(ATTRIBUTE_NAME_1, ATTRIBUTE_VALUE_1);
            attributes.put(ATTRIBUTE_NAME_2, ATTRIBUTE_VALUE_2);
            cmObject.setAttributes(attributes);
            cmResponse.setStatusCode(1);
            cmResponse.setCmObjects(cmObject);
            return cmResponse;
        }

    }
}
