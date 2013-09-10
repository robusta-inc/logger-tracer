package com.robusta.logger.tracer;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.slf4j.Logger;

import java.util.ArrayList;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;

import static com.robusta.logger.tracer.LoggingMethodArgumentHandler.METHOD_ARGUMENT_TRACE_LOG_MESSAGE;
import static java.util.UUID.randomUUID;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.MockitoAnnotations.initMocks;

public class AsyncLoggingMethodArgumentHandlerTest {
    private AsyncLoggingMethodArgumentHandler handler;
    @Mock private ExecutorService service;
    private Object[] methodArguments;
    @Mock protected Logger logger;
    private String uuid;
    private String methodName;
    private ArrayList<Object> methodArgumentsAsArrayList;

    @Before
    public void setUp() throws Exception {
        initMocks(this);
        methodArguments = new Object[]{1L, "AString"};
        methodArgumentsAsArrayList = LoggingMethodArgumentHandlerTest.<Object>newArrayList(1L, "AString");
        methodName = "AsyncLoggingMethodArgumentHandlerTest.someMethod";
        uuid = randomUUID().toString();
        handler = new AsyncLoggingMethodArgumentHandler(logger, methodName, uuid, service);
    }

    @SuppressWarnings("unchecked")
    @Test
    public void testLogMethodInfoAndArguments() throws Exception {
        handler.logMethodInfoAndArguments(methodArguments);
        ArgumentCaptor<Callable> callableCaptor = ArgumentCaptor.forClass(Callable.class);
        verify(service).submit(callableCaptor.capture());
        Callable captured = callableCaptor.getValue();
        assertNotNull("A null callable was sent into the executor", captured);
        captured.call();
        verify(logger).trace(METHOD_ARGUMENT_TRACE_LOG_MESSAGE, uuid, methodName, methodArgumentsAsArrayList);
        verifyNoMoreInteractions(logger);
    }

    @After
    public void tearDown() throws Exception {
        service.shutdown();
    }
}
