package org.egov.workflow.model;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class PositionsResponse {

	@JsonProperty("Positions")
	private List<PositionResponse> positions;

	@JsonProperty("PositionHierarchies")
	private List<PositionHierarchyResponse> positionHierarchies;

	public List<PositionResponse> getPositions() {
		return positions;
	}

	public void setPositions(List<PositionResponse> positions) {
		this.positions = positions;
	}

	public List<PositionHierarchyResponse> getPositionHierarchies() {
		return positionHierarchies;
	}

	public void setPositionHierarchies(List<PositionHierarchyResponse> positionHierarchies) {
		this.positionHierarchies = positionHierarchies;
	}

}
