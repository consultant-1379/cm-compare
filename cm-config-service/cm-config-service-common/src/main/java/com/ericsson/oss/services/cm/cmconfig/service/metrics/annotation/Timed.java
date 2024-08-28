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
package com.ericsson.oss.services.cm.cmconfig.service.metrics.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import javax.enterprise.util.Nonbinding;
import javax.interceptor.InterceptorBinding;

/**
 * An annotation for marking a method of an annotated object as timed.
 * <p/>
 * Given a method like this:
 * <pre><code> {@literal @}Timed(group = "group", name = "greatName")
 * public String greatName(String name) {
 * return "El matador " + name;
 * }
 * </code></pre>
 * <p/>
 * A timer for the defining class with the name {@code group.greatName} will be
 * created and each time the {@code #greatName(String)} method is invoked, the
 * method execution will be timed
 * <p>
 * If the Timed annotation does not specify the group or name fields, the
 * class name
 * and method name will be used for the metric name.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.METHOD, ElementType.TYPE })
@InterceptorBinding
public @interface Timed {
    /**
     * The group of the metric as part of the metric name.
     */
    @Nonbinding
    String group() default "";

    /**
     * The name of the meter as part of the metric name.
     */
    @Nonbinding
    String name() default "";
}
