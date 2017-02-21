package org.egov.workflow.domain.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

import java.util.List;

@Getter
@JsonIgnoreProperties(ignoreUnknown = true)
public class BoundaryServiceResponse {
    @JsonProperty("Boundary")
    private List<BoundaryResponse> boundary;
}
