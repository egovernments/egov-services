package org.egov.pgr.domain.service.validator.servicetypevalidators;

import org.egov.common.contract.response.ErrorField;
import org.egov.pgr.domain.exception.InvalidServiceTypeException;
import org.egov.pgr.domain.model.ServiceType;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class MandatoryFieldsValidator implements ServiceTypeValidator {

    private static final String TENANT_MESSAGE = "Tenant id is mandatory";
    private static final String TENANT_CODE = "PGR.TENANT_ID_MANDATORY";
    private static final String TENANT_FIELD_NAME = "serviceType.tenantId";

    private static final String MESSAGE = "Service Code is mandatory";
    private static final String CODE = "PGR.SERVICE_CODE_MANDATORY";
    private static final String FIELD_NAME = "serviceType.serviceCode";

    private static final String SERVICE_NAME_MESSAGE = "Service Name is mandatory";
    private static final String SERVICE_NAME_CODE = "PGR.SERVICE_NAME_MANDATORY";
    private static final String SERVICE_NAME_FIELD_NAME = "serviceType.serviceCode";

    private static final String SERVICE_CATEGORY_MESSAGE = "Service Category is mandatory";
    private static final String SERVICE_CATEGORY_CODE = "PGR.SERVICE_CATEGORY_MANDATORY";
    private static final String SERVICE_CATEGORY_FIELD_NAME = "serviceType.category";


    @Override
    public boolean canValidate(ServiceType serviceType) {
        return serviceType.isUpdate();
    }

    @Override
    public void validate(ServiceType serviceType) {
        if(!getErrorFields(serviceType).isEmpty())
            throw new InvalidServiceTypeException(getErrorFields(serviceType));
    }

    private List<ErrorField> getErrorFields(ServiceType model){

        List<ErrorField> errorFields = new ArrayList<>();

        updateValidations(model, errorFields);

        return  errorFields;
    }

    private void updateValidations(ServiceType model, List<ErrorField> errorFields){
        addTenantIdMandatoryError(model, errorFields);
        addServiceCodeMandatoryError(model, errorFields);
        addServiceNameMandatoryError(model, errorFields);
        addServiceCategoryMandatoryError(model, errorFields);
    }

    private void addTenantIdMandatoryError(ServiceType model, List<ErrorField> errorFields){
        if(!model.isTenantIdAbsent())
            return;

        final ErrorField error = ErrorField.builder()
                .code(TENANT_CODE)
                .field(TENANT_FIELD_NAME)
                .message(TENANT_MESSAGE)
                .build();

        errorFields.add(error);
    }

    private void addServiceCodeMandatoryError(ServiceType model, List<ErrorField> errorFields){
        if(!model.isServiceCodeAbsent())
            return;

        final ErrorField error = ErrorField.builder()
                .code(CODE)
                .field(FIELD_NAME)
                .message(MESSAGE)
                .build();

        errorFields.add(error);
    }

    private void addServiceNameMandatoryError(ServiceType model, List<ErrorField> errorFields){
        if(!model.isServiceNameAbsent())
            return;

        final ErrorField error = ErrorField.builder()
                .code(SERVICE_NAME_CODE)
                .field(SERVICE_NAME_FIELD_NAME)
                .message(SERVICE_NAME_MESSAGE)
                .build();

        errorFields.add(error);
    }

    private void addServiceCategoryMandatoryError(ServiceType model, List<ErrorField> errorFields){
        if(!model.isCategoryAbsent())
            return;

        final ErrorField error = ErrorField.builder()
                .code(SERVICE_CATEGORY_CODE)
                .field(SERVICE_CATEGORY_FIELD_NAME)
                .message(SERVICE_CATEGORY_MESSAGE)
                .build();

        errorFields.add(error);
    }
}