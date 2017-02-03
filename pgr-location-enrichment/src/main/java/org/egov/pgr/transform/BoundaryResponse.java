package org.egov.pgr.transform;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class BoundaryResponse {

	private Long id;

	private String name;

	public Long getId() {
		return id;
	}

	public String getName() {
		return name;
	}

}
