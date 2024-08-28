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
package com.ericsson.oss.services.cm.cmconfig.service.ejb.diff;

import static com.ericsson.oss.services.cm.cmconfig.service.log.CmConfigServiceMessages.SOLUTION_FOR_GENERIC_EXCEPTION;
import static com.ericsson.oss.services.cm.log.ErrorHandler.EXECUTION_ERROR;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.ejb.Stateless;
import javax.inject.Inject;

import com.ericsson.oss.itpf.datalayer.dps.DataBucket;
import com.ericsson.oss.itpf.datalayer.dps.object.delta.AttributeDelta;
import com.ericsson.oss.itpf.datalayer.dps.object.delta.ManagedObjectDelta;
import com.ericsson.oss.itpf.datalayer.dps.object.delta.ObjectDeltaState;
import com.ericsson.oss.itpf.datalayer.dps.persistence.ManagedObject;
import com.ericsson.oss.services.cm.cmconfig.service.api.CmConfigDiffParams;
import com.ericsson.oss.services.cm.cmconfig.service.api.CmConfigDiffParams.CmConfigDiff;
import com.ericsson.oss.services.cm.cmconfig.service.api.CmDiffResponse;
import com.ericsson.oss.services.cm.cmconfig.service.api.DiffAttributeDelta;
import com.ericsson.oss.services.cm.cmconfig.service.api.DiffObjectDeltaState;
import com.ericsson.oss.services.cm.cmconfig.service.api.DiffResult;
import com.ericsson.oss.services.cm.cmconfig.service.ejb.util.CmConfigDpsUtil;
import com.ericsson.oss.services.cm.cmconfig.service.exception.handling.CmConfigServiceExceptionHandler;
import com.ericsson.oss.services.cm.cmconfig.service.log.CmConfigServiceLog;
import com.ericsson.oss.services.cm.cmconfig.service.log.CmConfigServiceMessages;
import com.ericsson.oss.services.cm.cmconfig.service.validation.ValidationConfigService;
import com.ericsson.oss.services.cm.cmconfig.service.validation.ValidationException;
import com.ericsson.oss.services.cm.cmshared.dto.search.CmSearchCriteria;

/**
 * Bean implementing the diff config functionality.
 */
@Stateless
public class DiffConfigBean implements DiffConfig {

    @Inject
    private ValidationConfigService validationConfigService;

    @Inject
    private CmConfigServiceLog logger;

    @Inject
    private CmConfigDpsUtil dpsUtil;

    @Inject
    private CmConfigServiceExceptionHandler exceptionHandler;

    @SuppressWarnings("PMD.AvoidCatchingThrowable")
    @Override
    public CmDiffResponse diffConfig(final String targetConfigName, final String referenceConfigName, final CmSearchCriteria scope,
            final CmConfigDiffParams params) {

        final CmDiffResponse returnCmResponse = new CmDiffResponse();
        returnCmResponse.setStatusMessage(CmConfigServiceMessages.CONFIG_DIFF_SUCCESS_MESSAGE);

        try {
            final DataBucket targetBucket = this.validationConfigService.validateConfig(targetConfigName);
            final DataBucket referenceBucket = this.validationConfigService.validateConfig(referenceConfigName);
            this.validationConfigService.validateCmConfigDiffParams(params);
            this.validationConfigService.validateCmSearchCriteria(scope);
            final Collection<DiffResult> diffResults = performDiff(targetBucket, referenceBucket, scope, params);
            returnCmResponse.setDiffResults(diffResults);
        } catch (final ValidationException validationException) {
            this.exceptionHandler.handleValidationException(returnCmResponse, validationException.getMessage(),
                    validationException.getSuggestedSolution(), DiffConfigBean.class.getName(), EXECUTION_ERROR);
        } catch (final Throwable throwable) {
            this.exceptionHandler.handleError(returnCmResponse, throwable, SOLUTION_FOR_GENERIC_EXCEPTION, EXECUTION_ERROR);
        }

        this.logger.logCmCommand(CmConfigServiceMessages.DIFF_CONFIG, returnCmResponse, targetConfigName, referenceConfigName, scope, params);

        return returnCmResponse;
    }

    private Collection<DiffAttributeDelta> getAttributeDeltasFromDPS(final Collection<AttributeDelta> attributeDeltas) {
        final List<DiffAttributeDelta> diffAttrResults = new ArrayList<>();
        for (final AttributeDelta attributeDelta : attributeDeltas) {
            final DiffAttributeDelta diffAttrResult = new DiffAttributeDelta();
            diffAttrResult.setAttributeName(attributeDelta.getAttributeName());
            diffAttrResult.setReferenceValue(attributeDelta.getReferenceValue());
            diffAttrResult.setTargetValue(attributeDelta.getTargetValue());
            diffAttrResults.add(diffAttrResult);
        }
        return diffAttrResults;
    }

    private DiffObjectDeltaState getMoFromDPSState(final ObjectDeltaState dpsObjectDeltaState) {
        switch (dpsObjectDeltaState) {
            case CREATED:
                return DiffObjectDeltaState.CREATED;
            case DELETED:
                return DiffObjectDeltaState.DELETED;
            case MODIFIED:
                return DiffObjectDeltaState.MODIFIED;
        }
        this.logger.logErrorMessage(EXECUTION_ERROR, "unknown DPS ObjectDeltaState", DiffConfigBean.class.getName());
        return null;
    }

    /**
     * Diff based on the param.
     *
     * @param targetBucket
     *            target bucket
     * @param referenceBucket
     *            reference bucket
     * @param scope
     *            diff scope
     * @param params
     *            diff params
     * @return
     *         diff result
     */
    protected Collection<DiffResult> performDiff(final DataBucket targetBucket, final DataBucket referenceBucket, final CmSearchCriteria scope,
            final CmConfigDiffParams params) {

        // Only Intersecting NEs diff
        if (params.getCmDiffParams().isEmpty()) {
            return diffCommonNetworkElements(targetBucket, referenceBucket, scope, params);
        }

        // limit(subset) of NEs from the Intersection.
        if (params.getCmDiffParams().contains(CmConfigDiff.LIMIT)) {
            return diffLimitedNetworkElements(targetBucket, referenceBucket, scope, params);
        }

        if (params.getCmDiffParams().contains(CmConfigDiff.INLCUDE)) {
            return diffAllNetworkElements(targetBucket, referenceBucket, scope, params);
        }

        this.logger.logErrorMessage(EXECUTION_ERROR, CmConfigServiceMessages.DIFF_PARAM_IS_INVALID, DiffConfigBean.class.getName());
        return new ArrayList<DiffResult>(0);
    }

    /**
     * Diff of common NEs across buckets.
     *
     * @param targetBucket
     *            target bucket
     * @param referenceBucket
     *            reference bucket
     * @param scope
     *            Diff scope
     * @param params
     *            Diff params
     * @return
     *         diff output
     */
    protected Collection<DiffResult> diffCommonNetworkElements(final DataBucket targetBucket, final DataBucket referenceBucket,
            final CmSearchCriteria scope, final CmConfigDiffParams params) {
        final Collection<ManagedObject> commonMOs = this.dpsUtil.findNEsCommonAcrossBuckets(targetBucket, referenceBucket, scope);
        final Collection<ManagedObjectDelta> moDelta = targetBucket.compareFullTree(commonMOs.toArray(new ManagedObject[0]));
        return getDiffResults(moDelta);
    }

    /**
     * Diff of NEs specified in LIMIT option.
     *
     * @param targetBucket
     *            target bucket
     * @param referenceBucket
     *            reference bucket
     * @param scope
     *            Diff scope
     * @param params
     *            Diff params
     * @return
     *         diff output
     */
    protected Collection<DiffResult> diffLimitedNetworkElements(final DataBucket targetBucket, final DataBucket referenceBucket,
            final CmSearchCriteria scope, final CmConfigDiffParams params) {
        // Check the list of node specification is not empty
        this.validationConfigService.validateNodeSpecificationIsNotEmpty(scope.getCmSearchScopes());
        // Check scope is NODE_NAME
        this.validationConfigService.validateScopeIsNodeNameOrUnspecified(scope.getCmSearchScopes());

        final Collection<ManagedObject> subsetNes = this.dpsUtil.findNEsCommonAcrossBuckets(targetBucket, referenceBucket, scope);

        final Collection<ManagedObjectDelta> moDelta = targetBucket.compareFullTree(subsetNes.toArray(new ManagedObject[0]));
        return getDiffResults(moDelta);
    }

    /**
     * Diff of all NEs when INCLUDE option is specified.
     *
     * @param targetBucket
     *            target bucket
     * @param referenceBucket
     *            reference bucket
     * @param scope
     *            Diff scope
     * @param params
     *            Diff params
     * @return
     *         diff output
     */
    protected Collection<DiffResult> diffAllNetworkElements(final DataBucket targetBucket, final DataBucket referenceBucket,
            final CmSearchCriteria scope, final CmConfigDiffParams params) {
        final Set<DiffResult> uniqueMoDelta = new HashSet<>();

        final Collection<ManagedObject> commonMOs = this.dpsUtil.findNEsCommonAcrossBuckets(targetBucket, referenceBucket, scope);
        final List<ManagedObjectDelta> commonMoDelta = targetBucket.compareFullTree(commonMOs.toArray(new ManagedObject[0]));
        uniqueMoDelta.addAll(getDiffResults(commonMoDelta));

        final Collection<ManagedObject> targetMOs = this.dpsUtil.findNEsOnlyInThisBucket(targetBucket, commonMOs);
        uniqueMoDelta.addAll(getDiffResultsWithState(targetMOs, DiffObjectDeltaState.CREATED));

        final Collection<ManagedObject> referenceMOs = this.dpsUtil.findNEsOnlyInThisBucket(referenceBucket, commonMOs);
        uniqueMoDelta.addAll(getDiffResultsWithState(referenceMOs, DiffObjectDeltaState.DELETED));

        return uniqueMoDelta;
    }

    private List<DiffResult> getDiffResults(final Collection<ManagedObjectDelta> moDeltas) {
        final List<DiffResult> diffResults = new ArrayList<>();
        for (final ManagedObjectDelta moDelta : moDeltas) {
            final DiffResult diffresult = new DiffResult();
            diffresult.setFdn(moDelta.getFdn());
            diffresult.setMoName(moDelta.getName());
            diffresult.setMoState(getMoFromDPSState(moDelta.getDeltaState()));
            diffresult.setAttributeDeltas(getAttributeDeltasFromDPS(moDelta.getAttributeDeltas()));
            diffResults.add(diffresult);
        }
        return diffResults;
    }

    private List<DiffResult> getDiffResultsWithState(final Collection<ManagedObject> mos, final DiffObjectDeltaState state) {
        final List<DiffResult> diffResults = new ArrayList<>();
        final Collection<DiffAttributeDelta> attributeDeltas = new ArrayList<>(0);
        for (final ManagedObject mo : mos) {
            final DiffResult diffresult = new DiffResult();
            diffresult.setFdn(mo.getFdn());
            diffresult.setMoName(mo.getName());
            diffresult.setMoState(state);
            diffresult.setAttributeDeltas(attributeDeltas);
            diffResults.add(diffresult);
        }
        return diffResults;
    }
}
