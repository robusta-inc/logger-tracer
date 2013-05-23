package com.robusta.logger.tracer;

public class ExecutionTimeLoggerTracer implements LoggerTracer<ExecutionTimeLoggerTracer> {
    private final LoggingStopWatch stopWatch;

    public ExecutionTimeLoggerTracer(LoggingStopWatch stopWatch) {
        this.stopWatch = stopWatch;
    }

    @Override
    public ExecutionTimeLoggerTracer stop() {
        stopWatch.stop(); return this;
    }
}
