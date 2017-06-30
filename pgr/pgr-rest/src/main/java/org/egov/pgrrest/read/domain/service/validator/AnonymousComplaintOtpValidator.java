package org.egov.pgrrest.read.domain.service.validator;

import org.egov.pgrrest.read.domain.model.ServiceRequest;
import org.egov.pgrrest.read.domain.service.OtpService;
import org.egov.pgrrest.read.domain.service.ServiceRequestValidator;
import org.springframework.stereotype.Service;

@Service
public class AnonymousComplaintOtpValidator implements ServiceRequestValidator {

    private OtpService otpService;

    public AnonymousComplaintOtpValidator(OtpService otpService) {
        this.otpService = otpService;
    }

    @Override
    public boolean canValidate(ServiceRequest serviceRequest) {
        return serviceRequest.isAnonymous() && serviceRequest.isComplaintType();
    }

    @Override
    public void validate(ServiceRequest serviceRequest) {
        otpService.validateOtp(serviceRequest);
    }
}
