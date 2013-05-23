package com.robusta.logger.tracer;

public class MethodArgumentsAndExecutionTimeLoggerTracer implements ArgumentLoggerTracer<MethodArgumentsAndExecutionTimeLoggerTracer> {
    private final MethodArgumentHandler methodArgumentHandler;
    private final LoggingStopWatch stopWatch;

    public MethodArgumentsAndExecutionTimeLoggerTracer(MethodArgumentHandler methodArgumentHandler, LoggingStopWatch stopWatch) {
        Assert.notNull(methodArgumentHandler, "Non null MethodArgumentHandler required.");
        Assert.notNull(stopWatch, "Non null LoggingStopWatch required.");
        this.methodArgumentHandler = methodArgumentHandler;
        this.stopWatch = stopWatch;
    }

    @Override
    public MethodArgumentsAndExecutionTimeLoggerTracer stop() {
        stopWatch.stop(); return this;
    }

    @Override
    public MethodArgumentsAndExecutionTimeLoggerTracer logArguments(Object... arguments) {
        methodArgumentHandler.doWithMethodArguments(arguments); return this;
    }
}
