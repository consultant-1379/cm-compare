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
package com.ericsson.oss.services.cm.cmconfig.service.ejb.create;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.runners.MockitoJUnitRunner;

import com.ericsson.oss.itpf.datalayer.dps.DataPersistenceService;
import com.ericsson.oss.itpf.datalayer.dps.NonLiveDataBucket;
import com.ericsson.oss.itpf.datalayer.dps.exception.general.AlreadyDefinedException;
import com.ericsson.oss.services.cm.cmconfig.service.exception.handling.CmConfigServiceExceptionHandler;
import com.ericsson.oss.services.cm.cmconfig.service.exception.handling.CmConfigServiceExceptionHandlerImpl;
import com.ericsson.oss.services.cm.cmconfig.service.log.CmConfigServiceLog;
import com.ericsson.oss.services.cm.cmshared.dto.CmResponse;
import com.ericsson.oss.services.cm.log.ErrorHandler;

/**
 * Unit test for CreateConfigBean.
 */
@SuppressWarnings("PMD.UnusedPrivateField")
@RunWith(MockitoJUnitRunner.class)
public class CreateConfigBeanTest {

    private static final String CONFIG_NAME = "config1";
    private static final String DPS_EXCEPTION_MSG = "dpsexception";

    @Mock
    private CmConfigServiceLog logger;

    @Mock
    private DataPersistenceService dataPersistenceService;

    @Mock
    private AlreadyDefinedException alreadyDefinedException;

    @Mock
    private NonLiveDataBucket config;

    @Spy
    @InjectMocks
    private final CmConfigServiceExceptionHandler exceptionHandler = new CmConfigServiceExceptionHandlerImpl();

    @InjectMocks
    private final CreateConfigBean objUnderTest = new CreateConfigBean();

    /**
     * test createConfig with a unique name is successful.
     */
    @Test
    public void createConfig_newConfigName_configCreatedInDPS() {
        when(this.dataPersistenceService.createDataBucket(CONFIG_NAME, CONFIG_NAME)).thenReturn(this.config);

        final CmResponse result = this.objUnderTest.createConfig(CONFIG_NAME);
        assertEquals(0, result.getStatusCode());
    }

    /**
     * test createConfig with an already existing config name returns failure.
     */
    @Test
    public void createConfig_AlreadyExistingConfigName_configNotCreatedInDPS() {
        when(this.dataPersistenceService.createDataBucket(CONFIG_NAME, CONFIG_NAME)).thenThrow(this.alreadyDefinedException);

        final CmResponse result = this.objUnderTest.createConfig(CONFIG_NAME);
        assertEquals(ErrorHandler.EXECUTION_ERROR, result.getStatusCode());
    }

    /**
     * test createConfig can handle any exceptions thrown by DPS.
     */
    @SuppressWarnings("PMD.AvoidThrowingRawExceptionTypes")
    @Test
    public void createConfig_DPSThrowsAnyException_exceptionHandled() {
        when(this.dataPersistenceService.createDataBucket(CONFIG_NAME, CONFIG_NAME)).thenThrow(new RuntimeException(DPS_EXCEPTION_MSG));

        final CmResponse result = this.objUnderTest.createConfig(CONFIG_NAME);
        assertEquals(ErrorHandler.EXECUTION_ERROR, result.getStatusCode());
    }
}
