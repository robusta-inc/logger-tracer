package com.robusta.logger.tracer;

/**
 * A logging execution tracer that can be stopped to log
 * execution statistics such as execution time.
 * @author sudhir.ravindramohan
 * @since 1.0
 */
public interface LoggerTracer<T extends LoggerTracer<T>> {
    /**
     * Signal end of tracing, compute the execution time and log as appropriate.
     */
    T stop();
}
