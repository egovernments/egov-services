package org.egov.workflow.model;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class EmployeesResponse {

	@JsonProperty("Employees")
	private List<EmployeeResponse> employees;

	public List<EmployeeResponse> getEmployees() {
		return employees;
	}

	public void setEmployees(List<EmployeeResponse> employees) {
		this.employees = employees;
	}
	
	
}
