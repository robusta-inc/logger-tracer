package com.robusta.logger.tracer;

import org.slf4j.Logger;

import java.util.*;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;

/**
 * A method argument handler that asynchronously logs a message using
 * the method arguments.
 *
 * <p>Accepts an {@link java.util.concurrent.ExecutorService} into which the method argument
 * computation and trace logging operation is submitted as a callable.
 * </p>
 *
 * @author sudhir.ravindramohan
 * @since 1.0
 */
class AsyncLoggingMethodArgumentHandler extends LoggingMethodArgumentHandler {
    private final ExecutorService executor;

    public AsyncLoggingMethodArgumentHandler(Logger logger, String methodName, String uuid, ExecutorService executor) {
        super(logger, methodName, uuid);
        this.executor = executor;
    }

    @Override
    protected void logMethodInfoAndArguments(final Object[] arguments) {
        executor.submit(new Callable<Void>() {
            @Override
            public Void call() throws Exception {
                AsyncLoggingMethodArgumentHandler.super.logMethodInfoAndArguments(arguments);
                return null;
            }
        });
    }

    @Override
    protected List<Object> argumentsToList(Object... arguments) {
        List<Object> argumentList = new ArrayList<Object>();
        if(arguments != null) {
            for (Object argument : arguments) {
                argumentList.add(intercept(argument));
            }
        }
        return argumentList;
    }

    /**
     * Interception of {@link java.util.Collection} and {@link java.util.Map}
     * type of arguments will be required to avoid the possibility
     * of a {@link java.util.ConcurrentModificationException} due to iterative
     * operations on multiple threads.
     *
     * @param argument Object argument to be intercepted.
     * @return Object intercepted argument
     * @see java.util.ConcurrentModificationException
     */
    @SuppressWarnings("unchecked")
    private Object intercept(Object argument) {
        if(argument != null) {
            Class<?> argumentClass = argument.getClass();
            if(Collection.class.isAssignableFrom(argumentClass)) {
                return new ArrayList(Collection.class.cast(argument));
            } else if(Map.class.isAssignableFrom(argumentClass)) {
                return new LinkedHashMap(Map.class.cast(argument));
            }
        }
        return argument;
    }
}
