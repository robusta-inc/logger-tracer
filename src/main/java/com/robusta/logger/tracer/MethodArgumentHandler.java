package com.robusta.logger.tracer;

/**
 * @author sudhir.ravindramohan
 */
public interface MethodArgumentHandler {
    void doWithMethodArguments(Object... arguments);
}
