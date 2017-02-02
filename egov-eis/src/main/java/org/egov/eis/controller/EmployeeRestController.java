package org.egov.eis.controller;

import java.util.Date;

import org.egov.eis.model.EmployeeRes;
import org.egov.eis.model.Error;
import org.egov.eis.model.ErrorRes;
import org.egov.eis.model.ResponseInfo;
import org.egov.eis.service.EmployeeService;
import org.joda.time.LocalDate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class EmployeeRestController {

	@Autowired
	private EmployeeService employeeService;

	@RequestMapping(value = "/employee", method = RequestMethod.GET)
	@ResponseBody
	public EmployeeRes getEmployees(@RequestParam(value = "tenantId", required = true) String tenantId,
			@RequestParam(value = "code", required = false) String code,
			@RequestParam(value = "assignmentDepartmentCode", required = false) String assignmentDepartmentCode,
			@RequestParam(value = "assignmentDesignationCode", required = false) String assignmentDesignationCode,
			@RequestParam(value = "asOnDate", required = false) LocalDate asOnDate,
			@RequestParam(value = "assignmentIsPrimary", required = false) Boolean assignmentIsPrimary,
			@RequestParam(value = "positionId", required = true) String positionId,
			@RequestParam(value = "roleName", required = false) String roleName,
			@RequestParam(value = "pageSize", required = false, defaultValue = "20") Integer pageSize,
			@RequestParam(value = "pageNumber", required = false, defaultValue = "1") Integer pageNumber)
			throws Exception {

		EmployeeRes response = new EmployeeRes();
		response.setResponseInfo(new ResponseInfo("", "", new Date().toString(), "", "", "Successful response"));
		if (code != null && !code.isEmpty()) {
			response.getEmployee().add(employeeService.getByCode(code));
		} else if (positionId != null && !positionId.isEmpty() && asOnDate != null) {
			response.getEmployee().addAll(employeeService.getByPositionAndAsOnDate(Long.parseLong(positionId),
					asOnDate.toDateTimeAtStartOfDay().toDate()));

		} else if (assignmentDepartmentCode != null && !assignmentDepartmentCode.isEmpty()
				&& assignmentDesignationCode != null && !assignmentDesignationCode.isEmpty()) {
			response.getEmployee().addAll(
					employeeService.getByDepartmentDesignation(assignmentDepartmentCode, assignmentDesignationCode));

		} else if (roleName != null && !roleName.isEmpty()) {
			response.getEmployee().addAll(employeeService.getEmployeesByRoleName(roleName));
		} else {
			throw new Exception();
		}

		return response;
	}

	@ExceptionHandler(Exception.class)
	public ResponseEntity<ErrorRes> handleError(Exception ex) {
		ex.printStackTrace();
		ErrorRes response = new ErrorRes();
		ResponseInfo responseInfo = new ResponseInfo("", "", new Date().toString(), "", "", "Failed to get employee");
		response.setResponseInfo(responseInfo);
		Error error = new Error();
		error.setCode(400);
		error.setDescription("Failed to get employee");
		response.setError(error);
		return new ResponseEntity<ErrorRes>(response, HttpStatus.BAD_REQUEST);
	}

}