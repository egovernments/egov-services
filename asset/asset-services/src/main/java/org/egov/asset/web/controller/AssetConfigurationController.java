package org.egov.asset.web.controller;

import javax.validation.Valid;

import org.egov.asset.contract.AssetConfigurationResponse;
import org.egov.asset.contract.RequestInfoWrapper;
import org.egov.asset.exception.ErrorResponse;
import org.egov.asset.model.AssetConfigurationCriteria;
import org.egov.asset.service.AssetCommonService;
import org.egov.asset.service.AssetConfigurationService;
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
@RequestMapping("/assetconfigurations")
@Slf4j
public class AssetConfigurationController {

    @Autowired
    private AssetConfigurationService assetConfigurationService;

    @Autowired
    private AssetCommonService assetCommonService;

    @PostMapping("/_search")
    @ResponseBody
    public ResponseEntity<?> assetConfigurationSearch(@RequestBody @Valid final RequestInfoWrapper requestInfoWrapper,
            @ModelAttribute @Valid final AssetConfigurationCriteria assetConfigurationCriteria,
            final BindingResult bindingResult) {

        log.debug("assetConfigurationSearch assetConfigurationCriteria:" + assetConfigurationCriteria);
        if (bindingResult.hasErrors()) {
            final ErrorResponse errorResponse = assetCommonService.populateErrors(bindingResult);
            return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
        }

        final AssetConfigurationResponse assetConfigurationResponse = assetConfigurationService
                .search(assetConfigurationCriteria);

        return new ResponseEntity<AssetConfigurationResponse>(assetConfigurationResponse, HttpStatus.OK);
    }

}
