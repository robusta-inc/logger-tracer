package com.robusta.logger.tracer;

public interface StopWatch<T extends StopWatch<T>> extends Stoppable<T> {
    T start();
    long stopAndGet();
}
