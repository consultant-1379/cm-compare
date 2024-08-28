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

import static com.ericsson.oss.services.cm.testutil.TestDataBuilder.NODE_NAME1;

import java.lang.management.ManagementFactory;

import javax.management.MBeanInfo;
import javax.management.MBeanServer;
import javax.management.ObjectName;

import org.jboss.arquillian.junit.Arquillian;
import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.ericsson.oss.services.cm.cmconfig.service.api.CmCopyNodesFlags;
import com.ericsson.oss.services.cm.cmshared.dto.search.CmMatchCondition;
import com.ericsson.oss.services.cm.cmshared.dto.search.CmSearchCriteria;
import com.ericsson.oss.services.cm.cmshared.dto.search.CmSearchScope;
import com.ericsson.oss.services.cm.cmshared.dto.search.CmSearchScope.ScopeType;
import com.ericsson.oss.services.cm.testutil.mo.MeContext;

/**
 * Arquillian test for metrics component.
 */
@Ignore
@RunWith(Arquillian.class)
@SuppressWarnings("PMD.SignatureDeclareThrowsException")
public class CmConfigMetricsIT extends CmConfigServiceTestBase {

    private static final String CREATE_METRIC = "metrics:name=cmconfig.create-config";
    private static final String COPY_METRIC = "metrics:name=cmconfig.copy-nodes";
    private static final String LIST_METRIC = "metrics:name=cmconfig.list-config";

    private static final String SOURCE_CONFIG = CmConfigTestBuilder.CONFIGURATION_FOR_TEST;
    private static final String TARGET_CONFIG = "targetMetricConfig";

    @Override
    protected void setupTest() throws Exception {
        this.testBuilder.createTestData(SOURCE_CONFIG);
    }

    /**
     * Tests the metrics for config services are registered.
     * 
     * @throws Exception
     *             occurred
     */
    @Test
    public void checkMetricsRegistered() throws Exception {
        // Invoke the create config command
        this.cmConfigService.createConfig(TARGET_CONFIG);

        // Invoke the list command.
        this.cmConfigService.listConfig();

        // Invoke the copy command.
        this.cmConfigService.copyNodes(SOURCE_CONFIG, TARGET_CONFIG, getNode(), new CmCopyNodesFlags());

        // Check the metrics
        final MBeanServer mBeanServer = ManagementFactory.getPlatformMBeanServer();

        assertMetric(mBeanServer, CREATE_METRIC);

        assertMetric(mBeanServer, COPY_METRIC);

        assertMetric(mBeanServer, LIST_METRIC);
    }

    private CmSearchCriteria getNode() {
        final CmSearchCriteria nodes = new CmSearchCriteria();
        final CmSearchScope node = new CmSearchScope();
        node.setName(MeContext.ME_CONTEXT_MO_NAME);
        node.setValue(NODE_NAME1);
        node.setScopeType(ScopeType.NODE_NAME);
        node.setCmMatchCondition(CmMatchCondition.EQUALS);
        nodes.getCmSearchScopes().add(node);
        return nodes;
    }

    private void assertMetric(final MBeanServer mBeanServer, final String metricName) throws Exception {
        final ObjectName objectName = new ObjectName(metricName);
        final MBeanInfo mBeanInfo = mBeanServer.getMBeanInfo(objectName);
        Assert.assertNotNull("The MBean has to be registered", mBeanInfo);
        Assert.assertTrue("The counter has to be higher than 0", Integer.valueOf(mBeanServer.getAttribute(objectName, "Count").toString()) > 0);
    }
}
