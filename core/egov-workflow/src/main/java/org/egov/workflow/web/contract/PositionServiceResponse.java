package org.egov.workflow.web.contract;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;

@Getter
public class PositionServiceResponse {
	@JsonProperty("Positions")
	private List<PositionResponse> positions;
}
