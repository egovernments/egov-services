package org.egov.workflow.web.contract;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Position {

	private Long id;

	private String name;

	private DepartmentDesignation deptdesig;

	private Boolean isPostOutsourced;

	private Boolean active;

}
