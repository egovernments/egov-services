package org.egov.pgrrest.master.web.controller;

import java.util.ArrayList;
import java.util.List;

import javax.validation.Valid;

import org.egov.common.contract.request.RequestInfo;
import org.egov.common.contract.response.ResponseInfo;
import org.egov.pgrrest.master.model.ReceivingCenterType;
import org.egov.pgrrest.master.service.ReceivingCenterTypeService;
import org.egov.pgrrest.master.util.PgrMasterConstants;
import org.egov.pgrrest.master.web.contract.ReceivingCenterTypeReq;
import org.egov.pgrrest.master.web.contract.ReceivingCenterTypeRes;
import org.egov.pgrrest.master.web.contract.factory.ResponseInfoFactory;
import org.egov.pgrrest.master.web.errorhandlers.ErrorHandler;
import org.egov.pgrrest.read.web.contract.Error;
import org.egov.pgrrest.read.web.contract.ErrorField;
import org.egov.pgrrest.read.web.contract.ErrorResponse;
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
@RequestMapping("/receivingcenter")
public class ReceivingCenterTypeController {

	
	private static final Logger logger = LoggerFactory.getLogger(ReceivingCenterTypeController.class);

    @Autowired
    private ReceivingCenterTypeService receivingCenterService;

    @Autowired
    private ResponseInfoFactory responseInfoFactory;
    
    
    @PostMapping(value = "/_create")
    @ResponseBody
    public ResponseEntity<?> create(@RequestBody @Valid final ReceivingCenterTypeReq centerTypeRequest,
            final BindingResult errors) {
        if (errors.hasErrors()) {
            final ErrorResponse errRes = populateErrors(errors);
            return new ResponseEntity<>(errRes, HttpStatus.BAD_REQUEST);
        }
        logger.info("Receiving CenterType Request::" + centerTypeRequest);

        final List<ErrorResponse> errorResponses = validateReceivingCenterRequest(centerTypeRequest);
        if (!errorResponses.isEmpty())
            return new ResponseEntity<>(errorResponses, HttpStatus.BAD_REQUEST);

        final ReceivingCenterType ReceivingCenter = receivingCenterService.sendMessage(centerTypeRequest);
        final List<ReceivingCenterType> ReceivingCenters = new ArrayList<>();
        ReceivingCenters.add(ReceivingCenter);
        return getSuccessResponse(ReceivingCenters, centerTypeRequest.getRequestInfo());

    }
    
    
    private List<ErrorResponse> validateReceivingCenterRequest(final ReceivingCenterTypeReq receivingCenterRequest) {
        final List<ErrorResponse> errorResponses = new ArrayList<>();
        final ErrorResponse errorResponse = new ErrorResponse();
        final Error error = getError(receivingCenterRequest);
        errorResponse.setError(error);
        if (!errorResponse.getErrorFields().isEmpty())
            errorResponses.add(errorResponse);
        return errorResponses;
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
    
    private List<ErrorField> getErrorFields(final ReceivingCenterTypeReq categoryRequest) {
        final List<ErrorField> errorFields = new ArrayList<>();
        addReceivingCenterNameValidationErrors(categoryRequest, errorFields);
        addTeanantIdValidationErrors(categoryRequest, errorFields);
        return errorFields;
    }
    
    private void addReceivingCenterNameValidationErrors(final ReceivingCenterTypeReq receivingCenterRequest,
            final List<ErrorField> errorFields) {
        final ReceivingCenterType receivingCenter = receivingCenterRequest.getCenterType();
        if (receivingCenter.getName() == null || receivingCenter.getName().isEmpty()) {
            final ErrorField errorField = ErrorField.builder()
                    .code(PgrMasterConstants.CATEGORY_NAME_MANDATORY_CODE)
                    .message(PgrMasterConstants.CATEGORY_NAME_MANADATORY_ERROR_MESSAGE)
                    .field(PgrMasterConstants.CATEGORY_NAME_MANADATORY_FIELD_NAME)
                    .build();
            errorFields.add(errorField);
        } else
            return;
    }

    private void addTeanantIdValidationErrors(final ReceivingCenterTypeReq receivingCenterRequest,
            final List<ErrorField> errorFields) {
        final ReceivingCenterType receivingCenter = receivingCenterRequest.getCenterType();
        if (receivingCenter.getTenantId() == null || receivingCenter.getTenantId().isEmpty()) {
            final ErrorField errorField = ErrorField.builder()
                    .code(PgrMasterConstants.TENANTID_MANDATORY_CODE)
                    .message(PgrMasterConstants.TENANTID_MANADATORY_ERROR_MESSAGE)
                    .field(PgrMasterConstants.TENANTID_MANADATORY_FIELD_NAME)
                    .build();
            errorFields.add(errorField);
        } else
            return;
    }
    
    
    private Error getError(final ReceivingCenterTypeReq centerTypeRequest) {
    	centerTypeRequest.getCenterType();
        final List<ErrorField> errorFields = getErrorFields(centerTypeRequest);
        return Error.builder().code(HttpStatus.BAD_REQUEST.value())
                .message(PgrMasterConstants.INVALID_RECEIVING_CENTERTYPE_REQUEST_MESSAGE)
                .fields(errorFields)
                .build();
    }
    
    private List<ErrorResponse> validateCategoryRequest(final ReceivingCenterTypeReq centerTypeRequest) {
        final List<ErrorResponse> errorResponses = new ArrayList<>();
        final ErrorResponse errorResponse = new ErrorResponse();
        final Error error = getError(centerTypeRequest);
        errorResponse.setError(error);
        if (!errorResponse.getErrorFields().isEmpty())
            errorResponses.add(errorResponse);
        return errorResponses;
    }
    
    private ResponseEntity<?> getSuccessResponse(final List<ReceivingCenterType> centerList, final RequestInfo requestInfo) {
        final ReceivingCenterTypeRes receivingCenterResponse = new ReceivingCenterTypeRes();
        receivingCenterResponse.setCenterTypes(centerList);
        final ResponseInfo responseInfo = responseInfoFactory.createResponseInfoFromRequestInfo(requestInfo, true);
        responseInfo.setStatus(HttpStatus.OK.toString());
        receivingCenterResponse.setResponseInfo(responseInfo);
        return new ResponseEntity<>(receivingCenterResponse, HttpStatus.OK);

    }
 
}


