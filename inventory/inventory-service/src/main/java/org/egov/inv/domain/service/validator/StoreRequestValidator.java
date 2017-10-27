package org.egov.inv.domain.service.validator;

import org.egov.common.contract.response.ErrorField;
import org.egov.inv.constants.InvConstants;
import org.egov.inv.domain.StoreException;
import org.egov.inv.domain.model.Store;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class StoreRequestValidator {
    private CommonMasterValidator commonMasterValidator;

    private InvConstants invConstants;

    public StoreRequestValidator(CommonMasterValidator commonMasterValidator) {
        this.commonMasterValidator = commonMasterValidator;
    }

    public void validate(Store store, String tenantId) {
        List<ErrorField> errorFields = getError(store, tenantId);
        if (!errorFields.isEmpty()) {
            throw new StoreException(errorFields);
        }
    }

    private List<ErrorField> getError(Store store, String tenantId) {
        List<ErrorField> errorFields = new ArrayList<>();

        validation(store, errorFields, tenantId);

        return errorFields;
    }

    private void validation(Store store, List<ErrorField> errorFields, String tenantId) {
        commonMasterValidator.addLengthValidationError("Store code", store.getCode(), 5, 50, errorFields);
        commonMasterValidator.addLengthValidationError("Store name", store.getName(), 5, 50, errorFields);
        commonMasterValidator.addLengthValidationError("Store description", store.getDescription(), 0, 1000, errorFields);
        commonMasterValidator.addLengthValidationError("Store billingAddress", store.getBillingAddress(), 0, 1000, errorFields);
        commonMasterValidator.addLengthValidationError("Store deliveryAddress", store.getDeliveryAddress(), 0, 1000, errorFields);
        commonMasterValidator.addLengthValidationError("Store contactNo1", store.getContactNo1(), 0, 10, errorFields);
        commonMasterValidator.addLengthValidationError("Store contactNo2", store.getContactNo2(), 0, 10, errorFields);
        commonMasterValidator.addLengthValidationError("Store email", store.getEmail(), 0, 100, errorFields);
        commonMasterValidator.addNotNullForStringValidationErrors("Store code", store.getCode(), errorFields, InvConstants.STORE_CODE_MANDATORY_CODE);
        commonMasterValidator.addNotNullForStringValidationErrors("Store name", store.getName(), errorFields, InvConstants.STORE_NAME_MANDATORY_CODE);
        commonMasterValidator.addNotNullForStringValidationErrors("Store description", store.getDescription(), errorFields, InvConstants.STORE_DESCRIPTION_MANDATORY_CODE);
        commonMasterValidator.addNotNullForStringValidationErrors("Store billingAddress", store.getBillingAddress(), errorFields, InvConstants.STORE_BILLINGADDRESS_MANDATORY_CODE);
        commonMasterValidator.addNotNullForStringValidationErrors("Store deliveryAddress", store.getDeliveryAddress(), errorFields, InvConstants.STORE_DELIVERYADDRESS_MANDATORY_CODE);
        commonMasterValidator.addNotNullForStringValidationErrors("Store contactNo1", store.getContactNo1(), errorFields, InvConstants.STORE_CONTACTNO1_MANDATORY_CODE);
        commonMasterValidator.addNotNullForStringValidationErrors("Store email", store.getEmail(), errorFields, InvConstants.STORE_EMAIL_MANDATORY_CODE);
        commonMasterValidator.addNotNullForStringValidationErrors("tenantId", tenantId, errorFields, InvConstants.TENANTID_MANDATORY_CODE);
        commonMasterValidator.addNotNullForObjectValidationErrors("department", store.getDepartment(), errorFields, InvConstants.STORE_DEPARTMENT_MANDATORY_CODE);
        if (store.getDepartment() != null)
            commonMasterValidator.addNotNullForObjectValidationErrors("department code", store.getDepartment().getCode(), errorFields, InvConstants.STORE_DEPARTMENT_DETAILS_MANDATORY_CODE);
        commonMasterValidator.addNotNullForObjectValidationErrors("storeInCharge", store.getStoreInCharge(), errorFields, InvConstants.STORE_STOREINCHARGE_MANDATORY_CODE);
        if (store.getStoreInCharge() != null)
            commonMasterValidator.addNotNullForObjectValidationErrors("storeInCharge code", store.getStoreInCharge().getCode(), errorFields, InvConstants.STORE_STOREINCHARGE_DETAILS_MANDATORY_CODE);
        commonMasterValidator.validatePattern("Store code", store.getCode(), InvConstants.PATTERN_ALPHANUMERIC, errorFields, InvConstants.PATTERN_ALPHANUMERIC_CODE);
        commonMasterValidator.validatePattern("Store name", store.getName(), InvConstants.PATTERN_CHARACTER, errorFields, InvConstants.PATTERN_ALPHABETS_CODE);
        commonMasterValidator.validatePattern("Store description", store.getDescription(), InvConstants.PATTERN_ALPHANUMERIC_SPACE, errorFields, InvConstants.PATTERN_ALPHANUMERIC_WITH_SPACE_CODE);
        commonMasterValidator.validatePattern("Store billingAddress", store.getBillingAddress(), InvConstants.PATTERN_ALPHANUMERIC_SPACE, errorFields, InvConstants.PATTERN_ALPHANUMERIC_WITH_SPACE_CODE);
        commonMasterValidator.validatePattern("Store deliveryAddress", store.getDeliveryAddress(), InvConstants.PATTERN_ALPHANUMERIC_SPACE, errorFields, InvConstants.PATTERN_ALPHANUMERIC_WITH_SPACE_CODE);
        commonMasterValidator.validatePattern("Store contactNo1", store.getContactNo1(), InvConstants.PATTERN_NUMBERS, errorFields, InvConstants.PATTERN_NUMBERS_CODE);
        commonMasterValidator.validatePattern("Store contactNo2", store.getContactNo2(), InvConstants.PATTERN_NUMBERS, errorFields, InvConstants.PATTERN_NUMBERS_CODE);
        commonMasterValidator.validatePattern("Store email", store.getEmail(), InvConstants.PATTERN_EMAIL, errorFields, InvConstants.PATTERN_EMAIL_CODE);
    }


}
