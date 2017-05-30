/*
 * eGov suite of products aim to improve the internal efficiency,transparency,
 * accountability and the service delivery of the government  organizations.
 *
 *  Copyright (C) 2016  eGovernments Foundation
 *
 *  The updated version of eGov suite of products as by eGovernments Foundation
 *  is available at http://www.egovernments.org
 *
 *  This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this program. If not, see http://www.gnu.org/licenses/ or
 *  http://www.gnu.org/licenses/gpl.html .
 *
 *  In addition to the terms of the GPL license to be adhered to in using this
 *  program, the following additional terms are to be complied with:
 *
 *      1) All versions of this program, verbatim or modified must carry this
 *         Legal Notice.
 *
 *      2) Any misrepresentation of the origin of the material is prohibited. It
 *         is required that all modified versions of this material be marked in
 *         reasonable ways as different from the original version.
 *
 *      3) This license does not grant any rights to any user of the program
 *         with regards to rights under trademark law for use of the trade names
 *         or trademarks of eGovernments Foundation.
 *
 *  In case of any queries, you can reach eGovernments Foundation at contact@egovernments.org.
 */

package org.egov.wcms.web.controller;

import org.egov.common.contract.request.*;
import org.egov.common.contract.response.ErrorField;
import org.egov.common.contract.response.ResponseInfo;
import org.egov.wcms.config.ApplicationProperties;
import org.egov.wcms.model.UsageType;
import org.egov.wcms.service.UsageTypeService;
import org.egov.wcms.util.WcmsConstants;
import org.egov.wcms.web.contract.*;
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
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;


@RestController
@RequestMapping("/usagetype")
public class UsageTypeController {

    private static final Logger logger = LoggerFactory.getLogger(UsageTypeController.class);

    @Autowired
    private UsageTypeService usageTypeService;

    @Autowired
    private ErrorHandler errHandler;

    @Autowired
    private ResponseInfoFactory responseInfoFactory;

    @Autowired
    private ApplicationProperties applicationProperties;


    @PostMapping(value = "/_create")
    @ResponseBody
    public ResponseEntity<?> create(@RequestBody @Valid  final UsageTypeRequest usageTypeRequest,
                                    final BindingResult errors) {
        if (errors.hasErrors()) {
            final ErrorResponse errRes = populateErrors(errors);
            return new ResponseEntity<ErrorResponse>(errRes, HttpStatus.BAD_REQUEST);
        }
        logger.info("usageTypeRequest::" + usageTypeRequest);

        final List<ErrorResponse> errorResponses = validateUsageTypeRequest(usageTypeRequest);
        if (!errorResponses.isEmpty())
            return new ResponseEntity<List<ErrorResponse>>(errorResponses, HttpStatus.BAD_REQUEST);

        final UsageType usageType = usageTypeService.createUsageType(applicationProperties.getCreateUsageTypeTopicName(),"usagetype-create", usageTypeRequest);
        List<UsageType> usageTypes = new ArrayList<>();
        usageTypes.add(usageType);
        return getSuccessResponse(usageTypes, usageTypeRequest.getRequestInfo());

    }

    @PostMapping(value = "/_update/{code}")
    @ResponseBody
    public ResponseEntity<?> update(@RequestBody @Valid final UsageTypeRequest usageTypeRequest, final BindingResult errors, @PathVariable("code") String code) {
        if (errors.hasErrors()) {
            final ErrorResponse errRes = populateErrors(errors);
            return new ResponseEntity<ErrorResponse>(errRes, HttpStatus.BAD_REQUEST);
        }
        logger.info("usageTypeRequest::" + usageTypeRequest);
        usageTypeRequest.getUsageType().setCode(code);

        final List<ErrorResponse> errorResponses = validateUsageTypeRequest(usageTypeRequest);
        if (!errorResponses.isEmpty())
            return new ResponseEntity<List<ErrorResponse>>(errorResponses, HttpStatus.BAD_REQUEST);

        final UsageType usageType = usageTypeService.updateUsageType(applicationProperties.getUpdateUsageTypeTopicName(),"usagetype-update",usageTypeRequest);
        List<UsageType> usageTypes = new ArrayList<>();
        usageTypes.add(usageType);
        return getSuccessResponse(usageTypes, usageTypeRequest.getRequestInfo());
    }


    @PostMapping("_search")
    @ResponseBody
    public ResponseEntity<?> search(@ModelAttribute @Valid UsageTypeGetRequest usageTypeGetRequest,
                                    BindingResult modelAttributeBindingResult, @RequestBody @Valid RequestInfoWrapper requestInfoWrapper,
                                    BindingResult requestBodyBindingResult) {
        RequestInfo requestInfo = requestInfoWrapper.getRequestInfo();

        // validate input params
        if (modelAttributeBindingResult.hasErrors()) {
            return errHandler.getErrorResponseEntityForMissingParameters(modelAttributeBindingResult, requestInfo);
        }

        // validate input params
        if (requestBodyBindingResult.hasErrors()) {
            return errHandler.getErrorResponseEntityForMissingRequestInfo(requestBodyBindingResult, requestInfo);
        }

        // Call service
        List<UsageType> usageTypeList = null;
        try {
            usageTypeList = usageTypeService.getUsageTypes(usageTypeGetRequest);
        } catch (Exception exception) {
            logger.error("Error while processing request " + usageTypeGetRequest, exception);
            return errHandler.getResponseEntityForUnexpectedErrors(requestInfo);
        }

      return getSuccessResponse(usageTypeList, requestInfo);

    }

    private List<ErrorResponse> validateUsageTypeRequest(final UsageTypeRequest usageTypeRequest) {
        final List<ErrorResponse> errorResponses = new ArrayList<>();
        ErrorResponse errorResponse = new ErrorResponse();
        final Error error = getError(usageTypeRequest);
        errorResponse.setError(error);
        if(!errorResponse.getErrorFields().isEmpty())
            errorResponses.add(errorResponse);

        return errorResponses;
    }


    private Error getError(final UsageTypeRequest usageTypeRequest) {
        UsageType usageType = usageTypeRequest.getUsageType();
        List<ErrorField> errorFields = getErrorFields(usageTypeRequest);
        return Error.builder().code(HttpStatus.BAD_REQUEST.value())
                .message(WcmsConstants.INVALID_USAGETYPE_REQUEST_MESSAGE)
                .errorFields(errorFields)
                .build();
    }

    private List<ErrorField> getErrorFields(final UsageTypeRequest usageTypeRequest) {
        List<ErrorField> errorFields = new ArrayList<>();
        addUsageTypeNameValidationErrors(usageTypeRequest, errorFields);
        addTeanantIdValidationErrors(usageTypeRequest,errorFields);
        addActiveValidationErrors(usageTypeRequest,errorFields);
        return errorFields;
    }

    private void addUsageTypeNameValidationErrors(UsageTypeRequest usageTypeRequest, List<ErrorField> errorFields) {
        UsageType usageType=usageTypeRequest.getUsageType();
        if (usageType.getName() == null || usageType.getName().isEmpty()) {
            final ErrorField errorField = ErrorField.builder()
                    .code(WcmsConstants.USAGETYPE_NAME_MANDATORY_CODE)
                    .message(WcmsConstants.USAGETYPE_NAME_MANADATORY_ERROR_MESSAGE)
                    .field(WcmsConstants.USAGETYPE_NAME_MANADATORY_FIELD_NAME)
                    .build();
            errorFields.add(errorField);
        } else if (!usageTypeService.getUsageTypeByNameAndCode(usageType.getCode(),usageType.getName(),usageType.getTenantId())) {
            final ErrorField errorField = ErrorField.builder()
                    .code(WcmsConstants.USAGETYPE_NAME_UNIQUE_CODE)
                    .message(WcmsConstants.USAGETYPE_UNQ_ERROR_MESSAGE)
                    .field(WcmsConstants.USAGETYPE_NAME_UNQ_FIELD_NAME)
                    .build();
            errorFields.add(errorField);
   } else return;
    }

    private void addTeanantIdValidationErrors(UsageTypeRequest usageTypeRequest, List<ErrorField> errorFields){
        UsageType usageType=usageTypeRequest.getUsageType();
        if(usageType.getTenantId()==null || usageType.getTenantId().isEmpty()){
            final ErrorField errorField = ErrorField.builder()
                    .code(WcmsConstants.TENANTID_MANDATORY_CODE)
                    .message(WcmsConstants.TENANTID_MANADATORY_ERROR_MESSAGE)
                    .field(WcmsConstants.TENANTID_MANADATORY_FIELD_NAME)
                    .build();
            errorFields.add(errorField);
        } else return;
    }

    private void addActiveValidationErrors(UsageTypeRequest usageTypeRequest, List<ErrorField> errorFields){
        UsageType usageType=usageTypeRequest.getUsageType();
        if(usageType.getActive()==null){
            final ErrorField errorField = ErrorField.builder()
                    .code(WcmsConstants.ACTIVE_MANDATORY_CODE)
                    .message(WcmsConstants.ACTIVE_MANADATORY_ERROR_MESSAGE)
                    .field(WcmsConstants.ACTIVE_MANADATORY_FIELD_NAME)
                    .build();
            errorFields.add(errorField);
        } else return;
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

    private ResponseEntity<?> getSuccessResponse(List<UsageType> usageTypeList, RequestInfo requestInfo) {
        UsageTypeResponse usageTypeResponse = new UsageTypeResponse();
        usageTypeResponse.setUsageType(usageTypeList);
        ResponseInfo responseInfo = responseInfoFactory.createResponseInfoFromRequestInfo(requestInfo, true);
        responseInfo.setStatus(HttpStatus.OK.toString());
        usageTypeResponse.setResponseInfo(responseInfo);
        return new ResponseEntity<UsageTypeResponse>(usageTypeResponse, HttpStatus.OK);

    }


}
