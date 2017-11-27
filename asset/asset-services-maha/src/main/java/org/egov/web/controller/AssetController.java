package org.egov.web.controller;

import java.util.Set;

import javax.validation.Valid;

import org.egov.contract.AssetCurrentValueRequest;
import org.egov.contract.AssetCurrentValueResponse;
import org.egov.contract.AssetRequest;
import org.egov.contract.AssetResponse;
import org.egov.contract.DepreciationRequest;
import org.egov.contract.DepreciationResponse;
import org.egov.contract.DisposalRequest;
import org.egov.contract.DisposalResponse;
import org.egov.contract.RequestInfoWrapper;
import org.egov.contract.RevaluationRequest;
import org.egov.contract.RevaluationResponse;
import org.egov.model.criteria.AssetCriteria;
import org.egov.model.criteria.DisposalCriteria;
import org.egov.model.criteria.RevaluationCriteria;
import org.egov.service.AssetService;
import org.egov.service.CurrentValueService;
import org.egov.service.DepreciationService;
import org.egov.service.DisposalService;
import org.egov.service.RevaluationService;
import org.egov.web.validator.AssetValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/assets")
public class AssetController {

	@Autowired
	private AssetService assetService;

	@Autowired
	private RevaluationService revaluationService;
	
	@Autowired
	private DepreciationService depreciationService;
	
	@Autowired
	private DisposalService  disposalService;
	
	@Autowired
	private CurrentValueService currentValueService;
	
	@Autowired
	private AssetValidator assetValidator;

	@PostMapping("_search")
	@ResponseBody
	public ResponseEntity<?> search(@RequestBody @Valid final RequestInfoWrapper requestInfoWrapper,
			@ModelAttribute @Valid final AssetCriteria assetCriteria) {
		log.debug("assetCriteria::" + assetCriteria + "requestInfoWrapper::" + requestInfoWrapper);
		assetValidator.validateSearch(assetCriteria);
		AssetResponse assetResponse = assetService.getAssets(assetCriteria, requestInfoWrapper.getRequestInfo());
		return new ResponseEntity<>(assetResponse, HttpStatus.OK);

	}

	@PostMapping("_create")
	@ResponseBody
	public ResponseEntity<?> create(@RequestBody @Valid final AssetRequest assetRequest) {
		assetValidator.validateAsset(assetRequest);
		System.err.println(assetRequest);
		final AssetResponse assetResponse = assetService.createAsync(assetRequest);
		return new ResponseEntity<>(assetResponse, HttpStatus.CREATED);
	}

	@PostMapping("_update")
	@ResponseBody
	public ResponseEntity<?> update(@RequestBody @Valid final AssetRequest assetRequest) {
		assetValidator.validateAsset(assetRequest);
		assetValidator.validateAssetId(assetRequest);
		final AssetResponse assetResponse = assetService.updateAsync(assetRequest);
		return new ResponseEntity<>(assetResponse, HttpStatus.OK);
	}

	@PostMapping("revaluation/_create")
	@ResponseBody
	public ResponseEntity<?> revaluate(@RequestBody @Valid final RevaluationRequest revaluationRequest) {

		assetValidator.validateForRevaluation(revaluationRequest);
		final RevaluationResponse revaluationResponse = revaluationService.createAsync(revaluationRequest);
		return new ResponseEntity<>(revaluationResponse, HttpStatus.CREATED);
	}

	@PostMapping("revaluation/_search")
	@ResponseBody
	public ResponseEntity<?> reevaluateSearch(@RequestBody @Valid final RequestInfoWrapper requestInfoWrapper,
			@ModelAttribute @Valid final RevaluationCriteria revaluationCriteria) {

		final RevaluationResponse revaluationResponse = revaluationService.search(revaluationCriteria,
				requestInfoWrapper.getRequestInfo());

		return new ResponseEntity<RevaluationResponse>(revaluationResponse, HttpStatus.OK);
	}

	@PostMapping("dispose/_create")
	@ResponseBody
	public ResponseEntity<?> dispose(@RequestBody @Valid final DisposalRequest disposalRequest) {

		assetValidator.validateForDisposal(disposalRequest);
		final DisposalResponse disposalResponse = disposalService.createAsync(disposalRequest);
		return new ResponseEntity<DisposalResponse>(disposalResponse, HttpStatus.CREATED);
	}

	@PostMapping("dispose/_search")
	@ResponseBody
	public ResponseEntity<?> disposalSearch(@RequestBody @Valid final RequestInfoWrapper requestInfoWrapper,
			@ModelAttribute @Valid final DisposalCriteria disposalCriteria) {

		final DisposalResponse disposalResponse = disposalService.search(disposalCriteria,
				requestInfoWrapper.getRequestInfo());

		return new ResponseEntity<>(disposalResponse, HttpStatus.OK);
	}
	
	@PostMapping("currentvalues/_search")
	@ResponseBody
	public ResponseEntity<?> getAssetCurrentValue(
			@RequestParam(name = "assetIds", required = true) final Set<Long> assetIds,
			@RequestParam(name = "tenantId", required = true) final String tenantId,
			@RequestBody @Valid final RequestInfoWrapper requestInfoWrapper) {

		log.debug("getAssetCurrentValue assetId:" + assetIds + ",tenantId:" + tenantId);
		final AssetCurrentValueResponse assetCurrentValueResponse = currentValueService.getCurrentValues(assetIds,
				tenantId, requestInfoWrapper.getRequestInfo());

		log.debug("getAssetCurrentValue assetCurrentValueResponse:" + assetCurrentValueResponse);
		return new ResponseEntity<>(assetCurrentValueResponse, HttpStatus.OK);
	}

	@PostMapping("currentvalues/_create")
	@ResponseBody
	public ResponseEntity<?> saveCurrentValue(
			@RequestBody @Valid final AssetCurrentValueRequest assetCurrentValueRequest) {
		log.debug("create assetcurrentvalue :" + assetCurrentValueRequest);
		
		final AssetCurrentValueResponse assetCurrentValueResponse = currentValueService
				.createCurrentValueAsync(assetCurrentValueRequest);
		return new ResponseEntity<>(assetCurrentValueResponse, HttpStatus.CREATED);
	}
	
	@PostMapping("depreciations/_create")
	@ResponseBody
	public ResponseEntity<?> saveDepreciation(@RequestBody @Valid final DepreciationRequest depreciationRequest) {

		final DepreciationResponse depreciationResponse = depreciationService.createDepreciationAsync(depreciationRequest);
		return new ResponseEntity<>(depreciationResponse, HttpStatus.CREATED);
	}

}
