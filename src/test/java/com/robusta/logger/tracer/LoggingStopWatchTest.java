package com.robusta.logger.tracer;

import org.apache.commons.lang3.time.StopWatch;
import org.junit.Test;

import static org.mockito.Mockito.*;

public class LoggingStopWatchTest extends LoggingStopWatchTestBase {
    private LoggingStopWatch loggingStopWatch;
    private LoggingStopWatch loggingWStopWatch;

    @Override
    protected void doSpecificSetup() throws Exception {
        loggingStopWatch = new LoggingStopWatch(stopWatch, logger, methodName, uuid);        
        loggingWStopWatch = new LoggingStopWatch(new StopWatch(), logger, methodName, uuid);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInitializationWithoutAStopWatch_shouldRaiseException() throws Exception {
        new LoggingStopWatch(null, logger, methodName, uuid);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInitializationWithoutALogger_shouldRaiseException() throws Exception {
        new LoggingStopWatch(stopWatch, null, methodName, uuid);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInitializationWithoutAMethodName_shouldRaiseException() throws Exception {
        new LoggingStopWatch(stopWatch, logger, null, uuid);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInitializationWithAnEmptyMethodName_shouldRaiseException() throws Exception {
        new LoggingStopWatch(stopWatch, logger, "", uuid);
    }

    @Test
    public void testStop_whenTraceIsNotEnabled_shouldBeANoOp() throws Exception {
        loggingStopWatch.stop();
        verifyThatIsTraceEnabled_invokedOnTheLogger();
        verifyNoMoreInteractions(logger, stopWatch);
    }

    @Test
    public void testLap_whenTraceIsNotEnabled_shouldBeANoOp() throws Exception {
        loggingStopWatch.lap("test");
        verifyThatIsTraceEnabled_invokedOnTheLogger();
        verifyNoMoreInteractions(logger, stopWatch);
    }

    @Test
    public void testLap_stopWatchStarted_notStopped_gotTime_loggedUsingLogger() throws Exception {
        whenIsTraceEnabled_InvokedOnLogger_shouldReturnTrue();
        whenGetTime_invokedOnStopWatch_shouldReturn(lapTime);
        new LoggingStopWatch(stopWatch, logger, methodName, uuid).lap("test");
        verifyThatIsTraceEnabled_invokedOnTheLogger();
        verifyThatStopWatchWasStarted();
        verifyThatStopWatchWasNotStoppedToAllowFurtherLapCalls();
        verifyThatTimingInfoWasObtainedFromStopWatch_invocationOfGetTime();
        verifyThatLapCompletionWithLapDescriptionWasLoggedIntoTheLogger();
        verifyNoMoreInteractions(logger, stopWatch);
    }

    private void verifyThatStopWatchWasNotStoppedToAllowFurtherLapCalls() {
        verify(stopWatch, never()).stop();
    }

    private void verifyThatStopWatchWasStarted() {
        verify(stopWatch).start();
    }

    private void whenGetTime_invokedOnStopWatch_shouldReturn(long time) {
        when(stopWatch.getTime()).thenReturn(time);
    }

    @Test
    public void testStop_stopWatch_started_stopped_gotTime_loggedExectionTime_usingLogger() throws Exception {
        whenIsTraceEnabled_InvokedOnLogger_shouldReturnTrue();
        whenGetTime_invokedOnStopWatch_shouldReturn(executionTime);
        new LoggingStopWatch(stopWatch, logger, methodName, uuid).stop();
        verifyThatIsTraceEnabled_invokedOnTheLogger();
        verifyThatStopWatchWasStarted();
        verifyThatStopWatchWasStoppedToPreventFurtherLapCalls();
        verifyThatTimingInfoWasObtainedFromStopWatch_invocationOfGetTime();
        verifyThatExecutionCompletionWasLoggedIntoTheLogger();
        verifyNoMoreInteractions(logger, stopWatch);
    }

    @Test(expected = IllegalStateException.class)
    public void testLoggingStopWatch_ReStop_shouldRaiseException() {
    	when(logger.isTraceEnabled()).thenReturn(true);
    	loggingWStopWatch.stop();
    	loggingWStopWatch.stop();
    }  
    
    private void verifyThatTimingInfoWasObtainedFromStopWatch_invocationOfGetTime() {
        verify(stopWatch).getTime();
    }

    private void verifyThatStopWatchWasStoppedToPreventFurtherLapCalls() {
        verify(stopWatch).stop();
    }

    private void verifyThatIsTraceEnabled_invokedOnTheLogger() {
        verify(logger, atLeastOnce()).isTraceEnabled();
    }

    @SuppressWarnings("FinalizeCalledExplicitly")
    @Test
    public void testThatWhenLoggerTracerIsNotStopped_warningMessageIsPrintedOnFinally() throws Throwable {
    	whenIsTraceEnabled_InvokedOnLogger_shouldReturnTrue();
    	loggingStopWatch.finalize();
    	verify(logger).warn(LoggingStopWatch.STOP_NOT_CALLED_ON_TRACER, uuid);
    }
    
    @SuppressWarnings("FinalizeCalledExplicitly")
    @Test
    public void testThatWhenLoggerTracerIsStopped_jvmCallsFinalizeOnGC_noWarningMessageIsPrintedOnFinally() throws Throwable {
    	whenIsTraceEnabled_InvokedOnLogger_shouldReturnTrue();
    	whenGetTime_invokedOnStopWatch_shouldReturn(executionTime);
    	loggingStopWatch.stop().finalize();
    	verifyThatIsTraceEnabled_invokedOnTheLogger();
    	verifyThatExecutionCompletionWasLoggedIntoTheLogger();    	
    	verifyNoMoreInteractions(logger);
    }
}
