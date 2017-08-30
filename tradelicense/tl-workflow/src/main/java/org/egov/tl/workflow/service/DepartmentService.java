package org.egov.tl.workflow.service;

import java.util.List;

import org.egov.tl.commons.web.contract.RequestInfo;
import org.egov.tl.workflow.repository.DepartmentRepository;
import org.egov.tl.workflow.repository.contract.Department;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DepartmentService {

	private DepartmentRepository departmentRepository;

	@Autowired
	public DepartmentService(DepartmentRepository departmentRepository) {
		this.departmentRepository = departmentRepository;
	}

	public List<Department> getByName(final String departmentName, final String tenantId,final RequestInfo requestInfo) {

		return departmentRepository.getDepartmentByName(departmentName, tenantId,requestInfo).getDepartment();
	}

}
