package org.egov.works.workflow.service;

import java.util.List;

import org.egov.works.estimate.web.contract.RequestInfo;
import org.egov.works.workflow.contracts.Employee;
import org.egov.works.workflow.repository.EmployeeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class EmployeeService {

	private EmployeeRepository employeeRepository;

	@Autowired
	public EmployeeService(EmployeeRepository employeeRepository) {
		this.employeeRepository = employeeRepository;
	}

	public List<Employee> getByDeptIdAndDesgId(final String departmentId, final String designationId,
			final String tenantId, final RequestInfo requestInfo) {

		return employeeRepository.getEmployeeByDeptIdAndDesgId(departmentId, designationId, tenantId, requestInfo)
				.getEmployees();
	}

}
