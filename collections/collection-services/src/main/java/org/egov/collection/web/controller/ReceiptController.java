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
import org.egov.collection.model.ReceiptSearchCriteria;
import org.egov.collection.service.ReceiptService;
import org.egov.collection.web.contract.Receipt;
import org.egov.collection.web.contract.ReceiptReq;
import org.egov.collection.web.contract.ReceiptRes;
import org.egov.collection.web.contract.factory.ResponseInfoFactory;
import org.egov.collection.web.errorhandlers.Error;
import org.egov.collection.web.errorhandlers.ErrorResponse;
import org.egov.common.contract.request.RequestInfo;
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
@RequestMapping("receipt")
public class ReceiptController {
	public static final Logger LOGGER = LoggerFactory
			.getLogger(ReceiptController.class);

	@Autowired
	private ReceiptService receiptService;

	private ResponseInfoFactory responseInfoFactory;

	@PostMapping("_search")
	@ResponseBody
	public ResponseEntity<?> search(
			@ModelAttribute @Valid ReceiptSearchCriteria receiptSearchCriteria,
			@RequestBody RequestInfo requestInfo, BindingResult bindingResult) {

		if (bindingResult.hasErrors()) {
			ErrorResponse errorResponse = populateErrors(bindingResult);
			return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
		}

		LOGGER.info("AgreementController:getAgreements():searchAgreementsModel:"
				+ receiptSearchCriteria);
		//TODO: FIX ME: Need to map the Model to Contract to send back the response
		
		List receipts = receiptService.getReceipts(receiptSearchCriteria);
		
		//TODO: FIX ME: Catching the response in a interface ref variable is not right. 
		//Please create the instance of a concrete class like ArrayList<>() and then catch if you feel my observation is correct.
		if (receipts.isEmpty())
			try {
				throw new Exception("No receipts found for the given criteria");
			} catch (Exception e) {
				LOGGER.error("before sending for response su7ccess");
			}
		return getSuccessResponse(receipts, requestInfo);
	}

	private ResponseEntity<?> getSuccessResponse(List<Receipt> receipts,
			RequestInfo requestInfo) {
		ReceiptRes receiptResponse = new ReceiptRes();
		// receiptResponse.setReceiptInfo(receipts);
		receiptResponse.setResponseInfo(responseInfoFactory
				.createResponseInfoFromRequestInfo(requestInfo, true));
		System.err.println("before returning from getsucces resposne ::"
				+ receiptResponse);
		return new ResponseEntity<>(receiptResponse, HttpStatus.OK);
	}

	@PostMapping("/_create")
	@ResponseBody
	public ResponseEntity<?> create(
			@RequestBody @Valid ReceiptReq receiptRequest, BindingResult errors) {

		if (errors.hasFieldErrors()) {
			ErrorResponse errRes = populateErrors(errors);
			return new ResponseEntity<>(errRes, HttpStatus.BAD_REQUEST);
		}
		
		Receipt receiptInfo = receiptService.pushToQueue(receiptRequest.getReceiptInfo());
		
		if(null == receiptInfo){
			Error error = new Error();
			error.setMessage(CollectionServiceConstants.INVALID_RECEIPT_REQUEST);
			
			ErrorResponse errorResponse = new ErrorResponse();
			errorResponse.setError(error);
			
			return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
			
		}
		
		List<Receipt> receipts = new ArrayList<>();
		receipts.add(receiptInfo);
		
		return getSuccessResponse(receipts, receiptRequest.getRequestInfo());
	}

	@PostMapping("_update/{code}")
	@ResponseBody
	public ResponseEntity<?> update(@RequestBody ReceiptReq receiptRequest,
			BindingResult bindingResult) {

		if (bindingResult.hasErrors()) {
			ErrorResponse errorResponse = populateErrors(bindingResult);
			return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
		}

		return null;
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
				error.getFields()
						.put(errs.getField(), errs.getDefaultMessage());
			}
		}
		errRes.setError(error);
		return errRes;
	}
}
