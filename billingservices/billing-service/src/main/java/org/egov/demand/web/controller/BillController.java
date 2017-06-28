package org.egov.demand.web.controller;

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
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import lombok.extern.slf4j.Slf4j;

@RestController
@Slf4j
//TODO path not defined
public class BillController {
	
	@Autowired
	private BillService billService;
	
	@Autowired
	private BillValidator billValidator;
	
	@Autowired
	private ResponseFactory responseFactory;

	//TODO: Documentation is required for all the methods. This applies everywhere (controller, service, etc)

	@PostMapping("_create")
	@ResponseBody
	public ResponseEntity<?> create(@RequestBody BillRequest billRequest, BindingResult bindingResult){
		//TODO: try to give debug loggers instead info, this applied across the code base.
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
			@ModelAttribute GenerateBillCriteria generateBillCriteria, BindingResult bindingResult){
		log.debug("genrateBill generateBillCriteria : "+generateBillCriteria);
		log.debug("genrateBill requestInfoWrapper : "+requestInfoWrapper);
		
		if (bindingResult.hasErrors()) {
			return new ResponseEntity<>(responseFactory.
					getErrorResponse(bindingResult, requestInfoWrapper.getRequestInfo()), HttpStatus.BAD_REQUEST);
		}
		//billValidator.validateBillRequest(billRequest);
		BillResponse billResponse = billService.generateBill(generateBillCriteria, requestInfoWrapper.getRequestInfo());
		
		return new ResponseEntity<>(billResponse,HttpStatus.CREATED);
	}
	
	
	
	
	
	
}
