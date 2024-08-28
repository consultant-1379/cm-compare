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

import java.lang.reflect.Field;

import javax.enterprise.inject.spi.Annotated;
import javax.enterprise.inject.spi.InjectionPoint;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import com.codahale.metrics.Counter;
import com.codahale.metrics.Histogram;
import com.codahale.metrics.Meter;
import com.codahale.metrics.Timer;
import com.ericsson.oss.services.cm.cmconfig.service.metrics.annotation.Metric;

/**
 * Test for {@code MetricProducer}.
 */
@RunWith(MockitoJUnitRunner.class)
public class MetricProducerTest {

    @Metric
    public Timer timerField;

    @Metric(group = "groupName", name = "metricName")
    public Timer timerFieldWithOptions;

    @Metric
    public Meter meterField;

    @Metric(group = "groupName", name = "metricName")
    public Meter meterFieldWithOptions;

    @Metric
    public Counter counterField;

    @Metric(group = "groupName", name = "metricName")
    public Counter counterFieldWithOptions;

    @Metric
    public Histogram histogramField;

    @Metric(group = "groupName", name = "metricName")
    public Histogram histogramFieldWithOptions;

    @Mock
    private MetricRegistryWrapper mockMetricRegistryWrapper;

    @Mock
    private InjectionPoint mockInjectionPoint;

    @Mock
    private Annotated mockAnnotated;

    @Mock
    private Metric metricAnnotation;

    @InjectMocks
    private final MetricProducer metricProducer = new MetricProducer();

    @Before
    public void setUp() {
        Mockito.when(this.mockInjectionPoint.getAnnotated()).thenReturn(this.mockAnnotated);
        Mockito.when(this.mockAnnotated.getAnnotation(Metric.class)).thenReturn(this.metricAnnotation);
    }

    @Test
    public void produceTimerTestWithDefaultAnnotation() throws NoSuchFieldException {
        Mockito.when(this.mockInjectionPoint.getMember()).thenReturn(this.getClass().getField("timerField"));
        this.metricProducer.produceTimer(this.mockInjectionPoint);
        Mockito.verify(this.mockMetricRegistryWrapper).timer(this.getClass().getName() + ".timerField");
    }

    @Test
    public void produceTimerTestWithOptions() throws NoSuchFieldException {
        final Field field = this.getClass().getField("timerFieldWithOptions");
        Mockito.when(this.mockInjectionPoint.getMember()).thenReturn(field);
        Mockito.when(this.mockAnnotated.getAnnotation(Metric.class)).thenReturn(field.getAnnotation(Metric.class));

        this.metricProducer.produceTimer(this.mockInjectionPoint);
        Mockito.verify(this.mockMetricRegistryWrapper).timer("groupName.metricName");
    }

    @Test
    public void produceMeterTestWithDefaultAnnotation() throws NoSuchFieldException {
        Mockito.when(this.mockInjectionPoint.getMember()).thenReturn(this.getClass().getField("meterField"));
        this.metricProducer.produceMeter(this.mockInjectionPoint);
        Mockito.verify(this.mockMetricRegistryWrapper).meter(this.getClass().getName() + ".meterField");
    }

    @Test
    public void produceMeterTestWithOptions() throws NoSuchFieldException {
        final Field field = this.getClass().getField("meterFieldWithOptions");
        Mockito.when(this.mockInjectionPoint.getMember()).thenReturn(field);
        Mockito.when(this.mockAnnotated.getAnnotation(Metric.class)).thenReturn(field.getAnnotation(Metric.class));

        this.metricProducer.produceMeter(this.mockInjectionPoint);
        Mockito.verify(this.mockMetricRegistryWrapper).meter("groupName.metricName");
    }

    @Test
    public void produceCounterTestWithDefaultAnnotation() throws NoSuchFieldException {
        Mockito.when(this.mockInjectionPoint.getMember()).thenReturn(this.getClass().getField("counterField"));
        this.metricProducer.produceCounter(this.mockInjectionPoint);
        Mockito.verify(this.mockMetricRegistryWrapper).counter(this.getClass().getName() + ".counterField");
    }

    @Test
    public void produceCounterTestWithOptions() throws NoSuchFieldException {
        final Field field = this.getClass().getField("counterFieldWithOptions");
        Mockito.when(this.mockInjectionPoint.getMember()).thenReturn(field);
        Mockito.when(this.mockAnnotated.getAnnotation(Metric.class)).thenReturn(field.getAnnotation(Metric.class));

        this.metricProducer.produceCounter(this.mockInjectionPoint);
        Mockito.verify(this.mockMetricRegistryWrapper).counter("groupName.metricName");
    }

    @Test
    public void produceHistrogramTestWithDefaultAnnotation() throws NoSuchFieldException {
        Mockito.when(this.mockInjectionPoint.getMember()).thenReturn(this.getClass().getField("histogramField"));
        this.metricProducer.produceHistogram(this.mockInjectionPoint);
        Mockito.verify(this.mockMetricRegistryWrapper).histogram(this.getClass().getName() + ".histogramField");
    }

    @Test
    public void produceHistogramTestWithOptions() throws NoSuchFieldException {
        final Field field = this.getClass().getField("histogramFieldWithOptions");
        Mockito.when(this.mockInjectionPoint.getMember()).thenReturn(field);
        Mockito.when(this.mockAnnotated.getAnnotation(Metric.class)).thenReturn(field.getAnnotation(Metric.class));

        this.metricProducer.produceHistogram(this.mockInjectionPoint);
        Mockito.verify(this.mockMetricRegistryWrapper).histogram("groupName.metricName");
    }
}
