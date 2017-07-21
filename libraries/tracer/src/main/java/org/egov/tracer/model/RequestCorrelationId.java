package org.egov.tracer.model;

import java.util.HashMap;

public class RequestCorrelationId {

    private static final String CORRELATION_ID_FIELD_NAME = "correlationId";
    private static final String REQUEST_INFO_FIELD_NAME_IN_JAVA_CLASS_CASE = "RequestInfo";
    private static final String REQUEST_INFO_IN_CAMEL_CASE = "requestInfo";
    private HashMap<String, Object> requestMap;

    public RequestCorrelationId(HashMap<String, Object> requestMap) {
        this.requestMap = requestMap;
    }

    public String get() {
        if(this.requestMap == null) {
            return null;
        }
        final HashMap<String, Object> requestInfo = getRequestInfo();
        if (requestInfo != null) {
            return (String) requestInfo.get(CORRELATION_ID_FIELD_NAME);
        }
        return null;
    }

    public HashMap<String, Object> update(String correlationId) {
        if(this.requestMap == null) {
            return null;
        }
        final HashMap<String, Object> requestInfo = getRequestInfo();
        if (requestInfo != null) {
            requestInfo.put(CORRELATION_ID_FIELD_NAME, correlationId);
        }
        return requestMap;
    }

    @SuppressWarnings("unchecked")
    private HashMap<String, Object> getRequestInfo() {
        final Object requestInfo1 = requestMap.get(REQUEST_INFO_FIELD_NAME_IN_JAVA_CLASS_CASE);
        if (requestInfo1 == null) {
            final Object requestInfo2 = requestMap.get(REQUEST_INFO_IN_CAMEL_CASE);
            return (HashMap<String, Object> )requestInfo2;
        }
        return (HashMap<String, Object>) requestInfo1;
    }
}
