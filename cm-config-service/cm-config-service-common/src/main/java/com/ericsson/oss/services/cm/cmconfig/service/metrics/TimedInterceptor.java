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

import javax.inject.Inject;
import javax.interceptor.AroundInvoke;
import javax.interceptor.Interceptor;
import javax.interceptor.InvocationContext;

import com.codahale.metrics.Timer;
import com.ericsson.oss.services.cm.cmconfig.service.metrics.annotation.ApplicationMetrics;
import com.ericsson.oss.services.cm.cmconfig.service.metrics.annotation.Timed;

/**
 * CDI interceptor for the {@code Timed} annotation.
 * Intercept invocations of bean methods annotated with {@code Timed} annotation.
 * <p>
 * The CDI implementation automatically registers new Metric instances in the {@code MetricRegistryWrapper} registry
 * resolved for the CDI application.
 * <p>
 * A new metric is registered only the first time the method is intercepted for
 * the same metric name.
 */
@Interceptor
@Timed
public class TimedInterceptor {

    @Inject
    @ApplicationMetrics
    private MetricRegistryWrapper metricRegistryWrapper;

    /**
     * Interceptor method.
     *
     * @param ctx
     *            the invocation context
     * @return the return value of the method intercepted
     * @throws Exception
     *             occurred
     */
    @AroundInvoke
    @SuppressWarnings("PMD.SignatureDeclareThrowsException")
    public Object aroundInvoke(final InvocationContext ctx) throws Exception {

        Timed timed = ctx.getMethod().getAnnotation(Timed.class);

        if (timed == null) {
            timed = ctx.getTarget().getClass().getAnnotation(Timed.class);
        }

        if (timed != null) {
            final String metricName = MetricNameUtil.forTimedMethod(ctx.getTarget().getClass(), ctx.getMethod(), timed);

            final Timer timer = this.metricRegistryWrapper.timer(metricName);

            final Timer.Context context = timer.time();
            try {
                return ctx.proceed();
            } finally {
                context.stop();
            }
        }
        return ctx.proceed();
    }
}
