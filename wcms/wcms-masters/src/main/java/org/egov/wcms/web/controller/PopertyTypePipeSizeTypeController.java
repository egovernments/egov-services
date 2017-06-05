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

import java.util.ArrayList;
import java.util.List;

import javax.validation.Valid;

import org.egov.common.contract.request.RequestInfo;
import org.egov.common.contract.response.ErrorField;
import org.egov.common.contract.response.ResponseInfo;
import org.egov.wcms.config.ApplicationProperties;
import org.egov.wcms.model.PropertyTypePipeSizeType;
import org.egov.wcms.service.PropertyTypePipeSizeTypeService;
import org.egov.wcms.util.WcmsConstants;
import org.egov.wcms.web.contract.PropertyTypePipeSizeTypeGetRequest;
import org.egov.wcms.web.contract.PropertyTypePipeSizeTypeRequest;
import org.egov.wcms.web.contract.PropertyTypePipeSizeTypeResponse;
import org.egov.wcms.web.contract.RequestInfoWrapper;
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
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/propertytype-pipesizetype")
public class PopertyTypePipeSizeTypeController {

    private static final Logger logger = LoggerFactory.getLogger(PopertyTypePipeSizeTypeController.class);

    @Autowired
    private PropertyTypePipeSizeTypeService propertPipeSizeService;

    @Autowired
    private ErrorHandler errHandler;

    @Autowired
    private ResponseInfoFactory responseInfoFactory;

    @Autowired
    private ApplicationProperties applicationProperties;

    @PostMapping(value = "/_create")
    @ResponseBody
    public ResponseEntity<?> create(@RequestBody @Valid final PropertyTypePipeSizeTypeRequest propertyPipeSizeRequest,
            final BindingResult errors) {
        if (errors.hasErrors()) {
            final ErrorResponse errRes = populateErrors(errors);
            return new ResponseEntity<ErrorResponse>(errRes, HttpStatus.BAD_REQUEST);
        }
        logger.info("propertyPipeSizeRequest::" + propertyPipeSizeRequest);

        final List<ErrorResponse> errorResponses = validatePropertyPipeSizeRequest(propertyPipeSizeRequest);
        if (!errorResponses.isEmpty())
            return new ResponseEntity<List<ErrorResponse>>(errorResponses, HttpStatus.BAD_REQUEST);

        final PropertyTypePipeSizeType propertyPipeSize = propertPipeSizeService.createPropertyPipeSize(
                applicationProperties.getCreatePropertyPipeSizeTopicName(), "propertypipesize-create", propertyPipeSizeRequest);
        final List<PropertyTypePipeSizeType> propertyPipeSizes = new ArrayList<>();
        propertyPipeSizes.add(propertyPipeSize);
        return getSuccessResponse(propertyPipeSizes, propertyPipeSizeRequest.getRequestInfo());

    }

    @PostMapping(value = "/_update/{propertyPipeSizeId}")
    @ResponseBody
    public ResponseEntity<?> update(@RequestBody @Valid final PropertyTypePipeSizeTypeRequest propertyPipeSizeRequest,
            final BindingResult errors,
            @PathVariable("propertyPipeSizeId") final Long propertyPipeSizeId) {
        if (errors.hasErrors()) {
            final ErrorResponse errRes = populateErrors(errors);
            return new ResponseEntity<ErrorResponse>(errRes, HttpStatus.BAD_REQUEST);
        }
        logger.info("propertyPipeSizeRequest::" + propertyPipeSizeRequest);
        propertyPipeSizeRequest.getPropertyPipeSize().setId(propertyPipeSizeId);

        final List<ErrorResponse> errorResponses = validatePropertyPipeSizeRequest(propertyPipeSizeRequest);
        if (!errorResponses.isEmpty())
            return new ResponseEntity<List<ErrorResponse>>(errorResponses, HttpStatus.BAD_REQUEST);

        final PropertyTypePipeSizeType propertyPipeSize = propertPipeSizeService.createPropertyPipeSize(
                applicationProperties.getUpdatePropertyPipeSizeTopicName(), "propertypipesize-update", propertyPipeSizeRequest);
        final List<PropertyTypePipeSizeType> propertyPipeSizes = new ArrayList<>();
        propertyPipeSizes.add(propertyPipeSize);
        return getSuccessResponse(propertyPipeSizes, propertyPipeSizeRequest.getRequestInfo());
    }

    @PostMapping("_search")
    @ResponseBody
    public ResponseEntity<?> search(@ModelAttribute @Valid final PropertyTypePipeSizeTypeGetRequest propertyPipeSizeGetRequest,
            final BindingResult modelAttributeBindingResult, @RequestBody @Valid final RequestInfoWrapper requestInfoWrapper,
            final BindingResult requestBodyBindingResult) {
        final RequestInfo requestInfo = requestInfoWrapper.getRequestInfo();

        // validate input params
        if (modelAttributeBindingResult.hasErrors())
            return errHandler.getErrorResponseEntityForMissingParameters(modelAttributeBindingResult, requestInfo);

        // validate input params
        if (requestBodyBindingResult.hasErrors())
            return errHandler.getErrorResponseEntityForMissingRequestInfo(requestBodyBindingResult, requestInfo);

        // Call service
        List<PropertyTypePipeSizeType> propertyPipeSizeList = null;
        try {
            propertyPipeSizeList = propertPipeSizeService.getPropertyPipeSizes(propertyPipeSizeGetRequest);
        } catch (final Exception exception) {
            logger.error("Error while processing request " + propertyPipeSizeGetRequest, exception);
            return errHandler.getResponseEntityForUnexpectedErrors(requestInfo);
        }

        return getSuccessResponse(propertyPipeSizeList, requestInfo);

    }

    private List<ErrorResponse> validatePropertyPipeSizeRequest(final PropertyTypePipeSizeTypeRequest propertyPipeSizeRequest) {
        final List<ErrorResponse> errorResponses = new ArrayList<>();
        final ErrorResponse errorResponse = new ErrorResponse();
        final Error error = getError(propertyPipeSizeRequest);
        errorResponse.setError(error);
        if (!errorResponse.getErrorFields().isEmpty())
            errorResponses.add(errorResponse);

        return errorResponses;
    }

    private Error getError(final PropertyTypePipeSizeTypeRequest propertyPipeSizeRequest) {
        propertyPipeSizeRequest.getPropertyPipeSize();
        final List<ErrorField> errorFields = getErrorFields(propertyPipeSizeRequest);
        return Error.builder().code(HttpStatus.BAD_REQUEST.value())
                .message(WcmsConstants.INVALID_PROPERTY_PIPESIZE_REQUEST_MESSAGE)
                .errorFields(errorFields)
                .build();
    }

    private List<ErrorField> getErrorFields(final PropertyTypePipeSizeTypeRequest propertyPipeSizeRequest) {
        final List<ErrorField> errorFields = new ArrayList<>();
        addPropertyPipeSizeValidationErrors(propertyPipeSizeRequest, errorFields);
        addTeanantIdValidationErrors(propertyPipeSizeRequest, errorFields);
        addActiveValidationErrors(propertyPipeSizeRequest, errorFields);
        return errorFields;
    }

    private void addPropertyPipeSizeValidationErrors(final PropertyTypePipeSizeTypeRequest propertyPipeSizeRequest,
            final List<ErrorField> errorFields) {
        final PropertyTypePipeSizeType propertyPipeSize = propertyPipeSizeRequest.getPropertyPipeSize();
        if (propertyPipeSize.getPropertyTypeName() == null && !propertyPipeSize.getPropertyTypeName().isEmpty()) {
            final ErrorField errorField = ErrorField.builder()
                    .code(WcmsConstants.PROPERTY_PIPESIZE_PROPERTYTYPE_MANDATORY_CODE)
                    .message(WcmsConstants.PROPERTY_PIPESIZE_PROPERTYTYPE_MANADATORY_ERROR_MESSAGE)
                    .field(WcmsConstants.PROPERTY_PIPESIZE_PROPERTYTYPE_MANADATORY_FIELD_NAME)
                    .build();
            errorFields.add(errorField);
        } else if (propertyPipeSize.getPipeSizeType() == null) {
            final ErrorField errorField = ErrorField.builder()
                    .code(WcmsConstants.PROPERTY_PIPESIZE_HSCSIZEINMM_MANDATORY_CODE)
                    .message(WcmsConstants.PROPERTY_PIPESIZE_HSCSIZEINMM_MANADATORY_ERROR_MESSAGE)
                    .field(WcmsConstants.PROPERTY_PIPESIZE_HSCSIZEINMM_MANADATORY_FIELD_NAME)
                    .build();
            errorFields.add(errorField);
        } else if (propertPipeSizeService.checkPipeSizeExists(propertyPipeSize.getPipeSizeType(),
                propertyPipeSize.getTenantId())) {
            final ErrorField errorField = ErrorField.builder()
                    .code(WcmsConstants.PROPERTY_PIPESIZE_HSCSIZEINMM_INVALID_CODE)
                    .message(WcmsConstants.PROPERTY_PIPESIZE_HSCSIZEINMM_INVALID_ERROR_MESSAGE)
                    .field(WcmsConstants.PROPERTY_PIPESIZE_HSCSIZEINMM_INVALID_FIELD_NAME)
                    .build();
            errorFields.add(errorField);

        } else if (!propertPipeSizeService.checkPropertyByPipeSize(propertyPipeSize.getId(), propertyPipeSize.getPropertyTypeId(),
                propertyPipeSize.getPipeSizeId(), propertyPipeSize.getTenantId())) {
            final ErrorField errorField = ErrorField.builder()
                    .code(WcmsConstants.PROPERTY_PIPESIZE_SIZEINMM_UNIQUE_CODE)
                    .message(WcmsConstants.PROPERTY_PIPESIZE_SIZEINMM_UNQ_ERROR_MESSAGE)
                    .field(WcmsConstants.PROPERTY_PIPESIZE_SIZEINMM_UNQ_FIELD_NAME)
                    .build();
            errorFields.add(errorField);
        } else
            return;
    }

    private void addTeanantIdValidationErrors(final PropertyTypePipeSizeTypeRequest propertyPipeSizeRequest,
            final List<ErrorField> errorFields) {
        final PropertyTypePipeSizeType propertyPipeSize = propertyPipeSizeRequest.getPropertyPipeSize();
        if (propertyPipeSize.getTenantId() == null || propertyPipeSize.getTenantId().isEmpty()) {
            final ErrorField errorField = ErrorField.builder()
                    .code(WcmsConstants.TENANTID_MANDATORY_CODE)
                    .message(WcmsConstants.TENANTID_MANADATORY_ERROR_MESSAGE)
                    .field(WcmsConstants.TENANTID_MANADATORY_FIELD_NAME)
                    .build();
            errorFields.add(errorField);
        } else
            return;
    }

    private void addActiveValidationErrors(final PropertyTypePipeSizeTypeRequest propertyPipeSizeRequest,
            final List<ErrorField> errorFields) {
        final PropertyTypePipeSizeType propertyPipeSize = propertyPipeSizeRequest.getPropertyPipeSize();
        if (propertyPipeSize.getActive() == null) {
            final ErrorField errorField = ErrorField.builder()
                    .code(WcmsConstants.ACTIVE_MANDATORY_CODE)
                    .message(WcmsConstants.ACTIVE_MANADATORY_ERROR_MESSAGE)
                    .field(WcmsConstants.ACTIVE_MANADATORY_FIELD_NAME)
                    .build();
            errorFields.add(errorField);
        } else
            return;
    }

    private ErrorResponse populateErrors(final BindingResult errors) {
        final ErrorResponse errRes = new ErrorResponse();

        final Error error = new Error();
        error.setCode(1);
        error.setDescription("Error while binding request");
        if (errors.hasFieldErrors())
            for (final FieldError fieldError : errors.getFieldErrors())
                error.getFields().put(fieldError.getField(), fieldError.getRejectedValue());
        errRes.setError(error);
        return errRes;
    }

    private ResponseEntity<?> getSuccessResponse(final List<PropertyTypePipeSizeType> propertyPipeSizeList,
            final RequestInfo requestInfo) {
        final PropertyTypePipeSizeTypeResponse propertyPipeSizeResponse = new PropertyTypePipeSizeTypeResponse();
        propertyPipeSizeResponse.setPropertyPipeSizes(propertyPipeSizeList);
        final ResponseInfo responseInfo = responseInfoFactory.createResponseInfoFromRequestInfo(requestInfo, true);
        responseInfo.setStatus(HttpStatus.OK.toString());
        propertyPipeSizeResponse.setResponseInfo(responseInfo);
        return new ResponseEntity<PropertyTypePipeSizeTypeResponse>(propertyPipeSizeResponse, HttpStatus.OK);

    }

}
