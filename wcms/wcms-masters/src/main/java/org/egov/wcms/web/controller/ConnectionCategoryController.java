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
import org.egov.wcms.model.ConnectionCategory;
import org.egov.wcms.service.ConnectionCategoryService;
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
@RequestMapping("/category")
public class ConnectionCategoryController {

    private static final Logger logger = LoggerFactory.getLogger(ConnectionCategoryController.class);

   @Autowired
    private ConnectionCategoryService categoryService;

    @Autowired
    private ErrorHandler errHandler;

    @Autowired
    private ResponseInfoFactory responseInfoFactory;

    @Autowired
    private ApplicationProperties applicationProperties;


   @PostMapping(value = "/_create")
    @ResponseBody
    public ResponseEntity<?> create(@RequestBody @Valid  final ConnectionCategoryRequest categoryRequest,
                                    final BindingResult errors) {
        if (errors.hasErrors()) {
            final ErrorResponse errRes = populateErrors(errors);
            return new ResponseEntity<ErrorResponse>(errRes, HttpStatus.BAD_REQUEST);
        }
        logger.info("categoryRequest::" + categoryRequest);

        final List<ErrorResponse> errorResponses = validateCategoryRequest(categoryRequest);
        if (!errorResponses.isEmpty())
            return new ResponseEntity<List<ErrorResponse>>(errorResponses, HttpStatus.BAD_REQUEST);

        final ConnectionCategory category = categoryService.createCategory(applicationProperties.getCreateCategoryTopicName(),"category-create", categoryRequest);
        List<ConnectionCategory> categories = new ArrayList<>();
        categories.add(category);
        return getSuccessResponse(categories, categoryRequest.getRequestInfo());

    }

    @PostMapping(value = "/_update/{code}")
    @ResponseBody
    public ResponseEntity<?> update(@RequestBody @Valid final ConnectionCategoryRequest categoryRequest, final BindingResult errors, @PathVariable("code") String code) {
        if (errors.hasErrors()) {
            final ErrorResponse errRes = populateErrors(errors);
            return new ResponseEntity<ErrorResponse>(errRes, HttpStatus.BAD_REQUEST);
        }
        logger.info("categoryRequest::" + categoryRequest);
        categoryRequest.getCategory().setCode(code);

        final List<ErrorResponse> errorResponses = validateCategoryRequest(categoryRequest);
        if (!errorResponses.isEmpty())
            return new ResponseEntity<List<ErrorResponse>>(errorResponses, HttpStatus.BAD_REQUEST);

        final ConnectionCategory category = categoryService.updateCategory(applicationProperties.getUpdateCategoryTopicName(),"category-update",categoryRequest);
        List<ConnectionCategory> categories = new ArrayList<>();
        categories.add(category);
        return getSuccessResponse(categories, categoryRequest.getRequestInfo());
    }


   @PostMapping("_search")
    @ResponseBody
    public ResponseEntity<?> search(@ModelAttribute @Valid CategoryGetRequest categoryGetRequest,
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
        List<ConnectionCategory> categoryList = null;
        try {
            categoryList = categoryService.getCategories(categoryGetRequest);
        } catch (Exception exception) {
            logger.error("Error while processing request " + categoryGetRequest, exception);
            return errHandler.getResponseEntityForUnexpectedErrors(requestInfo);
        }

        return getSuccessResponse(categoryList, requestInfo);

    }

   private List<ErrorResponse> validateCategoryRequest(final ConnectionCategoryRequest categoryRequest) {
        final List<ErrorResponse> errorResponses = new ArrayList<>();
        ErrorResponse errorResponse = new ErrorResponse();
        final Error error = getError(categoryRequest);
        errorResponse.setError(error);
        if(!errorResponse.getErrorFields().isEmpty())
            errorResponses.add(errorResponse);
        return errorResponses;
    }


   private Error getError(final ConnectionCategoryRequest categoryRequest) {
        ConnectionCategory category = categoryRequest.getCategory();
        List<ErrorField> errorFields = getErrorFields(categoryRequest);
        return Error.builder().code(HttpStatus.BAD_REQUEST.value())
                .message(WcmsConstants.INVALID_CATEGORY_REQUEST_MESSAGE)
                .errorFields(errorFields)
                .build();
    }

    private List<ErrorField> getErrorFields(final ConnectionCategoryRequest categoryRequest) {
        List<ErrorField> errorFields = new ArrayList<>();
        addCategoryNameValidationErrors(categoryRequest, errorFields);
        addTeanantIdValidationErrors(categoryRequest,errorFields);
        addActiveValidationErrors(categoryRequest,errorFields);
        return errorFields;
    }

    private void addCategoryNameValidationErrors(ConnectionCategoryRequest categoryRequest, List<ErrorField> errorFields) {
        ConnectionCategory category=categoryRequest.getCategory();
        if (category.getName() == null || category.getName().isEmpty()) {
            final ErrorField errorField = ErrorField.builder()
                    .code(WcmsConstants.CATEGORY_NAME_MANDATORY_CODE)
                    .message(WcmsConstants.CATEGORY_NAME_MANADATORY_ERROR_MESSAGE)
                    .field(WcmsConstants.CATEGORY_NAME_MANADATORY_FIELD_NAME)
                    .build();
            errorFields.add(errorField);
        } else if (!categoryService.getCategoryByNameAndCode(category.getCode(),category.getName(),category.getTenantId())) {
            final ErrorField errorField = ErrorField.builder()
                    .code(WcmsConstants.CATEGORY_NAME_UNIQUE_CODE)
                    .message(WcmsConstants.CATEGORY_UNQ_ERROR_MESSAGE)
                    .field(WcmsConstants.CATEGORY_NAME_FIELD_NAME)
                    .build();
            errorFields.add(errorField);
        } else return;
    }

    private void addTeanantIdValidationErrors(ConnectionCategoryRequest categoryRequest, List<ErrorField> errorFields){
        ConnectionCategory category=categoryRequest.getCategory();
        if(category.getTenantId()==null || category.getTenantId().isEmpty()){
            final ErrorField errorField = ErrorField.builder()
                    .code(WcmsConstants.TEANANTID_MANDATORY_CODE)
                    .message(WcmsConstants.TEANANTID_MANADATORY_ERROR_MESSAGE)
                    .field(WcmsConstants.TEANANTID_MANADATORY_FIELD_NAME)
                    .build();
            errorFields.add(errorField);
        } else return;
    }

    private void addActiveValidationErrors(ConnectionCategoryRequest categoryRequest, List<ErrorField> errorFields){
        ConnectionCategory category=categoryRequest.getCategory();
        if(category.getActive()==null){
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

    private ResponseEntity<?> getSuccessResponse(List<ConnectionCategory> categoryList, RequestInfo requestInfo) {
        ConnectionCategoryResponse categoryResponse = new ConnectionCategoryResponse();
        categoryResponse.setCategories(categoryList);
        ResponseInfo responseInfo = responseInfoFactory.createResponseInfoFromRequestInfo(requestInfo, true);
        responseInfo.setStatus(HttpStatus.OK.toString());
        categoryResponse.setResponseInfo(responseInfo);
        return new ResponseEntity<ConnectionCategoryResponse>(categoryResponse, HttpStatus.OK);

    }


}
