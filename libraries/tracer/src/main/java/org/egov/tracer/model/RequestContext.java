package org.egov.tracer.model;

import org.slf4j.MDC;

public class RequestContext {

    public static String CORRELATION_ID = "CORRELATION_ID";

    private static final ThreadLocal<String> id = new ThreadLocal<>();

    public static String getId() { return id.get(); }

    public static void clear() {
        id.set(null);
        MDC.remove(CORRELATION_ID);
    }

    public static void setId(String correlationId) {
        id.set(correlationId);
        MDC.put(CORRELATION_ID, correlationId);
    }
}
