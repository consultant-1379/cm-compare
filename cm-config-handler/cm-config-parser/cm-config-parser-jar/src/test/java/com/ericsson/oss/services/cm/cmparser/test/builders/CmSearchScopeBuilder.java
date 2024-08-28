package com.ericsson.oss.services.cm.cmparser.test.builders;

import com.ericsson.oss.services.cm.cmshared.dto.search.CmMatchCondition;
import com.ericsson.oss.services.cm.cmshared.dto.search.CmSearchScope;

public class CmSearchScopeBuilder {

    private String name;
    private String value;
    private String neTypeFilter;
    private CmMatchCondition cmMatchCondition;
    private CmSearchScope.ScopeType scopeType;

    public CmSearchScopeBuilder() {
        // defaults for a search scope
        this.name = "MeContext";
        this.value = "ERBS01";
        this.scopeType = CmSearchScope.ScopeType.NODE_NAME;
        this.cmMatchCondition = CmMatchCondition.EQUALS;
    }

    public CmSearchScopeBuilder withName(final String name) {
        this.name = name;
        return this;
    }

    public CmSearchScopeBuilder withValue(final String value) {
        this.value = value;
        return this;
    }

    public CmSearchScopeBuilder withMatchCondition(final CmMatchCondition cmMatchCondition) {
        this.cmMatchCondition = cmMatchCondition;
        return this;
    }

    public CmSearchScopeBuilder withScopeType(final CmSearchScope.ScopeType scopeType) {
        this.scopeType = scopeType;
        return this;
    }

    public CmSearchScopeBuilder withNeTypeFilter(final String neTypeFilter) {
        this.neTypeFilter = neTypeFilter;
        return this;
    }

    public CmSearchScope build() {
        final CmSearchScope cmSearchScope = new CmSearchScope();
        cmSearchScope.setName(this.name);
        cmSearchScope.setValue(this.value);
        cmSearchScope.setScopeType(this.scopeType);
        cmSearchScope.setCmMatchCondition(this.cmMatchCondition);
        cmSearchScope.setNeTypeFilter(this.neTypeFilter);
        return cmSearchScope;
    }
}
