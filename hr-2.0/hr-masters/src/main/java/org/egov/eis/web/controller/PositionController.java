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

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.validation.Valid;

import org.egov.common.contract.request.RequestInfo;
import org.egov.common.contract.response.ResponseInfo;
import org.egov.eis.model.Position;
import org.egov.eis.service.PositionService;
import org.egov.eis.web.contract.PositionBulkRequest;
import org.egov.eis.web.contract.PositionGetRequest;
import org.egov.eis.web.contract.PositionRequest;
import org.egov.eis.web.contract.PositionResponse;
import org.egov.eis.web.contract.RequestInfoWrapper;
import org.egov.eis.web.contract.factory.ResponseInfoFactory;
import org.egov.eis.web.errorhandlers.ErrorHandler;
import org.egov.eis.web.errorhandlers.InvalidDataException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/positions")
public class PositionController {

	private static final Logger logger = LoggerFactory.getLogger(PositionController.class);

	@Autowired
	private PositionService positionService;

	@Autowired
	private ErrorHandler errHandler;

	@Autowired
	private ResponseInfoFactory responseInfoFactory;

	@PostMapping("_search")
	@ResponseBody
	public ResponseEntity<?> search(@ModelAttribute @Valid PositionGetRequest positionGetRequest,
			BindingResult modelAttributeBindingResult, @RequestBody @Valid RequestInfoWrapper requestInfoWrapper,
			BindingResult requestBodyBindingResult) {
		RequestInfo requestInfo = requestInfoWrapper.getRequestInfo();
		Integer pageSize = 0;
		// validate input params
		if (modelAttributeBindingResult.hasErrors()) {
			return errHandler.getErrorResponseEntityForMissingParameters(modelAttributeBindingResult, requestInfo);
		}

		// validate input params
		if (requestBodyBindingResult.hasErrors()) {
			return errHandler.getErrorResponseEntityForMissingRequestInfo(requestBodyBindingResult, requestInfo);
		}

		// Call service
		List<Position> positionsList = null;
		try {
			positionsList = positionService.getPositions(positionGetRequest);
		} catch (Exception exception) {
			logger.error("Error while processing request " + positionGetRequest, exception);
			return errHandler.getResponseEntityForUnexpectedErrors(requestInfo);
		}

		return getSuccessResponse(positionsList, requestInfo);
	}

	@PostMapping("_paginatedsearch")
	@ResponseBody
	public ResponseEntity<?> paginatedSearch(@ModelAttribute @Valid PositionGetRequest positionGetRequest,
			BindingResult modelAttributeBindingResult, @RequestBody @Valid RequestInfoWrapper requestInfoWrapper,
			BindingResult requestBodyBindingResult) {
		RequestInfo requestInfo = requestInfoWrapper.getRequestInfo();
		Integer pageSize = 0;
		// validate input params
		if (modelAttributeBindingResult.hasErrors()) {
			return errHandler.getErrorResponseEntityForMissingParameters(modelAttributeBindingResult, requestInfo);
		}

		// validate input params
		if (requestBodyBindingResult.hasErrors()) {
			return errHandler.getErrorResponseEntityForMissingRequestInfo(requestBodyBindingResult, requestInfo);
		}

		// Call service
		Map<String, Object> positionMap = null;
		try {
			positionMap = positionService.getPaginatedPosistions(positionGetRequest, requestInfo);
		} catch (Exception exception) {
			logger.error("Error while processing request " + positionGetRequest, exception);
			return errHandler.getResponseEntityForUnexpectedErrors(requestInfo);
		}

		return getPaginatedSuccessResponse(positionMap, requestInfo);
	}

	/**
	 * Maps Post Requests for _create & returns ResponseEntity of either
	 * PositionResponse type or ErrorResponse type
	 * 
	 * @param positionRequest
	 * @param bindingResult
	 * @return ResponseEntity<?>
	 */
	@PostMapping("_create")
	@ResponseBody
	public ResponseEntity<?> create(@RequestBody PositionRequest positionRequest, BindingResult bindingResult) {
		ResponseEntity<?> errorResponseEntity = validatePositionRequest(positionRequest, bindingResult);
		if (errorResponseEntity != null)
			return errorResponseEntity;

		try {
			return positionService.createPosition(positionRequest);
		} catch (InvalidDataException ex) {
			return errHandler.getErrorInvalidData(ex, positionRequest.getRequestInfo());
		} catch (Exception ex) {
			return errHandler.getResponseEntityForUnexpectedErrors(positionRequest.getRequestInfo());
		}
	}

	/**
	 * Maps Post Requests for _bulkcreate & returns ResponseEntity of either
	 * PositionResponse type or ErrorResponse type
	 *
	 * @param positionBulkRequest
	 * @param bindingResult
	 * @return ResponseEntity<?>
	 */
	@PostMapping("_bulkcreate")
	@ResponseBody
	public ResponseEntity<?> bulkCreate(@RequestBody PositionBulkRequest positionBulkRequest,
			BindingResult bindingResult) {
		if (bindingResult.hasErrors())
			return errHandler.getErrorResponseEntityForBindingErrors(bindingResult,
					positionBulkRequest.getRequestInfo());

		try {
			return positionService.createBulkPositions(positionBulkRequest);
		} catch (InvalidDataException ex) {
			return errHandler.getErrorInvalidData(ex, positionBulkRequest.getRequestInfo());
		} catch (Exception ex) {
			return errHandler.getResponseEntityForUnexpectedErrors(positionBulkRequest.getRequestInfo());
		}
	}

	/**
	 * Maps Post Requests for _update & returns ResponseEntity of either
	 * PositionResponse type or ErrorResponse type
	 * 
	 * @param positionRequest
	 * @param bindingResult
	 * @return ResponseEntity<?>
	 */
	@PostMapping("/_update")
	@ResponseBody
	public ResponseEntity<?> update(@RequestBody PositionRequest positionRequest, BindingResult bindingResult) {
		ResponseEntity<?> errorResponseEntity = validatePositionRequest(positionRequest, bindingResult);
		if (errorResponseEntity != null)
			return errorResponseEntity;
		return positionService.updatePosition(positionRequest);
	}

	/**
	 * Populate Response object and returnpositionsList
	 * 
	 * @param positionsList
	 * @return
	 */
	private ResponseEntity<?> getSuccessResponse(List<Position> positionsList,  RequestInfo requestInfo) {
		PositionResponse positionRes = new PositionResponse();
		positionRes.setPosition(positionsList);
		ResponseInfo responseInfo = responseInfoFactory.createResponseInfoFromRequestInfo(requestInfo, true);
		responseInfo.setStatus(HttpStatus.OK.toString());
		positionRes.setResponseInfo(responseInfo);
		return new ResponseEntity<PositionResponse>(positionRes, HttpStatus.OK);

	}

	/**
	 * Validate PositionRequest object & returns ErrorResponseEntity if there
	 * are any errors or else returns null
	 * 
	 * @param positionRequest
	 * @param bindingResult
	 * @return ResponseEntity<?>
	 */
	private ResponseEntity<?> validatePositionRequest(PositionRequest positionRequest, BindingResult bindingResult) {
		// validate input params that can be handled by annotations
		if (bindingResult.hasErrors()) {
			return errHandler.getErrorResponseEntityForBindingErrors(bindingResult, positionRequest.getRequestInfo());
		}

		return null;
	}

	public ResponseEntity<?> getPaginatedSuccessResponse(Map<String, Object> requestMap, RequestInfo requestInfo) {
		ResponseInfo responseInfo = responseInfoFactory.createResponseInfoFromRequestInfo(requestInfo, true);

		Map<String, Object> response = new LinkedHashMap<>();
		response.put("ResponseInfo", responseInfo);
		requestMap.forEach(response::put);

		return new ResponseEntity<>(response, HttpStatus.OK);
	}

}
