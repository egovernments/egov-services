package org.egov.egf.web.controller;

import java.util.ArrayList;
import java.util.Date;

import javax.validation.Valid;

import org.egov.egf.domain.exception.CustomBindException;
import org.egov.egf.persistence.entity.BankBranch;
import org.egov.egf.persistence.queue.contract.BankBranchContract;
import org.egov.egf.persistence.queue.contract.BankBranchContractRequest;
import org.egov.egf.persistence.queue.contract.BankBranchContractResponse;
import org.egov.egf.persistence.queue.contract.Pagination;
import org.egov.egf.persistence.queue.contract.RequestInfo;
import org.egov.egf.persistence.queue.contract.ResponseInfo;
import org.egov.egf.persistence.service.BankBranchService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/bankbranches")  
public class BankBranchController {
	@Autowired
	private BankBranchService  bankBranchService;

	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	public  BankBranchContractResponse create(@RequestBody @Valid BankBranchContractRequest bankBranchContractRequest, BindingResult errors) {
		ModelMapper modelMapper=new ModelMapper();
		bankBranchService.validate(bankBranchContractRequest,"create",errors);
		if (errors.hasErrors()) {
		  throw	new CustomBindException(errors);
		}
		
		BankBranchContractResponse bankBranchContractResponse = new BankBranchContractResponse();
		bankBranchContractResponse.setBankBranches(new ArrayList<BankBranchContract>());
		for(BankBranchContract bankBranchContract:bankBranchContractRequest.getBankBranches())
		{
		
		BankBranch	bankBranchEntity=	modelMapper.map(bankBranchContract, BankBranch.class);
		bankBranchEntity = bankBranchService.create(bankBranchEntity);
		BankBranchContract resp=modelMapper.map(bankBranchEntity, BankBranchContract.class);
		bankBranchContract.setId(bankBranchEntity.getId());
		bankBranchContractResponse.getBankBranches().add(resp);
		}

		bankBranchContractResponse.setResponseInfo(getResponseInfo(bankBranchContractRequest.getRequestInfo()));
		 
		return bankBranchContractResponse;
	}

	@PutMapping(value = "/{uniqueId}")
	@ResponseStatus(HttpStatus.OK)
	public BankBranchContractResponse update(@RequestBody @Valid BankBranchContractRequest bankBranchContractRequest, BindingResult errors,
			@PathVariable Long uniqueId) {
		
		bankBranchService.validate(bankBranchContractRequest,"update",errors);
		
		if (errors.hasErrors()) {
			  throw	new CustomBindException(errors);
			}
	 
		BankBranch bankBranchFromDb = bankBranchService.findOne(uniqueId);
		BankBranchContract bankBranch = bankBranchContractRequest.getBankBranch();
		
		ModelMapper model=new ModelMapper();
	 	model.map(bankBranch, bankBranchFromDb);
		bankBranchFromDb = bankBranchService.update(bankBranchFromDb);
		BankBranchContractResponse bankBranchContractResponse = new BankBranchContractResponse();
		bankBranchContractResponse.setBankBranch(bankBranch);  
		bankBranchContractResponse.setResponseInfo(getResponseInfo(bankBranchContractRequest.getRequestInfo()));
		bankBranchContractResponse.getResponseInfo().setStatus(HttpStatus.OK.toString());
		return bankBranchContractResponse;
	}
	
	@GetMapping(value = "/{uniqueId}")
	@ResponseStatus(HttpStatus.OK)
	public BankBranchContractResponse view(@RequestBody @Valid BankBranchContractRequest bankBranchContractRequest, BindingResult errors,
			@PathVariable Long uniqueId) {

		if (errors.hasErrors()) {
			  throw	new CustomBindException(errors);
			}
		bankBranchService.validate(bankBranchContractRequest,"view",errors);
		RequestInfo requestInfo = bankBranchContractRequest.getRequestInfo();
		BankBranch bankBranchFromDb = bankBranchService.findOne(uniqueId);
		BankBranchContract bankBranch = bankBranchContractRequest.getBankBranch();
		
		ModelMapper model=new ModelMapper();
	 	model.map(bankBranch, bankBranchFromDb);
		
		BankBranchContractResponse bankBranchContractResponse = new BankBranchContractResponse();
		bankBranchContractResponse.setBankBranch(bankBranch);  
		bankBranchContractResponse.setResponseInfo(getResponseInfo(bankBranchContractRequest.getRequestInfo()));
		bankBranchContractResponse.getResponseInfo().setStatus(HttpStatus.CREATED.toString());
		return bankBranchContractResponse ;
	}
	
	@PutMapping
	@ResponseStatus(HttpStatus.OK)
	public BankBranchContractResponse updateAll(@RequestBody @Valid BankBranchContractRequest bankBranchContractRequest, BindingResult errors) {
		bankBranchService.validate(bankBranchContractRequest,"updateAll",errors);
		if (errors.hasErrors()) {
			  throw	new CustomBindException(errors);
			}
		
 
		BankBranchContractResponse bankBranchContractResponse =new  BankBranchContractResponse();
		bankBranchContractResponse.setBankBranches(new ArrayList<BankBranchContract>());
		for(BankBranchContract bankBranchContract:bankBranchContractRequest.getBankBranches())
		{
		BankBranch bankBranchFromDb = bankBranchService.findOne(bankBranchContract.getId());
		 
		
		ModelMapper model=new ModelMapper();
	 	model.map(bankBranchContract, bankBranchFromDb);
		bankBranchFromDb = bankBranchService.update(bankBranchFromDb);
		model.map(bankBranchFromDb,bankBranchContract);
		bankBranchContractResponse.getBankBranches().add(bankBranchContract);  
		}

		bankBranchContractResponse.setResponseInfo(getResponseInfo(bankBranchContractRequest.getRequestInfo()));
		bankBranchContractResponse.getResponseInfo().setStatus(HttpStatus.OK.toString());
		
		return bankBranchContractResponse;
	}
	

	@GetMapping
	@ResponseBody
	@ResponseStatus(HttpStatus.OK)
	public BankBranchContractResponse search(@ModelAttribute BankBranchContractRequest bankBranchContractRequest,BindingResult errors) {
		bankBranchService.validate(bankBranchContractRequest,"search",errors);
		if (errors.hasErrors()) {
			  throw	new CustomBindException(errors);
			}
		BankBranchContractResponse bankBranchContractResponse =new  BankBranchContractResponse();
		bankBranchContractResponse.setBankBranches(new ArrayList<BankBranchContract>());
		bankBranchContractResponse.setPage(new Pagination());
		Page<BankBranch> allBankBranches;
		ModelMapper model=new ModelMapper();
	 
		allBankBranches = bankBranchService.search(bankBranchContractRequest);
		BankBranchContract bankBranchContract=null;
		for(BankBranch b:allBankBranches)
		{
			bankBranchContract=new BankBranchContract();
			model.map(b, bankBranchContract);
			bankBranchContractResponse.getBankBranches().add(bankBranchContract);
		}
		bankBranchContractResponse.getPage().map(allBankBranches);
		bankBranchContractResponse.setResponseInfo(getResponseInfo(bankBranchContractRequest.getRequestInfo()));
		bankBranchContractResponse.getResponseInfo().setStatus(HttpStatus.OK.toString());
		return bankBranchContractResponse;
	}

	
	private ResponseInfo getResponseInfo(RequestInfo requestInfo) {
        new ResponseInfo();
		return ResponseInfo.builder()
                .apiId(requestInfo.getApiId())
                .ver(requestInfo.getVer())
                .ts(new Date())
                .resMsgId(requestInfo.getMsgId())
                .resMsgId("placeholder")
                .status("placeholder")
                .build();
    }

}