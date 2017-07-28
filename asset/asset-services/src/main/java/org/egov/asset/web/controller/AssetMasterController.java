package org.egov.asset.web.controller;

import java.util.HashMap;
import java.util.Map;

import javax.validation.Valid;

import org.egov.asset.contract.AssetCategoryTypeResponse;
import org.egov.asset.contract.AssetStatusResponse;
import org.egov.asset.contract.DepreciationMethodResponse;
import org.egov.asset.contract.ModeOfAcquisitionReponse;
import org.egov.asset.contract.RequestInfoWrapper;
import org.egov.asset.exception.ErrorResponse;
import org.egov.asset.model.AssetStatusCriteria;
import org.egov.asset.model.enums.AssetCategoryType;
import org.egov.asset.model.enums.DepreciationMethod;
import org.egov.asset.model.enums.ModeOfAcquisition;
import org.egov.asset.service.AssetCommonService;
import org.egov.asset.service.AssetMasterService;
import org.egov.common.contract.response.ResponseInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AssetMasterController {

    @Autowired
    private AssetMasterService assetMasterService;

    @Autowired
    private AssetCommonService assetCommonService;

    private static final Logger logger = LoggerFactory.getLogger(AssetMasterController.class);

    @PostMapping("GET_ASSET_CATEGORY_TYPE")
    @ResponseBody
    public ResponseEntity<?> getAssetCategoryTypes(@RequestBody @Valid final RequestInfoWrapper requestInfoWrapper,
            final BindingResult bindingResult) {

        logger.debug("Get Asset Category Types for Asset");

        if (bindingResult.hasErrors()) {
            final ErrorResponse errorResponse = assetCommonService.populateErrors(bindingResult);
            return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
        }

        final Map<AssetCategoryType, AssetCategoryType> map = new HashMap<>();
        for (final AssetCategoryType key : AssetCategoryType.values())
            map.put(key, AssetCategoryType.valueOf(key.toString()));

        logger.debug("Asset Category Types :: " + map);
        final AssetCategoryTypeResponse assetCategoryTypeResponse = AssetCategoryTypeResponse.builder()
                .assetCategoryType(map).responseInfo(new ResponseInfo()).build();
        return new ResponseEntity<AssetCategoryTypeResponse>(assetCategoryTypeResponse, HttpStatus.OK);
    }

    @PostMapping("GET_DEPRECIATION_METHOD")
    @ResponseBody
    public ResponseEntity<?> getDepreciationMethod(@RequestBody @Valid final RequestInfoWrapper requestInfoWrapper,
            final BindingResult bindingResult) {

        logger.debug("Get Depreciation Methods for Asset");

        if (bindingResult.hasErrors()) {
            final ErrorResponse errorResponse = assetCommonService.populateErrors(bindingResult);
            return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
        }

        final Map<DepreciationMethod, DepreciationMethod> map = new HashMap<>();
        for (final DepreciationMethod key : DepreciationMethod.values())
            map.put(key, DepreciationMethod.valueOf(key.toString()));

        logger.debug("Depreciation Methods :: " + map);

        final DepreciationMethodResponse depreciationMethodResponse = DepreciationMethodResponse.builder()
                .depreciationMethod(map).responseInfo(new ResponseInfo()).build();
        return new ResponseEntity<DepreciationMethodResponse>(depreciationMethodResponse, HttpStatus.OK);
    }

    @PostMapping("GET_MODE_OF_ACQUISITION")
    @ResponseBody
    public ResponseEntity<?> getModeOfAcquisition(@RequestBody @Valid final RequestInfoWrapper requestInfoWrapper,
            final BindingResult bindingResult) {

        logger.debug("Get Mode of Acquisition for Asset");

        if (bindingResult.hasErrors()) {
            final ErrorResponse errorResponse = assetCommonService.populateErrors(bindingResult);
            return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
        }

        final Map<ModeOfAcquisition, ModeOfAcquisition> map = new HashMap<>();
        for (final ModeOfAcquisition key : ModeOfAcquisition.values())
            map.put(key, ModeOfAcquisition.valueOf(key.toString()));

        logger.debug("Mode of Acquisitions :: " + map);

        final ModeOfAcquisitionReponse modeOfAcquisitionReponse = ModeOfAcquisitionReponse.builder()
                .modeOfAcquisition(map).responseInfo(new ResponseInfo()).build();

        return new ResponseEntity<ModeOfAcquisitionReponse>(modeOfAcquisitionReponse, HttpStatus.OK);
    }

    @PostMapping("/assetstatuses/_search")
    @ResponseBody
    public ResponseEntity<?> assetStatusSearch(@RequestBody @Valid final RequestInfoWrapper requestInfoWrapper,
            @ModelAttribute final AssetStatusCriteria assetStatusCriteria, final BindingResult bindingResult) {

        logger.debug("assetStatusSearch assetStatusCriteria:" + assetStatusCriteria);
        if (bindingResult.hasErrors()) {
            final ErrorResponse errorResponse = assetCommonService.populateErrors(bindingResult);
            return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
        }

        final AssetStatusResponse assetStatusResponse = assetMasterService.search(assetStatusCriteria,
                requestInfoWrapper.getRequestInfo());

        return new ResponseEntity<AssetStatusResponse>(assetStatusResponse, HttpStatus.OK);
    }
}
