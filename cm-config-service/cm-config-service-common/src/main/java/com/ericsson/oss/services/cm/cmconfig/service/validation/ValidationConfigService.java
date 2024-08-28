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
package com.ericsson.oss.services.cm.cmconfig.service.validation;

import java.util.List;

import com.ericsson.oss.itpf.datalayer.dps.DataBucket;
import com.ericsson.oss.itpf.datalayer.dps.NonLiveDataBucket;
import com.ericsson.oss.services.cm.cmconfig.service.api.CmConfigDiffParams;
import com.ericsson.oss.services.cm.cmshared.dto.search.CmSearchCriteria;
import com.ericsson.oss.services.cm.cmshared.dto.search.CmSearchScope;

/**
 * Set of utilities to validate parameters used to DPS calls.
 */
public interface ValidationConfigService {
    /**
     * Validates and retrieves the {@code DataBucket} specified by name.
     *
     * @param config
     *            the configuration name
     * @return the configuration if already exists
     */
    DataBucket validateConfig(String config);

    /**
     * Validates if the list of node specification is empty.
     *
     * @param listCmSearchScope
     *            the list of node specification
     */
    void validateNodeSpecificationIsNotEmpty(List<CmSearchScope> listCmSearchScope);

    /**
     * Validates if the scope is node name or unspecified.
     *
     * @param listCmSearchScope
     *            the list of node specification
     */
    void validateScopeIsNodeNameOrUnspecified(List<CmSearchScope> listCmSearchScope);

    /**
     * Validates and retrieves the {@code NonLiveDataBucket} specified by name.
     *
     * @param config
     *            the configuration name
     * @return the configuration if already exists and it is a non-live
     *         configuration
     */
    NonLiveDataBucket validateNonLiveConfig(String config);

    /**
     * Create a {@code ValidationException} with a message and some additional
     * information.
     * This method can be used if a service needs to map an exception to a {@code ValidationException}
     *
     * @param message
     *            the message of the exception
     * @param suggestedSolution
     *            the suggested solution
     * @param additionalInfo
     *            some additional information to fill the message template
     * @return a ValidationException
     */
    ValidationException createValidationException(String message, String suggestedSolution, Object... additionalInfo);

    /**
     * Validates if the CmConfigDiffParams is not null.
     *
     * @param cmConfigDiffParams
     *            cmConfigDiffParams
     */
    void validateCmConfigDiffParams(CmConfigDiffParams cmConfigDiffParams);

    /**
     * Validates if the CmSearchCriteria is not null.
     *
     * @param cmSearchCriteria
     *            cmSearchCriteria
     */
    void validateCmSearchCriteria(CmSearchCriteria cmSearchCriteria);

}
