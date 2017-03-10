package org.egov.egf.web.controller;

import java.util.ArrayList;
import java.util.Date;

import javax.validation.Valid;

import org.egov.egf.domain.exception.CustomBindException;
import org.egov.egf.persistence.entity.FiscalPeriod;
import org.egov.egf.persistence.queue.contract.FiscalPeriodContract;
import org.egov.egf.persistence.queue.contract.FiscalPeriodContractRequest;
import org.egov.egf.persistence.queue.contract.FiscalPeriodContractResponse;
import org.egov.egf.persistence.queue.contract.Pagination;
import org.egov.egf.persistence.queue.contract.RequestInfo;
import org.egov.egf.persistence.queue.contract.ResponseInfo;
import org.egov.egf.persistence.service.FiscalPeriodService;
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
@RequestMapping("/fiscalperiods")  
public class FiscalPeriodController {
	@Autowired
	private FiscalPeriodService  fiscalPeriodService;

	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	public  FiscalPeriodContractResponse create(@RequestBody @Valid FiscalPeriodContractRequest fiscalPeriodContractRequest, BindingResult errors) {
		ModelMapper modelMapper=new ModelMapper();
		fiscalPeriodService.validate(fiscalPeriodContractRequest,"create",errors);
		if (errors.hasErrors()) {
		  throw	new CustomBindException(errors);
		}
		
		FiscalPeriodContractResponse fiscalPeriodContractResponse = new FiscalPeriodContractResponse();
		fiscalPeriodContractResponse.setFiscalPeriods(new ArrayList<FiscalPeriodContract>());
		for(FiscalPeriodContract fiscalPeriodContract:fiscalPeriodContractRequest.getFiscalPeriods())
		{
		
		FiscalPeriod	fiscalPeriodEntity=	modelMapper.map(fiscalPeriodContract, FiscalPeriod.class);
		fiscalPeriodEntity = fiscalPeriodService.create(fiscalPeriodEntity);
		FiscalPeriodContract resp=modelMapper.map(fiscalPeriodEntity, FiscalPeriodContract.class);
		fiscalPeriodContract.setId(fiscalPeriodEntity.getId());
		fiscalPeriodContractResponse.getFiscalPeriods().add(resp);
		}

		fiscalPeriodContractResponse.setResponseInfo(getResponseInfo(fiscalPeriodContractRequest.getRequestInfo()));
		 
		return fiscalPeriodContractResponse;
	}

	@PutMapping(value = "/{uniqueId}")
	@ResponseStatus(HttpStatus.OK)
	public FiscalPeriodContractResponse update(@RequestBody @Valid FiscalPeriodContractRequest fiscalPeriodContractRequest, BindingResult errors,
			@PathVariable Long uniqueId) {
		
		fiscalPeriodService.validate(fiscalPeriodContractRequest,"update",errors);
		
		if (errors.hasErrors()) {
			  throw	new CustomBindException(errors);
			}
	 
		FiscalPeriod fiscalPeriodFromDb = fiscalPeriodService.findOne(uniqueId);
		FiscalPeriodContract fiscalPeriod = fiscalPeriodContractRequest.getFiscalPeriod();
		
		ModelMapper model=new ModelMapper();
	 	model.map(fiscalPeriod, fiscalPeriodFromDb);
		fiscalPeriodFromDb = fiscalPeriodService.update(fiscalPeriodFromDb);
		FiscalPeriodContractResponse fiscalPeriodContractResponse = new FiscalPeriodContractResponse();
		fiscalPeriodContractResponse.setFiscalPeriod(fiscalPeriod);  
		fiscalPeriodContractResponse.setResponseInfo(getResponseInfo(fiscalPeriodContractRequest.getRequestInfo()));
		fiscalPeriodContractResponse.getResponseInfo().setStatus(HttpStatus.OK.toString());
		return fiscalPeriodContractResponse;
	}
	
	@GetMapping(value = "/{uniqueId}")
	@ResponseStatus(HttpStatus.OK)
	public FiscalPeriodContractResponse view(@RequestBody @Valid FiscalPeriodContractRequest fiscalPeriodContractRequest, BindingResult errors,
			@PathVariable Long uniqueId) {

		if (errors.hasErrors()) {
			  throw	new CustomBindException(errors);
			}
		fiscalPeriodService.validate(fiscalPeriodContractRequest,"view",errors);
		RequestInfo requestInfo = fiscalPeriodContractRequest.getRequestInfo();
		FiscalPeriod fiscalPeriodFromDb = fiscalPeriodService.findOne(uniqueId);
		FiscalPeriodContract fiscalPeriod = fiscalPeriodContractRequest.getFiscalPeriod();
		
		ModelMapper model=new ModelMapper();
	 	model.map(fiscalPeriod, fiscalPeriodFromDb);
		
		FiscalPeriodContractResponse fiscalPeriodContractResponse = new FiscalPeriodContractResponse();
		fiscalPeriodContractResponse.setFiscalPeriod(fiscalPeriod);  
		fiscalPeriodContractResponse.setResponseInfo(getResponseInfo(fiscalPeriodContractRequest.getRequestInfo()));
		fiscalPeriodContractResponse.getResponseInfo().setStatus(HttpStatus.CREATED.toString());
		return fiscalPeriodContractResponse ;
	}
	
	@PutMapping
	@ResponseStatus(HttpStatus.OK)
	public FiscalPeriodContractResponse updateAll(@RequestBody @Valid FiscalPeriodContractRequest fiscalPeriodContractRequest, BindingResult errors) {
		fiscalPeriodService.validate(fiscalPeriodContractRequest,"updateAll",errors);
		if (errors.hasErrors()) {
			  throw	new CustomBindException(errors);
			}
		
 
		FiscalPeriodContractResponse fiscalPeriodContractResponse =new  FiscalPeriodContractResponse();
		fiscalPeriodContractResponse.setFiscalPeriods(new ArrayList<FiscalPeriodContract>());
		for(FiscalPeriodContract fiscalPeriodContract:fiscalPeriodContractRequest.getFiscalPeriods())
		{
		FiscalPeriod fiscalPeriodFromDb = fiscalPeriodService.findOne(fiscalPeriodContract.getId());
		 
		
		ModelMapper model=new ModelMapper();
	 	model.map(fiscalPeriodContract, fiscalPeriodFromDb);
		fiscalPeriodFromDb = fiscalPeriodService.update(fiscalPeriodFromDb);
		model.map(fiscalPeriodFromDb,fiscalPeriodContract);
		fiscalPeriodContractResponse.getFiscalPeriods().add(fiscalPeriodContract);  
		}

		fiscalPeriodContractResponse.setResponseInfo(getResponseInfo(fiscalPeriodContractRequest.getRequestInfo()));
		fiscalPeriodContractResponse.getResponseInfo().setStatus(HttpStatus.OK.toString());
		
		return fiscalPeriodContractResponse;
	}
	

	@GetMapping
	@ResponseBody
	@ResponseStatus(HttpStatus.OK)
	public FiscalPeriodContractResponse search(@ModelAttribute FiscalPeriodContractRequest fiscalPeriodContractRequest,BindingResult errors) {
		fiscalPeriodService.validate(fiscalPeriodContractRequest,"search",errors);
		if (errors.hasErrors()) {
			  throw	new CustomBindException(errors);
			}
		FiscalPeriodContractResponse fiscalPeriodContractResponse =new  FiscalPeriodContractResponse();
		fiscalPeriodContractResponse.setFiscalPeriods(new ArrayList<FiscalPeriodContract>());
		fiscalPeriodContractResponse.setPage(new Pagination());
		Page<FiscalPeriod> allFiscalPeriods;
		ModelMapper model=new ModelMapper();
	 
		allFiscalPeriods = fiscalPeriodService.search(fiscalPeriodContractRequest);
		FiscalPeriodContract fiscalPeriodContract=null;
		for(FiscalPeriod b:allFiscalPeriods)
		{
			fiscalPeriodContract=new FiscalPeriodContract();
			model.map(b, fiscalPeriodContract);
			fiscalPeriodContractResponse.getFiscalPeriods().add(fiscalPeriodContract);
		}
		fiscalPeriodContractResponse.getPage().map(allFiscalPeriods);
		fiscalPeriodContractResponse.setResponseInfo(getResponseInfo(fiscalPeriodContractRequest.getRequestInfo()));
		fiscalPeriodContractResponse.getResponseInfo().setStatus(HttpStatus.OK.toString());
		return fiscalPeriodContractResponse;
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