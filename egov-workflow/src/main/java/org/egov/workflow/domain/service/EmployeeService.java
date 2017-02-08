package org.egov.workflow.domain.service;

import java.util.List;

import org.egov.workflow.domain.model.EmployeeResponse;

public interface EmployeeService {

	List<EmployeeResponse> getByRoleName(String roleName);

}
