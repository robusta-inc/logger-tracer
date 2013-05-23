package com.robusta.logger.tracer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.helpers.NOPLogger;

public abstract class LoggerTracerFactory {
    private static final Logger NOP_LOGGER = NOPLogger.NOP_LOGGER;
    private static final Logger TRACER_LOGGER = LoggerFactory.getLogger("LOGGER_TRACER");

    public static LoggerTracer loggerTracer() {
        return new ExecutionTimeLoggerTracer(new MethodNameLoggingStopWatch(stopWatchImplementationToUse(), loggerToUse(), deduceMethodName()));
    }

    private static String deduceMethodName() {
        // 0 is Thread.getStackTrace,
        // 1 is deduceMethodName,
        // 2 will be the caller of deduceMethodName in LoggerTracerFactory,
        // 3 will be the method using LoggerTracerFactory
        StackTraceElement stackTraceElement = Thread.currentThread().getStackTrace()[3];
        return String.format("%s.%s", stackTraceElement.getClassName(), stackTraceElement.getMethodName());
    }


    public static LoggerTracer loggerTracer(String methodName) {
        return new ExecutionTimeLoggerTracer(new MethodNameLoggingStopWatch(stopWatchImplementationToUse(), loggerToUse(), methodName));
    }

    public static ArgumentLoggerTracer argumentLoggerTracer(String methodName) {
        return new MethodArgumentsAndExecutionTimeLoggerTracer(new LoggingMethodArgumentHandler(loggerToUse(), methodName), new MethodNameLoggingStopWatch(stopWatchImplementationToUse(), loggerToUse(), methodName));
    }

    public static ArgumentLoggerTracer argumentLoggerTracer() {
        String methodName = deduceMethodName();
        return new MethodArgumentsAndExecutionTimeLoggerTracer(new LoggingMethodArgumentHandler(loggerToUse(), methodName), new MethodNameLoggingStopWatch(stopWatchImplementationToUse(), loggerToUse(), methodName));
    }

    private static SystemTimeBasedStopWatch stopWatchImplementationToUse() {
        return new SystemTimeBasedStopWatch();
    }

    private static Logger loggerToUse() {
        return TRACER_LOGGER;
    }
}
