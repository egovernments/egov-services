package org.egov.egf.web.controller;

import java.util.ArrayList;
import java.util.Date;

import javax.validation.Valid;

import org.egov.egf.domain.exception.CustomBindException;
import org.egov.egf.persistence.entity.Fund;
import org.egov.egf.persistence.queue.contract.FundContract;
import org.egov.egf.persistence.queue.contract.FundContractRequest;
import org.egov.egf.persistence.queue.contract.FundContractResponse;
import org.egov.egf.persistence.queue.contract.Pagination;
import org.egov.egf.persistence.queue.contract.RequestInfo;
import org.egov.egf.persistence.queue.contract.ResponseInfo;
import org.egov.egf.persistence.service.FundService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/funds")
public class FundController {
	@Autowired
	private FundService fundService;

	@PostMapping("/_create")
	@ResponseStatus(HttpStatus.CREATED)
	public FundContractResponse create(@RequestBody @Valid FundContractRequest fundContractRequest,
			BindingResult errors) {
		fundService.validate(fundContractRequest, "create", errors);
		if (errors.hasErrors()) {
			throw new CustomBindException(errors);
		}
		fundContractRequest.getRequestInfo().setAction("create");
		fundService.fetchRelatedContracts(fundContractRequest);
		fundService.push(fundContractRequest);
		FundContractResponse fundContractResponse = new FundContractResponse();
		fundContractResponse.setFunds(new ArrayList<FundContract>());
		if (fundContractRequest.getFunds() != null && !fundContractRequest.getFunds().isEmpty()) {
			for (FundContract fundContract : fundContractRequest.getFunds()) {
				fundContractResponse.getFunds().add(fundContract);
			}
		} else if (fundContractRequest.getFund() != null) {
			fundContractResponse.setFund(fundContractRequest.getFund());
		}
		fundContractResponse.setResponseInfo(getResponseInfo(fundContractRequest.getRequestInfo()));
		fundContractResponse.getResponseInfo().setStatus(HttpStatus.OK.toString());
		return fundContractResponse;
	}

	@PostMapping(value = "/{uniqueId}/_update")
	@ResponseStatus(HttpStatus.OK)
	public FundContractResponse update(@RequestBody @Valid FundContractRequest fundContractRequest,
			BindingResult errors, @PathVariable Long uniqueId) {

		fundService.validate(fundContractRequest, "update", errors);

		if (errors.hasErrors()) {
			throw new CustomBindException(errors);
		}
		fundContractRequest.getRequestInfo().setAction("update");
		fundService.fetchRelatedContracts(fundContractRequest);
		fundContractRequest.getFund().setId(uniqueId);
		fundService.push(fundContractRequest);
		FundContractResponse fundContractResponse = new FundContractResponse();
		fundContractResponse.setFund(fundContractRequest.getFund());
		fundContractResponse.setResponseInfo(getResponseInfo(fundContractRequest.getRequestInfo()));
		fundContractResponse.getResponseInfo().setStatus(HttpStatus.OK.toString());
		return fundContractResponse;
	}

	// @GetMapping(value = "/{uniqueId}")
	@ResponseStatus(HttpStatus.OK)
	public FundContractResponse view(@ModelAttribute FundContractRequest fundContractRequest, BindingResult errors,
			@PathVariable Long uniqueId) {
		fundService.validate(fundContractRequest, "view", errors);
		if (errors.hasErrors()) {
			throw new CustomBindException(errors);
		}
		fundService.fetchRelatedContracts(fundContractRequest);
		Fund fundFromDb = fundService.findOne(uniqueId);
		FundContract fund = fundContractRequest.getFund();

		ModelMapper model = new ModelMapper();
		model.map(fundFromDb, fund);

		FundContractResponse fundContractResponse = new FundContractResponse();
		fundContractResponse.setFund(fund);
		fundContractResponse.setResponseInfo(getResponseInfo(fundContractRequest.getRequestInfo()));
		fundContractResponse.getResponseInfo().setStatus(HttpStatus.CREATED.toString());
		return fundContractResponse;
	}

	@PostMapping("/_update")
	@ResponseStatus(HttpStatus.OK)
	public FundContractResponse updateAll(@RequestBody @Valid FundContractRequest fundContractRequest,
			BindingResult errors) {
		fundService.validate(fundContractRequest, "updateAll", errors);
		if (errors.hasErrors()) {
			throw new CustomBindException(errors);
		}
		fundContractRequest.getRequestInfo().setAction("updateAll");
		fundService.fetchRelatedContracts(fundContractRequest);
		fundService.push(fundContractRequest);
		FundContractResponse fundContractResponse = new FundContractResponse();
		fundContractResponse.setFunds(new ArrayList<FundContract>());
		for (FundContract fundContract : fundContractRequest.getFunds()) {
			fundContractResponse.getFunds().add(fundContract);
		}
		fundContractResponse.setResponseInfo(getResponseInfo(fundContractRequest.getRequestInfo()));
		fundContractResponse.getResponseInfo().setStatus(HttpStatus.OK.toString());

		return fundContractResponse;
	}

	@PostMapping("/_search")
	@ResponseBody
	@ResponseStatus(HttpStatus.OK)
	public FundContractResponse search(@ModelAttribute FundContract fundContracts, @RequestBody RequestInfo requestInfo,
			BindingResult errors) {
		String tenantId = fundContracts.getTenantId();
		final FundContractRequest fundContractRequest = new FundContractRequest();
		fundContractRequest.setFund(fundContracts);
		fundContractRequest.setRequestInfo(requestInfo);
		fundService.validate(fundContractRequest, "search", errors);
		if (errors.hasErrors()) {
			throw new CustomBindException(errors);
		}
		// fundService.fetchRelatedContracts(fundContractRequest);
		FundContractResponse fundContractResponse = new FundContractResponse();
		fundContractResponse.setFunds(new ArrayList<FundContract>());
		fundContractResponse.setPage(new Pagination());
		Page<Fund> allFunds;
		ModelMapper model = new ModelMapper();

		allFunds = fundService.search(fundContractRequest);
		FundContract fundContract = null;
		for (Fund b : allFunds) {
			fundContract = new FundContract();
			model.map(b, fundContract);
			fundContractResponse.getFunds().add(fundContract);
		}
		fundContractResponse.getPage().map(allFunds);
		fundContractResponse.setResponseInfo(getResponseInfo(fundContractRequest.getRequestInfo()));
		fundContractResponse.getResponseInfo().setStatus(HttpStatus.OK.toString());
		return fundContractResponse;
	}

	private ResponseInfo getResponseInfo(RequestInfo requestInfo) {
		new ResponseInfo();
		return ResponseInfo.builder().apiId(requestInfo.getApiId()).ver(requestInfo.getVer()).ts(new Date())
				.resMsgId(requestInfo.getMsgId()).resMsgId("placeholder").status("placeholder").build();
	}

}