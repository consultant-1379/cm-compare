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
package com.ericsson.oss.services.cm.cmconfig.service.ejb;

import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.batch.operations.JobOperator;
import javax.batch.runtime.BatchRuntime;
import javax.batch.runtime.BatchStatus;
import javax.batch.runtime.JobExecution;
import javax.batch.runtime.Metric;
import javax.batch.runtime.StepExecution;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.inject.Inject;

import com.ericsson.oss.services.cm.cmconfig.service.api.CmConfigDiffParams;
import com.ericsson.oss.services.cm.cmconfig.service.api.CmConfigService;
import com.ericsson.oss.services.cm.cmconfig.service.api.CmCopyNodesFlags;
import com.ericsson.oss.services.cm.cmconfig.service.api.CmCopyResponse;
import com.ericsson.oss.services.cm.cmconfig.service.api.CmDiffResponse;
import com.ericsson.oss.services.cm.cmconfig.service.api.ExportStatus;
import com.ericsson.oss.services.cm.cmconfig.service.ejb.copy.CopyNodes;
import com.ericsson.oss.services.cm.cmconfig.service.ejb.create.CreateConfig;
import com.ericsson.oss.services.cm.cmconfig.service.ejb.delete.DeleteConfig;
import com.ericsson.oss.services.cm.cmconfig.service.ejb.diff.DiffConfig;
import com.ericsson.oss.services.cm.cmconfig.service.ejb.list.ListConfig;
import com.ericsson.oss.services.cm.cmconfig.service.metrics.annotation.Timed;
import com.ericsson.oss.services.cm.cmshared.dto.CmResponse;
import com.ericsson.oss.services.cm.cmshared.dto.search.CmSearchCriteria;

/**
 * CM configuration service bean implementing the business logic for CM
 * configuration APIs.
 */
@Stateless
@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
public class CmConfigServiceBean implements CmConfigService {

    private static final String METRIC_GROUP = "cmconfig";

    @Inject
    private CreateConfig createConfig;

    @Inject
    private ListConfig listConfig;

    @Inject
    private DeleteConfig deleteConfig;

    @Inject
    private DiffConfig diffConfig;

    @Inject
    private CopyNodes copyNodes;

    @Timed(group = METRIC_GROUP, name = "create-config")
    @Override
    public CmResponse createConfig(final String configName) {
        return this.createConfig.createConfig(configName);
    }

    @Timed(group = METRIC_GROUP, name = "list-config")
    @Override
    public CmResponse listConfig() {
        return this.listConfig.listConfig();
    }

    @Timed(group = METRIC_GROUP, name = "diff-config")
    @Override
    public CmDiffResponse diffConfig(final String targetConfigName, final String referenceConfigName, final CmSearchCriteria scope,
            final CmConfigDiffParams params) {
        return this.diffConfig.diffConfig(targetConfigName, referenceConfigName, scope, params);
    }

    @Timed(group = METRIC_GROUP, name = "delete-config")
    @Override
    public CmResponse deleteConfig(final String configName) {
        return this.deleteConfig.deleteConfig(configName);
    }

    @Timed(group = METRIC_GROUP, name = "copy-nodes")
    @Override
    public CmCopyResponse copyNodes(final String sourceConfigName, final String targetConfigName, final CmSearchCriteria nodesScope,
            final CmCopyNodesFlags copyFlags) {
        return this.copyNodes.copyNodes(sourceConfigName, targetConfigName, nodesScope, copyFlags);
    }

    @Override
    public Long startExportJob(final String jobName, final Properties properties) {
        final JobOperator jobOperator = BatchRuntime.getJobOperator();
        final Long executionId = jobOperator.start(jobName, properties);
        return executionId;
    }

    @Override
    public ExportStatus exportJobStatus(final Long exportJobID) {
        final JobOperator jobOperator = BatchRuntime.getJobOperator();
        final JobExecution jobExecution = jobOperator.getJobExecution(exportJobID);
        final ExportStatus status = jobStatus(jobExecution.getBatchStatus());
        return status;
    }

    private ExportStatus jobStatus(final BatchStatus batchStatus) {
        switch (batchStatus) {
        case STARTING:
            return ExportStatus.STARTING;
        case STARTED:
            return ExportStatus.STARTED;
        case STOPPING:
            return ExportStatus.STOPPING;
        case STOPPED:
            return ExportStatus.STOPPED;
        case FAILED:
            return ExportStatus.FAILED;
        case COMPLETED:
            return ExportStatus.COMPLETED;
        case ABANDONED:
            return ExportStatus.ABANDONED;
        }
        return ExportStatus.STOPPED;
    }

    @Override
    public ExportStatus processExportTillComplete(final Long exportJobID) {
        final JobOperator jobOperator = BatchRuntime.getJobOperator();

        final JobExecution jobExecution = jobOperator.getJobExecution(exportJobID);

        /*
         * try {
         * BatchTestHelper.keepTestAlive(jobExecution);
         * } catch (final InterruptedException e) {
         * e.printStackTrace();
         * }
         */
        while (!jobExecution.getBatchStatus().equals(BatchStatus.COMPLETED)) {
            //wait
        }

        final List<StepExecution> stepExecutions = jobOperator.getStepExecutions(exportJobID);
        for (final StepExecution stepExecution : stepExecutions) {
            if (stepExecution.getStepName().equals("myStep1")) {
                final Map<Metric.MetricType, Long> metricsMap = BatchTestHelper.getMetricsMap(stepExecution.getMetrics());

                // <1> The read count should be 10 elements. Check +MyItemReader+.
                System.out.println("expected Read Count =" + 10L + " Actual ReadCount = " + metricsMap.get(Metric.MetricType.READ_COUNT).longValue());
                // <2> The write count should be 5. Only half of the elements read are processed to be written.
                System.out.println("expected Write Count =" + (10L / 2L) + " Actual Write Count = "
                        + metricsMap.get(Metric.MetricType.WRITE_COUNT).longValue());
                // <3> The commit count should be 4. Checkpoint is on every 3rd read, 4 commits for read elements.
                System.out.println("expected Commit  Count = "
                        + (10L / 3 + (10L % 3 > 0 ? 1 : 0) + " Actual Commit Count = " + metricsMap.get(Metric.MetricType.COMMIT_COUNT).longValue()));
            }
        }

        // <4> Job should be completed.
        final ExportStatus status = jobStatus(jobExecution.getBatchStatus());
        return status;

    }
}
