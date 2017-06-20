package org.egov.asset.web.controller;

import java.util.List;

import javax.validation.Valid;

import org.egov.asset.contract.AssetCategoryRequest;
import org.egov.asset.contract.AssetCategoryResponse;
import org.egov.asset.contract.RequestInfoWrapper;
import org.egov.asset.exception.Error;
import org.egov.asset.exception.ErrorResponse;
import org.egov.asset.model.AssetCategory;
import org.egov.asset.model.AssetCategoryCriteria;
import org.egov.asset.service.AssetCategoryService;
import org.egov.asset.web.validator.AssetCategoryValidator;
import org.egov.common.contract.response.ResponseInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/assetCategories")
public class AssetCategoryController {
	private static final Logger logger = LoggerFactory.getLogger(AssetCategoryController.class);

	@Autowired
	private AssetCategoryService assetCategoryService;

	@Autowired
	private AssetCategoryValidator assetCategoryValidator;

	@PostMapping("/_search")
	@ResponseBody
	public ResponseEntity<?> search(@RequestBody final RequestInfoWrapper requestInfoWrapper,
			@ModelAttribute @Valid final AssetCategoryCriteria assetCategoryCriteria,
			final BindingResult bindingResult) {

		if (bindingResult.hasErrors()) {
			final ErrorResponse errorResponse = populateErrors(bindingResult);
			return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
		}

		final List<AssetCategory> assetCategories = assetCategoryService.search(assetCategoryCriteria);
		final AssetCategoryResponse response = new AssetCategoryResponse();
		response.setAssetCategory(assetCategories);
		response.setResponseInfo(new ResponseInfo());

		return new ResponseEntity<>(response, HttpStatus.OK);
	}

	@PostMapping("/_create")
	@ResponseBody
	public ResponseEntity<?> create(@RequestBody @Valid final AssetCategoryRequest assetCategoryRequest,
			final BindingResult bindingResult) {

		logger.info("AssetCategory create::" + assetCategoryRequest);
		if (bindingResult.hasErrors()) {
			final ErrorResponse errorResponse = populateErrors(bindingResult);
			return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
		}

		assetCategoryValidator.validateAssetCategory(assetCategoryRequest);
		final AssetCategoryResponse response = assetCategoryService.createAsync(assetCategoryRequest);

		return new ResponseEntity<>(response, HttpStatus.CREATED);
	}

	@PostMapping("{code}/_update")
	public ResponseEntity<?> update(@PathVariable("code") final String code,
			@RequestBody @Valid final AssetCategoryRequest assetCategoryRequest, final BindingResult bindingResult) {

		logger.info("AssetCategory update::" + assetCategoryRequest + "," + "code:" + code);
		if (bindingResult.hasErrors()) {
			final ErrorResponse errorResponse = populateErrors(bindingResult);
			return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
		}
		if (!code.equals(assetCategoryRequest.getAssetCategory().getCode()))
			throw new RuntimeException("Invalid asset code");
		final AssetCategoryResponse assetCategoryResponse = assetCategoryService.updateAsync(assetCategoryRequest);

		return new ResponseEntity<>(assetCategoryResponse, HttpStatus.OK);
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