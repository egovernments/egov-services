package org.egov.workflow.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

import java.util.List;

@Getter
@JsonIgnoreProperties(ignoreUnknown = true)
public class PositionsResponse {

    @JsonProperty("Positions")
    private List<PositionResponse> positions;

    @JsonProperty("PositionHierarchies")
    private List<PositionHierarchyResponse> positionHierarchies;

}
