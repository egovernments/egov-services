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

import static org.springframework.util.ObjectUtils.isEmpty;

import java.util.List;

import javax.validation.Valid;

import lombok.Setter;
import org.egov.common.contract.request.RequestInfo;
import org.egov.common.contract.response.ResponseInfo;
import org.egov.eis.model.Nominee;
import org.egov.eis.service.NomineeService;
import org.egov.eis.web.contract.NomineeGetRequest;
import org.egov.eis.web.contract.NomineeRequest;
import org.egov.eis.web.contract.NomineeResponse;
import org.egov.eis.web.contract.RequestInfoWrapper;
import org.egov.eis.web.contract.factory.ResponseInfoFactory;
import org.egov.eis.web.errorhandler.ErrorHandler;
import org.egov.eis.web.validator.DataIntegrityValidatorForCreateNominee;
import org.egov.eis.web.validator.DataIntegrityValidatorForUpdateNominee;
import org.egov.eis.web.validator.RequestValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ValidationUtils;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@Setter
@RestController
@RequestMapping("/nominees")
public class NomineeController {

	private static final Logger LOGGER = LoggerFactory.getLogger(NomineeController.class);

	@Autowired
	private NomineeService nomineeService;

	@Autowired
	private RequestValidator requestValidator;

	@Autowired
	private ErrorHandler errorHandler;

	@Autowired
	private ResponseInfoFactory responseInfoFactory;

	@Autowired
	private DataIntegrityValidatorForCreateNominee dataIntegrityValidatorForCreateNominee;

	@Autowired
	private DataIntegrityValidatorForUpdateNominee dataIntegrityValidatorForUpdateNominee;

	/**
	 * Maps Post Requests for _search & returns ResponseEntity of either NomineeResponse type or ErrorResponse type
	 * @param nomineeGetRequest,
	 * @param modelAttributeBindingResult
	 * @param requestInfoWrapper
	 * @param requestBodyBindingResult
	 * @return ResponseEntity<?>
	 */
	@PostMapping("_search")
	@ResponseBody
	public ResponseEntity<?> search(@ModelAttribute @Valid NomineeGetRequest nomineeGetRequest,
			BindingResult modelAttributeBindingResult, @RequestBody @Valid RequestInfoWrapper requestInfoWrapper,
			BindingResult requestBodyBindingResult) {
		RequestInfo requestInfo = requestInfoWrapper.getRequestInfo();

		ResponseEntity<?> errorResponseEntity = requestValidator.validateSearchRequest(requestInfo,
				modelAttributeBindingResult, requestBodyBindingResult);

		if (!isEmpty(errorResponseEntity))
			return errorResponseEntity;

		List<Nominee> nomineesList = null;
		try {
			nomineesList = nomineeService.getNominees(nomineeGetRequest, requestInfo);
		} catch (Exception exception) {
			LOGGER.error("Error while processing request " + nomineeGetRequest, exception);
			return errorHandler.getResponseEntityForUnexpectedErrors(requestInfo);
		}

		return getSuccessResponseForSearch(nomineesList, requestInfo);
	}

	/**
	 * Maps Post Requests for _create & returns ResponseEntity of either NomineeResponse type or ErrorResponse type
	 * @param nomineeRequest
	 * @param bindingResult
	 * @return ResponseEntity<?>
	 */
	@PostMapping(value = "/_create")
	@ResponseBody
	public ResponseEntity<?> create(@RequestBody @Valid NomineeRequest nomineeRequest, BindingResult bindingResult) {
		LOGGER.debug("nomineeRequest::" + nomineeRequest);

		ResponseEntity<?> errorResponseEntity = validateNomineeRequest(nomineeRequest, bindingResult, false);
		if (!isEmpty(errorResponseEntity))
			return errorResponseEntity;

		List<Nominee> nominees = null;
		try {
			nominees = nomineeService.createAsync(nomineeRequest);
		} catch (Exception exception) {
			LOGGER.error("Error while processing request ", exception);
			return errorHandler.getResponseEntityForUnexpectedErrors(nomineeRequest.getRequestInfo());
		}
		return getSuccessResponseForCreateAndUpdate(nominees, nomineeRequest.getRequestInfo());
	}

	/**
	 * Maps Post Requests for _update & returns ResponseEntity of either NomineeResponse type or ErrorResponse type
	 * @param nomineeRequest
	 * @param bindingResult
	 * @return ResponseEntity<?>
	 */
	@PostMapping(value = "/_update")
	@ResponseBody
	public ResponseEntity<?> update(@RequestBody @Valid NomineeRequest nomineeRequest, BindingResult bindingResult) {
		LOGGER.debug("nomineeRequest::" + nomineeRequest);

		ResponseEntity<?> errorResponseEntity = validateNomineeRequest(nomineeRequest, bindingResult, true);
		if (!isEmpty(errorResponseEntity))
			return errorResponseEntity;
		
		List<Nominee> nominees = null;
		try {
			nominees = nomineeService.updateAsync(nomineeRequest);
		} catch (Exception exception) {
			LOGGER.error("Error while processing request ", exception);
			return errorHandler.getResponseEntityForUnexpectedErrors(nomineeRequest.getRequestInfo());
		}
		return getSuccessResponseForCreateAndUpdate(nominees, nomineeRequest.getRequestInfo());
	}

	/**
	 * Validate NomineeRequest object & returns ErrorResponseEntity if there are any errors or else returns null
	 * @param nomineeRequest
	 * @param bindingResult
	 * @param isUpdate
	 * @return ResponseEntity<?>
	 */
	private ResponseEntity<?> validateNomineeRequest(NomineeRequest nomineeRequest, BindingResult bindingResult,
			boolean isUpdate) {
		// validate input params that can be handled by annotations
		if (bindingResult.hasErrors()) {
			return errorHandler.getErrorResponseEntityForInvalidRequest(bindingResult, nomineeRequest.getRequestInfo());
		}

		if (isUpdate)
			ValidationUtils.invokeValidator(dataIntegrityValidatorForUpdateNominee, nomineeRequest, bindingResult);
		else
			ValidationUtils.invokeValidator(dataIntegrityValidatorForCreateNominee, nomineeRequest, bindingResult);

		if (bindingResult.hasErrors()) {
			return errorHandler.getErrorResponseEntityForInvalidRequest(bindingResult, nomineeRequest.getRequestInfo());
		}
		return null;
	}

	/**
	 * Populate NomineeResponse object & returns ResponseEntity of type NomineeResponse
	 * containing ResponseInfo & List of Nominee
	 * @param nomineesList
	 * @param requestInfo
	 * @return ResponseEntity<?>
	 */
	private ResponseEntity<?> getSuccessResponseForSearch(List<Nominee> nomineesList, RequestInfo requestInfo) {
		NomineeResponse nomineeResponse = new NomineeResponse();
		nomineeResponse.setNominees(nomineesList);
		ResponseInfo responseInfo = responseInfoFactory.createResponseInfoFromRequestInfo(requestInfo, true);
		responseInfo.setStatus(HttpStatus.OK.toString());
		nomineeResponse.setResponseInfo(responseInfo);
		return new ResponseEntity<>(nomineeResponse, HttpStatus.OK);
	}
	
	/**
	 * Populate NomineeResponse object & returns ResponseEntity of type NomineeResponse
	 * containing ResponseInfo & Nominee objects
	 * @param nominees
	 * @param requestInfo
	 * @return ResponseEntity<?>
	 */
	public ResponseEntity<?> getSuccessResponseForCreateAndUpdate(List<Nominee> nominees, RequestInfo requestInfo) {
		NomineeResponse nomineeResponse = new NomineeResponse();
		nomineeResponse.setNominees(nominees);
		ResponseInfo responseInfo = responseInfoFactory.createResponseInfoFromRequestInfo(requestInfo, true);
		responseInfo.setStatus(HttpStatus.OK.toString());
		nomineeResponse.setResponseInfo(responseInfo);
		return new ResponseEntity<>(nomineeResponse, HttpStatus.OK);
	}
}