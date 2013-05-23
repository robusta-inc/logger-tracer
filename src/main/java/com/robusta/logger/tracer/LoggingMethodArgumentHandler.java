package com.robusta.logger.tracer;

import org.slf4j.Logger;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class LoggingMethodArgumentHandler implements MethodArgumentHandler {
    private final Logger logger;
    private final String methodName;
    public LoggingMethodArgumentHandler(Logger logger, String methodName) {
        Assert.notNull(logger, "Non null Logger is required.");
        Assert.notNullOrEmpty(methodName, "A valid method name is required.");
        this.logger = logger;
        this.methodName = methodName;
    }

    @Override
    public void doWithMethodArguments(Object... arguments) {
        if(logger.isTraceEnabled()) {
            logMethodInfoAndArguments(arguments);
        }
    }

    protected void logMethodInfoAndArguments(Object[] arguments) {
        logger.trace("Execution of '{}' started with arguments '{}'", methodName, argumentsToList(arguments));
    }

    private List<Object> argumentsToList(Object... arguments) {
        List<Object> argumentList = new ArrayList<Object>();
        if(arguments != null) {
            Collections.addAll(argumentList, arguments);
        }
        return argumentList;
    }
}
