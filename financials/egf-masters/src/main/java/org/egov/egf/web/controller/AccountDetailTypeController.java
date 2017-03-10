package org.egov.egf.web.controller;

import java.util.ArrayList;
import java.util.Date;

import javax.validation.Valid;

import org.egov.egf.domain.exception.CustomBindException;
import org.egov.egf.persistence.entity.AccountDetailType;
import org.egov.egf.persistence.queue.contract.AccountDetailTypeContract;
import org.egov.egf.persistence.queue.contract.AccountDetailTypeContractRequest;
import org.egov.egf.persistence.queue.contract.AccountDetailTypeContractResponse;
import org.egov.egf.persistence.queue.contract.Pagination;
import org.egov.egf.persistence.queue.contract.RequestInfo;
import org.egov.egf.persistence.queue.contract.ResponseInfo;
import org.egov.egf.persistence.service.AccountDetailTypeService;
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
@RequestMapping("/accountdetailtypes")  
public class AccountDetailTypeController {
	@Autowired
	private AccountDetailTypeService  accountDetailTypeService;

	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	public  AccountDetailTypeContractResponse create(@RequestBody @Valid AccountDetailTypeContractRequest accountDetailTypeContractRequest, BindingResult errors) {
		ModelMapper modelMapper=new ModelMapper();
		accountDetailTypeService.validate(accountDetailTypeContractRequest,"create",errors);
		if (errors.hasErrors()) {
		  throw	new CustomBindException(errors);
		}
		
		AccountDetailTypeContractResponse accountDetailTypeContractResponse = new AccountDetailTypeContractResponse();
		accountDetailTypeContractResponse.setAccountDetailTypes(new ArrayList<AccountDetailTypeContract>());
		for(AccountDetailTypeContract accountDetailTypeContract:accountDetailTypeContractRequest.getAccountDetailTypes())
		{
		
		AccountDetailType	accountDetailTypeEntity=	modelMapper.map(accountDetailTypeContract, AccountDetailType.class);
		accountDetailTypeEntity = accountDetailTypeService.create(accountDetailTypeEntity);
		AccountDetailTypeContract resp=modelMapper.map(accountDetailTypeEntity, AccountDetailTypeContract.class);
		accountDetailTypeContract.setId(accountDetailTypeEntity.getId());
		accountDetailTypeContractResponse.getAccountDetailTypes().add(resp);
		}

		accountDetailTypeContractResponse.setResponseInfo(getResponseInfo(accountDetailTypeContractRequest.getRequestInfo()));
		 
		return accountDetailTypeContractResponse;
	}

	@PutMapping(value = "/{uniqueId}")
	@ResponseStatus(HttpStatus.OK)
	public AccountDetailTypeContractResponse update(@RequestBody @Valid AccountDetailTypeContractRequest accountDetailTypeContractRequest, BindingResult errors,
			@PathVariable Long uniqueId) {
		
		accountDetailTypeService.validate(accountDetailTypeContractRequest,"update",errors);
		
		if (errors.hasErrors()) {
			  throw	new CustomBindException(errors);
			}
	 
		AccountDetailType accountDetailTypeFromDb = accountDetailTypeService.findOne(uniqueId);
		AccountDetailTypeContract accountDetailType = accountDetailTypeContractRequest.getAccountDetailType();
		
		ModelMapper model=new ModelMapper();
	 	model.map(accountDetailType, accountDetailTypeFromDb);
		accountDetailTypeFromDb = accountDetailTypeService.update(accountDetailTypeFromDb);
		AccountDetailTypeContractResponse accountDetailTypeContractResponse = new AccountDetailTypeContractResponse();
		accountDetailTypeContractResponse.setAccountDetailType(accountDetailType);  
		accountDetailTypeContractResponse.setResponseInfo(getResponseInfo(accountDetailTypeContractRequest.getRequestInfo()));
		accountDetailTypeContractResponse.getResponseInfo().setStatus(HttpStatus.OK.toString());
		return accountDetailTypeContractResponse;
	}
	
	@GetMapping(value = "/{uniqueId}")
	@ResponseStatus(HttpStatus.OK)
	public AccountDetailTypeContractResponse view(@RequestBody @Valid AccountDetailTypeContractRequest accountDetailTypeContractRequest, BindingResult errors,
			@PathVariable Long uniqueId) {

		if (errors.hasErrors()) {
			  throw	new CustomBindException(errors);
			}
		accountDetailTypeService.validate(accountDetailTypeContractRequest,"view",errors);
		RequestInfo requestInfo = accountDetailTypeContractRequest.getRequestInfo();
		AccountDetailType accountDetailTypeFromDb = accountDetailTypeService.findOne(uniqueId);
		AccountDetailTypeContract accountDetailType = accountDetailTypeContractRequest.getAccountDetailType();
		
		ModelMapper model=new ModelMapper();
	 	model.map(accountDetailType, accountDetailTypeFromDb);
		
		AccountDetailTypeContractResponse accountDetailTypeContractResponse = new AccountDetailTypeContractResponse();
		accountDetailTypeContractResponse.setAccountDetailType(accountDetailType);  
		accountDetailTypeContractResponse.setResponseInfo(getResponseInfo(accountDetailTypeContractRequest.getRequestInfo()));
		accountDetailTypeContractResponse.getResponseInfo().setStatus(HttpStatus.CREATED.toString());
		return accountDetailTypeContractResponse ;
	}
	
	@PutMapping
	@ResponseStatus(HttpStatus.OK)
	public AccountDetailTypeContractResponse updateAll(@RequestBody @Valid AccountDetailTypeContractRequest accountDetailTypeContractRequest, BindingResult errors) {
		accountDetailTypeService.validate(accountDetailTypeContractRequest,"updateAll",errors);
		if (errors.hasErrors()) {
			  throw	new CustomBindException(errors);
			}
		
 
		AccountDetailTypeContractResponse accountDetailTypeContractResponse =new  AccountDetailTypeContractResponse();
		accountDetailTypeContractResponse.setAccountDetailTypes(new ArrayList<AccountDetailTypeContract>());
		for(AccountDetailTypeContract accountDetailTypeContract:accountDetailTypeContractRequest.getAccountDetailTypes())
		{
		AccountDetailType accountDetailTypeFromDb = accountDetailTypeService.findOne(accountDetailTypeContract.getId());
		 
		
		ModelMapper model=new ModelMapper();
	 	model.map(accountDetailTypeContract, accountDetailTypeFromDb);
		accountDetailTypeFromDb = accountDetailTypeService.update(accountDetailTypeFromDb);
		model.map(accountDetailTypeFromDb,accountDetailTypeContract);
		accountDetailTypeContractResponse.getAccountDetailTypes().add(accountDetailTypeContract);  
		}

		accountDetailTypeContractResponse.setResponseInfo(getResponseInfo(accountDetailTypeContractRequest.getRequestInfo()));
		accountDetailTypeContractResponse.getResponseInfo().setStatus(HttpStatus.OK.toString());
		
		return accountDetailTypeContractResponse;
	}
	

	@GetMapping
	@ResponseBody
	@ResponseStatus(HttpStatus.OK)
	public AccountDetailTypeContractResponse search(@ModelAttribute AccountDetailTypeContractRequest accountDetailTypeContractRequest,BindingResult errors) {
		accountDetailTypeService.validate(accountDetailTypeContractRequest,"search",errors);
		if (errors.hasErrors()) {
			  throw	new CustomBindException(errors);
			}
		AccountDetailTypeContractResponse accountDetailTypeContractResponse =new  AccountDetailTypeContractResponse();
		accountDetailTypeContractResponse.setAccountDetailTypes(new ArrayList<AccountDetailTypeContract>());
		accountDetailTypeContractResponse.setPage(new Pagination());
		Page<AccountDetailType> allAccountDetailTypes;
		ModelMapper model=new ModelMapper();
	 
		allAccountDetailTypes = accountDetailTypeService.search(accountDetailTypeContractRequest);
		AccountDetailTypeContract accountDetailTypeContract=null;
		for(AccountDetailType b:allAccountDetailTypes)
		{
			accountDetailTypeContract=new AccountDetailTypeContract();
			model.map(b, accountDetailTypeContract);
			accountDetailTypeContractResponse.getAccountDetailTypes().add(accountDetailTypeContract);
		}
		accountDetailTypeContractResponse.getPage().map(allAccountDetailTypes);
		accountDetailTypeContractResponse.setResponseInfo(getResponseInfo(accountDetailTypeContractRequest.getRequestInfo()));
		accountDetailTypeContractResponse.getResponseInfo().setStatus(HttpStatus.OK.toString());
		return accountDetailTypeContractResponse;
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