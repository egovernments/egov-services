package org.egov.egf.web.controller;

import java.util.ArrayList;
import java.util.Date;

import javax.validation.Valid;

import org.egov.egf.domain.exception.CustomBindException;
import org.egov.egf.persistence.entity.AccountCodePurpose;
import org.egov.egf.persistence.queue.contract.AccountCodePurposeContract;
import org.egov.egf.persistence.queue.contract.AccountCodePurposeContractRequest;
import org.egov.egf.persistence.queue.contract.AccountCodePurposeContractResponse;
import org.egov.egf.persistence.queue.contract.Pagination;
import org.egov.egf.persistence.queue.contract.RequestInfo;
import org.egov.egf.persistence.queue.contract.ResponseInfo;
import org.egov.egf.persistence.service.AccountCodePurposeService;
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
@RequestMapping("/accountcodepurposes")  
public class AccountCodePurposeController {
	@Autowired
	private AccountCodePurposeService  accountCodePurposeService;

	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	public  AccountCodePurposeContractResponse create(@RequestBody @Valid AccountCodePurposeContractRequest accountCodePurposeContractRequest, BindingResult errors) {
		ModelMapper modelMapper=new ModelMapper();
		accountCodePurposeService.validate(accountCodePurposeContractRequest,"create",errors);
		if (errors.hasErrors()) {
		  throw	new CustomBindException(errors);
		}
		accountCodePurposeService.fetchRelatedContracts(accountCodePurposeContractRequest);
		AccountCodePurposeContractResponse accountCodePurposeContractResponse = new AccountCodePurposeContractResponse();
		accountCodePurposeContractResponse.setAccountCodePurposes(new ArrayList<AccountCodePurposeContract>());
		for(AccountCodePurposeContract accountCodePurposeContract:accountCodePurposeContractRequest.getAccountCodePurposes())
		{
		
		AccountCodePurpose	accountCodePurposeEntity=	modelMapper.map(accountCodePurposeContract, AccountCodePurpose.class);
		accountCodePurposeEntity = accountCodePurposeService.create(accountCodePurposeEntity);
		AccountCodePurposeContract resp=modelMapper.map(accountCodePurposeEntity, AccountCodePurposeContract.class);
		accountCodePurposeContract.setId(accountCodePurposeEntity.getId());
		accountCodePurposeContractResponse.getAccountCodePurposes().add(resp);
		}

		accountCodePurposeContractResponse.setResponseInfo(getResponseInfo(accountCodePurposeContractRequest.getRequestInfo()));
		 
		return accountCodePurposeContractResponse;
	}

	@PutMapping(value = "/{uniqueId}")
	@ResponseStatus(HttpStatus.OK)
	public AccountCodePurposeContractResponse update(@RequestBody @Valid AccountCodePurposeContractRequest accountCodePurposeContractRequest, BindingResult errors,
			@PathVariable Long uniqueId) {
		
		accountCodePurposeService.validate(accountCodePurposeContractRequest,"update",errors);
		
		if (errors.hasErrors()) {
			  throw	new CustomBindException(errors);
			}
		accountCodePurposeService.fetchRelatedContracts(accountCodePurposeContractRequest);
		AccountCodePurpose accountCodePurposeFromDb = accountCodePurposeService.findOne(uniqueId);
		
		AccountCodePurposeContract accountCodePurpose = accountCodePurposeContractRequest.getAccountCodePurpose();
		//ignoring internally passed id if the put has id in url
	    accountCodePurpose.setId(uniqueId);
		ModelMapper model=new ModelMapper();
	 	model.map(accountCodePurpose, accountCodePurposeFromDb);
		accountCodePurposeFromDb = accountCodePurposeService.update(accountCodePurposeFromDb);
		AccountCodePurposeContractResponse accountCodePurposeContractResponse = new AccountCodePurposeContractResponse();
		accountCodePurposeContractResponse.setAccountCodePurpose(accountCodePurpose);  
		accountCodePurposeContractResponse.setResponseInfo(getResponseInfo(accountCodePurposeContractRequest.getRequestInfo()));
		accountCodePurposeContractResponse.getResponseInfo().setStatus(HttpStatus.OK.toString());
		return accountCodePurposeContractResponse;
	}
	
	@GetMapping(value = "/{uniqueId}")
	@ResponseStatus(HttpStatus.OK)
	public AccountCodePurposeContractResponse view(@ModelAttribute AccountCodePurposeContractRequest accountCodePurposeContractRequest, BindingResult errors,
			@PathVariable Long uniqueId) {
		accountCodePurposeService.validate(accountCodePurposeContractRequest,"view",errors);
		if (errors.hasErrors()) {
			  throw	new CustomBindException(errors);
			}
		accountCodePurposeService.fetchRelatedContracts(accountCodePurposeContractRequest);
		RequestInfo requestInfo = accountCodePurposeContractRequest.getRequestInfo();
		AccountCodePurpose accountCodePurposeFromDb = accountCodePurposeService.findOne(uniqueId);
		AccountCodePurposeContract accountCodePurpose = accountCodePurposeContractRequest.getAccountCodePurpose();
		
		ModelMapper model=new ModelMapper();
	 	model.map(accountCodePurposeFromDb,accountCodePurpose );
		
		AccountCodePurposeContractResponse accountCodePurposeContractResponse = new AccountCodePurposeContractResponse();
		accountCodePurposeContractResponse.setAccountCodePurpose(accountCodePurpose);  
		accountCodePurposeContractResponse.setResponseInfo(getResponseInfo(accountCodePurposeContractRequest.getRequestInfo()));
		accountCodePurposeContractResponse.getResponseInfo().setStatus(HttpStatus.CREATED.toString());
		return accountCodePurposeContractResponse ;
	}
	
	@PutMapping
	@ResponseStatus(HttpStatus.OK)
	public AccountCodePurposeContractResponse updateAll(@RequestBody @Valid AccountCodePurposeContractRequest accountCodePurposeContractRequest, BindingResult errors) {
		accountCodePurposeService.validate(accountCodePurposeContractRequest,"updateAll",errors);
		if (errors.hasErrors()) {
			  throw	new CustomBindException(errors);
			}
		accountCodePurposeService.fetchRelatedContracts(accountCodePurposeContractRequest);		
 
		AccountCodePurposeContractResponse accountCodePurposeContractResponse =new  AccountCodePurposeContractResponse();
		accountCodePurposeContractResponse.setAccountCodePurposes(new ArrayList<AccountCodePurposeContract>());
		for(AccountCodePurposeContract accountCodePurposeContract:accountCodePurposeContractRequest.getAccountCodePurposes())
		{
		AccountCodePurpose accountCodePurposeFromDb = accountCodePurposeService.findOne(accountCodePurposeContract.getId());
		
		ModelMapper model=new ModelMapper();
	 	model.map(accountCodePurposeContract, accountCodePurposeFromDb);
		accountCodePurposeFromDb = accountCodePurposeService.update(accountCodePurposeFromDb);
		model.map(accountCodePurposeFromDb,accountCodePurposeContract);
		accountCodePurposeContractResponse.getAccountCodePurposes().add(accountCodePurposeContract);  
		}

		accountCodePurposeContractResponse.setResponseInfo(getResponseInfo(accountCodePurposeContractRequest.getRequestInfo()));
		accountCodePurposeContractResponse.getResponseInfo().setStatus(HttpStatus.OK.toString());
		
		return accountCodePurposeContractResponse;
	}
	

	@GetMapping
	@ResponseBody
	@ResponseStatus(HttpStatus.OK)
	public AccountCodePurposeContractResponse search(@ModelAttribute AccountCodePurposeContractRequest accountCodePurposeContractRequest,BindingResult errors) {
		accountCodePurposeService.validate(accountCodePurposeContractRequest,"search",errors);
		if (errors.hasErrors()) {
			  throw	new CustomBindException(errors);
			}
		accountCodePurposeService.fetchRelatedContracts(accountCodePurposeContractRequest);
		AccountCodePurposeContractResponse accountCodePurposeContractResponse =new  AccountCodePurposeContractResponse();
		accountCodePurposeContractResponse.setAccountCodePurposes(new ArrayList<AccountCodePurposeContract>());
		accountCodePurposeContractResponse.setPage(new Pagination());
		Page<AccountCodePurpose> allAccountCodePurposes;
		ModelMapper model=new ModelMapper();
	 
		allAccountCodePurposes = accountCodePurposeService.search(accountCodePurposeContractRequest);
		AccountCodePurposeContract accountCodePurposeContract=null;
		for(AccountCodePurpose b:allAccountCodePurposes)
		{
			accountCodePurposeContract=new AccountCodePurposeContract();
			model.map(b, accountCodePurposeContract);
			accountCodePurposeContractResponse.getAccountCodePurposes().add(accountCodePurposeContract);
		}
		accountCodePurposeContractResponse.getPage().map(allAccountCodePurposes);
		accountCodePurposeContractResponse.setResponseInfo(getResponseInfo(accountCodePurposeContractRequest.getRequestInfo()));
		accountCodePurposeContractResponse.getResponseInfo().setStatus(HttpStatus.OK.toString());
		return accountCodePurposeContractResponse;
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