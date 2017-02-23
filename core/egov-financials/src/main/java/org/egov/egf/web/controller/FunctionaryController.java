package org.egov.egf.web.controller;

import java.util.ArrayList;
import java.util.Date;

import javax.validation.Valid;

import org.egov.egf.domain.exception.CustomBindException;
import org.egov.egf.persistence.entity.Functionary;
import org.egov.egf.persistence.queue.contract.FunctionaryContract;
import org.egov.egf.persistence.queue.contract.FunctionaryContractRequest;
import org.egov.egf.persistence.queue.contract.FunctionaryContractResponse;
import org.egov.egf.persistence.queue.contract.Pagination;
import org.egov.egf.persistence.queue.contract.RequestInfo;
import org.egov.egf.persistence.queue.contract.ResponseInfo;
import org.egov.egf.persistence.service.FunctionaryService;
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
@RequestMapping("/functionaries")  
public class FunctionaryController {
	@Autowired
	private FunctionaryService  functionaryService;

	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	public  FunctionaryContractResponse create(@RequestBody @Valid FunctionaryContractRequest functionaryContractRequest, BindingResult errors) {
		ModelMapper modelMapper=new ModelMapper();
		functionaryService.validate(functionaryContractRequest,"create",errors);
		if (errors.hasErrors()) {
		  throw	new CustomBindException(errors);
		}
		
		FunctionaryContractResponse functionaryContractResponse = new FunctionaryContractResponse();
		functionaryContractResponse.setFunctionaries(new ArrayList<FunctionaryContract>());
		for(FunctionaryContract functionaryContract:functionaryContractRequest.getFunctionaries())
		{
		
		Functionary	functionaryEntity=	modelMapper.map(functionaryContract, Functionary.class);
		functionaryEntity = functionaryService.create(functionaryEntity);
		FunctionaryContract resp=modelMapper.map(functionaryEntity, FunctionaryContract.class);
		functionaryContract.setId(functionaryEntity.getId());
		functionaryContractResponse.getFunctionaries().add(resp);
		}

		functionaryContractResponse.setResponseInfo(getResponseInfo(functionaryContractRequest.getRequestInfo()));
		 
		return functionaryContractResponse;
	}

	@PutMapping(value = "/{uniqueId}")
	@ResponseStatus(HttpStatus.OK)
	public FunctionaryContractResponse update(@RequestBody @Valid FunctionaryContractRequest functionaryContractRequest, BindingResult errors,
			@PathVariable Long uniqueId) {
		
		functionaryService.validate(functionaryContractRequest,"update",errors);
		
		if (errors.hasErrors()) {
			  throw	new CustomBindException(errors);
			}
	 
		Functionary functionaryFromDb = functionaryService.findOne(uniqueId);
		FunctionaryContract functionary = functionaryContractRequest.getFunctionary();
		
		ModelMapper model=new ModelMapper();
	 	model.map(functionary, functionaryFromDb);
		functionaryFromDb = functionaryService.update(functionaryFromDb);
		FunctionaryContractResponse functionaryContractResponse = new FunctionaryContractResponse();
		functionaryContractResponse.setFunctionary(functionary);  
		functionaryContractResponse.setResponseInfo(getResponseInfo(functionaryContractRequest.getRequestInfo()));
		functionaryContractResponse.getResponseInfo().setStatus(HttpStatus.OK.toString());
		return functionaryContractResponse;
	}
	
	@GetMapping(value = "/{uniqueId}")
	@ResponseStatus(HttpStatus.OK)
	public FunctionaryContractResponse view(@RequestBody @Valid FunctionaryContractRequest functionaryContractRequest, BindingResult errors,
			@PathVariable Long uniqueId) {

		if (errors.hasErrors()) {
			  throw	new CustomBindException(errors);
			}
		functionaryService.validate(functionaryContractRequest,"view",errors);
		RequestInfo requestInfo = functionaryContractRequest.getRequestInfo();
		Functionary functionaryFromDb = functionaryService.findOne(uniqueId);
		FunctionaryContract functionary = functionaryContractRequest.getFunctionary();
		
		ModelMapper model=new ModelMapper();
	 	model.map(functionary, functionaryFromDb);
		
		FunctionaryContractResponse functionaryContractResponse = new FunctionaryContractResponse();
		functionaryContractResponse.setFunctionary(functionary);  
		functionaryContractResponse.setResponseInfo(getResponseInfo(functionaryContractRequest.getRequestInfo()));
		functionaryContractResponse.getResponseInfo().setStatus(HttpStatus.CREATED.toString());
		return functionaryContractResponse ;
	}
	
	@PutMapping
	@ResponseStatus(HttpStatus.OK)
	public FunctionaryContractResponse updateAll(@RequestBody @Valid FunctionaryContractRequest functionaryContractRequest, BindingResult errors) {
		functionaryService.validate(functionaryContractRequest,"updateAll",errors);
		if (errors.hasErrors()) {
			  throw	new CustomBindException(errors);
			}
		
 
		FunctionaryContractResponse functionaryContractResponse =new  FunctionaryContractResponse();
		functionaryContractResponse.setFunctionaries(new ArrayList<FunctionaryContract>());
		for(FunctionaryContract functionaryContract:functionaryContractRequest.getFunctionaries())
		{
		Functionary functionaryFromDb = functionaryService.findOne(functionaryContract.getId());
		 
		
		ModelMapper model=new ModelMapper();
	 	model.map(functionaryContract, functionaryFromDb);
		functionaryFromDb = functionaryService.update(functionaryFromDb);
		model.map(functionaryFromDb,functionaryContract);
		functionaryContractResponse.getFunctionaries().add(functionaryContract);  
		}

		functionaryContractResponse.setResponseInfo(getResponseInfo(functionaryContractRequest.getRequestInfo()));
		functionaryContractResponse.getResponseInfo().setStatus(HttpStatus.OK.toString());
		
		return functionaryContractResponse;
	}
	

	@GetMapping
	@ResponseBody
	@ResponseStatus(HttpStatus.OK)
	public FunctionaryContractResponse search(@ModelAttribute FunctionaryContractRequest functionaryContractRequest,BindingResult errors) {
		functionaryService.validate(functionaryContractRequest,"search",errors);
		if (errors.hasErrors()) {
			  throw	new CustomBindException(errors);
			}
		FunctionaryContractResponse functionaryContractResponse =new  FunctionaryContractResponse();
		functionaryContractResponse.setFunctionaries(new ArrayList<FunctionaryContract>());
		functionaryContractResponse.setPage(new Pagination());
		Page<Functionary> allFunctionaries;
		ModelMapper model=new ModelMapper();
	 
		allFunctionaries = functionaryService.search(functionaryContractRequest);
		FunctionaryContract functionaryContract=null;
		for(Functionary b:allFunctionaries)
		{
			functionaryContract=new FunctionaryContract();
			model.map(b, functionaryContract);
			functionaryContractResponse.getFunctionaries().add(functionaryContract);
		}
		functionaryContractResponse.getPage().map(allFunctionaries);
		functionaryContractResponse.setResponseInfo(getResponseInfo(functionaryContractRequest.getRequestInfo()));
		functionaryContractResponse.getResponseInfo().setStatus(HttpStatus.OK.toString());
		return functionaryContractResponse;
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