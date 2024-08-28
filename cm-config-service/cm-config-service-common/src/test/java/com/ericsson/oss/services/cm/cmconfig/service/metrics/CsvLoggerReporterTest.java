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
package com.ericsson.oss.services.cm.cmconfig.service.metrics;

import java.util.concurrent.TimeUnit;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Matchers;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;
import org.slf4j.Logger;
import org.slf4j.Marker;

import com.codahale.metrics.MetricFilter;
import com.codahale.metrics.MetricRegistry;

/**
 * Test for {@code CsvLoggerReporter}.
 */
@RunWith(MockitoJUnitRunner.class)
public class CsvLoggerReporterTest {

    @Mock
    private Logger mockLogger;

    private final MetricRegistry metricRegistry = new MetricRegistry();

    private CsvLoggerReporter csvLoggerReporter;

    @Before
    public void setUp() {
        this.csvLoggerReporter = new CsvLoggerReporter(this.metricRegistry, this.mockLogger, null, TimeUnit.SECONDS, TimeUnit.MILLISECONDS,
                MetricFilter.ALL);
    }

    @Test
    public void testReportMetrics() {
        // Register some metrics
        this.metricRegistry.meter("meterName").mark();
        this.metricRegistry.counter("counterName").inc();
        this.metricRegistry.histogram("histogramName").update(1);
        this.metricRegistry.timer("timerName").time().close();

        this.csvLoggerReporter.report();
        final ArgumentCaptor<String> argument = ArgumentCaptor.forClass(String.class);
        Mockito.verify(this.mockLogger, Mockito.times(4)).info(Matchers.any(Marker.class), argument.capture());

        for (final String message : argument.getAllValues()) {
            Assert.assertTrue(
                    "The argument has to contain one metric",
                    message.contains("meterName") || message.contains("counterName") || message.contains("histogramName")
                            || message.contains("timerName"));
        }
    }
}
