package com.robusta.logger.tracer;

import org.junit.Before;
import org.junit.Test;
import org.springframework.mock.web.MockHttpServletRequest;

import static com.robusta.logger.tracer.LoggerTracerFactory.loggerTracer;
import static com.robusta.logger.tracer.Traceables.requestParams;
import static com.robusta.logger.tracer.Traceables.sessionAttrs;

/**
 * Not exactly a unit test, but a LoggerTracerFactory
 * showcase client code implemented in an invokable test.
 */
public class LoggerTracerFactoryTest {
    private MockHttpServletRequest request;

    @Before
    public void setUp() throws Exception {
        request = new MockHttpServletRequest();
        request.setParameter("username", "val1");
        request.setParameter("password", "val2");
        request.setParameter("domain", "val2");
        request.getSession().setAttribute("fieldList", "1,2,3,4");
    }

    @Test
    public void testLoggerTracerForAMethodWithArguments() throws Exception {
        invokeAMethodWithArguments("One", "Two", "Three", new String[] {"a", "b", "c"}, new Object(), new LoggerTracerFactoryTest());
    }

    @Test
    public void testLoggerTracerForAMethodWithoutArguments() throws Exception {
        invokeAMethodWithoutArguments();
    }

    private void invokeAMethodWithoutArguments() throws InterruptedException {
        final LoggerTracer tracer = loggerTracer(LoggerTracerFactoryTest.class, "invokeAMethodWithoutArguments");
        try {
            sleepAndClockLapTime(tracer);
        } finally {
            tracer.stop();
        }
    }

    private void invokeAMethodWithArguments(String one, String two, String three, String[] strings, Object o, LoggerTracerFactoryTest loggerTracerFactoryTest) throws InterruptedException {
        final LoggerTracer tracer = loggerTracer(LoggerTracerFactoryTest.class, "invokeAMethodWithArguments", one, two, three, strings, o, loggerTracerFactoryTest);
        try {
            sleepAndClockLapTime(tracer);
        } finally {
            tracer.stop();
        }
    }

    private void sleepAndClockLapTime(LoggerTracer tracer) throws InterruptedException {
        sleepForASecond();
        tracer.lap("Slept for 1 second");
        sleepForASecond();
        tracer.lap("Slept for 2 seconds");
    }

    private void sleepForASecond() throws InterruptedException {
        Thread.sleep(1000);
    }

    @Test
    public void testRequestTracing() throws Exception {
        loggerTracer(LoggerTracerFactoryTest.class, "testRequestTracing", requestParams(request, "username", "domain")).stop();
    }

    @Test
    public void testSessionTracing() throws Exception {
        loggerTracer(LoggerTracerFactoryTest.class, "testSessionTracing", sessionAttrs(request, "fieldList")).stop();
    }

    @Test
    public void testThatLoggerTracer_isCapableOfBeingStoppedMultipleTimes_shouldNotThrowException_shouldStopInternalStopWatchOnlyOnce() {
    	loggerTracer(LoggerTracerFactoryTest.class, "multipleStopping")
                .lap("The exception stack trace printed to console from this test is benign")
                .stop()
                .stop();
    }

    @Test
    public void testAsyncLoggerTracing() throws Exception {
        LoggerTracerFactory.startAsync();
        Object objectThatTakesTimeToString = new Object() {
            @Override
            public String toString() {
                try {
                    Thread.sleep(5000);
                } catch (InterruptedException ignored) {}
                return "ObjectThatTakes5SecondsToString";
            }
        };
        invokeAMethodWithArguments("One", "Two", "Three", new String[]{"a", "b", "c"}, objectThatTakesTimeToString, new LoggerTracerFactoryTest());
        LoggerTracerFactory.shutdownAsync();
    }
}
