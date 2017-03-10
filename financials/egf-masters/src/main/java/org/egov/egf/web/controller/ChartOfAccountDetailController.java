package org.egov.egf.web.controller;

import java.util.ArrayList;
import java.util.Date;

import javax.validation.Valid;

import org.egov.egf.domain.exception.CustomBindException;
import org.egov.egf.persistence.entity.ChartOfAccountDetail;
import org.egov.egf.persistence.queue.contract.ChartOfAccountDetailContract;
import org.egov.egf.persistence.queue.contract.ChartOfAccountDetailContractRequest;
import org.egov.egf.persistence.queue.contract.ChartOfAccountDetailContractResponse;
import org.egov.egf.persistence.queue.contract.Pagination;
import org.egov.egf.persistence.queue.contract.RequestInfo;
import org.egov.egf.persistence.queue.contract.ResponseInfo;
import org.egov.egf.persistence.service.ChartOfAccountDetailService;
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
@RequestMapping("/chartofaccountdetails")  
public class ChartOfAccountDetailController {
	@Autowired
	private ChartOfAccountDetailService  chartOfAccountDetailService;

	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	public  ChartOfAccountDetailContractResponse create(@RequestBody @Valid ChartOfAccountDetailContractRequest chartOfAccountDetailContractRequest, BindingResult errors) {
		ModelMapper modelMapper=new ModelMapper();
		chartOfAccountDetailService.validate(chartOfAccountDetailContractRequest,"create",errors);
		if (errors.hasErrors()) {
		  throw	new CustomBindException(errors);
		}
		
		ChartOfAccountDetailContractResponse chartOfAccountDetailContractResponse = new ChartOfAccountDetailContractResponse();
		chartOfAccountDetailContractResponse.setChartOfAccountDetails(new ArrayList<ChartOfAccountDetailContract>());
		for(ChartOfAccountDetailContract chartOfAccountDetailContract:chartOfAccountDetailContractRequest.getChartOfAccountDetails())
		{
		
		ChartOfAccountDetail	chartOfAccountDetailEntity=	modelMapper.map(chartOfAccountDetailContract, ChartOfAccountDetail.class);
		chartOfAccountDetailEntity = chartOfAccountDetailService.create(chartOfAccountDetailEntity);
		ChartOfAccountDetailContract resp=modelMapper.map(chartOfAccountDetailEntity, ChartOfAccountDetailContract.class);
		chartOfAccountDetailContract.setId(chartOfAccountDetailEntity.getId());
		chartOfAccountDetailContractResponse.getChartOfAccountDetails().add(resp);
		}

		chartOfAccountDetailContractResponse.setResponseInfo(getResponseInfo(chartOfAccountDetailContractRequest.getRequestInfo()));
		 
		return chartOfAccountDetailContractResponse;
	}

	@PutMapping(value = "/{uniqueId}")
	@ResponseStatus(HttpStatus.OK)
	public ChartOfAccountDetailContractResponse update(@RequestBody @Valid ChartOfAccountDetailContractRequest chartOfAccountDetailContractRequest, BindingResult errors,
			@PathVariable Long uniqueId) {
		
		chartOfAccountDetailService.validate(chartOfAccountDetailContractRequest,"update",errors);
		
		if (errors.hasErrors()) {
			  throw	new CustomBindException(errors);
			}
	 
		ChartOfAccountDetail chartOfAccountDetailFromDb = chartOfAccountDetailService.findOne(uniqueId);
		ChartOfAccountDetailContract chartOfAccountDetail = chartOfAccountDetailContractRequest.getChartOfAccountDetail();
		
		ModelMapper model=new ModelMapper();
	 	model.map(chartOfAccountDetail, chartOfAccountDetailFromDb);
		chartOfAccountDetailFromDb = chartOfAccountDetailService.update(chartOfAccountDetailFromDb);
		ChartOfAccountDetailContractResponse chartOfAccountDetailContractResponse = new ChartOfAccountDetailContractResponse();
		chartOfAccountDetailContractResponse.setChartOfAccountDetail(chartOfAccountDetail);  
		chartOfAccountDetailContractResponse.setResponseInfo(getResponseInfo(chartOfAccountDetailContractRequest.getRequestInfo()));
		chartOfAccountDetailContractResponse.getResponseInfo().setStatus(HttpStatus.OK.toString());
		return chartOfAccountDetailContractResponse;
	}
	
	@GetMapping(value = "/{uniqueId}")
	@ResponseStatus(HttpStatus.OK)
	public ChartOfAccountDetailContractResponse view(@RequestBody @Valid ChartOfAccountDetailContractRequest chartOfAccountDetailContractRequest, BindingResult errors,
			@PathVariable Long uniqueId) {

		if (errors.hasErrors()) {
			  throw	new CustomBindException(errors);
			}
		chartOfAccountDetailService.validate(chartOfAccountDetailContractRequest,"view",errors);
		RequestInfo requestInfo = chartOfAccountDetailContractRequest.getRequestInfo();
		ChartOfAccountDetail chartOfAccountDetailFromDb = chartOfAccountDetailService.findOne(uniqueId);
		ChartOfAccountDetailContract chartOfAccountDetail = chartOfAccountDetailContractRequest.getChartOfAccountDetail();
		
		ModelMapper model=new ModelMapper();
	 	model.map(chartOfAccountDetail, chartOfAccountDetailFromDb);
		
		ChartOfAccountDetailContractResponse chartOfAccountDetailContractResponse = new ChartOfAccountDetailContractResponse();
		chartOfAccountDetailContractResponse.setChartOfAccountDetail(chartOfAccountDetail);  
		chartOfAccountDetailContractResponse.setResponseInfo(getResponseInfo(chartOfAccountDetailContractRequest.getRequestInfo()));
		chartOfAccountDetailContractResponse.getResponseInfo().setStatus(HttpStatus.CREATED.toString());
		return chartOfAccountDetailContractResponse ;
	}
	
	@PutMapping
	@ResponseStatus(HttpStatus.OK)
	public ChartOfAccountDetailContractResponse updateAll(@RequestBody @Valid ChartOfAccountDetailContractRequest chartOfAccountDetailContractRequest, BindingResult errors) {
		chartOfAccountDetailService.validate(chartOfAccountDetailContractRequest,"updateAll",errors);
		if (errors.hasErrors()) {
			  throw	new CustomBindException(errors);
			}
		
 
		ChartOfAccountDetailContractResponse chartOfAccountDetailContractResponse =new  ChartOfAccountDetailContractResponse();
		chartOfAccountDetailContractResponse.setChartOfAccountDetails(new ArrayList<ChartOfAccountDetailContract>());
		for(ChartOfAccountDetailContract chartOfAccountDetailContract:chartOfAccountDetailContractRequest.getChartOfAccountDetails())
		{
		ChartOfAccountDetail chartOfAccountDetailFromDb = chartOfAccountDetailService.findOne(chartOfAccountDetailContract.getId());
		 
		
		ModelMapper model=new ModelMapper();
	 	model.map(chartOfAccountDetailContract, chartOfAccountDetailFromDb);
		chartOfAccountDetailFromDb = chartOfAccountDetailService.update(chartOfAccountDetailFromDb);
		model.map(chartOfAccountDetailFromDb,chartOfAccountDetailContract);
		chartOfAccountDetailContractResponse.getChartOfAccountDetails().add(chartOfAccountDetailContract);  
		}

		chartOfAccountDetailContractResponse.setResponseInfo(getResponseInfo(chartOfAccountDetailContractRequest.getRequestInfo()));
		chartOfAccountDetailContractResponse.getResponseInfo().setStatus(HttpStatus.OK.toString());
		
		return chartOfAccountDetailContractResponse;
	}
	

	@GetMapping
	@ResponseBody
	@ResponseStatus(HttpStatus.OK)
	public ChartOfAccountDetailContractResponse search(@ModelAttribute ChartOfAccountDetailContractRequest chartOfAccountDetailContractRequest,BindingResult errors) {
		chartOfAccountDetailService.validate(chartOfAccountDetailContractRequest,"search",errors);
		if (errors.hasErrors()) {
			  throw	new CustomBindException(errors);
			}
		ChartOfAccountDetailContractResponse chartOfAccountDetailContractResponse =new  ChartOfAccountDetailContractResponse();
		chartOfAccountDetailContractResponse.setChartOfAccountDetails(new ArrayList<ChartOfAccountDetailContract>());
		chartOfAccountDetailContractResponse.setPage(new Pagination());
		Page<ChartOfAccountDetail> allChartOfAccountDetails;
		ModelMapper model=new ModelMapper();
	 
		allChartOfAccountDetails = chartOfAccountDetailService.search(chartOfAccountDetailContractRequest);
		ChartOfAccountDetailContract chartOfAccountDetailContract=null;
		for(ChartOfAccountDetail b:allChartOfAccountDetails)
		{
			chartOfAccountDetailContract=new ChartOfAccountDetailContract();
			model.map(b, chartOfAccountDetailContract);
			chartOfAccountDetailContractResponse.getChartOfAccountDetails().add(chartOfAccountDetailContract);
		}
		chartOfAccountDetailContractResponse.getPage().map(allChartOfAccountDetails);
		chartOfAccountDetailContractResponse.setResponseInfo(getResponseInfo(chartOfAccountDetailContractRequest.getRequestInfo()));
		chartOfAccountDetailContractResponse.getResponseInfo().setStatus(HttpStatus.OK.toString());
		return chartOfAccountDetailContractResponse;
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