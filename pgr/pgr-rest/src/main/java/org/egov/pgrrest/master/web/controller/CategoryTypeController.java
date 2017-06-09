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

package org.egov.pgrrest.master.web.controller;

import java.util.ArrayList;
import java.util.List;

import javax.validation.Valid;

//import org.apache.log4j.spi.ErrorHandler;
import org.egov.common.contract.request.RequestInfo;
import org.egov.common.contract.response.ResponseInfo;
import org.egov.pgrrest.master.model.CategoryType;
import org.egov.pgrrest.master.service.CategoryTypeService;
import org.egov.pgrrest.master.util.PgrMasterConstants;
import org.egov.pgrrest.master.web.contract.CategoryTypeRequest;
import org.egov.pgrrest.master.web.contract.CategoryTypeResponse;
import org.egov.pgrrest.master.web.contract.factory.ResponseInfoFactory;
import org.egov.pgrrest.read.web.contract.ErrorResponse;
import org.egov.pgrrest.read.web.contract.Error;
import org.egov.pgrrest.read.web.contract.ErrorField;
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
@RequestMapping("/categorytype")
public class CategoryTypeController {

    private static final Logger logger = LoggerFactory.getLogger(CategoryTypeController.class);

    @Autowired
    private CategoryTypeService categoryService;

 /*   @Autowired
    private ErrorHandler errHandler; */

    @Autowired
    private ResponseInfoFactory responseInfoFactory;
    
    
    @PostMapping(value = "/_create")
    @ResponseBody
    public ResponseEntity<?> create(@RequestBody @Valid final CategoryTypeRequest categoryRequest,
            final BindingResult errors) {
        if (errors.hasErrors()) {
            final ErrorResponse errRes = populateErrors(errors);
            return new ResponseEntity<>(errRes, HttpStatus.BAD_REQUEST);
        }
        logger.info("categoryRequest::" + categoryRequest);

        final List<ErrorResponse> errorResponses = validateCategoryRequest(categoryRequest);
        if (!errorResponses.isEmpty())
            return new ResponseEntity<>(errorResponses, HttpStatus.BAD_REQUEST);

        final CategoryType category = categoryService.createCategory(categoryRequest);
        final List<CategoryType> categories = new ArrayList<>();
        categories.add(category);
        return getSuccessResponse(categories, categoryRequest.getRequestInfo());

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

    private List<ErrorResponse> validateCategoryRequest(final CategoryTypeRequest categoryRequest) {
        final List<ErrorResponse> errorResponses = new ArrayList<>();
        final ErrorResponse errorResponse = new ErrorResponse();
        final Error error = getError(categoryRequest);
        errorResponse.setError(error);
        if (!errorResponse.getErrorFields().isEmpty())
            errorResponses.add(errorResponse);
        return errorResponses;
    }

    private Error getError(final CategoryTypeRequest categoryRequest) {
        categoryRequest.getCategory();
        final List<ErrorField> errorFields = getErrorFields(categoryRequest);
        return Error.builder().code(HttpStatus.BAD_REQUEST.value())
                .message(PgrMasterConstants.INVALID_CATEGORY_REQUEST_MESSAGE)
                .fields(errorFields)
                .build();
    }

    private List<ErrorField> getErrorFields(final CategoryTypeRequest categoryRequest) {
        final List<ErrorField> errorFields = new ArrayList<>();
        addCategoryNameValidationErrors(categoryRequest, errorFields);
        addTeanantIdValidationErrors(categoryRequest, errorFields);
        return errorFields;
    }

    private void addCategoryNameValidationErrors(final CategoryTypeRequest categoryRequest,
            final List<ErrorField> errorFields) {
        final CategoryType category = categoryRequest.getCategory();
        if (category.getName() == null || category.getName().isEmpty()) {
            final ErrorField errorField = ErrorField.builder()
                    .code(PgrMasterConstants.CATEGORY_NAME_MANDATORY_CODE)
                    .message(PgrMasterConstants.CATEGORY_NAME_MANADATORY_ERROR_MESSAGE)
                    .field(PgrMasterConstants.CATEGORY_NAME_MANADATORY_FIELD_NAME)
                    .build();
            errorFields.add(errorField);
        } else
            return;
    }

    private void addTeanantIdValidationErrors(final CategoryTypeRequest categoryRequest,
            final List<ErrorField> errorFields) {
        final CategoryType category = categoryRequest.getCategory();
        if (category.getTenantId() == null || category.getTenantId().isEmpty()) {
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
            for (final FieldError fieldError : errors.getFieldErrors()){
            	ErrorField errorfield = new ErrorField();
            	errorfield.setCode(fieldError.getCode());
            	errorfield.setField(fieldError.getField());
            	errorfield.setMessage(fieldError.getDefaultMessage());
                error.getFields().add(errorfield);
            }
        errRes.setError(error);
        return errRes;
    }

    private ResponseEntity<?> getSuccessResponse(final List<CategoryType> categoryList, final RequestInfo requestInfo) {
        final CategoryTypeResponse categoryResponse = new CategoryTypeResponse();
        categoryResponse.setCategories(categoryList);
        final ResponseInfo responseInfo = responseInfoFactory.createResponseInfoFromRequestInfo(requestInfo, true);
        responseInfo.setStatus(HttpStatus.OK.toString());
        categoryResponse.setResponseInfo(responseInfo);
        return new ResponseEntity<>(categoryResponse, HttpStatus.OK);

    }

}
