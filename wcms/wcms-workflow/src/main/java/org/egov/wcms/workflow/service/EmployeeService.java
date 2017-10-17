package org.egov.wcms.workflow.service;

import java.util.List;

import org.egov.wcms.workflow.model.contract.WorkFlowRequestInfo;
import org.egov.wcms.workflow.repository.EmployeeRepository;
import org.egov.wcms.workflow.repository.contract.Employee;
import org.egov.wcms.workflow.repository.contract.EmployeeResponse;
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
			final String tenantId,final WorkFlowRequestInfo requestInfo) {

	    EmployeeResponse empResp=employeeRepository.getEmployeeByDeptIdAndDesgId(departmentId, designationId, tenantId,requestInfo);
		return empResp.getEmployees();
	}

}
