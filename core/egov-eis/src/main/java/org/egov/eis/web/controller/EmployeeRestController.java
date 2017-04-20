package org.egov.eis.web.controller;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.egov.common.contract.response.ResponseInfo;
import org.egov.eis.domain.service.EmployeeService;
import org.egov.eis.web.contract.EmployeeRes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class EmployeeRestController {

	@Autowired
	private EmployeeService employeeService;

	@GetMapping(value = "/employee")
	@ResponseBody
	public EmployeeRes getEmployees(@RequestParam(value = "tenantId", required = true) String tenantId,
			@RequestParam(value = "code", required = false) String code,
			@RequestParam(value = "assignmentDepartmentCode", required = false) String assignmentDepartmentCode,
			@RequestParam(value = "assignmentDesignationCode", required = false) String assignmentDesignationCode,
			@RequestParam(value = "asOnDate", required = false) String asOnDate,
			@RequestParam(value = "assignmentIsPrimary", required = false) Boolean assignmentIsPrimary,
			@RequestParam(value = "positionId", required = false) String positionId,
			@RequestParam(value = "roleName", required = false) String roleName,
			@RequestParam(value = "userId", required = false) Long userId,
			@RequestParam(value = "pageSize", required = false, defaultValue = "20") Integer pageSize,
			@RequestParam(value = "pageNumber", required = false, defaultValue = "1") Integer pageNumber)
			throws Exception {

		EmployeeRes response = new EmployeeRes();
        response.setResponseInfo(new ResponseInfo());
		if (tenantId != null && !tenantId.isEmpty()) {
			if (code != null && !code.isEmpty()) {
				response.getEmployees().add(employeeService.getByCode(code,tenantId));
			} else if (positionId != null && !positionId.isEmpty() && asOnDate != null) {
				response.getEmployees().addAll(employeeService.getByPositionAndAsOnDate(Long.parseLong(positionId),
						new SimpleDateFormat("yyyy-mm-dd").parse(asOnDate),tenantId));
			} else if (assignmentDepartmentCode != null && !assignmentDepartmentCode.isEmpty()
					&& assignmentDesignationCode != null && !assignmentDesignationCode.isEmpty()) {
				response.getEmployees().addAll(employeeService.getByDepartmentDesignation(assignmentDepartmentCode,
						assignmentDesignationCode,tenantId));

			} else if (roleName != null && !roleName.isEmpty()) {
				response.getEmployees().addAll(employeeService.getEmployeesByRoleName(roleName,tenantId));
			} else if (userId != null && userId > 0) {
				response.getEmployees().add(employeeService.getEmployeeById(userId,tenantId));
			} else {
				throw new Exception();
			}
		}

		return response;
	}

}