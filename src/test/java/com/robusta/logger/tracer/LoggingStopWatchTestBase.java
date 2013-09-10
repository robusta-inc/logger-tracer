package com.robusta.logger.tracer;

import org.apache.commons.lang3.time.StopWatch;
import org.junit.Before;
import org.mockito.Mock;
import org.slf4j.Logger;

import java.util.Random;

import static com.robusta.logger.tracer.LoggingStopWatch.EXECUTION_COMPLETION_TRACE_MSG;
import static com.robusta.logger.tracer.LoggingStopWatch.LAP_COMPLETION_TRACE_MSG;
import static java.util.UUID.randomUUID;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

public class LoggingStopWatchTestBase {
	@Mock protected StopWatch stopWatch;
    @Mock protected Logger logger;
    protected String methodName;
    protected String uuid;

    protected long lapTime = new Random().nextLong();
    protected long executionTime = new Random().nextLong();
    protected static final String LAP_DESCRIPTION = "test";

    @Before
    public void setUp() throws Exception {
        initMocks(this);
        methodName = "LoggingStopWatchTest.aMethod";
        uuid = randomUUID().toString();
        doSpecificSetup();
    }

	protected void doSpecificSetup() throws Exception {
		// default does nothing, child tests can implement to do specific stuff.
	}

    protected void verifyThatExecutionCompletionWasLoggedIntoTheLogger() {
        verify(logger).trace(EXECUTION_COMPLETION_TRACE_MSG, uuid, methodName, executionTime);
    }

    protected void whenIsTraceEnabled_InvokedOnLogger_shouldReturnTrue() {
        when(logger.isTraceEnabled()).thenReturn(true);
    }

    protected void verifyThatLapCompletionWithLapDescriptionWasLoggedIntoTheLogger() {
        verify(logger).trace(LAP_COMPLETION_TRACE_MSG, uuid, LAP_DESCRIPTION, lapTime);
    }
}
