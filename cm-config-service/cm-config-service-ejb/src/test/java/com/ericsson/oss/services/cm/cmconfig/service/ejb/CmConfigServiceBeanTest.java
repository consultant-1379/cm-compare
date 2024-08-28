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
package com.ericsson.oss.services.cm.cmconfig.service.ejb;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Matchers;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import com.ericsson.oss.services.cm.cmconfig.service.api.CmCopyResponse;
import com.ericsson.oss.services.cm.cmconfig.service.api.CmDiffResponse;
import com.ericsson.oss.services.cm.cmconfig.service.ejb.copy.CopyNodes;
import com.ericsson.oss.services.cm.cmconfig.service.ejb.create.CreateConfig;
import com.ericsson.oss.services.cm.cmconfig.service.ejb.delete.DeleteConfig;
import com.ericsson.oss.services.cm.cmconfig.service.ejb.diff.DiffConfig;
import com.ericsson.oss.services.cm.cmconfig.service.ejb.list.ListConfig;
import com.ericsson.oss.services.cm.cmshared.dto.CmResponse;

/**
 * Unit tests for CmConfigServiceBean.
 */
@RunWith(MockitoJUnitRunner.class)
public class CmConfigServiceBeanTest {

    @Mock
    private CreateConfig createConfig;

    @Mock
    private ListConfig listConfig;

    @Mock
    private DeleteConfig deleteConfig;

    @Mock
    private DiffConfig diffConfig;

    @Mock
    private CopyNodes copyNodes;

    @Mock
    private CmResponse mockCmResponse;

    @Mock
    private CmDiffResponse mockCmDiffResponse;

    @Mock
    private CmCopyResponse mockCmCopyResponse;

    @InjectMocks
    private final CmConfigServiceBean objUnderTest = new CmConfigServiceBean();

    @Test
    public void serviceExecutor_executeCreateConfig_CmResponseReturned() {
        Mockito.when(this.createConfig.createConfig(Matchers.anyString())).thenReturn(this.mockCmResponse);
        Assert.assertEquals(this.mockCmResponse, this.objUnderTest.createConfig("configName"));
    }

    @Test
    public void serviceExecutor_executeListConfig_CmResponseReturned() {
        Mockito.when(this.listConfig.listConfig()).thenReturn(this.mockCmResponse);
        Assert.assertEquals(this.mockCmResponse, this.objUnderTest.listConfig());
    }

    @Test
    public void serviceExecutor_executeDiffConfig_CmDiffResponseReturned() {
        Mockito.when(this.diffConfig.diffConfig(null, null, null, null)).thenReturn(this.mockCmDiffResponse);
        Assert.assertEquals(this.mockCmDiffResponse, this.objUnderTest.diffConfig(null, null, null, null));
    }

    @Test
    public void serviceExecutor_executeDeleteConfig_CmResponseReturned() {
        Mockito.when(this.deleteConfig.deleteConfig(Matchers.anyString())).thenReturn(this.mockCmResponse);
        Assert.assertEquals(this.mockCmResponse, this.objUnderTest.deleteConfig(""));
    }

    @Test
    public void serviceExecutor_executeCopyNodes_CmCopyResponseReturned() {
        Mockito.when(this.copyNodes.copyNodes(null, null, null, null)).thenReturn(this.mockCmCopyResponse);
        Assert.assertEquals(this.mockCmCopyResponse, this.objUnderTest.copyNodes(null, null, null, null));
    }
}
