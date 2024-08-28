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

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import com.codahale.metrics.Counter;
import com.codahale.metrics.Histogram;
import com.codahale.metrics.JmxReporter;
import com.codahale.metrics.Meter;
import com.codahale.metrics.MetricRegistry;
import com.codahale.metrics.Timer;

/**
 * Test for {@code MetricRegistryWrapper}.
 */
@RunWith(MockitoJUnitRunner.class)
public class MetricRegistryWrapperTest {

    private static final String METRIC_NAME = "metricName";

    private final MetricRegistry metricRegistry = new MetricRegistry();
    private final JmxReporter jmxReporter = JmxReporter.forRegistry(this.metricRegistry).build();
    private final CsvLoggerReporter csvLoggerReporter = CsvLoggerReporter.forRegistry(this.metricRegistry).build();

    private final MetricRegistryWrapper metricRegistryWrapper = new MetricRegistryWrapper(this.metricRegistry, this.jmxReporter,
            this.csvLoggerReporter);

    @Test
    public void testGetMetricRegistry() {
        final MetricRegistry registry = this.metricRegistryWrapper.getMetricRegistry();
        Assert.assertEquals(this.metricRegistry, registry);
    }

    @Test
    public void testGetJmxReporter() {
        final JmxReporter reporter = this.metricRegistryWrapper.getJmxReporter();
        Assert.assertEquals(this.jmxReporter, reporter);
    }

    @Test
    public void testGetCsvLoggerReporter() {
        final CsvLoggerReporter reporter = this.metricRegistryWrapper.getCsvLoggerReporter();
        Assert.assertEquals(this.csvLoggerReporter, reporter);
    }

    @Test
    public void testInvokeMeter() {
        final Meter meter = this.metricRegistryWrapper.meter(METRIC_NAME);

        final Meter meterFromRegistry = (Meter) this.metricRegistry.getMetrics().get(METRIC_NAME);
        Assert.assertEquals(meter, meterFromRegistry);
    }

    @Test
    public void testInvokeTimer() {
        final Timer timer = this.metricRegistryWrapper.timer(METRIC_NAME);

        final Timer timerFromRegistry = (Timer) this.metricRegistry.getMetrics().get(METRIC_NAME);
        Assert.assertEquals(timer, timerFromRegistry);
    }

    @Test
    public void testInvokeCounter() {
        final Counter counter = this.metricRegistryWrapper.counter(METRIC_NAME);

        final Counter counterFromRegistry = (Counter) this.metricRegistry.getMetrics().get(METRIC_NAME);
        Assert.assertEquals(counter, counterFromRegistry);
    }

    @Test
    public void testInvokeHistogram() {
        final Histogram histogram = this.metricRegistryWrapper.histogram(METRIC_NAME);

        final Histogram histogramFromRegistry = (Histogram) this.metricRegistry.getMetrics().get(METRIC_NAME);
        Assert.assertEquals(histogram, histogramFromRegistry);
    }

    @Test
    public void testShutdown() {
        final JmxReporter mockJmxReporter = Mockito.mock(JmxReporter.class);
        final CsvLoggerReporter mockCsvLoggerReporter = Mockito.mock(CsvLoggerReporter.class);
        final MetricRegistryWrapper wrapper = new MetricRegistryWrapper(this.metricRegistry, mockJmxReporter, mockCsvLoggerReporter);
        wrapper.shutdown();
        Mockito.verify(mockJmxReporter).close();
        Mockito.verify(mockCsvLoggerReporter).close();
    }
}
