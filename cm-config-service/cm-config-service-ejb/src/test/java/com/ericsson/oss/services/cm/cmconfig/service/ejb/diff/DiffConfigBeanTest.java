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

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Matchers;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.runners.MockitoJUnitRunner;

import com.ericsson.oss.itpf.datalayer.dps.DataBucket;
import com.ericsson.oss.itpf.datalayer.dps.object.delta.AttributeDelta;
import com.ericsson.oss.itpf.datalayer.dps.object.delta.ManagedObjectDelta;
import com.ericsson.oss.itpf.datalayer.dps.object.delta.ObjectDeltaState;
import com.ericsson.oss.itpf.datalayer.dps.persistence.ManagedObject;
import com.ericsson.oss.services.cm.cmconfig.service.api.CmConfigDiffParams;
import com.ericsson.oss.services.cm.cmconfig.service.api.CmConfigDiffParams.CmConfigDiff;
import com.ericsson.oss.services.cm.cmconfig.service.api.CmConfigDiffParams.CmConfigDiffDetailType;
import com.ericsson.oss.services.cm.cmconfig.service.api.CmDiffResponse;
import com.ericsson.oss.services.cm.cmconfig.service.api.DiffResult;
import com.ericsson.oss.services.cm.cmconfig.service.ejb.util.CmConfigDpsUtil;
import com.ericsson.oss.services.cm.cmconfig.service.exception.handling.CmConfigServiceExceptionHandler;
import com.ericsson.oss.services.cm.cmconfig.service.exception.handling.CmConfigServiceExceptionHandlerImpl;
import com.ericsson.oss.services.cm.cmconfig.service.log.CmConfigServiceLog;
import com.ericsson.oss.services.cm.cmconfig.service.validation.ValidationConfigService;
import com.ericsson.oss.services.cm.cmconfig.service.validation.ValidationConfigServiceBean;
import com.ericsson.oss.services.cm.cmconfig.service.validation.ValidationException;
import com.ericsson.oss.services.cm.cmshared.dto.AbstractCmResponse;
import com.ericsson.oss.services.cm.cmshared.dto.search.CmSearchCriteria;
import com.ericsson.oss.services.cm.cmshared.dto.search.CmSearchScope;
import com.ericsson.oss.services.cm.log.ErrorHandler;

/**
 * Unit tests for Diff config.
 */
@SuppressWarnings("PMD.UnusedPrivateField")
@RunWith(MockitoJUnitRunner.class)
public class DiffConfigBeanTest {

    private static final String TARGET_CONFIG_NAME = "targetConfiguration";
    private static final String REFERENCE_CONFIG_NAME = "referenceConfiguration";

    @Spy
    private final ValidationConfigService validationConfigService = new ValidationConfigServiceBean();

    @Mock
    private CmConfigServiceLog logger;

    @Mock
    private CmSearchCriteria scope;

    @Mock
    private CmConfigDiffParams diffParams;

    @Mock
    private DataBucket referenceBucket;

    @Mock
    private DataBucket targetBucket;

    @Mock
    private ManagedObjectDelta moDelta;

    @Mock
    private ManagedObject mockManagedObject;

    @Mock
    private AttributeDelta mockAttributeDelta;

    @Mock
    private ValidationException mockValidationException;

    @Mock
    private CmConfigDpsUtil dpsUtil;

    @Spy
    @InjectMocks
    private final CmConfigServiceExceptionHandler exceptionHandler = new CmConfigServiceExceptionHandlerImpl();

    @InjectMocks
    private final DiffConfigBean diffConfigBean = new DiffConfigBean();

    @Before
    public void initMocks() {
        Mockito.doReturn(this.referenceBucket).when(this.validationConfigService).validateConfig(TARGET_CONFIG_NAME);
        Mockito.doReturn(this.targetBucket).when(this.validationConfigService).validateConfig(REFERENCE_CONFIG_NAME);
        Mockito.when(this.diffParams.getDiffDetailType()).thenReturn(CmConfigDiffDetailType.ATTRIBUTE);
        Mockito.when(this.moDelta.getDeltaState()).thenReturn(ObjectDeltaState.MODIFIED);
        Mockito.when(this.moDelta.getFdn()).thenReturn("ERBS1");
    }

    /**
     * <li>unit of work: simple test of diffConfig call.
     * <li>state under test: valid parameters .
     * <li>expected behavior: CmDiffResponse is not null.
     */
    @Test
    public void diffConfig_params_diffResponseNotNull() {
        Mockito.when(this.targetBucket.compareFullTree(Matchers.<ManagedObject>anyVararg())).thenReturn(null);
        final CmDiffResponse diffResponse = this.diffConfigBean.diffConfig(TARGET_CONFIG_NAME, REFERENCE_CONFIG_NAME, this.scope, this.diffParams);
        Assert.assertNotNull(diffResponse);
    }

    /**
     * <li>unit of work: moDelta for Intersecting NEs.
     * <li>state under test: CmDiffParamsIsEmpty
     * <li>expected behavior: diffResults is not null and the expected MO diffResult matches the actual return value.
     */
    @Test
    public void performDiff_CmDiffParamsIsEmpty_moDeltaNotNull() {
        final List<ManagedObjectDelta> expectedMODeltas = new ArrayList<>();
        expectedMODeltas.add(this.moDelta);
        Mockito.when(this.targetBucket.compareFullTree(Matchers.<ManagedObject>anyVararg())).thenReturn(expectedMODeltas);
        final Collection<DiffResult> diffResults = this.diffConfigBean.performDiff(this.targetBucket, this.referenceBucket, this.scope,
                this.diffParams);
        Assert.assertNotNull(diffResults);
        final DiffResult diffResult = diffResults.iterator().next();
        Assert.assertEquals(this.moDelta.getFdn(), diffResult.getFdn());
    }

    /**
     * <li>unit of work: moDelta when LIMIT is specified.
     * <li>state under test: CmDiffParams contains CmConfigDiff.LIMIT
     * <li>expected behavior: diffResults is not null and the expected MO diffResult matches the actual return value.
     */
    @Test
    public void performDiff_CmDiffParamsLimit_moDeltaLimitedByNE() {
        final List<ManagedObjectDelta> expectedMODeltas = new ArrayList<>();
        expectedMODeltas.add(this.moDelta);
        Mockito.when(this.moDelta.getDeltaState()).thenReturn(ObjectDeltaState.DELETED);
        Mockito.when(this.moDelta.getFdn()).thenReturn("ERBS2");
        Mockito.when(this.targetBucket.compareFullTree(Matchers.<ManagedObject>anyVararg())).thenReturn(expectedMODeltas);

        final CmConfigDiffParams diffParams = new CmConfigDiffParams();
        diffParams.addCmDiffParam(CmConfigDiff.LIMIT);

        final List<CmSearchScope> searchScopes = new ArrayList<>();
        final CmSearchScope searchScope = new CmSearchScope();
        searchScopes.add(searchScope);
        Mockito.when(this.scope.getCmSearchScopes()).thenReturn(searchScopes);

        final Collection<DiffResult> diffResults = this.diffConfigBean.performDiff(this.targetBucket, this.referenceBucket, this.scope, diffParams);
        Assert.assertNotNull(diffResults);
        final DiffResult diffResult = diffResults.iterator().next();
        Assert.assertEquals(this.moDelta.getFdn(), diffResult.getFdn());
    }

    /**
     * <li>unit of work: moDelta when INLCUDE is specified.
     * <li>state under test: CmDiffParams contains CmConfigDiff.INLCUDE
     * <li>expected behavior: diffResults is not null and the expected MO diffResult matches the actual return value.
     */
    @Test
    public void performDiff_CmDiffParamsInclude_moDelta() {
        final List<ManagedObjectDelta> expectedMODeltas = new ArrayList<>();
        expectedMODeltas.add(this.moDelta);
        Mockito.when(this.moDelta.getDeltaState()).thenReturn(ObjectDeltaState.CREATED);
        Mockito.when(this.moDelta.getFdn()).thenReturn("ERBS1");
        Mockito.when(this.targetBucket.compareFullTree(Matchers.<ManagedObject>anyVararg())).thenReturn(expectedMODeltas);

        final Collection<ManagedObject> targetMOs = new ArrayList<>();
        targetMOs.add(this.mockManagedObject);
        Mockito.when(this.mockManagedObject.getFdn()).thenReturn("ERBS1");
        final Collection<ManagedObject> commonMOs = new LinkedList<>();
        Mockito.when(this.dpsUtil.findNEsOnlyInThisBucket(this.targetBucket, commonMOs)).thenReturn(targetMOs);

        final CmConfigDiffParams diffParams = new CmConfigDiffParams();
        diffParams.addCmDiffParam(CmConfigDiff.INLCUDE);

        final List<CmSearchScope> searchScopes = new ArrayList<>();
        final CmSearchScope searchScope = new CmSearchScope();
        searchScopes.add(searchScope);
        Mockito.when(this.scope.getCmSearchScopes()).thenReturn(searchScopes);

        final Collection<DiffResult> diffResults = this.diffConfigBean.performDiff(this.targetBucket, this.referenceBucket, this.scope, diffParams);
        Assert.assertNotNull(diffResults);
        final DiffResult diffResult = diffResults.iterator().next();
        Assert.assertEquals(this.moDelta.getFdn(), diffResult.getFdn());
    }

    /**
     * <li>unit of work: moDelta when diff detail type is ATTRIBUTE.
     * <li>state under test: CmConfigDiffDetailType.ATTRIBUTE
     * <li>expected behavior: diffResults is not null and the expected MO diffResult matches the actual return value.
     */
    @Test
    public void performDiff_DiffDetailTypeIsATTRIBUTE_moDeltaNotNull() {
        final List<ManagedObjectDelta> expectedMODeltas = new ArrayList<>();
        expectedMODeltas.add(this.moDelta);
        Mockito.when(this.moDelta.getDeltaState()).thenReturn(ObjectDeltaState.MODIFIED);
        Mockito.when(this.moDelta.getFdn()).thenReturn("ERBS2");
        Mockito.when(this.moDelta.getAttributeDelta(Matchers.anyString())).thenReturn(this.mockAttributeDelta);
        Mockito.when(this.diffParams.getDiffDetailType()).thenReturn(CmConfigDiffDetailType.ATTRIBUTE);
        Mockito.when(this.targetBucket.compareFullTree(Matchers.<ManagedObject>anyVararg())).thenReturn(expectedMODeltas);
        final Collection<DiffResult> diffResults = this.diffConfigBean.performDiff(this.targetBucket, this.referenceBucket, this.scope,
                this.diffParams);
        Assert.assertNotNull(diffResults);
        final DiffResult diffResult = diffResults.iterator().next();
        Assert.assertEquals(this.moDelta.getFdn(), diffResult.getFdn());
    }

    /**
     * <li>unit of work: handle any exceptions thrown by Validation Service.
     * <li>state under test: ValidationException thrown when validateConfig is called.
     * <li>expected behavior: exception handled correctly.
     */
    @Test
    public void diffConfig_ValidationException_exceptionHandled() {
        Mockito.doThrow(this.mockValidationException).when(this.validationConfigService).validateConfig(Matchers.anyString());
        final AbstractCmResponse result = this.diffConfigBean.diffConfig(TARGET_CONFIG_NAME, REFERENCE_CONFIG_NAME, this.scope, this.diffParams);
        assertEquals(ErrorHandler.EXECUTION_ERROR, result.getStatusCode());
    }

    /**
     * <li>unit of work: handle any exceptions thrown by Validation Service.
     * <li>state under test: ValidationException thrown when CmConfigDiffParam is null.
     * <li>expected behavior: exception handled correctly.
     */
    @Test
    public void diffConfig_DiffParamsInvalid_exceptionHandled() {
        final AbstractCmResponse result = this.diffConfigBean.diffConfig(TARGET_CONFIG_NAME, REFERENCE_CONFIG_NAME, this.scope, null);
        assertEquals(ErrorHandler.EXECUTION_ERROR, result.getStatusCode());
    }

    /**
     * <li>unit of work: handle any exceptions thrown by Validation Service.
     * <li>state under test: ValidationException thrown when CmSearchCriteris is null.
     * <li>expected behavior: exception handled correctly.
     */
    @Test
    public void diffConfig_SearchCriteriaInvalid_exceptionHandled() {
        final AbstractCmResponse result = this.diffConfigBean.diffConfig(TARGET_CONFIG_NAME, REFERENCE_CONFIG_NAME, null, this.diffParams);
        assertEquals(ErrorHandler.EXECUTION_ERROR, result.getStatusCode());
    }

    /**
     * <li>unit of work: handle any DPS exceptions thrown by call to DPS apis.
     * <li>state under test: DPS exception thrown when DPS compareFullTree is called.
     * <li>expected behavior: exception handled correctly.
     */
    @SuppressWarnings("PMD.AvoidThrowingRawExceptionTypes")
    @Test
    public void diffConfig_DPSException_exceptionHandled() {
        Mockito.when(this.validationConfigService.validateConfig(TARGET_CONFIG_NAME)).thenReturn(this.targetBucket);
        Mockito.doThrow(new RuntimeException("DPS Exception")).when(this.targetBucket).compareFullTree(Matchers.<ManagedObject>anyVararg());
        final AbstractCmResponse result = this.diffConfigBean.diffConfig(TARGET_CONFIG_NAME, REFERENCE_CONFIG_NAME, this.scope, this.diffParams);
        assertEquals(ErrorHandler.EXECUTION_ERROR, result.getStatusCode());
    }

}
