package org.egov.pgrrest.read.domain.service.validator;

import org.egov.pgrrest.read.domain.model.ServiceRequest;
import org.egov.pgrrest.read.domain.service.ServiceRequestValidator;
import org.springframework.stereotype.Service;

@Service
public class AttributeValuesValidator implements ServiceRequestValidator {

    @Override
    public boolean canValidate(ServiceRequest serviceRequest) {
        return true;
    }

    @Override
    public void validate(ServiceRequest serviceRequest) {
        serviceRequest.validateAttributeEntries();
    }
}

