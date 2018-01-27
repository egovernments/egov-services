package org.egov.works.estimate.web.controller;

import java.util.List;

import javax.validation.Valid;

import org.egov.works.estimate.domain.service.DetailedEstimateService;
import org.egov.works.estimate.utils.EstimateUtils;
import org.egov.works.estimate.web.contract.DetailedEstimate;
import org.egov.works.estimate.web.contract.DetailedEstimateRequest;
import org.egov.works.estimate.web.contract.DetailedEstimateResponse;
import org.egov.works.estimate.web.contract.DetailedEstimateSearchContract;
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
			@RequestBody RequestInfo requestInfo, @RequestParam(required = true) String tenantId) {
		final List<DetailedEstimate> detailedEstimates = detailedEstimateService.search(detailedEstimateSearchContract, requestInfo);
		final DetailedEstimateResponse response = new DetailedEstimateResponse();
		response.setDetailedEstimates(detailedEstimates);
		response.setResponseInfo(estimateUtils.getResponseInfo(requestInfo));
		return response;
	}

	@PostMapping("/_create")
	@ResponseStatus(HttpStatus.OK)
	public DetailedEstimateResponse create(@Valid @RequestBody DetailedEstimateRequest detailedEstimateRequest,
			@RequestParam(required = false, defaultValue = "false") Boolean isRevision) {
		return detailedEstimateService.create(detailedEstimateRequest, isRevision);
	}

	@PostMapping("/_update")
	@ResponseStatus(HttpStatus.OK)
	public DetailedEstimateResponse update(@Valid @RequestBody DetailedEstimateRequest detailedEstimateRequest,
			@RequestParam(required = false, defaultValue = "false") Boolean isRevision) {
		return detailedEstimateService.update(detailedEstimateRequest, isRevision);
	}
}
