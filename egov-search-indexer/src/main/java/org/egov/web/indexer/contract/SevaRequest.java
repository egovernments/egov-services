package org.egov.web.indexer.contract;

import com.fasterxml.jackson.annotation.JsonProperty;

public class SevaRequest {
    @JsonProperty("RequestInfo")
    private RequestInfo requestInfo = null;

    @JsonProperty("ServiceRequest")
    private ServiceRequest serviceRequest = null;

    public ServiceRequest getServiceRequest() {
        return serviceRequest;
    }

    public RequestInfo getRequestInfo() {

        return requestInfo;
    }
}