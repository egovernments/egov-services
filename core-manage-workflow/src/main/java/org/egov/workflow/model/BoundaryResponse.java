package org.egov.workflow.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class BoundaryResponse {

	private Long id;

	private String name;

	private BoundaryResponse parent;

	public Long getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public BoundaryResponse getParent() {
		return parent;
	}

}
