package org.egov.pgr.write.contracts.position;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

import java.util.List;

@Getter
public class PositionsResponse {

    @JsonProperty("Positions")
    List<PositionResponse> positions;

}
