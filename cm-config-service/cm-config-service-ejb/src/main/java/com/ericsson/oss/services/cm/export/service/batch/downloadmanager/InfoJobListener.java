/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ericsson.oss.services.cm.export.service.batch.downloadmanager;

import javax.batch.api.listener.JobListener;
import javax.batch.api.listener.StepListener;
import javax.batch.runtime.context.JobContext;
import javax.batch.runtime.context.StepContext;
import javax.enterprise.context.Dependent;
import javax.inject.Inject;
import javax.inject.Named;

@Dependent
@Named("InfoJobListener")
public class InfoJobListener implements JobListener, StepListener {

    @Inject
    JobContext jobCtx;

    @Inject
    StepContext stepCtx;

    @Override
    public void beforeJob() throws Exception {
        System.out.println("Job starting " + this.jobCtx.getJobName());
    }

    @Override
    public void afterJob() {
        System.out.println("Job Finished");
    }

    @Override
    public void beforeStep() throws Exception {
        System.out.println("Start STep " + this.stepCtx.getStepName());
    }

    @Override
    public void afterStep() throws Exception {
        System.out.println("End step");
    }
}
