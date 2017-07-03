package org.egov.asset.web.controller;

import java.util.HashMap;
import java.util.Map;

import javax.validation.Valid;

import org.egov.asset.contract.AssetStatusResponse;
import org.egov.asset.contract.RequestInfoWrapper;
import org.egov.asset.exception.Error;
import org.egov.asset.exception.ErrorResponse;
import org.egov.asset.model.AssetStatusCriteria;
import org.egov.asset.model.enums.AssetCategoryType;
import org.egov.asset.model.enums.DepreciationMethod;
import org.egov.asset.model.enums.ModeOfAcquisition;
import org.egov.asset.service.AssetMasterService;
import org.egov.common.contract.response.ResponseInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AssetMasterController {

	@Autowired
	private AssetMasterService assetMasterService;

	private static final Logger logger = LoggerFactory.getLogger(AssetMasterController.class);

	@GetMapping("GET_ASSET_CATEGORY_TYPE")
	public Map<AssetCategoryType, AssetCategoryType> getAssetCategoryTypes() {

		final Map<AssetCategoryType, AssetCategoryType> map = new HashMap<>();
		for (final AssetCategoryType key : AssetCategoryType.values())
			map.put(key, AssetCategoryType.valueOf(key.toString()));

		return map;
	}

	@GetMapping("GET_DEPRECIATION_METHOD")
	public Map<DepreciationMethod, DepreciationMethod> getDepreciationMethod() {

		final Map<DepreciationMethod, DepreciationMethod> map = new HashMap<>();
		for (final DepreciationMethod key : DepreciationMethod.values())
			map.put(key, DepreciationMethod.valueOf(key.toString()));

		return map;
	}

	@GetMapping("GET_MODE_OF_ACQUISITION")
	public Map<ModeOfAcquisition, ModeOfAcquisition> getModeOfAcquisition() {

		final Map<ModeOfAcquisition, ModeOfAcquisition> map = new HashMap<>();
		for (final ModeOfAcquisition key : ModeOfAcquisition.values())
			map.put(key, ModeOfAcquisition.valueOf(key.toString()));

		return map;
	}

	@PostMapping("/assetstatuses/_search")
	@ResponseBody
	public ResponseEntity<?> assetStatusSearch(@RequestBody @Valid final RequestInfoWrapper requestInfoWrapper,
			@ModelAttribute final AssetStatusCriteria assetStatusCriteria, final BindingResult bindingResult) {

		logger.info("assetStatusSearch assetStatusCriteria:" + assetStatusCriteria);
		if (bindingResult.hasErrors()) {
			final ErrorResponse errorResponse = populateErrors(bindingResult);
			return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
		}

		final AssetStatusResponse assetStatusResponse = assetMasterService.search(assetStatusCriteria,
				requestInfoWrapper.getRequestInfo());

		return new ResponseEntity<AssetStatusResponse>(assetStatusResponse, HttpStatus.OK);
	}

	private ErrorResponse populateErrors(final BindingResult errors) {
		final ErrorResponse errRes = new ErrorResponse();

		final ResponseInfo responseInfo = new ResponseInfo();
		responseInfo.setStatus(HttpStatus.BAD_REQUEST.toString());
		errRes.setResponseInfo(responseInfo);

		final Error error = new Error();
		error.setCode(1);
		error.setDescription("Error while binding request");
		if (errors.hasFieldErrors())
			for (final FieldError errs : errors.getFieldErrors())
				error.getFields().put(errs.getField(), errs.getRejectedValue());
		errRes.setError(error);
		return errRes;
	}

}
