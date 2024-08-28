/*
 * JBoss, Home of Professional Open Source.
 * Copyright 2013, Red Hat, Inc., and individual contributors
 * as indicated by the @author tags. See the copyright.txt file in the
 * distribution for a full listing of individual contributors.
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 2.1 of
 * the License, or (at your option) any later version.
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org.
 */

package com.ericsson.oss.services.cm.export.service.jberet;

import java.util.Iterator;
import java.util.Set;

import javax.enterprise.inject.spi.Bean;
import javax.enterprise.inject.spi.BeanManager;

import org.jberet.creation.AbstractArtifactFactory;

/**
 * ArtifactFactory for Java EE runtime environment.
 * 
 * @author <a href="mailto:jperkins@redhat.com">James R. Perkins</a>
 */
public final class WildFlyArtifactFactory extends AbstractArtifactFactory {
    private final BeanManager beanManager;

    public WildFlyArtifactFactory(final BeanManager beanManager) {
        this.beanManager = beanManager;
    }

    @Override
    public Class<?> getArtifactClass(final String ref, final ClassLoader classLoader) {
        final Bean<?> bean = getBean(ref);
        return bean == null ? null : bean.getBeanClass();
    }

    @Override
    public Object create(final String ref, final Class<?> cls, final ClassLoader classLoader) throws Exception {
        final Bean<?> bean = getBean(ref);
        return bean == null ? null : this.beanManager.getReference(bean, bean.getBeanClass(), this.beanManager.createCreationalContext(bean));
    }

    private Bean<?> getBean(final String ref) {
        final Set<Bean<?>> beans = this.beanManager.getBeans(ref);
        final Iterator<Bean<?>> iter = beans.iterator();
        if (iter.hasNext()) {
            final Bean<?> bean = iter.next();
            return bean;
        }
        return null;
    }
}
