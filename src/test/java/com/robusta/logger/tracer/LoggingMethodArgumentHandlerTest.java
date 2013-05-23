package com.robusta.logger.tracer;

import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

public class LoggingMethodArgumentHandlerTest {
    private LoggingMethodArgumentHandler argumentHandler;
    private Logger logger;
    private String methodName;
    private List<String> anArrayList;
    private Object aNull;
    private Bean aBean;

    @Before
    public void setUp() throws Exception {
        methodName = "LoggingMethodArgumentHandlerTest.someMethod";
        logger = LoggerFactory.getLogger("LoggingMethodArgumentHandlerTest");
        argumentHandler = new LoggingMethodArgumentHandler(logger, methodName);
        anArrayList = new ArrayList<String>();
        aBean = new Bean();
    }

    @Test
    public void testDoWithMethodArguments_whenNoArgumentsSpecified() throws Exception {
        argumentHandler.doWithMethodArguments(null);
    }

    @Test
    public void testDoWithMethodArguments_whenEmptyArrayArgumentsSpecified() throws Exception {
        argumentHandler.doWithMethodArguments(new Object[]{});
    }

    @Test
    public void testDoWithMethodArguments_whenSomeArguments() throws Exception {
        argumentHandler.doWithMethodArguments(1L, "AString", anArrayList, aNull, aBean);
    }

    private class Bean {
        private final int count;
        private final String value;


        private Bean() {
            this.count = 10;
            this.value = "Something";
        }

        @Override
        public String toString() {
            return "Bean{" +
                    "count=" + count +
                    ", value='" + value + '\'' +
                    '}';
        }
    }
}
