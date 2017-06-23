package org.egov.pgr.location.contract;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;

import java.util.List;

@AllArgsConstructor
public class BoundaryServiceResponse {
    @JsonProperty("Boundary")
    private List<BoundaryResponse> boundary;

    public BoundaryResponse getBoundary() {
        return boundary.get(0);
    }
}
