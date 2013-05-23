package com.robusta.logger.tracer;

/**
 * @author sudhir.ravindramohan
 * @since 1.0
 */
public interface Stoppable<T extends Stoppable<T>> {
    T stop();
}
