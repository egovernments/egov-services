package org.egov.workflow.domain.service;

import java.util.List;

import org.egov.workflow.domain.model.Employee;
import org.egov.workflow.domain.model.EmployeeRes;
import org.joda.time.LocalDate;

public interface EmployeeService {

	List<Employee> getByRoleName(String roleName);

	EmployeeRes getEmployeeForPosition(Long posId, LocalDate asOnDate);

	EmployeeRes getEmployeeForUserId(Long userId);
}
