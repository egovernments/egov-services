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

import java.util.List;
import java.util.Set;

import javax.validation.Valid;

import org.egov.asset.contract.AssetCurrentValueRequest;
import org.egov.asset.contract.AssetCurrentValueResponse;
import org.egov.asset.contract.AssetRequest;
import org.egov.asset.contract.AssetResponse;
import org.egov.asset.contract.DepreciationReportResponse;
import org.egov.asset.contract.DepreciationRequest;
import org.egov.asset.contract.DepreciationResponse;
import org.egov.asset.contract.DisposalRequest;
import org.egov.asset.contract.DisposalResponse;
import org.egov.asset.contract.RequestInfoWrapper;
import org.egov.asset.contract.RevaluationRequest;
import org.egov.asset.contract.RevaluationResponse;
import org.egov.asset.exception.ErrorResponse;
import org.egov.asset.model.AssetCriteria;
import org.egov.asset.model.DepreciationReportCriteria;
import org.egov.asset.model.DisposalCriteria;
import org.egov.asset.model.RevaluationCriteria;
import org.egov.asset.model.enums.TransactionType;
import org.egov.asset.service.AssetCommonService;
import org.egov.asset.service.AssetService;
import org.egov.asset.service.CurrentValueService;
import org.egov.asset.service.DepreciationService;
import org.egov.asset.service.DisposalService;
import org.egov.asset.service.RevaluationService;
import org.egov.asset.web.validator.AssetValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
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
    private AssetValidator assetValidator;

    @Autowired
    private RevaluationService revaluationService;

    @Autowired
    private CurrentValueService currentValueService;

    @Autowired
    private DisposalService disposalService;

    @Autowired
    private AssetCommonService assetCommonService;

    @Autowired
    private DepreciationService depreciationservice;

    @PostMapping("_search")
    @ResponseBody
    public ResponseEntity<?> search(@RequestBody @Valid final RequestInfoWrapper requestInfoWrapper,
            @ModelAttribute @Valid final AssetCriteria assetCriteria, final BindingResult bindingResult) {
        log.debug("assetCriteria::" + assetCriteria + "requestInfoWrapper::" + requestInfoWrapper);

        if (bindingResult.hasErrors()) {
            final ErrorResponse errorResponse = assetCommonService.populateErrors(bindingResult);
            return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
            
        }
        if (assetCriteria.getTransaction() != null) {
            if (assetCriteria.getTransaction().toString().equals(TransactionType.DEPRECIATION.toString())) {
        final List<ErrorResponse> errorResponses = assetValidator.validateSearchAssetDepreciation(assetCriteria);
        if (!errorResponses.isEmpty())
            return new ResponseEntity<>(errorResponses, HttpStatus.BAD_REQUEST);
            }
        }

        if (assetCriteria.getGrossValue() != null && assetCriteria.getFromCapitalizedValue() != null
                && assetCriteria.getToCapitalizedValue() != null)
            throw new RuntimeException(
                    "Gross Value should not be present with from capitalized value and to capitalized value");
        
       final AssetResponse assetResponse = assetService.getAssets(assetCriteria, requestInfoWrapper.getRequestInfo());
        return new ResponseEntity<>(assetResponse, HttpStatus.OK);
    }

    @PostMapping("_create")
    @ResponseBody
    public ResponseEntity<?> create(@RequestBody @Valid final AssetRequest assetRequest,
            final BindingResult bindingResult) {
        log.debug("create asset:" + assetRequest);
        if (bindingResult.hasErrors()) {
            final ErrorResponse errorResponse = assetCommonService.populateErrors(bindingResult);
            return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
        }

        final List<ErrorResponse> errorResponses =  assetValidator.validateAssetRequest(assetRequest);
        if (!errorResponses.isEmpty())
                return new ResponseEntity<List<ErrorResponse>>(errorResponses, HttpStatus.BAD_REQUEST);
        assetValidator.validateAsset(assetRequest);

        final AssetResponse assetResponse = assetService.createAsync(assetRequest);
        return new ResponseEntity<>(assetResponse, HttpStatus.CREATED);
    }

    @PostMapping("_update")
    @ResponseBody
    public ResponseEntity<?> update(@RequestBody final AssetRequest assetRequest, final BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            final ErrorResponse errorResponse = assetCommonService.populateErrors(bindingResult);
            return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
        }
        assetValidator.validateAssetForUpdate(assetRequest);
        final AssetResponse assetResponse = assetService.updateAsync(assetRequest);
        return new ResponseEntity<>(assetResponse, HttpStatus.OK);
    }

    @PostMapping("revaluation/_create")
    @ResponseBody
    public ResponseEntity<?> revaluate(@RequestBody @Valid final RevaluationRequest revaluationRequest,
            final BindingResult bindingResult, @RequestHeader final HttpHeaders headers) {

        log.debug("create revaluate:" + revaluationRequest);
        if (bindingResult.hasErrors()) {
            final ErrorResponse errorResponse = assetCommonService.populateErrors(bindingResult);
            return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
        }
        log.debug("Request Headers :: " + headers);
        assetValidator.validateRevaluation(revaluationRequest);
        final RevaluationResponse revaluationResponse = revaluationService.createAsync(revaluationRequest, headers);

        return new ResponseEntity<>(revaluationResponse, HttpStatus.CREATED);
    }

    @PostMapping("revaluation/_search")
    @ResponseBody
    public ResponseEntity<?> reevaluateSearch(@RequestBody @Valid final RequestInfoWrapper requestInfoWrapper,
            @ModelAttribute @Valid final RevaluationCriteria revaluationCriteria, final BindingResult bindingResult) {

        log.debug("reevaluateSearch revaluationCriteria:" + revaluationCriteria);
        if (bindingResult.hasErrors()) {
            final ErrorResponse errorResponse = assetCommonService.populateErrors(bindingResult);
            return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
        }
        assetValidator.validateRevaluationCriteria(revaluationCriteria);

        final RevaluationResponse revaluationResponse = revaluationService.search(revaluationCriteria,
                requestInfoWrapper.getRequestInfo());

        return new ResponseEntity<RevaluationResponse>(revaluationResponse, HttpStatus.OK);
    }

    @PostMapping("dispose/_create")
    @ResponseBody
    public ResponseEntity<?> dispose(@RequestBody @Valid final DisposalRequest disposalRequest,
            final BindingResult bindingResult, @RequestHeader final HttpHeaders headers) {

        log.debug("create dispose:" + disposalRequest);
        if (bindingResult.hasErrors()) {
            final ErrorResponse errorResponse = assetCommonService.populateErrors(bindingResult);
            return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
        }

        log.debug("Request Headers :: " + headers);
        assetValidator.validateDisposal(disposalRequest);

        final DisposalResponse disposalResponse = disposalService.createAsync(disposalRequest, headers);
        log.debug("dispose disposalResponse:" + disposalResponse);
        return new ResponseEntity<DisposalResponse>(disposalResponse, HttpStatus.CREATED);
    }

    @PostMapping("dispose/_search")
    @ResponseBody
    public ResponseEntity<?> disposalSearch(@RequestBody @Valid final RequestInfoWrapper requestInfoWrapper,
            @ModelAttribute @Valid final DisposalCriteria disposalCriteria, final BindingResult bindingResult) {

        log.debug("disposalSearch disposalCriteria:" + disposalCriteria);
        if (bindingResult.hasErrors()) {
            final ErrorResponse errorResponse = assetCommonService.populateErrors(bindingResult);
            return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
        }
        assetValidator.validateDisposalCriteria(disposalCriteria);

        final DisposalResponse disposalResponse = disposalService.search(disposalCriteria,
                requestInfoWrapper.getRequestInfo());

        return new ResponseEntity<>(disposalResponse, HttpStatus.OK);
    }

    @PostMapping("currentvalue/_search")
    @ResponseBody
    public ResponseEntity<?> getAssetCurrentValue(
            @RequestParam(name = "assetIds", required = true) final Set<Long> assetIds,
            @RequestParam(name = "tenantId", required = true) final String tenantId,
            @RequestBody @Valid final RequestInfoWrapper requestInfoWrapper, final BindingResult bindingResult) {

        log.debug("getAssetCurrentValue assetId:" + assetIds + ",tenantId:" + tenantId);
        if (bindingResult.hasErrors()) {
            final ErrorResponse errorResponse = assetCommonService.populateErrors(bindingResult);
            return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
        }

        final AssetCurrentValueResponse assetCurrentValueResponse = currentValueService.getCurrentValues(assetIds,
                tenantId, requestInfoWrapper.getRequestInfo());

        log.debug("getAssetCurrentValue assetCurrentValueResponse:" + assetCurrentValueResponse);
        return new ResponseEntity<>(assetCurrentValueResponse, HttpStatus.OK);
    }

    @PostMapping("currentvalue/_create")
    @ResponseBody
    public ResponseEntity<?> saveCurrentValue(
            @RequestBody @Valid final AssetCurrentValueRequest assetCurrentValueRequest,
            final BindingResult bindingResult) {
        log.debug("create assetcurrentvalue :" + assetCurrentValueRequest);
        if (bindingResult.hasErrors()) {
            final ErrorResponse errorResponse = assetCommonService.populateErrors(bindingResult);
            return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
        }
        final AssetCurrentValueResponse assetCurrentValueResponse = currentValueService
                .createCurrentValueAsync(assetCurrentValueRequest);
        return new ResponseEntity<>(assetCurrentValueResponse, HttpStatus.CREATED);
    }

    @PostMapping("depreciations/_create")
    @ResponseBody
    public ResponseEntity<?> saveDepreciation(@RequestBody @Valid final DepreciationRequest depreciationRequest,
            final BindingResult bindingResult, @RequestHeader final HttpHeaders headers) {
        log.debug("create depreciationRequest :" + depreciationRequest);
        if (bindingResult.hasErrors()) {
            final ErrorResponse errorResponse = assetCommonService.populateErrors(bindingResult);
            return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
        }

        log.debug("Request Headers :: " + headers);
        final DepreciationResponse depreciationResponse = depreciationservice.depreciateAsset(depreciationRequest,
                headers);
        return new ResponseEntity<>(depreciationResponse, HttpStatus.CREATED);
    }

    @PostMapping("depreciations/_search")
    @ResponseBody
    public ResponseEntity<?> depreciationReport(@RequestBody @Valid final RequestInfoWrapper requestInfoWrapper,
            @ModelAttribute @Valid final DepreciationReportCriteria depreciationReportCriteria,
            final BindingResult bindingResult) {
        log.debug("depreciationReportCriteria :" + depreciationReportCriteria);
        if (bindingResult.hasErrors()) {
            final ErrorResponse errorResponse = assetCommonService.populateErrors(bindingResult);
            return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
        }

        final DepreciationReportResponse depreciationResponse = depreciationservice.getDepreciationReport(
                requestInfoWrapper.getRequestInfo(),
                depreciationReportCriteria);
        return new ResponseEntity<>(depreciationResponse, HttpStatus.OK);
    }
}