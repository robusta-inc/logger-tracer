package com.robusta.logger.tracer;

import org.slf4j.Logger;

public class MethodNameLoggingStopWatch extends LoggingStopWatch {
    private final String methodName;

    public MethodNameLoggingStopWatch(StopWatch stopWatch, Logger logger, String methodName) {
        super(stopWatch, logger);
        this.methodName = methodName;
    }

    @Override
    protected void logExecutionTime(long executionTimeInMilliSeconds, Logger logger) {
        logger.trace("Execution of '{}' completed in '{}' ms", methodName, executionTimeInMilliSeconds);
    }
}
