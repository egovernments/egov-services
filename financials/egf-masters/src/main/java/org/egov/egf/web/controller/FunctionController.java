package org.egov.egf.web.controller;

import java.util.ArrayList;
import java.util.Date;

import javax.validation.Valid;

import org.egov.egf.domain.exception.CustomBindException;
import org.egov.egf.persistence.entity.Function;
import org.egov.egf.persistence.queue.contract.FunctionContract;
import org.egov.egf.persistence.queue.contract.FunctionContractRequest;
import org.egov.egf.persistence.queue.contract.FunctionContractResponse;
import org.egov.egf.persistence.queue.contract.Pagination;
import org.egov.egf.persistence.queue.contract.RequestInfo;
import org.egov.egf.persistence.queue.contract.ResponseInfo;
import org.egov.egf.persistence.service.FunctionService;
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
@RequestMapping("/functions")  
public class FunctionController {
	@Autowired
	private FunctionService  functionService;

	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	public  FunctionContractResponse create(@RequestBody @Valid FunctionContractRequest functionContractRequest, BindingResult errors) {
		ModelMapper modelMapper=new ModelMapper();
		functionService.validate(functionContractRequest,"create",errors);
		if (errors.hasErrors()) {
		  throw	new CustomBindException(errors);
		}
		
		FunctionContractResponse functionContractResponse = new FunctionContractResponse();
		functionContractResponse.setFunctions(new ArrayList<FunctionContract>());
		for(FunctionContract functionContract:functionContractRequest.getFunctions())
		{
		
		Function	functionEntity=	modelMapper.map(functionContract, Function.class);
		functionEntity = functionService.create(functionEntity);
		FunctionContract resp=modelMapper.map(functionEntity, FunctionContract.class);
		functionContract.setId(functionEntity.getId());
		functionContractResponse.getFunctions().add(resp);
		}

		functionContractResponse.setResponseInfo(getResponseInfo(functionContractRequest.getRequestInfo()));
		 
		return functionContractResponse;
	}

	@PutMapping(value = "/{uniqueId}")
	@ResponseStatus(HttpStatus.OK)
	public FunctionContractResponse update(@RequestBody @Valid FunctionContractRequest functionContractRequest, BindingResult errors,
			@PathVariable Long uniqueId) {
		
		functionService.validate(functionContractRequest,"update",errors);
		
		if (errors.hasErrors()) {
			  throw	new CustomBindException(errors);
			}
	 
		Function functionFromDb = functionService.findOne(uniqueId);
		FunctionContract function = functionContractRequest.getFunction();
		
		ModelMapper model=new ModelMapper();
	 	model.map(function, functionFromDb);
		functionFromDb = functionService.update(functionFromDb);
		FunctionContractResponse functionContractResponse = new FunctionContractResponse();
		functionContractResponse.setFunction(function);  
		functionContractResponse.setResponseInfo(getResponseInfo(functionContractRequest.getRequestInfo()));
		functionContractResponse.getResponseInfo().setStatus(HttpStatus.OK.toString());
		return functionContractResponse;
	}
	
	@GetMapping(value = "/{uniqueId}")
	@ResponseStatus(HttpStatus.OK)
	public FunctionContractResponse view(@RequestBody @Valid FunctionContractRequest functionContractRequest, BindingResult errors,
			@PathVariable Long uniqueId) {

		if (errors.hasErrors()) {
			  throw	new CustomBindException(errors);
			}
		functionService.validate(functionContractRequest,"view",errors);
		RequestInfo requestInfo = functionContractRequest.getRequestInfo();
		Function functionFromDb = functionService.findOne(uniqueId);
		FunctionContract function = functionContractRequest.getFunction();
		
		ModelMapper model=new ModelMapper();
	 	model.map(function, functionFromDb);
		
		FunctionContractResponse functionContractResponse = new FunctionContractResponse();
		functionContractResponse.setFunction(function);  
		functionContractResponse.setResponseInfo(getResponseInfo(functionContractRequest.getRequestInfo()));
		functionContractResponse.getResponseInfo().setStatus(HttpStatus.CREATED.toString());
		return functionContractResponse ;
	}
	
	@PutMapping
	@ResponseStatus(HttpStatus.OK)
	public FunctionContractResponse updateAll(@RequestBody @Valid FunctionContractRequest functionContractRequest, BindingResult errors) {
		functionService.validate(functionContractRequest,"updateAll",errors);
		if (errors.hasErrors()) {
			  throw	new CustomBindException(errors);
			}
		
 
		FunctionContractResponse functionContractResponse =new  FunctionContractResponse();
		functionContractResponse.setFunctions(new ArrayList<FunctionContract>());
		for(FunctionContract functionContract:functionContractRequest.getFunctions())
		{
		Function functionFromDb = functionService.findOne(functionContract.getId());
		 
		
		ModelMapper model=new ModelMapper();
	 	model.map(functionContract, functionFromDb);
		functionFromDb = functionService.update(functionFromDb);
		model.map(functionFromDb,functionContract);
		functionContractResponse.getFunctions().add(functionContract);  
		}

		functionContractResponse.setResponseInfo(getResponseInfo(functionContractRequest.getRequestInfo()));
		functionContractResponse.getResponseInfo().setStatus(HttpStatus.OK.toString());
		
		return functionContractResponse;
	}
	

	@GetMapping
	@ResponseBody
	@ResponseStatus(HttpStatus.OK)
	public FunctionContractResponse search(@ModelAttribute FunctionContractRequest functionContractRequest,BindingResult errors) {
		functionService.validate(functionContractRequest,"search",errors);
		if (errors.hasErrors()) {
			  throw	new CustomBindException(errors);
			}
		FunctionContractResponse functionContractResponse =new  FunctionContractResponse();
		functionContractResponse.setFunctions(new ArrayList<FunctionContract>());
		functionContractResponse.setPage(new Pagination());
		Page<Function> allFunctions;
		ModelMapper model=new ModelMapper();
	 
		allFunctions = functionService.search(functionContractRequest);
		FunctionContract functionContract=null;
		for(Function b:allFunctions)
		{
			functionContract=new FunctionContract();
			model.map(b, functionContract);
			functionContractResponse.getFunctions().add(functionContract);
		}
		functionContractResponse.getPage().map(allFunctions);
		functionContractResponse.setResponseInfo(getResponseInfo(functionContractRequest.getRequestInfo()));
		functionContractResponse.getResponseInfo().setStatus(HttpStatus.OK.toString());
		return functionContractResponse;
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