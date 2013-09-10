package com.robusta.logger.tracer;

import org.slf4j.Logger;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * A method argument handler that logs a message using the method
 * arguments.
 *
 * <p>Requires a logger and a method name (as: ClassName.methodName)</p>
 * <p>When logger is not trace enabled, is a NO-OP argument handler.</p>
 *
 * @author sudhir.ravindramohan
 * @since 1.0
 */
class LoggingMethodArgumentHandler implements MethodArgumentHandler {
    static final String METHOD_ARGUMENT_TRACE_LOG_MESSAGE = "Execution: '{}' of '{}' started with arguments '{}'";
    private final Logger logger;
    private final String methodName;
    private final String uuid;

    public LoggingMethodArgumentHandler(Logger logger, String methodName, String uuid) {
        Assert.notNull(logger, "Non-null Logger instance is required");
        Assert.notNullOrEmpty(methodName, "A valid (not null and not empty) method name is required");
        this.logger = logger;
        this.methodName = methodName;
        this.uuid = uuid;
    }

    @Override
    public void doWithMethodArguments(Object... arguments) {
        if(logger.isTraceEnabled()) {
            logMethodInfoAndArguments(arguments);
        }
    }

    protected void logMethodInfoAndArguments(Object[] arguments) {
        logger.trace(METHOD_ARGUMENT_TRACE_LOG_MESSAGE, uuid, methodName, argumentsToList(arguments));
    }

    protected List<Object> argumentsToList(Object... arguments) {
        List<Object> argumentList = new ArrayList<Object>();
        if(arguments != null) {
            Collections.addAll(argumentList, arguments);
        }
        return argumentList;
    }
}
