package com.robusta.logger.tracer;

import org.apache.commons.lang3.time.StopWatch;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.helpers.NOPLogger;

/**
 * A factory for getting {@link LoggerTracer} implementations.
 * All implementations have the following characteristics.
 * <p>Log with a log name of LOGGER_TRACER.</p>
 * <p>Log at trace level, if logger is not trace enabled,
 * all operations are suppressed.</p>
 */
public abstract class LoggerTracerFactory {
    /**
     * A simple logger tracer that can be stopped to log execution time.
     * The ClassName.methodName from which tracing is done is deduced for logging.
     * @return {@link LoggerTracer}
     */
    public static LoggerTracer loggerTracer() {
        return new LoggingStopWatch(stopWatchImplementationToUse(), loggerToUse(), deduceMethodName());
    }

    private static String deduceMethodName() {
        // 0 is Thread.getStackTrace,
        // 1 is deduceMethodName,
        // 2 will be the caller of deduceMethodName in LoggerTracerFactory,
        // 3 will be the method using LoggerTracerFactory
        StackTraceElement stackTraceElement = Thread.currentThread().getStackTrace()[3];
        return String.format("%s.%s", stackTraceElement.getClassName(), stackTraceElement.getMethodName());
    }


    /**
     * A simple logger tracer that can be stopped to log execution time.
     * Accepts the method name (should be ClassName.methodName) to be used in
     * logging.
     * @param methodName String (should be ClassName.methodName)
     * @return {@link LoggerTracer}
     */
    public static LoggerTracer loggerTracer(String methodName) {
        return new LoggingStopWatch(stopWatchImplementationToUse(), loggerToUse(), methodName);
    }

    /**
     * An argument logging capable logger tracer.
     * Accepts the method name (should be ClassName.methodName) to be used in
     * logging.
     * @param methodName String (should be ClassName.methodName)
     * @return {@link ArgumentLoggerTracer}
     */
    public static ArgumentLoggerTracer argumentLoggerTracer(String methodName) {
        return argumentLoggerTracer(new LoggingMethodArgumentHandler(loggerToUse(), methodName), new LoggingStopWatch(stopWatchImplementationToUse(), loggerToUse(), methodName));
    }

    /**
     * An argument logging capable logger tracer.
     * The ClassName.methodName from which tracing is done is deduced for logging.
     * @return {@link ArgumentLoggerTracer}
     */
    public static ArgumentLoggerTracer argumentLoggerTracer() {
        String methodName = deduceMethodName();
        return argumentLoggerTracer(new LoggingMethodArgumentHandler(loggerToUse(), methodName), new LoggingStopWatch(stopWatchImplementationToUse(), loggerToUse(), methodName));
    }

    private static StopWatch stopWatchImplementationToUse() {
        return new StopWatch();
    }

    private static final Logger NOP_LOGGER = NOPLogger.NOP_LOGGER;
    public static final String LOGGER_NAME = "LOGGER_TRACER";
    private static final Logger TRACER_LOGGER = LoggerFactory.getLogger(LOGGER_NAME);
    private static Logger loggerToUse() {
        // replace with NOP_LOGGER to stop all logging.
        return TRACER_LOGGER;
    }

    private static ArgumentLoggerTracer argumentLoggerTracer(final MethodArgumentHandler methodArgumentHandler, final LoggingStopWatch stopWatch) {
        return new ArgumentLoggerTracer() {
            @Override
            public ArgumentLoggerTracer stop() {
                stopWatch.stop();
                return this;
            }

            @Override
            public ArgumentLoggerTracer logArguments(Object... arguments) {
                methodArgumentHandler.doWithMethodArguments(arguments);
                return this;
            }
        };
    }
}
