package org.egov.egf.web.controller;

import java.util.List;

import javax.validation.Valid;

import org.egov.egf.persistence.entity.Bank;
import org.egov.egf.persistence.queue.contract.BankContract;
import org.egov.egf.persistence.queue.contract.BankContractRequest;
import org.egov.egf.persistence.queue.contract.BankContractResponse;
import org.egov.egf.persistence.queue.contract.ErrorResponse;
import org.egov.egf.persistence.queue.contract.RequestInfo;
import org.egov.egf.persistence.queue.contract.ResponseInfo;
import org.egov.egf.persistence.service.BankService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.egov.egf.persistence.queue.contract.Error;

@RestController
@RequestMapping("/banks")
public class BankController {
	@Autowired
	private BankService bankService;
 
	

	@PostMapping
	public ResponseEntity<?> create(@RequestBody @Valid BankContractRequest bankRequest, BindingResult errors) {
		ModelMapper modelMapper=new ModelMapper();
		if (errors.hasErrors()) {
			ErrorResponse errRes = populateErrors(errors);
			return new ResponseEntity<ErrorResponse>(errRes, HttpStatus.BAD_REQUEST);
		}
		
		RequestInfo requestInfo = bankRequest.getRequestInfo();
		BankContract bankContract = bankRequest.getBanks().get(0);
	//	Bank bankEntity=new Bank();
	//	bankEntity.map(bankContract);
		
		 
		Bank	bankEntity=	modelMapper.map(bankContract, Bank.class);
		bankEntity = bankService.create(bankEntity);
		BankContract resp=modelMapper.map(bankEntity, BankContract.class);
		bankContract.setId(bankEntity.getId());
		BankContractResponse BankContractResponse = new BankContractResponse();
		BankContractResponse.getBanks().add(resp);

		ResponseInfo responseInfo = new ResponseInfo();
		responseInfo.setStatus(HttpStatus.CREATED.toString());
	//	responseInfo.setApi_id(requestInfo.getApi_id());
		BankContractResponse.setResponseInfo(responseInfo);
		return new ResponseEntity<BankContractResponse>(BankContractResponse, HttpStatus.CREATED);
	}

	@PutMapping(value = "/{code}")
 
	public ResponseEntity<?> update(@RequestBody @Valid BankContractRequest bankRequest, BindingResult errors,
			@PathVariable String code) {

		if (errors.hasErrors()) {
			ErrorResponse errRes = populateErrors(errors);
			return new ResponseEntity<ErrorResponse>(errRes, HttpStatus.BAD_REQUEST);
		}
		
		RequestInfo requestInfo = bankRequest.getRequestInfo();
		Bank bankFromDb = bankService.findByCode(code);
		BankContract bank = bankRequest.getBanks().get(0);
		
		ModelMapper model=new ModelMapper();
	 	model.map(bank, bankFromDb);
		bankFromDb = bankService.update(bankFromDb);
		BankContractResponse BankContractResponse = new BankContractResponse();
		BankContractResponse.getBanks().add(bank);  

		ResponseInfo responseInfo = new ResponseInfo();
		responseInfo.setStatus(HttpStatus.CREATED.toString());
	//	responseInfo.setApi_id(requestInfo.getApi_id());
		BankContractResponse.setResponseInfo(responseInfo);
		return new ResponseEntity<BankContractResponse>(BankContractResponse, HttpStatus.CREATED);
	}

	@GetMapping
	@ResponseBody
	public ResponseEntity<?> search(@ModelAttribute BankContractRequest bankRequest) {

		BankContractResponse BankContractResponse =new  BankContractResponse();
		List<Bank> allBanks;
		Bank bankEntity=new Bank();
		ModelMapper model=new ModelMapper();
		if(bankRequest.getBank()!=null)
		{
		model.map(bankRequest.getBank(), bankEntity);
		
		allBanks = bankService.search(bankRequest.getBank());
		}else
		{
			  allBanks = bankService.findAll();
		}
		//BankContractResponse.getBanks().addAll(bankRequest.getBank());
		BankContract bank=null;
		for(Bank b:allBanks)
		{
			bank=new BankContract();
			model.map(b, bank);
			BankContractResponse.getBanks().add(bank);
		}
		ResponseInfo responseInfo = new ResponseInfo();
		responseInfo.setStatus(HttpStatus.CREATED.toString());
		BankContractResponse.setResponseInfo(responseInfo);
		return new ResponseEntity<BankContractResponse>(BankContractResponse, HttpStatus.OK);
	}

	private ErrorResponse populateErrors(BindingResult errors) {
		ErrorResponse errRes = new ErrorResponse();

		ResponseInfo responseInfo = new ResponseInfo();
		responseInfo.setStatus(HttpStatus.BAD_REQUEST.toString());
		errRes.setResponseInfo(responseInfo);
		Error error = new Error();
		error.setCode(1);
		error.setDescription("Error while binding request");
		/*if (errors.hasFieldErrors()) {
			for (FieldError errs : errors.getFieldErrors()) {
				FieldError f=new FieldError();
				error.getFilelds().add(errs.getField());
				error.getFilelds().add(errs.getRejectedValue());
			}
		}*/
		errRes.setError(error);
		return errRes;
	}

}