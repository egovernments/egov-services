/*
 *    eGov  SmartCity eGovernance suite aims to improve the internal efficiency,transparency,
 *    accountability and the service delivery of the government  organizations.
 *
 *     Copyright (C) 2017  eGovernments Foundation
 *
 *     The updated version of eGov suite of products as by eGovernments Foundation
 *     is available at http://www.egovernments.org
 *
 *     This program is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     any later version.
 *
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 *
 *     You should have received a copy of the GNU General Public License
 *     along with this program. If not, see http://www.gnu.org/licenses/ or
 *     http://www.gnu.org/licenses/gpl.html .
 *
 *     In addition to the terms of the GPL license to be adhered to in using this
 *     program, the following additional terms are to be complied with:
 *
 *         1) All versions of this program, verbatim or modified must carry this
 *            Legal Notice.
 *            Further, all user interfaces, including but not limited to citizen facing interfaces,
 *            Urban Local Bodies interfaces, dashboards, mobile applications, of the program and any
 *            derived works should carry eGovernments Foundation logo on the top right corner.
 *
 *            For the logo, please refer http://egovernments.org/html/logo/egov_logo.png.
 *            For any further queries on attribution, including queries on brand guidelines,
 *            please contact contact@egovernments.org
 *
 *         2) Any misrepresentation of the origin of the material is prohibited. It
 *            is required that all modified versions of this material be marked in
 *            reasonable ways as different from the original version.
 *
 *         3) This license does not grant any rights to any user of the program
 *            with regards to rights under trademark law for use of the trade names
 *            or trademarks of eGovernments Foundation.
 *
 *   In case of any queries, you can reach eGovernments Foundation at contact@egovernments.org.
 *
 */

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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import lombok.extern.slf4j.Slf4j;

@RestController
@Slf4j
public class AssetMasterController {

    @Autowired
    private AssetMasterService assetMasterService;

    @Autowired
    private AssetCommonService assetCommonService;

    @PostMapping("GET_ASSET_CATEGORY_TYPE")
    @ResponseBody
    public ResponseEntity<?> getAssetCategoryTypes(@RequestBody @Valid final RequestInfoWrapper requestInfoWrapper,
            final BindingResult bindingResult) {

        log.debug("Get Asset Category Types for Asset");

        if (bindingResult.hasErrors()) {
            final ErrorResponse errorResponse = assetCommonService.populateErrors(bindingResult);
            return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
        }

        final Map<AssetCategoryType, AssetCategoryType> map = new HashMap<>();
        for (final AssetCategoryType key : AssetCategoryType.values())
            map.put(key, AssetCategoryType.valueOf(key.toString()));

        log.debug("Asset Category Types :: " + map);
        final AssetCategoryTypeResponse assetCategoryTypeResponse = AssetCategoryTypeResponse.builder()
                .assetCategoryType(map).responseInfo(new ResponseInfo()).build();
        return new ResponseEntity<AssetCategoryTypeResponse>(assetCategoryTypeResponse, HttpStatus.OK);
    }

    @PostMapping("GET_DEPRECIATION_METHOD")
    @ResponseBody
    public ResponseEntity<?> getDepreciationMethod(@RequestBody @Valid final RequestInfoWrapper requestInfoWrapper,
            final BindingResult bindingResult) {

        log.debug("Get Depreciation Methods for Asset");

        if (bindingResult.hasErrors()) {
            final ErrorResponse errorResponse = assetCommonService.populateErrors(bindingResult);
            return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
        }

        final Map<DepreciationMethod, DepreciationMethod> map = new HashMap<>();
        for (final DepreciationMethod key : DepreciationMethod.values())
            map.put(key, DepreciationMethod.valueOf(key.toString()));

        log.debug("Depreciation Methods :: " + map);

        final DepreciationMethodResponse depreciationMethodResponse = DepreciationMethodResponse.builder()
                .depreciationMethod(map).responseInfo(new ResponseInfo()).build();
        return new ResponseEntity<DepreciationMethodResponse>(depreciationMethodResponse, HttpStatus.OK);
    }

    @PostMapping("GET_MODE_OF_ACQUISITION")
    @ResponseBody
    public ResponseEntity<?> getModeOfAcquisition(@RequestBody @Valid final RequestInfoWrapper requestInfoWrapper,
            final BindingResult bindingResult) {

        log.debug("Get Mode of Acquisition for Asset");

        if (bindingResult.hasErrors()) {
            final ErrorResponse errorResponse = assetCommonService.populateErrors(bindingResult);
            return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
        }

        final Map<ModeOfAcquisition, ModeOfAcquisition> map = new HashMap<>();
        for (final ModeOfAcquisition key : ModeOfAcquisition.values())
            map.put(key, ModeOfAcquisition.valueOf(key.toString()));

        log.debug("Mode of Acquisitions :: " + map);

        final ModeOfAcquisitionReponse modeOfAcquisitionReponse = ModeOfAcquisitionReponse.builder()
                .modeOfAcquisition(map).responseInfo(new ResponseInfo()).build();

        return new ResponseEntity<ModeOfAcquisitionReponse>(modeOfAcquisitionReponse, HttpStatus.OK);
    }

    @PostMapping("/assetstatuses/_search")
    @ResponseBody
    public ResponseEntity<?> assetStatusSearch(@RequestBody @Valid final RequestInfoWrapper requestInfoWrapper,
            @ModelAttribute @Valid final AssetStatusCriteria assetStatusCriteria, final BindingResult bindingResult) {

        log.debug("assetStatusSearch assetStatusCriteria:" + assetStatusCriteria);
        if (bindingResult.hasErrors()) {
            final ErrorResponse errorResponse = assetCommonService.populateErrors(bindingResult);
            return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
        }

        final AssetStatusResponse assetStatusResponse = assetMasterService.search(assetStatusCriteria,
                requestInfoWrapper.getRequestInfo());

        return new ResponseEntity<AssetStatusResponse>(assetStatusResponse, HttpStatus.OK);
    }
}
