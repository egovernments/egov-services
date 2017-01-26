package org.egov.eis.controller;

import java.util.Date;
import java.util.List;

import org.egov.eis.model.Error;
import org.egov.eis.model.ErrorRes;
import org.egov.eis.model.PositionRes;
import org.egov.eis.model.ResponseInfo;
import org.egov.eis.service.PositionService;
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
public class PositionRestController {

	@Autowired
	private PositionService positionService;

	@RequestMapping(value = "/employee/{code}/positions", method = RequestMethod.GET)
	@ResponseBody
	public PositionRes getPositions(@PathVariable("code") String code,
			@RequestParam(value = "tenantId", required = true) String tenantId,
			@RequestParam(value = "departmentCode") String departmentCode,
			@RequestParam(value = "designationCode") String designationCode,
			@RequestParam(value = "pageSize", required = false, defaultValue = "20") Integer pageSize,
			@RequestParam(value = "pageNumber", required = false, defaultValue = "1") Integer pageNumber,
			@RequestParam(value = "sort", required = false, defaultValue = "[-fromDate, -primary]") List<String> sort,
			@RequestParam(value = "asOnDate", required = false) LocalDate asOnDate,
			@RequestParam(value = "isPrimary") Boolean isPrimary) throws Exception {

		PositionRes response = new PositionRes();
		response.setResponseInfo(new ResponseInfo("", "", new Date().toString(), "", "", "Successful response"));
		if (code != null && !code.isEmpty() && asOnDate != null) {
			response.getPosition()
					.addAll(positionService.getAllPositionsByEmpCode(code, asOnDate.toDateTimeAtStartOfDay().toDate()));
		} else {
			throw new Exception();
		}

		return response;
	}

	@RequestMapping(value = "/position", method = RequestMethod.GET)
	@ResponseBody
	public PositionRes getPosition(@RequestParam(value = "tenantId", required = true) String tenantId,
			@RequestParam(value = "id") String id) throws Exception {

		PositionRes response = new PositionRes();
		response.setResponseInfo(new ResponseInfo("", "", new Date().toString(), "", "", "Successful response"));
		if (id != null && !id.isEmpty()) {
			response.getPosition().add(positionService.getById(Long.valueOf(id)));
		} else {
			throw new Exception();
		}

		return response;
	}

	@ExceptionHandler(Exception.class)
	public ResponseEntity<ErrorRes> handleError(Exception ex) {
		ex.printStackTrace();
		ErrorRes response = new ErrorRes();
		ResponseInfo responseInfo = new ResponseInfo("", "", new Date().toString(), "", "", "Failed to get positions");
		response.setResponseInfo(responseInfo);
		Error error = new Error();
		error.setCode(400);
		error.setDescription("Failed to get positions");
		response.setError(error);
		return new ResponseEntity<ErrorRes>(response, HttpStatus.BAD_REQUEST);
	}

}