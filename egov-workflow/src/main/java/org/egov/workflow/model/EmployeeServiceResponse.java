package org.egov.workflow.model;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class EmployeeServiceResponse {

	@JsonProperty("Employees")
	private List<EmployeeResponse> employee;

	public List<EmployeeResponse> getEmployee() {
		return employee;
	}

}
