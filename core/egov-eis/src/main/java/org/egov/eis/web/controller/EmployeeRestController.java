package org.egov.eis.web.controller;

import java.util.Date;

import org.egov.eis.web.contract.EmployeeRes;
import org.egov.eis.web.contract.Error;
import org.egov.eis.web.contract.ErrorRes;
import org.egov.eis.web.contract.ResponseInfo;
import org.egov.eis.domain.service.EmployeeService;
import org.joda.time.LocalDate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
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
			@RequestParam(value = "asOnDate", required = false) LocalDate asOnDate,
			@RequestParam(value = "assignmentIsPrimary", required = false) Boolean assignmentIsPrimary,
			@RequestParam(value = "positionId", required = false) String positionId,
			@RequestParam(value = "roleName", required = false) String roleName,
			@RequestParam(value = "userId", required = false) Long userId,
			@RequestParam(value = "pageSize", required = false, defaultValue = "20") Integer pageSize,
			@RequestParam(value = "pageNumber", required = false, defaultValue = "1") Integer pageNumber)
			throws Exception {

		EmployeeRes response = new EmployeeRes();
		response.setResponseInfo(new ResponseInfo("", "", new Date().toString(), "", "", "Successful response"));
		if (code != null && !code.isEmpty()) {
			response.getEmployees().add(employeeService.getByCode(code));
		} else if (positionId != null && !positionId.isEmpty() && asOnDate != null) {
			response.getEmployees().addAll(employeeService.getByPositionAndAsOnDate(Long.parseLong(positionId),
					asOnDate.toDateTimeAtStartOfDay().toDate()));

		} else if (assignmentDepartmentCode != null && !assignmentDepartmentCode.isEmpty()
				&& assignmentDesignationCode != null && !assignmentDesignationCode.isEmpty()) {
			response.getEmployees().addAll(
					employeeService.getByDepartmentDesignation(assignmentDepartmentCode, assignmentDesignationCode));

		} else if (roleName != null && !roleName.isEmpty()) {
			response.getEmployees().addAll(employeeService.getEmployeesByRoleName(roleName));
		} else if (userId != null && userId > 0) {
			response.getEmployees().add(employeeService.getEmployeeById(userId));
		} else {
			throw new Exception();
		}

		return response;
	}

//	@ExceptionHandler(Exception.class)
//	public ResponseEntity<ErrorRes> handleError(Exception ex) {
//		ex.printStackTrace();
//		ErrorRes response = new ErrorRes();
//		ResponseInfo responseInfo = new ResponseInfo("", "", new Date().toString(), "", "", "Failed to get employee");
//		response.setResponseInfo(responseInfo);
//		Error error = new Error();
//		error.setCode(400);
//		error.setDescription("Failed to get employee");
//		response.setError(error);
//		return new ResponseEntity<ErrorRes>(response, HttpStatus.BAD_REQUEST);
//	}

}