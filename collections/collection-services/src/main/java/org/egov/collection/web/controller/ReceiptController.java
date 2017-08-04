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

package org.egov.collection.web.controller;

import java.util.ArrayList;
import java.util.List;

import javax.validation.Valid;

import org.egov.collection.config.CollectionServiceConstants;
import org.egov.collection.exception.CustomException;
import org.egov.collection.model.ReceiptSearchCriteria;
import org.egov.collection.service.ReceiptService;
import org.egov.collection.service.WorkflowService;
import org.egov.collection.util.ReceiptReqValidator;
import org.egov.collection.web.contract.Receipt;
import org.egov.collection.web.contract.ReceiptReq;
import org.egov.collection.web.contract.ReceiptRes;
import org.egov.collection.web.contract.ReceiptSearchGetRequest;
import org.egov.collection.web.contract.WorkFlowDetailsResponse;
import org.egov.collection.web.contract.WorkflowDetailsRequest;
import org.egov.collection.web.contract.factory.RequestInfoWrapper;
import org.egov.collection.web.contract.factory.ResponseInfoFactory;
import org.egov.collection.web.errorhandlers.Error;
import org.egov.collection.web.errorhandlers.ErrorHandler;
import org.egov.collection.web.errorhandlers.ErrorResponse;
import org.egov.common.contract.request.RequestInfo;
import org.egov.common.contract.response.ResponseInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/receipts")
public class ReceiptController {
	public static final Logger LOGGER = LoggerFactory.getLogger(ReceiptController.class);

	@Autowired
	private ReceiptService receiptService;

	@Autowired
	private ReceiptReqValidator receiptReqValidator;

	@Autowired
	private ResponseInfoFactory responseInfoFactory;

	@Autowired
	private ErrorHandler errHandler;

	@Autowired
	private WorkflowService workFlowService;

	@PostMapping("/_search")
	@ResponseBody
	public ResponseEntity<?> search(@ModelAttribute @Valid ReceiptSearchGetRequest receiptGetRequest,
			final BindingResult modelAttributeBindingResult,
			@RequestBody @Valid final RequestInfoWrapper requestInfoWrapper,
			final BindingResult requestBodyBindingResult) {

		ReceiptSearchCriteria searchCriteria = ReceiptSearchCriteria.builder()
				.businessCode(receiptGetRequest.getBusinessCode()).classification(receiptGetRequest.getClassification())
				.collectedBy(receiptGetRequest.getCollectedBy()).consumerCode(receiptGetRequest.getConsumerCode())
				.fromDate(receiptGetRequest.getFromDate()).toDate(receiptGetRequest.getToDate())
				.paymentType(receiptGetRequest.getPaymentType()).receiptNumbers(receiptGetRequest.getReceiptNumbers())
				.status(receiptGetRequest.getStatus()).tenantId(receiptGetRequest.getTenantId())
				.sortBy(receiptGetRequest.getSortBy()).sortOrder(receiptGetRequest.getSortOrder()).build();

		final RequestInfo requestInfo = requestInfoWrapper.getRequestInfo();

		if (modelAttributeBindingResult.hasErrors())
			return errHandler.getErrorResponseEntityForMissingParameters(modelAttributeBindingResult, requestInfo);
		if (requestBodyBindingResult.hasErrors())
			return errHandler.getErrorResponseEntityForMissingRequestInfo(requestBodyBindingResult, requestInfo);
		List<Receipt> receipts = new ArrayList<>();
		try {
			receipts = receiptService.getReceipts(searchCriteria).toDomainContract();
		} catch (final Exception exception) {
			LOGGER.error("Error while processing request " + receiptGetRequest, exception);
			return errHandler.getResponseEntityForUnexpectedErrors(requestInfo);
		}
		return getSuccessResponse(receipts, requestInfo);
	}

	@PostMapping("/_view")
	@ResponseBody
	public List<Receipt> view(@ModelAttribute @Valid ReceiptSearchGetRequest receiptGetRequest) {

		ReceiptSearchCriteria searchCriteria = ReceiptSearchCriteria.builder()
				.businessCode(receiptGetRequest.getBusinessCode()).classification(receiptGetRequest.getClassification())
				.collectedBy(receiptGetRequest.getCollectedBy()).consumerCode(receiptGetRequest.getConsumerCode())
				.fromDate(receiptGetRequest.getFromDate()).toDate(receiptGetRequest.getToDate())
				.paymentType(receiptGetRequest.getPaymentType()).receiptNumbers(receiptGetRequest.getReceiptNumbers())
				.status(receiptGetRequest.getStatus()).tenantId(receiptGetRequest.getTenantId())
				.sortBy(receiptGetRequest.getSortBy()).sortOrder(receiptGetRequest.getSortOrder()).build();

		List<Receipt> receipts = new ArrayList<>();
		try {
			receipts = receiptService.getReceipts(searchCriteria).toDomainContract();
		} catch (final Exception exception) {
			LOGGER.error("Error while processing request " + receiptGetRequest, exception);
		}
		return receipts;
	}

	@PostMapping("/_cancel")
	@ResponseBody
	public ResponseEntity<?> cancelReceipt(@RequestBody @Valid ReceiptReq receiptRequest, BindingResult errors) {
		if (errors.hasFieldErrors()) {
			ErrorResponse errRes = populateErrors(errors);
			return new ResponseEntity<>(errRes, HttpStatus.BAD_REQUEST);
		}

		final List<ErrorResponse> errorResponses = receiptReqValidator.validatecreateReceiptRequest(receiptRequest);
		if (!errorResponses.isEmpty())
			return new ResponseEntity<>(errorResponses, HttpStatus.BAD_REQUEST);

		List<Receipt> receipt = receiptService.cancelReceiptPushToQueue(receiptRequest);
		return getSuccessResponse(receipt, receiptRequest.getRequestInfo());
	}

	@PostMapping("/_create")
	@ResponseBody
	public ResponseEntity<?> create(@RequestBody @Valid ReceiptReq receiptRequest, BindingResult errors) {

		if (errors.hasFieldErrors()) {
			ErrorResponse errRes = populateErrors(errors);
			return new ResponseEntity<>(errRes, HttpStatus.BAD_REQUEST);
		}
		LOGGER.info("Request: " + receiptRequest.toString());
		final List<ErrorResponse> errorResponses = receiptReqValidator.validatecreateReceiptRequest(receiptRequest);
		if (!errorResponses.isEmpty())
			return new ResponseEntity<>(errorResponses, HttpStatus.BAD_REQUEST);
		Receipt receiptInfo = null;
		try{
			receiptInfo = receiptService.apportionAndCreateReceipt(receiptRequest);
		}catch(CustomException e){
			LOGGER.info("Exception Message: "+e.getCustomMessage());
			Error error = new Error();
			final ResponseInfo responseInfo = responseInfoFactory.createResponseInfoFromRequestInfo(receiptRequest.getRequestInfo(), true);
			error.setCode(Integer.valueOf(e.getCode().toString()));
			error.setMessage(e.getCustomMessage());
			error.setDescription(e.getDescription());
			ErrorResponse errorResponse = new ErrorResponse();
			errorResponse.setError(error);
			errorResponse.setResponseInfo(responseInfo);

			return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);	
		}

		List<Receipt> receipts = new ArrayList<>();
		receipts.add(receiptInfo);

		return getSuccessResponse(receipts, receiptRequest.getRequestInfo());
	}

	@PostMapping("/_update")
	@ResponseBody
	public ResponseEntity<?> update(@RequestBody @Valid WorkflowDetailsRequest workFlowRequest, BindingResult errors) {

		if (errors.hasFieldErrors()) {
			ErrorResponse errRes = populateErrors(errors);
			return new ResponseEntity<>(errRes, HttpStatus.BAD_REQUEST);
		}
		if (!validator(workFlowRequest.getTenantId(), workFlowRequest.getReceiptHeaderId())) {
			LOGGER.info("Invalid TenantId");
			Error error = new Error();
			error.setCode(Integer.parseInt(HttpStatus.BAD_REQUEST.toString()));
			error.setMessage(CollectionServiceConstants.TENANT_ID_MISSING_MESSAGE);
			ErrorResponse errorResponse = new ErrorResponse();
			errorResponse.setError(error);

			return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
		}
		WorkflowDetailsRequest workFlowDetailsRequest = workFlowService.update(workFlowRequest);

		return getSuccessResponseForUpdate(workFlowDetailsRequest);
	}

	private ResponseEntity<?> getSuccessResponseForUpdate(WorkflowDetailsRequest workFlowDetailsRequest) {
		LOGGER.info("Building success response.");
		WorkFlowDetailsResponse workFlowDetailsResponse = new WorkFlowDetailsResponse();
		final ResponseInfo responseInfo = responseInfoFactory
				.createResponseInfoFromRequestInfo(workFlowDetailsRequest.getRequestInfo(), true);
		responseInfo.setStatus(HttpStatus.OK.toString());
		workFlowDetailsResponse.setResponseInfo(responseInfo);
		workFlowDetailsResponse.setTenantId(workFlowDetailsRequest.getTenantId());
		workFlowDetailsResponse.setReceiptNumber(workFlowDetailsRequest.getReceiptNumber());
		workFlowDetailsResponse.setBusinessKey(workFlowDetailsRequest.getBusinessKey());
		workFlowDetailsResponse.setAction(workFlowDetailsRequest.getAction());
		workFlowDetailsResponse.setAssignee(workFlowDetailsRequest.getAssignee());
		workFlowDetailsResponse.setComments(workFlowDetailsRequest.getComments());
		workFlowDetailsResponse.setDepartment(workFlowDetailsRequest.getDepartment());
		workFlowDetailsResponse.setDesignation(workFlowDetailsRequest.getDesignation());
		workFlowDetailsResponse.setInitiatorPosition(workFlowDetailsRequest.getInitiatorPosition());
		workFlowDetailsResponse.setReceiptHeaderId(workFlowDetailsRequest.getReceiptHeaderId());
		workFlowDetailsResponse.setState(workFlowDetailsRequest.getState());
		workFlowDetailsResponse.setStateId(workFlowDetailsRequest.getStateId());
		workFlowDetailsResponse.setStatus(workFlowDetailsRequest.getStatus());
		workFlowDetailsResponse.setUser(workFlowDetailsRequest.getUser());
		return new ResponseEntity<>(workFlowDetailsResponse, HttpStatus.OK);

	}

	private boolean validator(String tenantId, long receiptHeaderId) {
		boolean isTenantValid = true;
		if (null == tenantId || tenantId.isEmpty())
			isTenantValid = false;
		else if (receiptHeaderId == 0L)
			isTenantValid = false;

		return isTenantValid;
	}

	private ResponseEntity<?> getSuccessResponse(List<Receipt> receipts, RequestInfo requestInfo) {
		LOGGER.info("Building success response.");
		ReceiptRes receiptResponse = new ReceiptRes();
		final ResponseInfo responseInfo = responseInfoFactory.createResponseInfoFromRequestInfo(requestInfo, true);
		responseInfo.setStatus(HttpStatus.OK.toString());
		receiptResponse.setReceipts(receipts);
		receiptResponse.setResponseInfo(responseInfo);
		return new ResponseEntity<>(receiptResponse, HttpStatus.OK);
	}

	private ErrorResponse populateErrors(BindingResult errors) {
		ErrorResponse errRes = new ErrorResponse();

		/*
		 * ResponseInfo responseInfo = new ResponseInfo();
		 * responseInfo.setStatus(HttpStatus.BAD_REQUEST.toString());
		 * responseInfo.setApi_id(""); errRes.setResponseInfo(responseInfo);
		 */

		Error error = new Error();
		error.setCode(1);
		error.setDescription("Error while binding request");
		if (errors.hasFieldErrors()) {
			for (FieldError errs : errors.getFieldErrors()) {
				error.getFields().put(errs.getField(), errs.getDefaultMessage());
			}
		}
		errRes.setError(error);
		return errRes;
	}
}
