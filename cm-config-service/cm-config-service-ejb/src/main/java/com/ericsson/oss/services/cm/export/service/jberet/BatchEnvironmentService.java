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

import java.util.Properties;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;

import javax.annotation.PostConstruct;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.enterprise.inject.spi.BeanManager;
import javax.inject.Inject;
import javax.transaction.TransactionManager;

import org.jberet.repository.JobRepository;
import org.jberet.spi.ArtifactFactory;
import org.jberet.spi.BatchEnvironment;

/**
 * @author <a href="mailto:jperkins@redhat.com">James R. Perkins</a>
 */
@Startup
@Singleton
public class BatchEnvironmentService {

    // This can be removed after the getBatchConfigurationProperties() is removed from jBeret
    private static final Properties PROPS = new Properties();

    @Inject
    BeanManager beanManager;

    @Inject
    AsynExecutorService executorService;

    @Inject
    TransactionManagerService transactionManagerService;

    private JobRepository jobRepository;
    private ClassLoader classLoader;
    private BatchEnvironment batchEnvironment = null;

    @PostConstruct
    void start() {

        this.classLoader = BatchEnvironmentService.class.getClassLoader();
        this.jobRepository = JobRepositoryFactory.getInstance().getJobRepository();

        final BatchEnvironment batchEnvironment = new WildFlyBatchEnvironment(this.beanManager, this.executorService,
                this.transactionManagerService.getTransactionManagerFromJNDI());
        // Add the service to the factory
        BatchEnvironmentFactory.getInstance().add(this.classLoader, batchEnvironment);

        this.batchEnvironment = batchEnvironment;
    }

    public BatchEnvironment getValue() throws IllegalStateException, IllegalArgumentException {
        return this.batchEnvironment;
    }

    private class WildFlyBatchEnvironment implements BatchEnvironment {

        private final ArtifactFactory artifactFactory;
        private final ExecutorService executorService;
        private final TransactionManager transactionManager;

        WildFlyBatchEnvironment(final BeanManager beanManager, final ExecutorService executorService, final TransactionManager transactionManager) {
            this.artifactFactory = (beanManager == null ? null : new WildFlyArtifactFactory(beanManager));
            this.executorService = executorService;
            this.transactionManager = transactionManager;
        }

        @Override
        public ClassLoader getClassLoader() {
            return BatchEnvironmentService.this.classLoader;
        }

        @Override
        public ArtifactFactory getArtifactFactory() {
            if (this.artifactFactory == null) {
                throw new IllegalArgumentException("BeanManager");
            }
            return this.artifactFactory;
        }

        @Override
        public Future<?> submitTask(final Runnable task) {
            return this.executorService.submit(task);
        }

        @Override
        public <T> Future<T> submitTask(final Runnable task, final T result) {
            return this.executorService.submit(task, result);
        }

        @Override
        public <T> Future<T> submitTask(final Callable<T> task) {
            return this.executorService.submit(task);
        }

        @Override
        public TransactionManager getTransactionManager() {
            if (this.transactionManager == null) {
                throw new IllegalArgumentException("TransactionManager");
            }
            return this.transactionManager;
        }

        @Override
        public JobRepository getJobRepository() {
            return BatchEnvironmentService.this.jobRepository;
        }

        /**
         * {@inheritDoc}
         * 
         * @deprecated this is no longer used in jBeret and will be removed
         * @return
         */
        @Override
        @Deprecated
        public Properties getBatchConfigurationProperties() {
            return PROPS;
        }

    }
}
