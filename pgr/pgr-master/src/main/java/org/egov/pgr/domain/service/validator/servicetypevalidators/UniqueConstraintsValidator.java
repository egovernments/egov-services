package org.egov.pgr.domain.service.validator.servicetypevalidators;

import org.egov.common.contract.response.ErrorField;
import org.egov.pgr.domain.exception.InvalidServiceTypeException;
import org.egov.pgr.domain.model.ServiceType;
import org.egov.pgr.persistence.repository.ServiceTypeRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class UniqueConstraintsValidator implements ServiceTypeValidator {

    private static final String MESSAGE = "Invalid service Type";
    private static final String CODE = "PGR.INVALID_SERVICE_TYPE";
    private static final String FIELD_NAME = "serviceType.id";

    private ServiceTypeRepository serviceTypeRepository;

    public UniqueConstraintsValidator(ServiceTypeRepository serviceTypeRepository) {
        this.serviceTypeRepository = serviceTypeRepository;
    }

    @Override
    public boolean canValidate(ServiceType serviceType) {
        return serviceType.isUpdate();
    }

    @Override
    public void validate(ServiceType serviceType) {
        org.egov.pgr.persistence.dto.ServiceType record = serviceTypeRepository.
                findByCodeAndTenantId(serviceType.toDto());

        if(!record.getId().equals(serviceType.getId()))
            throw new InvalidServiceTypeException(getErrorFields());
    }

    private List<ErrorField> getErrorFields(){

        List<ErrorField> errorFields = new ArrayList<>();

        final ErrorField error = ErrorField.builder()
                .code(CODE)
                .field(FIELD_NAME)
                .message(MESSAGE)
                .build();

        errorFields.add(error);

        return  errorFields;
    }
}