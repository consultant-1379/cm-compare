/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ericsson.oss.services.cm.export.service.batch.downloadmanager;

import java.io.Serializable;
import java.util.Properties;

import javax.batch.api.chunk.AbstractItemReader;
import javax.batch.runtime.BatchRuntime;
import javax.batch.runtime.context.JobContext;
import javax.inject.Inject;
import javax.inject.Named;

@Named("ItemReader")
public class ItemReader extends AbstractItemReader {

    @Inject
    private JobContext jobContext;
    private String[] fileURLArray;
    private int counter;

    public ItemReader() {
    }

    @Override
    public void open(final Serializable e) {
        System.out.println("Open readItems stage - prepare item list");
        final Properties jobParameters = BatchRuntime.getJobOperator().getParameters(this.jobContext.getExecutionId());
        final String fileList = jobParameters.getProperty("fileList");
        this.fileURLArray = fileList.split(";");
        this.counter = 0;
    }

    @Override
    public Object readItem() {
        if (this.counter < this.fileURLArray.length) {
            System.out.println("ReadItem returns " + this.fileURLArray[this.counter]);
            return this.fileURLArray[this.counter++];
        } else {
            return null;
        }
    }
}
