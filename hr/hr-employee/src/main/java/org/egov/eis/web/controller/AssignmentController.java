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
import org.egov.eis.model.Assignment;
import org.egov.eis.service.AssignmentService;
import org.egov.eis.web.contract.AssignmentGetRequest;
import org.egov.eis.web.contract.AssignmentResponse;
import org.egov.eis.web.contract.RequestInfoWrapper;
import org.egov.eis.web.contract.factory.ResponseInfoFactory;
import org.egov.eis.web.errorhandler.ErrorHandler;
import org.egov.eis.web.validator.RequestValidator;
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
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/employees/{id}/assignments")
public class AssignmentController {

	private static final Logger logger = LoggerFactory.getLogger(AssignmentController.class);

	@Autowired
	private AssignmentService assignmentService;

	@Autowired
	private ErrorHandler errHandler;

	@Autowired
	private RequestValidator requestValidator;

	@Autowired
	private ResponseInfoFactory responseInfoFactory;

	/**
	 * Maps Post Requests for _search & returns ResponseEntity of either
	 * AssignmentResponse type or ErrorResponse type
	 * 
	 * @param assignmentGetRequest
	 * @param modelAttributeBindingResult
	 * @param requestInfoWrapper
	 * @param requestBodyBindingResult
	 * @param id
	 * @return ResponseEntity<?>
	 */
	@PostMapping("_search")
	@ResponseBody
	public ResponseEntity<?> search(@ModelAttribute @Valid AssignmentGetRequest assignmentGetRequest,
			BindingResult modelAttributeBindingResult, @RequestBody @Valid RequestInfoWrapper requestInfoWrapper,
			BindingResult requestBodyBindingResult, @PathVariable Long id) {
		RequestInfo requestInfo = requestInfoWrapper.getRequestInfo();

		ResponseEntity<?> errorResponseEntity = requestValidator.validateSearchRequest(requestInfo,
				modelAttributeBindingResult, requestBodyBindingResult);

		if (errorResponseEntity != null)
			return errorResponseEntity;

		// Call service
		List<Assignment> assignmentsList = null;
		try {
			assignmentsList = assignmentService.getAssignments(id, assignmentGetRequest);
		} catch (Exception exception) {
			logger.error("Error while processing request " + assignmentGetRequest, exception);
			return errHandler.getResponseEntityForUnexpectedErrors(requestInfo);
		}

		return getSuccessResponse(assignmentsList, requestInfo);
	}

	/**
	 * Populate AssignmentResponse object & returns ResponseEntity of type
	 * AssignmentResponse containing ResponseInfo & List of Assignments
	 * 
	 * @param assignmentsList
	 * @param requestInfo
	 * @return ResponseEntity<?>
	 */
	private ResponseEntity<?> getSuccessResponse(List<Assignment> assignmentsList, RequestInfo requestInfo) {
		AssignmentResponse assignmentRes = new AssignmentResponse();
		assignmentRes.setAssignment(assignmentsList);
		ResponseInfo responseInfo = responseInfoFactory.createResponseInfoFromRequestInfo(requestInfo, true);
		responseInfo.setStatus(HttpStatus.OK.toString());
		assignmentRes.setResponseInfo(responseInfo);
		return new ResponseEntity<AssignmentResponse>(assignmentRes, HttpStatus.OK);

	}

}
