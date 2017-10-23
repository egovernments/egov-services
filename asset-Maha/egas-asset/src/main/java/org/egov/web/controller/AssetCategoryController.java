package org.egov.web.controller;

import java.util.List;

import javax.validation.Valid;

import org.egov.common.contract.response.ErrorResponse;
import org.egov.common.contract.response.ResponseInfo;
import org.egov.contract.AssetCategoryRequest;
import org.egov.contract.AssetCategoryResponse;
import org.egov.contract.RequestInfoWrapper;
import org.egov.model.AssetCategory;
import org.egov.model.AssetCategoryCriteria;
import org.egov.service.AssetCategoryService;
import org.egov.service.AssetCommonService;
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

   /* @Autowired
    private AssetCategoryValidator assetCategoryValidator;*/

    @PostMapping("/_search")
    @ResponseBody
    public ResponseEntity<?> search(@RequestBody final RequestInfoWrapper requestInfoWrapper,
            @ModelAttribute @Valid final AssetCategoryCriteria assetCategoryCriteria) {
        final List<AssetCategory> assetCategories = assetCategoryService.search(assetCategoryCriteria);
        final AssetCategoryResponse response = new AssetCategoryResponse();
        response.setAssetCategory(assetCategories);
        response.setResponseInfo(new ResponseInfo());

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping("/_create")
    @ResponseBody
    public ResponseEntity<?> create(@RequestBody @Valid final AssetCategoryRequest assetCategoryRequest) {
         log.debug("AssetCategory create::" + assetCategoryRequest);
      // assetCategoryValidator.validateAssetCategory(assetCategoryRequest);
         final AssetCategoryResponse response = assetCategoryService.createAsync(assetCategoryRequest);
         return new ResponseEntity<>(response, HttpStatus.CREATED);
    }
}
