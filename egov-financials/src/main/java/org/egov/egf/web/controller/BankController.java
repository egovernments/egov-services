package org.egov.egf.web.controller;

import java.util.List;

import javax.validation.Valid;

import org.egov.egf.entity.BankEntity;
import org.egov.egf.service.BankService;
import org.egov.egf.web.contract.Bank;
import org.egov.egf.web.contract.BankRequest;
import org.egov.egf.web.contract.BankResponse;
import org.egov.egf.web.contract.ErrorResponse;
import org.egov.egf.web.contract.RequestInfo;
import org.egov.egf.web.contract.ResponseInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.egov.egf.web.contract.Error;

@RestController
@RequestMapping("/banks")
public class BankController {
	@Autowired
	private BankService bankService;

	@PostMapping
	@ResponseBody
	public ResponseEntity<?> create(@RequestBody @Valid BankRequest bankRequest, BindingResult errors) {

		if (errors.hasErrors()) {
			ErrorResponse errRes = populateErrors(errors);
			return new ResponseEntity<ErrorResponse>(errRes, HttpStatus.BAD_REQUEST);
		}
		RequestInfo requestInfo = bankRequest.getRequestInfo();
		Bank bank = bankRequest.getBank();
		BankEntity bankEntity=new BankEntity();
		bankEntity.map(bank);
		bankEntity = bankService.create(bankEntity);

		BankResponse bankResponse = new BankResponse();
		bankResponse.getBanks().add(bank);

		ResponseInfo responseInfo = new ResponseInfo();
		responseInfo.setStatus(HttpStatus.CREATED.toString());
		responseInfo.setApi_id(requestInfo.getApi_id());
		bankResponse.setResponseInfo(responseInfo);
		return new ResponseEntity<BankResponse>(bankResponse, HttpStatus.CREATED);
	}

	@PutMapping(value = "/{code}")
	@ResponseBody
	public ResponseEntity<?> update(@RequestBody @Valid BankRequest bankRequest, BindingResult errors,
			@PathVariable String code) {

		if (errors.hasErrors()) {
			ErrorResponse errRes = populateErrors(errors);
			return new ResponseEntity<ErrorResponse>(errRes, HttpStatus.BAD_REQUEST);
		}
		RequestInfo requestInfo = bankRequest.getRequestInfo();
		BankEntity bankFromDb = bankService.findByCode(code);
		Bank bank = bankRequest.getBank();
		bankFromDb.map(bank);
		bankFromDb = bankService.update(bankFromDb);

		BankResponse bankResponse = new BankResponse();
		bankResponse.getBanks().add(bank);  

		ResponseInfo responseInfo = new ResponseInfo();
		responseInfo.setStatus(HttpStatus.CREATED.toString());
		responseInfo.setApi_id(requestInfo.getApi_id());
		bankResponse.setResponseInfo(responseInfo);
		return new ResponseEntity<BankResponse>(bankResponse, HttpStatus.CREATED);
	}

	@GetMapping
	@ResponseBody
	public ResponseEntity<?> search(@ModelAttribute BankRequest bankRequest) {

		BankResponse bankResponse = new BankResponse();
		BankEntity bankEntity=new BankEntity();
		bankEntity.map(bankRequest.getBank());
		List<BankEntity> allBanks = bankService.findAll();
		//bankResponse.getBanks().addAll(bankRequest.getBank());
		Bank bank=null;
		for(BankEntity b:allBanks)
		{
			bank=new Bank();
			b.mapContract(bank);
			bankResponse.getBanks().add(bank);
		}
		ResponseInfo responseInfo = new ResponseInfo();
		responseInfo.setStatus(HttpStatus.CREATED.toString());
		bankResponse.setResponseInfo(responseInfo);
		return new ResponseEntity<BankResponse>(bankResponse, HttpStatus.OK);
	}

	private ErrorResponse populateErrors(BindingResult errors) {
		ErrorResponse errRes = new ErrorResponse();

		ResponseInfo responseInfo = new ResponseInfo();
		responseInfo.setStatus(HttpStatus.BAD_REQUEST.toString());
		responseInfo.setApi_id("");
		errRes.setResponseInfo(responseInfo);
		Error error = new Error();
		error.setCode(1);
		error.setDescription("Error while binding request");
		if (errors.hasFieldErrors()) {
			for (FieldError errs : errors.getFieldErrors()) {
				error.getFilelds().add(errs.getField());
				error.getFilelds().add(errs.getRejectedValue());
			}
		}
		errRes.setError(error);
		return errRes;
	}

}