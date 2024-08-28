package com.ericsson.oss.services.cm.export.service.batch.myjob2;

import java.util.List;

import javax.batch.api.chunk.AbstractItemWriter;
import javax.enterprise.context.Dependent;
import javax.inject.Named;

/**
 * 
 */
@Dependent
@Named
public class MyItemWriter extends AbstractItemWriter {

    @Override
    public void writeItems(final List list) {
        System.out.println("writeItems: " + list);
    }
}
