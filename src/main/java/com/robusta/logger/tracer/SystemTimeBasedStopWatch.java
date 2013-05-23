package com.robusta.logger.tracer;

/**
 * A nano time based stop watch.
 * This class is not thread-safe
 */
public class SystemTimeBasedStopWatch implements StopWatch<SystemTimeBasedStopWatch> {
    private long startTime;
    private long stopTime;
    private boolean started;
    @Override
    public SystemTimeBasedStopWatch start() {
        startTime = snapshotSystemTime();
        started = true;
        return this;
    }

    @Override
    public long stopAndGet() {
        return stop().lapTime();
    }

    private long lapTime() {
        verifyWatchHasBeenStated();
        return stopTime - startTime;
    }

    private void verifyWatchHasBeenStated() {
        Assert.checkState(started, "The stop watch has not yet been started.");
    }

    @Override
    public SystemTimeBasedStopWatch stop() {
        verifyWatchHasBeenStated();
        stopTime = snapshotSystemTime();
        return this;
    }

    protected long snapshotSystemTime() {
        return System.currentTimeMillis();
    }
}
