package org.egov.pgrrest.read.domain.service.validator;

import org.egov.pgrrest.read.domain.exception.InvalidServiceRequestException;
import org.egov.pgrrest.read.domain.model.ServiceRequest;
import org.egov.pgrrest.read.domain.service.ServiceRequestValidator;
import org.springframework.stereotype.Service;

@Service
public class NewServiceRequestProcessingFeeAbsentValidator implements ServiceRequestValidator {

    @Override
    public boolean canValidate(ServiceRequest serviceRequest) {
        return serviceRequest.isNewServiceRequest();
    }

    @Override
    public void validate(ServiceRequest serviceRequest) {
        if (serviceRequest.isProcessingFeePresent()) {
            throw new InvalidServiceRequestException(serviceRequest);
        }
    }
}


