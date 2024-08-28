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

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Matchers;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.runners.MockitoJUnitRunner;

import com.ericsson.oss.itpf.datalayer.dps.DataBucket;
import com.ericsson.oss.itpf.datalayer.dps.NonLiveDataBucket;
import com.ericsson.oss.itpf.datalayer.dps.exception.DpsTransactionRollbackException;
import com.ericsson.oss.itpf.datalayer.dps.exception.general.AlreadyDefinedException;
import com.ericsson.oss.itpf.datalayer.dps.exception.general.DpsIllegalArgumentException;
import com.ericsson.oss.itpf.datalayer.dps.exception.general.DpsIllegalStateException;
import com.ericsson.oss.itpf.datalayer.dps.persistence.ManagedObject;
import com.ericsson.oss.services.cm.cmconfig.service.api.CmCopyNodesFlags;
import com.ericsson.oss.services.cm.cmconfig.service.api.CmCopyNodesFlags.CmConfigCopy;
import com.ericsson.oss.services.cm.cmconfig.service.api.CopyResult;
import com.ericsson.oss.services.cm.cmconfig.service.api.CopyResult.CopyStatus;
import com.ericsson.oss.services.cm.cmconfig.service.ejb.util.CmConfigDpsUtil;
import com.ericsson.oss.services.cm.cmconfig.service.log.CmConfigServiceMessages;
import com.ericsson.oss.services.cm.cmconfig.service.validation.ValidationConfigService;
import com.ericsson.oss.services.cm.cmconfig.service.validation.ValidationConfigServiceBean;
import com.ericsson.oss.services.cm.cmconfig.service.validation.ValidationException;
import com.ericsson.oss.services.cm.cmshared.dto.search.CmMatchCondition;
import com.ericsson.oss.services.cm.cmshared.dto.search.CmSearchScope;
import com.ericsson.oss.services.cm.cmshared.dto.search.CmSearchScope.ScopeType;

/**
 * Test for the {@code CopyNodes}.
 */
@RunWith(MockitoJUnitRunner.class)
public class CopyNodesTest {

    private static final String NODE_1_NAME = "RBS1";
    private static final String NODE_2_NAME = "RBS2";
    private static final String NODE_3_NAME = "RBS3";
    private static final String ME_CONTEXT = "MeContext";
    @Rule
    public final ExpectedException exception = ExpectedException.none();

    @Mock
    private DataBucket mockSource;

    @Mock
    private NonLiveDataBucket mockTarget;

    @Mock
    private ManagedObject mockNode1;

    @Mock
    private ManagedObject mockNode2;

    @Mock
    private ManagedObject mockNode3;

    @Mock
    private CmConfigDpsUtil mockDpsUtil;

    @SuppressWarnings("PMD.UnusedPrivateField")
    @Spy
    private final ValidationConfigService validationConfigService = new ValidationConfigServiceBean();

    @InjectMocks
    private final CopyNodesBean bean = new CopyNodesBean();

    @Before
    public void setup() {

        Mockito.when(this.mockNode1.getFdn()).thenReturn(ME_CONTEXT + "=" + NODE_1_NAME);
        Mockito.when(this.mockNode2.getFdn()).thenReturn(ME_CONTEXT + "=" + NODE_2_NAME);
        Mockito.when(this.mockNode3.getFdn()).thenReturn(ME_CONTEXT + "=" + NODE_3_NAME);

        Mockito.when(this.mockNode1.getName()).thenReturn(NODE_1_NAME);
        Mockito.when(this.mockNode2.getName()).thenReturn(NODE_2_NAME);
        Mockito.when(this.mockNode3.getName()).thenReturn(NODE_3_NAME);
    }

    /**
     * <li>unit of work: copy nodes.
     * <li>state under test: empty search scope provided.
     * <li>expected behavior: empty list returned.
     */
    @Test
    public void copyNodesEmptySearchScope() {
        final List<CopyResult> result = this.bean.processCopyNodes(this.mockSource, this.mockTarget, new ArrayList<CmSearchScope>(),
                new CmCopyNodesFlags());
        Assert.assertEquals(0, result.size());
    }

    /**
     * <li>unit of work: copy node.
     * <li>state under test: one node in search scope provided.
     * <li>expected behavior: one entry in list returned,
     * with copied message.
     */
    @Test
    public void copyOneNode_CorrectResult() {

        final List<ManagedObject> sourceNodes = new ArrayList<>();
        sourceNodes.add(this.mockNode1);

        Mockito.when(this.mockDpsUtil.findNesFromSearchScope(this.mockSource, getSingleSearchScope().get(0))).thenReturn(sourceNodes);
        Mockito.when(this.mockDpsUtil.getManagedElementByFdn(ME_CONTEXT + "=" + NODE_1_NAME, this.mockTarget)).thenReturn(null);

        final List<CopyResult> result = this.bean.processCopyNodes(this.mockSource, this.mockTarget, getSingleSearchScope(), new CmCopyNodesFlags());
        Assert.assertEquals(1, result.size());
        Assert.assertEquals(NODE_1_NAME, result.get(0).getNodeName());
        Assert.assertEquals(ME_CONTEXT + "=" + NODE_1_NAME, result.get(0).getFdn());
        Assert.assertEquals(CopyStatus.COPIED, result.get(0).getCopyStatus());
        Assert.assertEquals("", result.get(0).getStatusMessage());
        Mockito.verify(this.mockTarget).copyInFullTree(this.mockNode1);
    }

    /**
     * <li>unit of work: copy node with overwrite.
     * <li>state under test: one node in search scope provided, overwrite flag
     * is set.
     * <li>expected behavior: one entry in list returned, with overwrite
     * message.
     */
    @Test
    public void copyOneNodeOverwrite_CorrectResult() {

        final List<ManagedObject> sourceNodes = new ArrayList<>();
        sourceNodes.add(this.mockNode1);

        Mockito.when(this.mockDpsUtil.findNesFromSearchScope(this.mockSource, getSingleSearchScope().get(0))).thenReturn(sourceNodes);
        Mockito.when(this.mockDpsUtil.getManagedElementByFdn(ME_CONTEXT + "=" + NODE_1_NAME, this.mockTarget)).thenReturn(this.mockNode1);

        final List<CopyResult> result = this.bean.processCopyNodes(this.mockSource, this.mockTarget, getSingleSearchScope(), new CmCopyNodesFlags(
                EnumSet.of(CmConfigCopy.OVERWRITE)));

        Mockito.verify(this.mockDpsUtil).deleteManagedElementByMoReference(this.mockNode1, this.mockTarget);
        Mockito.verify(this.mockTarget).copyInFullTree(this.mockNode1);

        Assert.assertEquals(1, result.size());
        Assert.assertEquals(NODE_1_NAME, result.get(0).getNodeName());
        Assert.assertEquals(ME_CONTEXT + "=" + NODE_1_NAME, result.get(0).getFdn());
        Assert.assertEquals(CopyStatus.OVERWRITEN, result.get(0).getCopyStatus());
        Assert.assertEquals("", result.get(0).getStatusMessage());
    }

    /**
     * <li>unit of work: copy node, without overwrite.
     * <li>state under test: one node in search scope provided, overwrite flag
     * is NOT set, MO exists in target.
     * <li>expected behavior: one entry in list returned, with error message for
     * correct node.
     */
    @Test
    public void copyOneNodeCannotOverwrite_CorrectError() {
        final List<ManagedObject> sourceNodes = new ArrayList<>();
        sourceNodes.add(this.mockNode1);

        Mockito.when(this.mockDpsUtil.findNesFromSearchScope(this.mockSource, getSingleSearchScope().get(0))).thenReturn(sourceNodes);
        Mockito.when(this.mockDpsUtil.getManagedElementByFdn(ME_CONTEXT + "=" + NODE_1_NAME, this.mockTarget)).thenReturn(this.mockNode1);

        Mockito.when(this.mockDpsUtil.findNesFromSearchScope(this.mockSource, getSingleSearchScope().get(0))).thenReturn(sourceNodes);
        Mockito.when(this.mockDpsUtil.getManagedElementByFdn(ME_CONTEXT + "=" + NODE_1_NAME, this.mockTarget)).thenReturn(this.mockNode1);

        final List<CopyResult> result = this.bean.processCopyNodes(this.mockSource, this.mockTarget, getSingleSearchScope(), new CmCopyNodesFlags());

        Mockito.verify(this.mockDpsUtil, Mockito.never()).deleteManagedElementByMoReference(this.mockNode1, this.mockTarget);
        Mockito.verify(this.mockTarget, Mockito.never()).copyInFullTree(Matchers.any(ManagedObject.class));

        Assert.assertEquals(1, result.size());
        Assert.assertEquals(NODE_1_NAME, result.get(0).getNodeName());
        Assert.assertEquals(ME_CONTEXT + "=" + NODE_1_NAME, result.get(0).getFdn());
        Assert.assertEquals(CopyStatus.NOT_COPIED, result.get(0).getCopyStatus());
        Assert.assertEquals(CmConfigServiceMessages.CONFIG_COPY_NODE_EXIST_IN_TARGET, result.get(0).getStatusMessage());
    }

    /**
     * <li>unit of work: copy node, which is not in source.
     * <li>state under test: one node in search scope provided, MO does not
     * exist in source.
     * <li>expected behavior: one entry in list returned, with error message for
     * correct node.
     */
    @Test
    public void copyOneNodeNotInSource_CorrectError() {

        final List<ManagedObject> emptySourceNodes = new ArrayList<>();
        Mockito.when(this.mockDpsUtil.findNesFromSearchScope(this.mockSource, getSingleSearchScope().get(0))).thenReturn(emptySourceNodes);
        Mockito.when(this.mockDpsUtil.getFDN(getSingleSearchScope().get(0))).thenReturn(ME_CONTEXT + "=" + NODE_1_NAME);

        final List<CopyResult> result = this.bean.processCopyNodes(this.mockSource, this.mockTarget, getSingleSearchScope(), new CmCopyNodesFlags());

        Mockito.verify(this.mockTarget, Mockito.never()).copyInFullTree(Matchers.any(ManagedObject.class));
        Assert.assertEquals(1, result.size());
        Assert.assertEquals(NODE_1_NAME, result.get(0).getNodeName());
        Assert.assertEquals(ME_CONTEXT + "=" + NODE_1_NAME, result.get(0).getFdn());
        Assert.assertEquals(CopyStatus.NOT_COPIED, result.get(0).getCopyStatus());
        Assert.assertEquals(CmConfigServiceMessages.CONFIG_COPY_NODE_DOES_NOT_EXIST_IN_SOURCE, result.get(0).getStatusMessage());
    }

    /**
     * <li>unit of work: copy nodes.
     * <li>state under test: three node in search scope provided.
     * <li>expected behavior: three entry in list
     * returned, with copied messages.
     */
    @Test
    public void copyThreeNodes_CorrectResult() {

        final List<ManagedObject> sourceNodes1 = new ArrayList<>();
        sourceNodes1.add(this.mockNode1);

        final List<ManagedObject> sourceNodes2 = new ArrayList<>();
        sourceNodes2.add(this.mockNode2);

        final List<ManagedObject> sourceNodes3 = new ArrayList<>();
        sourceNodes3.add(this.mockNode3);

        Mockito.when(this.mockDpsUtil.findNesFromSearchScope(this.mockSource, getThreeSearchScopes().get(0))).thenReturn(sourceNodes1);
        Mockito.when(this.mockDpsUtil.findNesFromSearchScope(this.mockSource, getThreeSearchScopes().get(1))).thenReturn(sourceNodes2);
        Mockito.when(this.mockDpsUtil.findNesFromSearchScope(this.mockSource, getThreeSearchScopes().get(2))).thenReturn(sourceNodes3);

        Mockito.when(this.mockDpsUtil.getManagedElementByFdn(ME_CONTEXT + "=" + NODE_1_NAME, this.mockTarget)).thenReturn(null);
        Mockito.when(this.mockDpsUtil.getManagedElementByFdn(ME_CONTEXT + "=" + NODE_2_NAME, this.mockTarget)).thenReturn(null);
        Mockito.when(this.mockDpsUtil.getManagedElementByFdn(ME_CONTEXT + "=" + NODE_3_NAME, this.mockTarget)).thenReturn(null);

        final List<CopyResult> copyResults = this.bean.processCopyNodes(this.mockSource, this.mockTarget, getThreeSearchScopes(),
                new CmCopyNodesFlags());

        Mockito.verify(this.mockTarget).copyInFullTree(this.mockNode1);
        Mockito.verify(this.mockTarget).copyInFullTree(this.mockNode2);
        Mockito.verify(this.mockTarget).copyInFullTree(this.mockNode3);

        Assert.assertEquals(3, copyResults.size());

        final CopyResult node1Result = findResultByName(copyResults, NODE_1_NAME);
        final CopyResult node2Result = findResultByName(copyResults, NODE_2_NAME);
        final CopyResult node3Result = findResultByName(copyResults, NODE_3_NAME);

        Assert.assertEquals(ME_CONTEXT + "=" + NODE_1_NAME, node1Result.getFdn());
        Assert.assertEquals(ME_CONTEXT + "=" + NODE_2_NAME, node2Result.getFdn());
        Assert.assertEquals(ME_CONTEXT + "=" + NODE_3_NAME, node3Result.getFdn());

        Assert.assertEquals(CopyStatus.COPIED, node1Result.getCopyStatus());
        Assert.assertEquals(CopyStatus.COPIED, node2Result.getCopyStatus());
        Assert.assertEquals(CopyStatus.COPIED, node3Result.getCopyStatus());

        Assert.assertEquals("", node1Result.getStatusMessage());
        Assert.assertEquals("", node2Result.getStatusMessage());
        Assert.assertEquals("", node3Result.getStatusMessage());
    }

    /**
     * <li>unit of work: copy nodes.
     * <li>state under test: three node in search scope provided, one not in
     * source, one already in target
     * <li>expected behavior: three entry in list returned, with correct
     * messages.
     */
    @Test
    public void copyThreeNodes_NotInSourceOverwritenAndCopied_CorrectResult() {

        final List<ManagedObject> sourceNodes1 = new ArrayList<>();

        final List<ManagedObject> sourceNodes2 = new ArrayList<>();
        sourceNodes2.add(this.mockNode2);

        final List<ManagedObject> sourceNodes3 = new ArrayList<>();
        sourceNodes3.add(this.mockNode3);

        Mockito.when(this.mockDpsUtil.findNesFromSearchScope(this.mockSource, getThreeSearchScopes().get(0))).thenReturn(sourceNodes1);
        Mockito.when(this.mockDpsUtil.findNesFromSearchScope(this.mockSource, getThreeSearchScopes().get(1))).thenReturn(sourceNodes2);
        Mockito.when(this.mockDpsUtil.findNesFromSearchScope(this.mockSource, getThreeSearchScopes().get(2))).thenReturn(sourceNodes3);

        Mockito.when(this.mockDpsUtil.getFDN(getThreeSearchScopes().get(0))).thenReturn(ME_CONTEXT + "=" + NODE_1_NAME);
        Mockito.when(this.mockDpsUtil.getManagedElementByFdn(ME_CONTEXT + "=" + NODE_1_NAME, this.mockTarget)).thenReturn(null);
        Mockito.when(this.mockDpsUtil.getManagedElementByFdn(ME_CONTEXT + "=" + NODE_2_NAME, this.mockTarget)).thenReturn(this.mockNode2);
        Mockito.when(this.mockDpsUtil.getManagedElementByFdn(ME_CONTEXT + "=" + NODE_3_NAME, this.mockTarget)).thenReturn(null);

        final List<CopyResult> copyResults = this.bean.processCopyNodes(this.mockSource, this.mockTarget, getThreeSearchScopes(),
                new CmCopyNodesFlags(EnumSet.of(CmConfigCopy.OVERWRITE)));
        Assert.assertEquals(3, copyResults.size());

        final CopyResult node1Result = findResultByName(copyResults, NODE_1_NAME);
        final CopyResult node2Result = findResultByName(copyResults, NODE_2_NAME);
        final CopyResult node3Result = findResultByName(copyResults, NODE_3_NAME);

        Assert.assertEquals(ME_CONTEXT + "=" + NODE_1_NAME, node1Result.getFdn());
        Assert.assertEquals(ME_CONTEXT + "=" + NODE_2_NAME, node2Result.getFdn());
        Assert.assertEquals(ME_CONTEXT + "=" + NODE_3_NAME, node3Result.getFdn());

        Assert.assertEquals(CopyStatus.NOT_COPIED, node1Result.getCopyStatus());
        Assert.assertEquals(CopyStatus.OVERWRITEN, node2Result.getCopyStatus());
        Assert.assertEquals(CopyStatus.COPIED, node3Result.getCopyStatus());

        Assert.assertEquals(CmConfigServiceMessages.CONFIG_COPY_NODE_DOES_NOT_EXIST_IN_SOURCE, node1Result.getStatusMessage());
        Assert.assertEquals("", node2Result.getStatusMessage());
        Assert.assertEquals("", node3Result.getStatusMessage());

        Mockito.verify(this.mockTarget).copyInFullTree(this.mockNode2);
        Mockito.verify(this.mockTarget).copyInFullTree(this.mockNode3);
        Mockito.verify(this.mockTarget, Mockito.times(2)).copyInFullTree(Matchers.any(ManagedObject.class));
    }

    /**
     * <li>unit of work: copy using wildcards.
     * <li>state under test: search scopes with wild cards that overlap supplied
     * (*BS and *S).
     * <li>expected behavior: only one instance of the node copied and reported.
     */
    @SuppressWarnings("unchecked")
    @Test
    public void copyOverlappingScopes() {

        final List<ManagedObject> nodesList = new ArrayList<>();
        nodesList.add(this.mockNode1);
        nodesList.add(this.mockNode2);

        Mockito.when(this.mockDpsUtil.findNesFromSearchScope(this.mockSource, getTwoWildcardSearchScopes().get(0))).thenReturn(nodesList);
        Mockito.when(this.mockDpsUtil.findNesFromSearchScope(this.mockSource, getTwoWildcardSearchScopes().get(1))).thenReturn(nodesList);

        final List<CopyResult> copyResults = this.bean.processCopyNodes(this.mockSource, this.mockTarget, getTwoWildcardSearchScopes(),
                new CmCopyNodesFlags());

        Assert.assertEquals(2, copyResults.size());

        final CopyResult node1Result = findResultByName(copyResults, NODE_1_NAME);
        final CopyResult node2Result = findResultByName(copyResults, NODE_2_NAME);

        Assert.assertEquals(ME_CONTEXT + "=" + NODE_1_NAME, node1Result.getFdn());
        Assert.assertEquals(ME_CONTEXT + "=" + NODE_2_NAME, node2Result.getFdn());
        Assert.assertEquals(CopyStatus.COPIED, node1Result.getCopyStatus());
        Assert.assertEquals(CopyStatus.COPIED, node2Result.getCopyStatus());
        Assert.assertEquals("", node1Result.getStatusMessage());
        Assert.assertEquals("", node2Result.getStatusMessage());

        Mockito.verify(this.mockTarget).copyInFullTree(this.mockNode1);
        Mockito.verify(this.mockTarget).copyInFullTree(this.mockNode2);
    }

    /**
     * <li>unit of work: copy using all nodes "*" wild card.
     * <li>state under test: search scopes with the all nodes wild card.
     * <li>expected behavior: only one instance of the node copied and reported.
     */
    @SuppressWarnings("unchecked")
    @Test
    public void copyAllNodesWilcard() {

        final List<ManagedObject> nodesList = new ArrayList<>();
        nodesList.add(this.mockNode1);
        nodesList.add(this.mockNode2);

        Mockito.when(this.mockDpsUtil.findNesFromSearchScope(this.mockSource, getOneAllNodesWildcardSearchScopes().get(0))).thenReturn(nodesList);

        Mockito.when(this.mockDpsUtil.findNesFromSearchScope(this.mockSource, getOneAllNodesWildcardSearchScopes().get(0))).thenReturn(nodesList);

        final List<CopyResult> copyResults = this.bean.processCopyNodes(this.mockSource, this.mockTarget, getOneAllNodesWildcardSearchScopes(),
                new CmCopyNodesFlags());

        Assert.assertEquals(2, copyResults.size());

        final CopyResult node1Result = findResultByName(copyResults, NODE_1_NAME);
        final CopyResult node2Result = findResultByName(copyResults, NODE_2_NAME);

        Assert.assertEquals(ME_CONTEXT + "=" + NODE_1_NAME, node1Result.getFdn());
        Assert.assertEquals(ME_CONTEXT + "=" + NODE_2_NAME, node2Result.getFdn());
        Assert.assertEquals(CopyStatus.COPIED, node1Result.getCopyStatus());
        Assert.assertEquals(CopyStatus.COPIED, node2Result.getCopyStatus());
        Assert.assertEquals("", node1Result.getStatusMessage());
        Assert.assertEquals("", node2Result.getStatusMessage());

        Mockito.verify(this.mockTarget).copyInFullTree(this.mockNode1);
        Mockito.verify(this.mockTarget).copyInFullTree(this.mockNode2);
    }

    /**
     * <li>unit of work: copy using wildcards, not found.
     * <li>state under test: search scopes using ends with supplied, matches
     * nothing.
     * <li>expected behavior: result has *END-OF-NODE-NAME.
     */
    @SuppressWarnings("unchecked")
    @Test
    public void copyNoWildcardsFoundEndsWith() {
        final CmSearchScope scope = new CmSearchScope();
        scope.setScopeType(ScopeType.NODE_NAME);
        scope.setName(ME_CONTEXT);
        scope.setValue(NODE_1_NAME.substring(1, 4));
        scope.setCmMatchCondition(CmMatchCondition.ENDS_WITH);

        final List<CmSearchScope> scopes = new ArrayList<>();
        scopes.add(scope);

        final List<ManagedObject> emptyNodesList = new ArrayList<>();
        Mockito.when(this.mockDpsUtil.findNesFromSearchScope(this.mockSource, scope)).thenReturn(emptyNodesList);

        final List<CopyResult> copyResults = this.bean.processCopyNodes(this.mockSource, this.mockTarget, scopes, new CmCopyNodesFlags());

        final CopyResult node1Result = findResultByName(copyResults, "*" + NODE_1_NAME.substring(1, 4));

        Assert.assertEquals("", node1Result.getFdn());
        Assert.assertEquals(CopyStatus.NO_MATCHES_FOUND, node1Result.getCopyStatus());
        Assert.assertEquals("", node1Result.getStatusMessage());

        Mockito.verify(this.mockTarget, Mockito.never()).copyInFullTree(Matchers.any(ManagedObject.class));
    }

    /**
     * <li>unit of work: copy using wildcards, not found.
     * <li>state under test: search scopes using starts with supplied, matches
     * nothing.
     * <li>expected behavior: result has START-OF-NODE-NAME*.
     */
    @SuppressWarnings("unchecked")
    @Test
    public void copyNoWildcardsFoundStartsWith() {

        final CmSearchScope scope = new CmSearchScope();
        scope.setScopeType(ScopeType.NODE_NAME);
        scope.setName(ME_CONTEXT);
        scope.setValue(NODE_1_NAME.substring(0, 3));
        scope.setCmMatchCondition(CmMatchCondition.STARTS_WITH);

        final List<CmSearchScope> scopes = new ArrayList<>();
        scopes.add(scope);

        final List<ManagedObject> emptyNodesList = new ArrayList<>();
        Mockito.when(this.mockDpsUtil.findNesFromSearchScope(this.mockSource, scope)).thenReturn(emptyNodesList);

        final List<CopyResult> copyResults = this.bean.processCopyNodes(this.mockSource, this.mockTarget, scopes, new CmCopyNodesFlags());

        final CopyResult node1Result = findResultByName(copyResults, NODE_1_NAME.substring(0, 3) + "*");

        Assert.assertEquals("", node1Result.getFdn());
        Assert.assertEquals(CopyStatus.NO_MATCHES_FOUND, node1Result.getCopyStatus());
        Assert.assertEquals("", node1Result.getStatusMessage());

        Mockito.verify(this.mockTarget, Mockito.never()).copyInFullTree(Matchers.any(ManagedObject.class));
    }

    /**
     * <li>unit of work: copy using wildcards, not found.
     * <li>state under test: search scopes using contains with supplied, matches
     * nothing.
     * <li>expected behavior: result has *MIDDLE-OF-NODE-NAME*.
     */
    @SuppressWarnings("unchecked")
    @Test
    public void copyNoWildcardsFoundContains() {

        final CmSearchScope scope = new CmSearchScope();
        scope.setScopeType(ScopeType.NODE_NAME);
        scope.setName(ME_CONTEXT);
        scope.setValue(NODE_1_NAME.substring(1, 3));
        scope.setCmMatchCondition(CmMatchCondition.CONTAINS);

        final List<CmSearchScope> scopes = new ArrayList<>();
        scopes.add(scope);

        final List<ManagedObject> emptyNodesList = new ArrayList<>();
        Mockito.when(this.mockDpsUtil.findNesFromSearchScope(this.mockSource, scope)).thenReturn(emptyNodesList);

        final List<CopyResult> copyResults = this.bean.processCopyNodes(this.mockSource, this.mockTarget, scopes, new CmCopyNodesFlags());

        final CopyResult node1Result = findResultByName(copyResults, "*" + NODE_1_NAME.substring(1, 3) + "*");

        Assert.assertEquals("", node1Result.getFdn());
        Assert.assertEquals(CopyStatus.NO_MATCHES_FOUND, node1Result.getCopyStatus());
        Assert.assertEquals("", node1Result.getStatusMessage());

        Mockito.verify(this.mockTarget, Mockito.never()).copyInFullTree(Matchers.any(ManagedObject.class));
    }

    /**
     * <li>unit of work: copy using all nodes wildcard, not found.
     * <li>state under test: search scopes scope type unspecified
     * nothing.
     * <li>expected behavior: result has the *.
     */
    @SuppressWarnings("unchecked")
    @Test
    public void copyNoWildcardsFoundAllNodes() {

        final CmSearchScope scope = new CmSearchScope();
        scope.setScopeType(ScopeType.UNSPECIFIED);
        scope.setName(ME_CONTEXT);

        final List<CmSearchScope> scopes = new ArrayList<>();
        scopes.add(scope);

        final List<ManagedObject> emptyNodesList = new ArrayList<>();
        Mockito.when(this.mockDpsUtil.findNesFromSearchScope(this.mockSource, scope)).thenReturn(emptyNodesList);

        final List<CopyResult> copyResults = this.bean.processCopyNodes(this.mockSource, this.mockTarget, scopes, new CmCopyNodesFlags());

        final CopyResult node1Result = findResultByName(copyResults, "*");

        Assert.assertEquals("", node1Result.getFdn());
        Assert.assertEquals(CopyStatus.NO_MATCHES_FOUND, node1Result.getCopyStatus());
        Assert.assertEquals("", node1Result.getStatusMessage());

        Mockito.verify(this.mockTarget, Mockito.never()).copyInFullTree(Matchers.any(ManagedObject.class));
    }

    /**
     * <li>unit of work: copy node.
     * <li>state under test: one node in search scope provided.
     * <li>expected behavior: ValidationException with correct message.
     */
    @Test
    public void copyOneNode_ThrowDpsIllegalArgumentException() {

        final List<ManagedObject> sourceNodes = new ArrayList<>();
        sourceNodes.add(this.mockNode1);
        Mockito.when(this.mockDpsUtil.findNesFromSearchScope(this.mockSource, getSingleSearchScope().get(0))).thenReturn(sourceNodes);
        Mockito.when(this.mockDpsUtil.getManagedElementByFdn(ME_CONTEXT + "=" + NODE_1_NAME, this.mockTarget)).thenReturn(null);
        Mockito.when(this.mockNode1.getName()).thenReturn(NODE_1_NAME);
        Mockito.doThrow(DpsIllegalArgumentException.class).when(this.mockTarget).copyInFullTree(this.mockNode1);

        this.exception.expect(ValidationException.class);

        this.bean.processCopyNodes(this.mockSource, this.mockTarget, getSingleSearchScope(), new CmCopyNodesFlags());

        final MessageFormat messageFormat = new MessageFormat(MANAGED_OBJECT_IS_NOT_TOP_ROOT_ERROR);
        this.exception.expectMessage(messageFormat.format(new Object[] { NODE_1_NAME }));
    }

    /**
     * <li>unit of work: copy node.
     * <li>state under test: one node in search scope provided.
     * <li>expected behavior: ValidationException with correct message.
     */
    @Test
    public void copyOneNode_ThrowAlreadyDefinedException() {
        final List<ManagedObject> sourceNodes = new ArrayList<>();
        sourceNodes.add(this.mockNode1);

        Mockito.when(this.mockDpsUtil.findNesFromSearchScope(this.mockSource, getSingleSearchScope().get(0))).thenReturn(sourceNodes);
        Mockito.when(this.mockDpsUtil.getManagedElementByFdn(ME_CONTEXT + "=" + NODE_1_NAME, this.mockTarget)).thenReturn(null);
        Mockito.when(this.mockNode1.getName()).thenReturn(NODE_1_NAME);
        Mockito.doThrow(AlreadyDefinedException.class).when(this.mockTarget).copyInFullTree(this.mockNode1);

        this.exception.expect(ValidationException.class);

        this.bean.processCopyNodes(this.mockSource, this.mockTarget, getSingleSearchScope(), new CmCopyNodesFlags());

        final MessageFormat messageFormat = new MessageFormat(MANAGED_OBJECT_ALREADY_EXISTS);
        this.exception.expectMessage(messageFormat.format(new Object[] { NODE_1_NAME }));
    }

    /**
     * <li>unit of work: copy node.
     * <li>state under test: one node in search scope provided.
     * <li>expected behavior: ValidationException with correct message.
     */
    @Test
    public void copyOneNode_ThrowDpsIllegalStateException() {

        final List<ManagedObject> sourceNodes = new ArrayList<>();
        sourceNodes.add(this.mockNode1);

        Mockito.when(this.mockDpsUtil.findNesFromSearchScope(this.mockSource, getSingleSearchScope().get(0))).thenReturn(sourceNodes);
        Mockito.when(this.mockDpsUtil.getManagedElementByFdn(ME_CONTEXT + "=" + NODE_1_NAME, this.mockTarget)).thenReturn(null);

        Mockito.when(this.mockNode1.getName()).thenReturn(NODE_1_NAME);
        Mockito.doThrow(DpsIllegalStateException.class).when(this.mockTarget).copyInFullTree(this.mockNode1);

        this.exception.expect(ValidationException.class);

        this.bean.processCopyNodes(this.mockSource, this.mockTarget, getSingleSearchScope(), new CmCopyNodesFlags());

        final MessageFormat messageFormat = new MessageFormat(MANAGED_OBJECT_DELETED_OR_READ_ONLY_BUCKET);
        this.exception.expectMessage(messageFormat.format(new Object[] { NODE_1_NAME }));
    }

    /**
     * <li>unit of work: copy node.
     * <li>state under test: one node in search scope provided.
     * <li>expected behavior: ValidationException with correct message.
     */
    @Test
    public void copyOneNode_ThrowDpsTransactionRollbackException() {
        final List<ManagedObject> sourceNodes = new ArrayList<>();
        sourceNodes.add(this.mockNode1);

        Mockito.when(this.mockDpsUtil.findNesFromSearchScope(this.mockSource, getSingleSearchScope().get(0))).thenReturn(sourceNodes);
        Mockito.when(this.mockDpsUtil.getManagedElementByFdn(ME_CONTEXT + "=" + NODE_1_NAME, this.mockTarget)).thenReturn(null);

        Mockito.when(this.mockNode1.getName()).thenReturn(NODE_1_NAME);
        final DpsTransactionRollbackException mockException = Mockito.mock(DpsTransactionRollbackException.class, Mockito.CALLS_REAL_METHODS);
        Mockito.doThrow(mockException).when(this.mockTarget).copyInFullTree(this.mockNode1);

        this.exception.expect(ValidationException.class);

        this.bean.processCopyNodes(this.mockSource, this.mockTarget, getSingleSearchScope(), new CmCopyNodesFlags());

        final MessageFormat messageFormat = new MessageFormat(MANAGED_OBJECT_COPY_ERROR);
        this.exception.expectMessage(messageFormat.format(new Object[] { NODE_1_NAME }));
    }

    private List<CmSearchScope> getSingleSearchScope() {
        final List<CmSearchScope> searchScopes = new ArrayList<CmSearchScope>();
        searchScopes.add(getSearchScope(NODE_1_NAME));
        return searchScopes;
    }

    private List<CmSearchScope> getThreeSearchScopes() {
        final List<CmSearchScope> searchScopes = new ArrayList<CmSearchScope>();
        searchScopes.add(getSearchScope(NODE_1_NAME));
        searchScopes.add(getSearchScope(NODE_2_NAME));
        searchScopes.add(getSearchScope(NODE_3_NAME));
        return searchScopes;
    }

    private CmSearchScope getSearchScope(final String nodeName) {
        final CmSearchScope scope = new CmSearchScope();
        scope.setScopeType(ScopeType.NODE_NAME);
        scope.setName(ME_CONTEXT);
        scope.setValue(nodeName);
        scope.setCmMatchCondition(CmMatchCondition.EQUALS);
        return scope;
    }

    private List<CmSearchScope> getTwoWildcardSearchScopes() {
        final List<CmSearchScope> searchScopes = new ArrayList<CmSearchScope>();
        searchScopes.add(getWildcardSearchScope(NODE_1_NAME.substring(0, 1)));
        searchScopes.add(getWildcardSearchScope(NODE_2_NAME.substring(0, 2)));
        return searchScopes;
    }

    private List<CmSearchScope> getOneAllNodesWildcardSearchScopes() {
        final List<CmSearchScope> searchScopes = new ArrayList<CmSearchScope>();
        final CmSearchScope scope = new CmSearchScope();
        scope.setScopeType(ScopeType.UNSPECIFIED);
        searchScopes.add(scope);
        return searchScopes;
    }

    private CmSearchScope getWildcardSearchScope(final String partOfName) {
        final CmSearchScope scope = new CmSearchScope();
        scope.setScopeType(ScopeType.NODE_NAME);
        scope.setName(ME_CONTEXT);
        scope.setValue(partOfName);
        scope.setCmMatchCondition(CmMatchCondition.STARTS_WITH);
        return scope;
    }

    private CopyResult findResultByName(final List<CopyResult> results, final String nodeName) {
        for (final CopyResult result : results) {
            if (result.getNodeName().equals(nodeName)) {
                return result;
            }
        }
        Assert.fail("Node " + nodeName + " not found in results");
        return null;
    }
}
