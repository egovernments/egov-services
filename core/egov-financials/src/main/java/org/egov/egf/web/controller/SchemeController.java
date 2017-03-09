package org.egov.egf.web.controller;

import java.util.ArrayList;
import java.util.Date;

import javax.validation.Valid;

import org.egov.egf.domain.exception.CustomBindException;
import org.egov.egf.persistence.entity.Scheme;
import org.egov.egf.persistence.queue.contract.SchemeContract;
import org.egov.egf.persistence.queue.contract.SchemeContractRequest;
import org.egov.egf.persistence.queue.contract.SchemeContractResponse;
import org.egov.egf.persistence.queue.contract.Pagination;
import org.egov.egf.persistence.queue.contract.RequestInfo;
import org.egov.egf.persistence.queue.contract.ResponseInfo;
import org.egov.egf.persistence.service.SchemeService;
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
@RequestMapping("/schemes")  
public class SchemeController {
	@Autowired
	private SchemeService  schemeService;

	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	public  SchemeContractResponse create(@RequestBody @Valid SchemeContractRequest schemeContractRequest, BindingResult errors) {
		ModelMapper modelMapper=new ModelMapper();
		schemeService.validate(schemeContractRequest,"create",errors);
		if (errors.hasErrors()) {
		  throw	new CustomBindException(errors);
		}
		schemeService.fetchRelatedContracts(schemeContractRequest);
		SchemeContractResponse schemeContractResponse = new SchemeContractResponse();
		schemeContractResponse.setSchemes(new ArrayList<SchemeContract>());
		for(SchemeContract schemeContract:schemeContractRequest.getSchemes())
		{
		
		Scheme	schemeEntity=	modelMapper.map(schemeContract, Scheme.class);
		schemeEntity = schemeService.create(schemeEntity);
		SchemeContract resp=modelMapper.map(schemeEntity, SchemeContract.class);
		schemeContract.setId(schemeEntity.getId());
		schemeContractResponse.getSchemes().add(resp);
		}

		schemeContractResponse.setResponseInfo(getResponseInfo(schemeContractRequest.getRequestInfo()));
		 
		return schemeContractResponse;
	}

	@PutMapping(value = "/{uniqueId}")
	@ResponseStatus(HttpStatus.OK)
	public SchemeContractResponse update(@RequestBody @Valid SchemeContractRequest schemeContractRequest, BindingResult errors,
			@PathVariable Long uniqueId) {
		
		schemeService.validate(schemeContractRequest,"update",errors);
		
		if (errors.hasErrors()) {
			  throw	new CustomBindException(errors);
			}
		schemeService.fetchRelatedContracts(schemeContractRequest);
		Scheme schemeFromDb = schemeService.findOne(uniqueId);
		
		SchemeContract scheme = schemeContractRequest.getScheme();
		//ignoring internally passed id if the put has id in url
	    scheme.setId(uniqueId);
		ModelMapper model=new ModelMapper();
	 	model.map(scheme, schemeFromDb);
		schemeFromDb = schemeService.update(schemeFromDb);
		SchemeContractResponse schemeContractResponse = new SchemeContractResponse();
		schemeContractResponse.setScheme(scheme);  
		schemeContractResponse.setResponseInfo(getResponseInfo(schemeContractRequest.getRequestInfo()));
		schemeContractResponse.getResponseInfo().setStatus(HttpStatus.OK.toString());
		return schemeContractResponse;
	}
	
	@GetMapping(value = "/{uniqueId}")
	@ResponseStatus(HttpStatus.OK)
	public SchemeContractResponse view(@ModelAttribute SchemeContractRequest schemeContractRequest, BindingResult errors,
			@PathVariable Long uniqueId) {
		schemeService.validate(schemeContractRequest,"view",errors);
		if (errors.hasErrors()) {
			  throw	new CustomBindException(errors);
			}
		schemeService.fetchRelatedContracts(schemeContractRequest);
		RequestInfo requestInfo = schemeContractRequest.getRequestInfo();
		Scheme schemeFromDb = schemeService.findOne(uniqueId);
		SchemeContract scheme = schemeContractRequest.getScheme();
		
		ModelMapper model=new ModelMapper();
	 	model.map(schemeFromDb,scheme );
		
		SchemeContractResponse schemeContractResponse = new SchemeContractResponse();
		schemeContractResponse.setScheme(scheme);  
		schemeContractResponse.setResponseInfo(getResponseInfo(schemeContractRequest.getRequestInfo()));
		schemeContractResponse.getResponseInfo().setStatus(HttpStatus.CREATED.toString());
		return schemeContractResponse ;
	}
	
	@PutMapping
	@ResponseStatus(HttpStatus.OK)
	public SchemeContractResponse updateAll(@RequestBody @Valid SchemeContractRequest schemeContractRequest, BindingResult errors) {
		schemeService.validate(schemeContractRequest,"updateAll",errors);
		if (errors.hasErrors()) {
			  throw	new CustomBindException(errors);
			}
		schemeService.fetchRelatedContracts(schemeContractRequest);		
 
		SchemeContractResponse schemeContractResponse =new  SchemeContractResponse();
		schemeContractResponse.setSchemes(new ArrayList<SchemeContract>());
		for(SchemeContract schemeContract:schemeContractRequest.getSchemes())
		{
		Scheme schemeFromDb = schemeService.findOne(schemeContract.getId());
		
		ModelMapper model=new ModelMapper();
	 	model.map(schemeContract, schemeFromDb);
		schemeFromDb = schemeService.update(schemeFromDb);
		model.map(schemeFromDb,schemeContract);
		schemeContractResponse.getSchemes().add(schemeContract);  
		}

		schemeContractResponse.setResponseInfo(getResponseInfo(schemeContractRequest.getRequestInfo()));
		schemeContractResponse.getResponseInfo().setStatus(HttpStatus.OK.toString());
		
		return schemeContractResponse;
	}
	

	@GetMapping
	@ResponseBody
	@ResponseStatus(HttpStatus.OK)
	public SchemeContractResponse search(@ModelAttribute SchemeContractRequest schemeContractRequest,BindingResult errors) {
		schemeService.validate(schemeContractRequest,"search",errors);
		if (errors.hasErrors()) {
			  throw	new CustomBindException(errors);
			}
		schemeService.fetchRelatedContracts(schemeContractRequest);
		SchemeContractResponse schemeContractResponse =new  SchemeContractResponse();
		schemeContractResponse.setSchemes(new ArrayList<SchemeContract>());
		schemeContractResponse.setPage(new Pagination());
		Page<Scheme> allSchemes;
		ModelMapper model=new ModelMapper();
	 
		allSchemes = schemeService.search(schemeContractRequest);
		SchemeContract schemeContract=null;
		for(Scheme b:allSchemes)
		{
			schemeContract=new SchemeContract();
			model.map(b, schemeContract);
			schemeContractResponse.getSchemes().add(schemeContract);
		}
		schemeContractResponse.getPage().map(allSchemes);
		schemeContractResponse.setResponseInfo(getResponseInfo(schemeContractRequest.getRequestInfo()));
		schemeContractResponse.getResponseInfo().setStatus(HttpStatus.OK.toString());
		return schemeContractResponse;
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