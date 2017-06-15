package org.egov.demand.web.controller;

import org.egov.demand.service.BillService;
import org.egov.demand.web.contract.BillRequest;
import org.egov.demand.web.contract.BillResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class BillController {
	
	@Autowired
	private BillService billService;
	
	@PostMapping("_create")
	@ResponseBody
	public ResponseEntity<?> create(@RequestBody BillRequest billRequest, BindingResult bindingResult){
		
		/*if (bindingResult.hasErrors()) {
			ErrorResponse errorResponse = populateErrors(bindingResult);
			return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
		}*/
		BillResponse billResponse = billService.createAsync(billRequest);
		
		return new ResponseEntity<>(billResponse,HttpStatus.CREATED);
	}
	
	
	
	
}
