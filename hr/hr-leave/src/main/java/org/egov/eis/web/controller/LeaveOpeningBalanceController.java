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

import org.egov.common.contract.request.RequestInfo;
import org.egov.common.contract.response.ResponseInfo;
import org.egov.eis.model.LeaveOpeningBalance;
import org.egov.eis.service.LeaveOpeningBalanceService;
import org.egov.eis.web.contract.LeaveOpeningBalanceGetRequest;
import org.egov.eis.web.contract.LeaveOpeningBalanceRequest;
import org.egov.eis.web.contract.LeaveOpeningBalanceResponse;
import org.egov.eis.web.contract.RequestInfoWrapper;
import org.egov.eis.web.contract.factory.ResponseInfoFactory;
import org.egov.eis.web.errorhandlers.ErrorHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/leaveopeningbalances")
public class LeaveOpeningBalanceController {

	private static final Logger logger = LoggerFactory.getLogger(LeaveOpeningBalanceController.class);

	@Autowired
	private LeaveOpeningBalanceService leaveOpeningBalanceService;

	@Autowired
	private ErrorHandler errorHandler;

	@Autowired
	private ResponseInfoFactory responseInfoFactory;

	@PostMapping("_search")
	@ResponseBody
	public ResponseEntity<?> search(@ModelAttribute @Valid LeaveOpeningBalanceGetRequest leaveOpeningBalanceGetRequest,
			BindingResult modelAttributeBindingResult, @RequestBody @Valid RequestInfoWrapper requestInfoWrapper,
			BindingResult requestBodyBindingResult) {
		RequestInfo requestInfo = requestInfoWrapper.getRequestInfo();

		// validate input params
		if (modelAttributeBindingResult.hasErrors()) {
			return errorHandler.getErrorResponseEntityForMissingParameters(modelAttributeBindingResult, requestInfo);
		}

		// validate input params
		if (requestBodyBindingResult.hasErrors()) {
			return errorHandler.getErrorResponseEntityForMissingRequestInfo(requestBodyBindingResult, requestInfo);
		}

		// Call service
		List<LeaveOpeningBalance> leaveOpeningBalancesList = null;
		try {
			leaveOpeningBalancesList = leaveOpeningBalanceService
					.getLeaveOpeningBalance(leaveOpeningBalanceGetRequest, requestInfo);
		} catch (Exception exception) {
			logger.error("Error while processing request " + leaveOpeningBalanceGetRequest, exception);
			return errorHandler.getResponseEntityForUnexpectedErrors(requestInfo);
		}

		return getSuccessResponse(leaveOpeningBalancesList, requestInfo);
	}

	/**
	 * Maps Post Requests for _create & returns ResponseEntity of either
	 * LeaveOpeningBalanceResponse type or ErrorResponse type
	 * 
	 * @param leaveOpeningBalanceRequest
	 * @param bindingResult
	 * @param type
	 * @return ResponseEntity<?>
	 */

	@PostMapping("_create")
	@ResponseBody
	public ResponseEntity<?> create(@RequestBody LeaveOpeningBalanceRequest leaveOpeningBalanceRequest,
			BindingResult bindingResult, @RequestParam(name = "type", required = false) String type) {

		ResponseEntity<?> errorResponseEntity = validateLeaveOpeningBalanceRequest(leaveOpeningBalanceRequest,
				bindingResult);
		if (errorResponseEntity != null)
			return errorResponseEntity;

		return leaveOpeningBalanceService.createLeaveOpeningBalance(leaveOpeningBalanceRequest, type);
	}

	@PostMapping("_carryforward")
	@ResponseBody
	public ResponseEntity<?> carryForward(
			@ModelAttribute @Valid LeaveOpeningBalanceGetRequest leaveOpeningBalanceGetRequest,
			BindingResult modelAttributeBindingResult, @RequestBody @Valid RequestInfoWrapper requestInfoWrapper,
			BindingResult requestBodyBindingResult) {
		RequestInfo requestInfo = requestInfoWrapper.getRequestInfo();
		// validate input params
		if (modelAttributeBindingResult.hasErrors()) {
			return errorHandler.getErrorResponseEntityForMissingParameters(modelAttributeBindingResult, requestInfo);
		}

		// validate input params
		if (requestBodyBindingResult.hasErrors()) {
			return errorHandler.getErrorResponseEntityForMissingRequestInfo(requestBodyBindingResult, requestInfo);
		}

		return leaveOpeningBalanceService.carryForwardLeaveOpeningBalance(leaveOpeningBalanceGetRequest, requestInfo);
	}

	/**
	 * Maps Post Requests for _create & returns ResponseEntity of either
	 * LeaveOpeningBalanceResponse type or ErrorResponse type
	 * 
	 * @param leaveOpeningBalanceRequest
	 * @param employeeId
	 * @param bindingResult
	 * @return ResponseEntity<?>
	 */

	@PostMapping("/{employeeId}/_update")
	@ResponseBody
	public ResponseEntity<?> update(@RequestBody LeaveOpeningBalanceRequest leaveOpeningBalanceRequest,
			@PathVariable(required = true, name = "employeeId") Long employeeId, BindingResult bindingResult) {

		ResponseEntity<?> errorResponseEntity = validateLeaveOpeningBalanceRequest(leaveOpeningBalanceRequest,
				bindingResult);
		if (errorResponseEntity != null)
			return errorResponseEntity;

		return leaveOpeningBalanceService.updateLeaveOpeningBalance(leaveOpeningBalanceRequest);
	}

	/**
	 * Populate Response object and returnleaveOpeningBalancesList
	 * 
	 * @param leaveOpeningBalancesList
	 * @return
	 */
	private ResponseEntity<?> getSuccessResponse(List<LeaveOpeningBalance> leaveOpeningBalancesList,
			RequestInfo requestInfo) {
		LeaveOpeningBalanceResponse leaveOpeningBalanceRes = new LeaveOpeningBalanceResponse();
		leaveOpeningBalanceRes.setLeaveOpeningBalance(leaveOpeningBalancesList);
		ResponseInfo responseInfo = responseInfoFactory.createResponseInfoFromRequestInfo(requestInfo, true);
		responseInfo.setStatus(HttpStatus.OK.toString());
		leaveOpeningBalanceRes.setResponseInfo(responseInfo);
		return new ResponseEntity<LeaveOpeningBalanceResponse>(leaveOpeningBalanceRes, HttpStatus.OK);

	}

	/**
	 * Validate EmployeeRequest object & returns ErrorResponseEntity if there
	 * are any errors or else returns null
	 * 
	 * @param leaveOpeningBalanceRequest
	 * @param bindingResult
	 * @return ResponseEntity<?>
	 */
	private ResponseEntity<?> validateLeaveOpeningBalanceRequest(LeaveOpeningBalanceRequest leaveOpeningBalanceRequest,
			BindingResult bindingResult) {
		// validate input params that can be handled by annotations
		if (bindingResult.hasErrors()) {
			return errorHandler.getErrorResponseEntityForBindingErrors(bindingResult,
					leaveOpeningBalanceRequest.getRequestInfo());
		}
		return null;
	}

}
