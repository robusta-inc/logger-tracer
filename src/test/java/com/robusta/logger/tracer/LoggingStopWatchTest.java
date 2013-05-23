package com.robusta.logger.tracer;

import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LoggingStopWatchTest {
    private LoggingStopWatch loggingStopWatch;
    private SystemTimeBasedStopWatch stopWatch;
    private Logger logger;

    @Before
    public void setUp() throws Exception {
        stopWatch = new SystemTimeBasedStopWatch();
        logger = LoggerFactory.getLogger("LoggingStopWatchTest");
        loggingStopWatch = new LoggingStopWatch(stopWatch, logger);
    }

    @Test
    public void testStop() throws Exception {
        simulateActivity();
        loggingStopWatch.stop();
    }

    private void simulateActivity() throws InterruptedException {
        Thread.sleep(2000);
    }
}
