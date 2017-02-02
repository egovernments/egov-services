package org.egov.pgr.transform;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class CrossHierarchyResponse {

	private BoundaryResponse parent;

	private BoundaryResponse child;

	public BoundaryResponse getParent() {
		return parent;
	}

	public BoundaryResponse getChild() {
		return child;
	}

}
