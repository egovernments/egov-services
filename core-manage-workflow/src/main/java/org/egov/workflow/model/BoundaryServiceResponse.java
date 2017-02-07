package org.egov.workflow.model;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class BoundaryServiceResponse {

	@JsonProperty("Boundary")
	private List<BoundaryResponse> boundary;

	public List<BoundaryResponse> getBoundary() {
		return boundary;
	}
}
