package org.egov.pgr.domain.service.validator.servicetypevalidators;

import org.egov.common.contract.response.ErrorField;
import org.egov.pgr.domain.exception.InvalidServiceTypeException;
import org.egov.pgr.domain.model.ServiceType;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ServiceTypeLengthValidator implements ServiceTypeValidator {

    private static final String TENANT_MESSAGE = "Tenant id invalid Length";
    private static final String TENANT_CODE = "PGR.TENANT_INVALID_LENGTH";
    private static final String TENANT_FIELD_NAME = "serviceType.tenantId";

    private static final String DESCRIPTION_MESSAGE = "Description invalid Length";
    private static final String DESCRIPTION_CODE = "PGR.DESCRIPTION_INVALID_LENGTH";
    private static final String DESCRIPTION_FIELD_NAME = "serviceType.description";

    private static final String CODE_MESSAGE = "Code invalid Length";
    private static final String CODE_CODE = "PGR.CODE_INVALID_LENGTH";
    private static final String CODE_FIELD_NAME = "serviceType.code";

    private static final String SERVICE_NAME_MESSAGE = "Service Name invalid Length";
    private static final String SERVICE_NAME_CODE = "PGR.SERVICE_NAME_INVALID_LENGTH";
    private static final String SERVICE_NAME_FIELD_NAME = "serviceType.serviceName";

    private static final String TYPE_MESSAGE = "Type invalid Length";
    private static final String TYPE_CODE = "PGR.TYPE_INVALID_LENGTH";
    private static final String TYPE_FIELD_NAME = "serviceType.type";


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

        updateLengthValidations(model, errorFields);

        return  errorFields;
    }

    private void updateLengthValidations(ServiceType model, List<ErrorField> errorFields){
        addTenantIdLengthError(model, errorFields);
        addDescriptionLengthError(model, errorFields);
        addCodeLengthError(model, errorFields);
        addServiceNameLengthError(model, errorFields);
        addTypeLengthError(model, errorFields);
    }

    private void addTenantIdLengthError(ServiceType model, List<ErrorField> errorFields){
        if(model.isTenantIdLengthMatch())
            return;

        final ErrorField error = ErrorField.builder()
                .code(TENANT_CODE)
                .field(TENANT_FIELD_NAME)
                .message(TENANT_MESSAGE)
                .build();

        errorFields.add(error);
    }

    private void addDescriptionLengthError(ServiceType model, List<ErrorField> errorFields){
        if(model.isDescriptionLengthMatch())
            return;

        final ErrorField error = ErrorField.builder()
                .code(DESCRIPTION_CODE)
                .field(DESCRIPTION_FIELD_NAME)
                .message(DESCRIPTION_MESSAGE)
                .build();

        errorFields.add(error);
    }

    private void addCodeLengthError(ServiceType model, List<ErrorField> errorFields){
        if(model.isCodeLengthMatch())
            return;

        final ErrorField error = ErrorField.builder()
                .code(CODE_CODE)
                .field(CODE_FIELD_NAME)
                .message(CODE_MESSAGE)
                .build();

        errorFields.add(error);
    }

    private void addServiceNameLengthError(ServiceType model, List<ErrorField> errorFields){
        if(model.isnameLengthMatch())
            return;

        final ErrorField error = ErrorField.builder()
                .code(SERVICE_NAME_CODE)
                .field(SERVICE_NAME_FIELD_NAME)
                .message(SERVICE_NAME_MESSAGE)
                .build();

        errorFields.add(error);
    }


    private void addTypeLengthError(ServiceType model, List<ErrorField> errorFields){
        if(model.isTypeLengthMatch())
            return;

        final ErrorField error = ErrorField.builder()
                .code(TYPE_CODE)
                .field(TYPE_FIELD_NAME)
                .message(TYPE_MESSAGE)
                .build();

        errorFields.add(error);
    }
}
