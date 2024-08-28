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
package com.ericsson.oss.services.cm.config.service.test;

import static org.junit.Assert.assertEquals;

import static com.ericsson.oss.services.cm.testutil.DpsTestMoType.ENODEB_FUNCTION;
import static com.ericsson.oss.services.cm.testutil.DpsTestMoType.MANAGED_ELEMENT;
import static com.ericsson.oss.services.cm.testutil.DpsTestMoType.MECONTEXT;
import static com.ericsson.oss.services.cm.testutil.TestDataBuilder.CPP_NAMESPACE_VERSION;
import static com.ericsson.oss.services.cm.testutil.TestDataBuilder.ERBS_NAMESPACE_VERSION;
import static com.ericsson.oss.services.cm.testutil.TestDataBuilder.TOP_NAMESPACE_VERSION;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ejb.Stateless;

import org.jboss.arquillian.junit.Arquillian;
import org.junit.After;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ericsson.oss.itpf.datalayer.dps.DataBucket;
import com.ericsson.oss.itpf.datalayer.dps.object.delta.ManagedObjectDelta;
import com.ericsson.oss.itpf.datalayer.dps.persistence.ManagedObject;
import com.ericsson.oss.services.cm.cmconfig.service.api.CmConfigDiffParams;
import com.ericsson.oss.services.cm.cmconfig.service.api.CmDiffResponse;
import com.ericsson.oss.services.cm.cmconfig.service.api.DiffAttributeDelta;
import com.ericsson.oss.services.cm.cmconfig.service.api.DiffResult;
import com.ericsson.oss.services.cm.cmshared.dto.search.CmMatchCondition;
import com.ericsson.oss.services.cm.cmshared.dto.search.CmSearchCriteria;
import com.ericsson.oss.services.cm.cmshared.dto.search.CmSearchScope;
import com.ericsson.oss.services.cm.cmshared.dto.search.CmSearchScope.ScopeType;
import com.ericsson.oss.services.cm.testutil.TestDataBuilder;
import com.ericsson.oss.services.cm.testutil.mo.MeContext;

/**
 * Arquillian Tests for CM Diff.
 */
@Ignore
@RunWith(Arquillian.class)
@Stateless
@SuppressWarnings("PMD.SignatureDeclareThrowsException")
public class CmConfigDiffIT extends CmConfigServiceTestBase {
    private static final Logger logger = LoggerFactory.getLogger(CmConfigDiffIT.class);
    private static final String NON_EXISTING_VALUE = "";
    private static final String TARGET_BUCKET_NAME = "targetBucket";
    private static final String REFERENCE_BUCKET_NAME = "referenceBucket";
    private static final String NAME_TARGET = "target";
    private static final String NAME_BOTH = "both";
    private static final String FDN_BOTH = "MeContext=" + NAME_BOTH;
    private static final String NAME_BOTH2 = "both2";
    private static final String FDN_BOTH2 = "MeContext=" + NAME_BOTH2;
    private static final String NAME_REFERENCE = "reference";

    @Override
    protected void setupTest() throws Exception {
        setupBuckets();
    }

    /**
     * Cleans up.
     * 
     * @throws Exception
     *             if there is some error.
     */
    @Override
    @After
    public void cleanup() throws Exception {
        this.testBuilder.removeTestData(TARGET_BUCKET_NAME);
        this.testBuilder.removeTestData(REFERENCE_BUCKET_NAME);
    }

    @Test
    public void testDiffLimitedToOneNode() throws Exception {

        final CmSearchCriteria cmSearchCriteria = getNodes(NAME_BOTH);
        final CmConfigDiffParams params = new CmConfigDiffParams();
        params.addCmDiffParam(CmConfigDiffParams.CmConfigDiff.LIMIT);
        final CmDiffResponse cmDiffResponse = this.cmConfigService.diffConfig(TARGET_BUCKET_NAME, REFERENCE_BUCKET_NAME, cmSearchCriteria, params);

        final DiffOutput diffOutput = processDiffOutput(cmDiffResponse);

        assertEquals("EXPECTED_CREATED_OBJECTS should be equal", 2, diffOutput.createdObjects.size());
        assertEquals("EXPECTED_MODIFIED_OBJECTS should be equal", 1, diffOutput.modifiedObjects.size());
        assertEquals("EXPECTED_DELETED_OBJECTS should be equal", 2, diffOutput.deletedObjects.size());

        final DiffDpsOutput diffDpsOutput = runDPSComparison(FDN_BOTH);

        assertEquals("EXPECTED_CREATED_OBJECTS in DPS should be equal", 2, diffDpsOutput.createdDPSObjects.size());
        assertEquals("EXPECTED_MODIFIED_OBJECTS in DPS should be equal", 1, diffDpsOutput.modifiedDPSObjects.size());
        assertEquals("EXPECTED_DELETED_OBJECTS in DPS should be equal", 2, diffDpsOutput.deletedDPSObjects.size());
    }

    @Test
    public void testDiffLimitedToTwoNodes() throws Exception {

        final CmSearchCriteria cmSearchCriteria = getNodes(NAME_BOTH, NAME_BOTH2);
        final CmConfigDiffParams params = new CmConfigDiffParams();
        params.addCmDiffParam(CmConfigDiffParams.CmConfigDiff.LIMIT);
        final CmDiffResponse cmDiffResponse = this.cmConfigService.diffConfig(TARGET_BUCKET_NAME, REFERENCE_BUCKET_NAME, cmSearchCriteria, params);

        final DiffOutput diffOutput = processDiffOutput(cmDiffResponse);

        assertEquals("EXPECTED_CREATED_OBJECTS should be equal", 4, diffOutput.createdObjects.size());
        assertEquals("EXPECTED_MODIFIED_OBJECTS should be equal", 2, diffOutput.modifiedObjects.size());
        assertEquals("EXPECTED_DELETED_OBJECTS should be equal", 4, diffOutput.deletedObjects.size());

        final DiffDpsOutput diffDpsOutput = runDPSComparison(FDN_BOTH, FDN_BOTH2);

        assertEquals("EXPECTED_CREATED_OBJECTS in DPS should be equal", 4, diffDpsOutput.createdDPSObjects.size());
        assertEquals("EXPECTED_MODIFIED_OBJECTS in DPS should be equal", 2, diffDpsOutput.modifiedDPSObjects.size());
        assertEquals("EXPECTED_DELETED_OBJECTS in DPS should be equal", 4, diffDpsOutput.deletedDPSObjects.size());
    }

    @Test
    public void testDiffWithNoOptions() throws Exception {

        final CmDiffResponse cmDiffResponse = this.cmConfigService.diffConfig(TARGET_BUCKET_NAME, REFERENCE_BUCKET_NAME, new CmSearchCriteria(),
                new CmConfigDiffParams());

        final DiffOutput diffOutput = processDiffOutput(cmDiffResponse);

        assertEquals("EXPECTED_CREATED_OBJECTS should be equal", 4, diffOutput.createdObjects.size());
        assertEquals("EXPECTED_MODIFIED_OBJECTS should be equal", 2, diffOutput.modifiedObjects.size());
        assertEquals("EXPECTED_DELETED_OBJECTS should be equal", 4, diffOutput.deletedObjects.size());

        final DiffDpsOutput diffDpsOutput = runDPSComparison(FDN_BOTH, FDN_BOTH2);

        assertEquals("EXPECTED_CREATED_OBJECTS in DPS should be equal", 4, diffDpsOutput.createdDPSObjects.size());
        assertEquals("EXPECTED_MODIFIED_OBJECTS in DPS should be equal", 2, diffDpsOutput.modifiedDPSObjects.size());
        assertEquals("EXPECTED_DELETED_OBJECTS in DPS should be equal", 4, diffDpsOutput.deletedDPSObjects.size());
    }

    public void setupBuckets() throws Exception {
        populateReferenceBucket();
        populateTargetBucket();
    }

    /*-
     * Creates a data structure like this in the reference bucket:
     * <pre>
     *    MeContext=both
     *           |
     *           |---------------------------------------------------+
     *           |                                                   |
     *    ManagedElement=both                                ManagedElement=reference
     *           |                                                   |
     *           |                                                   |
     *           |                                                   |
     *    ENodeBFunction=both#                          ENodeBFunction=1
     *
     *
     *    MeContext=both2
     *           |
     *           |---------------------------------------------------+
     *           |                                                   |
     *    ManagedElement=both                                ManagedElement=reference
     *           |                                                   |
     *           |                                                   |
     *           |                                                   |
     *    ENodeBFunction=both#                               ENodeBFunction=1
     *
     * </pre>
     *
     * NB # means the MO has different attribute values compared to the corresponding object in the reference bucket.
     *
     * @throws Exception
     */
    private void populateReferenceBucket() throws Exception {
        this.testBuilder.userTransaction.begin();
        final DataBucket bucket = this.testBuilder.createConfiguration(REFERENCE_BUCKET_NAME);

        createNetworkElementInReference(bucket, NAME_BOTH);
        createNetworkElementInReference(bucket, NAME_BOTH2);
        this.testBuilder.createRootTestMoInDps(bucket, MECONTEXT, NAME_REFERENCE, TOP_NAMESPACE_VERSION);

        this.testBuilder.userTransaction.commit();
    }

    @SuppressWarnings("PMD.UnusedLocalVariable")
    private void createNetworkElementInReference(final DataBucket bucket, final String meContextId) {
        final ManagedObject meContextBoth = this.testBuilder.createRootTestMoInDps(bucket, MECONTEXT, meContextId, TOP_NAMESPACE_VERSION);
        final ManagedObject managedElementBoth = this.testBuilder.createTestMoInDps(bucket, MANAGED_ELEMENT, meContextBoth, meContextId, meContextId,
                CPP_NAMESPACE_VERSION);
        final ManagedObject eNodeBFunctionBothHash = this.testBuilder.createTestMoInDps(bucket, ENODEB_FUNCTION, managedElementBoth, meContextId,
                meContextId, ERBS_NAMESPACE_VERSION);
        eNodeBFunctionBothHash.setAttribute(TestDataBuilder.USER_LABEL_ATTRIBUTE, "RefDifferentValue");
        final ManagedObject managedElementReference = this.testBuilder.createTestMoInDps(bucket, MANAGED_ELEMENT, meContextBoth, NAME_REFERENCE,
                NAME_REFERENCE, CPP_NAMESPACE_VERSION);
        this.testBuilder.createTestMoInDps(bucket, ENODEB_FUNCTION, managedElementReference, "1", NAME_REFERENCE, ERBS_NAMESPACE_VERSION);
    }

    /*-
     * Creates a data structure like this in the target bucket:
     *
     * <pre>
     *                   MeContext=both
     *                        |
     *                        |--------------------------------------------+
     *                        |                                            |
     *                   ManagedElement=target                  ManagedElement=both
     *                        |                                            |
     *                        |                                            |
     *                   ENodeBFunction=1                       ENodeBFunction=both#
     *
     *
     *                   MeContext=both2
     *                        |
     *                        +--------------------------------------------+
     *                        |                                            |
     *                   ManagedElement=target                  ManagedElement=both
     *                        |                                            |
     *                        |                                            |
     *                   ENodeBFunction=1                    ENodeBFunction=both#
     *
     * </pre>
     * NB # means the MO has different attribute values compared to the corresponding object in the reference bucket.
     *
     * @throws Exception
     */
    private void populateTargetBucket() throws Exception {
        this.testBuilder.userTransaction.begin();
        final DataBucket bucket = this.testBuilder.createConfiguration(TARGET_BUCKET_NAME);

        createNetworkElementInTarget(bucket, NAME_BOTH);
        createNetworkElementInTarget(bucket, NAME_BOTH2);
        this.testBuilder.createRootTestMoInDps(bucket, MECONTEXT, NAME_TARGET, TOP_NAMESPACE_VERSION);

        this.testBuilder.userTransaction.commit();
    }

    @SuppressWarnings("PMD.UnusedLocalVariable")
    private void createNetworkElementInTarget(final DataBucket bucket, final String meContextId) {
        final ManagedObject meContextBoth = this.testBuilder.createRootTestMoInDps(bucket, MECONTEXT, meContextId, TOP_NAMESPACE_VERSION);
        final ManagedObject managedElementBoth = this.testBuilder.createTestMoInDps(bucket, MANAGED_ELEMENT, meContextBoth, meContextId, meContextId,
                CPP_NAMESPACE_VERSION);
        this.testBuilder.createTestMoInDps(bucket, ENODEB_FUNCTION, managedElementBoth, meContextId, meContextId, ERBS_NAMESPACE_VERSION);
        final ManagedObject managedElementTarget = this.testBuilder.createTestMoInDps(bucket, MANAGED_ELEMENT, meContextBoth, NAME_TARGET,
                NAME_TARGET, CPP_NAMESPACE_VERSION);
        this.testBuilder.createTestMoInDps(bucket, ENODEB_FUNCTION, managedElementTarget, "1", NAME_TARGET, ERBS_NAMESPACE_VERSION);
    }

    private CmSearchCriteria getNodes(final String... nodeNames) {
        final CmSearchCriteria nodes = new CmSearchCriteria();
        final List<CmSearchScope> scopes = new ArrayList<>();
        for (final String nodeName : nodeNames) {
            scopes.add(getNode(nodeName));
        }
        nodes.setCmSearchScopes(scopes);
        return nodes;
    }

    private CmSearchScope getNode(final String nodeName) {
        final CmSearchScope node = new CmSearchScope();
        node.setName(MeContext.ME_CONTEXT_MO_NAME);
        node.setScopeType(ScopeType.NODE_NAME);
        node.setCmMatchCondition(CmMatchCondition.EQUALS);
        node.setValue(nodeName);
        return node;
    }

    private DiffOutput processDiffOutput(final CmDiffResponse cmDiffResponse) {
        final DiffOutput diffOutPut = new DiffOutput();
        for (final DiffResult objectDelta : cmDiffResponse.getDiffResults()) {
            switch (objectDelta.getMoState()) {
            case CREATED:
                logger.info("CREATED FDN = {} ", objectDelta.getFdn());
                diffOutPut.createdObjects.put(objectDelta.getFdn(), objectDelta);
                break;
            case MODIFIED:
                logger.info("MODIFIED FDN =  {}", objectDelta.getFdn());
                diffOutPut.modifiedObjects.put(objectDelta.getFdn(), objectDelta);
                displayAttribDiff(objectDelta);
                break;
            case DELETED:
                logger.info("DELETED FDN = {}", objectDelta.getFdn());
                diffOutPut.deletedObjects.put(objectDelta.getFdn(), objectDelta);
                break;
            }
        }
        return diffOutPut;
    }

    private void displayAttribDiff(final DiffResult objectDelta) {
        logger.info("ATTRIBUTE DIFF of {} ", objectDelta.getFdn());
        for (final DiffAttributeDelta diffAttributeDelta : objectDelta.getAttributeDeltas()) {
            String targetAttributeValue = NON_EXISTING_VALUE;
            if (diffAttributeDelta.getTargetValue() != null) {
                targetAttributeValue = diffAttributeDelta.getTargetValue().toString();
            }
            String refAttributeValue = NON_EXISTING_VALUE;
            if (diffAttributeDelta.getReferenceValue() != null) {
                refAttributeValue = diffAttributeDelta.getReferenceValue().toString();
            }
            logger.info("{} : {} : {}", diffAttributeDelta.getAttributeName(), targetAttributeValue, refAttributeValue);
        }
    }

    /*
     * DPS Diff directly.
     */
    private DiffDpsOutput runDPSComparison(final String... commonNodesFdn) throws Exception {
        try {
            this.testBuilder.userTransaction.begin();
            final DataBucket referenceBucket = this.testBuilder.tryCreateConfiguration(REFERENCE_BUCKET_NAME);
            final DataBucket targetBucket = this.testBuilder.tryCreateConfiguration(TARGET_BUCKET_NAME);

            final ManagedObject[] referenceTopRootObjects = new ManagedObject[commonNodesFdn.length];
            int index = 0;
            for (final String fdn : commonNodesFdn) {
                final ManagedObject referenceTopRootObject = referenceBucket.findMoByFdn(fdn);
                referenceTopRootObjects[index] = referenceTopRootObject;
                index++;
            }

            return splitUpResults(targetBucket.compareFullTree(referenceTopRootObjects));
        } finally {
            this.testBuilder.userTransaction.commit();
        }
    }

    private DiffDpsOutput splitUpResults(final List<ManagedObjectDelta> deltaList) {
        final DiffDpsOutput diffDpsOutPut = new DiffDpsOutput();
        for (final ManagedObjectDelta objectDelta : deltaList) {
            switch (objectDelta.getDeltaState()) {
            case CREATED:
                diffDpsOutPut.createdDPSObjects.put(objectDelta.getFdn(), objectDelta);
                break;
            case MODIFIED:
                diffDpsOutPut.modifiedDPSObjects.put(objectDelta.getFdn(), objectDelta);
                break;
            case DELETED:
                diffDpsOutPut.deletedDPSObjects.put(objectDelta.getFdn(), objectDelta);
                break;
            }
        }
        return diffDpsOutPut;
    }

    private class DiffOutput {
        Map<String, DiffResult> createdObjects = new HashMap<>();
        Map<String, DiffResult> modifiedObjects = new HashMap<>();
        Map<String, DiffResult> deletedObjects = new HashMap<>();
    }

    private class DiffDpsOutput {
        Map<String, ManagedObjectDelta> createdDPSObjects = new HashMap<>();
        Map<String, ManagedObjectDelta> modifiedDPSObjects = new HashMap<>();
        Map<String, ManagedObjectDelta> deletedDPSObjects = new HashMap<>();
    }
}
