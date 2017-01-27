package org.egov.eis.controller;

import java.util.Date;
import java.util.List;

import org.egov.eis.model.DepartmentRes;
import org.egov.eis.model.Error;
import org.egov.eis.model.ErrorRes;
import org.egov.eis.model.ResponseInfo;
import org.egov.eis.service.DepartmentService;
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
public class DepartmentRestController {

	@Autowired
	private DepartmentService departmentService;

	@RequestMapping(value = "/departments", method = RequestMethod.GET)
	@ResponseBody
	public DepartmentRes getDesignations(@RequestParam(value = "code") String code,
			@RequestParam(value = "pageSize", required = false, defaultValue = "20") Integer pageSize,
			@RequestParam(value = "pageNumber", required = false, defaultValue = "1") Integer pageNumber,
			@RequestParam(value = "sort", required = false, defaultValue = "[-name]") List<String> sort)
			throws Exception {

		DepartmentRes response = new DepartmentRes();
		response.setResponseInfo(new ResponseInfo("", "", new Date().toString(), "", "", "Successful response"));
		if (code != null && !code.isEmpty()) {
			response.getDepartment().add(departmentService.getByCode(code));
		}
		if (code == null || code.isEmpty()) {
			response.getDepartment().addAll(departmentService.getAllDepartments());
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
				"Failed to get departments");
		response.setResponseInfo(responseInfo);
		Error error = new Error();
		error.setCode(400);
		error.setDescription("Failed to get departments");
		response.setError(error);
		return new ResponseEntity<ErrorRes>(response, HttpStatus.BAD_REQUEST);
	}

}