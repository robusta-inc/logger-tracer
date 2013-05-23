package com.robusta.logger.tracer;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertNotNull;

/**
 * @author sudhir.ravindramohan
 * @since 1.0
 */
public class SystemTimeBasedStopWatchTest {
    private SystemTimeBasedStopWatch startable;
    private SystemTimeBasedStopWatch started;

    @Before
    public void setUp() throws Exception {
        startable = new SystemTimeBasedStopWatch();
        started = new SystemTimeBasedStopWatch().start();
    }

    @Test
    public void testStart_shouldStartTheWatchAndReturnInstance() throws Exception {
        assertNotNull(startable.start());
    }

    @Test
    public void testStop_shouldCaptureTheStopTimeAndReturnInstance() throws Exception {
        assertNotNull(started.stop());
    }

    @Test(expected = IllegalStateException.class)
    public void testStopAndGet_whenInvokedWithoutTheState_shouldThrowAnIllegalStateException() throws Exception {
        startable.stopAndGet();
    }

    @Test
    public void testStopAndGet() throws Exception {
        assertNotNull(started.stopAndGet());
    }
}
