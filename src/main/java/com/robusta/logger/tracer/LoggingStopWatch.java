package com.robusta.logger.tracer;

import org.apache.commons.lang3.time.StopWatch;
import org.slf4j.Logger;

public class LoggingStopWatch implements LoggerTracer<LoggingStopWatch>{
    private final StopWatch stopWatch;
    private final Logger logger;
    private final String methodName;

    public LoggingStopWatch(StopWatch stopWatch, Logger logger, String methodName) {
        this.stopWatch = stopWatch;
        this.logger = logger;
        this.methodName = methodName;
        if(logger.isTraceEnabled()) {
            stopWatch.start();
        }
    }

    @Override
    public LoggingStopWatch stop() {
        if(logger.isTraceEnabled()) {
            long executionTime = stopWatch.getTime();
            logExecutionTime(executionTime, logger);
        }
        return this;
    }

    protected void logExecutionTime(long executionTimeInMilliSeconds, Logger logger) {
        logger.trace("Execution of '{}' completed in '{}' ms", methodName, executionTimeInMilliSeconds);
    }
}
