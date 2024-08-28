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
package com.ericsson.oss.services.cm.cmconfig.utilities;

import static org.hamcrest.Matchers.contains;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

import com.ericsson.oss.services.cm.cmshared.dto.CmObject;
import com.ericsson.oss.services.cm.cmshared.dto.CmResponse;
import com.ericsson.oss.services.scriptengine.spi.dtos.CommandResponseDto;
import com.ericsson.oss.services.scriptengine.spi.dtos.ListDto;
import com.ericsson.oss.services.scriptengine.spi.dtos.OrderedTableDto;

public class ResponseFormatterTest {

    private static final String NAMESPACE_NAME = "Some_model";
    private static final String NAMESPACE_VERSION = "1.0.1";
    private static final String MO_TYPE = "MeContext";
    private static final String NODE_NAME = "SomeName";
    private static final String FDN = "MeContext=" + NODE_NAME;
    public static final String ATTRIBUTE_1_NAME = "id";
    public static final String ATTRIBUTE_2_NAME = "label";
    public static final String ATTRIBUTE_3_NAME = "somethingThatIsNull";
    public static final int ATTRIBUTE_1_VALUE = 123;
    public static final String ATTRIBUTE_2_VALUE = "some label";
    public static final Object ATTRIBUTE_3_VALUE = null;
    private static final String ATTRIBUTE_DESCRIPTION_TYPE_KEY = "[00]";
    private static final String ATTRIBUTE_DESCRIPTION_TYPE_VALUE = "Mo Type";
    private static final String ATTRIBUTE_DESCRIPTION_LINE_1_KEY = "[01]";
    private static final String ATTRIBUTE_DESCRIPTION_LINE_1_VALUE = "Description line 1";

    ResponseFormatter objUnderTest;
    CmResponse cmResponse;

    @Before
    public void setUp() {
        objUnderTest = new ResponseFormatter();
        setUpCommandResponse();
    }

    private CmObject createCmObject() {
        final CmObject cmObject = new CmObject();
        cmObject.setFdn(FDN);
        cmObject.setName(NODE_NAME);
        cmObject.setType(MO_TYPE);
        cmObject.setNamespace(NAMESPACE_NAME);
        cmObject.setNamespaceVersion(NAMESPACE_VERSION);
        final Map<String, Object> attributes = new HashMap<>();
        attributes.put(ATTRIBUTE_1_NAME, ATTRIBUTE_1_VALUE);
        attributes.put(ATTRIBUTE_2_NAME, ATTRIBUTE_2_VALUE);
        attributes.put(ATTRIBUTE_3_NAME, ATTRIBUTE_3_VALUE);
        cmObject.setAttributes(attributes);

        return cmObject;
    }

    private void setUpCommandResponse() {
        final List<CmObject> cmObjects = new ArrayList<>();
        cmObjects.add(createCmObject());
        cmResponse = new CmResponse();
        cmResponse.setCmObjects(cmObjects);
        cmResponse.setStatusCode(cmObjects.size());
    }

    @Test
    public void canCreateCommandResponseDtoFromCmCommandResponseContainingOnlyOneMoListFormat() {
        final CommandResponseDto editorResponseResult = objUnderTest.generateCommandResponseDtoListFormat(cmResponse);

        checkResultValues(editorResponseResult);
        assertTrue(((ListDto) editorResponseResult.getCommandResultDto()).getDtos().get(0) instanceof ListDto);
    }

    @Test
    public void canCreateCommandResponseDtoFromCmCommandResponseContainingOnlyOneMoTableFormat() {
        final List<CmObject> cmObjects = new ArrayList<>();
        cmObjects.add(createCmObject());
        cmObjects.add(createCmObject());
        cmObjects.add(createCmObject());
        cmResponse = new CmResponse();
        cmResponse.setCmObjects(cmObjects);
        cmResponse.setStatusCode(cmObjects.size());

        final CommandResponseDto editorResponseResult = objUnderTest.generateCommandResponseDtoTableFormat(cmResponse);
        assertEquals(1, ((ListDto) editorResponseResult.getCommandResultDto()).getDtos().size());

        final OrderedTableDto tableDto = (OrderedTableDto) ((ListDto) editorResponseResult.getCommandResultDto()).getDtos().get(0);

        assertEquals(3, tableDto.getRows().size());
        assertEquals(4, tableDto.getColumnList().size());
        final List<String> resultColumns = new ArrayList<>();
        resultColumns.addAll(tableDto.getColumnList());

        assertThat(resultColumns, contains("FDN", ATTRIBUTE_1_NAME, ATTRIBUTE_2_NAME, ATTRIBUTE_3_NAME));

        final List<List> rows = tableDto.getRows();
        for (final List<String> row : rows) {
            assertThat(row, contains(FDN, "" + ATTRIBUTE_1_VALUE, ATTRIBUTE_2_VALUE, "" + ATTRIBUTE_3_VALUE));
        }
    }

    private void checkResultValues(final CommandResponseDto editorResponseResult) {
        assertEquals(1, editorResponseResult.getStatusCode());
        final String resultAsString = editorResponseResult.getCommandResultDto().toString();
        assertTrue(resultAsString.contains("FDN=MeContext=SomeName"));
        assertTrue(resultAsString.contains("somethingThatIsNull=null"));
        assertTrue(resultAsString.contains("label=some label"));
    }

    @Test
    public void canCreateCommandResponseForDescribeCommand() {
        final CommandResponseDto editorResponseResult = objUnderTest.generateCommandResponseDtoListFormat(createCmResponseForDescribeCommand());
        assertTrue(((ListDto) editorResponseResult.getCommandResultDto()).getDtos().get(0) instanceof ListDto);
        assertEquals(1, editorResponseResult.getStatusCode());
        assertTrue(editorResponseResult.getCommandResultDto().toString().contains(ATTRIBUTE_DESCRIPTION_TYPE_KEY));
        assertTrue(editorResponseResult.getCommandResultDto().toString().contains(ATTRIBUTE_DESCRIPTION_TYPE_VALUE));
        assertTrue(editorResponseResult.getCommandResultDto().toString().contains(ATTRIBUTE_DESCRIPTION_LINE_1_KEY));
        assertTrue(editorResponseResult.getCommandResultDto().toString().contains(ATTRIBUTE_DESCRIPTION_LINE_1_VALUE));
    }

    private CmResponse createCmResponseForDescribeCommand() {
        final CmResponse cmResponseDescribeCommand = new CmResponse();
        final List<CmObject> cmObjects = new ArrayList<>();
        final CmObject cmObject = new CmObject();
        final Map<String, Object> attributes = new HashMap<>();
        attributes.put(ATTRIBUTE_DESCRIPTION_TYPE_KEY, ATTRIBUTE_DESCRIPTION_TYPE_VALUE);
        attributes.put(ATTRIBUTE_DESCRIPTION_LINE_1_KEY, ATTRIBUTE_DESCRIPTION_LINE_1_VALUE);
        cmObject.setAttributes(attributes);
        cmObjects.add(cmObject);
        cmResponseDescribeCommand.setCmObjects(cmObjects);
        cmResponseDescribeCommand.setStatusCode(cmObjects.size());
        return cmResponseDescribeCommand;
    }
}
