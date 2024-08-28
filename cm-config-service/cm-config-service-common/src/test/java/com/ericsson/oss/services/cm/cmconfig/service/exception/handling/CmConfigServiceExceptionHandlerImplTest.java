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
package com.ericsson.oss.services.cm.cmconfig.service.exception.handling;

import javax.inject.Inject;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Matchers;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import com.ericsson.oss.services.cm.cmconfig.service.log.CmConfigServiceLog;
import com.ericsson.oss.services.cm.cmshared.dto.AbstractCmResponse;
import com.ericsson.oss.services.cm.cmshared.dto.CmResponse;

/**
 * Unit tests for CmConfigServiceExceptionHandler.
 */
@SuppressWarnings("PMD.UnusedPrivateField")
@RunWith(MockitoJUnitRunner.class)
public class CmConfigServiceExceptionHandlerImplTest {

    private static final String ERROR_MESSAGE = "an error";
    private static final String SOLUTION_MESSAGE = "solution";
    private static final int STATUS_CODE = -99;

    @Inject
    @Mock
    private CmConfigServiceLog logger;

    @Mock
    private AbstractCmResponse cmResponse;

    @InjectMocks
    private final CmConfigServiceExceptionHandler exceptionHandler = new CmConfigServiceExceptionHandlerImpl();

    @Test
    public void testHandleError() {
        Mockito.when(this.logger.logAndCreateErrorMessageFromException(Matchers.any(Throwable.class))).thenReturn(ERROR_MESSAGE);
        final AbstractCmResponse cmResponse = new CmResponse();
        this.exceptionHandler.handleError(cmResponse, new IllegalArgumentException(ERROR_MESSAGE), SOLUTION_MESSAGE, STATUS_CODE);
        Assert.assertEquals(STATUS_CODE, cmResponse.getStatusCode());
        Assert.assertEquals(SOLUTION_MESSAGE, cmResponse.getSolution());
        Assert.assertEquals(ERROR_MESSAGE, cmResponse.getStatusMessage());
    }

    @Test
    public void testHandleValidationException() {
        final AbstractCmResponse cmResponse = new CmResponse();
        this.exceptionHandler.handleValidationException(cmResponse, ERROR_MESSAGE, SOLUTION_MESSAGE,
                CmConfigServiceExceptionHandlerImplTest.class.getName(), STATUS_CODE);
        Assert.assertEquals(STATUS_CODE, cmResponse.getStatusCode());
        Assert.assertEquals(SOLUTION_MESSAGE, cmResponse.getSolution());
        Assert.assertEquals(ERROR_MESSAGE, cmResponse.getStatusMessage());
    }

}
