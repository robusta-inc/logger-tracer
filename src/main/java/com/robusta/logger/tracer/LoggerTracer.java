package com.robusta.logger.tracer;

/**
 * A logging execution tracer that can be stopped to log
 * execution statistics such as execution time.
 *
 * <p>A {@code lap()} api to log lap timing statistics</p>
 *
 * @author sudhir.ravindramohan
 * @since 1.0
 */
public interface LoggerTracer {
    /**
     * Signal end of tracing, compute the execution time and log as appropriate.
     * Should be called only once.
     */
    LoggerTracer stop();

    /**
     * Lap time computation and logging for an event described by lapDescription
     * Can be called multiple time between initialization and invocation of {@code stop()}
     * @param lapDescription Description of the lap event
     */
    LoggerTracer lap(String lapDescription);
}
