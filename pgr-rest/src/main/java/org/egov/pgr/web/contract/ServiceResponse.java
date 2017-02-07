package org.egov.pgr.web.contract;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class ServiceResponse {
    @JsonProperty("response_info")
    private ResponseInfo responseInfo;

    @JsonProperty("service_requests")
    private List<ServiceRequest> serviceRequests;
}