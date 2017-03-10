package org.egov.egf.web.controller;

import java.util.ArrayList;
import java.util.Date;

import javax.validation.Valid;

import org.egov.egf.domain.exception.CustomBindException;
import org.egov.egf.persistence.entity.Fundsource;
import org.egov.egf.persistence.queue.contract.FundsourceContract;
import org.egov.egf.persistence.queue.contract.FundsourceContractRequest;
import org.egov.egf.persistence.queue.contract.FundsourceContractResponse;
import org.egov.egf.persistence.queue.contract.Pagination;
import org.egov.egf.persistence.queue.contract.RequestInfo;
import org.egov.egf.persistence.queue.contract.ResponseInfo;
import org.egov.egf.persistence.service.FundsourceService;
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
@RequestMapping("/fundsources")  
public class FundsourceController {
	@Autowired
	private FundsourceService  fundsourceService;

	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	public  FundsourceContractResponse create(@RequestBody @Valid FundsourceContractRequest fundsourceContractRequest, BindingResult errors) {
		ModelMapper modelMapper=new ModelMapper();
		fundsourceService.validate(fundsourceContractRequest,"create",errors);
		if (errors.hasErrors()) {
		  throw	new CustomBindException(errors);
		}
		
		FundsourceContractResponse fundsourceContractResponse = new FundsourceContractResponse();
		fundsourceContractResponse.setFundsources(new ArrayList<FundsourceContract>());
		for(FundsourceContract fundsourceContract:fundsourceContractRequest.getFundsources())
		{
		
		Fundsource	fundsourceEntity=	modelMapper.map(fundsourceContract, Fundsource.class);
		fundsourceEntity = fundsourceService.create(fundsourceEntity);
		FundsourceContract resp=modelMapper.map(fundsourceEntity, FundsourceContract.class);
		fundsourceContract.setId(fundsourceEntity.getId());
		fundsourceContractResponse.getFundsources().add(resp);
		}

		fundsourceContractResponse.setResponseInfo(getResponseInfo(fundsourceContractRequest.getRequestInfo()));
		 
		return fundsourceContractResponse;
	}

	@PutMapping(value = "/{uniqueId}")
	@ResponseStatus(HttpStatus.OK)
	public FundsourceContractResponse update(@RequestBody @Valid FundsourceContractRequest fundsourceContractRequest, BindingResult errors,
			@PathVariable Long uniqueId) {
		
		fundsourceService.validate(fundsourceContractRequest,"update",errors);
		
		if (errors.hasErrors()) {
			  throw	new CustomBindException(errors);
			}
	 
		Fundsource fundsourceFromDb = fundsourceService.findOne(uniqueId);
		FundsourceContract fundsource = fundsourceContractRequest.getFundsource();
		
		ModelMapper model=new ModelMapper();
	 	model.map(fundsource, fundsourceFromDb);
		fundsourceFromDb = fundsourceService.update(fundsourceFromDb);
		FundsourceContractResponse fundsourceContractResponse = new FundsourceContractResponse();
		fundsourceContractResponse.setFundsource(fundsource);  
		fundsourceContractResponse.setResponseInfo(getResponseInfo(fundsourceContractRequest.getRequestInfo()));
		fundsourceContractResponse.getResponseInfo().setStatus(HttpStatus.OK.toString());
		return fundsourceContractResponse;
	}
	
	@GetMapping(value = "/{uniqueId}")
	@ResponseStatus(HttpStatus.OK)
	public FundsourceContractResponse view(@RequestBody @Valid FundsourceContractRequest fundsourceContractRequest, BindingResult errors,
			@PathVariable Long uniqueId) {

		if (errors.hasErrors()) {
			  throw	new CustomBindException(errors);
			}
		fundsourceService.validate(fundsourceContractRequest,"view",errors);
		RequestInfo requestInfo = fundsourceContractRequest.getRequestInfo();
		Fundsource fundsourceFromDb = fundsourceService.findOne(uniqueId);
		FundsourceContract fundsource = fundsourceContractRequest.getFundsource();
		
		ModelMapper model=new ModelMapper();
	 	model.map(fundsource, fundsourceFromDb);
		
		FundsourceContractResponse fundsourceContractResponse = new FundsourceContractResponse();
		fundsourceContractResponse.setFundsource(fundsource);  
		fundsourceContractResponse.setResponseInfo(getResponseInfo(fundsourceContractRequest.getRequestInfo()));
		fundsourceContractResponse.getResponseInfo().setStatus(HttpStatus.CREATED.toString());
		return fundsourceContractResponse ;
	}
	
	@PutMapping
	@ResponseStatus(HttpStatus.OK)
	public FundsourceContractResponse updateAll(@RequestBody @Valid FundsourceContractRequest fundsourceContractRequest, BindingResult errors) {
		fundsourceService.validate(fundsourceContractRequest,"updateAll",errors);
		if (errors.hasErrors()) {
			  throw	new CustomBindException(errors);
			}
		
 
		FundsourceContractResponse fundsourceContractResponse =new  FundsourceContractResponse();
		fundsourceContractResponse.setFundsources(new ArrayList<FundsourceContract>());
		for(FundsourceContract fundsourceContract:fundsourceContractRequest.getFundsources())
		{
		Fundsource fundsourceFromDb = fundsourceService.findOne(fundsourceContract.getId());
		 
		
		ModelMapper model=new ModelMapper();
	 	model.map(fundsourceContract, fundsourceFromDb);
		fundsourceFromDb = fundsourceService.update(fundsourceFromDb);
		model.map(fundsourceFromDb,fundsourceContract);
		fundsourceContractResponse.getFundsources().add(fundsourceContract);  
		}

		fundsourceContractResponse.setResponseInfo(getResponseInfo(fundsourceContractRequest.getRequestInfo()));
		fundsourceContractResponse.getResponseInfo().setStatus(HttpStatus.OK.toString());
		
		return fundsourceContractResponse;
	}
	

	@GetMapping
	@ResponseBody
	@ResponseStatus(HttpStatus.OK)
	public FundsourceContractResponse search(@ModelAttribute FundsourceContractRequest fundsourceContractRequest,BindingResult errors) {
		fundsourceService.validate(fundsourceContractRequest,"search",errors);
		if (errors.hasErrors()) {
			  throw	new CustomBindException(errors);
			}
		FundsourceContractResponse fundsourceContractResponse =new  FundsourceContractResponse();
		fundsourceContractResponse.setFundsources(new ArrayList<FundsourceContract>());
		fundsourceContractResponse.setPage(new Pagination());
		Page<Fundsource> allFundsources;
		ModelMapper model=new ModelMapper();
	 
		allFundsources = fundsourceService.search(fundsourceContractRequest);
		FundsourceContract fundsourceContract=null;
		for(Fundsource b:allFundsources)
		{
			fundsourceContract=new FundsourceContract();
			model.map(b, fundsourceContract);
			fundsourceContractResponse.getFundsources().add(fundsourceContract);
		}
		fundsourceContractResponse.getPage().map(allFundsources);
		fundsourceContractResponse.setResponseInfo(getResponseInfo(fundsourceContractRequest.getRequestInfo()));
		fundsourceContractResponse.getResponseInfo().setStatus(HttpStatus.OK.toString());
		return fundsourceContractResponse;
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