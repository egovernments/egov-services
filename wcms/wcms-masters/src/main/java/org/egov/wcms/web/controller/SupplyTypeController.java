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
import org.egov.wcms.model.SupplyType;
import org.egov.wcms.service.SupplyTypeService;
import org.egov.wcms.util.WcmsConstants;
import org.egov.wcms.web.contract.RequestInfoWrapper;
import org.egov.wcms.web.contract.SupplyTypeGetRequest;
import org.egov.wcms.web.contract.SupplyTypeRequest;
import org.egov.wcms.web.contract.SupplyTypeResponse;
import org.egov.wcms.web.contract.factory.ResponseInfoFactory;
import org.egov.wcms.web.errorhandlers.Error;
import org.egov.wcms.web.errorhandlers.ErrorHandler;
import org.egov.wcms.web.errorhandlers.ErrorResponse;
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
@RequestMapping("/supplytype")
public class SupplyTypeController {

    @Autowired
    private SupplyTypeService supplyTypeService;

    @Autowired
    private ApplicationProperties applicationProperties;

    @Autowired
    private ResponseInfoFactory responseInfoFactory;

    @Autowired
    private ErrorHandler errHandler;

    @SuppressWarnings("unchecked")
    @PostMapping("/_create")
    @ResponseBody
    public ResponseEntity<?> create(@RequestBody @Valid final SupplyTypeRequest supplyTypeRequest, final BindingResult errors) {
        if (errors.hasErrors()) {
            final ErrorResponse errRes = populateErrors(errors);
            return new ResponseEntity(errRes, HttpStatus.BAD_REQUEST);
        }

        final List<ErrorResponse> errorRespList = validateSupplyType(supplyTypeRequest);
        if (!errorRespList.isEmpty())
            return new ResponseEntity(errorRespList, HttpStatus.BAD_REQUEST);

        final SupplyType supplytypeobj = supplyTypeService.createSupplyType(applicationProperties.getCreateSupplyTypeTopicName(),
                "supplytype-create", supplyTypeRequest);
        final List<SupplyType> supplyTypes = new ArrayList<>();
        supplyTypes.add(supplytypeobj);
        return getSuccessResponse(supplyTypes, supplyTypeRequest.getRequestInfo());
    }

    @PostMapping(value = "/{code}/_update")
    @ResponseBody
    public ResponseEntity<?> update(@RequestBody @Valid final SupplyTypeRequest supplyTypeRequest,
            final BindingResult errors, @PathVariable("code") final String code) {
        if (errors.hasErrors()) {
            final ErrorResponse errRes = populateErrors(errors);
            return new ResponseEntity<>(errRes, HttpStatus.BAD_REQUEST);
        }
        supplyTypeRequest.getSupplyType().setCode(code);

        final List<ErrorResponse> errorResponses = validateSupplyType(supplyTypeRequest);
        if (!errorResponses.isEmpty())
            return new ResponseEntity<>(errorResponses, HttpStatus.BAD_REQUEST);

        final SupplyType supplyType = supplyTypeService.updateSupplyType(applicationProperties.getUpdateSupplyTypeTopicName(),
                "supplyType-update", supplyTypeRequest);
        final List<SupplyType> supplyTypes = new ArrayList<>();
        supplyTypes.add(supplyType);
        return getSuccessResponse(supplyTypes, supplyTypeRequest.getRequestInfo());
    }

    @PostMapping("_search")
    @ResponseBody
    public ResponseEntity<?> search(@ModelAttribute @Valid final SupplyTypeGetRequest supplyGetRequest,
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
        List<SupplyType> supplyTypeList = null;
        try {
            supplyTypeList = supplyTypeService.getSupplyTypes(supplyGetRequest);
        } catch (final Exception exception) {
            return errHandler.getResponseEntityForUnexpectedErrors(requestInfo);
        }

        return getSuccessResponse(supplyTypeList, requestInfo);
    }

    private ResponseEntity<?> getSuccessResponse(final List<SupplyType> supplyType, final RequestInfo requestInfo) {
        final SupplyTypeResponse supplyResponse = new SupplyTypeResponse();
        supplyResponse.setSupplytypes(supplyType);
        final ResponseInfo responseInfo = responseInfoFactory.createResponseInfoFromRequestInfo(requestInfo, true);
        responseInfo.setStatus(HttpStatus.OK.toString());
        supplyResponse.setResponseInfo(responseInfo);
        return new ResponseEntity<>(supplyResponse, HttpStatus.OK);
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

    private List<ErrorField> getErrorFields(final SupplyTypeRequest supplyTypeRequest) {
        final List<ErrorField> errorFields = new ArrayList<>();
        addCategoryNameValidationErrors(supplyTypeRequest, errorFields);
        addTeanantIdValidationErrors(supplyTypeRequest, errorFields);
        addActiveValidationErrors(supplyTypeRequest, errorFields);
        return errorFields;
    }

    private void addCategoryNameValidationErrors(final SupplyTypeRequest supplyTypeRequest,
            final List<ErrorField> errorFields) {
        final SupplyType supply = supplyTypeRequest.getSupplyType();
        if (supply.getName() == null || supply.getName().isEmpty()) {
            final ErrorField errorField = ErrorField.builder()
                    .code(WcmsConstants.SUPPLYTYPE_NAME_MANDATORY_CODE)
                    .message(WcmsConstants.SUPPLYTYPE_NAME_MANADATORY_ERROR_MESSAGE)
                    .field(WcmsConstants.SUPPLYTYPE_NAME_MANADATORY_FIELD_NAME)
                    .build();
            errorFields.add(errorField);
        } else if (supply.getCode() !=null && !supplyTypeService.getSupplyTypeByNameAndCode(supply.getCode(), supply.getName(), supply.getTenantId())) {
            final ErrorField errorField = ErrorField.builder()
                    .code(WcmsConstants.SUPPLYTYPE_NAME_UNIQUE_CODE)
                    .message(WcmsConstants.SUPPLYTYPE_UNQ_ERROR_MESSAGE)
                    .field(WcmsConstants.SUPPLYTYPE_NAME_UNQ_FIELD_NAME)
                    .build();
            errorFields.add(errorField);
        } else
            return;
    }

    private void addTeanantIdValidationErrors(final SupplyTypeRequest supplyTypeRequest,
            final List<ErrorField> errorFields) {
        final SupplyType supply = supplyTypeRequest.getSupplyType();
        if (supply.getTenantId() == null || supply.getTenantId().isEmpty()) {
            final ErrorField errorField = ErrorField.builder()
                    .code(WcmsConstants.TENANTID_MANDATORY_CODE)
                    .message(WcmsConstants.TENANTID_MANADATORY_ERROR_MESSAGE)
                    .field(WcmsConstants.TENANTID_MANADATORY_FIELD_NAME)
                    .build();
            errorFields.add(errorField);
        } else
            return;
    }

    private void addActiveValidationErrors(final SupplyTypeRequest supplyTypeRequest, final List<ErrorField> errorFields) {
        final SupplyType supply = supplyTypeRequest.getSupplyType();
        if (supply.getActive() == null) {
            final ErrorField errorField = ErrorField.builder()
                    .code(WcmsConstants.ACTIVE_MANDATORY_CODE)
                    .message(WcmsConstants.ACTIVE_MANADATORY_ERROR_MESSAGE)
                    .field(WcmsConstants.ACTIVE_MANADATORY_FIELD_NAME)
                    .build();
            errorFields.add(errorField);
        } else
            return;
    }

    private Error getError(final SupplyTypeRequest supplyTypeRequest) {
        final List<ErrorField> errorFiled = getErrorFields(supplyTypeRequest);
        return Error.builder().code(HttpStatus.BAD_REQUEST.value())
                .message(WcmsConstants.INVALID_CATEGORY_REQUEST_MESSAGE)
                .errorFields(errorFiled).build();
    }

    private List<ErrorResponse> validateSupplyType(final SupplyTypeRequest supplyTypeRequest) {
        final ErrorResponse errorResponse = new ErrorResponse();
        final List<ErrorResponse> errorResponseList = new ArrayList<>();
        final Error error = getError(supplyTypeRequest);
        errorResponse.setError(error);
        if (!errorResponse.getErrorFields().isEmpty())
            errorResponseList.add(errorResponse);

        return errorResponseList;
    }

}
