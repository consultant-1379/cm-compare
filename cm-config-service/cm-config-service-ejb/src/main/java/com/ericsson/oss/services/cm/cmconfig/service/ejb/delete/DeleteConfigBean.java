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

import static com.ericsson.oss.services.cm.cmconfig.service.api.CmConfigConstants.DPS_LOCAL_JNDI;
import static com.ericsson.oss.services.cm.cmconfig.service.log.CmConfigServiceMessages.CONFIGURATION_DOES_NOT_EXIST_ERROR;
import static com.ericsson.oss.services.cm.cmconfig.service.log.CmConfigServiceMessages.DELETE_LIVE_CONFIGURATION_ERROR;
import static com.ericsson.oss.services.cm.cmconfig.service.log.CmConfigServiceMessages.DELETE_NON_EMPTY_CONFIGURATION_ERROR;
import static com.ericsson.oss.services.cm.cmconfig.service.log.CmConfigServiceMessages.SOLUTION_FOR_CONFIG_INVALID;
import static com.ericsson.oss.services.cm.cmconfig.service.log.CmConfigServiceMessages.SOLUTION_FOR_DELETE_LIVE_CONFIGURATION_ERROR;
import static com.ericsson.oss.services.cm.cmconfig.service.log.CmConfigServiceMessages.SOLUTION_FOR_DELETE_NON_EMPTY_CONFIGURATION_ERROR;
import static com.ericsson.oss.services.cm.cmconfig.service.log.CmConfigServiceMessages.SOLUTION_FOR_GENERIC_EXCEPTION;
import static com.ericsson.oss.services.cm.log.ErrorHandler.EXECUTION_ERROR;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.inject.Inject;

import com.ericsson.oss.itpf.datalayer.dps.DataPersistenceService;
import com.ericsson.oss.itpf.datalayer.dps.exception.general.DpsIllegalArgumentException;
import com.ericsson.oss.itpf.datalayer.dps.exception.general.DpsIllegalStateException;
import com.ericsson.oss.itpf.datalayer.dps.exception.general.ObjectNotFoundException;
import com.ericsson.oss.services.cm.cmconfig.service.exception.handling.CmConfigServiceExceptionHandler;
import com.ericsson.oss.services.cm.cmconfig.service.log.CmConfigServiceLog;
import com.ericsson.oss.services.cm.cmconfig.service.log.CmConfigServiceMessages;
import com.ericsson.oss.services.cm.cmshared.dto.CmResponse;

/**
 * Delete configuration.
 */
@Stateless
public class DeleteConfigBean implements DeleteConfig {

    @Inject
    private CmConfigServiceLog logger;

    @Inject
    private CmConfigServiceExceptionHandler exceptionHandler;

    @EJB(lookup = DPS_LOCAL_JNDI)
    private DataPersistenceService dataPersistenceService;

    @Override
    @SuppressWarnings("PMD.AvoidCatchingThrowable")
    public CmResponse deleteConfig(final String configName) {
        final CmResponse returnCmResponse = new CmResponse();
        returnCmResponse.setStatusMessage(CmConfigServiceMessages.CONFIG_DELETE_SUCCESS_MESSAGE);
        try {
            this.dataPersistenceService.deleteDataBucket(configName, true);
        } catch (final ObjectNotFoundException ex) {
            this.exceptionHandler.handleValidationException(returnCmResponse, CONFIGURATION_DOES_NOT_EXIST_ERROR, SOLUTION_FOR_CONFIG_INVALID,
                    DeleteConfigBean.class.getName(), EXECUTION_ERROR);
        } catch (final DpsIllegalStateException ex) {
            this.exceptionHandler.handleValidationException(returnCmResponse, DELETE_NON_EMPTY_CONFIGURATION_ERROR,
                    SOLUTION_FOR_DELETE_NON_EMPTY_CONFIGURATION_ERROR, DeleteConfigBean.class.getName(), EXECUTION_ERROR);
        } catch (final DpsIllegalArgumentException ex) {
            this.exceptionHandler.handleValidationException(returnCmResponse, DELETE_LIVE_CONFIGURATION_ERROR,
                    SOLUTION_FOR_DELETE_LIVE_CONFIGURATION_ERROR, DeleteConfigBean.class.getName(), EXECUTION_ERROR);
        } catch (final Throwable t) {
            this.exceptionHandler.handleError(returnCmResponse, t, SOLUTION_FOR_GENERIC_EXCEPTION, EXECUTION_ERROR);
        }
        this.logger.logCmCommand(CmConfigServiceMessages.DELETE_CONFIG, returnCmResponse, configName);
        return returnCmResponse;
    }
}
