package com.ericsson.oss.services.cm.cmparser.test.builders;

import java.util.Arrays;
import java.util.List;

import com.ericsson.oss.services.cm.cmparser.dto.ParserResult;
import com.ericsson.oss.services.cm.cmshared.dto.ActionSpecification;
import com.ericsson.oss.services.cm.cmshared.dto.CmObjectSpecification;
import com.ericsson.oss.services.cm.cmshared.dto.StringifiedAttributeSpecifications;
import com.ericsson.oss.services.cm.cmshared.dto.search.CmSearchScope;

public class ParserResultBuilder {

    private int statusCode;
    private ParserResult.CommandType commandType;
    private List<CmSearchScope> setCmSearchScopes;
    private List<CmObjectSpecification> cmObjectSpecifications;
    private ActionSpecification actionSpecification;
    private StringifiedAttributeSpecifications attributeSetters;
    private boolean includeAllDescendants;
    private String configurationName;

    public ParserResultBuilder() {
        // defaults for a ParserResult
        this.statusCode = 1;
        // Default Command type
        this.commandType = ParserResult.CommandType.CREATE_CONFIG_COMMAND;

    }

    public ParserResultBuilder withCommandType(final ParserResult.CommandType commandType) {
        this.commandType = commandType;
        return this;
    }

    public ParserResultBuilder withStatusCode(final int statusCode) {
        this.statusCode = statusCode;
        return this;
    }

    public ParserResultBuilder withCmSearchScopes(final CmSearchScope... cmSearchScopes) {
        this.setCmSearchScopes = Arrays.asList(cmSearchScopes);
        return this;
    }

    public ParserResultBuilder withCmSearchScopes(final List<CmSearchScope> cmSearchScopes) {
        this.setCmSearchScopes = cmSearchScopes;
        return this;
    }

    public ParserResultBuilder withCmObjectSpecifications(final CmObjectSpecification... cmObjectSpecifications) {
        this.cmObjectSpecifications = Arrays.asList(cmObjectSpecifications);
        return this;
    }

    public ParserResultBuilder withCmObjectSpecifications(final List<CmObjectSpecification> cmObjectSpecifications) {
        this.cmObjectSpecifications = cmObjectSpecifications;
        return this;
    }

    public ParserResultBuilder withActionSpecification(final ActionSpecification actionSpecification) {
        this.actionSpecification = actionSpecification;
        return this;
    }

    public ParserResultBuilder withAttributeSetters(final StringifiedAttributeSpecifications attributeSetters) {
        this.attributeSetters = attributeSetters;
        return this;
    }

    public ParserResultBuilder withIncludeAllDescendants(final boolean includeAllDescendants) {
        this.includeAllDescendants = includeAllDescendants;
        return this;
    }

    public ParserResultBuilder withConfigurationName(final String configurationName) {
        this.configurationName = configurationName;
        return this;
    }

    public ParserResult build() {
        final ParserResult parserResult = new ParserResult();
        parserResult.setStatusCode(statusCode);
        parserResult.setCommandType(this.commandType);
        parserResult.setCmObjectSpecifications(this.cmObjectSpecifications);
        parserResult.setCmSearchScopes(this.setCmSearchScopes);
        parserResult.setActionSpecification(this.actionSpecification);
        parserResult.setAttributeSetters(attributeSetters);
        parserResult.setIncludeAllDescendants(includeAllDescendants);
        parserResult.setConfigurationName(this.configurationName);
        return parserResult;
    }
}
