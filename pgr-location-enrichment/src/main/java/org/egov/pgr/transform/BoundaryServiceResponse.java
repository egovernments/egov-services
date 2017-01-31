package org.egov.pgr.transform;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class BoundaryServiceResponse {

    @JsonProperty("Boundary")
    private List<BoundaryResponse> boundary;

    public List<BoundaryResponse> getBoundary() {
        return boundary;
    }
}
