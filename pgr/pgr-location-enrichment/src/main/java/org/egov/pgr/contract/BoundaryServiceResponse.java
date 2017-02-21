package org.egov.pgr.contract;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class BoundaryServiceResponse {
    @JsonProperty("Boundary")
    private List<BoundaryResponse> boundary;
}
