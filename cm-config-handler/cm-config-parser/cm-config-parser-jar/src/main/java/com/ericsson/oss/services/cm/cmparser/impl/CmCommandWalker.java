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

import java.util.ArrayList;
import java.util.List;

import org.antlr.v4.runtime.misc.NotNull;

import com.ericsson.oss.services.cm.cmparser.CmCommandParser;
import com.ericsson.oss.services.cm.cmparser.CmCommandParser.AllAttributesContext;
import com.ericsson.oss.services.cm.cmparser.CmCommandParser.AllFlagContext;
import com.ericsson.oss.services.cm.cmparser.CmCommandParser.AllTypesContext;
import com.ericsson.oss.services.cm.cmparser.CmCommandParser.AnyValueContext;
import com.ericsson.oss.services.cm.cmparser.CmCommandParser.AttributeComparisonContext;
import com.ericsson.oss.services.cm.cmparser.CmCommandParser.AttributeFiltersContext;
import com.ericsson.oss.services.cm.cmparser.CmCommandParser.AttributeSelectorContext;
import com.ericsson.oss.services.cm.cmparser.CmCommandParser.AttributeSetterContext;
import com.ericsson.oss.services.cm.cmparser.CmCommandParser.AttributeSettersContext;
import com.ericsson.oss.services.cm.cmparser.CmCommandParser.AttributeSpecificationForComplexContext;
import com.ericsson.oss.services.cm.cmparser.CmCommandParser.AttributeSpecificationsContext;
import com.ericsson.oss.services.cm.cmparser.CmCommandParser.AttributeStringValueContext;
import com.ericsson.oss.services.cm.cmparser.CmCommandParser.AttributeValueInSequenceContext;
import com.ericsson.oss.services.cm.cmparser.CmCommandParser.CmObjectSpecificationContext;
import com.ericsson.oss.services.cm.cmparser.CmCommandParser.CommandContext;
import com.ericsson.oss.services.cm.cmparser.CmCommandParser.ComplexAttributeValueInSequenceContext;
import com.ericsson.oss.services.cm.cmparser.CmCommandParser.ContainsValueContext;
import com.ericsson.oss.services.cm.cmparser.CmCommandParser.EndsWithValueContext;
import com.ericsson.oss.services.cm.cmparser.CmCommandParser.GreaterThanOrEqualToValueContext;
import com.ericsson.oss.services.cm.cmparser.CmCommandParser.GreaterThanValueContext;
import com.ericsson.oss.services.cm.cmparser.CmCommandParser.LessThanOrEqualToValueContext;
import com.ericsson.oss.services.cm.cmparser.CmCommandParser.LessThanValueContext;
import com.ericsson.oss.services.cm.cmparser.CmCommandParser.MeContextNameContainsContext;
import com.ericsson.oss.services.cm.cmparser.CmCommandParser.MeContextNameContext;
import com.ericsson.oss.services.cm.cmparser.CmCommandParser.MeContextNameEndsWithContext;
import com.ericsson.oss.services.cm.cmparser.CmCommandParser.MeContextNameEqualsContext;
import com.ericsson.oss.services.cm.cmparser.CmCommandParser.MeContextNameStartsWithContext;
import com.ericsson.oss.services.cm.cmparser.CmCommandParser.NamespaceContext;
import com.ericsson.oss.services.cm.cmparser.CmCommandParser.StartsWithValueContext;
import com.ericsson.oss.services.cm.cmparser.CmCommandParser.TableOutputContext;
import com.ericsson.oss.services.cm.cmparser.CmCommandParser.VersionContext;
import com.ericsson.oss.services.cm.cmparser.CmCommandParserBaseListener;
import com.ericsson.oss.services.cm.cmparser.dto.ParserResult;
import com.ericsson.oss.services.cm.cmparser.dto.ParserResult.CommandType;
import com.ericsson.oss.services.cm.cmshared.dto.AttributeSpecificationContainer;
import com.ericsson.oss.services.cm.cmshared.dto.CmObjectSpecification;
import com.ericsson.oss.services.cm.cmshared.dto.StringifiedAttributeSpecifications;
import com.ericsson.oss.services.cm.cmshared.dto.search.CmMatchCondition;
import com.ericsson.oss.services.cm.cmshared.dto.search.CmSearchScope;
import com.ericsson.oss.services.cm.cmshared.dto.search.CmSearchScope.ScopeType;

/**
 * 
 * {@link CmCommandParserBaseListener} implementation for composing a
 * {@link ParserResult} object.
 * 
 * 
 */
@SuppressWarnings({ "PMD.ExcessivePublicCount", "PMD.TooManyMethods" })
public class CmCommandWalker extends CmCommandParserBaseListener {

    private static final int STATUS_SUCCESS = 1;
    private static final int STATUS_FAIL = -3;

    private ParserResult parserResult;
    private CmSearchScope cmSearchScope = new CmSearchScope();
    private final List<CmSearchScope> cmScopeList = new ArrayList<>();
    private Object lastAttributeValue;
    private CmMatchCondition cmMatchCondition;

    private final Stack<CmObjectSpecification> cmObjectSpecificationStack = new DequeStack<>();

    private final Stack<DataContainer> dataContainerStack = new DequeStack<>();

    public ParserResult getParserResult() {
        return this.parserResult;
    }

    /*
     * SCOPE - HANDLING
     */
    @Override
    public void enterScope(@NotNull final CmCommandParser.ScopeContext ctx) {
        this.cmSearchScope = new CmSearchScope();
    }

    @Override
    public void exitScope(@NotNull final CmCommandParser.ScopeContext ctx) {
        this.cmScopeList.add(this.cmSearchScope);
    }

    @Override
    public void exitFdn(@NotNull final CmCommandParser.FdnContext ctx) {
        this.cmSearchScope.setScopeType(ScopeType.FDN);
        this.cmSearchScope.setValue(ctx.getText());
    }

    @Override
    public void enterCmObjectSpecification(final CmObjectSpecificationContext ctx) {
        handleNewCmObjectSpecification();
    }

    @Override
    public void exitCmObjectSpecification(final CmObjectSpecificationContext ctx) {
        final CmObjectSpecification cmObjectSpecification = this.cmObjectSpecificationStack.pop();
        if (this.cmObjectSpecificationStack.isEmpty()) {
            // This is the main (parent) cmObjectSpecification and needs to be
            // added to parser result
            this.parserResult.setCmObjectSpecifications(cmObjectSpecification);
        } else {
            // This must be a child cmObjectSpecification, add it to the parent
            final CmObjectSpecification parentCmObjectSpecification = getCurrentCmObjectSpecification();
            parentCmObjectSpecification.getChildCmObjectSpecifications().add(cmObjectSpecification);
        }
    }

    @Override
    public void exitType(@NotNull final CmCommandParser.TypeContext ctx) {
        this.getCurrentCmObjectSpecification().setType(ctx.getText());
    }

    @Override
    public void exitAllTypes(final AllTypesContext ctx) {
        this.getCurrentCmObjectSpecification().setChildCmObjectSpecifications(CmObjectSpecification.ALL_CHILDREN);
    }

    @Override
    public void exitName(@NotNull final CmCommandParser.NameContext ctx) {
        this.getCurrentCmObjectSpecification().setName(ctx.getText());
    }

    @Override
    public void exitMeContextName(final MeContextNameContext ctx) {
        this.cmSearchScope.setScopeType(ScopeType.NODE_NAME);
        // For a NODE_NAME scope the name(/mo type) is always MeContext
        this.cmSearchScope.setName("MeContext");
    }

    @Override
    public void exitMeContextNameEquals(final MeContextNameEqualsContext ctx) {
        this.cmSearchScope.setCmMatchCondition(CmMatchCondition.EQUALS);
        this.cmSearchScope.setValue(ctx.id().getText());
    }

    @Override
    public void exitMeContextNameContains(final MeContextNameContainsContext ctx) {
        this.cmSearchScope.setCmMatchCondition(CmMatchCondition.CONTAINS);
        this.cmSearchScope.setValue(ctx.id().getText());
    }

    @Override
    public void exitMeContextNameStartsWith(final MeContextNameStartsWithContext ctx) {
        this.cmSearchScope.setCmMatchCondition(CmMatchCondition.STARTS_WITH);
        this.cmSearchScope.setValue(ctx.id().getText());
    }

    @Override
    public void exitMeContextNameEndsWith(final MeContextNameEndsWithContext ctx) {
        this.cmSearchScope.setCmMatchCondition(CmMatchCondition.ENDS_WITH);
        this.cmSearchScope.setValue(ctx.id().getText());
    }

    /*
     * ATTRIBUTE - HANDLING
     */

    //ATTRIBUTE - CONTAINERS
    @Override
    public void enterAttributeSpecifications(final AttributeSpecificationsContext ctx) {
        final DataContainer dataContainer = new AttributeSpecificationDataContainer();
        if (this.dataContainerStack.isEmpty()) { // First setter (not a complex attribute)
            // Add the container to the result
            this.getCurrentCmObjectSpecification().setAttributeSpecificationContainer((AttributeSpecificationContainer) dataContainer.getContainer());
        }
        this.dataContainerStack.push(dataContainer);
    }

    @Override
    public void exitAttributeSpecifications(final AttributeSpecificationsContext ctx) {
        this.lastAttributeValue = this.dataContainerStack.pop().getContainer();
        // only set this when it is the last on the stack, if not it is a
        // complex attribute which is handled in its own exit method
    }

    @Override
    public void enterAttributeSetters(final AttributeSettersContext ctx) {
        final DataContainer dataContainer = new AttributeSpecificationDataContainer();
        // Add the container to the result
        this.parserResult.setAttributeSetters((StringifiedAttributeSpecifications) dataContainer.getContainer());
        this.dataContainerStack.push(dataContainer);
    }

    @Override
    public void enterAttributeFilters(final AttributeFiltersContext ctx) {
        final DataContainer dataContainer = new AttributeSpecificationDataContainer();
        // Add the container to the result
        this.getCurrentCmObjectSpecification().setAttributeSpecificationContainer((AttributeSpecificationContainer) dataContainer.getContainer());
        this.dataContainerStack.push(dataContainer);
    }

    @Override
    public void exitAttributeFilters(final AttributeFiltersContext ctx) {
        this.dataContainerStack.pop();
    }

    @Override
    public void exitAllAttributes(final AllAttributesContext ctx) {
        this.getCurrentCmObjectSpecification().setAttributeSpecificationContainer(CmObjectSpecification.ALL_ATTRIBUTES);
    }

    @Override
    public void exitAttributeSpecificationForComplex(@NotNull final AttributeSpecificationForComplexContext ctx) {
        this.addAttributeSpecification(ctx.attributeName().getText(), this.lastAttributeValue);
    }

    @Override
    public void enterSequenceAttributeValue(@NotNull final CmCommandParser.SequenceAttributeValueContext ctx) {
        final DataContainer dataContainer = new SequenceDataContainer();
        this.dataContainerStack.push(dataContainer);
    }

    @Override
    public void exitSequenceAttributeValue(@NotNull final CmCommandParser.SequenceAttributeValueContext ctx) {
        this.lastAttributeValue = this.dataContainerStack.pop().getContainer();
    }

    //ATTRIBUTES

    @Override
    public void exitAttributeIdValue(@NotNull final CmCommandParser.AttributeIdValueContext ctx) {
        this.lastAttributeValue = ctx.getText();
    }

    @Override
    public void exitAttributeStringValue(final AttributeStringValueContext ctx) {
        this.lastAttributeValue = ctx.string().getText();
    }

    @Override
    public void exitAttributeSetter(final AttributeSetterContext ctx) {
        this.addAttributeSpecification(ctx.attributeName().getText(), this.lastAttributeValue, CmMatchCondition.NO_MATCH_REQUIRED);
    }

    @Override
    public void exitAttributeSelector(final AttributeSelectorContext ctx) {
        this.addAttributeSpecification(ctx.attributeName().getText(), this.lastAttributeValue, CmMatchCondition.NO_MATCH_REQUIRED);
    }

    @Override
    public void exitAttributeValueInSequence(final AttributeValueInSequenceContext ctx) {
        this.addAttributeSpecification(this.lastAttributeValue);
    }

    @Override
    public void exitComplexAttributeValueInSequence(final ComplexAttributeValueInSequenceContext ctx) {
        this.addAttributeSpecification(this.lastAttributeValue);
    }

    // ATTRIBUTE - COMPARISONS

    @Override
    public void enterAttributeComparison(final AttributeComparisonContext ctx) {
        // Set default match condition
        this.cmMatchCondition = CmMatchCondition.EQUALS;
    }

    @Override
    public void exitStartsWithValue(final StartsWithValueContext ctx) {
        this.cmMatchCondition = CmMatchCondition.STARTS_WITH;
    }

    @Override
    public void exitEndsWithValue(final EndsWithValueContext ctx) {
        this.cmMatchCondition = CmMatchCondition.ENDS_WITH;
    }

    @Override
    public void exitContainsValue(final ContainsValueContext ctx) {
        this.cmMatchCondition = CmMatchCondition.CONTAINS;
    }

    @Override
    public void exitAttributeComparison(final AttributeComparisonContext ctx) {
        addAttributeSpecification(ctx.attributeName().getText(), this.lastAttributeValue, this.cmMatchCondition);
    }

    @Override
    public void exitAnyValue(final AnyValueContext ctx) {
        this.cmMatchCondition = CmMatchCondition.NO_MATCH_REQUIRED;
    }

    /*
     * NUMERIC - COMPARATORS
     */

    @Override
    public void exitLessThanValue(final LessThanValueContext ctx) {
        this.cmMatchCondition = CmMatchCondition.LESS_THAN;
    }

    @Override
    public void exitLessThanOrEqualToValue(final LessThanOrEqualToValueContext ctx) {
        this.cmMatchCondition = CmMatchCondition.LESS_THAN_OR_EQUAL_TO;
    }

    @Override
    public void exitGreaterThanValue(final GreaterThanValueContext ctx) {
        this.cmMatchCondition = CmMatchCondition.GREATER_THAN;
    }

    @Override
    public void exitGreaterThanOrEqualToValue(final GreaterThanOrEqualToValueContext ctx) {
        this.cmMatchCondition = CmMatchCondition.GREATER_THAN_OR_EQUAL_TO;
    }

    /*
     * OPTIONS
     */

    @Override
    public void exitNamespace(final NamespaceContext ctx) {
        this.getCurrentCmObjectSpecification().setNamespace(ctx.id().getText());
    }

    @Override
    public void exitVersion(final VersionContext ctx) {
        this.getCurrentCmObjectSpecification().setNamespaceVersion(ctx.versionValue().getText());
    }

    @Override
    public void exitAllFlag(final AllFlagContext ctx) {
        this.parserResult.setIncludeAllDescendants(true);
    }

    @Override
    public void exitTableOutput(final TableOutputContext ctx) {
        this.parserResult.setOutputTable(true);
    }

    /*
     * COMMAND - HANDLING
     */

    @Override
    public void enterCommand(final CommandContext ctx) {
        this.parserResult = new ParserResult();
    }

    @Override
    public void exitCreateConfigCommand(@NotNull final CmCommandParser.CreateConfigCommandContext ctx) {
        this.parserResult.setCommandType(CommandType.CREATE_CONFIG_COMMAND);
    }

    @Override
    public void exitListConfigCommand(@NotNull final CmCommandParser.ListConfigCommandContext ctx) {
        this.parserResult.setCommandType(CommandType.LIST_CONFIG_COMMAND);

    }

    @Override
    public void exitSetScopeConfigCommand(@NotNull final CmCommandParser.SetScopeConfigCommandContext ctx) {
        this.parserResult.setCommandType(CommandType.SET_SCOPE_CONFIG_COMMAND);

    }

    @Override
    public void exitUpdateConfigCommand(@NotNull final CmCommandParser.UpdateConfigCommandContext ctx) {
        this.parserResult.setCommandType(CommandType.UPDATE_CONFIG_COMMAND);

    }

    @Override
    public void exitDeleteConfigCommand(@NotNull final CmCommandParser.DeleteConfigCommandContext ctx) {
        this.parserResult.setCommandType(CommandType.DELETE_CONFIG_COMMAND);
    }

    @Override
    public void exitLockConfigCommand(@NotNull final CmCommandParser.LockConfigCommandContext ctx) {
        this.parserResult.setCommandType(CommandType.LOCK_CONFIG_COMMAND);
    }

    @Override
    public void exitUnlockConfigCommand(@NotNull final CmCommandParser.UnlockConfigCommandContext ctx) {
        this.parserResult.setCommandType(CommandType.UNLOCK_CONFIG_COMMAND);
    }

    @Override
    public void enterDiffConfigCommand(@NotNull final CmCommandParser.DiffConfigCommandContext ctx) {
        this.parserResult.setCommandType(CommandType.DIFF_CONFIG_COMMAND);
    }

    @Override
    public void exitLinkConfigCommand(@NotNull final CmCommandParser.LinkConfigCommandContext ctx) {
        this.parserResult.setCommandType(CommandType.LINK_CONFIG_COMMAND);
    }

    @Override
    public void exitUnlinkConfigCommand(@NotNull final CmCommandParser.UnlinkConfigCommandContext ctx) {
        this.parserResult.setCommandType(CommandType.UNLINK_CONFIG_COMMAND);
    }

    @Override
    public void exitConfigName(@NotNull final CmCommandParser.ConfigNameContext ctx) {
        this.parserResult.setConfigurationName(ctx.getText());

    }

    @Override
    public void exitListConfigOwnerOption(@NotNull final CmCommandParser.ListConfigOwnerOptionContext ctx) {
        this.parserResult.getListConfigOptions().setListConfigOwnerOption(true);
    }

    @Override
    public void exitConfigOwner(@NotNull final CmCommandParser.ConfigOwnerContext ctx) {
        this.parserResult.getListConfigOptions().setConfigOwner(ctx.getText());
    }

    @Override
    public void exitListConfigAllOption(@NotNull final CmCommandParser.ListConfigAllOptionContext ctx) {
        this.parserResult.getListConfigOptions().setListConfigAllOption(true);
    }

    @Override
    public void exitFromConfigName(@NotNull final CmCommandParser.FromConfigNameContext ctx) {
        this.parserResult.setSourceConfigurationName(ctx.getText());
    }

    @Override
    public void exitUpdateConfigCreateOption(@NotNull final CmCommandParser.UpdateConfigCreateOptionContext ctx) {
        this.parserResult.getUpdateConfigOptions().setUpdateConfigCreateOption(true);
    }

    @Override
    public void exitUpdateConfigDeleteOption(@NotNull final CmCommandParser.UpdateConfigDeleteOptionContext ctx) {
        this.parserResult.getUpdateConfigOptions().setUpdateConfigDeleteOption(true);
    }

    @Override
    public void exitUpdateConfigOverwriteOption(@NotNull final CmCommandParser.UpdateConfigOverwriteOptionContext ctx) {
        this.parserResult.getUpdateConfigOptions().setUpdateConfigOverwriteOption(true);
    }

    @Override
    public void exitReasonMessageString(@NotNull final CmCommandParser.ReasonMessageStringContext ctx) {
        this.parserResult.setLockReasonMessage((String) this.lastAttributeValue);
    }

    @Override
    public void exitDiffConfigImportOption(@NotNull final CmCommandParser.DiffConfigImportOptionContext ctx) {
        this.parserResult.getDiffConfigOptions().setDiffConfigImportOption(true);
    }

    @Override
    public void exitDiffConfigDeepOption(@NotNull final CmCommandParser.DiffConfigDeepOptionContext ctx) {
        this.parserResult.getDiffConfigOptions().setDiffConfigDeepOption(true);
    }

    @Override
    public void exitDiffConfigUpdateOption(@NotNull final CmCommandParser.DiffConfigUpdateOptionContext ctx) {
        this.parserResult.getDiffConfigOptions().setDiffConfigUpdateOption(true);
    }

    @Override
    public void exitDiffConfigName(@NotNull final CmCommandParser.DiffConfigNameContext ctx) {
        this.parserResult.getDiffConfigOptions().setDiffConfigUpdateName(ctx.getText());
    }

    @Override
    public void exitBaseConfigName(@NotNull final CmCommandParser.BaseConfigNameContext ctx) {
        this.parserResult.setBaseConfigurationName(ctx.getText());
    }

    @Override
    public void exitCommand(final CommandContext ctx) {
        if (this.parserResult.getStatusCode() == STATUS_FAIL) {
            return;
        }

        if (this.parserResult.getCmObjectSpecifications() == null) {
            // To avoid null checks in cm-editor Parser will at least always
            // return empty collection
            final List<CmObjectSpecification> noCmObjectSpecifications = new ArrayList<>(0);
            this.parserResult.setCmObjectSpecifications(noCmObjectSpecifications);
        }
        if (this.cmScopeList.isEmpty()) {
            this.cmScopeList.add(this.cmSearchScope);
        }
        this.parserResult.setCmSearchScopes(this.cmScopeList);
        this.parserResult.setStatusCode(this.STATUS_SUCCESS);
    }

    /*
     * PRIVATE - METHODS
     */

    private void handleNewCmObjectSpecification() {
        final CmObjectSpecification newCmObjectSpecification = new CmObjectSpecification();
        // For cm-editor language the default is NO attributes
        newCmObjectSpecification.setAttributeSpecificationContainer(new StringifiedAttributeSpecifications());
        this.cmObjectSpecificationStack.push(newCmObjectSpecification);
    }

    private CmObjectSpecification getCurrentCmObjectSpecification() {
        if (this.cmObjectSpecificationStack.isEmpty()) {
            // Currently only 1 CmObjectSpecification supported
            return this.parserResult.getCmObjectSpecifications().get(0);
        }
        // If there is something on the stack then that is the 'current'
        // CmObjectSpecification
        return this.cmObjectSpecificationStack.peek();
    }

    private void createFirsAttributeSpecification() {
        final DataContainer dataContainer = new AttributeSpecificationDataContainer();
        // Add the container to the result
        this.getCurrentCmObjectSpecification().setAttributeSpecificationContainer((AttributeSpecificationContainer) dataContainer.getContainer());
        this.dataContainerStack.push(dataContainer);
    }

    private void addAttributeSpecification(final String name, final Object value, final CmMatchCondition cmMatchCondition) {
        if (this.dataContainerStack.isEmpty()) {
            /*
             * It is possible to have 1 single attribute specification that is
             * not in any container<br> in that case we need to create a
             * container here<br>
             * 
             * This happens for instance for this command
             * "get * EnodeBFunction.userLabel"
             */
            this.createFirsAttributeSpecification();
        }

        final DataContainer dataContainer = this.dataContainerStack.peek();
        dataContainer.addValue(name, value, cmMatchCondition);
    }

    private void addAttributeSpecification(final String name, final Object value) {
        this.addAttributeSpecification(name, value, CmMatchCondition.NO_MATCH_REQUIRED);
    }

    private void addAttributeSpecification(final Object value) {
        this.addAttributeSpecification(null, value, CmMatchCondition.NO_MATCH_REQUIRED);
    }

}
