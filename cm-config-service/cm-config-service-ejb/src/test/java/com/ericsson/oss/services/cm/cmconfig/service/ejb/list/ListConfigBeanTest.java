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
package com.ericsson.oss.services.cm.cmconfig.service.ejb.list;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.runners.MockitoJUnitRunner;

import com.ericsson.oss.itpf.datalayer.dps.DataPersistenceService;
import com.ericsson.oss.services.cm.cmconfig.service.exception.handling.CmConfigServiceExceptionHandler;
import com.ericsson.oss.services.cm.cmconfig.service.exception.handling.CmConfigServiceExceptionHandlerImpl;
import com.ericsson.oss.services.cm.cmconfig.service.log.CmConfigServiceLog;
import com.ericsson.oss.services.cm.cmshared.dto.CmResponse;
import com.ericsson.oss.services.cm.log.ErrorHandler;

/**
 * Unit tests for ListConfigBean.
 */
@SuppressWarnings("PMD.UnusedPrivateField")
@RunWith(MockitoJUnitRunner.class)
public class ListConfigBeanTest {

    private static final String CONFIG_NAME = "config1";
    private static final String DPS_EXCEPTION_MSG = "dpsexception";

    @Mock
    private CmConfigServiceLog logger;

    @Mock
    private DataPersistenceService dataPersistenceService;

    @Spy
    @InjectMocks
    private final CmConfigServiceExceptionHandler exceptionHandler = new CmConfigServiceExceptionHandlerImpl();

    @InjectMocks
    private final ListConfigBean objUnderTest = new ListConfigBean();

    /**
     * test listConfig to list all existing configs.
     */
    @Test
    public void listConfig_all_allConfigsListed() {
        final List<String> configNames = new ArrayList<>();
        configNames.add(CONFIG_NAME);
        configNames.add("CONFIG_NAME2");
        when(this.dataPersistenceService.getAllDataBucketNames()).thenReturn(configNames);

        final CmResponse result = this.objUnderTest.listConfig();
        assertEquals(configNames.size(), result.getStatusCode());
        assertEquals(configNames.size(), result.getCmObjects().size());
    }

    /**
     * test listConfig when no configs exits, returns status code 0.
     */
    @Test
    public void listConfig_noConfigsExist_statusCodeis0() {
        final List<String> configNames = new ArrayList<>();
        when(this.dataPersistenceService.getAllDataBucketNames()).thenReturn(configNames);

        final CmResponse result = this.objUnderTest.listConfig();
        assertEquals(configNames.size(), result.getStatusCode());
        assertEquals(configNames.size(), result.getCmObjects().size());
    }

    /**
     * test listConfig can handle any exceptions thrown by DPS.
     */
    @SuppressWarnings("PMD.AvoidThrowingRawExceptionTypes")
    @Test
    public void listConfig_DPSThrowsAnyException_exceptionHandled() {
        when(this.dataPersistenceService.getAllDataBucketNames()).thenThrow(new RuntimeException(DPS_EXCEPTION_MSG));
        final CmResponse result = this.objUnderTest.listConfig();
        assertEquals(ErrorHandler.EXECUTION_ERROR, result.getStatusCode());
    }

}
