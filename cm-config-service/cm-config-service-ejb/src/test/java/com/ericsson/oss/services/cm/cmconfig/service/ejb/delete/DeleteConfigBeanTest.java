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
package com.ericsson.oss.services.cm.cmconfig.service.ejb.delete;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.runners.MockitoJUnitRunner;

import com.ericsson.oss.itpf.datalayer.dps.DataPersistenceService;
import com.ericsson.oss.itpf.datalayer.dps.exception.general.DpsIllegalArgumentException;
import com.ericsson.oss.itpf.datalayer.dps.exception.general.DpsIllegalStateException;
import com.ericsson.oss.itpf.datalayer.dps.exception.general.ObjectNotFoundException;
import com.ericsson.oss.services.cm.cmconfig.service.exception.handling.CmConfigServiceExceptionHandler;
import com.ericsson.oss.services.cm.cmconfig.service.exception.handling.CmConfigServiceExceptionHandlerImpl;
import com.ericsson.oss.services.cm.cmconfig.service.log.CmConfigServiceLog;
import com.ericsson.oss.services.cm.cmshared.dto.CmResponse;
import com.ericsson.oss.services.cm.log.ErrorHandler;

/**
 * Unit test for DeleteConfigBean.
 */
@SuppressWarnings("PMD.UnusedPrivateField")
@RunWith(MockitoJUnitRunner.class)
public class DeleteConfigBeanTest {
    private static final String CONFIG_NAME = "config1";
    private static final String DPS_EXCEPTION_MSG = "dpsexception";

    @Mock
    private CmConfigServiceLog logger;

    @Mock
    private DataPersistenceService dataPersistenceService;

    @Mock
    private ObjectNotFoundException objectNotFoundException;

    @Mock
    private DpsIllegalStateException dpsIllegalStateException;

    @Mock
    private DpsIllegalArgumentException dpsIllegalArgumentException;

    @Spy
    @InjectMocks
    private final CmConfigServiceExceptionHandler exceptionHandler = new CmConfigServiceExceptionHandlerImpl();

    @InjectMocks
    private final DeleteConfigBean objUnderTest = new DeleteConfigBean();

    /**
     * test deleteConfig with a valid config name, deletes the config from dps.
     */
    @Test
    public void deletConfig_existingConfigName_configRemovedFromDPS() {
        when(this.dataPersistenceService.deleteDataBucket(CONFIG_NAME, true)).thenReturn((long) 0);
        final CmResponse result = this.objUnderTest.deleteConfig(CONFIG_NAME);
        assertEquals(0, result.getStatusCode());
    }

    /**
     * test deleteConfig with a non exitsing config name, returns a status code of -1.
     */
    @Test
    public void deletConfig_nonExistingConfigName_exceptionThrownByDPS() {
        when(this.dataPersistenceService.deleteDataBucket(CONFIG_NAME, true)).thenThrow(this.objectNotFoundException);
        final CmResponse result = this.objUnderTest.deleteConfig(CONFIG_NAME);
        assertEquals(ErrorHandler.EXECUTION_ERROR, result.getStatusCode());
    }

    /**
     * test deleteConfig handles DpsIllegalStateException.
     */
    @Test
    public void deletConfig_DpsIllegalStateExceptionFromDPS_exceptionHandled() {
        when(this.dataPersistenceService.deleteDataBucket(CONFIG_NAME, true)).thenThrow(this.dpsIllegalStateException);
        final CmResponse result = this.objUnderTest.deleteConfig(CONFIG_NAME);
        assertEquals(ErrorHandler.EXECUTION_ERROR, result.getStatusCode());
    }

    /**
     * test deleteConfig with a Live config name, returns a status code of -1.
     */
    @Test
    public void deletConfig_LiveConfigName_exceptionThrownByDPS() {
        when(this.dataPersistenceService.deleteDataBucket(CONFIG_NAME, true)).thenThrow(this.dpsIllegalArgumentException);
        final CmResponse result = this.objUnderTest.deleteConfig(CONFIG_NAME);
        assertEquals(ErrorHandler.EXECUTION_ERROR, result.getStatusCode());
    }

    /**
     * test deleteConfig handles any Exception thrown by DPS.
     */
    @SuppressWarnings("PMD.AvoidThrowingRawExceptionTypes")
    @Test
    public void deletConfig_AnyExceptionFromDPS_exceptionHandled() {
        when(this.dataPersistenceService.deleteDataBucket(CONFIG_NAME, true)).thenThrow(new RuntimeException(DPS_EXCEPTION_MSG));
        final CmResponse result = this.objUnderTest.deleteConfig(CONFIG_NAME);
        assertEquals(ErrorHandler.EXECUTION_ERROR, result.getStatusCode());
    }

}
