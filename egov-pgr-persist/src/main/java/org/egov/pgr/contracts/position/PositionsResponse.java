package org.egov.pgr.contracts.position;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class PositionsResponse {

    @JsonProperty("Position")
    List<PositionResponse> positions;

    public List<PositionResponse> getPositions() {
        return positions;
    }
}
