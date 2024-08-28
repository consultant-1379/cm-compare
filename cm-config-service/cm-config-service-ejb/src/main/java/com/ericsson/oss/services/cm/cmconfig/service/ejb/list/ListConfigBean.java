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

import static com.ericsson.oss.services.cm.cmconfig.service.api.CmConfigConstants.DPS_LOCAL_JNDI;
import static com.ericsson.oss.services.cm.cmconfig.service.log.CmConfigServiceMessages.SOLUTION_FOR_GENERIC_EXCEPTION;
import static com.ericsson.oss.services.cm.log.ErrorHandler.EXECUTION_ERROR;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.inject.Inject;

import com.ericsson.oss.itpf.datalayer.dps.DataPersistenceService;
import com.ericsson.oss.services.cm.cmconfig.service.exception.handling.CmConfigServiceExceptionHandler;
import com.ericsson.oss.services.cm.cmconfig.service.log.CmConfigServiceLog;
import com.ericsson.oss.services.cm.cmconfig.service.log.CmConfigServiceMessages;
import com.ericsson.oss.services.cm.cmshared.dto.CmObject;
import com.ericsson.oss.services.cm.cmshared.dto.CmResponse;

/**
 * List configurations.
 */
@Stateless
public class ListConfigBean implements ListConfig {

    @Inject
    private CmConfigServiceLog logger;

    @EJB(lookup = DPS_LOCAL_JNDI)
    private DataPersistenceService dataPersistenceService;

    @Inject
    private CmConfigServiceExceptionHandler exceptionHandler;

    @Override
    @SuppressWarnings("PMD.AvoidCatchingThrowable")
    public CmResponse listConfig() {
        final CmResponse returnCmResponse = new CmResponse();
        final Collection<String> configs;
        try {
            configs = this.dataPersistenceService.getAllDataBucketNames();
            populateCmResponse(returnCmResponse, mapConfigToCmObject(configs));
        } catch (final Throwable t) {
            this.exceptionHandler.handleError(returnCmResponse, t, SOLUTION_FOR_GENERIC_EXCEPTION, EXECUTION_ERROR);
        }
        this.logger.logCmCommand(CmConfigServiceMessages.LIST_CONFIG, returnCmResponse);
        return returnCmResponse;
    }

    private List<CmObject> mapConfigToCmObject(final Collection<String> configNames) {
        final List<CmObject> cmObjects = new ArrayList<>();
        final Iterator<String> nameIterator = configNames.iterator();
        while (nameIterator.hasNext()) {
            final CmObject cmObject = new CmObject();
            cmObject.setName(nameIterator.next());
            cmObjects.add(cmObject);
        }
        return cmObjects;
    }

    private void populateCmResponse(final CmResponse cmResponse, final List<CmObject> cmObjects) {
        cmResponse.setCmObjects(cmObjects);
        final int responseSize = cmObjects.size();
        cmResponse.setStatusCode(responseSize);
        cmResponse.setStatusMessage(responseSize + " config(s) ");
    }
}
