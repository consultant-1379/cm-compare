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

import java.lang.reflect.Method;

import javax.interceptor.InvocationContext;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Matchers;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import com.codahale.metrics.Meter;
import com.codahale.metrics.MetricRegistry;
import com.ericsson.oss.services.cm.cmconfig.service.metrics.annotation.Metered;

/**
 * Test for {@code MeteredInterceptor}.
 */
@RunWith(MockitoJUnitRunner.class)
public class MeteredInterceptorTest {

    private static final String METHOD_FOR_TESTING = "methodForTesting";

    @Mock
    private MetricRegistryWrapper mockRegistryWrapper;

    @Mock
    private InvocationContext mockInvocationContext;

    @InjectMocks
    private final MeteredInterceptor meteredInterceptor = new MeteredInterceptor();

    @Test
    public void testAroundInvoke() throws Exception {
        final Method method = this.getClass().getMethod(METHOD_FOR_TESTING);
        Mockito.when(this.mockInvocationContext.getMethod()).thenReturn(method);
        Mockito.when(this.mockInvocationContext.getTarget()).thenReturn(this);
        final Meter meter = new MetricRegistry().meter("meterName");
        Mockito.when(this.mockRegistryWrapper.meter(Matchers.anyString())).thenReturn(meter);

        // Invoke the method 3 times.
        this.meteredInterceptor.aroundInvoke(this.mockInvocationContext);
        this.meteredInterceptor.aroundInvoke(this.mockInvocationContext);
        this.meteredInterceptor.aroundInvoke(this.mockInvocationContext);

        Assert.assertEquals("The meter has to receive the event", 3, meter.getCount());
        Mockito.verify(this.mockRegistryWrapper, Mockito.times(3)).meter(Matchers.anyString());
        Mockito.verify(this.mockInvocationContext, Mockito.times(3)).proceed();
    }

    @Metered
    public void methodForTesting() {
        // Empty method annotated with @Metered for testing purpose.
    }
}
