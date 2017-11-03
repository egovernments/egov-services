package org.egov.works.estimate.web.controller;

import java.util.List;

import javax.validation.Valid;

import org.egov.works.estimate.domain.exception.CustomBindException;
import org.egov.works.estimate.domain.service.AbstractEstimateService;
import org.egov.works.estimate.utils.EstimateUtils;
import org.egov.works.estimate.web.contract.AbstractEstimate;
import org.egov.works.estimate.web.contract.AbstractEstimateRequest;
import org.egov.works.estimate.web.contract.AbstractEstimateResponse;
import org.egov.works.estimate.web.contract.AbstractEstimateSearchContract;
import org.egov.works.estimate.web.contract.RequestInfo;
import org.egov.works.estimate.web.contract.ResponseInfo;
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
	
	@Autowired
	private EstimateUtils estimateUtils;

	@PostMapping("/_create")
	@ResponseStatus(HttpStatus.CREATED)
	public AbstractEstimateResponse create(@RequestBody AbstractEstimateRequest abstractEstimateRequest,
			BindingResult errors, @RequestParam String tenantId) {
		if (errors.hasErrors()) {
			throw new CustomBindException(errors);
		}
		abstractEstimateService.validateEstimates(abstractEstimateRequest, errors, true);
		final List<AbstractEstimate> abstractEstimates = abstractEstimateService.create(abstractEstimateRequest);
		final AbstractEstimateResponse response = new AbstractEstimateResponse();
		response.setAbstractEstimates(abstractEstimates);
		response.setResponseInfo(estimateUtils.getResponseInfo(abstractEstimateRequest.getRequestInfo()));
		return response;
	}

	@PostMapping("/_update")
	@ResponseStatus(HttpStatus.CREATED)
	public AbstractEstimateResponse update(@RequestBody AbstractEstimateRequest abstractEstimateRequest,
			BindingResult errors, @RequestParam String tenantId) {
		abstractEstimateService.validateEstimates(abstractEstimateRequest, errors, false);
		if (errors.hasErrors()) {
			throw new CustomBindException(errors);
		}
		final List<AbstractEstimate> abstractEstimates = abstractEstimateService.update(abstractEstimateRequest);
		final AbstractEstimateResponse response = new AbstractEstimateResponse();
		response.setAbstractEstimates(abstractEstimates);
		response.setResponseInfo(estimateUtils.getResponseInfo(abstractEstimateRequest.getRequestInfo()));
		return response;
	}

	@PostMapping("/_search")
	@ResponseStatus(HttpStatus.CREATED)
	public AbstractEstimateResponse search(
			@ModelAttribute @Valid AbstractEstimateSearchContract abstractEstimateSearchContract,
			@RequestBody RequestInfo requestInfo, BindingResult errors, @RequestParam String tenantId) {
		if (errors.hasErrors())
			throw new CustomBindException(errors);
		final List<AbstractEstimate> abstractEstimates = abstractEstimateService.search(abstractEstimateSearchContract);
		final AbstractEstimateResponse response = new AbstractEstimateResponse();
		response.setAbstractEstimates(abstractEstimates);
		response.setResponseInfo(estimateUtils.getResponseInfo(requestInfo));
		return response;
	}
}
