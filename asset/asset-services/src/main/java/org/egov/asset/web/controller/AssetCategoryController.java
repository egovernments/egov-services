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

import java.util.ArrayList;
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
import org.egov.asset.web.wrapperfactory.ResponseInfoFactory;
import org.egov.common.contract.request.RequestInfo;
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
    
    public static final String UPDATE = "update";
    
    @Autowired
    private AssetCategoryService assetCategoryService;

    @Autowired
    private AssetCategoryValidator assetCategoryValidator;

    @Autowired
    private AssetCommonService assetCommonService;
    

    @Autowired
    private ResponseInfoFactory responseInfoFactory;

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
        if(!assetCategories.isEmpty() && UPDATE.equalsIgnoreCase(assetCategoryCriteria.getAction()))
            assetCategoryValidator.getAssetExistInAgreements( assetCategories,requestInfoWrapper.getRequestInfo());
        
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
        final AssetCategory assetCategory = assetCategoryService.createAssetCategory(assetCategoryRequest);
        List<AssetCategory> assetCategories = new ArrayList<>();
        assetCategories.add(assetCategory);
       
        return getSuccessResponse(assetCategories, assetCategoryRequest.getRequestInfo());

    }

    @PostMapping("/_update")
    public ResponseEntity<?> update(@RequestBody @Valid AssetCategoryRequest assetCategoryRequest,
            BindingResult bindingResult) {

        log.debug("AssetCategory update::" + assetCategoryRequest);
        if (bindingResult.hasErrors()) {
            ErrorResponse errorResponse = assetCommonService.populateErrors(bindingResult);
            return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
        }

        assetCategoryValidator.validateAssetCategoryForUpdate(assetCategoryRequest);
        AssetCategory assetCategory = assetCategoryService.updateAssetCategory(assetCategoryRequest);
        List<AssetCategory> assetCategories = new ArrayList<>();
        assetCategories.add(assetCategory);
        return getSuccessResponse(assetCategories, assetCategoryRequest.getRequestInfo());
        
    }
    
    private ResponseEntity<?> getSuccessResponse( List<AssetCategory> assetCategories,
             RequestInfo requestInfo) {
         AssetCategoryResponse assetCategoryRes = new AssetCategoryResponse();
         assetCategoryRes.setAssetCategory(assetCategories);
        ResponseInfo responseInfo = responseInfoFactory.createResponseInfoFromRequestHeaders(requestInfo);
        assetCategoryRes.setResponseInfo(responseInfoFactory.createResponseInfoFromRequestHeaders(requestInfo));
        responseInfo.setStatus(HttpStatus.OK.toString());
       return new ResponseEntity<AssetCategoryResponse>(assetCategoryRes, HttpStatus.OK);
    }

}