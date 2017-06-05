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

import org.egov.common.contract.request.RequestInfo;
import org.egov.common.contract.response.ErrorField;
import org.egov.common.contract.response.ResponseInfo;
import org.egov.wcms.config.ApplicationProperties;
import org.egov.wcms.model.MeterCost;
import org.egov.wcms.service.MeterCostService;
import org.egov.wcms.util.WcmsConstants;
import org.egov.wcms.web.contract.MeterCostRequest;
import org.egov.wcms.web.contract.MeterCostResponse;
import org.egov.wcms.web.contract.factory.ResponseInfoFactory;
import org.egov.wcms.web.errorhandlers.Error;
import org.egov.wcms.web.errorhandlers.ErrorHandler;
import org.egov.wcms.web.errorhandlers.ErrorResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/metercost")
public class MeterCostController {

    private static final Logger logger = LoggerFactory.getLogger(MeterCostController.class);

    @Autowired
    private MeterCostService meterCostService;

    @Autowired
    private ErrorHandler errHandler;

    @Autowired
    private ResponseInfoFactory responseInfoFactory;

    @Autowired
    private ApplicationProperties applicationProperties;


    @PostMapping(value = "/_create")
    @ResponseBody
    public ResponseEntity<?> create(@RequestBody @Valid  final MeterCostRequest meterCostRequest, final BindingResult errors) {
        if (errors.hasErrors()) {
            final ErrorResponse errRes = populateErrors(errors);
            return new ResponseEntity<ErrorResponse>(errRes, HttpStatus.BAD_REQUEST);
        }
        logger.info("meterCostRequest::" + meterCostRequest);

        final List<ErrorResponse> errorResponses = validateMeterCostRequest(meterCostRequest);
        if (!errorResponses.isEmpty())
            return new ResponseEntity<List<ErrorResponse>>(errorResponses, HttpStatus.BAD_REQUEST);

        final MeterCost meterCost = meterCostService.createMeterCost(applicationProperties.getCreateMeterCostTopicName(),"metercost-create", meterCostRequest);
        List<MeterCost> meterCosts = new ArrayList<>();
        meterCosts.add(meterCost);
        return getSuccessResponse(meterCosts, meterCostRequest.getRequestInfo());

    }
    
    
    private ResponseEntity<?> getSuccessResponse(List<MeterCost> meterCostList, RequestInfo requestInfo) {
        MeterCostResponse meterCostResponse = new MeterCostResponse();
        meterCostResponse.setMeterCost(meterCostList);
        ResponseInfo responseInfo = responseInfoFactory.createResponseInfoFromRequestInfo(requestInfo, true);
        responseInfo.setStatus(HttpStatus.OK.toString());
        meterCostResponse.setResponseInfo(responseInfo);
        return new ResponseEntity<MeterCostResponse>(meterCostResponse, HttpStatus.OK);

    }
    
    private ErrorResponse populateErrors(final BindingResult errors) {
        final ErrorResponse errRes = new ErrorResponse();

        final Error error = new Error();
        error.setCode(1);
        error.setDescription("Error while binding request");
        if (errors.hasFieldErrors())
            for (final FieldError fieldError : errors.getFieldErrors()) {
                error.getFields().put(fieldError.getField(), fieldError.getRejectedValue());
            }
        errRes.setError(error);
        return errRes;
    }
    
    private List<ErrorResponse> validateMeterCostRequest(final MeterCostRequest meterCostRequest) {
        final List<ErrorResponse> errorResponses = new ArrayList<>();
        ErrorResponse errorResponse = new ErrorResponse();
        final Error error = getError(meterCostRequest);
        errorResponse.setError(error);
        if(!errorResponse.getErrorFields().isEmpty())
            errorResponses.add(errorResponse);

        return errorResponses;
    }
    
    private Error getError(final MeterCostRequest meterCostRequest) {
       // MeterCost meterCost = meterCostRequest.getMeterCost();
        List<ErrorField> errorFields = getErrorFields(meterCostRequest);
        return Error.builder().code(HttpStatus.BAD_REQUEST.value())
                .message(WcmsConstants.INVALID_USAGETYPE_REQUEST_MESSAGE)
                .errorFields(errorFields)
                .build();
    }
    
    private List<ErrorField> getErrorFields(final MeterCostRequest meterCostRequest) {
        List<ErrorField> errorFields = new ArrayList<>();
        addTeanantIdValidationErrors(meterCostRequest,errorFields);
        addActiveValidationErrors(meterCostRequest,errorFields);
        return errorFields;
    }
    
    private void addTeanantIdValidationErrors(MeterCostRequest meterCostRequest, List<ErrorField> errorFields){
        MeterCost meterCost=meterCostRequest.getMeterCost();
        if(meterCost.getTenantId()==null || meterCost.getTenantId().isEmpty()){
            final ErrorField errorField = ErrorField.builder()
                    .code(WcmsConstants.TENANTID_MANDATORY_CODE)
                    .message(WcmsConstants.TENANTID_MANADATORY_ERROR_MESSAGE)
                    .field(WcmsConstants.TENANTID_MANADATORY_FIELD_NAME)
                    .build();
            errorFields.add(errorField);
        } else return;
    }

    private void addActiveValidationErrors(MeterCostRequest meterCostRequest, List<ErrorField> errorFields){
    	MeterCost meterCost=meterCostRequest.getMeterCost();
        if(meterCost.getActive()==null){
            final ErrorField errorField = ErrorField.builder()
                    .code(WcmsConstants.ACTIVE_MANDATORY_CODE)
                    .message(WcmsConstants.ACTIVE_MANADATORY_ERROR_MESSAGE)
                    .field(WcmsConstants.ACTIVE_MANADATORY_FIELD_NAME)
                    .build();
            errorFields.add(errorField);
        } else return;
    }
}

