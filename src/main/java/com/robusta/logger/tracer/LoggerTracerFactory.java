package com.robusta.logger.tracer;

import org.apache.commons.lang3.time.StopWatch;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static java.util.UUID.randomUUID;

/**
 * A factory for getting {@link LoggerTracer} implementation.
 * All implementations have the following characteristics.
 * <p>Logs with a log name of LOGGER_TRACER. Logs at trace
 * level, if logger is not trace enabled, a No-Op logger
 * tracer is returned.</p>
 *
 * <p>Asynchronous logging support since version 1.2+. To
 * enable async logging, web application must register
 * {@link LoggerTracerServletContextListener} in the
 * application context {@code web.xml}. Async logging for non
 * web application is not supported.</p>
 *
 * <p>Asynchronous trace logging uses an executor service
 * with a single thread and a FIFO queue. This ensure that
 * trace logging done from a single execution flow (thread)
 * appear in the logs in the right sequence.</p>
 *
 * @see LoggerTracerServletContextListener
 * @since 1.0
 */
public final class LoggerTracerFactory {

    private static ExecutorService executor;
    private static boolean asyncEnabled = false;
    private static final Logger LOGGER = LoggerFactory.getLogger(LoggerTracerFactory.class);

    private LoggerTracerFactory() {}
    /**
     * Sets up the factory to produce logger tracers
     * with asynchronous logging support.
     *
     * <p>Only for invocation from the
     * {@link LoggerTracerServletContextListener}
     * for web application asynchronous trace logging
     * support. During web application start up.</p>
     *
     * <p>Non listener invocations are not supported and
     * the effects can be unknown.</p>
     */
    static void startAsync() {
        if(!asyncEnabled) {
            LOGGER.trace("Starting asynchronous logging, initializing executor service with a single thread executor");
            executor = Executors.newSingleThreadExecutor();
            asyncEnabled = true;
            LOGGER.trace("Async mode enabled.");
        } else {
            LOGGER.trace("Factory is already setup for asynchronous logging, this invocation to startAsync will be a no-op.");
        }
    }

    /**
     * Stops the factory from producing asynchronous
     * logger tracers.
     *
     * <p>Only for invocation from the
     * {@link LoggerTracerServletContextListener}
     * for factory tear down operations. During web
     * application shutdown</p>
     *
     * <p>Non listener invocations are not supported and
     * the effects can be unknown.</p>
     */
    static void shutdownAsync() {
        try {
            if(asyncEnabled) {
                if(executor != null) {
                    LOGGER.trace("Shutting down asynchronous logging, shutting down the executor service.");
                    executor.shutdown();
                    while (!executor.isTerminated()) {}
                    LOGGER.trace("Executor service has been terminated. No running jobs and no queued jobs either");
                } else {
                    LOGGER.trace("Executor service is null but async is enabled, this is not expected scenario. " +
                            "Skipping executor service shutdown operations");
                }
                LOGGER.trace("Async mode disabled");
            } else {
                LOGGER.trace("Factory is NOT setup for asynchronous logging, this invocation to shutdownAsync will be a no-op.");
            }
        } finally {
            executor = null;
            asyncEnabled = false;
        }
    }

    /**
     * Logs method arguments against the ClassName.methodName computed from
     * the arguments, and returns a logger-tracer that can be lapped (zero to many)
     * and finally stopped.
     *
     * <p>When no arguments are passed in, "[]" is printed.</p>
     * <p>For operations on logger-tracer, see {@link LoggerTracer}</p>
     *
     * @param classBeingTraced Class
     * @param methodBeingTraced String
     * @param methodArguments Object...
     * @return LoggerTracer
     */
    public static LoggerTracer loggerTracer(Class classBeingTraced, String methodBeingTraced, Object... methodArguments) {
        Assert.notNull(classBeingTraced, "Argument: Class being traced is mandatory to obtain logger tracer");
        Assert.notNullOrEmpty(methodBeingTraced, "Argument: Method name being traced is mandatory to obtain logger tracer");
        Logger logger = loggerToUse();
        if(logger.isTraceEnabled()) {
            LOGGER.trace("LOGGER_TRACER trace logging is enabled. An operative logger tracer will be instantiated");
            String classDotMethodName = classDotMethodName(classBeingTraced, methodBeingTraced);
            String uuid = randomUUID().toString();
            (asyncEnabled ?
                    asyncHandler(logger, classDotMethodName, uuid) :
                    defaultHandler(logger, classDotMethodName, uuid))
                    .doWithMethodArguments(methodArguments);
            return asyncEnabled ?
                    asyncLoggerTracer(logger, classDotMethodName, uuid) :
                    defaultLoggerTracer(logger, classDotMethodName, uuid);
        }
        LOGGER.trace("LOGGER_TRACER trace logging is not enabled. A no-op logger will be returned");
        return NO_OP_TRACER;
    }

    private static LoggingReStopWatch defaultLoggerTracer(Logger logger, String classDotMethodName, String uuid) {
        return new LoggingReStopWatch(stopWatchImplementationToUse(), logger, classDotMethodName, uuid);
    }

    private static AsyncLoggingReStopWatch asyncLoggerTracer(Logger logger, String classDotMethodName, String uuid) {
        return new AsyncLoggingReStopWatch(stopWatchImplementationToUse(), logger, classDotMethodName, uuid, executor);
    }

    private static LoggingMethodArgumentHandler defaultHandler(Logger logger, String classDotMethodName, String uuid) {
        return new LoggingMethodArgumentHandler(logger, classDotMethodName, uuid);
    }

    private static AsyncLoggingMethodArgumentHandler asyncHandler(Logger logger, String classDotMethodName, String uuid) {
        return new AsyncLoggingMethodArgumentHandler(logger, classDotMethodName, uuid, executor);
    }

    static final LoggerTracer NO_OP_TRACER = new NoOpLoggerTracer();

    static class NoOpLoggerTracer implements LoggerTracer {

        @Override
        public LoggerTracer stop() {
            return this;
        }

        @Override
        public LoggerTracer lap(String lapDescription) {
            return this;
        }
    }


    private static String classDotMethodName(Class classBeingTraced, String methodBeingTraced) {
        return String.format("%s.%s", classBeingTraced.getSimpleName(), methodBeingTraced);
    }

    private static StopWatch stopWatchImplementationToUse() {
        return new StopWatch();
    }

    public static final String LOGGER_NAME = "LOGGER_TRACER";
    private static final Logger TRACER_LOGGER = LoggerFactory.getLogger(LOGGER_NAME);
    private static Logger loggerToUse() {
        // replace with org.slf4j.helpers.NOPLogger.NOP_LOGGER to stop all logging.
        return TRACER_LOGGER;
    }
}
