package org.egov.workflow.web.contract;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import org.egov.common.contract.response.ResponseInfo;

@Getter
public class EmployeeRes {

	@JsonProperty("ResponseInfo")
	private ResponseInfo responseInfo = null;

	@JsonProperty("Employee")
	private List<Employee> employees = new ArrayList<Employee>();

	public EmployeeRes responseInfo(ResponseInfo responseInfo) {
		this.responseInfo = responseInfo;
		return this;
	}

	public EmployeeRes employees(List<Employee> employees) {
		this.employees = employees;
		return this;
	}

	public EmployeeRes addEmployeeItem(Employee employeeItem) {
		this.employees.add(employeeItem);
		return this;
	}

}
