package org.egov.eis.web.controller;

import org.egov.eis.domain.service.DepartmentService;
import org.egov.eis.persistence.entity.Department;
import org.egov.eis.web.contract.DepartmentResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class DepartmentController {

    private DepartmentService departmentService;

    @Autowired
    public DepartmentController(DepartmentService departmentService) {
        this.departmentService = departmentService;
    }

    @GetMapping(value = "/departments")
    public DepartmentResponse getDepartments(@RequestParam(value = "code", required = false) String departmentCode) {
        final List<Department> allDepartments = departmentService.find(departmentCode);
        return new DepartmentResponse(allDepartments);
    }

}