package org.egov.egf.web.controller;

import java.util.ArrayList;
import java.util.Date;

import javax.validation.Valid;

import org.egov.egf.domain.exception.CustomBindException;
import org.egov.egf.persistence.entity.SubScheme;
import org.egov.egf.persistence.queue.contract.Pagination;
import org.egov.egf.persistence.queue.contract.RequestInfo;
import org.egov.egf.persistence.queue.contract.ResponseInfo;
import org.egov.egf.persistence.queue.contract.SubSchemeContract;
import org.egov.egf.persistence.queue.contract.SubSchemeContractRequest;
import org.egov.egf.persistence.queue.contract.SubSchemeContractResponse;
import org.egov.egf.persistence.service.SubSchemeService;
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
@RequestMapping("/subschemes")  
public class SubSchemeController {
	@Autowired
	private SubSchemeService  subSchemeService;

	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	public  SubSchemeContractResponse create(@RequestBody @Valid SubSchemeContractRequest subSchemeContractRequest, BindingResult errors) {
		ModelMapper modelMapper=new ModelMapper();
		subSchemeService.validate(subSchemeContractRequest,"create",errors);
		if (errors.hasErrors()) {
		  throw	new CustomBindException(errors);
		}
		
		SubSchemeContractResponse subSchemeContractResponse = new SubSchemeContractResponse();
		subSchemeContractResponse.setSubSchemes(new ArrayList<SubSchemeContract>());
		for(SubSchemeContract subSchemeContract:subSchemeContractRequest.getSubSchemes())
		{
		
		SubScheme	subSchemeEntity=	modelMapper.map(subSchemeContract, SubScheme.class);
		subSchemeEntity = subSchemeService.create(subSchemeEntity);
		SubSchemeContract resp=modelMapper.map(subSchemeEntity, SubSchemeContract.class);
		subSchemeContract.setId(subSchemeEntity.getId());
		subSchemeContractResponse.getSubSchemes().add(resp);
		}

		subSchemeContractResponse.setResponseInfo(getResponseInfo(subSchemeContractRequest.getRequestInfo()));
		 
		return subSchemeContractResponse;
	}

	@PutMapping(value = "/{uniqueId}")
	@ResponseStatus(HttpStatus.OK)
	public SubSchemeContractResponse update(@RequestBody @Valid SubSchemeContractRequest subSchemeContractRequest, BindingResult errors,
			@PathVariable Long uniqueId) {
		
		subSchemeService.validate(subSchemeContractRequest,"update",errors);
		
		if (errors.hasErrors()) {
			  throw	new CustomBindException(errors);
			}
	 
		SubScheme subSchemeFromDb = subSchemeService.findOne(uniqueId);
		SubSchemeContract subScheme = subSchemeContractRequest.getSubScheme();
		
		ModelMapper model=new ModelMapper();
	 	model.map(subScheme, subSchemeFromDb);
		subSchemeFromDb = subSchemeService.update(subSchemeFromDb);
		SubSchemeContractResponse subSchemeContractResponse = new SubSchemeContractResponse();
		subSchemeContractResponse.setSubScheme(subScheme);  
		subSchemeContractResponse.setResponseInfo(getResponseInfo(subSchemeContractRequest.getRequestInfo()));
		subSchemeContractResponse.getResponseInfo().setStatus(HttpStatus.OK.toString());
		return subSchemeContractResponse;
	}
	
	@GetMapping(value = "/{uniqueId}")
	@ResponseStatus(HttpStatus.OK)
	public SubSchemeContractResponse view(@RequestBody @Valid SubSchemeContractRequest subSchemeContractRequest, BindingResult errors,
			@PathVariable Long uniqueId) {

		if (errors.hasErrors()) {
			  throw	new CustomBindException(errors);
			}
		subSchemeService.validate(subSchemeContractRequest,"view",errors);
		RequestInfo requestInfo = subSchemeContractRequest.getRequestInfo();
		SubScheme subSchemeFromDb = subSchemeService.findOne(uniqueId);
		SubSchemeContract subScheme = subSchemeContractRequest.getSubScheme();
		
		ModelMapper model=new ModelMapper();
	 	model.map(subScheme, subSchemeFromDb);
		
		SubSchemeContractResponse subSchemeContractResponse = new SubSchemeContractResponse();
		subSchemeContractResponse.setSubScheme(subScheme);  
		subSchemeContractResponse.setResponseInfo(getResponseInfo(subSchemeContractRequest.getRequestInfo()));
		subSchemeContractResponse.getResponseInfo().setStatus(HttpStatus.CREATED.toString());
		return subSchemeContractResponse ;
	}
	
	@PutMapping
	@ResponseStatus(HttpStatus.OK)
	public SubSchemeContractResponse updateAll(@RequestBody @Valid SubSchemeContractRequest subSchemeContractRequest, BindingResult errors) {
		subSchemeService.validate(subSchemeContractRequest,"updateAll",errors);
		if (errors.hasErrors()) {
			  throw	new CustomBindException(errors);
			}
		
 
		SubSchemeContractResponse subSchemeContractResponse =new  SubSchemeContractResponse();
		subSchemeContractResponse.setSubSchemes(new ArrayList<SubSchemeContract>());
		for(SubSchemeContract subSchemeContract:subSchemeContractRequest.getSubSchemes())
		{
		SubScheme subSchemeFromDb = subSchemeService.findOne(subSchemeContract.getId());
		 
		
		ModelMapper model=new ModelMapper();
	 	model.map(subSchemeContract, subSchemeFromDb);
		subSchemeFromDb = subSchemeService.update(subSchemeFromDb);
		model.map(subSchemeFromDb,subSchemeContract);
		subSchemeContractResponse.getSubSchemes().add(subSchemeContract);  
		}

		subSchemeContractResponse.setResponseInfo(getResponseInfo(subSchemeContractRequest.getRequestInfo()));
		subSchemeContractResponse.getResponseInfo().setStatus(HttpStatus.OK.toString());
		
		return subSchemeContractResponse;
	}
	

	@GetMapping
	@ResponseBody
	@ResponseStatus(HttpStatus.OK)
	public SubSchemeContractResponse search(@ModelAttribute SubSchemeContractRequest subSchemeContractRequest,BindingResult errors) {
		subSchemeService.validate(subSchemeContractRequest,"search",errors);
		if (errors.hasErrors()) {
			  throw	new CustomBindException(errors);
			}
		SubSchemeContractResponse subSchemeContractResponse =new  SubSchemeContractResponse();
		subSchemeContractResponse.setSubSchemes(new ArrayList<SubSchemeContract>());
		subSchemeContractResponse.setPage(new Pagination());
		Page<SubScheme> allSubSchemes;
		ModelMapper model=new ModelMapper();
	 
		allSubSchemes = subSchemeService.search(subSchemeContractRequest);
		SubSchemeContract subSchemeContract=null;
		for(SubScheme b:allSubSchemes)
		{
			subSchemeContract=new SubSchemeContract();
			model.map(b, subSchemeContract);
			subSchemeContractResponse.getSubSchemes().add(subSchemeContract);
		}
		subSchemeContractResponse.getPage().map(allSubSchemes);
		subSchemeContractResponse.setResponseInfo(getResponseInfo(subSchemeContractRequest.getRequestInfo()));
		subSchemeContractResponse.getResponseInfo().setStatus(HttpStatus.OK.toString());
		return subSchemeContractResponse;
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