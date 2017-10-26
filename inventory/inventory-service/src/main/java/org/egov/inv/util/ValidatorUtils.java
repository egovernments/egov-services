package org.egov.inv.util;


import org.egov.inv.errorhandlers.ErrorResponse;
import org.egov.inv.web.contract.StoreRequest;
import org.springframework.beans.factory.annotation.Autowired;
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
import org.egov.inv.domain.service.StoreService;
import org.egov.inv.errorhandlers.Error;

@Service
public class ValidatorUtils {
    
    @Autowired
    private StoreService storeService;
    
    
    
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
        for (int i=0;i < storeRequest.getStores().size();i++){
            addStoresValidationErrors(storeRequest.getStores().get(i), errorFields,tenantId,i);
        }
        return errorFields;
    }

    private void addStoresValidationErrors(final Store store, final List<ErrorField> errorFields,String tenantId,int i) {
        
            if (StringUtils.isBlank(store.getCode())) {
                final ErrorField errorField = ErrorField.builder().code(InvConstants.STORE_CODE_MANDATORY_CODE)
                        .message(InvConstants.STORE_CODE_MANDATORY_ERROR_MESSAGE)
                        .field(InvConstants.STORE_CODE_MANDATORY_FIELD_NAME).build();
                String errorMessage = errorField.getMessage();
                errorMessage = errorMessage + " for store[" + i + "]";
                errorField.setMessage(errorMessage);
                errorFields.add(errorField);
            }
            else if(storeService.checkStoreCodeExists(store.getCode(),tenantId))
            {
                final ErrorField errorField = ErrorField.builder().code(InvConstants.STORE_CODE_UNIQUE_CODE)
                        .message(InvConstants.STORE_CODE_UNIQUE_ERROR_MESSAGE)
                        .field(InvConstants.STORE_CODE_UNIQUE_FIELD_NAME).build();
                String errorMessage = errorField.getMessage();
                errorMessage = errorMessage + " for store[" + i + "]";
                errorField.setMessage(errorMessage);
                errorFields.add(errorField); 
            }
           if (StringUtils.isBlank(tenantId)) {
            final ErrorField errorField = ErrorField.builder().code(InvConstants.TENANTID_MANDATORY_CODE)
                    .message(InvConstants.TENANTID_MANADATORY_ERROR_MESSAGE)
                    .field(InvConstants.TENANTID_MANADATORY_FIELD_NAME).build();
            String errorMessage = errorField.getMessage();
            errorMessage = errorMessage + " for store[" + i + "]";
            errorField.setMessage(errorMessage);
            errorFields.add(errorField);
        } 
           if (StringUtils.isBlank(store.getName())) {
            final ErrorField errorField = ErrorField.builder().code(InvConstants.STORE_NAME_MANDATORY_CODE)
                    .message(InvConstants.STORE_NAME_MANADATORY_ERROR_MESSAGE)
                    .field(InvConstants.STORE_NAME_MANADATORY_FIELD_NAME).build();
            String errorMessage = errorField.getMessage();
            errorMessage = errorMessage + " for store[" + i + "]";
            errorField.setMessage(errorMessage);
            errorFields.add(errorField);
        } else if (StringUtils.isBlank(store.getDescription())) {
            final ErrorField errorField = ErrorField.builder().code(InvConstants.STORE_DESCRIPTION_MANDATORY_CODE)
                    .message(InvConstants.STORE_DESCRIPTION_MANADATORY_ERROR_MESSAGE)
                    .field(InvConstants.STORE_DESCRIPTION_MANADATORY_FIELD_NAME).build();
            String errorMessage = errorField.getMessage();
            errorMessage = errorMessage + " for store[" + i + "]";
            errorField.setMessage(errorMessage);
            errorFields.add(errorField);
        }

        if (StringUtils.isBlank(store.getBillingAddress())) {
            final ErrorField errorField = ErrorField.builder().code(InvConstants.STORE_BILLINGADDRESS_MANDATORY_CODE)
                    .message(InvConstants.STORE_BILLINGADDRESS_MANADATORY_ERROR_MESSAGE)
                    .field(InvConstants.STORE_BILLINGADDRESS_MANADATORY_FIELD_NAME).build();
            String errorMessage = errorField.getMessage();
            errorMessage = errorMessage + " for store[" + i + "]";
            errorField.setMessage(errorMessage);
            errorFields.add(errorField);
        } 
        if (StringUtils.isBlank(store.getDeliveryAddress())) {
            final ErrorField errorField = ErrorField.builder().code(InvConstants.STORE_DELIVERYADDRESS_MANDATORY_CODE)
                    .message(InvConstants.STORE_DELIVERYADDRESS_MANADATORY_ERROR_MESSAGE)
                    .field(InvConstants.STORE_DELIVERYADDRESS_MANADATORY_FIELD_NAME).build();
            String errorMessage = errorField.getMessage();
            errorMessage = errorMessage + " for store[" + i + "]";
            errorField.setMessage(errorMessage);
            errorFields.add(errorField);
        } 
        if (StringUtils.isBlank(store.getContactNo1())) {
            final ErrorField errorField = ErrorField.builder().code(InvConstants.STORE_CONTACTNO1_MANDATORY_CODE)
                    .message(InvConstants.STORE_CONTACTNO1_MANADATORY_ERROR_MESSAGE)
                    .field(InvConstants.STORE_CONTACTNO1_MANADATORY_FIELD_NAME).build();
            String errorMessage = errorField.getMessage();
            errorMessage = errorMessage + " for store[" + i + "]";
            errorField.setMessage(errorMessage);
            errorFields.add(errorField);
        } 
        if (StringUtils.isBlank(store.getEmail())){
            final ErrorField errorField = ErrorField.builder().code(InvConstants.STORE_EMAIL_MANDATORY_CODE)
                    .message(InvConstants.STORE_EMAIL_MANADATORY_ERROR_MESSAGE)
                    .field(InvConstants.STORE_EMAIL_MANADATORY_FIELD_NAME).build();
            String errorMessage = errorField.getMessage();
            errorMessage = errorMessage + " for store[" + i + "]";
            errorField.setMessage(errorMessage);
            errorFields.add(errorField);
        }
        
        if(store.getDepartment() == null){
            final ErrorField errorField = ErrorField.builder().code(InvConstants.STORE_DEPARTMENT_MANDATORY_CODE)
                    .message(InvConstants.STORE_DEPARTMENT_MANADATORY_ERROR_MESSAGE)
                    .field(InvConstants.STORE_DEPARTMENT_MANADATORY_FIELD_NAME).build();
           String errorMessage = errorField.getMessage();
           errorMessage = errorMessage + " for store[" + i + "]";
           errorField.setMessage(errorMessage);
            errorFields.add(errorField); 
        }
        else if(store.getDepartment() != null && StringUtils.isBlank(store.getDepartment().getCode())){
            final ErrorField errorField = ErrorField.builder().code(InvConstants.STORE_DEPARTMENT_DETAILS_MANDATORY_CODE)
                    .message(InvConstants.STORE_DEPARTMENT_DETAILS_MANADATORY_ERROR_MESSAGE)
                    .field(InvConstants.STORE_DEPARTMENT_DETAILS_MANADATORY_FIELD_NAME).build();
            String errorMessage = errorField.getMessage();
            errorMessage = errorMessage + " for store[" + i + "]";
            errorField.setMessage(errorMessage);
            errorFields.add(errorField); 
        }
         if(store.getStoreInCharge() == null){
            final ErrorField errorField = ErrorField.builder().code(InvConstants.STORE_STOREINCHARGE_MANDATORY_CODE)
                    .message(InvConstants.STORE_STOREINCHARGE_MANADATORY_ERROR_MESSAGE)
                    .field(InvConstants.STORE_STOREINCHARGE_MANADATORY_FIELD_NAME).build();
            String errorMessage = errorField.getMessage();
            errorMessage = errorMessage + " for store[" + i + "]";
            errorField.setMessage(errorMessage);
            errorFields.add(errorField); 
        }
        else if(store.getStoreInCharge() != null && StringUtils.isBlank(store.getStoreInCharge().getCode())){
            final ErrorField errorField = ErrorField.builder().code(InvConstants.STORE_STOREINCHARGE_DETAILS_MANDATORY_CODE)
                    .message(InvConstants.STORE_STOREINCHARGE_DETAILS_MANADATORY_ERROR_MESSAGE)
                    .field(InvConstants.STORE_STOREINCHARGE_DETAILS_MANADATORY_FIELD_NAME).build();
            String errorMessage = errorField.getMessage();
            errorMessage = errorMessage + " for store[" + i + "]";
            errorField.setMessage(errorMessage);
            errorFields.add(errorField); 
        }
       
            else
            return;

    }

}
