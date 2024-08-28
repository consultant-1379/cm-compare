package com.ericsson.oss.services.cm.export.service.batch.myjob;

import javax.batch.api.AbstractBatchlet;
import javax.batch.runtime.BatchStatus;
import javax.inject.Named;

/**
 * 
 */
@Named
public class MyBatchlet extends AbstractBatchlet {
    @Override
    public String process() {
        System.out.println("PROCESSING A BATCHLET");

        return BatchStatus.COMPLETED.toString();
    }
}
