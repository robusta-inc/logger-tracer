package com.robusta.logger.tracer;

public interface ArgumentLoggerTracer<T extends ArgumentLoggerTracer<T>> extends LoggerTracer<T> {
    T logArguments(Object... arguments);
}
