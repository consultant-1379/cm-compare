package com.ericsson.oss.services.cm.export.service.batch.downloadmanager;

import java.io.File;
import java.util.Properties;

import javax.batch.api.Batchlet;
import javax.batch.runtime.BatchRuntime;
import javax.batch.runtime.context.JobContext;
import javax.inject.Inject;
import javax.inject.Named;

@Named("MakeDirectoryBatchlet")
public class MakeDirectoryBatchlet implements Batchlet {

    @Inject
    JobContext jobCtx;

    @Override
    public String process() throws Exception {
        final Properties jobParameters = BatchRuntime.getJobOperator().getParameters(this.jobCtx.getExecutionId());
        final String downloadDirectoryRoot = this.jobCtx.getProperties().getProperty("downloadDirectory");
        System.out.println("Create temporary download directory " + downloadDirectoryRoot + "/job" + this.jobCtx.getExecutionId());
        new File(downloadDirectoryRoot + "/job" + this.jobCtx.getExecutionId()).mkdirs();
        return "done";
    }

    @Override
    public void stop() throws Exception {

    }

    public MakeDirectoryBatchlet() {
    }

}
