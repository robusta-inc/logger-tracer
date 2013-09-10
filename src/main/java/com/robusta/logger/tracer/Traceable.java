package com.robusta.logger.tracer;

/**
 * Fine grained and precise tracing control over
 * content of the object being traced.
 *
 * <p>Instead of using the toString() api on an
 * object such as {@link javax.servlet.http.HttpServletRequest}
 * wrap it in an appropriate Traceable.
 * Traceable's output is better formatted with information
 * that is significant to tracing.</p>
 * @see Traceables
 * @author sudhir.ravindramohan
 * @since 1.0
 */
public abstract class Traceable<T> {
    protected final T traced;
    protected final Class<T> tracedClass;

    protected Traceable(Class<T> tracedClass, T traced) {
        this.tracedClass = tracedClass;
        this.traced = traced;
    }

    @Override
    public final String toString() {
        return asString();
    }

    protected abstract String asString();

    protected String nullAsString() {
        return String.format("[null - %s]", tracedClass.getSimpleName());
    }

    protected String objectAsString() {
        return traced == null ? nullAsString(): traced.toString();
    }
}
