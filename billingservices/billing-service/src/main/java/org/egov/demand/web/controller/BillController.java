package org.egov.demand.web.controller;

import org.egov.common.contract.request.RequestInfo;
import org.egov.demand.model.GenerateBillCriteria;
import org.egov.demand.service.BillService;
import org.egov.demand.web.contract.BillRequest;
import org.egov.demand.web.contract.BillResponse;
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

@RestController
public class BillController {
	
	@Autowired
	private BillService billService;
	
	@Autowired
	private BillValidator billValidator;
	
	@PostMapping("_create")
	@ResponseBody
	public ResponseEntity<?> create(@RequestBody BillRequest billRequest, BindingResult bindingResult){
		
		/*if (bindingResult.hasErrors()) {
			ErrorResponse errorResponse = populateErrors(bindingResult);
			return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
		}*/
		billValidator.validateBillRequest(billRequest);
		BillResponse billResponse = billService.createAsync(billRequest);
		
		return new ResponseEntity<>(billResponse,HttpStatus.CREATED);
	}
	
	@PostMapping("_generate")
	@ResponseBody
	public ResponseEntity<?> genrateBill(@RequestBody RequestInfo requestInfo, 
			@ModelAttribute GenerateBillCriteria generateBillCriteria, BindingResult bindingResult){
		
		/*if (bindingResult.hasErrors()) {
			ErrorResponse errorResponse = populateErrors(bindingResult);
			return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
		}*/
		//billValidator.validateBillRequest(billRequest);
		BillResponse billResponse = billService.generateBill(generateBillCriteria, requestInfo);
		
		return new ResponseEntity<>(billResponse,HttpStatus.CREATED);
	}
	
	
	
	
	
	
}
