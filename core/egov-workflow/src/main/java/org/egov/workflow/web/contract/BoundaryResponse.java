package org.egov.workflow.web.contract;
import lombok.Getter;

@Getter
public class BoundaryResponse {

	private Long id;
	private String name;
	private BoundaryResponse parent;

}
