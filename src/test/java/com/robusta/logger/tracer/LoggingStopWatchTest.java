package com.robusta.logger.tracer;

import org.apache.commons.lang3.time.StopWatch;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LoggingStopWatchTest {
    private LoggingStopWatch loggingStopWatch;
    private StopWatch stopWatch;
    private Logger logger;
    private String methodName;

    @Before
    public void setUp() throws Exception {
        stopWatch = new StopWatch();
        logger = LoggerFactory.getLogger("LoggingStopWatchTest");
        methodName = "LoggingStopWatchTest.aMethod";
        loggingStopWatch = new LoggingStopWatch(stopWatch, logger, methodName);
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
