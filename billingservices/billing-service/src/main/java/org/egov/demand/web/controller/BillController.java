package org.egov.demand.web.controller;

import java.util.ArrayList;
import java.util.List;

import javax.validation.Valid;

import org.egov.common.contract.request.RequestInfo;
import org.egov.common.contract.response.Error;
import org.egov.common.contract.response.ErrorField;
import org.egov.common.contract.response.ErrorResponse;
import org.egov.common.contract.response.ResponseInfo;
import org.egov.demand.model.BillSearchCriteria;
import org.egov.demand.model.GenerateBillCriteria;
import org.egov.demand.service.BillService;
import org.egov.demand.web.contract.BillRequest;
import org.egov.demand.web.contract.BillResponse;
import org.egov.demand.web.contract.RequestInfoWrapper;
import org.egov.demand.web.contract.factory.ResponseFactory;
import org.egov.demand.web.validator.BillValidator;
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

import lombok.extern.slf4j.Slf4j;

@RestController
@Slf4j
@RequestMapping("bill")
public class BillController {
	
	@Autowired
	private BillService billService;
	
	@Autowired
	private BillValidator billValidator;
	
	@Autowired
	private ResponseFactory responseFactory;
	
	@PostMapping("_search")
	@ResponseBody
	public ResponseEntity<?> search(@RequestBody @Valid final RequestInfoWrapper requestInfoWrapper,
			@ModelAttribute @Valid final BillSearchCriteria billCriteria,
			final BindingResult bindingResult) {
		billValidator.validateBillSearchCriteria(billCriteria, bindingResult);
		RequestInfo requestInfo = requestInfoWrapper.getRequestInfo();
		if (bindingResult.hasErrors()) {
			final ErrorResponse errorResponse = responseFactory.getErrorResponse(bindingResult, requestInfo);
			return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
		}
		
		return new ResponseEntity<>(billService.searchBill(billCriteria,requestInfo), HttpStatus.OK);
	}

	@PostMapping("_create")
	@ResponseBody
	public ResponseEntity<?> create(@RequestBody BillRequest billRequest, BindingResult bindingResult){

		log.debug("create billRequest:"+billRequest);
		
		if (bindingResult.hasErrors()) {
			return new ResponseEntity<>(responseFactory.
					getErrorResponse(bindingResult, billRequest.getRequestInfo()), HttpStatus.BAD_REQUEST);
		}
		billValidator.validateBillRequest(billRequest);
		BillResponse billResponse = billService.createAsync(billRequest);
		
		return new ResponseEntity<>(billResponse,HttpStatus.CREATED);
	}
	
	@PostMapping("_generate")
	@ResponseBody
	public ResponseEntity<?> genrateBill(@RequestBody RequestInfoWrapper requestInfoWrapper, 
			@ModelAttribute @Valid GenerateBillCriteria generateBillCriteria, BindingResult bindingResult){
		log.debug("genrateBill generateBillCriteria : "+generateBillCriteria);
		log.debug("genrateBill requestInfoWrapper : "+requestInfoWrapper);
		
		if (bindingResult.hasErrors()) {
			return new ResponseEntity<>(responseFactory.
					getErrorResponse(bindingResult, requestInfoWrapper.getRequestInfo()), HttpStatus.BAD_REQUEST);
		}
		billValidator.validateBillGenRequest(generateBillCriteria,bindingResult);
		if (bindingResult.hasErrors()) {
			return new ResponseEntity<>(responseFactory.
					getErrorResponse(bindingResult, requestInfoWrapper.getRequestInfo()), HttpStatus.BAD_REQUEST);
		}
		BillResponse billResponse = billService.generateBill(generateBillCriteria, requestInfoWrapper.getRequestInfo());
		
		if (billResponse.getBill() != null) {
			return new ResponseEntity<>(billResponse, HttpStatus.CREATED);
		} else {
			Error error = new Error();
			error.setCode(400);
			error.setMessage("The due to be paid is zero");
			error.setDescription(
					"bill Cannot be generated because the amount to be collected on the respective bill is zero");
			List<ErrorField> errorFields = new ArrayList<>();
			error.setFields(errorFields);

			ErrorField errorField = new ErrorField(HttpStatus.BAD_REQUEST.toString(), error.getMessage(), "");
			error.getFields().add(errorField);
			errorFields.add(errorField);
			ResponseInfo responseInfo = responseFactory.getResponseInfo(requestInfoWrapper.getRequestInfo(),
					HttpStatus.BAD_REQUEST);
			return new ResponseEntity<>(new ErrorResponse(responseInfo, error), HttpStatus.BAD_REQUEST);
		}
	}

	
	@PostMapping("_apportion")
	@ResponseBody
	public ResponseEntity<?> apportion(@RequestBody BillRequest billRequest, BindingResult bindingResult) {

		RequestInfo requestInfo = billRequest.getRequestInfo();
		log.debug("genrateBill requestInfo : " + requestInfo);

		if (bindingResult.hasErrors()) {
			return new ResponseEntity<>(responseFactory.getErrorResponse(bindingResult, requestInfo),
					HttpStatus.BAD_REQUEST);
		}
		BillResponse billResponse = billService.apportion(billRequest);
		return new ResponseEntity<>(billResponse, HttpStatus.CREATED);
	}
}
