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
package com.ericsson.oss.services.cm.writer.test;

import java.util.Collection;

import com.ericsson.oss.itpf.datalayer.dps.DataBucket;
import com.ericsson.oss.itpf.datalayer.dps.DataPersistenceService;
import com.ericsson.oss.itpf.datalayer.dps.query.QueryBuilder;
import com.ericsson.oss.itpf.sdk.core.annotation.EServiceRef;

public class DpsFacade implements DataPersistenceService {

    @EServiceRef
    private DataPersistenceService dataPersistenceService;

    @Override
    public DataBucket getLiveBucket() {
        return dataPersistenceService.getLiveBucket();
    }

    @Override
    public DataBucket getDataBucket(final String bucketName, final String... optionalParameters) {
        return dataPersistenceService.getDataBucket(bucketName, optionalParameters);
    }

    @Override
    public QueryBuilder getQueryBuilder() {
        return dataPersistenceService.getQueryBuilder();
    }

    @Override
    public DataBucket createDataBucket(final String bucketName, final String description, final String... optionalParameters) {
        //new dataPersistenceService method that dpsFacade must override  even though we don't use it (yet)
        return dataPersistenceService.createDataBucket(bucketName, description, optionalParameters);
    }

    @Override
    public long deleteDataBucket(final String arg0, final boolean arg1) {
        //new dataPersistenceService method that dpsFacade must override  even though we don't use it (yet)
        return dataPersistenceService.deleteDataBucket(arg0, arg1);
    }

    @Override
    public Collection<String> getAllDataBucketNames() {
        //new dataPersistenceService method that dpsFacade must override  even though we don't use it (yet)
        return dataPersistenceService.getAllDataBucketNames();
    }

}
