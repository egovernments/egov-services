package org.egov.pgr.transform;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class CrossHierarchyServiceResponse {

	@JsonProperty("CrossHierarchy")
	private List<CrossHierarchyResponse> crossHierarchys;

	public List<CrossHierarchyResponse> getCrossHierarchys() {
		return crossHierarchys;
	}

}
