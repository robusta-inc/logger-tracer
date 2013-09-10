package com.robusta.logger.tracer;

import org.apache.commons.lang3.time.StopWatch;
import org.junit.Before;
import org.junit.Test;

import static org.mockito.Mockito.when;

public class LoggingReStopWatchTest extends LoggingStopWatchTestBase {
	private LoggingStopWatch loggingReStopWatch;

	@Before
	public void doSpecificSetup() throws Exception {
		loggingReStopWatch = new LoggingReStopWatch(new StopWatch(), logger, methodName, uuid);
	}

	@Test
    public void testLoggingReStopWatch_ReStop_shouldNotRaiseException() {
    	when(logger.isTraceEnabled()).thenReturn(true);
    	loggingReStopWatch.stop().stop();
    }
}
