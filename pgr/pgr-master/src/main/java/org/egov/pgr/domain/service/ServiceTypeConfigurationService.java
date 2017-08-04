package org.egov.pgr.domain.service;

import org.egov.pgr.domain.model.ServiceTypeConfiguration;
import org.egov.pgr.domain.service.validator.ServiceTypeConfigurationValidator;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ServiceTypeConfigurationService {

    private List<ServiceTypeConfigurationValidator> validators;

    public ServiceTypeConfigurationService(List<ServiceTypeConfigurationValidator> validators) {
        this.validators = validators;
    }

    public void create(ServiceTypeConfiguration serviceTypeConfiguration){
        validate(serviceTypeConfiguration);
    }

    private void validate(ServiceTypeConfiguration serviceTypeConfiguration){
        validators.stream()
                .filter(validator -> validator.canValidate(serviceTypeConfiguration))
                .forEach(v -> v.validate(serviceTypeConfiguration));
    }

}
