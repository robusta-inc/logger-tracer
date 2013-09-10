package com.robusta.logger.tracer;

import org.junit.Before;
import org.junit.Test;

import static com.robusta.logger.tracer.LoggerTracerFactory.loggerTracer;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.*;

public class LoggerTracerServletContextListenerTest extends ServletContextListenerTestBase {
    private LoggerTracerServletContextListener listener;

    @Before
    public void setUp() throws Exception {
        super.setUp();
        listener = new LoggerTracerServletContextListener();
    }

    @Test
    public void testLoggerTracerServletContextListener() throws Exception {
        listener.contextInitialized(context);
        assertThat((Boolean) staticFieldValueByName("asyncEnabled", LoggerTracerFactory.class), equalTo(true));
        assertNotNull("LoggerTracerFactory's executor service must be initialized.", staticFieldValueByName("executor", LoggerTracerFactory.class));
        loggerTracer(LoggerTracerServletContextListenerTest.class,
                "testContextInitialized",
                "arg1",
                "arg2",
                1,
                2,
                new Object())
                .lap("Lap 1")
                .stop();
        listener.contextDestroyed(context);
        assertThat((Boolean) staticFieldValueByName("asyncEnabled", LoggerTracerFactory.class), equalTo(false));
        assertNull("LoggerTracerFactory's executor service must be tear down (must be null).", staticFieldValueByName("executor", LoggerTracerFactory.class));
    }

}
