package org.egov.inv.util;


import org.egov.inv.errorhandlers.ErrorResponse;
import org.egov.inv.web.contract.StoreRequest;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.egov.common.contract.response.ErrorField;
import org.egov.inv.constants.InvConstants;
import org.egov.inv.domain.model.Store;
import org.egov.inv.errorhandlers.Error;

@Service
public class ValidatorUtils {
    
    
    
    public ErrorResponse populateErrors(final BindingResult errors) {
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

    public List<ErrorResponse> validateStoresRequest(StoreRequest storeRequest,String tenantId) {
        final ErrorResponse errorResponse = new ErrorResponse();
        final List<ErrorResponse> errorResponseList = new ArrayList<>();
        final Error error = getError(storeRequest,tenantId);
        errorResponse.setError(error);
        if (!errorResponse.getErrorFields().isEmpty())
            errorResponseList.add(errorResponse);
        return errorResponseList;  
    }
    

    private Error getError(final StoreRequest storeRequest,String tenantId) {
        final List<ErrorField> errorFiled = getErrorFields(storeRequest, tenantId);
        return Error.builder().code(HttpStatus.BAD_REQUEST.value())
                .message(InvConstants.INVALID_STORES_REQUEST_MESSAGE).errorFields(errorFiled).build();
    }

    private List<ErrorField> getErrorFields(final StoreRequest storeRequest,String tenantId) {
        final List<ErrorField> errorFields = new ArrayList<>();
        for (final Store store : storeRequest.getStores())
            addStoresValidationErrors(store, errorFields,tenantId);
        return errorFields;
    }

    private void addStoresValidationErrors(final Store store, final List<ErrorField> errorFields,String tenantId) {
        
            if (StringUtils.isBlank(store.getCode())) {
                final ErrorField errorField = ErrorField.builder().code(InvConstants.STORE_CODE_MANDATORY_CODE)
                        .message(InvConstants.STORE_CODE_MANDATORY_ERROR_MESSAGE)
                        .field(InvConstants.STORE_CODE_MANDATORY_FIELD_NAME).build();
                errorFields.add(errorField);
            }
            else if (StringUtils.isBlank(tenantId)) {
            final ErrorField errorField = ErrorField.builder().code(InvConstants.TENANTID_MANDATORY_CODE)
                    .message(InvConstants.TENANTID_MANADATORY_ERROR_MESSAGE)
                    .field(InvConstants.TENANTID_MANADATORY_FIELD_NAME).build();
            errorFields.add(errorField);
        } else if (StringUtils.isBlank(store.getName())) {
            final ErrorField errorField = ErrorField.builder().code(InvConstants.STORE_NAME_MANDATORY_CODE)
                    .message(InvConstants.STORE_NAME_MANADATORY_ERROR_MESSAGE)
                    .field(InvConstants.STORE_NAME_MANADATORY_FIELD_NAME).build();
            errorFields.add(errorField);
        } else if (StringUtils.isBlank(store.getDescription())) {
            final ErrorField errorField = ErrorField.builder().code(InvConstants.STORE_DESCRIPTION_MANDATORY_CODE)
                    .message(InvConstants.STORE_DESCRIPTION_MANADATORY_ERROR_MESSAGE)
                    .field(InvConstants.STORE_DESCRIPTION_MANADATORY_FIELD_NAME).build();
            errorFields.add(errorField);
        }

        else if (StringUtils.isBlank(store.getBillingAddress())) {
            final ErrorField errorField = ErrorField.builder().code(InvConstants.STORE_BILLINGADDRESS_MANDATORY_CODE)
                    .message(InvConstants.STORE_BILLINGADDRESS_MANADATORY_ERROR_MESSAGE)
                    .field(InvConstants.STORE_BILLINGADDRESS_MANADATORY_FIELD_NAME).build();
            errorFields.add(errorField);
        } else if (StringUtils.isBlank(store.getDeliveryAddress())) {
            final ErrorField errorField = ErrorField.builder().code(InvConstants.STORE_DELIVERYADDRESS_MANDATORY_CODE)
                    .message(InvConstants.STORE_DELIVERYADDRESS_MANADATORY_ERROR_MESSAGE)
                    .field(InvConstants.STORE_DELIVERYADDRESS_MANADATORY_FIELD_NAME).build();
            errorFields.add(errorField);
        } else if (StringUtils.isBlank(store.getContactNo1())) {
            final ErrorField errorField = ErrorField.builder().code(InvConstants.STORE_CONTACTNO1_MANDATORY_CODE)
                    .message(InvConstants.STORE_CONTACTNO1_MANADATORY_ERROR_MESSAGE)
                    .field(InvConstants.STORE_CONTACTNO1_MANADATORY_FIELD_NAME).build();
            errorFields.add(errorField);
        } else if (StringUtils.isBlank(store.getEmail())){
            final ErrorField errorField = ErrorField.builder().code(InvConstants.STORE_EMAIL_MANDATORY_CODE)
                    .message(InvConstants.STORE_EMAIL_MANADATORY_ERROR_MESSAGE)
                    .field(InvConstants.STORE_EMAIL_MANADATORY_FIELD_NAME).build();
            errorFields.add(errorField);
        } else
            return;

    }

}
