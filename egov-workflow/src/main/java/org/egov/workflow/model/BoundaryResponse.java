package org.egov.workflow.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;

@Getter
@JsonIgnoreProperties(ignoreUnknown = true)
public class BoundaryResponse {

	private Long id;
	private String name;
	private BoundaryResponse parent;

}
