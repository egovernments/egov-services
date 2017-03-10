package org.egov.egf.web.controller;

import java.util.ArrayList;
import java.util.Date;

import javax.validation.Valid;

import org.egov.egf.domain.exception.CustomBindException;
import org.egov.egf.persistence.entity.Bank;
import org.egov.egf.persistence.queue.contract.BankContract;
import org.egov.egf.persistence.queue.contract.BankContractRequest;
import org.egov.egf.persistence.queue.contract.BankContractResponse;
import org.egov.egf.persistence.queue.contract.Pagination;
import org.egov.egf.persistence.queue.contract.RequestInfo;
import org.egov.egf.persistence.queue.contract.ResponseInfo;
import org.egov.egf.persistence.service.BankService;
import org.modelmapper.ModelMapper;
import org.modelmapper.builder.SkipExpression;
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
@RequestMapping("/banks")  
public class BankController {
	@Autowired
	private BankService  bankService;

	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	public  BankContractResponse create(@RequestBody @Valid BankContractRequest bankContractRequest, BindingResult errors) {
		ModelMapper modelMapper=new ModelMapper();
		bankService.validate(bankContractRequest,"create",errors);
		if (errors.hasErrors()) {
		  throw	new CustomBindException(errors);
		}
		bankService.fetchRelatedContracts(bankContractRequest);
		BankContractResponse bankContractResponse = new BankContractResponse();
		bankContractResponse.setBanks(new ArrayList<BankContract>());
		for(BankContract bankContract:bankContractRequest.getBanks())
		{
		
		Bank	bankEntity=	modelMapper.map(bankContract, Bank.class);
		bankEntity = bankService.create(bankEntity);
		BankContract resp=modelMapper.map(bankEntity, BankContract.class);
		bankContract.setId(bankEntity.getId());
		bankContractResponse.getBanks().add(resp);
		}

		bankContractResponse.setResponseInfo(getResponseInfo(bankContractRequest.getRequestInfo()));
		 
		return bankContractResponse;
	}

	@PutMapping(value = "/{uniqueId}")
	@ResponseStatus(HttpStatus.OK)
	public BankContractResponse update(@RequestBody @Valid BankContractRequest bankContractRequest, BindingResult errors,
			@PathVariable Long uniqueId) {
		
		bankService.validate(bankContractRequest,"update",errors);
		
		if (errors.hasErrors()) {
			  throw	new CustomBindException(errors);
			}
		bankService.fetchRelatedContracts(bankContractRequest);
		Bank bankFromDb = bankService.findOne(uniqueId);
		
		BankContract bank = bankContractRequest.getBank();
		//ignoring internally passed id if the put has id in url
	    bank.setId(uniqueId);
		ModelMapper model=new ModelMapper();
	 	model.map(bank, bankFromDb);
		bankFromDb = bankService.update(bankFromDb);
		BankContractResponse bankContractResponse = new BankContractResponse();
		bankContractResponse.setBank(bank);  
		bankContractResponse.setResponseInfo(getResponseInfo(bankContractRequest.getRequestInfo()));
		bankContractResponse.getResponseInfo().setStatus(HttpStatus.OK.toString());
		return bankContractResponse;
	}
	
	@PostMapping(value = "/{uniqueId}")
	@ResponseStatus(HttpStatus.OK)
	public BankContractResponse view(@ModelAttribute BankContractRequest bankContractRequest, BindingResult errors,
			@PathVariable Long uniqueId) {
		bankService.validate(bankContractRequest,"view",errors);
		if (errors.hasErrors()) {
			  throw	new CustomBindException(errors);
			}
		bankService.fetchRelatedContracts(bankContractRequest);
		RequestInfo requestInfo = bankContractRequest.getRequestInfo();
		Bank bankFromDb = bankService.findOne(uniqueId);
		BankContract bank = bankContractRequest.getBank();
		
		ModelMapper model=new ModelMapper();
	 	model.map(bankFromDb,bank );
		
		BankContractResponse bankContractResponse = new BankContractResponse();
		bankContractResponse.setBank(bank);  
		bankContractResponse.setResponseInfo(getResponseInfo(bankContractRequest.getRequestInfo()));
		bankContractResponse.getResponseInfo().setStatus(HttpStatus.CREATED.toString());
		return bankContractResponse ;
	}
	
	@PutMapping
	@ResponseStatus(HttpStatus.OK)
	public BankContractResponse updateAll(@RequestBody @Valid BankContractRequest bankContractRequest, BindingResult errors) {
		bankService.validate(bankContractRequest,"updateAll",errors);
		if (errors.hasErrors()) {
			  throw	new CustomBindException(errors);
			}
		bankService.fetchRelatedContracts(bankContractRequest);		
 
		BankContractResponse bankContractResponse =new  BankContractResponse();
		bankContractResponse.setBanks(new ArrayList<BankContract>());
		for(BankContract bankContract:bankContractRequest.getBanks())
		{
		Bank bankFromDb = bankService.findOne(bankContract.getId());
		
		ModelMapper model=new ModelMapper();
	 	model.map(bankContract, bankFromDb);
		bankFromDb = bankService.update(bankFromDb);
		model.map(bankFromDb,bankContract);
		bankContractResponse.getBanks().add(bankContract);  
		}

		bankContractResponse.setResponseInfo(getResponseInfo(bankContractRequest.getRequestInfo()));
		bankContractResponse.getResponseInfo().setStatus(HttpStatus.OK.toString());
		
		return bankContractResponse;
	}
	

	@PostMapping("/_search")
	@ResponseBody
	@ResponseStatus(HttpStatus.OK)
	public BankContractResponse search(@ModelAttribute BankContract bankContracts,@RequestBody RequestInfo requestInfo, BindingResult errors) {
	    BankContractRequest bankContractRequest=new BankContractRequest();
	    bankContractRequest.setBank(bankContracts);
	    bankContractRequest.setRequestInfo(requestInfo);
	    
	    bankService.validate(bankContractRequest,"search",errors);
		if (errors.hasErrors()) {
			  throw	new CustomBindException(errors);
			}
	       
		bankService.fetchRelatedContracts(bankContractRequest);
		BankContractResponse bankContractResponse =new  BankContractResponse();
		bankContractResponse.setBanks(new ArrayList<BankContract>());
		bankContractResponse.setPage(new Pagination());
		Page<Bank> allBanks;
		ModelMapper model=new ModelMapper();
	 
		allBanks = bankService.search(bankContractRequest);
		BankContract bankContract=null;
		for(Bank b:allBanks)
		{
			bankContract=new BankContract();
			model.map(b, bankContract);
			bankContractResponse.getBanks().add(bankContract);
		}
		bankContractResponse.getPage().map(allBanks);
		bankContractResponse.setResponseInfo(getResponseInfo(bankContractRequest.getRequestInfo()));
		bankContractResponse.getResponseInfo().setStatus(HttpStatus.OK.toString());
		return bankContractResponse;
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