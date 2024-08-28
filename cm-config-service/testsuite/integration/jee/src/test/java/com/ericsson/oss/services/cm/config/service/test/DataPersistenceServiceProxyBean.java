/*------------------------------------------------------------------------------
 *******************************************************************************
 * COPYRIGHT Ericsson 2013
 *
 * The copyright to the computer program(s) herein is the property of
 * Ericsson Inc. The programs may be used and/or copied only with written
 * permission from Ericsson Inc. or in accordance with the terms and
 * conditions stipulated in the agreement/contract under which the
 * program(s) have been supplied.
 *******************************************************************************
 *----------------------------------------------------------------------------*/
package com.ericsson.oss.services.cm.config.service.test;

import static com.ericsson.oss.services.cm.cmconfig.service.api.CmConfigConstants.DPS_LOCAL_JNDI;

import javax.ejb.EJB;
import javax.enterprise.context.ApplicationScoped;

import com.ericsson.oss.itpf.datalayer.dps.DataPersistenceService;
import com.ericsson.oss.services.cm.testutil.DataPersistenceServiceProxy;

/**
 * Proxy (wrapper) required for resolving EService Reference of {@link DataPersistenceService}.
 */
@ApplicationScoped
public class DataPersistenceServiceProxyBean implements DataPersistenceServiceProxy {

    @EJB(lookup = DPS_LOCAL_JNDI)
    protected DataPersistenceService dataPersistenceService;

    @Override
    public DataPersistenceService getDataPersistenceService() {
        return this.dataPersistenceService;
    }

}
