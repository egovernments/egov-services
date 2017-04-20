package org.egov.workflow.web.contract;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BoundaryResponse {

	private Long id;
	private String name;
	private BoundaryResponse parent;
    private String tenantId;
}
