package com.robusta.logger.tracer;

import org.junit.Before;
import org.mockito.Mock;

import javax.servlet.ServletContextEvent;
import java.lang.reflect.Field;

import static org.mockito.MockitoAnnotations.initMocks;
import static org.springframework.util.ReflectionUtils.*;

public class ServletContextListenerTestBase {
	
    @Mock protected ServletContextEvent context;

    @Before
    public void setUp() throws Exception {
        initMocks(this);
    }

    protected Object staticFieldValueByName(String fieldName, Class<?> clasz ) {
        Field field = findField(clasz, fieldName);
        makeAccessible(field);
        return getField(field, null);
    }

    protected <T, U> U fieldValueByName(String fieldName, Class<T> clasz, T instance, Class<U> fieldClass) {
        Field field = findField(clasz, fieldName);
        makeAccessible(field);
        return fieldClass.cast(getField(field, instance));
    }
}
