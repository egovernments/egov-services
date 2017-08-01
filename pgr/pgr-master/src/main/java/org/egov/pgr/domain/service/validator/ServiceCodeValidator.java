package org.egov.pgr.domain.service.validator;

import org.egov.pgr.domain.exception.ServiceCodeMandatoryException;
import org.egov.pgr.domain.model.ServiceTypeConfiguration;
import org.springframework.stereotype.Service;

@Service
public class ServiceCodeValidator implements ServiceTypeConfigurationValidator {
    @Override
    public boolean canValidate(ServiceTypeConfiguration serviceTypeConfiguration) {
        return true;
    }

    @Override
    public void validate(ServiceTypeConfiguration serviceTypeConfiguration) {
        if(serviceTypeConfiguration.isServiceCodeAbsent()){
            throw new ServiceCodeMandatoryException();
        }
    }
}
