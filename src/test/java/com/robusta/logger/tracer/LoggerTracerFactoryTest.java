package com.robusta.logger.tracer;

import org.junit.Test;

import static com.robusta.logger.tracer.LoggerTracerFactory.argumentLoggerTracer;
import static com.robusta.logger.tracer.LoggerTracerFactory.loggerTracer;

public class LoggerTracerFactoryTest {
    @Test
    public void testSimpleLoggerTracer() throws Exception {
        final LoggerTracer loggerTracer = loggerTracer("LoggerTracerFactoryTest.testSimpleLoggerTracer");
        try {
            simulateTheMethodDoingSomething();
        } finally {
            loggerTracer.stop();
        }
    }

    private void simulateTheMethodDoingSomething() throws InterruptedException {
        Thread.sleep(1000);
    }

    @Test
    public void testComputingLoggerTracer() throws Exception {
        final LoggerTracer loggerTracer = loggerTracer();
        try {
            simulateTheMethodDoingSomething();
        } finally {
            loggerTracer.stop();
        }
    }

    @Test
    public void testArgumentHandlingLoggerTracer_withMethodName() throws Exception {
        final ArgumentLoggerTracer loggerTracer = argumentLoggerTracer("LoggerTracerFactoryTest.testArgumentHandlingLoggerTracer_withMethodName");
        loggerTracer.logArguments("One", "Two", "Three", 1L, 2, null, new Object());
        try {
            simulateTheMethodDoingSomething();
        } finally {
            loggerTracer.stop();
        }
    }

    @Test
    public void testArgumentHandlingLoggerTracer() throws Exception {
        final ArgumentLoggerTracer loggerTracer = argumentLoggerTracer().logArguments("One", "Two", "Three", 1L, 2, null, new Object());
        try {
            simulateTheMethodDoingSomething();
        } finally {
            loggerTracer.stop();
        }
    }
}
