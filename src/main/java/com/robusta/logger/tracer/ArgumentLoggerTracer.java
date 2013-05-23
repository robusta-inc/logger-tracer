package com.robusta.logger.tracer;

/**
 * A logger tracer extension that is capable of logging method arguments.
 * @param <T> ArgumentLoggerTracer implementation that is returned after logging
 *           for method chaining.
 */
public interface ArgumentLoggerTracer<T extends ArgumentLoggerTracer<T>> extends LoggerTracer<T> {
    T logArguments(Object... arguments);
}
