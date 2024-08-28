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
import java.lang.reflect.Method;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import com.ericsson.oss.services.cm.cmconfig.service.metrics.annotation.Metered;
import com.ericsson.oss.services.cm.cmconfig.service.metrics.annotation.Metric;
import com.ericsson.oss.services.cm.cmconfig.service.metrics.annotation.Timed;

/**
 * Test for {@code MetricNameUtil}.
 */
@RunWith(MockitoJUnitRunner.class)
public class MetricNameUtilTest {

    public String field = "";

    @Mock
    private Timed mockTimedAnnotation;

    @Mock
    private Metered mockMeteredAnnotation;

    @Mock
    private Metric mockMetricAnnotation;

    @Test
    public void testGetMetricNameforTimedMethod() {
        final Method method = this.getClass().getMethods()[0];
        final String metricName = MetricNameUtil.forTimedMethod(MetricNameUtilTest.class, method, this.mockTimedAnnotation);

        Assert.assertEquals("The metric name should be the same", MetricNameUtilTest.class.getName() + "." + method.getName(), metricName);
    }

    @Test
    public void testGetMetricNameforTimedMethodWithGroup() {
        final Method method = this.getClass().getMethods()[0];
        Mockito.when(this.mockTimedAnnotation.group()).thenReturn("groupName");
        final String metricName = MetricNameUtil.forTimedMethod(MetricNameUtilTest.class, method, this.mockTimedAnnotation);

        Assert.assertEquals("The metric name should be the same", "groupName" + "." + method.getName(), metricName);
    }

    @Test
    public void testGetMetricNameforTimedMethodWithName() {
        final Method method = this.getClass().getMethods()[0];
        Mockito.when(this.mockTimedAnnotation.name()).thenReturn("metricName");
        final String metricName = MetricNameUtil.forTimedMethod(MetricNameUtilTest.class, method, this.mockTimedAnnotation);

        Assert.assertEquals("The metric name should be the same", MetricNameUtilTest.class.getName() + "." + "metricName", metricName);
    }

    @Test
    public void testGetMetricNameforTimedMethodWithGroupAndName() {
        final Method method = this.getClass().getMethods()[0];
        Mockito.when(this.mockTimedAnnotation.group()).thenReturn("groupName");
        Mockito.when(this.mockTimedAnnotation.name()).thenReturn("metricName");
        final String metricName = MetricNameUtil.forTimedMethod(MetricNameUtilTest.class, method, this.mockTimedAnnotation);

        Assert.assertEquals("The metric name should be the same", "groupName.metricName", metricName);
    }

    @Test
    public void testGetMetricNameforMeteredMethod() {
        final Method method = this.getClass().getMethods()[0];
        final String metricName = MetricNameUtil.forMeteredMethod(MetricNameUtilTest.class, method, this.mockMeteredAnnotation);

        Assert.assertEquals("The metric name should be the same", MetricNameUtilTest.class.getName() + "." + method.getName(), metricName);
    }

    @Test
    public void testGetMetricNameforMeteredMethodWithGroup() {
        final Method method = this.getClass().getMethods()[0];
        Mockito.when(this.mockMeteredAnnotation.group()).thenReturn("groupName");
        final String metricName = MetricNameUtil.forMeteredMethod(MetricNameUtilTest.class, method, this.mockMeteredAnnotation);

        Assert.assertEquals("The metric name should be the same", "groupName" + "." + method.getName(), metricName);
    }

    @Test
    public void testGetMetricNameforMeteredMethodWithName() {
        final Method method = this.getClass().getMethods()[0];
        Mockito.when(this.mockMeteredAnnotation.name()).thenReturn("metricName");
        final String metricName = MetricNameUtil.forMeteredMethod(MetricNameUtilTest.class, method, this.mockMeteredAnnotation);

        Assert.assertEquals("The metric name should be the same", MetricNameUtilTest.class.getName() + "." + "metricName", metricName);
    }

    @Test
    public void testGetMetricNameforMeteredMethodWithGroupAndName() {
        final Method method = this.getClass().getMethods()[0];
        Mockito.when(this.mockMeteredAnnotation.group()).thenReturn("groupName");
        Mockito.when(this.mockMeteredAnnotation.name()).thenReturn("metricName");
        final String metricName = MetricNameUtil.forMeteredMethod(MetricNameUtilTest.class, method, this.mockMeteredAnnotation);

        Assert.assertEquals("The metric name should be the same", "groupName.metricName", metricName);
    }

    @Test
    public void testGetMetricNameforInjectedMetricField() {
        final Field fieldToTest = this.getClass().getFields()[0];
        final String metricName = MetricNameUtil.forInjectedMetricField(MetricNameUtilTest.class, fieldToTest, this.mockMetricAnnotation);

        Assert.assertEquals("The metric name should be the same", MetricNameUtilTest.class.getName() + "." + fieldToTest.getName(), metricName);
    }

    @Test
    public void testGetMetricNameforInjectedMetricFieldWithName() {
        final Field fieldToTest = this.getClass().getFields()[0];
        Mockito.when(this.mockMetricAnnotation.name()).thenReturn("metricName");
        final String metricName = MetricNameUtil.forInjectedMetricField(MetricNameUtilTest.class, fieldToTest, this.mockMetricAnnotation);

        Assert.assertEquals("The metric name should be the same", MetricNameUtilTest.class.getName() + "." + "metricName", metricName);
    }

    @Test
    public void testGetMetricNameforInjectedMetricFieldWithGroup() {
        final Field fieldToTest = this.getClass().getFields()[0];
        Mockito.when(this.mockMetricAnnotation.group()).thenReturn("groupName");
        final String metricName = MetricNameUtil.forInjectedMetricField(MetricNameUtilTest.class, fieldToTest, this.mockMetricAnnotation);

        Assert.assertEquals("The metric name should be the same", "groupName" + "." + fieldToTest.getName(), metricName);
    }

    @Test
    public void testGetMetricNameforInjectedMetricFieldWithNameAndGroup() {
        final Field fieldToTest = this.getClass().getFields()[0];
        Mockito.when(this.mockMetricAnnotation.group()).thenReturn("groupName");
        Mockito.when(this.mockMetricAnnotation.name()).thenReturn("metricName");
        final String metricName = MetricNameUtil.forInjectedMetricField(MetricNameUtilTest.class, fieldToTest, this.mockMetricAnnotation);

        Assert.assertEquals("The metric name should be the same", "groupName.metricName", metricName);
    }
}
