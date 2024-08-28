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
package com.ericsson.oss.services.cm.cmconfig.service.ejb.copy;

import static com.ericsson.oss.services.cm.cmconfig.service.log.CmConfigServiceMessages.MANAGED_OBJECT_ALREADY_EXISTS;
import static com.ericsson.oss.services.cm.cmconfig.service.log.CmConfigServiceMessages.MANAGED_OBJECT_COPY_ERROR;
import static com.ericsson.oss.services.cm.cmconfig.service.log.CmConfigServiceMessages.MANAGED_OBJECT_DELETED_OR_READ_ONLY_BUCKET;
import static com.ericsson.oss.services.cm.cmconfig.service.log.CmConfigServiceMessages.MANAGED_OBJECT_IS_NOT_TOP_ROOT_ERROR;
import static com.ericsson.oss.services.cm.cmconfig.service.log.CmConfigServiceMessages.SOLUTION_FOR_GENERIC_EXCEPTION;
import static com.ericsson.oss.services.cm.cmconfig.service.log.CmConfigServiceMessages.SOLUTION_FOR_MANAGED_OBJECT_ERROR;
import static com.ericsson.oss.services.cm.log.ErrorHandler.EXECUTION_ERROR;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.inject.Inject;

import com.ericsson.oss.itpf.datalayer.dps.DataBucket;
import com.ericsson.oss.itpf.datalayer.dps.NonLiveDataBucket;
import com.ericsson.oss.itpf.datalayer.dps.exception.DpsTransactionRollbackException;
import com.ericsson.oss.itpf.datalayer.dps.exception.general.AlreadyDefinedException;
import com.ericsson.oss.itpf.datalayer.dps.exception.general.DpsIllegalArgumentException;
import com.ericsson.oss.itpf.datalayer.dps.exception.general.DpsIllegalStateException;
import com.ericsson.oss.itpf.datalayer.dps.persistence.ManagedObject;
import com.ericsson.oss.services.cm.cmconfig.service.api.CmCopyNodesFlags;
import com.ericsson.oss.services.cm.cmconfig.service.api.CmCopyNodesFlags.CmConfigCopy;
import com.ericsson.oss.services.cm.cmconfig.service.api.CmCopyResponse;
import com.ericsson.oss.services.cm.cmconfig.service.api.CopyResult;
import com.ericsson.oss.services.cm.cmconfig.service.api.CopyResult.CopyStatus;
import com.ericsson.oss.services.cm.cmconfig.service.ejb.util.CmConfigDpsUtil;
import com.ericsson.oss.services.cm.cmconfig.service.exception.handling.CmConfigServiceExceptionHandler;
import com.ericsson.oss.services.cm.cmconfig.service.log.CmConfigServiceLog;
import com.ericsson.oss.services.cm.cmconfig.service.log.CmConfigServiceMessages;
import com.ericsson.oss.services.cm.cmconfig.service.validation.ValidationConfigService;
import com.ericsson.oss.services.cm.cmconfig.service.validation.ValidationException;
import com.ericsson.oss.services.cm.cmshared.dto.search.CmMatchCondition;
import com.ericsson.oss.services.cm.cmshared.dto.search.CmSearchCriteria;
import com.ericsson.oss.services.cm.cmshared.dto.search.CmSearchScope;
import com.ericsson.oss.services.cm.cmshared.dto.search.CmSearchScope.ScopeType;

/**
 * Provides the ability to copy nodes from one configuration to another.
 */
public final class CopyNodesBean implements CopyNodes {

    private static final String STAR_WILDCARD = "*";
    private static final String WITHOUT_FDN = "";

    /** Validation service. */
    @Inject
    private ValidationConfigService validationConfigService;

    @Inject
    private CmConfigServiceExceptionHandler exceptionHandler;

    @Inject
    private CmConfigServiceLog logger;

    @Inject
    private CmConfigDpsUtil dpsUtil;

    @SuppressWarnings("PMD.AvoidCatchingThrowable")
    @Override
    public CmCopyResponse copyNodes(final String sourceConfigName, final String targetConfigName, final CmSearchCriteria nodesScope,
            final CmCopyNodesFlags copyFlags) {

        final CmCopyResponse returnCmResponse = new CmCopyResponse();

        try {
            final DataBucket sourceConfig = this.validationConfigService.validateConfig(sourceConfigName);

            final NonLiveDataBucket targetConfig = this.validationConfigService.validateNonLiveConfig(targetConfigName);

            final List<CmSearchScope> listCmSearchScope = nodesScope.getCmSearchScopes();

            // Check if the list of node specification is empty
            this.validationConfigService.validateNodeSpecificationIsNotEmpty(listCmSearchScope);

            // Check if the scope is NODE_NAME or UNSPECIFIED
            this.validationConfigService.validateScopeIsNodeNameOrUnspecified(listCmSearchScope);

            final List<CopyResult> copyResults = processCopyNodes(sourceConfig, targetConfig, listCmSearchScope, copyFlags);
            returnCmResponse.setCopyResults(copyResults);
            returnCmResponse.setStatusCode(copyResults.size());

            returnCmResponse.setStatusMessage(CmConfigServiceMessages.CONFIG_COPY_COMPLETED_MESSAGE);

        } catch (final ValidationException validationException) {
            this.exceptionHandler.handleValidationException(returnCmResponse, validationException.getMessage(),
                    validationException.getSuggestedSolution(), CopyNodesBean.class.getName(), EXECUTION_ERROR);
        } catch (final Throwable throwable) {
            this.exceptionHandler.handleError(returnCmResponse, throwable, SOLUTION_FOR_GENERIC_EXCEPTION, EXECUTION_ERROR);
        }
        this.logger.logCmCommand(CmConfigServiceMessages.COPY_NODES, returnCmResponse, sourceConfigName, targetConfigName, nodesScope, copyFlags);
        return returnCmResponse;
    }

    /**
     * Copies nodes from one configuration to another. The nodes to be copied
     * are specified as the node names in the {@link CmSearchScope}. Copy Flags
     * are supplied via {@link CmCopyNodesFlags}.
     *
     * @param sourceConfig
     *            the configuration to get the nodes from
     * @param targetConfig
     *            the configuration to copy the nodes into
     * @param listCmSearchScope
     *            the list of search scopes which define the node names
     * @param copyFlags
     *            the flags to set how the copy will be performed
     * @return list of copy results, each containing the result of copying a
     *         node
     */
    public List<CopyResult> processCopyNodes(final DataBucket sourceConfig, final NonLiveDataBucket targetConfig,
            final List<CmSearchScope> listCmSearchScope, final CmCopyNodesFlags copyFlags) {
        final List<CopyResult> copyResults = new ArrayList<>();

        final Set<ManagedObject> nodesFound = new HashSet<>();
        for (final CmSearchScope cmSearchScope : listCmSearchScope) {
            final List<ManagedObject> moSourceList = this.dpsUtil.findNesFromSearchScope(sourceConfig, cmSearchScope);
            if (moSourceList.isEmpty()) {
                copyResults.add(getEmptyCopyResult(cmSearchScope));
            } else {
                nodesFound.addAll(moSourceList);
            }
        }
        for (final ManagedObject node : nodesFound) {
            copyResults.add(copyNode(targetConfig, node, copyFlags));
        }
        return copyResults;
    }

    /**
     * Generates a CopyResult to indicate the nodes specified in the
     * CmSearchScope don't exist in the source configuration.
     *
     * @param cmSearchScope
     *            with the nodes specified.
     * @return CopyResult with different node name depending on the scope
     *         specified (NODE_NAME, START_WITH,etc.)
     */
    private CopyResult getEmptyCopyResult(final CmSearchScope cmSearchScope) {
        CopyResult copyResult = null;
        if (cmSearchScope.getScopeType() == ScopeType.NODE_NAME) {
            if (cmSearchScope.getCmMatchCondition() == CmMatchCondition.EQUALS) {
                copyResult = new CopyResult(cmSearchScope.getValue(), this.dpsUtil.getFDN(cmSearchScope));
                copyResult.setCopyStatus(CopyStatus.NOT_COPIED);
                copyResult.setStatusMessage(CmConfigServiceMessages.CONFIG_COPY_NODE_DOES_NOT_EXIST_IN_SOURCE);
            } else if (cmSearchScope.getCmMatchCondition() == CmMatchCondition.STARTS_WITH) {
                copyResult = new CopyResult(cmSearchScope.getValue() + STAR_WILDCARD, WITHOUT_FDN);
                copyResult.setCopyStatus(CopyStatus.NO_MATCHES_FOUND);
            } else if (cmSearchScope.getCmMatchCondition() == CmMatchCondition.ENDS_WITH) {
                copyResult = new CopyResult(STAR_WILDCARD + cmSearchScope.getValue(), WITHOUT_FDN);
                copyResult.setCopyStatus(CopyStatus.NO_MATCHES_FOUND);
            } else if (cmSearchScope.getCmMatchCondition() == CmMatchCondition.CONTAINS) {
                copyResult = new CopyResult(STAR_WILDCARD + cmSearchScope.getValue() + STAR_WILDCARD, WITHOUT_FDN);
                copyResult.setCopyStatus(CopyStatus.NO_MATCHES_FOUND);
            }
        } else if (cmSearchScope.getScopeType() == ScopeType.UNSPECIFIED) {
            copyResult = new CopyResult(STAR_WILDCARD, WITHOUT_FDN);
            copyResult.setCopyStatus(CopyStatus.NO_MATCHES_FOUND);
        }
        return copyResult;
    }

    private CopyResult copyNode(final NonLiveDataBucket targetConfig, final ManagedObject moSource, final CmCopyNodesFlags copyFlags) {
        final CopyResult copyResult = new CopyResult(moSource.getName(), moSource.getFdn());

        // Check if the MO exists in the target configuration
        final ManagedObject moTarget = this.dpsUtil.getManagedElementByFdn(moSource.getFdn(), targetConfig);
        if (moTarget != null) {
            if (copyFlags.getCopyFlags().contains(CmConfigCopy.OVERWRITE)) {
                this.dpsUtil.deleteManagedElementByMoReference(moTarget, targetConfig);
                copyResult.setCopyStatus(CopyStatus.OVERWRITEN);
            } else {
                copyResult.setCopyStatus(CopyStatus.NOT_COPIED);
                copyResult.setStatusMessage(CmConfigServiceMessages.CONFIG_COPY_NODE_EXIST_IN_TARGET);
                return copyResult;
            }
        }
        // TODO This version use copy one by one.
        // But it is possible to implement a copy several nodes in one step if the performance is not good (We have no idea yet)
        copyNodeInTarget(targetConfig, moSource);
        if (copyResult.getCopyStatus() == null) {
            copyResult.setCopyStatus(CopyStatus.COPIED);
        }
        return copyResult;
    }

    private void copyNodeInTarget(final NonLiveDataBucket targetConfig, final ManagedObject moSource) {
        try {
            targetConfig.copyInFullTree(moSource);
        } catch (final DpsIllegalArgumentException ex) {
            throw this.validationConfigService.createValidationException(MANAGED_OBJECT_IS_NOT_TOP_ROOT_ERROR, SOLUTION_FOR_MANAGED_OBJECT_ERROR,
                    moSource.getName());
        } catch (final AlreadyDefinedException ex) {
            throw this.validationConfigService.createValidationException(MANAGED_OBJECT_ALREADY_EXISTS, SOLUTION_FOR_MANAGED_OBJECT_ERROR,
                    moSource.getName());
        } catch (final DpsIllegalStateException ex) {
            throw this.validationConfigService.createValidationException(MANAGED_OBJECT_DELETED_OR_READ_ONLY_BUCKET,
                    SOLUTION_FOR_MANAGED_OBJECT_ERROR, moSource.getName());
        } catch (final DpsTransactionRollbackException ex) {
            throw this.validationConfigService.createValidationException(MANAGED_OBJECT_COPY_ERROR, SOLUTION_FOR_MANAGED_OBJECT_ERROR,
                    moSource.getName());
        }
    }
}
