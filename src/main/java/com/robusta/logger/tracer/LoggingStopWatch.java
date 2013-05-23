package com.robusta.logger.tracer;

import org.slf4j.Logger;

public class LoggingStopWatch implements Stoppable<LoggingStopWatch> {
    private final StopWatch stopWatch;
    private final Logger logger;

    public LoggingStopWatch(StopWatch stopWatch, Logger logger) {
        this.stopWatch = stopWatch;
        this.logger = logger;
        if(logger.isTraceEnabled()) {
            stopWatch.start();
        }
    }

    @Override
    public LoggingStopWatch stop() {
        if(logger.isTraceEnabled()) {
            long executionTime = stopWatch.stopAndGet();
            logExecutionTime(executionTime, logger);
        }
        return this;
    }

    protected void logExecutionTime(long executionTimeInMilliSeconds, Logger logger) {
        logger.trace("Execution completed in '{}' ms", executionTimeInMilliSeconds);
    }
}
