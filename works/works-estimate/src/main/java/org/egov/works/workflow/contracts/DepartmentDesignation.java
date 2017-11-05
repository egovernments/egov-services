package org.egov.works.workflow.contracts;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class DepartmentDesignation {

	private Long id;

	@JsonProperty("department")
	private Long departmentId;

	private Designation designation;

}
