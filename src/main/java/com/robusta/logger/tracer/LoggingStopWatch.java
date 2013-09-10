package com.robusta.logger.tracer;

import org.apache.commons.lang3.time.StopWatch;
import org.slf4j.Logger;

/**
 * A logger-tracer implementation that operates on a {@link StopWatch} and
 * a {@link Logger}.
 *
 * <p>When the logger is not trace enabled, becomes a no-op logger-tracer</p>
 * <p>Starts the {@link StopWatch} on initialization</p>
 * <p>Lap and Stop implementation that use the {@link StopWatch} for timing
 * and log execution (stop) and lap (lap) time info using the {@link Logger}</p>
 *
 * <p>The class is not thread-safe since the {@link StopWatch} being used isn't</p>
 *
 * @author sudhir.ravindramohan
 * @since 1.0
 */
class LoggingStopWatch implements LoggerTracer {
    static final String STOP_NOT_CALLED_ON_TRACER = "Stop not called on tracer : '{}'";
    static final String LAP_COMPLETION_TRACE_MSG = "Execution: '{}', Lap: '{}' completed in '{}' ms";
    static final String EXECUTION_COMPLETION_TRACE_MSG = "Execution: '{}' for '{}' completed in '{}' ms";
    private final StopWatch stopWatch;
    private final Logger logger;
    private final String methodName;
    private final String uuid;
    private boolean isStopped = false;


    public LoggingStopWatch(StopWatch stopWatch, Logger logger, String methodName, String uuid) {
        this.uuid = uuid;
        Assert.notNull(stopWatch, "Non-null stop watch is mandatory");
        Assert.notNull(logger, "Non-null logger is mandatory");
        Assert.notNullOrEmpty(methodName, "A valid (not null and not empty) method name is required");
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
            stopWatch.stop();
            logExecutionTime(stopWatch.getTime(), logger);
        }
        isStopped = true;
        return this;
    }

    @Override
    public LoggingStopWatch lap(String lapDescription) {
        if(logger.isTraceEnabled()) {
            logLapTime(stopWatch.getTime(), lapDescription, logger);
        }
        return this;
    }

    protected void logLapTime(long lapTime, String lapDescription, Logger logger) {
        logger.trace(LAP_COMPLETION_TRACE_MSG, uuid, lapDescription, lapTime);
    }

    protected void logExecutionTime(long executionTimeInMilliSeconds, Logger logger) {
        logger.trace(EXECUTION_COMPLETION_TRACE_MSG, uuid, methodName, executionTimeInMilliSeconds);
    }

    protected Logger getLogger() {
        return logger;
    }

    @Override
    protected void finalize() throws Throwable {
        if (!isStopped) {
            logger.warn(STOP_NOT_CALLED_ON_TRACER, uuid);
        }
        super.finalize();
    }
}
