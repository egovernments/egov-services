package org.egov.web.indexer.contract;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

@Getter
public class SevaRequest {
    @JsonProperty("RequestInfo")
    private RequestInfo requestInfo;

    @JsonProperty("ServiceRequest")
    private ServiceRequest serviceRequest;
}