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

import static com.ericsson.oss.services.cm.testutil.TestDataBuilder.MECONTEXT_FDN1;
import static com.ericsson.oss.services.cm.testutil.TestDataBuilder.NODE_NAME1;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;

import org.hamcrest.CoreMatchers;
import org.jboss.arquillian.junit.Arquillian;
import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.ericsson.oss.itpf.datalayer.dps.DataBucket;
import com.ericsson.oss.itpf.datalayer.dps.persistence.ManagedObject;
import com.ericsson.oss.services.cm.cmconfig.service.api.CmCopyNodesFlags;
import com.ericsson.oss.services.cm.cmconfig.service.api.CmCopyNodesFlags.CmConfigCopy;
import com.ericsson.oss.services.cm.cmconfig.service.api.CmCopyResponse;
import com.ericsson.oss.services.cm.cmconfig.service.api.CopyResult;
import com.ericsson.oss.services.cm.cmconfig.service.api.CopyResult.CopyStatus;
import com.ericsson.oss.services.cm.cmshared.dto.search.CmMatchCondition;
import com.ericsson.oss.services.cm.cmshared.dto.search.CmSearchCriteria;
import com.ericsson.oss.services.cm.cmshared.dto.search.CmSearchScope;
import com.ericsson.oss.services.cm.cmshared.dto.search.CmSearchScope.ScopeType;
import com.ericsson.oss.services.cm.testutil.mo.MeContext;

/**
 * Arquillian tests for the copy method of the CmConfigService.
 */
@Ignore
@SuppressWarnings("PMD.SignatureDeclareThrowsException")
@RunWith(Arquillian.class)
public class CmConfigCopyNodesIT extends CmConfigServiceTestBase {

    private static final String SOURCE_CONFIG = CmConfigTestBuilder.CONFIGURATION_FOR_TEST;
    private static final String SOURCE_CONFIG_EMPTY = "sourceConfigEmpty";
    private static final String TARGET_CONFIG = "targetConfig";

    private DataBucket sourceConfig;
    private DataBucket targetConfig;

    @Override
    protected void setupTest() throws Exception {

        this.cmConfigService.createConfig(SOURCE_CONFIG_EMPTY);
        this.testBuilder.createTestData(SOURCE_CONFIG);
        this.testBuilder.userTransaction.begin();
        this.cmConfigService.createConfig(TARGET_CONFIG);
        this.targetConfig = getConfig(TARGET_CONFIG);
        this.sourceConfig = getConfig(SOURCE_CONFIG);
        this.testBuilder.userTransaction.commit();
    }

    /**
     * <li>unit of work : copy one node.
     * <li>state under test : single node search criteria provided.
     * <li>expected behavior : node copied, correct results.
     */
    @Test
    public void oneCopy() throws Exception {

        final CmSearchCriteria nodes = getNodes(NODE_NAME1);

        final CmCopyResponse response = this.cmConfigService.copyNodes(SOURCE_CONFIG, TARGET_CONFIG, nodes, new CmCopyNodesFlags());

        final CmCopyResponse expectedResponse = new CmCopyResponse();
        expectedResponse.setStatusCode(1);

        assertResponse(expectedResponse, response);

        final CopyResult copyResult = response.getCopyResults().get(0);

        Assert.assertEquals(NODE_NAME1, copyResult.getNodeName());
        Assert.assertEquals(MECONTEXT_FDN1, copyResult.getFdn());
        Assert.assertThat(copyResult.getStatusMessage(), CoreMatchers.equalTo(""));
        Assert.assertEquals(CopyStatus.COPIED, copyResult.getCopyStatus());

        this.testBuilder.userTransaction.begin();
        final ManagedObject originalMo = this.sourceConfig.findMoByFdn(MECONTEXT_FDN1);
        final ManagedObject copiedMo = this.targetConfig.findMoByFdn(MECONTEXT_FDN1);
        this.testBuilder.userTransaction.commit();

        Assert.assertNotNull("Copied MO should exist in target configuration", copiedMo);
        Assert.assertEquals("FDN should be equals", MECONTEXT_FDN1, copiedMo.getFdn());
        Assert.assertEquals("Namespace should be equals", originalMo.getNamespace(), copiedMo.getNamespace());
        Assert.assertNotEquals("PO IDs should be different", originalMo.getPoId(), copiedMo.getPoId());
    }

    /**
     * <li>unit of work : copy one node.
     * <li>state under test : single node search criteria provided.
     * <li>expected behavior : node copied and verify full tree correct results.
     */
    @Test
    public void oneCopyVerifingFullTree() throws Exception {
        final CmSearchCriteria nodes = getNodes(NODE_NAME1);

        final CmCopyResponse response = this.cmConfigService.copyNodes(SOURCE_CONFIG, TARGET_CONFIG, nodes, new CmCopyNodesFlags());

        final CmCopyResponse expectedResponse = new CmCopyResponse();
        expectedResponse.setStatusCode(1);

        assertResponse(expectedResponse, response);

        final CopyResult copyResult = response.getCopyResults().get(0);

        Assert.assertEquals(NODE_NAME1, copyResult.getNodeName());
        Assert.assertEquals(MECONTEXT_FDN1, copyResult.getFdn());
        Assert.assertThat(copyResult.getStatusMessage(), CoreMatchers.equalTo(""));
        Assert.assertEquals(CopyStatus.COPIED, copyResult.getCopyStatus());

        this.testBuilder.userTransaction.begin();

        final List<ManagedObject> fullTreeSourceManagedObjects = getMoHierarchy(this.sourceConfig, MECONTEXT_FDN1);

        for (final ManagedObject sourceMo : fullTreeSourceManagedObjects) {
            final ManagedObject copiedMo = this.targetConfig.findMoByFdn(sourceMo.getFdn());
            Assert.assertNotNull("Copied MO should exist in target configuration", copiedMo);
            Assert.assertEquals("FDN should be equals", sourceMo.getFdn(), copiedMo.getFdn());
            Assert.assertEquals("Namespace should be equals", sourceMo.getNamespace(), copiedMo.getNamespace());
            Assert.assertNotEquals("PO IDs should be different", sourceMo.getPoId(), copiedMo.getPoId());
        }

        this.testBuilder.userTransaction.commit();
    }

    /**
     * <li>unit of work : copy one node, source missing.
     * <li>state under test : single node search criteria provided, node doesn't
     * exist in source.
     * <li>expected behavior : node not copied, not copied in results.
     */
    @Test
    public void oneCopyMissingSource() throws Exception {
        final CmSearchCriteria nodes = getNodes(NODE_NAME1);

        final CmCopyResponse response = this.cmConfigService.copyNodes(SOURCE_CONFIG_EMPTY, TARGET_CONFIG, nodes, new CmCopyNodesFlags());

        final CmCopyResponse expectedResponse = new CmCopyResponse();
        expectedResponse.setStatusCode(1);
        assertResponse(expectedResponse, response);

        final CopyResult copyResult = response.getCopyResults().get(0);

        Assert.assertEquals(NODE_NAME1, copyResult.getNodeName());
        Assert.assertEquals(MECONTEXT_FDN1, copyResult.getFdn());
        Assert.assertEquals(CopyStatus.NOT_COPIED, copyResult.getCopyStatus());
        Assert.assertThat(copyResult.getStatusMessage(), CoreMatchers.containsString("does not exist in the source configuration"));

        this.testBuilder.userTransaction.begin();
        final ManagedObject copiedMo = this.targetConfig.findMoByFdn(MECONTEXT_FDN1);
        this.testBuilder.userTransaction.commit();

        Assert.assertNull(copiedMo);
    }

    /**
     * <li>unit of work : copy one node which already exists in target.
     * <li>state under test : single node search criteria provided, node exists
     * in source and target.
     * <li>expected behavior : node not copied, not copied in results.
     */
    @Test
    public void duplicateCopy() throws Exception {

        final CmSearchCriteria nodes = getNodes(NODE_NAME1);

        final CmCopyResponse response = this.cmConfigService.copyNodes(SOURCE_CONFIG, TARGET_CONFIG, nodes, new CmCopyNodesFlags());
        final CmCopyResponse expectedResponse = new CmCopyResponse();
        expectedResponse.setStatusCode(1);
        assertResponse(expectedResponse, response);

        final CopyResult copyResult = response.getCopyResults().get(0);

        Assert.assertEquals(NODE_NAME1, copyResult.getNodeName());
        Assert.assertEquals(MECONTEXT_FDN1, copyResult.getFdn());
        Assert.assertEquals(CopyStatus.COPIED, copyResult.getCopyStatus());
        Assert.assertThat(copyResult.getStatusMessage(), CoreMatchers.equalTo(""));

        this.testBuilder.userTransaction.begin();
        final ManagedObject originalMo = this.sourceConfig.findMoByFdn(MECONTEXT_FDN1);
        final ManagedObject copiedMo = this.targetConfig.findMoByFdn(MECONTEXT_FDN1);
        this.testBuilder.userTransaction.commit();

        Assert.assertNotNull("Copied MO should exist in target configuration", copiedMo);
        Assert.assertEquals("FDN should be equals", MECONTEXT_FDN1, copiedMo.getFdn());
        Assert.assertEquals("Namespace should be equals", originalMo.getNamespace(), copiedMo.getNamespace());
        Assert.assertNotEquals("PO IDs should be different", originalMo.getPoId(), copiedMo.getPoId());

        final CmCopyResponse secondCopyResponse = this.cmConfigService.copyNodes(SOURCE_CONFIG, TARGET_CONFIG, nodes, new CmCopyNodesFlags());

        expectedResponse.setStatusCode(1);
        assertResponse(expectedResponse, secondCopyResponse);

        final CopyResult secondCopyResult = secondCopyResponse.getCopyResults().get(0);

        Assert.assertEquals(NODE_NAME1, secondCopyResult.getNodeName());
        Assert.assertEquals(MECONTEXT_FDN1, secondCopyResult.getFdn());
        Assert.assertEquals(CopyStatus.NOT_COPIED, secondCopyResult.getCopyStatus());
        Assert.assertThat(secondCopyResult.getStatusMessage(), CoreMatchers.containsString("already exists in the target configuration"));

        this.testBuilder.userTransaction.begin();
        final ManagedObject copiedMoNew = this.targetConfig.findMoByFdn(MECONTEXT_FDN1);
        this.testBuilder.userTransaction.commit();
        Assert.assertEquals("PO IDs should not be different", copiedMoNew.getPoId(), copiedMo.getPoId());
    }

    /**
     * <li>unit of work : overwrite one node which already exists in target by
     * copy.
     * <li>state under test : single node search criteria provided, node exists
     * in source and target.
     * <li>expected behavior : node overwritten. the node in the target will
     * have a new poID.
     */
    @Test
    public void overWriteNodeByCopy() throws Exception {

        final CmSearchCriteria nodes = getNodes(NODE_NAME1);

        this.cmConfigService.copyNodes(SOURCE_CONFIG, TARGET_CONFIG, nodes, new CmCopyNodesFlags());

        this.testBuilder.userTransaction.begin();
        final ManagedObject copiedMo = this.targetConfig.findMoByFdn(MECONTEXT_FDN1);
        this.testBuilder.userTransaction.commit();

        final CmCopyNodesFlags overWriteNode = new CmCopyNodesFlags(EnumSet.of(CmConfigCopy.OVERWRITE));
        final CmCopyResponse secondCopyResponse = this.cmConfigService.copyNodes(SOURCE_CONFIG, TARGET_CONFIG, nodes, overWriteNode);

        final CmCopyResponse expectedResponse = new CmCopyResponse();
        expectedResponse.setStatusCode(1);
        assertResponse(expectedResponse, secondCopyResponse);

        final CopyResult secondCopyResult = secondCopyResponse.getCopyResults().get(0);

        Assert.assertEquals(NODE_NAME1, secondCopyResult.getNodeName());
        Assert.assertEquals(MECONTEXT_FDN1, secondCopyResult.getFdn());
        Assert.assertThat(secondCopyResult.getStatusMessage(), CoreMatchers.equalTo(""));
        Assert.assertEquals(CopyStatus.OVERWRITEN, secondCopyResult.getCopyStatus());

        this.testBuilder.userTransaction.begin();
        final ManagedObject copiedMoNew = this.targetConfig.findMoByFdn(MECONTEXT_FDN1);
        this.testBuilder.userTransaction.commit();
        Assert.assertNotEquals("PO IDs should be different", copiedMoNew.getPoId(), copiedMo.getPoId());
    }

    /**
     * <li>unit of work : copy all nodes.
     * <li>state under test : single node search criteria provided with all
     * nodes "*" wildcard.
     * <li>expected behavior : node copied, correct results.
     */
    @Test
    public void allCopy() throws Exception {

        final CmSearchCriteria nodes = getAllNodes();

        final CmCopyResponse response = this.cmConfigService.copyNodes(SOURCE_CONFIG, TARGET_CONFIG, nodes, new CmCopyNodesFlags());

        final CmCopyResponse expectedResponse = new CmCopyResponse();
        expectedResponse.setStatusCode(3);
        assertResponse(expectedResponse, response);

        this.testBuilder.userTransaction.begin();

        final List<ManagedObject> soureManagedObjects = getAllMeContextMos(this.sourceConfig);

        Assert.assertEquals("The size should be the same", soureManagedObjects.size(), response.getCopyResults().size());

        for (final ManagedObject sourceMo : soureManagedObjects) {
            final ManagedObject copiedMo = this.targetConfig.findMoByFdn(sourceMo.getFdn());

            Assert.assertNotNull("Copied MO should exist in target configuration", copiedMo);
            Assert.assertEquals("FDN should be equals", sourceMo.getFdn(), copiedMo.getFdn());
            Assert.assertEquals("Namespace should be equals", sourceMo.getNamespace(), copiedMo.getNamespace());
            Assert.assertNotEquals("PO IDs should be different", sourceMo.getPoId(), copiedMo.getPoId());

            final CopyResult copyResult = findCopyResult(sourceMo.getFdn(), response.getCopyResults());
            Assert.assertEquals("Result FDN should be equals", sourceMo.getFdn(), copyResult.getFdn());
            Assert.assertEquals("Result Name should be equals", sourceMo.getName(), copyResult.getNodeName());
            Assert.assertEquals("Status should be COPIED", CopyStatus.COPIED, copyResult.getCopyStatus());
        }
        this.testBuilder.userTransaction.commit();
    }

    private CopyResult findCopyResult(final String fdn, final List<CopyResult> copyResults) {
        for (final CopyResult copyResult : copyResults) {
            if (fdn.equals(copyResult.getFdn())) {
                return copyResult;
            }
        }
        return null;
    }

    private DataBucket getConfig(final String configName) throws Exception {
        return this.dataPersistenceServiceProxy.getDataPersistenceService().getDataBucket(configName);
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

    private CmSearchCriteria getAllNodes() {
        final CmSearchCriteria nodes = new CmSearchCriteria();
        final List<CmSearchScope> scopes = new ArrayList<>();
        final CmSearchScope node = new CmSearchScope();
        node.setName(MeContext.ME_CONTEXT_MO_NAME);
        node.setScopeType(ScopeType.UNSPECIFIED);
        scopes.add(node);
        nodes.setCmSearchScopes(scopes);
        return nodes;
    }

    private CmSearchScope getNode(final String nodeName) {
        final CmSearchScope node = new CmSearchScope();
        node.setName(MeContext.ME_CONTEXT_MO_NAME);
        node.setValue(nodeName);
        node.setScopeType(ScopeType.NODE_NAME);
        node.setCmMatchCondition(CmMatchCondition.EQUALS);
        return node;
    }

}
