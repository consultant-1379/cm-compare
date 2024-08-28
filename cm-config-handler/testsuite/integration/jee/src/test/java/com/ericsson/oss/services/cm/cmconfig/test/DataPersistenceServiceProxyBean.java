package com.ericsson.oss.services.cm.cmconfig.test;

import javax.enterprise.context.ApplicationScoped;

import com.ericsson.oss.itpf.datalayer.dps.DataPersistenceService;
import com.ericsson.oss.itpf.sdk.core.annotation.EServiceRef;
import com.ericsson.oss.services.cm.testutil.DataPersistenceServiceProxy;

@ApplicationScoped
public class DataPersistenceServiceProxyBean implements DataPersistenceServiceProxy {

    @EServiceRef
    protected DataPersistenceService dataPersistenceService;

    @Override
    public DataPersistenceService getDataPersistenceService() {
        return dataPersistenceService;
    }
}
