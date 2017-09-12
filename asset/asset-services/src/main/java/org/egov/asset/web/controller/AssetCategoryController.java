package org.egov.asset.web.controller;

import java.util.List;

import javax.validation.Valid;

import org.egov.asset.contract.AssetCategoryRequest;
import org.egov.asset.contract.AssetCategoryResponse;
import org.egov.asset.contract.RequestInfoWrapper;
import org.egov.asset.exception.ErrorResponse;
import org.egov.asset.model.AssetCategory;
import org.egov.asset.model.AssetCategoryCriteria;
import org.egov.asset.service.AssetCategoryService;
import org.egov.asset.service.AssetCommonService;
import org.egov.asset.web.validator.AssetCategoryValidator;
import org.egov.common.contract.response.ResponseInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/assetCategories")
@Slf4j
public class AssetCategoryController {
    @Autowired
    private AssetCategoryService assetCategoryService;

    @Autowired
    private AssetCategoryValidator assetCategoryValidator;

    @Autowired
    private AssetCommonService assetCommonService;

    @PostMapping("/_search")
    @ResponseBody
    public ResponseEntity<?> search(@RequestBody final RequestInfoWrapper requestInfoWrapper,
            @ModelAttribute @Valid final AssetCategoryCriteria assetCategoryCriteria,
            final BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            final ErrorResponse errorResponse = assetCommonService.populateErrors(bindingResult);
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

        log.debug("AssetCategory create::" + assetCategoryRequest);
        if (bindingResult.hasErrors()) {
            final ErrorResponse errorResponse = assetCommonService.populateErrors(bindingResult);
            return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
        }

        assetCategoryValidator.validateAssetCategory(assetCategoryRequest);
        final AssetCategoryResponse response = assetCategoryService.createAsync(assetCategoryRequest);

        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @PostMapping("/_update")
    public ResponseEntity<?> update(@RequestBody @Valid final AssetCategoryRequest assetCategoryRequest,
            final BindingResult bindingResult) {

        log.debug("AssetCategory update::" + assetCategoryRequest);
        if (bindingResult.hasErrors()) {
            final ErrorResponse errorResponse = assetCommonService.populateErrors(bindingResult);
            return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
        }

        assetCategoryValidator.validateAssetCategoryForUpdate(assetCategoryRequest);
        final AssetCategoryResponse assetCategoryResponse = assetCategoryService.updateAsync(assetCategoryRequest);

        return new ResponseEntity<>(assetCategoryResponse, HttpStatus.OK);
    }

}