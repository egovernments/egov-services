/*
 * eGov suite of products aim to improve the internal efficiency,transparency,
 * accountability and the service delivery of the government  organizations.
 *
 *  Copyright (C) 2016  eGovernments Foundation
 *
 *  The updated version of eGov suite of products as by eGovernments Foundation
 *  is available at http://www.egovernments.org
 *
 *  This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this program. If not, see http://www.gnu.org/licenses/ or
 *  http://www.gnu.org/licenses/gpl.html .
 *
 *  In addition to the terms of the GPL license to be adhered to in using this
 *  program, the following additional terms are to be complied with:
 *
 *      1) All versions of this program, verbatim or modified must carry this
 *         Legal Notice.
 *
 *      2) Any misrepresentation of the origin of the material is prohibited. It
 *         is required that all modified versions of this material be marked in
 *         reasonable ways as different from the original version.
 *
 *      3) This license does not grant any rights to any user of the program
 *         with regards to rights under trademark law for use of the trade names
 *         or trademarks of eGovernments Foundation.
 *
 *  In case of any queries, you can reach eGovernments Foundation at contact@egovernments.org.
 */

package org.egov.eis.web.controller;

import java.util.List;

import javax.validation.Valid;

import org.egov.eis.model.EmployeeInfo;
import org.egov.eis.service.EmployeeService;
import org.egov.eis.web.contract.EmployeeGetRequest;
import org.egov.eis.web.contract.EmployeeInfoResponse;
import org.egov.eis.web.contract.EmployeeRequest;
import org.egov.eis.web.contract.RequestInfo;
import org.egov.eis.web.contract.ResponseInfo;
import org.egov.eis.web.contract.factory.ResponseInfoFactory;
import org.egov.eis.web.errorhandler.ErrorHandler;
import org.egov.eis.web.validator.EmployeeAssignmentValidator;
import org.egov.eis.web.validator.RequestValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ValidationUtils;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/employees")
public class EmployeeController {

	private static final Logger LOGGER = LoggerFactory.getLogger(EmployeeController.class);

	@Autowired
	private EmployeeService employeeService;

	@Autowired
	private RequestValidator requestValidator;

	@Autowired
	private ErrorHandler errorHandler;

	@Autowired
	private ResponseInfoFactory responseInfoFactory;

	@Autowired
	private EmployeeAssignmentValidator employeeAssignmentValidator;

	/**
	 * Maps Post Requests for _search & returns ResponseEntity of either
	 * EmployeeResponse type or ErrorResponse type
	 * 
	 * @param employeeGetRequest,
	 * @param bindingResult
	 * @param requestInfo
	 * @param headers
	 * @return ResponseEntity<?>
	 */
	@PostMapping(value = "_search", headers = { "x-user-info" })
	@ResponseBody
	public ResponseEntity<?> search(@RequestHeader HttpHeaders headers,
			@ModelAttribute @Valid EmployeeGetRequest employeeGetRequest, BindingResult bindingResult,
			@RequestBody RequestInfo requestInfo) {

		ResponseEntity<?> errorResponseEntity = requestValidator.validateSearchRequest(requestInfo, bindingResult);
		if (errorResponseEntity != null)
			return errorResponseEntity;

		// Call service
		List<EmployeeInfo> employeesList = null;
		try {
			employeesList = employeeService.getEmployees(employeeGetRequest, requestInfo, headers);
		} catch (Exception exception) {
			LOGGER.error("Error while processing request " + employeeGetRequest, exception);
			return errorHandler.getResponseEntityForUnexpectedErrors(requestInfo);
		}

		return getSuccessResponseForSearch(employeesList, requestInfo);
	}

	/**
	 * Maps Post Requests for _search & returns ResponseEntity of either
	 * EmployeeResponse type or ErrorResponse type
	 * 
	 * @param employeeGetRequest
	 * @param bindingResult
	 * @param requestInfo
	 * @param headers
	 * @return ResponseEntity<?>
	 */
	@PostMapping(value = "/_create", headers = { "x-user-info" })
	@ResponseBody
	public ResponseEntity<?> create(@RequestHeader HttpHeaders headers,
			@RequestBody @Valid EmployeeRequest employeeRequest, BindingResult bindingResult) {
		LOGGER.debug("employeeRequest::" + employeeRequest);

		ResponseEntity<?> errorResponseEntity = validateEmployeeCreateRequest(employeeRequest, bindingResult);
		if (errorResponseEntity != null)
			return errorResponseEntity;

		return employeeService.createEmployee(employeeRequest, headers);
	}

	/**
	 * Validate EmployeeRequest object & returns ErrorResponseEntity if there
	 * are any errors or else returns null
	 * 
	 * @param employeeRequest
	 * @param headers
	 * @param bindingResult
	 * @return ResponseEntity<?>
	 */
	private ResponseEntity<?> validateEmployeeCreateRequest(EmployeeRequest employeeRequest, BindingResult bindingResult) {
		// validate input params that can be handled by annotations
		if (bindingResult.hasErrors()) {
			return errorHandler.getErrorResponseEntityForBindingErrors(bindingResult, employeeRequest.getRequestInfo());
		}

		// validate input params that can't be handled by annotations
		ValidationUtils.invokeValidator(employeeAssignmentValidator, employeeRequest.getEmployee(), bindingResult);

		if (bindingResult.hasErrors()) {
			return errorHandler.getErrorResponseEntityForBindingErrors(bindingResult, employeeRequest.getRequestInfo());
		}
		return null;
	}

	/**
	 * Populate EmployeeInfoResponse object & returns ResponseEntity of type
	 * EmployeeInfoResponse containing ResponseInfo & List of EmployeeInfo
	 * 
	 * @param employeeList
	 * @param requestInfo
	 * @param headers
	 * @return ResponseEntity<?>
	 */
	private ResponseEntity<?> getSuccessResponseForSearch(List<EmployeeInfo> employeesList, RequestInfo requestInfo) {
		EmployeeInfoResponse employeeInfoResponse = new EmployeeInfoResponse();
		employeeInfoResponse.setEmployees(employeesList);
		ResponseInfo responseInfo = responseInfoFactory.createResponseInfoFromRequestInfo(requestInfo, true);
		responseInfo.setStatus(HttpStatus.OK.toString());
		employeeInfoResponse.setResponseInfo(responseInfo);
		return new ResponseEntity<EmployeeInfoResponse>(employeeInfoResponse, HttpStatus.OK);
	}

}
