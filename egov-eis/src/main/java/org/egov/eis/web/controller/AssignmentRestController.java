package org.egov.eis.web.controller;

import java.util.Date;
import java.util.List;

import org.egov.eis.web.contract.EmployeeRes;
import org.egov.eis.web.contract.Error;
import org.egov.eis.web.contract.ErrorRes;
import org.egov.eis.web.contract.ResponseInfo;
import org.egov.eis.domain.service.AssignmentService;
import org.joda.time.LocalDate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AssignmentRestController {

	@Autowired
	private AssignmentService assignmentService;

	@RequestMapping(value = "/employee/{code}/assignments", method = RequestMethod.GET)
	@ResponseBody
	public EmployeeRes getAssignments(@PathVariable("code") String code,
			@RequestParam(value = "tenantId", required = true) String tenantId,
			@RequestParam(value = "assignmentId", required = false) Long assignmentId,
			@RequestParam(value = "pageSize", required = false, defaultValue = "20") Integer pageSize,
			@RequestParam(value = "pageNumber", required = false, defaultValue = "1") Integer pageNumber,
			@RequestParam(value = "sort", required = false, defaultValue = "[-fromDate, -primary]") List<String> sort,
			@RequestParam(value = "asOnDate", required = false) LocalDate asOnDate,
			@RequestParam(value = "isPrimary", required = false) Boolean isPrimary) throws Exception {

		EmployeeRes response = new EmployeeRes();
		response.setResponseInfo(new ResponseInfo("", "", new Date().toString(), "", "", "Successful response"));
		if (code != null && !code.isEmpty() && asOnDate != null) {
			response.getAssignments().addAll(assignmentService.getAllActiveEmployeeAssignmentsByEmpCode(code,
					asOnDate.toDateTimeAtStartOfDay().toDate()));
		} else {
			throw new Exception();
		}

		return response;
	}

	@ExceptionHandler(Exception.class)
	public ResponseEntity<ErrorRes> handleError(Exception ex) {
		ex.printStackTrace();
		ErrorRes response = new ErrorRes();
		ResponseInfo responseInfo = new ResponseInfo("", "", new Date().toString(), "", "",
				"Failed to get assignments");
		response.setResponseInfo(responseInfo);
		//TODO: Fill right values
		Error error = new Error(null, null, null);
//        error.setCode(400);
//        error.setDescription("Failed to get positions");
		response.setError(error);
		response.setError(error);
		return new ResponseEntity<ErrorRes>(response, HttpStatus.BAD_REQUEST);
	}

}