package org.egov.workflow.model;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

@Getter
@JsonIgnoreProperties(ignoreUnknown = true)
public class PositionHierarchyServiceResponse {

	@JsonProperty("positionHierarchy")
	private List<PositionHierarchyResponse> positionHierarchy;

}
