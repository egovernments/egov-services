package org.egov.workflow.service;

import java.util.List;

import org.egov.workflow.model.EmployeeResponse;

public interface EmployeeService {

	List<EmployeeResponse> getByRoleName(String roleName);

}
