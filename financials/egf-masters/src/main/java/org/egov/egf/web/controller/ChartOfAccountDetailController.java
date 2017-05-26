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
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/chartofaccountdetails")
public class ChartOfAccountDetailController {
	@Autowired
	private ChartOfAccountDetailService chartOfAccountDetailService;

	@PostMapping("/_create")
	@ResponseStatus(HttpStatus.CREATED)
	public ChartOfAccountDetailContractResponse create(
			@RequestBody @Valid ChartOfAccountDetailContractRequest chartOfAccountDetailContractRequest,
			BindingResult errors) {
		chartOfAccountDetailService.validate(chartOfAccountDetailContractRequest, "create", errors);
		if (errors.hasErrors()) {
			throw new CustomBindException(errors);
		}
		chartOfAccountDetailContractRequest.getRequestInfo().setAction("create");
		chartOfAccountDetailService.fetchRelatedContracts(chartOfAccountDetailContractRequest);
		chartOfAccountDetailService.push(chartOfAccountDetailContractRequest);
		ChartOfAccountDetailContractResponse chartOfAccountDetailContractResponse = new ChartOfAccountDetailContractResponse();
		chartOfAccountDetailContractResponse.setChartOfAccountDetails(new ArrayList<ChartOfAccountDetailContract>());
		if (chartOfAccountDetailContractRequest.getChartOfAccountDetails() != null
				&& !chartOfAccountDetailContractRequest.getChartOfAccountDetails().isEmpty()) {
			for (ChartOfAccountDetailContract chartOfAccountDetailContract : chartOfAccountDetailContractRequest
					.getChartOfAccountDetails()) {
				chartOfAccountDetailContractResponse.getChartOfAccountDetails().add(chartOfAccountDetailContract);
			}
		} else if (chartOfAccountDetailContractRequest.getChartOfAccountDetail() != null) {
			chartOfAccountDetailContractResponse
					.setChartOfAccountDetail(chartOfAccountDetailContractRequest.getChartOfAccountDetail());
		}
		chartOfAccountDetailContractResponse
				.setResponseInfo(getResponseInfo(chartOfAccountDetailContractRequest.getRequestInfo()));
		chartOfAccountDetailContractResponse.getResponseInfo().setStatus(HttpStatus.OK.toString());
		return chartOfAccountDetailContractResponse;
	}

	@PostMapping(value = "/{uniqueId}/_update")
	@ResponseStatus(HttpStatus.OK)
	public ChartOfAccountDetailContractResponse update(
			@RequestBody @Valid ChartOfAccountDetailContractRequest chartOfAccountDetailContractRequest,
			BindingResult errors, @PathVariable Long uniqueId) {

		chartOfAccountDetailService.validate(chartOfAccountDetailContractRequest, "update", errors);

		if (errors.hasErrors()) {
			throw new CustomBindException(errors);
		}
		chartOfAccountDetailContractRequest.getRequestInfo().setAction("update");
		chartOfAccountDetailService.fetchRelatedContracts(chartOfAccountDetailContractRequest);
		chartOfAccountDetailContractRequest.getChartOfAccountDetail().setId(uniqueId);
		chartOfAccountDetailService.push(chartOfAccountDetailContractRequest);
		chartOfAccountDetailContractRequest.getChartOfAccountDetail().setId(uniqueId);
		ChartOfAccountDetailContractResponse chartOfAccountDetailContractResponse = new ChartOfAccountDetailContractResponse();
		chartOfAccountDetailContractResponse
				.setChartOfAccountDetail(chartOfAccountDetailContractRequest.getChartOfAccountDetail());
		chartOfAccountDetailContractResponse
				.setResponseInfo(getResponseInfo(chartOfAccountDetailContractRequest.getRequestInfo()));
		chartOfAccountDetailContractResponse.getResponseInfo().setStatus(HttpStatus.OK.toString());
		return chartOfAccountDetailContractResponse;
	}

	@GetMapping(value = "/{uniqueId}")
	@ResponseStatus(HttpStatus.OK)
	public ChartOfAccountDetailContractResponse view(
			@ModelAttribute ChartOfAccountDetailContractRequest chartOfAccountDetailContractRequest,
			BindingResult errors, @PathVariable Long uniqueId) {
		chartOfAccountDetailService.validate(chartOfAccountDetailContractRequest, "view", errors);
		if (errors.hasErrors()) {
			throw new CustomBindException(errors);
		}
		chartOfAccountDetailService.fetchRelatedContracts(chartOfAccountDetailContractRequest);
		ChartOfAccountDetail chartOfAccountDetailFromDb = chartOfAccountDetailService.findOne(uniqueId);
		ChartOfAccountDetailContract chartOfAccountDetail = chartOfAccountDetailContractRequest
				.getChartOfAccountDetail();

		ModelMapper model = new ModelMapper();
		model.map(chartOfAccountDetailFromDb, chartOfAccountDetail);

		ChartOfAccountDetailContractResponse chartOfAccountDetailContractResponse = new ChartOfAccountDetailContractResponse();
		chartOfAccountDetailContractResponse.setChartOfAccountDetail(chartOfAccountDetail);
		chartOfAccountDetailContractResponse
				.setResponseInfo(getResponseInfo(chartOfAccountDetailContractRequest.getRequestInfo()));
		chartOfAccountDetailContractResponse.getResponseInfo().setStatus(HttpStatus.CREATED.toString());
		return chartOfAccountDetailContractResponse;
	}

	@PostMapping("/_update")
	@ResponseStatus(HttpStatus.OK)
	public ChartOfAccountDetailContractResponse updateAll(
			@RequestBody @Valid ChartOfAccountDetailContractRequest chartOfAccountDetailContractRequest,
			BindingResult errors) {
		chartOfAccountDetailService.validate(chartOfAccountDetailContractRequest, "updateAll", errors);
		if (errors.hasErrors()) {
			throw new CustomBindException(errors);
		}
		chartOfAccountDetailContractRequest.getRequestInfo().setAction("updateAll");
		chartOfAccountDetailService.fetchRelatedContracts(chartOfAccountDetailContractRequest);
		chartOfAccountDetailService.push(chartOfAccountDetailContractRequest);
		ChartOfAccountDetailContractResponse chartOfAccountDetailContractResponse = new ChartOfAccountDetailContractResponse();
		chartOfAccountDetailContractResponse.setChartOfAccountDetails(new ArrayList<ChartOfAccountDetailContract>());
		for (ChartOfAccountDetailContract chartOfAccountDetailContract : chartOfAccountDetailContractRequest
				.getChartOfAccountDetails()) {
			chartOfAccountDetailContractResponse.getChartOfAccountDetails().add(chartOfAccountDetailContract);
		}

		chartOfAccountDetailContractResponse
				.setResponseInfo(getResponseInfo(chartOfAccountDetailContractRequest.getRequestInfo()));
		chartOfAccountDetailContractResponse.getResponseInfo().setStatus(HttpStatus.OK.toString());

		return chartOfAccountDetailContractResponse;
	}

	@PostMapping("/_search")
	@ResponseBody
	@ResponseStatus(HttpStatus.OK)
	public ChartOfAccountDetailContractResponse search(
			@ModelAttribute ChartOfAccountDetailContract chartOfAccountDetailContracts,
			@RequestBody RequestInfo requestInfo, BindingResult errors) {
		ChartOfAccountDetailContractRequest chartOfAccountDetailContractRequest = new ChartOfAccountDetailContractRequest();
		chartOfAccountDetailContractRequest.setChartOfAccountDetail(chartOfAccountDetailContracts);
		chartOfAccountDetailContractRequest.setRequestInfo(requestInfo);
		chartOfAccountDetailService.validate(chartOfAccountDetailContractRequest, "search", errors);
		if (errors.hasErrors()) {
			throw new CustomBindException(errors);
		}
		chartOfAccountDetailService.fetchRelatedContracts(chartOfAccountDetailContractRequest);
		ChartOfAccountDetailContractResponse chartOfAccountDetailContractResponse = new ChartOfAccountDetailContractResponse();
		chartOfAccountDetailContractResponse.setChartOfAccountDetails(new ArrayList<ChartOfAccountDetailContract>());
		chartOfAccountDetailContractResponse.setPage(new Pagination());
		Page<ChartOfAccountDetail> allChartOfAccountDetails;
		ModelMapper model = new ModelMapper();

		allChartOfAccountDetails = chartOfAccountDetailService.search(chartOfAccountDetailContractRequest);
		ChartOfAccountDetailContract chartOfAccountDetailContract = null;
		for (ChartOfAccountDetail b : allChartOfAccountDetails) {
			chartOfAccountDetailContract = new ChartOfAccountDetailContract();
			model.map(b, chartOfAccountDetailContract);
			chartOfAccountDetailContractResponse.getChartOfAccountDetails().add(chartOfAccountDetailContract);
		}
		chartOfAccountDetailContractResponse.getPage().map(allChartOfAccountDetails);
		chartOfAccountDetailContractResponse
				.setResponseInfo(getResponseInfo(chartOfAccountDetailContractRequest.getRequestInfo()));
		chartOfAccountDetailContractResponse.getResponseInfo().setStatus(HttpStatus.OK.toString());
		return chartOfAccountDetailContractResponse;
	}

	private ResponseInfo getResponseInfo(RequestInfo requestInfo) {
		new ResponseInfo();
		return ResponseInfo.builder().apiId(requestInfo.getApiId()).ver(requestInfo.getVer()).ts(new Date())
				.resMsgId(requestInfo.getMsgId()).resMsgId("placeholder").status("placeholder").build();
	}

}