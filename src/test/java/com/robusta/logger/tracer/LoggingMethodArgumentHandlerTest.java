package com.robusta.logger.tracer;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.slf4j.Logger;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static com.robusta.logger.tracer.LoggingMethodArgumentHandler.METHOD_ARGUMENT_TRACE_LOG_MESSAGE;
import static java.util.UUID.randomUUID;
import static org.mockito.Mockito.*;
import static org.mockito.MockitoAnnotations.initMocks;

@SuppressWarnings("ALL")
public class LoggingMethodArgumentHandlerTest {
    public static final ArrayList<Object> NO_ARG_LIST = new ArrayList<Object>();
    private LoggingMethodArgumentHandler argumentHandler;
    @Mock private Logger logger;
    private String methodName;
    private Object aNull;
    private Object[] methodArguments;
    private ArrayList<Object> methodArgumentsAsArrayList;
    private String uuid;

    @Before
    public void setUp() throws Exception {
        initMocks(this);
        methodName = "LoggingMethodArgumentHandlerTest.someMethod";
        uuid = randomUUID().toString();
        argumentHandler = new LoggingMethodArgumentHandler(logger, methodName, uuid);
        List<String> anArrayList = new ArrayList<String>();
        Bean aBean = new Bean();
        methodArguments = new Object[]{1L, "AString", anArrayList, aNull, aBean};
        methodArgumentsAsArrayList = newArrayList(1L, "AString", anArrayList, aNull, aBean);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInitialization_whenLoggerIsInvalid_shouldRaiseException() throws Exception {
        new LoggingMethodArgumentHandler(null, methodName, uuid);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInitialization_whenMethodNameIsInvalid_shouldRaiseException() throws Exception {
        new LoggingMethodArgumentHandler(logger, null, uuid);
    }

    @Test
    public void testDoWithMethodArguments_whenNoArgumentsSpecified() throws Exception {
        expectingIsTraceEnabledCallOnTheLogger_willReturn(true);
        argumentHandler.doWithMethodArguments();
        verifyThatMessageIsLoggedAtTraceLevelWithMethodArguments(NO_ARG_LIST);
    }

    private void verifyThatMessageIsLoggedAtTraceLevelWithMethodArguments(ArrayList<Object> argumentList) {
        verify(logger).isTraceEnabled();
        verify(logger).trace(METHOD_ARGUMENT_TRACE_LOG_MESSAGE, uuid, methodName, argumentList);
        verifyNoMoreInteractions(logger);
    }

    private void expectingIsTraceEnabledCallOnTheLogger_willReturn(boolean enabled) {
        when(logger.isTraceEnabled()).thenReturn(enabled);
    }

    @Test
    public void testDoWithMethodArguments_whenEmptyArrayArgumentsSpecified() throws Exception {
        expectingIsTraceEnabledCallOnTheLogger_willReturn(true);
        argumentHandler.doWithMethodArguments(new Object[]{});
        verifyThatMessageIsLoggedAtTraceLevelWithMethodArguments(NO_ARG_LIST);
    }

    @Test
    public void testDoWithMethodArguments_whenSomeArguments() throws Exception {
        expectingIsTraceEnabledCallOnTheLogger_willReturn(true);
        argumentHandler.doWithMethodArguments(methodArguments);
        verifyThatMessageIsLoggedAtTraceLevelWithMethodArguments(methodArgumentsAsArrayList);
    }

    @Test
    public void testTraceLevelIsNotEnabledOnLogger_shouldBeANoOp() throws Exception {
        expectingIsTraceEnabledCallOnTheLogger_willReturn(false);
        argumentHandler.doWithMethodArguments(methodArguments);
        verifyThatNoInteractionsOnLogger();
    }

    private void verifyThatNoInteractionsOnLogger() {
        verify(logger).isTraceEnabled();
        verifyNoMoreInteractions(logger);
    }

    protected static <T> ArrayList<T> newArrayList(T... elements) {
        ArrayList<T> arrayList = new ArrayList<T>();
        if(elements != null) {
            Collections.addAll(arrayList, elements);
        }
        return arrayList;
    }

    private class Bean {
        private final int count;
        private final String value;


        private Bean() {
            this.count = 10;
            this.value = "Something";
        }

        @Override
        public String toString() {
            return "Bean{" +
                    "count=" + count +
                    ", value='" + value + '\'' +
                    '}';
        }
    }
}
