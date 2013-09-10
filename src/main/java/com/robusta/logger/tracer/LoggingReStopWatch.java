package com.robusta.logger.tracer;

import org.apache.commons.lang3.time.StopWatch;
import org.slf4j.Logger;

class LoggingReStopWatch extends LoggingStopWatch {

	public LoggingReStopWatch(StopWatch stopWatch, Logger logger,
			String methodName, String uuid) {
		super(stopWatch, logger, methodName, uuid);
	}
	
    @Override
    public LoggingReStopWatch stop() {
    	try {
			super.stop();
		} catch (IllegalStateException e) {
			getLogger().warn("loggerTracer.stop() called more then once.");
            getLogger().warn("The following warning is benign (does not change the control flow).");
            getLogger().warn("It is logged for detecting and rectifying code anomaly :).", e);
		}
        return this;
    }
}
