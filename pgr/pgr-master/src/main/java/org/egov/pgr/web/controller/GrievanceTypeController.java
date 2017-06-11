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

import org.egov.common.contract.request.RequestInfo;
import org.egov.common.contract.response.ErrorField;
import org.egov.common.contract.response.ResponseInfo;
import org.egov.pgr.config.ApplicationProperties;
import org.egov.pgr.model.ServiceType;
import org.egov.pgr.service.ServiceTypeService;
import org.egov.pgr.util.PgrMasterConstants;
import org.egov.pgr.web.contract.RequestInfoWrapper;
import org.egov.pgr.web.contract.ServiceGetRequest;
import org.egov.pgr.web.contract.ServiceRequest;
import org.egov.pgr.web.contract.ServiceResponse;
import org.egov.pgr.web.contract.factory.ResponseInfoFactory;
import org.egov.pgr.web.errorhandlers.Error;
import org.egov.pgr.web.errorhandlers.ErrorHandler;
import org.egov.pgr.web.errorhandlers.ErrorResponse;
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
@RequestMapping("/service")
public class GrievanceTypeController {

    private static final Logger logger = LoggerFactory.getLogger(GrievanceTypeController.class);

    @Autowired
    private ServiceTypeService serviceTypeService;

    @Autowired
    private ErrorHandler errHandler;

    @Autowired
    private ResponseInfoFactory responseInfoFactory;
    
    @Autowired
	private ApplicationProperties applicationProperties;

    @PostMapping(value = "/_create")
    @ResponseBody
    public ResponseEntity<?> create(@RequestBody @Valid final ServiceRequest serviceTypeRequest,
            final BindingResult errors) {
        if (errors.hasErrors()) {
            final ErrorResponse errRes = populateErrors(errors);
            return new ResponseEntity<>(errRes, HttpStatus.BAD_REQUEST);
        }
        logger.info("Grievance Type Request::" + serviceTypeRequest);

        final List<ErrorResponse> errorResponses = validateCategoryRequest(serviceTypeRequest);
        if (!errorResponses.isEmpty())
            return new ResponseEntity<>(errorResponses, HttpStatus.BAD_REQUEST);

        final ServiceType serviceType = serviceTypeService.createServiceType(applicationProperties.getCreateServiceTypeTopicName(),applicationProperties.getCreateServiceTypeTopicKey(), serviceTypeRequest);
        final List<ServiceType> serviceTypes = new ArrayList<>();
        serviceTypes.add(serviceType);
        return getSuccessResponse(serviceTypes, serviceTypeRequest.getRequestInfo());

    }

    @PostMapping(value = "/_update/{code}")
    @ResponseBody
    public ResponseEntity<?> update(@RequestBody @Valid final ServiceRequest serviceTypeRequest,
            final BindingResult errors, @PathVariable("code") final String code) {
        if (errors.hasErrors()) {
            final ErrorResponse errRes = populateErrors(errors);
            return new ResponseEntity<>(errRes, HttpStatus.BAD_REQUEST);
        }
        logger.info("Service Request::" + serviceTypeRequest);

        final List<ErrorResponse> errorResponses = validateCategoryRequest(serviceTypeRequest);
        if (!errorResponses.isEmpty())
            return new ResponseEntity<>(errorResponses, HttpStatus.BAD_REQUEST);

        final ServiceType service = serviceTypeService.updateServices("",
                "category-update", serviceTypeRequest);
        final List<ServiceType> services = new ArrayList<>();
        services.add(service);
        return getSuccessResponse(services, serviceTypeRequest.getRequestInfo());
    }

    @PostMapping("_search")
    @ResponseBody
    public ResponseEntity<?> search(@ModelAttribute @Valid final ServiceGetRequest serviceTypeGetRequest,
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
        List<ServiceType> serviceTypeList = null;
        try {
            serviceTypeList = serviceTypeService.getServiceTypes(serviceTypeGetRequest);
        } catch (final Exception exception) {
            logger.error("Error while processing request " + serviceTypeGetRequest, exception);
            return errHandler.getResponseEntityForUnexpectedErrors(requestInfo);
        }

        return getSuccessResponse(serviceTypeList, requestInfo);

    }

    private List<ErrorResponse> validateCategoryRequest(final ServiceRequest serviceTypeRequest) {
        final List<ErrorResponse> errorResponses = new ArrayList<>();
        final ErrorResponse errorResponse = new ErrorResponse();
        final Error error = getError(serviceTypeRequest);
        errorResponse.setError(error);
        if (!errorResponse.getErrorFields().isEmpty())
            errorResponses.add(errorResponse);
        return errorResponses;
    }

    private Error getError(final ServiceRequest serviceTypeRequest) {
        serviceTypeRequest.getService();
		final List<ErrorField> errorFields = getErrorFields(serviceTypeRequest);
		return Error.builder().code(HttpStatus.BAD_REQUEST.value())
				.message(PgrMasterConstants.INVALID_RECEIVING_CENTERTYPE_REQUEST_MESSAGE).errorFields(errorFields)
				.build();
    }

    private List<ErrorField> getErrorFields(final ServiceRequest serviceTypeRequest) {
        final List<ErrorField> errorFields = new ArrayList<>();
        addGrievanceNameValidationErrors(serviceTypeRequest, errorFields);
        addTeanantIdValidationErrors(serviceTypeRequest, errorFields);
        /*addActiveValidationErrors(serviceTypeRequest, errorFields);*/
        return errorFields;
    }

    private void addGrievanceNameValidationErrors(final ServiceRequest serviceTypeRequest,
            final List<ErrorField> errorFields) {
        final ServiceType service = serviceTypeRequest.getService();
        if (service.getServiceName() == null || service.getServiceName().isEmpty()) {
            final ErrorField errorField = ErrorField.builder()
                    .code(PgrMasterConstants.GRIEVANCETYPE_NAME_MANDATORY_CODE)
                    .message(PgrMasterConstants.GRIEVANCETYPE_NAME_MANADATORY_ERROR_MESSAGE)
                    .field(PgrMasterConstants.GRIEVANCETYPE_NAME_MANADATORY_FIELD_NAME)
                    .build();
            errorFields.add(errorField);
       /* } else if (!serviceTypeService.getCategoryByNameAndCode(service.getServiceCode(), service.getServiceName(), service.getTenantId())) {
            final ErrorField errorField = ErrorField.builder()
                    .code(PgrMasterConstants.GRIEVANCE_TYPE_MANDATORY_CODE)
                    .message(PgrMasterConstants.GRIEVANCE_TYPE_MANADATORY_ERROR_MESSAGE)
                    .field(PgrMasterConstants.GRIEVANCE_TYPE_MANADATORY_FIELD_NAME)
                    .build();
            errorFields.add(errorField);*/
        } else
            return;
    }

    private void addTeanantIdValidationErrors(final ServiceRequest serviceTypeRequest,
            final List<ErrorField> errorFields) {
        final ServiceType serviceType = serviceTypeRequest.getService();
        if (serviceType.getTenantId() == null || serviceType.getTenantId().isEmpty()) {
            final ErrorField errorField = ErrorField.builder()
                    .code(PgrMasterConstants.TENANTID_MANDATORY_CODE)
                    .message(PgrMasterConstants.TENANTID_MANADATORY_ERROR_MESSAGE)
                    .field(PgrMasterConstants.TENANTID_MANADATORY_FIELD_NAME)
                    .build();
            errorFields.add(errorField);
        } else
            return;
    }

    /*private void addActiveValidationErrors(final ServiceRequest serviceTypeRequest, final List<ErrorField> errorFields) {
        final ServiceType service = serviceTypeRequest.getService();
        if (service.getActive() == null) {
            final ErrorField errorField = ErrorField.builder()
                    .code(PgrMasterConstants.ACTIVE_MANDATORY_CODE)
                    .message(PgrMasterConstants.ACTIVE_MANADATORY_ERROR_MESSAGE)
                    .field(PgrMasterConstants.ACTIVE_MANADATORY_FIELD_NAME)
                    .build();
            errorFields.add(errorField);
        } else
            return;
    }*/

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

    private ResponseEntity<?> getSuccessResponse(final List<ServiceType> serviceTypeList, final RequestInfo requestInfo) {
        final ServiceResponse serviceTypeResponse = new ServiceResponse();
        serviceTypeResponse.setServices(serviceTypeList);
        final ResponseInfo responseInfo = responseInfoFactory.createResponseInfoFromRequestInfo(requestInfo, true);
        responseInfo.setStatus(HttpStatus.OK.toString());
        serviceTypeResponse.setResponseInfo(responseInfo);
        return new ResponseEntity<>(serviceTypeResponse, HttpStatus.OK);

    }

}
