package org.egov.pgr.domain.service.validator.servicetypevalidators;


import org.egov.pgr.domain.model.ServiceType;
import org.springframework.stereotype.Service;

@Service
public class TenantIdMandatoryValidator implements ServiceTypeValidator {
    @Override
    public boolean canValidate(ServiceType serviceType) {
        return false;
    }

    @Override
    public void validate(ServiceType serviceType) {

    }
}
