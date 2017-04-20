package org.egov.eis.web.controller;

import java.util.ArrayList;
import java.util.List;

import org.egov.eis.domain.service.DepartmentService;
import org.egov.eis.persistence.entity.Department;
import org.egov.eis.web.contract.DepartmentResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class DepartmentController {

	private DepartmentService departmentService;

	@Autowired
	public DepartmentController(DepartmentService departmentService) {
		this.departmentService = departmentService;
	}

	@GetMapping(value = "/departments")
	public DepartmentResponse getDepartments(@RequestParam(value = "tenantId", required = true) String tenantId,
			@RequestParam(value = "code", required = false) String departmentCode,
			@RequestParam(value = "id", required = false) Long id) {
		List<Department> allDepartments = new ArrayList<>();
		if (tenantId != null && !tenantId.isEmpty()) {
			allDepartments = departmentService.find(departmentCode, id, tenantId);
		}
		return new DepartmentResponse(allDepartments);
	}

}