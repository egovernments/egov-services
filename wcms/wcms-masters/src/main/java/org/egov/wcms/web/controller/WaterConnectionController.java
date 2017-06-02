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

import org.egov.common.contract.response.ErrorField;
import org.egov.wcms.config.ApplicationProperties;
import org.egov.wcms.util.WcmsConstants;
import org.egov.wcms.web.contract.WaterConnectionReq;
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
@RequestMapping("/connection")
public class WaterConnectionController {
	
    private static final Logger logger = LoggerFactory.getLogger(WaterConnectionController.class);
    
    @Autowired
    private ErrorHandler errHandler;

    @Autowired
    private ResponseInfoFactory responseInfoFactory;

    @Autowired
    private ApplicationProperties applicationProperties;
    
    @PostMapping(value = "/_create")
    @ResponseBody
    public ResponseEntity<?> create(@RequestBody @Valid  final WaterConnectionReq waterConnectionRequest,
                                    final BindingResult errors) {
        if (errors.hasErrors()) {
            final ErrorResponse errRes = populateErrors(errors);
            return new ResponseEntity<ErrorResponse>(errRes, HttpStatus.BAD_REQUEST);
        }
        logger.info("WaterConnectionRequest::" + waterConnectionRequest);
        
        final List<ErrorResponse> errorResponses = validatePropertyCategoryRequest(waterConnectionRequest);
        if (!errorResponses.isEmpty()){
            return new ResponseEntity<List<ErrorResponse>>(errorResponses, HttpStatus.BAD_REQUEST);
        }
        
        
        
        
        
        //Call to service.
        
        
     
        
        return null;
        
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
    
    private List<ErrorResponse> validatePropertyCategoryRequest(final WaterConnectionReq waterConnectionRequest) {
        final List<ErrorResponse> errorResponses = new ArrayList<>();
        ErrorResponse errorResponse = new ErrorResponse();
        final Error error = getError(waterConnectionRequest);
        errorResponse.setError(error);
        if(!errorResponse.getErrorFields().isEmpty())
            errorResponses.add(errorResponse);
        return errorResponses;
    }
    
    private Error getError(final WaterConnectionReq waterConnectionRequest) {
        List<ErrorField> errorFields = new ArrayList<>();
        if (waterConnectionRequest.getConnection().getBillingType() == null || 
        		waterConnectionRequest.getConnection().getBillingType().isEmpty()) {
            final ErrorField errorField = ErrorField.builder()
                    .code(WcmsConstants.BILLING_TYPE_INVALID_CODE)
                    .message(WcmsConstants.BILLING_TYPE_INVALID_ERROR_MESSAGE)
                    .field(WcmsConstants.BILLING_TYPE_INVALID_FIELD_NAME)
                    .build();
            errorFields.add(errorField);
        } else if (waterConnectionRequest.getConnection().getCategoryType() == null || 
        		waterConnectionRequest.getConnection().getCategoryType().isEmpty()) {
            final ErrorField errorField = ErrorField.builder()
                    .code(WcmsConstants.CATEGORY_NAME_MANDATORY_CODE)
                    .message(WcmsConstants.CATEGORY_NAME_MANADATORY_ERROR_MESSAGE)
                    .field(WcmsConstants.CATEGORY_NAME_MANADATORY_FIELD_NAME)
                    .build();
            errorFields.add(errorField);
        } else if (waterConnectionRequest.getConnection().getConnectionType() == null || 
        		waterConnectionRequest.getConnection().getConnectionType().isEmpty()) {
            final ErrorField errorField = ErrorField.builder()
                    .code(WcmsConstants.CONNECTION_TYPE_INVALID_CODE)
                    .message(WcmsConstants.CONNECTION_INVALID_ERROR_MESSAGE)
                    .field(WcmsConstants.CONNECTION_TYPE_INVALID_FIELD_NAME)
                    .build();
            errorFields.add(errorField);
        } else if (waterConnectionRequest.getConnection().getDocuments() == null || 
        		waterConnectionRequest.getConnection().getDocuments().isEmpty()) {
            final ErrorField errorField = ErrorField.builder()
                    .code(WcmsConstants.DOCUMENTS_INVALID_CODE)
                    .message(WcmsConstants.DOCUMENTS_INVALID_ERROR_MESSAGE)
                    .field(WcmsConstants.DOCUMENTS_INVALID_FIELD_NAME)
                    .build();
            errorFields.add(errorField);
        } else if (waterConnectionRequest.getConnection().getHscPipeSizeType() == null || 
        		waterConnectionRequest.getConnection().getHscPipeSizeType().isEmpty()) {
            final ErrorField errorField = ErrorField.builder()
                    .code(WcmsConstants.PIPESIZE_SIZEINMM_MANDATORY_CODE)
                    .message(WcmsConstants.PIPESIZE_SIZEINMM__MANADATORY_ERROR_MESSAGE)
                    .field(WcmsConstants.PIPESIZE_SIZEINMM__MANADATORY_FIELD_NAME)
                    .build();
            errorFields.add(errorField);
        } else if (waterConnectionRequest.getConnection().getProperty() == null || 
        		waterConnectionRequest.getConnection().getProperty().getPropertyType().isEmpty()) {
            final ErrorField errorField = ErrorField.builder()
                    .code(WcmsConstants.PROPERTY_TYPE_MANDATORY_CODE)
                    .message(WcmsConstants.PROPERTY_TYPE_MANDATORY_ERROR_MESSAGE)
                    .field(WcmsConstants.PROPERTY_TYPE_MANDATORY_FIELD_NAME)
                    .build();
            errorFields.add(errorField);
        } else if (waterConnectionRequest.getConnection().getProperty() == null || 
        		waterConnectionRequest.getConnection().getProperty().getUsageType().isEmpty()) {
            final ErrorField errorField = ErrorField.builder()
                    .code(WcmsConstants.USAGETYPE_NAME_MANDATORY_CODE)
                    .message(WcmsConstants.USAGETYPE_NAME_MANADATORY_ERROR_MESSAGE)
                    .field(WcmsConstants.USAGETYPE_NAME_MANADATORY_FIELD_NAME)
                    .build();
            errorFields.add(errorField);
        } else if (waterConnectionRequest.getConnection().getSourceType() == null || 
        		waterConnectionRequest.getConnection().getSourceType().isEmpty()) {
            final ErrorField errorField = ErrorField.builder()
                    .code(WcmsConstants.SOURCE_TYPE_INVALID_CODE)
                    .message(WcmsConstants.SOURCE_TYPE_INVALID_ERROR_MESSAGE)
                    .field(WcmsConstants.SOURCE_TYPE_INVALID_FIELD_NAME)
                    .build();
            errorFields.add(errorField);
        } else if (waterConnectionRequest.getConnection().getSumpCapacity() == 0L) {
            final ErrorField errorField = ErrorField.builder()
                    .code(WcmsConstants.SUMP_CAPACITY_INVALID_CODE)
                    .message(WcmsConstants.SUMP_CAPACITY_INVALID_ERROR_MESSAGE)
                    .field(WcmsConstants.SUMP_CAPACITY_INVALID_FIELD_NAME)
                    .build();
            errorFields.add(errorField);
        } else if (waterConnectionRequest.getConnection().getSupplyType() == null || 
        		waterConnectionRequest.getConnection().getSupplyType().isEmpty()) {
            final ErrorField errorField = ErrorField.builder()
                    .code(WcmsConstants.SUPPLY_TYPE_INVALID_CODE)
                    .message(WcmsConstants.SUPPLY_TYPE_INVALID_ERROR_MESSAGE)
                    .field(WcmsConstants.SUPPLY_TYPE_INVALID_FIELD_NAME)
                    .build();
            errorFields.add(errorField);
        }
       
        return Error.builder().code(HttpStatus.BAD_REQUEST.value())
                .message(WcmsConstants.INVALID_CATEGORY_REQUEST_MESSAGE)
                .errorFields(errorFields)
                .build();
    }


}
