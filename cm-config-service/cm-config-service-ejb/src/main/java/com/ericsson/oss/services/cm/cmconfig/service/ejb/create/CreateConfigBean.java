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

import static com.ericsson.oss.services.cm.cmconfig.service.api.CmConfigConstants.DPS_LOCAL_JNDI;
import static com.ericsson.oss.services.cm.cmconfig.service.log.CmConfigServiceMessages.CONFIGURATION_ALREADY_EXISTS_ERROR;
import static com.ericsson.oss.services.cm.cmconfig.service.log.CmConfigServiceMessages.CONFIG_CREATE_SUCCESS_MESSAGE;
import static com.ericsson.oss.services.cm.cmconfig.service.log.CmConfigServiceMessages.CREATE_CONFIG;
import static com.ericsson.oss.services.cm.cmconfig.service.log.CmConfigServiceMessages.SOLUTION_FOR_CONFIG_ALREADY_EXISTS;
import static com.ericsson.oss.services.cm.cmconfig.service.log.CmConfigServiceMessages.SOLUTION_FOR_GENERIC_EXCEPTION;
import static com.ericsson.oss.services.cm.log.ErrorHandler.EXECUTION_ERROR;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.inject.Inject;

import com.ericsson.oss.itpf.datalayer.dps.DataPersistenceService;
import com.ericsson.oss.itpf.datalayer.dps.exception.general.AlreadyDefinedException;
import com.ericsson.oss.services.cm.cmconfig.service.exception.handling.CmConfigServiceExceptionHandler;
import com.ericsson.oss.services.cm.cmconfig.service.log.CmConfigServiceLog;
import com.ericsson.oss.services.cm.cmshared.dto.CmResponse;

/**
 * Create Configuration.
 */
@Stateless
public class CreateConfigBean implements CreateConfig {

    @Inject
    private CmConfigServiceLog logger;

    @Inject
    private CmConfigServiceExceptionHandler exceptionHandler;

    @EJB(lookup = DPS_LOCAL_JNDI)
    private DataPersistenceService dataPersistenceService;

    @Override
    @SuppressWarnings("PMD.AvoidCatchingThrowable")
    public CmResponse createConfig(final String configName) {
        final CmResponse returnCmResponse = new CmResponse();
        returnCmResponse.setStatusMessage(CONFIG_CREATE_SUCCESS_MESSAGE);
        try {
            this.dataPersistenceService.createDataBucket(configName, configName);
        } catch (final AlreadyDefinedException ex) {
            this.exceptionHandler.handleValidationException(returnCmResponse, CONFIGURATION_ALREADY_EXISTS_ERROR, SOLUTION_FOR_CONFIG_ALREADY_EXISTS,
                    CreateConfigBean.class.getName(), EXECUTION_ERROR);
        } catch (final Throwable t) {
            this.exceptionHandler.handleError(returnCmResponse, t, SOLUTION_FOR_GENERIC_EXCEPTION, EXECUTION_ERROR);
        }
        this.logger.logCmCommand(CREATE_CONFIG, returnCmResponse, configName);
        return returnCmResponse;
    }
}
