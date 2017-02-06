package org.egov.workflow.model;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class PositionServiceResponse {

	@JsonProperty("Positions")
	private List<PositionResponse> position;

	public List<PositionResponse> getPosition() {
		return position;
	}

}
