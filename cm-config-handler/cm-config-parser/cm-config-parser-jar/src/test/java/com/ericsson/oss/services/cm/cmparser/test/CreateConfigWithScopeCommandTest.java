package com.ericsson.oss.services.cm.cmparser.test;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import static org.unitils.reflectionassert.ReflectionAssert.assertReflectionEquals;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import org.junit.Test;

import com.ericsson.oss.services.cm.cmparser.dto.ParserResult;
import com.ericsson.oss.services.cm.cmparser.test.builders.CmObjectSpecificationBuilder;
import com.ericsson.oss.services.cm.cmparser.test.builders.CmSearchScopeBuilder;
import com.ericsson.oss.services.cm.cmparser.test.builders.ParserResultBuilder;
import com.ericsson.oss.services.cm.cmshared.dto.AttributeSpecification;
import com.ericsson.oss.services.cm.cmshared.dto.CmObjectSpecification;
import com.ericsson.oss.services.cm.cmshared.dto.StringifiedAttributeSpecifications;
import com.ericsson.oss.services.cm.cmshared.dto.search.CmMatchCondition;
import com.ericsson.oss.services.cm.cmshared.dto.search.CmSearchScope;
import com.ericsson.oss.services.cm.cmshared.dto.search.CmSearchScope.ScopeType;

public class CreateConfigWithScopeCommandTest extends CommandTest {

    final String type = "myType";
    final String namespace = "model";
    final String typeSpecification = " " + type + " -namespace=" + namespace;
    private final static String VERSION = "1.1.1";
    final String[] ALL_ATTRIBUTES = { "*" };

    @Test
    public void parseCommandReturnsCorrectDataForCreateConfigWithScopeFDNCommand() {
        final String fdn = "MeContext=bar,ManagedElement=splat,ENodeBFunction=hey123_-";
        final ParserResult parserResult = cmParserService.parseCommand("create myconfig scope " + fdn);
        assertParseResult(parserResult, ScopeType.FDN, fdn, null, null);
    }

    @Test
    public void parseCreateConfigCommandWithMultipeIdsScope() {
        final String ids[] = { "myId0", "myId1", "myId2", "myId3" };
        final ParserResult parserResult = cmParserService.parseCommand(String.format("create myconfig scope %s;%s;%s", ids));
        final ScopeType scopeTypes[] = { ScopeType.NODE_NAME, ScopeType.NODE_NAME, ScopeType.NODE_NAME };
        assertParseResult(parserResult, Arrays.asList(scopeTypes), Arrays.asList(ids), CmMatchCondition.EQUALS, null);
    }

    @Test
    public void parseCreateConfigCommandWithChildScope() {
        final ParserResult parserResult = cmParserService.parseCommand("create myconfig scope * MeContext.*,ManagedElement.*");
        final CmObjectSpecification cmObjectSpecification = parserResult.getCmObjectSpecifications().get(0);
        assertEquals("MeContext", cmObjectSpecification.getType());
        assertEquals(1, cmObjectSpecification.getChildCmObjectSpecifications().size());
        assertEquals("ManagedElement", cmObjectSpecification.getChildCmObjectSpecifications().get(0).getType());
    }

    @Test
    public void parseCreateConfigCommandWithAllChildrenScope() {
        final ParserResult parserResult = cmParserService.parseCommand("create myconfig scope * MeContext,*");
        final CmObjectSpecification cmObjectSpecification = parserResult.getCmObjectSpecifications().get(0);
        assertEquals("MeContext", cmObjectSpecification.getType());
        assertEquals(CmObjectSpecification.ALL_CHILDREN, cmObjectSpecification.getChildCmObjectSpecifications());
    }

    @Test
    public void parseCreateConfigCommandWithIdScope() {
        final String id = "myId";
        final ParserResult parserResult = cmParserService.parseCommand("create myconfig scope " + id);
        assertParseResult(parserResult, ScopeType.NODE_NAME, id, CmMatchCondition.EQUALS, null);
    }

    @Test
    public void parseCreateConfigCommandWithTwoStarsScope() {
        final ParserResult parserResult = cmParserService.parseCommand("create myconfig scope **");
        assertThat(parserResult.getStatusCode(), equalTo(-3));
    }

    @Test
    public void parseCreateConfigCommandWithIdAndTypeScope() {
        final String id = "myId";
        final ParserResult parserResult = cmParserService.parseCommand("create myconfig scope " + id + " " + typeSpecification);
        assertParseResult(parserResult, ScopeType.NODE_NAME, id, CmMatchCondition.EQUALS, type);
    }

    @Test
    public void parseCreateConfigCommandWithMultipeIdsAndTypeScope() {
        final String ids[] = { "myId0", "myId1", "myId2", "myId3" };
        final ParserResult parserResult = cmParserService
                .parseCommand(String.format("create myconfig scope %s;%s;%s", ids) + " " + typeSpecification);
        final ScopeType scopeTypes[] = { ScopeType.NODE_NAME, ScopeType.NODE_NAME, ScopeType.NODE_NAME };
        assertParseResult(parserResult, Arrays.asList(scopeTypes), Arrays.asList(ids), CmMatchCondition.EQUALS, type);
    }

    @Test
    public void parseCreateConfigCommandWithIdAndTypeAndAllAttributesScope() {
        final String id = "myId";
        final ParserResult parserResult = cmParserService.parseCommand("create myconfig scope " + id + " " + type + ".* -namespace=" + namespace);
        assertParseResult(parserResult, ScopeType.NODE_NAME, id, CmMatchCondition.EQUALS, type, ALL_ATTRIBUTES);
    }

    @Test
    public void parseCreateConfigCommandWithIdAndTypeAnd1AttributeScope() {
        final String id = "myId";
        final ParserResult parserResult = cmParserService.parseCommand("create myconfig scope " + id + " " + type + ".(attr1) -namespace="
                + namespace);
        assertParseResult(parserResult, ScopeType.NODE_NAME, id, CmMatchCondition.EQUALS, type, "attr1");
    }

    @Test
    public void parseCreateConfigCommandWithIdAndTypeAndSeveralAttributesScope() {
        final String id = "myId";
        final ParserResult parserResult = cmParserService.parseCommand("create myconfig scope " + id + " " + type
                + ".(attr1,attr2,attr3) -namespace=" + namespace);
        assertParseResult(parserResult, ScopeType.NODE_NAME, id, CmMatchCondition.EQUALS, type, "attr1", "attr2", "attr3");
    }

    @Test
    public void parseCreateConfigCommandWithIdAndTypeAndAttributeFilterScope() {
        final String id = "myId";
        final ParserResult parserResult = cmParserService.parseCommand("create myconfig scope " + id + " " + type + ".(attr1==1) -namespace="
                + namespace);
        assertParseResult(parserResult, ScopeType.NODE_NAME, id, CmMatchCondition.EQUALS, type, "attr1==1");
    }

    @Test
    public void parseCreateConfigCommandWithIdAndTypeAndAttributeFilterForStringScope() {
        final String id = "myId";
        final ParserResult parserResult = cmParserService.parseCommand("create myconfig scope " + id + " " + type
                + ".(attr1==\"my string\") -namespace=" + namespace);
        assertParseResult(parserResult, ScopeType.NODE_NAME, id, CmMatchCondition.EQUALS, type, "attr1==my string");
    }

    @Test
    public void parseCreateConfigCommandWithIdAndTypeAndAttributeFiltersAndSelectionScope() {
        final String id = "myId";
        final ParserResult parserResult = cmParserService.parseCommand("create myconfig scope " + id + " " + type
                + ".(attr1==\"my string\",attr2,attr3==123,attr4) -namespace=" + namespace);
        assertParseResult(parserResult, ScopeType.NODE_NAME, id, CmMatchCondition.EQUALS, type, "attr1==my string", "attr2", "attr3==123", "attr4");
    }

    @Test
    public void parseCreateConfigCommandWithStarAndTypeScope() {
        final String id = "*";
        final ParserResult parserResult = cmParserService.parseCommand("create myconfig scope " + id + " " + typeSpecification);
        assertParseResult(parserResult, ScopeType.UNSPECIFIED, null, null, type);
    }

    @Test
    public void parseCreateConfigCommandWithFdnAndTypeScope() {
        final String id = "MeContext=ERBS01";
        final ParserResult parserResult = cmParserService.parseCommand("create myconfig scope " + id + " " + typeSpecification);
        assertParseResult(parserResult, ScopeType.FDN, id, null, type);
    }

    @Test
    public void parseCreateConfigCommandWithStartsWithAndTypeScope() {
        final String id = "ERBS";
        final ParserResult parserResult = cmParserService.parseCommand("create myconfig scope " + id + "* " + typeSpecification);
        assertParseResult(parserResult, ScopeType.NODE_NAME, id, CmMatchCondition.STARTS_WITH, type);
    }

    @Test
    public void parseCreateConfigCommandWithEndsWithAndTypeScope() {
        final String id = "ERBS";
        final ParserResult parserResult = cmParserService.parseCommand("create myconfig scope *" + id + " " + typeSpecification);
        assertParseResult(parserResult, ScopeType.NODE_NAME, id, CmMatchCondition.ENDS_WITH, type);
    }

    @Test
    public void parseCreateConfigCommandWithContainsAndTypeScope() {
        final String id = "ERBS";
        final ParserResult parserResult = cmParserService.parseCommand("create myconfig scope *" + id + "* " + typeSpecification);
        assertParseResult(parserResult, ScopeType.NODE_NAME, id, CmMatchCondition.CONTAINS, type);
    }

    @Test
    public void parseCreateConfigCommandWithTypeAndAnyAttributeValueScope() {
        final String id = "ERBS";
        final ParserResult parserResult = cmParserService.parseCommand("create myconfig scope " + id + " " + type + ".attr1==* -namespace="
                + namespace);
        assertParseResult(parserResult, ScopeType.NODE_NAME, id, CmMatchCondition.EQUALS, type, "attr1==*");
    }

    @Test
    public void parseCreateConfigCommandWithTypeAndAttributeValueStartingWithPartialStringScope() {
        final String id = "ERBS";
        final ParserResult parserResult = cmParserService.parseCommand("create myconfig scope " + id + " " + type + ".attr1==Ath* -namespace="
                + namespace);
        assertParseResult(parserResult, ScopeType.NODE_NAME, id, CmMatchCondition.EQUALS, type, "attr1==Ath*");
    }

    @Test
    public void parseCreateConfigCommandWithTypeAndAttributeValueEndingWithPartialStringScope() {
        final String id = "ERBS";
        final ParserResult parserResult = cmParserService.parseCommand("create myconfig scope " + id + " " + type + ".attr1==*Ath -namespace="
                + namespace);
        assertParseResult(parserResult, ScopeType.NODE_NAME, id, CmMatchCondition.EQUALS, type, "attr1==*Ath");
    }

    @Test
    public void parseCreateConfigCommandWithTypeAndAttributeValueContainingStringScope() {
        final String id = "ERBS";
        final ParserResult parserResult = cmParserService.parseCommand("create myconfig scope " + id + " " + type + ".attr1==*Ath* -namespace="
                + namespace);
        assertParseResult(parserResult, ScopeType.NODE_NAME, id, CmMatchCondition.EQUALS, type, "attr1==*Ath*");
    }

    @Test
    public void parseCreateConfigCommandWithTypeAndAttributeValueStartingWithPartialStringInQuotesScope() {
        final String id = "ERBS";
        final ParserResult parserResult = cmParserService.parseCommand("create myconfig scope " + id + " " + type + ".attr1==\"Ath \"* -namespace="
                + namespace);
        assertParseResult(parserResult, ScopeType.NODE_NAME, id, CmMatchCondition.EQUALS, type, "attr1==Ath *");
    }

    @Test
    public void parseCreateConfigCommandWithTypeAndAttributeValueEndingWithPartialStringInQuotesScope() {
        final String id = "ERBS";
        final ParserResult parserResult = cmParserService.parseCommand("create myconfig scope " + id + " " + type + ".attr1==*\"Ath_\" -namespace="
                + namespace);
        assertParseResult(parserResult, ScopeType.NODE_NAME, id, CmMatchCondition.EQUALS, type, "attr1==*Ath_");
    }

    @Test
    public void parseCreateConfigCommandWithTypeAndAttributeValueContainingStringInQuotesScope() {
        final String id = "ERBS";
        final ParserResult parserResult = cmParserService.parseCommand("create myconfig scope " + id + " " + type + ".attr1==*\"Ath\"* -namespace="
                + namespace);
        assertParseResult(parserResult, ScopeType.NODE_NAME, id, CmMatchCondition.EQUALS, type, "attr1==*Ath*");
    }

    @Test
    public void parseCreateConfigCommandWithTypeAndNoNamespaceScope() {
        final ParserResult actualParserResult = cmParserService.parseCommand("create myconfig scope ERBS01 ENodeBFunction");

        final CmSearchScope cmSearchScope = new CmSearchScopeBuilder().build();

        final CmObjectSpecification cmObjectSpecification = new CmObjectSpecificationBuilder().withAttributeSpecificationContainer(
                new StringifiedAttributeSpecifications()).build();

        final ParserResult expectedParserResult = new ParserResultBuilder().withCommandType(ParserResult.CommandType.CREATE_CONFIG_COMMAND)
                .withConfigurationName("myconfig").withCmSearchScopes(cmSearchScope).withCmObjectSpecifications(cmObjectSpecification).build();

        assertReflectionEquals(expectedParserResult, actualParserResult);
    }

    @Test
    public void parseCreateConfigCommandWithTypeAndVersionScope() {
        final ParserResult actualParserResult = cmParserService.parseCommand("create myconfig scope ERBS01 ENodeBFunction -version=" + VERSION);

        final CmSearchScope cmSearchScope = new CmSearchScopeBuilder().build();

        final CmObjectSpecification cmObjectSpecification = new CmObjectSpecificationBuilder()
                .withAttributeSpecificationContainer(new StringifiedAttributeSpecifications()).withNamespaceVersion(VERSION).build();

        final ParserResult expectedParserResult = new ParserResultBuilder().withCommandType(ParserResult.CommandType.CREATE_CONFIG_COMMAND)
                .withConfigurationName("myconfig").withCmSearchScopes(cmSearchScope).withCmObjectSpecifications(cmObjectSpecification)

                .build();

        assertReflectionEquals(expectedParserResult, actualParserResult);
    }

    /*
     * N E G A T I V E - T E S T S
     */

    public void parseInvalidCommands() {
        final String id = "ER*BS";
        final ParserResult parserResult = cmParserService.parseCommand("create myconfig scope " + id + " " + typeSpecification);
        assertEquals(-3, parserResult.getStatusCode());

    }

    private void assertParseResult(final ParserResult parserResult, final ScopeType expectedScopeType, final String expectedId,
            final CmMatchCondition expectedCmMatchCondition, final String type, final String... expectedAttributes) {
        final List<String> expectedIds = new ArrayList<>(1);
        final List<ScopeType> expectedScopeTypes = new ArrayList<>(1);
        expectedScopeTypes.add(expectedScopeType);
        expectedIds.add(expectedId);
        assertParseResult(parserResult, expectedScopeTypes, expectedIds, expectedCmMatchCondition, type, expectedAttributes);
    }

    private void assertParseResult(final ParserResult parserResult, final List<ScopeType> scopeTypes, final List<String> expectedIds,
            final CmMatchCondition expectedCmMatchCondition, final String type, final String... expectedAttributes) {
        assertEquals(1, parserResult.getStatusCode());

        final Iterator idIterator = expectedIds.iterator();
        final Iterator scopeTypeIterator = scopeTypes.iterator();
        for (final CmSearchScope nodeId : parserResult.getCmScopes()) {
            assertEquals(scopeTypeIterator.next(), nodeId.getScopeType());
            if (scopeTypes.equals(ScopeType.NODE_NAME)) {
                assertEquals("MeContext", nodeId.getName());
            }

            assertEquals(idIterator.next(), nodeId.getValue());
            assertEquals(expectedCmMatchCondition, nodeId.getCmMatchCondition());
        }
        if (type == null) {
            assertTrue(parserResult.getCmObjectSpecifications().isEmpty());
        } else {
            final CmObjectSpecification cmObjectSpecification = parserResult.getCmObjectSpecifications().iterator().next();
            assertEquals(type, cmObjectSpecification.getType());
            assertEquals(namespace, cmObjectSpecification.getNamespace());
            assertAttributes(cmObjectSpecification, expectedAttributes);
        }
    }

    private void assertAttributes(final CmObjectSpecification cmObjectSpecification, final String... expectedAttributes) {
        if (expectedAttributes.length == 0) {
            assertTrue(cmObjectSpecification.includeNoAttributes());
        } else {
            if (expectedAttributes.equals(ALL_ATTRIBUTES)) {
                assertTrue(cmObjectSpecification.includeAllAttributes());
            } else {
                assertEquals(expectedAttributes.length, cmObjectSpecification.getAttributeSpecificationContainer().size());
                for (final String expectedAttribute : expectedAttributes) {

                    if (expectedAttribute.contains("==")) {
                        assertAttribute(cmObjectSpecification, expectedAttribute);
                    } else {
                        final AttributeSpecification attributeSpecification = cmObjectSpecification.getAttributeSpecificationContainer()
                                .getAttributeSpecification(expectedAttribute);
                        assertEquals(CmMatchCondition.NO_MATCH_REQUIRED, attributeSpecification.getCmMatchCondition());
                    }
                }
            }
        }
    }

    private void assertAttribute(final CmObjectSpecification cmObjectSpecification, final String expectedAttribute) {
        final String[] tokens = expectedAttribute.split("==");
        final String name = tokens[0];
        String value = tokens[1];
        final AttributeSpecification attributeSpecification = cmObjectSpecification.getAttributeSpecificationContainer().getAttributeSpecification(
                name);
        assertEquals(name, attributeSpecification.getName());
        CmMatchCondition expectedCmMatchCondition = CmMatchCondition.EQUALS;
        if (value.equals("*")) {
            expectedCmMatchCondition = CmMatchCondition.NO_MATCH_REQUIRED;
            value = null;
        } else if (value.startsWith("*") && value.endsWith("*")) {
            expectedCmMatchCondition = CmMatchCondition.CONTAINS;
            value = value.substring(1, value.length() - 1);
        } else if (value.endsWith("*")) {
            expectedCmMatchCondition = CmMatchCondition.STARTS_WITH;
            value = value.substring(0, value.length() - 1);
        } else if (value.startsWith("*")) {
            expectedCmMatchCondition = CmMatchCondition.ENDS_WITH;
            value = value.substring(1);
        }
        assertEquals(value, attributeSpecification.getValue());
        assertEquals(expectedCmMatchCondition, attributeSpecification.getCmMatchCondition());
    }
}
