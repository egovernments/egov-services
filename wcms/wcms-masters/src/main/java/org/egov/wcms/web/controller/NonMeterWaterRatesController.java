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

import java.util.List;

import javax.validation.Valid;

import org.apache.commons.lang3.StringUtils;
import org.egov.common.contract.request.RequestInfo;
import org.egov.common.contract.response.ResponseInfo;
import org.egov.wcms.config.ApplicationProperties;
import org.egov.wcms.model.NonMeterWaterRates;
import org.egov.wcms.service.NonMeterWaterRatesService;
import org.egov.wcms.util.ValidatorUtils;
import org.egov.wcms.web.contract.NonMeterWaterRatesGetReq;
import org.egov.wcms.web.contract.NonMeterWaterRatesReq;
import org.egov.wcms.web.contract.NonMeterWaterRatesResponse;
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

import lombok.extern.slf4j.Slf4j;

@RestController
@Slf4j
@RequestMapping("/nonmeterwaterrates")
public class NonMeterWaterRatesController {

    @Autowired
    private NonMeterWaterRatesService nonMeterWaterRatesService;

    @Autowired
    private ResponseInfoFactory responseInfoFactory;

    @Autowired
    private ApplicationProperties applicationProperties;

    @Autowired
    private ValidatorUtils validatorUtils;

    @Autowired
    private ErrorHandler errHandler;

    @PostMapping(value = "/_create")
    @ResponseBody
    public ResponseEntity<?> create(@RequestBody @Valid final NonMeterWaterRatesReq nonMeterWaterRatesReq,
            final BindingResult errors) {
        if (errors.hasErrors()) {
            final ErrorResponse errRes = validatorUtils.populateErrors(errors);
            return new ResponseEntity<>(errRes, HttpStatus.BAD_REQUEST);
        }
        log.info(" Non Meter Water Rates Request::" + nonMeterWaterRatesReq);

        final List<ErrorResponse> errorResponses = validatorUtils.validateNonMeterWaterRatesRequest(nonMeterWaterRatesReq, false);
        if (!errorResponses.isEmpty())
            return new ResponseEntity<>(errorResponses, HttpStatus.BAD_REQUEST);

        final List<NonMeterWaterRates> nonMeterWaterRates = nonMeterWaterRatesService.pushCreateToQueue(
                applicationProperties.getCreateNonMeterWaterRatesTopicName(), "nonmeterwaterrates-create", nonMeterWaterRatesReq);

        return getSuccessResponse(nonMeterWaterRates, "Created", nonMeterWaterRatesReq.getRequestInfo());
    }

    @PostMapping(value = "/_update")
    @ResponseBody
    public ResponseEntity<?> update(@RequestBody @Valid final NonMeterWaterRatesReq nonMeterWaterRatesReq,
            final BindingResult errors) {
        if (errors.hasErrors()) {
            final ErrorResponse errRes = validatorUtils.populateErrors(errors);
            return new ResponseEntity<>(errRes, HttpStatus.BAD_REQUEST);
        }
        log.info("NonMeterWaterRatesReq::" + nonMeterWaterRatesReq);

        final List<ErrorResponse> errorResponses = validatorUtils.validateNonMeterWaterRatesRequest(nonMeterWaterRatesReq, true);
        if (!errorResponses.isEmpty())
            return new ResponseEntity<>(errorResponses, HttpStatus.BAD_REQUEST);

        final List<NonMeterWaterRates> nonMeterWaterRates = nonMeterWaterRatesService.pushUpdateToQueue(
                applicationProperties.getUpdateNonMeterWaterRatesTopicName(), "nonmeterwaterrates-update", nonMeterWaterRatesReq);

        return getSuccessResponse(nonMeterWaterRates, null, nonMeterWaterRatesReq.getRequestInfo());
    }

    @PostMapping("/_search")
    @ResponseBody
    public ResponseEntity<?> search(
            @ModelAttribute @Valid final NonMeterWaterRatesGetReq nonMeterWaterRatesGetReq,
            final BindingResult modelAttributeBindingResult,
            @RequestBody @Valid final RequestInfoWrapper requestInfoWrapper,
            final BindingResult requestBodyBindingResult) {
        final RequestInfo requestInfo = requestInfoWrapper.getRequestInfo();

        // validate input params
        if (modelAttributeBindingResult.hasErrors())
            return errHandler.getErrorResponseEntityForMissingParameters(modelAttributeBindingResult, requestInfo);

        // validate input params
        if (requestBodyBindingResult.hasErrors())
            return errHandler.getErrorResponseEntityForMissingRequestInfo(requestBodyBindingResult, requestInfo);

        // Call service
        List<NonMeterWaterRates> nonMeterWaterRatesList = null;
        try {
            nonMeterWaterRatesList = nonMeterWaterRatesService.getNonMeterWaterRates(nonMeterWaterRatesGetReq);
        } catch (final Exception exception) {
            log.error("Error while processing request " + nonMeterWaterRatesGetReq, exception);
            return errHandler.getResponseEntityForUnexpectedErrors(requestInfo);
        }

        return getSuccessResponse(nonMeterWaterRatesList, null, requestInfo);

    }

    private ResponseEntity<?> getSuccessResponse(final List<NonMeterWaterRates> nonMeterWaterRates,
            final String mode, final RequestInfo requestInfo) {
        final NonMeterWaterRatesResponse nonMeterWaterRatesResponse = new NonMeterWaterRatesResponse();
        nonMeterWaterRatesResponse.setNonMeterWaterRates(nonMeterWaterRates);
        final ResponseInfo responseInfo = responseInfoFactory.createResponseInfoFromRequestInfo(requestInfo, true);
        if (StringUtils.isNotBlank(mode))
            responseInfo.setStatus(HttpStatus.CREATED.toString());
        else
            responseInfo.setStatus(HttpStatus.OK.toString());
        nonMeterWaterRatesResponse.setResponseInfo(responseInfo);
        return new ResponseEntity<>(nonMeterWaterRatesResponse, HttpStatus.OK);

    }

}
