package com.ericsson.oss.services.cm.cmconfig.service.validation;

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
import static com.ericsson.oss.services.cm.cmconfig.service.api.CmConfigConstants.DPS_LOCAL_JNDI;
import static com.ericsson.oss.services.cm.cmconfig.service.log.CmConfigServiceMessages.CONFIGURATION_PARAM_DOES_NOT_EXIST_ERROR;
import static com.ericsson.oss.services.cm.cmconfig.service.log.CmConfigServiceMessages.CONFIG_COPY_NODES_NOT_SPECIFIED_MESSAGE;
import static com.ericsson.oss.services.cm.cmconfig.service.log.CmConfigServiceMessages.CONFIG_COPY_NODES_SCOPE_NOT_CORRECT;
import static com.ericsson.oss.services.cm.cmconfig.service.log.CmConfigServiceMessages.CONFIG_PARAM_IS_NOT_NON_LIVE_ERROR;
import static com.ericsson.oss.services.cm.cmconfig.service.log.CmConfigServiceMessages.DIFF_PARAM_IS_INVALID;
import static com.ericsson.oss.services.cm.cmconfig.service.log.CmConfigServiceMessages.DIFF_SEARCH_CRITERIA_IS_INVALID;
import static com.ericsson.oss.services.cm.cmconfig.service.log.CmConfigServiceMessages.SOLUTION_FOR_CONFIG_COPY_NODES_NOT_SPECIFIED;
import static com.ericsson.oss.services.cm.cmconfig.service.log.CmConfigServiceMessages.SOLUTION_FOR_CONFIG_INVALID;
import static com.ericsson.oss.services.cm.cmconfig.service.log.CmConfigServiceMessages.SOLUTION_FOR_DIFF_PARAM_IS_INVALID;
import static com.ericsson.oss.services.cm.cmconfig.service.log.CmConfigServiceMessages.SOLUTION_FOR_INVALID_SCOPE;

import java.text.MessageFormat;
import java.util.List;

import javax.ejb.EJB;
import javax.enterprise.inject.Default;

import com.ericsson.oss.itpf.datalayer.dps.DataBucket;
import com.ericsson.oss.itpf.datalayer.dps.DataPersistenceService;
import com.ericsson.oss.itpf.datalayer.dps.NonLiveDataBucket;
import com.ericsson.oss.itpf.datalayer.dps.exception.general.ObjectNotFoundException;
import com.ericsson.oss.services.cm.cmconfig.service.api.CmConfigDiffParams;
import com.ericsson.oss.services.cm.cmshared.dto.search.CmSearchCriteria;
import com.ericsson.oss.services.cm.cmshared.dto.search.CmSearchScope;
import com.ericsson.oss.services.cm.cmshared.dto.search.CmSearchScope.ScopeType;

/**
 * {@link ValidationConfigService} implementation.
 */
@Default
public class ValidationConfigServiceBean implements ValidationConfigService {

    @EJB(lookup = DPS_LOCAL_JNDI)
    private DataPersistenceService dataPersistenceService;

    @Override
    public DataBucket validateConfig(final String config) {
        try {
            final DataBucket bucket = this.dataPersistenceService.getDataBucket(config);
            return bucket;
        } catch (final ObjectNotFoundException objectNotFoundException) {
            throw createValidationException(CONFIGURATION_PARAM_DOES_NOT_EXIST_ERROR, SOLUTION_FOR_CONFIG_INVALID, config);
        }
    }

    @Override
    public void validateNodeSpecificationIsNotEmpty(final List<CmSearchScope> listCmSearchScope) {
        if (listCmSearchScope.isEmpty()) {
            throw createValidationException(CONFIG_COPY_NODES_NOT_SPECIFIED_MESSAGE, SOLUTION_FOR_CONFIG_COPY_NODES_NOT_SPECIFIED);
        }
    }

    @Override
    public void validateScopeIsNodeNameOrUnspecified(final List<CmSearchScope> listCmSearchScope) {

        for (final CmSearchScope cmSearchScope : listCmSearchScope) {
            if (cmSearchScope.getScopeType() != ScopeType.NODE_NAME && cmSearchScope.getScopeType() != ScopeType.UNSPECIFIED) {
                throw createValidationException(CONFIG_COPY_NODES_SCOPE_NOT_CORRECT, SOLUTION_FOR_INVALID_SCOPE);
            }
        }
    }

    @Override
    public ValidationException createValidationException(final String message, final String suggestedSolution, final Object... additionalInfo) {
        final MessageFormat messageFormat = new MessageFormat(message);
        return new ValidationException(messageFormat.format(additionalInfo), suggestedSolution);
    }

    @Override
    public NonLiveDataBucket validateNonLiveConfig(final String config) {
        final DataBucket bucket = validateConfig(config);
        if (!bucket.isLiveBucket()) {
            return (NonLiveDataBucket) bucket;
        } else {
            throw createValidationException(CONFIG_PARAM_IS_NOT_NON_LIVE_ERROR, SOLUTION_FOR_CONFIG_INVALID, config);
        }
    }

    @Override
    public void validateCmConfigDiffParams(final CmConfigDiffParams cmConfigDiffParams) {
        if (cmConfigDiffParams == null) {
            throw createValidationException(DIFF_PARAM_IS_INVALID, SOLUTION_FOR_DIFF_PARAM_IS_INVALID);
        }
    }

    @Override
    public void validateCmSearchCriteria(final CmSearchCriteria cmSearchCriteria) {
        if (cmSearchCriteria == null) {
            throw createValidationException(DIFF_SEARCH_CRITERIA_IS_INVALID, SOLUTION_FOR_DIFF_PARAM_IS_INVALID);
        }
    }
}
