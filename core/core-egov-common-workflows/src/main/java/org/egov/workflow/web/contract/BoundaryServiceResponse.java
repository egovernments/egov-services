package org.egov.workflow.web.contract;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;

@Getter
public class BoundaryServiceResponse {
    @JsonProperty("Boundary")
    private List<BoundaryResponse> boundary;
}
