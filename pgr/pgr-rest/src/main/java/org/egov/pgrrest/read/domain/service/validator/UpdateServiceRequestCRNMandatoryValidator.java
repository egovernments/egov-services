package org.egov.pgrrest.read.domain.service.validator;

import org.egov.pgrrest.read.domain.exception.ServiceRequestIdMandatoryException;
import org.egov.pgrrest.read.domain.model.ServiceRequest;
import org.egov.pgrrest.read.domain.service.ServiceRequestValidator;
import org.springframework.stereotype.Service;

@Service
public class UpdateServiceRequestCRNMandatoryValidator implements ServiceRequestValidator {

    @Override
    public boolean canValidate(ServiceRequest serviceRequest) {
        return serviceRequest.isModifyServiceRequest();
    }

    @Override
    public void validate(ServiceRequest serviceRequest) {
        if (serviceRequest.isCrnAbsent()) {
            throw new ServiceRequestIdMandatoryException();
        }
    }
}

