package org.egov.pgr.employee.enrichment.consumer.contract;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

@Getter
public class SevaRequest {

    @JsonProperty("RequestInfo")
    private RequestInfo requestInfo = null;

    @JsonProperty("ServiceRequest")
    private ServiceRequest serviceRequest = null;
    
}