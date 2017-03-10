package org.egov.egf.web.controller;

import java.util.ArrayList;
import java.util.Date;

import javax.validation.Valid;

import org.egov.egf.domain.exception.CustomBindException;
import org.egov.egf.persistence.entity.BudgetGroup;
import org.egov.egf.persistence.queue.contract.BudgetGroupContract;
import org.egov.egf.persistence.queue.contract.BudgetGroupContractRequest;
import org.egov.egf.persistence.queue.contract.BudgetGroupContractResponse;
import org.egov.egf.persistence.queue.contract.Pagination;
import org.egov.egf.persistence.queue.contract.RequestInfo;
import org.egov.egf.persistence.queue.contract.ResponseInfo;
import org.egov.egf.persistence.service.BudgetGroupService;
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
@RequestMapping("/budgetgroups")  
public class BudgetGroupController {
	@Autowired
	private BudgetGroupService  budgetGroupService;

	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	public  BudgetGroupContractResponse create(@RequestBody @Valid BudgetGroupContractRequest budgetGroupContractRequest, BindingResult errors) {
		ModelMapper modelMapper=new ModelMapper();
		budgetGroupService.validate(budgetGroupContractRequest,"create",errors);
		if (errors.hasErrors()) {
		  throw	new CustomBindException(errors);
		}
		budgetGroupService.fetchRelatedContracts(budgetGroupContractRequest);
		BudgetGroupContractResponse budgetGroupContractResponse = new BudgetGroupContractResponse();
		budgetGroupContractResponse.setBudgetGroups(new ArrayList<BudgetGroupContract>());
		for(BudgetGroupContract budgetGroupContract:budgetGroupContractRequest.getBudgetGroups())
		{
		
		BudgetGroup	budgetGroupEntity=	modelMapper.map(budgetGroupContract, BudgetGroup.class);
		budgetGroupEntity = budgetGroupService.create(budgetGroupEntity);
		BudgetGroupContract resp=modelMapper.map(budgetGroupEntity, BudgetGroupContract.class);
		budgetGroupContract.setId(budgetGroupEntity.getId());
		budgetGroupContractResponse.getBudgetGroups().add(resp);
		}

		budgetGroupContractResponse.setResponseInfo(getResponseInfo(budgetGroupContractRequest.getRequestInfo()));
		 
		return budgetGroupContractResponse;
	}

	@PutMapping(value = "/{uniqueId}")
	@ResponseStatus(HttpStatus.OK)
	public BudgetGroupContractResponse update(@RequestBody @Valid BudgetGroupContractRequest budgetGroupContractRequest, BindingResult errors,
			@PathVariable Long uniqueId) {
		
		budgetGroupService.validate(budgetGroupContractRequest,"update",errors);
		
		if (errors.hasErrors()) {
			  throw	new CustomBindException(errors);
			}
		budgetGroupService.fetchRelatedContracts(budgetGroupContractRequest);
		BudgetGroup budgetGroupFromDb = budgetGroupService.findOne(uniqueId);
		
		BudgetGroupContract budgetGroup = budgetGroupContractRequest.getBudgetGroup();
		//ignoring internally passed id if the put has id in url
	    budgetGroup.setId(uniqueId);
		ModelMapper model=new ModelMapper();
	 	model.map(budgetGroup, budgetGroupFromDb);
		budgetGroupFromDb = budgetGroupService.update(budgetGroupFromDb);
		BudgetGroupContractResponse budgetGroupContractResponse = new BudgetGroupContractResponse();
		budgetGroupContractResponse.setBudgetGroup(budgetGroup);  
		budgetGroupContractResponse.setResponseInfo(getResponseInfo(budgetGroupContractRequest.getRequestInfo()));
		budgetGroupContractResponse.getResponseInfo().setStatus(HttpStatus.OK.toString());
		return budgetGroupContractResponse;
	}
	
	@GetMapping(value = "/{uniqueId}")
	@ResponseStatus(HttpStatus.OK)
	public BudgetGroupContractResponse view(@ModelAttribute BudgetGroupContractRequest budgetGroupContractRequest, BindingResult errors,
			@PathVariable Long uniqueId) {
		budgetGroupService.validate(budgetGroupContractRequest,"view",errors);
		if (errors.hasErrors()) {
			  throw	new CustomBindException(errors);
			}
		budgetGroupService.fetchRelatedContracts(budgetGroupContractRequest);
		RequestInfo requestInfo = budgetGroupContractRequest.getRequestInfo();
		BudgetGroup budgetGroupFromDb = budgetGroupService.findOne(uniqueId);
		BudgetGroupContract budgetGroup = budgetGroupContractRequest.getBudgetGroup();
		
		ModelMapper model=new ModelMapper();
	 	model.map(budgetGroupFromDb,budgetGroup );
		
		BudgetGroupContractResponse budgetGroupContractResponse = new BudgetGroupContractResponse();
		budgetGroupContractResponse.setBudgetGroup(budgetGroup);  
		budgetGroupContractResponse.setResponseInfo(getResponseInfo(budgetGroupContractRequest.getRequestInfo()));
		budgetGroupContractResponse.getResponseInfo().setStatus(HttpStatus.CREATED.toString());
		return budgetGroupContractResponse ;
	}
	
	@PutMapping
	@ResponseStatus(HttpStatus.OK)
	public BudgetGroupContractResponse updateAll(@RequestBody @Valid BudgetGroupContractRequest budgetGroupContractRequest, BindingResult errors) {
		budgetGroupService.validate(budgetGroupContractRequest,"updateAll",errors);
		if (errors.hasErrors()) {
			  throw	new CustomBindException(errors);
			}
		budgetGroupService.fetchRelatedContracts(budgetGroupContractRequest);		
 
		BudgetGroupContractResponse budgetGroupContractResponse =new  BudgetGroupContractResponse();
		budgetGroupContractResponse.setBudgetGroups(new ArrayList<BudgetGroupContract>());
		for(BudgetGroupContract budgetGroupContract:budgetGroupContractRequest.getBudgetGroups())
		{
		BudgetGroup budgetGroupFromDb = budgetGroupService.findOne(budgetGroupContract.getId());
		
		ModelMapper model=new ModelMapper();
	 	model.map(budgetGroupContract, budgetGroupFromDb);
		budgetGroupFromDb = budgetGroupService.update(budgetGroupFromDb);
		model.map(budgetGroupFromDb,budgetGroupContract);
		budgetGroupContractResponse.getBudgetGroups().add(budgetGroupContract);  
		}

		budgetGroupContractResponse.setResponseInfo(getResponseInfo(budgetGroupContractRequest.getRequestInfo()));
		budgetGroupContractResponse.getResponseInfo().setStatus(HttpStatus.OK.toString());
		
		return budgetGroupContractResponse;
	}
	

	@GetMapping
	@ResponseBody
	@ResponseStatus(HttpStatus.OK)
	public BudgetGroupContractResponse search(@ModelAttribute BudgetGroupContractRequest budgetGroupContractRequest,BindingResult errors) {
		budgetGroupService.validate(budgetGroupContractRequest,"search",errors);
		if (errors.hasErrors()) {
			  throw	new CustomBindException(errors);
			}
		budgetGroupService.fetchRelatedContracts(budgetGroupContractRequest);
		BudgetGroupContractResponse budgetGroupContractResponse =new  BudgetGroupContractResponse();
		budgetGroupContractResponse.setBudgetGroups(new ArrayList<BudgetGroupContract>());
		budgetGroupContractResponse.setPage(new Pagination());
		Page<BudgetGroup> allBudgetGroups;
		ModelMapper model=new ModelMapper();
	 
		allBudgetGroups = budgetGroupService.search(budgetGroupContractRequest);
		BudgetGroupContract budgetGroupContract=null;
		for(BudgetGroup b:allBudgetGroups)
		{
			budgetGroupContract=new BudgetGroupContract();
			model.map(b, budgetGroupContract);
			budgetGroupContractResponse.getBudgetGroups().add(budgetGroupContract);
		}
		budgetGroupContractResponse.getPage().map(allBudgetGroups);
		budgetGroupContractResponse.setResponseInfo(getResponseInfo(budgetGroupContractRequest.getRequestInfo()));
		budgetGroupContractResponse.getResponseInfo().setStatus(HttpStatus.OK.toString());
		return budgetGroupContractResponse;
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