package org.egov.pgrrest.read.domain.service.validator;

import org.egov.pgrrest.read.domain.exception.TenantIdMandatoryException;
import org.egov.pgrrest.read.domain.model.ServiceRequest;
import org.egov.pgrrest.read.domain.service.ServiceRequestValidator;
import org.springframework.stereotype.Service;

@Service
public class TenantIdMandatoryValidator implements ServiceRequestValidator {

    @Override
    public boolean canValidate(ServiceRequest serviceRequest) {
        return true;
    }

    @Override
    public void validate(ServiceRequest serviceRequest) {
        if (serviceRequest.isTenantIdAbsent()) {
            throw new TenantIdMandatoryException();
        }
    }
}

