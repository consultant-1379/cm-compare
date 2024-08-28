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
package com.ericsson.oss.services.cm.cmparser.dto;

import java.util.Arrays;
import java.util.List;

import com.ericsson.oss.services.cm.cmshared.dto.ActionSpecification;
import com.ericsson.oss.services.cm.cmshared.dto.CmObjectSpecification;
import com.ericsson.oss.services.cm.cmshared.dto.StringifiedAttributeSpecifications;
import com.ericsson.oss.services.cm.cmshared.dto.search.CmSearchScope;

/**
 * 
 * This class represent a parse result for all kind of cm config commands.
 * Depending on the CLI commands, some members will be null.
 * 
 */
public class ParserResult {
    /**
     * 
     * cm config command.
     * 
     */
    public enum CommandType {

        PARSER_ERROR,

        CREATE_CONFIG_COMMAND,

        LIST_CONFIG_COMMAND,

        SET_SCOPE_CONFIG_COMMAND,

        UPDATE_CONFIG_COMMAND,

        DELETE_CONFIG_COMMAND,

        LOCK_CONFIG_COMMAND,

        UNLOCK_CONFIG_COMMAND,

        DIFF_CONFIG_COMMAND,

        LINK_CONFIG_COMMAND,

        UNLINK_CONFIG_COMMAND

    };

    private CommandType commandType;
    private String statusMessage;
    private int statusCode;
    private List<CmSearchScope> cmSearchScopes;
    private List<CmObjectSpecification> cmObjectSpecifications;
    private StringifiedAttributeSpecifications attributeSetters;
    private ActionSpecification actionSpecification;
    private boolean includeAllDescendants;
    private boolean outputTable;
    private String configurationName;
    private String sourceConfigurationName;
    private String baseConfigurationName;
    private final UpdateConfigOptions updateConfigOptions = new UpdateConfigOptions();
    private final DiffConfigOptions diffConfigOptions = new DiffConfigOptions();
    private final ListConfigOptions listConfigOptions = new ListConfigOptions();
    private String lockReasonMessage;

    public CommandType getCommandType() {
        return this.commandType;
    }

    public String getStatusMessage() {
        return this.statusMessage;
    }

    public int getStatusCode() {
        return this.statusCode;
    }

    public List<CmSearchScope> getCmScopes() {
        return this.cmSearchScopes;
    }

    public void setCmSearchScopes(final List<CmSearchScope> cmScopes) {
        this.cmSearchScopes = cmScopes;
    }

    public void setCmSearchScopes(final CmSearchScope... cmSearchScopes) {
        this.cmSearchScopes = Arrays.asList(cmSearchScopes);
    }

    public List<CmObjectSpecification> getCmObjectSpecifications() {
        return this.cmObjectSpecifications;
    }

    public void setCmObjectSpecifications(final List<CmObjectSpecification> cmObjectSpecifications) {
        this.cmObjectSpecifications = cmObjectSpecifications;
    }

    public void setCmObjectSpecifications(final CmObjectSpecification... cmObjectSpecifications) {
        this.cmObjectSpecifications = Arrays.asList(cmObjectSpecifications);
    }

    public void setCommandType(final CommandType commandType) {
        this.commandType = commandType;
    }

    public void setStatusMessage(final String statusMessage) {
        this.statusMessage = statusMessage;
    }

    public void setStatusCode(final int statusCode) {
        this.statusCode = statusCode;
    }

    /**
     * 
     * @return includeAllDescendants
     */
    public boolean includeAllDescendants() {
        return this.includeAllDescendants;
    }

    public void setIncludeAllDescendants(final boolean includeAllDescendants) {
        this.includeAllDescendants = includeAllDescendants;
    }

    /**
     * @return the attributeSetters
     */
    public StringifiedAttributeSpecifications getAttributeSetters() {
        return this.attributeSetters;
    }

    /**
     * @param attributeSetters
     *            the attributeSetters to set
     */
    public void setAttributeSetters(final StringifiedAttributeSpecifications attributeSetters) {
        this.attributeSetters = attributeSetters;
    }

    public ActionSpecification getActionSpecification() {
        return this.actionSpecification;
    }

    /**
     * 
     * @param actionSpecification
     *            the actionScpecification to set
     */
    public void setActionSpecification(final ActionSpecification actionSpecification) {
        this.actionSpecification = actionSpecification;
    }

    /**
     * 
     * @return the outputTable
     */
    public boolean outputTable() {
        return this.outputTable;
    }

    public void setOutputTable(final boolean outputTable) {
        this.outputTable = outputTable;
    }

    /**
     * @return the configurationName
     */
    public String getConfigurationName() {
        return this.configurationName;
    }

    /**
     * @param configurationName
     *            the configurationName to set
     */
    public void setConfigurationName(final String configurationName) {
        this.configurationName = configurationName;
    }

    /**
     * @return the sourceConfigurationName
     */
    public String getSourceConfigurationName() {
        return this.sourceConfigurationName;
    }

    /**
     * @param sourceConfigurationName
     *            the sourceConfigurationName to set
     */
    public void setSourceConfigurationName(final String sourceConfigurationName) {
        this.sourceConfigurationName = sourceConfigurationName;
    }

    /**
     * @return the lockReasonMessage
     */
    public String getLockReasonMessage() {
        return this.lockReasonMessage;
    }

    /**
     * @param lockReasonMessage
     *            the lockReasonMessage to set
     */
    public void setLockReasonMessage(final String lockReasonMessage) {
        this.lockReasonMessage = lockReasonMessage;
    }

    /**
     * @return the updateConfigOptions
     */
    public UpdateConfigOptions getUpdateConfigOptions() {
        return this.updateConfigOptions;
    }

    /**
     * @return the diffConfigOptions
     */
    public DiffConfigOptions getDiffConfigOptions() {
        return this.diffConfigOptions;
    }

    /**
     * @return the listConfigOptions
     */
    public ListConfigOptions getListConfigOptions() {
        return this.listConfigOptions;
    }

    /**
     * @return the baseConfigurationName
     */
    public String getBaseConfigurationName() {
        return this.baseConfigurationName;
    }

    /**
     * @param baseConfigurationName
     *            the baseConfigurationName to set
     */
    public void setBaseConfigurationName(final String baseConfigurationName) {
        this.baseConfigurationName = baseConfigurationName;
    }

}
