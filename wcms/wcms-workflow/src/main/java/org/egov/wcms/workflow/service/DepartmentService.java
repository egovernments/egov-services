package org.egov.wcms.workflow.service;

import java.util.List;

import org.egov.common.contract.request.RequestInfo;
import org.egov.wcms.workflow.model.contract.WorkFlowRequestInfo;
import org.egov.wcms.workflow.repository.DepartmentRepository;
import org.egov.wcms.workflow.repository.contract.Department;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DepartmentService {

	private DepartmentRepository departmentRepository;

	@Autowired
	public DepartmentService(DepartmentRepository departmentRepository) {
		this.departmentRepository = departmentRepository;
	}

	public List<Department> getByName(final String departmentName, final String tenantId,final WorkFlowRequestInfo requestInfo) {

		return departmentRepository.getDepartmentByName(departmentName, tenantId,requestInfo).getDepartment();
	}

}
