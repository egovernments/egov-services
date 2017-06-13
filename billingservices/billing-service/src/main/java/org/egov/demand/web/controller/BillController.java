package org.egov.demand.web.controller;

import java.util.ArrayList;
import java.util.List;

import org.egov.demand.model.Bill;
import org.egov.demand.service.BillService;
import org.egov.demand.web.contract.BillRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
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
		System.out.println("billInfo:"+billRequest);
		billService.createAsync(billRequest);
		
		return null;
	}
	
	@GetMapping("_search")
	@ResponseBody
	public ResponseEntity<?> get(){
		
		/*if (bindingResult.hasErrors()) {
			ErrorResponse errorResponse = populateErrors(bindingResult);
			return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
		}*/
		Bill billInfo =new Bill();
		BillRequest billRequest =new BillRequest();
		List<Bill> billInfos = new ArrayList<>();
		billInfos.add(billInfo);
		billRequest.setBillInfos(billInfos);
		System.out.println("billInfo:"+billInfo);
		//billService.createAsync(billInfo);
		
		return new ResponseEntity<>(billRequest, HttpStatus.OK);
	}
	
	/*private ErrorResponse populateErrors(BindingResult errors) {
		ErrorResponse errRes = new ErrorResponse();

		ResponseInfo responseInfo = new ResponseInfo();
		responseInfo.setStatus(HttpStatus.BAD_REQUEST.toString());
		errRes.setResponseInfo(responseInfo);

		Error error = new Error();
		error.setCode(1);
		error.setDescription("Error while binding request");
		if (errors.hasFieldErrors()) {
			for (FieldError errs : errors.getFieldErrors()) {
				error.getFields().put(errs.getField(), errs.getRejectedValue());
			}
		}
		errRes.setError(error);
		return errRes;
	}*/

}
