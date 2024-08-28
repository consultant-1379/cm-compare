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
package com.ericsson.oss.services.cm.cmconfig.service.ejb.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Matchers;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import com.ericsson.oss.itpf.datalayer.dps.DataBucket;
import com.ericsson.oss.itpf.datalayer.dps.DataPersistenceService;
import com.ericsson.oss.itpf.datalayer.dps.persistence.ManagedObject;
import com.ericsson.oss.itpf.datalayer.dps.query.Query;
import com.ericsson.oss.itpf.datalayer.dps.query.QueryBuilder;
import com.ericsson.oss.itpf.datalayer.dps.query.QueryExecutor;
import com.ericsson.oss.services.cm.cmshared.dto.search.CmMatchCondition;
import com.ericsson.oss.services.cm.cmshared.dto.search.CmSearchCriteria;
import com.ericsson.oss.services.cm.cmshared.dto.search.CmSearchScope;
import com.ericsson.oss.services.cm.cmshared.dto.search.CmSearchScope.ScopeType;

/**
 * Unit tests for {@link CmConfigDpsUtil}.
 */
@RunWith(MockitoJUnitRunner.class)
public class CmConfigDpsUtilTest {

    private static final String NODE_1_NAME = "RBS1";
    private static final String ME_CONTEXT = "MeContext";

    @Mock
    private DataBucket mockDataBucket;

    @Mock
    private ManagedObject mockManagedObject;

    @Mock
    private CmSearchScope mockSearchScope;

    @Mock
    private Query mockQuery;

    @Mock
    private QueryExecutor mockQueryExecutor;

    @Mock
    private DataBucket mockTargetBucket;

    @Mock
    private DataBucket mockReferenceBucket;

    @Mock
    private CmSearchCriteria mockSearchCriteria;

    @Mock
    private DataPersistenceService mockDataPersistenceService;

    @Mock
    private QueryBuilder mockQueryBuilder;

    @InjectMocks
    private final CmConfigDpsUtil cmConfigDPSUtil = new CmConfigDpsUtil();

    @Before
    public void init() {
        Mockito.when(this.mockDataPersistenceService.getQueryBuilder()).thenReturn(this.mockQueryBuilder);
        Mockito.when(this.mockQueryBuilder.createTypeQuery(Matchers.anyString(), Matchers.anyString())).thenReturn(this.mockQuery);
        Mockito.when(this.mockTargetBucket.getQueryExecutor()).thenReturn(this.mockQueryExecutor);
        Mockito.when(this.mockReferenceBucket.getQueryExecutor()).thenReturn(this.mockQueryExecutor);
        Mockito.when(this.mockDataBucket.getQueryExecutor()).thenReturn(this.mockQueryExecutor);
        final Collection<Object> pos = new ArrayList<Object>();
        pos.add(this.mockManagedObject);
        Mockito.when(this.mockQueryExecutor.execute(this.mockQuery)).thenReturn(pos.iterator());
    }

    @Test
    public void getFDN_CmSearchScope_validFdn() {
        Mockito.when(this.mockSearchScope.getName()).thenReturn(ME_CONTEXT);
        Mockito.when(this.mockSearchScope.getValue()).thenReturn(NODE_1_NAME);
        final String fdn = this.cmConfigDPSUtil.getFDN(this.mockSearchScope);
        final String expectedFdn = ME_CONTEXT + "=" + NODE_1_NAME;
        Assert.assertEquals(expectedFdn, fdn);
        Mockito.verify(this.mockSearchScope).getName();
        Mockito.verify(this.mockSearchScope).getValue();
    }

    @Test
    public void getManagedElementByFdn_fdnAndDataBucketParam_validManagedObject() {
        final String fdn = ME_CONTEXT + "=" + NODE_1_NAME;
        Mockito.when(this.mockDataBucket.findMoByFdn(fdn)).thenReturn(this.mockManagedObject);
        final ManagedObject actualMo = this.cmConfigDPSUtil.getManagedElementByFdn(fdn, this.mockDataBucket);
        Assert.assertEquals(this.mockManagedObject, actualMo);
        Mockito.verify(this.mockDataBucket).findMoByFdn(fdn);
    }

    @Test
    public void deleteManagedElementByMoReference_moAndDataBucketParam_deletePoIsCalled() {
        this.cmConfigDPSUtil.deleteManagedElementByMoReference(this.mockManagedObject, this.mockDataBucket);
        Mockito.verify(this.mockDataBucket).deletePo(this.mockManagedObject);
    }

    @Test
    public void getNesFromSearchScope_ScopeTypeNODE_NAME_nodesMatchingNone() {
        Mockito.when(this.mockSearchScope.getScopeType()).thenReturn(ScopeType.NODE_NAME);
        Mockito.when(this.mockSearchScope.getCmMatchCondition()).thenReturn(CmMatchCondition.EQUALS);
        final List<ManagedObject> nes = this.cmConfigDPSUtil.findNesFromSearchScope(this.mockDataBucket, this.mockSearchScope);
        Assert.assertTrue(nes.size() == 0);
    }

    @Test
    public void getNesFromSearchScope_ScopeTypeUNSPECIFIED_nodesMatchingNone() {
        Mockito.when(this.mockSearchScope.getScopeType()).thenReturn(ScopeType.UNSPECIFIED);
        final List<ManagedObject> nes = this.cmConfigDPSUtil.findNesFromSearchScope(this.mockDataBucket, this.mockSearchScope);
        Assert.assertTrue(nes.size() == 1);
    }

    @Test
    public void getNEsCommonAcrossBuckets_Buckets_CommonMos() {
        final List<ManagedObject> nesInTarget = new ArrayList<>();
        nesInTarget.add(this.mockManagedObject);

        final CmConfigDpsUtil testCmConfigDPSUtil = new CmConfigDpsUtil() {
            @Override
            public List<ManagedObject> findNesFromSearchScope(final DataBucket config, final CmSearchScope cmSearchScope) {
                return nesInTarget;
            }
        };

        Mockito.when(this.mockReferenceBucket.findMoByFdn(Matchers.anyString())).thenReturn(this.mockManagedObject);

        final HashSet<ManagedObject> expectedNesCommon = new HashSet<>();
        expectedNesCommon.add(this.mockManagedObject);

        final Collection<ManagedObject> actualNesCommon = testCmConfigDPSUtil.findNEsCommonAcrossBuckets(this.mockTargetBucket,
                this.mockReferenceBucket, new CmSearchCriteria());

        Assert.assertEquals(expectedNesCommon, actualNesCommon);
    }

    @Test
    public void getAllNEsFromBuckets_Buckets_AllMos() {
        final Collection<ManagedObject> allMos = this.cmConfigDPSUtil.findAllNEsFromBuckets(this.mockTargetBucket, this.mockReferenceBucket,
                this.mockSearchCriteria);
        Assert.assertTrue(allMos.size() == 1);
    }

    @Test
    public void getNEsOnlyInThisBucket_BucketAndCommonMos_MosOnlyInThisBucket() {
        final Collection<ManagedObject> commonMos = new ArrayList<>(0);
        final Collection<ManagedObject> mosOnlyInThisBucket = this.cmConfigDPSUtil.findNEsOnlyInThisBucket(this.mockTargetBucket, commonMos);
        Assert.assertTrue(mosOnlyInThisBucket.size() == 1);
    }
}
