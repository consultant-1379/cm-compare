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

import com.codahale.metrics.MetricRegistry;
import com.codahale.metrics.Timer;
import com.ericsson.oss.services.cm.cmconfig.service.metrics.annotation.Timed;

/**
 * Test for {@code TimedInterceptor}.
 */
@RunWith(MockitoJUnitRunner.class)
public class TimedInterceptorTest {

    private static final String METHOD_FOR_TESTING = "methodForTesting";

    @Mock
    private MetricRegistryWrapper mockRegistryWrapper;

    @Mock
    private InvocationContext mockInvocationContext;

    @InjectMocks
    private final TimedInterceptor timedInterceptor = new TimedInterceptor();

    @Test
    public void testAroundInvoke() throws Exception {
        final Method method = this.getClass().getMethod(METHOD_FOR_TESTING);
        Mockito.when(this.mockInvocationContext.getMethod()).thenReturn(method);
        Mockito.when(this.mockInvocationContext.getTarget()).thenReturn(this);
        final Timer timer = new MetricRegistry().timer("timerName");
        Mockito.when(this.mockRegistryWrapper.timer(Matchers.anyString())).thenReturn(timer);

        // Invoke the method 3 times.
        this.timedInterceptor.aroundInvoke(this.mockInvocationContext);
        this.timedInterceptor.aroundInvoke(this.mockInvocationContext);
        this.timedInterceptor.aroundInvoke(this.mockInvocationContext);

        Assert.assertEquals("The timer has to receive the event", 3, timer.getCount());
        Mockito.verify(this.mockRegistryWrapper, Mockito.times(3)).timer(Matchers.anyString());
        Mockito.verify(this.mockInvocationContext, Mockito.times(3)).proceed();
    }

    @Timed
    public void methodForTesting() {
        // Empty method annotated with @Timed for testing purpose.
    }
}
