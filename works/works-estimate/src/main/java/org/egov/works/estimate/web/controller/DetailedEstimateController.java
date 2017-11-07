package org.egov.works.estimate.web.controller;

import java.util.List;

import javax.validation.Valid;

import org.egov.works.commons.exception.CustomBindException;
import org.egov.works.estimate.domain.service.DetailedEstimateService;
import org.egov.works.estimate.utils.EstimateUtils;
import org.egov.works.estimate.web.contract.DetailedEstimate;
import org.egov.works.estimate.web.contract.DetailedEstimateRequest;
import org.egov.works.estimate.web.contract.DetailedEstimateResponse;
import org.egov.works.estimate.web.contract.DetailedEstimateSearchContract;
import org.egov.works.estimate.web.contract.RequestInfo;
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
@RequestMapping("/detailedestimates")
public class DetailedEstimateController {

	@Autowired
	private DetailedEstimateService detailedEstimateService;
	
	@Autowired
	private EstimateUtils estimateUtils;

	
	@PostMapping("/_search")
	@ResponseStatus(HttpStatus.OK)
	public DetailedEstimateResponse search(
			@ModelAttribute @Valid DetailedEstimateSearchContract detailedEstimateSearchContract,
			@RequestBody RequestInfo requestInfo, BindingResult errors, @RequestParam(required = true) String tenantId) {
		if (errors.hasErrors())
			throw new CustomBindException(errors);
		final List<DetailedEstimate> detailedEstimates = detailedEstimateService.search(detailedEstimateSearchContract);
		final DetailedEstimateResponse response = new DetailedEstimateResponse();
		response.setDetailedEstimates(detailedEstimates);
		response.setResponseInfo(estimateUtils.getResponseInfo(requestInfo));
		return response;
	}


    @PostMapping("/_create")
    @ResponseStatus(HttpStatus.OK)
    public DetailedEstimateResponse create(@Valid @RequestBody DetailedEstimateRequest detailedEstimateRequest) {
        detailedEstimateService.validateDetailedEstimates(detailedEstimateRequest);
        final List<DetailedEstimate> detailedEstimates = detailedEstimateService.create(detailedEstimateRequest);
        final DetailedEstimateResponse response = new DetailedEstimateResponse();
        response.setDetailedEstimates(detailedEstimates);
        response.setResponseInfo(estimateUtils.getResponseInfo(detailedEstimateRequest.getRequestInfo()));
        return response;
    }
    
    @PostMapping("/_update")
    @ResponseStatus(HttpStatus.OK)
    public DetailedEstimateResponse update(@Valid @RequestBody DetailedEstimateRequest detailedEstimateRequest) {
        detailedEstimateService.validateDetailedEstimates(detailedEstimateRequest);
        final List<DetailedEstimate> detailedEstimates = detailedEstimateService.update(detailedEstimateRequest);
        final DetailedEstimateResponse response = new DetailedEstimateResponse();
        response.setDetailedEstimates(detailedEstimates);
        response.setResponseInfo(estimateUtils.getResponseInfo(detailedEstimateRequest.getRequestInfo()));
        return response;
    }
}
