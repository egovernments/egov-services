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

import org.egov.common.contract.request.RequestInfo;
import org.egov.common.contract.response.ResponseInfo;
import org.egov.eis.model.LeaveType;
import org.egov.eis.service.LeaveTypeService;
import org.egov.eis.util.ApplicationConstants;
import org.egov.eis.web.contract.LeaveTypeGetRequest;
import org.egov.eis.web.contract.LeaveTypeRequest;
import org.egov.eis.web.contract.LeaveTypeResponse;
import org.egov.eis.web.contract.RequestInfoWrapper;
import org.egov.eis.web.contract.factory.ResponseInfoFactory;
import org.egov.eis.web.errorhandlers.Error;
import org.egov.eis.web.errorhandlers.ErrorHandler;
import org.egov.eis.web.errorhandlers.ErrorResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/leavetypes")
public class LeaveTypeController {

	private static final Logger logger = LoggerFactory.getLogger(LeaveTypeController.class);

	@Autowired
	private LeaveTypeService leaveTypeService;

	@Autowired
	private ErrorHandler errHandler;

	@Autowired
	private ResponseInfoFactory responseInfoFactory;

	@Autowired
	private ApplicationConstants applicationConstants;

	@PostMapping(value = "_create")
	@ResponseBody
	public ResponseEntity<?> create(@RequestBody @Valid final LeaveTypeRequest leaveTypeRequest,
			final BindingResult errors) {
		if (errors.hasErrors()) {
			final ErrorResponse errRes = populateErrors(errors);
			return new ResponseEntity<ErrorResponse>(errRes, HttpStatus.BAD_REQUEST);
		}
		logger.info("leaveTypeRequest::" + leaveTypeRequest);

		final List<ErrorResponse> errorResponses = validateLeaveTypeRequest(leaveTypeRequest);
		if (!errorResponses.isEmpty())
			return new ResponseEntity<List<ErrorResponse>>(errorResponses, HttpStatus.BAD_REQUEST);

		final List<LeaveType> leaveTypes = leaveTypeService.createLeaveType(leaveTypeRequest);

		return getSuccessResponse(leaveTypes, leaveTypeRequest.getRequestInfo());
	}

	@PostMapping(value = "/{leaveTypeId}/_update")
	@ResponseBody
	public ResponseEntity<?> update(@RequestBody @Valid final LeaveTypeRequest leaveTypeRequest,
			final BindingResult errors, @PathVariable Long leaveTypeId) {
		// validate header
		if (errors.hasErrors()) {
			final ErrorResponse errRes = populateErrors(errors);
			return new ResponseEntity<ErrorResponse>(errRes, HttpStatus.BAD_REQUEST);
		}
		leaveTypeRequest.getLeaveType().get(0).setId(leaveTypeId);
		logger.info("leaveTypeRequest::" + leaveTypeRequest);

		final List<ErrorResponse> errorResponses = validateLeaveTypeRequest(leaveTypeRequest);
		if (!errorResponses.isEmpty())
			return new ResponseEntity<List<ErrorResponse>>(errorResponses, HttpStatus.BAD_REQUEST);

		final List<LeaveType> leaveTypes = leaveTypeService.createLeaveType(leaveTypeRequest);

		return getSuccessResponse(leaveTypes, leaveTypeRequest.getRequestInfo());
	}

	@SuppressWarnings("deprecation")
	private List<ErrorResponse> validateLeaveTypeRequest(final LeaveTypeRequest leaveTypeRequest) {
		boolean isLeaveTypeExists = false;
		final List<ErrorResponse> errorResponses = new ArrayList<>();

		for (LeaveType leaveType : leaveTypeRequest.getLeaveType()) {
			if (leaveTypeService.getLeaveTypeByName(leaveType.getId(), leaveType.getName(), leaveType.getTenantId())) {
				isLeaveTypeExists = true;
				break;
			}
		}

		if (isLeaveTypeExists) {
			final ErrorResponse errorResponse = new ErrorResponse();
			final Error error = new Error();
			error.setDescription(applicationConstants.getErrorMessage(ApplicationConstants.MSG_LEAVETYPE_PRESENT));
			errorResponse.setError(error);
			errorResponses.add(errorResponse);
		}
		return errorResponses;

	}

	@PostMapping("_search")
	@ResponseBody
	public ResponseEntity<?> search(@ModelAttribute @Valid LeaveTypeGetRequest leaveTypeGetRequest,
			BindingResult modelAttributeBindingResult, @RequestBody @Valid RequestInfoWrapper requestInfoWrapper,
			BindingResult requestBodyBindingResult) {
		RequestInfo requestInfo = requestInfoWrapper.getRequestInfo();

		// validate input params
		if (modelAttributeBindingResult.hasErrors()) {
			return errHandler.getErrorResponseEntityForMissingParameters(modelAttributeBindingResult, requestInfo);
		}

		// validate input params
		if (requestBodyBindingResult.hasErrors()) {
			return errHandler.getErrorResponseEntityForMissingRequestInfo(requestBodyBindingResult, requestInfo);
		}

		// Call service
		List<LeaveType> leaveTypesList = null;
		try {
			leaveTypesList = leaveTypeService.getLeaveTypes(leaveTypeGetRequest);
		} catch (Exception exception) {
			logger.error("Error while processing request " + leaveTypeGetRequest, exception);
			return errHandler.getResponseEntityForUnexpectedErrors(requestInfo);
		}

		return getSuccessResponse(leaveTypesList, requestInfo);
	}

	/**
	 * Populate Response object and returnleaveTypesList
	 * 
	 * @param leaveTypesList
	 * @return
	 */
	private ResponseEntity<?> getSuccessResponse(List<LeaveType> leaveTypesList, RequestInfo requestInfo) {
		LeaveTypeResponse leaveTypeRes = new LeaveTypeResponse();
		leaveTypeRes.setLeaveType(leaveTypesList);
		ResponseInfo responseInfo = responseInfoFactory.createResponseInfoFromRequestInfo(requestInfo, true);
		responseInfo.setStatus(HttpStatus.OK.toString());
		leaveTypeRes.setResponseInfo(responseInfo);
		return new ResponseEntity<LeaveTypeResponse>(leaveTypeRes, HttpStatus.OK);

	}

	private ErrorResponse populateErrors(final BindingResult errors) {
		final ErrorResponse errRes = new ErrorResponse();

		final Error error = new Error();
		error.setCode(1);
		error.setDescription("Error while binding request");
		if (errors.hasFieldErrors())
			for (final FieldError fieldError : errors.getFieldErrors()) {
				error.getFields().put(fieldError.getField(), fieldError.getRejectedValue());
			}
		errRes.setError(error);
		return errRes;
	}

}
