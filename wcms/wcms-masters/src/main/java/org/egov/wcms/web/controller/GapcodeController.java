/*
 * eGov suite of products aim to improve the internal efficiency,transparency,
 *    accountability and the service delivery of the government  organizations.
 *
 *     Copyright (C) <2015>  eGovernments Foundation
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
 */
package org.egov.wcms.web.controller;

import java.util.ArrayList;
import java.util.List;

import javax.validation.Valid;

import lombok.extern.slf4j.Slf4j;

import org.apache.commons.lang3.StringUtils;
import org.egov.common.contract.request.RequestInfo;
import org.egov.common.contract.response.ResponseInfo;
import org.egov.wcms.config.ApplicationProperties;
import org.egov.wcms.model.CommonDataModel;
import org.egov.wcms.model.Gapcode;
import org.egov.wcms.model.enums.GapcodeLastMonths;
import org.egov.wcms.model.enums.GapcodeLogic;
import org.egov.wcms.service.GapcodeService;
import org.egov.wcms.util.ValidatorUtils;
import org.egov.wcms.web.contract.CommonEnumResponse;
import org.egov.wcms.web.contract.GapcodeGetRequest;
import org.egov.wcms.web.contract.GapcodeRequest;
import org.egov.wcms.web.contract.GapcodeResponse;
import org.egov.wcms.web.contract.RequestInfoWrapper;
import org.egov.wcms.web.contract.factory.ResponseInfoFactory;
import org.egov.wcms.web.errorhandlers.ErrorHandler;
import org.egov.wcms.web.errorhandlers.ErrorResponse;
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

@RestController
@Slf4j
@RequestMapping("/gapcodes")
public class GapcodeController {

    @Autowired
    private ErrorHandler errHandler;

    @Autowired
    private ApplicationProperties applicationProperties;

    @Autowired
    private ValidatorUtils validatorUtils;

    @Autowired
    private GapcodeService gapcodeServie;

    @Autowired
    private ResponseInfoFactory responseInfoFactory;

    @PostMapping("_create")
    @ResponseBody
    public ResponseEntity<?> createGapcode(@RequestBody @Valid final GapcodeRequest gapcodeRequest,
            final BindingResult errors) {

        if (errors.hasErrors()) {
            final ErrorResponse errRes = validatorUtils.populateErrors(errors);
            return new ResponseEntity<>(errRes, HttpStatus.BAD_REQUEST);
        }
        log.info("gapcodeRequest::" + gapcodeRequest);

        final List<ErrorResponse> errorResponses = validatorUtils.validateGapcodeRequest(gapcodeRequest, false);
        if (!errorResponses.isEmpty())
            return new ResponseEntity<>(errorResponses, HttpStatus.BAD_REQUEST);

        final List<Gapcode> gapcodeList = gapcodeServie.pushCreateToQueue(applicationProperties.getCreateGapcodeTopicName(),
                "gapcode-create", gapcodeRequest);

        return getSuccessResponse(gapcodeList, "Created", gapcodeRequest.getRequestInfo());
    }

    @PostMapping("_update")
    public ResponseEntity<?> updateGapcode(@RequestBody @Valid final GapcodeRequest gapcodeRequest, final BindingResult errors) {

        if (errors.hasErrors()) {
            final ErrorResponse errRes = validatorUtils.populateErrors(errors);
            return new ResponseEntity<>(errRes, HttpStatus.BAD_REQUEST);
        }

        log.info("gapcodeRequest::" + gapcodeRequest);

        final List<ErrorResponse> errorResponses = validatorUtils.validateGapcodeRequest(gapcodeRequest, false);
        if (!errorResponses.isEmpty())
            return new ResponseEntity<>(errorResponses, HttpStatus.BAD_REQUEST);

        final List<Gapcode> gapcodeList = gapcodeServie.pushUpdateToQueue(applicationProperties.getUpdateGapcodeTopicName(),
                "gapcode-update", gapcodeRequest);

        return getSuccessResponse(gapcodeList, "Updated", gapcodeRequest.getRequestInfo());

    }

    @PostMapping("_search")
    public ResponseEntity<?> searchGapcode(@ModelAttribute @Valid final GapcodeGetRequest gapcodeGetRequest,
            final BindingResult modelAttributeBindingResult,
            @RequestBody @Valid final RequestInfoWrapper requestInfoWrapper,
            final BindingResult requestBodyBindingResult) {

        final RequestInfo requestInfo = requestInfoWrapper.getRequestInfo();

        if (modelAttributeBindingResult.hasErrors())
            return errHandler.getErrorResponseEntityForMissingParameters(modelAttributeBindingResult, requestInfo);

        if (requestBodyBindingResult.hasErrors())
            return errHandler.getErrorResponseEntityForMissingRequestInfo(requestBodyBindingResult, requestInfo);

        List<Gapcode> gapCodeList = null;
        try {
            gapCodeList = gapcodeServie.getGapcodes(gapcodeGetRequest);
        } catch (final Exception exception) {
            log.error("Error while processing request " + gapcodeGetRequest, exception);
            return errHandler.getResponseEntityForUnexpectedErrors(requestInfo);
        }

        return getSuccessResponse(gapCodeList, null, requestInfo);
    }

    @PostMapping("/formula/_search")
    @ResponseBody
    public ResponseEntity<?> searchGapcodeFormula(@RequestBody @Valid final RequestInfoWrapper requestInfoWrapper,
            final BindingResult bindingResult) {
        final RequestInfo requestInfo = requestInfoWrapper.getRequestInfo();

        if (bindingResult.hasErrors())
            return errHandler.getErrorResponseEntityForMissingRequestInfo(bindingResult, requestInfo);

        return getSuccessResponse(gapcodeServie.getFormulaQuery(), requestInfoWrapper.getRequestInfo());
    }

    @PostMapping("/lastmonths/_search")
    @ResponseBody
    public ResponseEntity<?> searchGapcodeNoOfLastMonths(@RequestBody @Valid final RequestInfoWrapper requestInfoWrapper,
            final BindingResult bindingResult) {
        final RequestInfo requestInfo = requestInfoWrapper.getRequestInfo();

        if (bindingResult.hasErrors())
            return errHandler.getErrorResponseEntityForMissingRequestInfo(bindingResult, requestInfo);

        final List<CommonDataModel> modelList = new ArrayList<>();
        for (final GapcodeLastMonths key : GapcodeLastMonths.values())
            modelList.add(new CommonDataModel(key.name(), key.getValue()));
        return getSuccessResponse(modelList, requestInfoWrapper.getRequestInfo());
    }

    @PostMapping("/logic/_search")
    @ResponseBody
    public ResponseEntity<?> searchGapcodeLogic(@RequestBody @Valid final RequestInfoWrapper requestInfoWrapper,
            final BindingResult bindingResult) {
        final RequestInfo requestInfo = requestInfoWrapper.getRequestInfo();

        if (bindingResult.hasErrors())
            return errHandler.getErrorResponseEntityForMissingRequestInfo(bindingResult, requestInfo);

        final List<CommonDataModel> modelList = new ArrayList<>();
        for (final GapcodeLogic key : GapcodeLogic.values())
            modelList.add(new CommonDataModel(key.name(), key));
        return getSuccessResponse(modelList, requestInfoWrapper.getRequestInfo());
    }

    private ResponseEntity<?> getSuccessResponse(final List<CommonDataModel> modelList,
            final RequestInfo requestInfo) {
        final CommonEnumResponse response = new CommonEnumResponse();
        final ResponseInfo responseInfo = responseInfoFactory.createResponseInfoFromRequestInfo(requestInfo, true);
        responseInfo.setStatus(HttpStatus.CREATED.toString());
        response.setResponseInfo(responseInfo);
        response.setDataModelList(modelList);
        return new ResponseEntity<>(response, HttpStatus.CREATED);

    }

    private ResponseEntity<?> getSuccessResponse(final List<Gapcode> gapcodeList, final String mode,
            final RequestInfo requestInfo) {
        final GapcodeResponse gapcodeResponse = new GapcodeResponse();
        gapcodeResponse.setGapcodeResponse(gapcodeList);
        final ResponseInfo responseInfo = responseInfoFactory.createResponseInfoFromRequestInfo(requestInfo, true);
        if (StringUtils.isNotBlank(mode))
            responseInfo.setStatus(HttpStatus.CREATED.toString());
        else
            responseInfo.setStatus(HttpStatus.OK.toString());
        gapcodeResponse.setResponseInfo(responseInfo);
        return new ResponseEntity<>(gapcodeResponse, HttpStatus.OK);
    }
}
