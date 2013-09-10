package com.robusta.logger.tracer;

import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.RejectedExecutionException;

import static org.junit.Assert.assertNotNull;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class AsyncLoggingReStopWatchTest extends LoggingStopWatchTestBase {
    private AsyncLoggingReStopWatch loggerTracer;
    @Mock private ExecutorService executor;

    @Override
    protected void doSpecificSetup() throws Exception {
        loggerTracer = new AsyncLoggingReStopWatch(stopWatch, logger, methodName, uuid, executor);
    }

    @Test
    public void testLogLapTime() throws Exception {
        whenIsTraceEnabled_InvokedOnLogger_shouldReturnTrue();
        loggerTracer.logLapTime(lapTime, LAP_DESCRIPTION, logger);
        captureCallableSubmittedToExecutorAndCall();
        verifyThatLapCompletionWithLapDescriptionWasLoggedIntoTheLogger();
    }

    @SuppressWarnings("unchecked")
    private void captureCallableSubmittedToExecutorAndCall() throws Exception {
        ArgumentCaptor<Callable> callableCaptor = ArgumentCaptor.forClass(Callable.class);
        verify(executor).submit(callableCaptor.capture());
        Callable captured = callableCaptor.getValue();
        assertNotNull("A null callable was sent into the executor", captured);
        captured.call();
    }

    @Test
    public void testLogExecutionTime() throws Exception {
        whenIsTraceEnabled_InvokedOnLogger_shouldReturnTrue();
        loggerTracer.logExecutionTime(executionTime, logger);
        captureCallableSubmittedToExecutorAndCall();
        verifyThatExecutionCompletionWasLoggedIntoTheLogger();
    }

    @SuppressWarnings("unchecked")
    @Test
    public void testLogExecutionTime_whenCallableSubmissionThrowsRejectedExecutionException() throws Exception {
        whenIsTraceEnabled_InvokedOnLogger_shouldReturnTrue();
        when(executor.submit(any(Callable.class))).thenThrow(new RejectedExecutionException("Simulation of Callable submission rejection"));
        loggerTracer.logExecutionTime(executionTime, logger);
    }
}
