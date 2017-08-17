package org.egov.pgr.domain.service.validator.servicetypevalidators;

import org.egov.pgr.domain.model.ServiceType;

public interface ServiceTypeValidator {
    boolean canValidate(ServiceType serviceType);
    void validate(ServiceType serviceType);
}