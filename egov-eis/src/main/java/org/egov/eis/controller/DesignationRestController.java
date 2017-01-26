package org.egov.eis.controller;

import java.util.Date;
import java.util.List;

import org.egov.eis.model.DesignationRes;
import org.egov.eis.model.Error;
import org.egov.eis.model.ErrorRes;
import org.egov.eis.model.ResponseInfo;
import org.egov.eis.service.DesignationService;
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
public class DesignationRestController {

	@Autowired
	private DesignationService designationService;

	@RequestMapping(value = "/designations", method = RequestMethod.GET)
	@ResponseBody
	public DesignationRes getDesignations(@RequestParam(value = "name", required = true) String name,
			@RequestParam(value = "pageSize", required = false, defaultValue = "20") Integer pageSize,
			@RequestParam(value = "pageNumber", required = false, defaultValue = "1") Integer pageNumber,
			@RequestParam(value = "sort", required = false, defaultValue = "[-name]") List<String> sort)
			throws Exception {

		DesignationRes response = new DesignationRes();
		response.setResponseInfo(new ResponseInfo("", "", new Date().toString(), "", "", "Successful response"));
		if (name != null && !name.isEmpty()) {
			response.getDesignation().addAll(designationService.getDesignationsByName(name));
		} else {
			throw new Exception();
		}

		return response;
	}

	@RequestMapping(value = "/designation", method = RequestMethod.GET)
	@ResponseBody
	public DesignationRes getDesignation(@RequestParam(value = "tenantId", required = true) String tenantId,
			@RequestParam(value = "id") String id) throws Exception {

		DesignationRes response = new DesignationRes();
		response.setResponseInfo(new ResponseInfo("", "", new Date().toString(), "", "", "Successful response"));
		if (id != null && !id.isEmpty()) {
			response.getDesignation().add(designationService.getDesignationById(Long.valueOf(id)));
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
				"Failed to get designations");
		response.setResponseInfo(responseInfo);
		Error error = new Error();
		error.setCode(400);
		error.setDescription("Failed to get designations");
		response.setError(error);
		return new ResponseEntity<ErrorRes>(response, HttpStatus.BAD_REQUEST);
	}

}