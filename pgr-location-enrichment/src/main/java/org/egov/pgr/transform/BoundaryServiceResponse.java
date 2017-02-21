package org.egov.pgr.transform;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
@Getter
@AllArgsConstructor
public class BoundaryServiceResponse {

    @JsonProperty("Boundary")
    private List<BoundaryResponse> boundary;

}
