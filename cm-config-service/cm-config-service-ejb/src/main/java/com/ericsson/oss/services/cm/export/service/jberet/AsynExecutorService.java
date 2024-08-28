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
package com.ericsson.oss.services.cm.export.service.jberet;

import java.util.Collection;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import javax.ejb.AsyncResult;
import javax.ejb.Asynchronous;

public class AsynExecutorService implements ExecutorService {

    protected Executor threadPoolExecutor;

    @Asynchronous
    @Override
    public <T> Future<T> submit(final Callable<T> task) {
        try {
            return new AsyncResult<T>(task.call());
        } catch (final Exception e) {
            throw new IllegalArgumentException("Exception in Async Callable");
        }
    }

    /*
     * (non-Javadoc)
     * @see java.util.concurrent.Executor#execute(java.lang.Runnable)
     */
    @Asynchronous
    @Override
    public void execute(final Runnable command) {
        command.run();
    }

    /*
     * (non-Javadoc)
     * @see java.util.concurrent.ExecutorService#shutdown()
     */
    @Override
    public void shutdown() {
        // TODO Auto-generated method stub

    }

    /*
     * (non-Javadoc)
     * @see java.util.concurrent.ExecutorService#shutdownNow()
     */
    @Override
    public List<Runnable> shutdownNow() {
        // TODO Auto-generated method stub
        return null;
    }

    /*
     * (non-Javadoc)
     * @see java.util.concurrent.ExecutorService#isShutdown()
     */
    @Override
    public boolean isShutdown() {
        // TODO Auto-generated method stub
        return false;
    }

    /*
     * (non-Javadoc)
     * @see java.util.concurrent.ExecutorService#isTerminated()
     */
    @Override
    public boolean isTerminated() {
        // TODO Auto-generated method stub
        return false;
    }

    /*
     * (non-Javadoc)
     * @see java.util.concurrent.ExecutorService#awaitTermination(long, java.util.concurrent.TimeUnit)
     */
    @Override
    public boolean awaitTermination(final long timeout, final TimeUnit unit) throws InterruptedException {
        // TODO Auto-generated method stub
        return false;
    }

    /*
     * (non-Javadoc)
     * @see java.util.concurrent.ExecutorService#submit(java.lang.Runnable, java.lang.Object)
     */
    @Asynchronous
    @Override
    public <T> Future<T> submit(final Runnable task, final T result) {
        task.run();
        return new AsyncResult<T>(result);

    }

    /*
     * (non-Javadoc)
     * @see java.util.concurrent.ExecutorService#submit(java.lang.Runnable)
     */
    @Asynchronous
    @Override
    public Future<?> submit(final Runnable task) {
        task.run();
        return null;
    }

    /*
     * (non-Javadoc)
     * @see java.util.concurrent.ExecutorService#invokeAll(java.util.Collection)
     */
    @Override
    public <T> List<Future<T>> invokeAll(final Collection<? extends Callable<T>> tasks) throws InterruptedException {
        // TODO Auto-generated method stub
        return null;
    }

    /*
     * (non-Javadoc)
     * @see java.util.concurrent.ExecutorService#invokeAll(java.util.Collection, long, java.util.concurrent.TimeUnit)
     */
    @Override
    public <T> List<Future<T>> invokeAll(final Collection<? extends Callable<T>> tasks, final long timeout, final TimeUnit unit)
            throws InterruptedException {
        // TODO Auto-generated method stub
        return null;
    }

    /*
     * (non-Javadoc)
     * @see java.util.concurrent.ExecutorService#invokeAny(java.util.Collection)
     */
    @Override
    public <T> T invokeAny(final Collection<? extends Callable<T>> tasks) throws InterruptedException, ExecutionException {
        // TODO Auto-generated method stub
        return null;
    }

    /*
     * (non-Javadoc)
     * @see java.util.concurrent.ExecutorService#invokeAny(java.util.Collection, long, java.util.concurrent.TimeUnit)
     */
    @Override
    public <T> T invokeAny(final Collection<? extends Callable<T>> tasks, final long timeout, final TimeUnit unit) throws InterruptedException,
            ExecutionException, TimeoutException {
        // TODO Auto-generated method stub
        return null;
    }

}
