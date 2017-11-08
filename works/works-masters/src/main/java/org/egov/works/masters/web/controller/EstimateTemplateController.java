package org.egov.works.masters.web.controller;

import org.egov.works.masters.domain.service.EstimateTemplateService;
import org.egov.works.masters.domain.validator.EstimateTemplateValidator;
import org.egov.works.masters.utils.MasterUtils;
import org.egov.works.masters.web.contract.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/estimatetemplates")
public class EstimateTemplateController {

	@Autowired
	private EstimateTemplateService estimateTemplateService;

	@Autowired
	private MasterUtils masterUtils;

	@Autowired
	private EstimateTemplateValidator estimateTemplateValidator;

	@PostMapping("/_create")
	@ResponseStatus(HttpStatus.CREATED)
	public ResponseEntity<?> create(@Valid @RequestBody EstimateTemplateRequest estimateTemplateRequest) {
		estimateTemplateValidator.validate(estimateTemplateRequest);
		estimateTemplateValidator.validateForExistance(estimateTemplateRequest);
		final List<EstimateTemplate> estimateTemplates = estimateTemplateService.create(estimateTemplateRequest);
		final EstimateTemplateResponse response = new EstimateTemplateResponse();
		response.setEstimateTemplates(estimateTemplates);
		response.setResponseInfo(masterUtils.createResponseInfoFromRequestInfo(estimateTemplateRequest.getRequestInfo(), true));
		return new ResponseEntity<>(response, HttpStatus.CREATED);
	}

	@PostMapping("/_search")
	public ResponseEntity<?> search(
			@ModelAttribute @Valid EstimateTemplateSearchCriteria estimateTemplateSearchCriteria,
			@RequestBody RequestInfo requestInfo, BindingResult errors, @RequestParam String tenantId) {
		final List<EstimateTemplate> estimateTemplates = estimateTemplateService.search(estimateTemplateSearchCriteria);
		final EstimateTemplateResponse response = new EstimateTemplateResponse();
		response.setEstimateTemplates(estimateTemplates);
		response.setResponseInfo(masterUtils.createResponseInfoFromRequestInfo(requestInfo, true));
		return new ResponseEntity<>(response, HttpStatus.OK);
	}

	@PostMapping("/_update")
	public ResponseEntity<?> update(@RequestBody EstimateTemplateRequest estimateTemplateRequest) {
		estimateTemplateValidator.validate(estimateTemplateRequest);
		final List<EstimateTemplate> estimateTemplates = estimateTemplateService.update(estimateTemplateRequest);
		final EstimateTemplateResponse response = new EstimateTemplateResponse();
		response.setEstimateTemplates(estimateTemplates);
		response.setResponseInfo(masterUtils.createResponseInfoFromRequestInfo(estimateTemplateRequest.getRequestInfo(), true));
		return new ResponseEntity<>(response, HttpStatus.OK);
	}
}
