package org.egov.works.estimate.web.controller;

import javax.validation.Valid;

import org.egov.works.estimate.domain.service.AbstractEstimateService;
import org.egov.works.estimate.web.contract.AbstractEstimateRequest;
import org.egov.works.estimate.web.contract.AbstractEstimateResponse;
import org.egov.works.estimate.web.contract.AbstractEstimateSearchContract;
import org.egov.works.estimate.web.contract.RequestInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/abstractestimates")
public class AbstractEstimateController {

	@Autowired
	private AbstractEstimateService abstractEstimateService;
	
	@PostMapping("/_create")
	@ResponseStatus(HttpStatus.OK)
	public AbstractEstimateResponse create(@RequestBody @Valid AbstractEstimateRequest abstractEstimateRequest) {
		return abstractEstimateService.create(abstractEstimateRequest);
	}

	@PostMapping("/_update")
	@ResponseStatus(HttpStatus.OK)
	public AbstractEstimateResponse update(@RequestBody @Valid AbstractEstimateRequest abstractEstimateRequest) {
		return abstractEstimateService.update(abstractEstimateRequest);
	}

	@PostMapping("/_search")
	@ResponseStatus(HttpStatus.OK)
	public AbstractEstimateResponse search(
			@ModelAttribute @Valid AbstractEstimateSearchContract abstractEstimateSearchContract,
			@RequestBody RequestInfo requestInfo,
			@RequestParam(required = true) String tenantId) {
		return abstractEstimateService.search(abstractEstimateSearchContract, requestInfo);
	}
}
