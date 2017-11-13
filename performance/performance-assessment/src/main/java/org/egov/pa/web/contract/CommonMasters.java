package org.egov.pa.web.contract;

import java.util.List;

import org.egov.pa.model.Department;

import com.fasterxml.jackson.annotation.JsonProperty;

public class CommonMasters {
	
	@JsonProperty("Department")
	private List<Department> departments;

	public List<Department> getDepartments() {
		return departments;
	}

	public void setDepartments(List<Department> departments) {
		this.departments = departments;
	} 
	
	

}
