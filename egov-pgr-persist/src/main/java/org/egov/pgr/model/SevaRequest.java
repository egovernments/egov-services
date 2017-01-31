package org.egov.pgr.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class SevaRequest {

    @JsonProperty("RequestInfo")
    private RequestInfo requestInfo = null;

    @JsonProperty("ServiceRequest")
    private ServiceRequest serviceRequest = null;

    public RequestInfo getRequestInfo() {
        return requestInfo;
    }

    public ServiceRequest getServiceRequest() {
        return serviceRequest;
    }
}