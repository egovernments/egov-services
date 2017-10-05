package org.egov.workflow.web.contract;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BoundaryResponse {

	private Long id;
	private String name;
	private BoundaryResponse parent;

}
