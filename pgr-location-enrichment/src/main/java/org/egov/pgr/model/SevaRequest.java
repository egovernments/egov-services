package org.egov.pgr.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class SevaRequest {
    @JsonProperty("RequestInfo")
    private RequestInfo requestInfo = null;

    @JsonProperty("ServiceRequest")
    private ServiceRequest serviceRequest = null;

}