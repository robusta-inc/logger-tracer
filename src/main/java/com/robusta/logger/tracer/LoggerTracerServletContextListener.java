package com.robusta.logger.tracer;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

public class LoggerTracerServletContextListener implements ServletContextListener {

    @Override
    public void contextInitialized(ServletContextEvent servletContextEvent) {
        LoggerTracerFactory.startAsync();
    }

    @Override
    public void contextDestroyed(ServletContextEvent servletContextEvent) {
        LoggerTracerFactory.shutdownAsync();
    }
}
