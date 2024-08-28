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
import org.mockito.runners.MockitoJUnitRunner;

/**
 * Test for {@code MetricRegistryProducer}.
 */
@RunWith(MockitoJUnitRunner.class)
public class MetricRegistryProducerTest {

    private static final String ENABLE_JMX_REPORTER_SYSTEM_PROPERTY = "com.ericsson.oss.services.cm.cmconfig.metrics.jmxreporter.enable";
    private static final String ENABLE_LOGGER_REPORTER_SYSTEM_PROPERTY = "com.ericsson.oss.services.cm.cmconfig.metrics.csvreporter.enable";

    private final MetricRegistryProducer metricRegistryProducer = new MetricRegistryProducer();

    @Test
    public void testProduceMetricRegistryWithDefaultSystemProps() {

        final MetricRegistryWrapper metricRegistryWrapper = this.metricRegistryProducer.produceMetricsRegistry();
        Assert.assertNotNull(metricRegistryWrapper.getMetricRegistry());
        Assert.assertNotNull(metricRegistryWrapper.getJmxReporter());

        // By default there is no any CsvLoggerReporter instance.
        Assert.assertNull(metricRegistryWrapper.getCsvLoggerReporter());
    }

    @Test
    public void testProduceMetricRegistryWithJmxReporter() {

        System.setProperty(ENABLE_JMX_REPORTER_SYSTEM_PROPERTY, "true");

        final MetricRegistryWrapper metricRegistryWrapper = this.metricRegistryProducer.produceMetricsRegistry();

        System.clearProperty(ENABLE_JMX_REPORTER_SYSTEM_PROPERTY);

        Assert.assertNotNull(metricRegistryWrapper.getMetricRegistry());
        Assert.assertNotNull(metricRegistryWrapper.getJmxReporter());
        Assert.assertNull(metricRegistryWrapper.getCsvLoggerReporter());
    }

    @Test
    public void testProduceMetricRegistryWithNoJmxReporter() {

        System.setProperty(ENABLE_JMX_REPORTER_SYSTEM_PROPERTY, "false");

        final MetricRegistryWrapper metricRegistryWrapper = this.metricRegistryProducer.produceMetricsRegistry();

        System.clearProperty(ENABLE_JMX_REPORTER_SYSTEM_PROPERTY);

        Assert.assertNotNull(metricRegistryWrapper.getMetricRegistry());
        Assert.assertNull(metricRegistryWrapper.getJmxReporter());
        Assert.assertNull(metricRegistryWrapper.getCsvLoggerReporter());
    }

    @Test
    public void testProduceMetricRegistryWithCsvLoggerReporter() {

        System.setProperty(ENABLE_LOGGER_REPORTER_SYSTEM_PROPERTY, "true");

        final MetricRegistryWrapper metricRegistryWrapper = this.metricRegistryProducer.produceMetricsRegistry();

        System.clearProperty(ENABLE_LOGGER_REPORTER_SYSTEM_PROPERTY);

        Assert.assertNotNull(metricRegistryWrapper.getMetricRegistry());
        Assert.assertNotNull(metricRegistryWrapper.getJmxReporter());
        Assert.assertNotNull(metricRegistryWrapper.getCsvLoggerReporter());
    }

    @Test
    public void testProduceMetricRegistryWittNoCsvLoggerReporter() {

        System.setProperty(ENABLE_LOGGER_REPORTER_SYSTEM_PROPERTY, "false");

        final MetricRegistryWrapper metricRegistryWrapper = this.metricRegistryProducer.produceMetricsRegistry();

        System.clearProperty(ENABLE_LOGGER_REPORTER_SYSTEM_PROPERTY);

        Assert.assertNotNull(metricRegistryWrapper.getMetricRegistry());
        Assert.assertNotNull(metricRegistryWrapper.getJmxReporter());
        Assert.assertNull(metricRegistryWrapper.getCsvLoggerReporter());
    }
}
