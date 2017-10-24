package org.egov.works.estimate.web.controller;

import org.egov.works.commons.web.contract.RequestInfo;
import org.egov.works.estimate.domain.service.AbstractEstimateService;
import org.egov.works.estimate.web.contract.AbstractEstimateRequest;
import org.egov.works.estimate.web.contract.AbstractEstimateResponse;
import org.egov.works.estimate.web.contract.AbstractEstimateSearchContract;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
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
	@ResponseStatus(HttpStatus.CREATED)
	public AbstractEstimateResponse create(@RequestBody AbstractEstimateRequest abstractEstimateRequest,
			BindingResult errors, @RequestParam String tenantId) {
		// do some logic here
		return abstractEstimateService.create(abstractEstimateRequest);
	}

	@PostMapping("/_update")
	@ResponseStatus(HttpStatus.CREATED)
	public AbstractEstimateResponse update(@RequestBody AbstractEstimateRequest abstractEstimateRequest,
			BindingResult errors, @RequestParam String tenantId) {
		// do some logic here
		return abstractEstimateService.create(abstractEstimateRequest);
	}

	@PostMapping("/_search")
	@ResponseStatus(HttpStatus.CREATED)
	public AbstractEstimateResponse search(
			@ModelAttribute AbstractEstimateSearchContract abstractEstimateSearchContract,
			@RequestBody RequestInfo requestInfo, BindingResult errors, @RequestParam String tenantId) {
		// do some logic here
		return abstractEstimateService.search(abstractEstimateSearchContract);
	}
}
