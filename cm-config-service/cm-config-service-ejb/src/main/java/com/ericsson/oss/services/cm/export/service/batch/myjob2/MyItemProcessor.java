package com.ericsson.oss.services.cm.export.service.batch.myjob2;

import javax.batch.api.chunk.ItemProcessor;
import javax.enterprise.context.Dependent;
import javax.inject.Named;

/**
 * 
 */
@Dependent
@Named
public class MyItemProcessor implements ItemProcessor {

    @Override
    public MyOutputRecord processItem(final Object t) {
        System.out.println("processItem: " + t);

        return (((MyInputRecord) t).getId() % 2 == 0) ? null : new MyOutputRecord(((MyInputRecord) t).getId() * 2);
    }
}
