/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ericsson.oss.services.cm.export.service.batch.downloadmanager;

import java.util.Properties;

import javax.batch.api.Batchlet;
import javax.batch.runtime.BatchRuntime;
import javax.batch.runtime.context.JobContext;
import javax.inject.Inject;
import javax.inject.Named;

@Named("RemoveDirectoryBatchlet")
public class RemoveDirectoryBatchlet implements Batchlet {

    @Inject
    JobContext jobCtx;

    @Override
    public String process() throws Exception {
        final Properties jobParameters = BatchRuntime.getJobOperator().getParameters(this.jobCtx.getExecutionId());
        final String downloadDirectoryRoot = this.jobCtx.getProperties().getProperty("downloadDirectory");
        System.out.println("Remove directory " + downloadDirectoryRoot + "/job" + this.jobCtx.getExecutionId());

        return "done";
    }

    @Override
    public void stop() throws Exception {

    }

    public RemoveDirectoryBatchlet() {
    }

}
