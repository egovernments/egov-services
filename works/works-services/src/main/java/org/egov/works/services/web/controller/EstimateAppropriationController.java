package org.egov.works.services.web.controller;

import java.util.List;

import org.egov.works.services.domain.service.EstimateAppropriationService;
import org.egov.works.services.domain.exception.CustomBindException;
import org.egov.works.services.web.contract.EstimateAppropriationRequest;
import org.egov.works.services.web.contract.EstimateAppropriationResponse;
import org.egov.works.services.web.contract.RequestInfo;
import org.egov.works.services.web.model.AuditDetails;
import org.egov.works.services.web.model.EstimateAppropriation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("estimateappropriations")
public class EstimateAppropriationController {

	@Autowired
	private EstimateAppropriationService estimateAppropriationService;

	@PostMapping("/_create")
	public EstimateAppropriationResponse create(@RequestBody EstimateAppropriationRequest estimateAppropriationRequest,
			BindingResult errors, @RequestParam String tenantId) {

		final RequestInfo requestInfo = estimateAppropriationRequest.getRequestInfo();
		if (errors.hasErrors()) {
			throw new CustomBindException(errors);
		}
		EstimateAppropriationResponse estimateAppropriationResponse = new EstimateAppropriationResponse();
		AuditDetails auditDetails = new AuditDetails();
		// auditDetails.setCreatedTime(new Date().getTime());
		auditDetails.setCreatedBy(estimateAppropriationRequest.getRequestInfo().getUserInfo().getUsername());
		for (EstimateAppropriation estimateAppropriation : estimateAppropriationRequest.getEstimateAppropriations()) {
			estimateAppropriation.setAuditDetails(auditDetails);
			estimateAppropriationService.validateEstimateAppropriation(estimateAppropriation);
		}
		List<EstimateAppropriation> estimateAppropriations = estimateAppropriationService
				.save(estimateAppropriationRequest);
		estimateAppropriationResponse.setEstimateAppropriations(estimateAppropriations);
		return estimateAppropriationResponse;
	}
	
	@PostMapping("/_update")
	public EstimateAppropriationResponse update(@RequestBody EstimateAppropriationRequest estimateAppropriationRequest,
			BindingResult errors, @RequestParam String tenantId) {

		final RequestInfo requestInfo = estimateAppropriationRequest.getRequestInfo();
		if (errors.hasErrors()) {
			throw new CustomBindException(errors);
		}
		EstimateAppropriationResponse estimateAppropriationResponse = new EstimateAppropriationResponse();
		for (EstimateAppropriation estimateAppropriation : estimateAppropriationRequest.getEstimateAppropriations()) {
			estimateAppropriationService.validateEstimateAppropriation(estimateAppropriation);
		}
		List<EstimateAppropriation> estimateAppropriations = estimateAppropriationService
				.update(estimateAppropriationRequest);
		estimateAppropriationResponse.setEstimateAppropriations(estimateAppropriations);
		return estimateAppropriationResponse;
	}

}
