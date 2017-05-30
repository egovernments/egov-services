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
import org.egov.wcms.model.PropertyUsageType;
import org.egov.wcms.service.PropertyUsageTypeService;
import org.egov.wcms.util.WcmsConstants;
import org.egov.wcms.web.contract.PropertyUsageTypeGetRequest;
import org.egov.wcms.web.contract.PropertyUsageTypeRequest;
import org.egov.wcms.web.contract.PropertyUsageTypeResponse;
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
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/propertyUsageType")
public class PropertyTypeUsageTypeController {
	
	private static final Logger logger = LoggerFactory.getLogger(PropertyTypeUsageTypeController.class);
	
	@Autowired
    private PropertyUsageTypeService propUsageTypeService;
	
	@Autowired
    private ErrorHandler errHandler;

    @Autowired
    private ResponseInfoFactory responseInfoFactory;

    @Autowired
    private ApplicationProperties applicationProperties;
    
    @PostMapping(value = "/_create")
    @ResponseBody
    public ResponseEntity<?> create(@RequestBody @Valid  final PropertyUsageTypeRequest propUsageTypeRequest,
                                    final BindingResult errors) {
        if (errors.hasErrors()) {
            final ErrorResponse errRes = populateErrors(errors);
            return new ResponseEntity<ErrorResponse>(errRes, HttpStatus.BAD_REQUEST);
        }
        logger.info("Property Usage Type Request::" + propUsageTypeRequest);

        final List<ErrorResponse> errorResponses = validateUsageTypeRequest(propUsageTypeRequest);
        if (!errorResponses.isEmpty())
            return new ResponseEntity<List<ErrorResponse>>(errorResponses, HttpStatus.BAD_REQUEST);

        final PropertyUsageType propUsageType = propUsageTypeService.createPropertyUsageType(applicationProperties.getCreatePropertyUsageTopicName(),"propertyusage-create", propUsageTypeRequest);
        List<PropertyUsageType> propUsageTypes = new ArrayList<>();
        propUsageTypes.add(propUsageType);
        return getSuccessResponse(propUsageTypes, propUsageTypeRequest.getRequestInfo());
    }
    
    @PostMapping(value = "/_search")
    @ResponseBody
    public ResponseEntity<?> search(@ModelAttribute @Valid PropertyUsageTypeGetRequest propUsageTypeRequest,
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
        List<PropertyUsageType> propUsageTypes = null;
        try {
        	propUsageTypes = propUsageTypeService.getPropertyUsageTypes(propUsageTypeRequest);
        } catch (Exception exception) {
            logger.error("Error while processing request " + propUsageTypeRequest, exception);
            return errHandler.getResponseEntityForUnexpectedErrors(requestInfo);
        }

      return getSuccessResponse(propUsageTypes, requestInfo);

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
    
    private List<ErrorResponse> validateUsageTypeRequest(final PropertyUsageTypeRequest propUsageTypeRequest) {
        final List<ErrorResponse> errorResponses = new ArrayList<>();
        ErrorResponse errorResponse = new ErrorResponse();
        final Error error = getError(propUsageTypeRequest);
        errorResponse.setError(error);
        if(!errorResponse.getErrorFields().isEmpty())
            errorResponses.add(errorResponse);

        return errorResponses;
    }
    
    
	private Error getError(final PropertyUsageTypeRequest propUsageTypeRequest) {
    	PropertyUsageType propUsageType = propUsageTypeRequest.getPropertyUsageType();
    	List<ErrorField> errorFields = new ArrayList<>();
    	validatePropertyTypeValue(errorFields, propUsageType);
    	validateUsageTypeValue(errorFields, propUsageType);
    	validateTenantValue(errorFields, propUsageType);
        if((errorFields.size() <= 0) && !(propUsageType.getTenantId() == null || propUsageType.getTenantId().isEmpty())){
        	if(propUsageTypeService.checkPropertyUsageTypeExists(propUsageTypeRequest)){
        		final ErrorField errorField = ErrorField.builder()
                        .code(WcmsConstants.PROPERTYTYPE_USAGETYPE_UNIQUE_CODE)
                        .message(WcmsConstants.PROPERTYTYPE_USAGETYPE_UNQ_ERROR_MESSAGE)
                        .field(WcmsConstants.PROPERTYTYPE_USAGETYPE_UNQ_FIELD_NAME)
                        .build();
                errorFields.add(errorField);
        	}
        }
        return Error.builder().code(HttpStatus.BAD_REQUEST.value())
                .message(WcmsConstants.INVALID_PROPERTYUSAGETYPE_REQUEST_MESSAGE)
                .errorFields(errorFields)
                .build();
    }
	
	private List<ErrorField> validatePropertyTypeValue(List<ErrorField> errorFields, PropertyUsageType propUsageType){
		if (propUsageType.getPropertyType() == null || propUsageType.getPropertyType().isEmpty()){
            final ErrorField errorField = ErrorField.builder()
                    .code(WcmsConstants.PROPERTYTYPE_MANDATORY_CODE)
                    .message(WcmsConstants.PROPERTYTYPE_MANDATORY_ERROR_MESSAGE)
                    .field(WcmsConstants.PROPERTYTYPE_MANDATORY_FIELD_NAME)
                    .build();
            errorFields.add(errorField);
    	}
		return errorFields;
	}
	
	private List<ErrorField> validateUsageTypeValue(List<ErrorField> errorFields, PropertyUsageType propUsageType) {
		if (propUsageType.getUsageType() == null || propUsageType.getUsageType().isEmpty()){
        	final ErrorField errorField = ErrorField.builder()
                    .code(WcmsConstants.USAGETYPE_NAME_MANDATORY_CODE)
                    .message(WcmsConstants.USAGETYPE_NAME_MANADATORY_ERROR_MESSAGE)
                    .field(WcmsConstants.USAGETYPE_NAME_MANADATORY_FIELD_NAME)
                    .build();
            errorFields.add(errorField);
        }
		return errorFields;
	}
	
	private List<ErrorField> validateTenantValue(List<ErrorField> errorFields, PropertyUsageType propUsageType) { 
		if(propUsageType.getTenantId() == null || propUsageType.getTenantId().isEmpty()){
			final ErrorField errorField = ErrorField.builder()
					.code(WcmsConstants.TEANANTID_MANDATORY_CODE)
                    .message(WcmsConstants.TEANANTID_MANADATORY_ERROR_MESSAGE)
                    .field(WcmsConstants.TEANANTID_MANADATORY_FIELD_NAME)
                    .build();
			errorFields.add(errorField);
		}
		return errorFields;
	}
	
	
	private ResponseEntity<?> getSuccessResponse(List<PropertyUsageType> propertyUsageTypes, RequestInfo requestInfo) {
        PropertyUsageTypeResponse propUsageTypeResponse = new PropertyUsageTypeResponse();
        propUsageTypeResponse.setPropertyUsageTypes(propertyUsageTypes);
        ResponseInfo responseInfo = responseInfoFactory.createResponseInfoFromRequestInfo(requestInfo, true);
        responseInfo.setStatus(HttpStatus.OK.toString());
        propUsageTypeResponse.setResponseInfo(responseInfo);
        return new ResponseEntity<PropertyUsageTypeResponse>(propUsageTypeResponse, HttpStatus.OK);

    }
    
}