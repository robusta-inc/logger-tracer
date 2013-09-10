package com.robusta.logger.tracer;

import org.apache.commons.lang3.time.StopWatch;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.RejectedExecutionException;

/**
 * Asynchronous version of the {@link LoggingReStopWatch}
 *
 * <p>Accepts an {@link java.util.concurrent.ExecutorService} into which the logging
 * operations are submitted as a callable.</p>
 *
 * @author sudhir.ravindramohan
 * @since 1.0
 */
class AsyncLoggingReStopWatch extends LoggingReStopWatch {
    private final ExecutorService executor;
    private static final Logger LOGGER = LoggerFactory.getLogger(AsyncLoggingReStopWatch.class);

    public AsyncLoggingReStopWatch(StopWatch stopWatch, Logger logger, String methodName, String uuid, ExecutorService executor) {
		super(stopWatch, logger, methodName, uuid);
        this.executor = executor;
    }

    @Override
    protected void logLapTime(final long lapTime, final String lapDescription, final Logger logger) {
        Callable<Void> callable = new Callable<Void>() {
            @Override
            public Void call() throws Exception {
                AsyncLoggingReStopWatch.super.logLapTime(lapTime, lapDescription, logger);
                return null;
            }
        };
        submitAndForget(callable);
    }

    private void submitAndForget(Callable<Void> callable) {
        try {
            executor.submit(callable);
        } catch (RejectedExecutionException e) {
            LOGGER.warn("Logging async request submission into the executor service has failed with RejectedExecutionException, logging will be impacted.", e);
        }
    }

    @Override
    protected void logExecutionTime(final long executionTimeInMilliSeconds, final Logger logger) {
        submitAndForget(new Callable<Void>() {
            @Override
            public Void call() throws Exception {
                AsyncLoggingReStopWatch.super.logExecutionTime(executionTimeInMilliSeconds, logger);
                return null;
            }
        });
    }
}
