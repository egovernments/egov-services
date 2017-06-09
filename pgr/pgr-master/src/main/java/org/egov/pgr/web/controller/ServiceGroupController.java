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

package org.egov.pgr.web.controller;

import java.util.ArrayList;
import java.util.List;

import javax.validation.Valid;

//import org.apache.log4j.spi.ErrorHandler;
import org.egov.common.contract.request.RequestInfo;
import org.egov.common.contract.response.ErrorField;
import org.egov.common.contract.response.ResponseInfo;
import org.egov.pgr.config.ApplicationProperties;
import org.egov.pgr.model.ServiceGroup;
import org.egov.pgr.service.ServiceGroupService;
import org.egov.pgr.util.PgrMasterConstants;
import org.egov.pgr.web.contract.ServiceGroupRequest;
import org.egov.pgr.web.contract.ServiceGroupResponse;
import org.egov.pgr.web.contract.factory.ResponseInfoFactory;
import org.egov.pgr.web.errorhandlers.ErrorResponse;
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
import org.egov.pgr.web.errorhandlers.Error;

@RestController
@RequestMapping("/serviceGroup")
public class ServiceGroupController {

    private static final Logger logger = LoggerFactory.getLogger(ServiceGroupController.class);

    @Autowired
    private ServiceGroupService categoryService;

 /*   @Autowired
    private ErrorHandler errHandler; */

    @Autowired
    private ResponseInfoFactory responseInfoFactory;
    
    @Autowired
    private ApplicationProperties applicationProperties;
    
    
    @PostMapping(value = "/_create")
    @ResponseBody
    public ResponseEntity<?> create(@RequestBody @Valid final ServiceGroupRequest serviceGroupRequest,
            final BindingResult errors) {
        if (errors.hasErrors()) {
            final ErrorResponse errRes = populateErrors(errors);
            return new ResponseEntity<>(errRes, HttpStatus.BAD_REQUEST);
        }
        logger.info("serviceGroupRequest::" + serviceGroupRequest);

        final List<ErrorResponse> errorResponses = validateCategoryRequest(serviceGroupRequest);
        if (!errorResponses.isEmpty())
            return new ResponseEntity<>(errorResponses, HttpStatus.BAD_REQUEST);

        final ServiceGroup category = categoryService.createCategory(applicationProperties.getCreateServiceGroupTopicName(), 
        		applicationProperties.getCreateServiceGroupTopicKey(), serviceGroupRequest);
        final List<ServiceGroup> categories = new ArrayList<>();
        categories.add(category);
        return getSuccessResponse(categories, serviceGroupRequest.getRequestInfo());

    }

 /*   @PostMapping(value = "/_update/{code}")
    @ResponseBody
    public ResponseEntity<?> update(@RequestBody @Valid final CategoryTypeRequest categoryRequest,
            final BindingResult errors, @PathVariable("code") final String code) {
        if (errors.hasErrors()) {
            final ErrorResponse errRes = populateErrors(errors);
            return new ResponseEntity<>(errRes, HttpStatus.BAD_REQUEST);
        }
        logger.info("categoryRequest::" + categoryRequest);
        categoryRequest.getCategory().setCode(code);

        final List<ErrorResponse> errorResponses = validateCategoryRequest(categoryRequest);
        if (!errorResponses.isEmpty())
            return new ResponseEntity<>(errorResponses, HttpStatus.BAD_REQUEST);

        final CategoryType category = categoryService.updateCategory(applicationProperties.getUpdateCategoryTopicName(),
                "category-update", categoryRequest);
        final List<CategoryType> categories = new ArrayList<>();
        categories.add(category);
        return getSuccessResponse(categories, categoryRequest.getRequestInfo());
    }

    @PostMapping("_search")
    @ResponseBody
    public ResponseEntity<?> search(@ModelAttribute @Valid final CategoryTypeGetRequest categoryGetRequest,
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
        List<CategoryType> categoryList = null;
        try {
            categoryList = categoryService.getCategories(categoryGetRequest);
        } catch (final Exception exception) {
            logger.error("Error while processing request " + categoryGetRequest, exception);
            return errHandler.getResponseEntityForUnexpectedErrors(requestInfo);
        }

        return getSuccessResponse(categoryList, requestInfo);

    } */

    private List<ErrorResponse> validateCategoryRequest(final ServiceGroupRequest serviceGroupRequest) {
        final List<ErrorResponse> errorResponses = new ArrayList<>();
        final ErrorResponse errorResponse = new ErrorResponse();
        final Error error = getError(serviceGroupRequest);
        errorResponse.setError(error);
        if (!errorResponse.getErrorFields().isEmpty())
            errorResponses.add(errorResponse);
        return errorResponses;
    }

    private Error getError(final ServiceGroupRequest serviceGroupRequest) {
    	serviceGroupRequest.getServiceGroup();
        final List<ErrorField> errorFields = getErrorFields(serviceGroupRequest);
        return Error.builder().code(HttpStatus.BAD_REQUEST.value())
                .message(PgrMasterConstants.INVALID_SERVICEGROUP_REQUEST_MESSAGE)
                .errorFields(errorFields)
                .build();
    }

    private List<ErrorField> getErrorFields(final ServiceGroupRequest serviceGroupRequest) {
        final List<ErrorField> errorFields = new ArrayList<>();
        addCategoryNameValidationErrors(serviceGroupRequest, errorFields);
        addTeanantIdValidationErrors(serviceGroupRequest, errorFields);
        return errorFields;
    }

    private void addCategoryNameValidationErrors(final ServiceGroupRequest serviceGroupRequest,
            final List<ErrorField> errorFields) {
        final ServiceGroup serviceGroup = serviceGroupRequest.getServiceGroup();
        if (serviceGroup.getName() == null || serviceGroup.getName().isEmpty()) {
            final ErrorField errorField = ErrorField.builder()
                    .code(PgrMasterConstants.SERVICEGROUP_NAME_MANDATORY_CODE)
                    .message(PgrMasterConstants.SERVICEGROUP_NAME_MANADATORY_ERROR_MESSAGE)
                    .field(PgrMasterConstants.SERVICEGROUP_NAME_MANADATORY_FIELD_NAME)
                    .build();
            errorFields.add(errorField);
        } 
    }

    private void addTeanantIdValidationErrors(final ServiceGroupRequest serviceGroupRequest,
            final List<ErrorField> errorFields) {
        final ServiceGroup serviceGroup = serviceGroupRequest.getServiceGroup();
        if (serviceGroup.getTenantId() == null || serviceGroup.getTenantId().isEmpty()) {
            final ErrorField errorField = ErrorField.builder()
                    .code(PgrMasterConstants.TENANTID_MANDATORY_CODE)
                    .message(PgrMasterConstants.TENANTID_MANADATORY_ERROR_MESSAGE)
                    .field(PgrMasterConstants.TENANTID_MANADATORY_FIELD_NAME)
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

    private ResponseEntity<?> getSuccessResponse(final List<ServiceGroup> categoryList, final RequestInfo requestInfo) {
        final ServiceGroupResponse categoryResponse = new ServiceGroupResponse();
        final ResponseInfo responseInfo = responseInfoFactory.createResponseInfoFromRequestInfo(requestInfo, true);
        responseInfo.setStatus(HttpStatus.OK.toString());
        categoryResponse.setResponseInfo(responseInfo);
        try {

        } catch (final Exception e) {
            logger.error("Exception Encountered : " + e);
        }
        return new ResponseEntity<>(categoryResponse, HttpStatus.OK);

    }


}
