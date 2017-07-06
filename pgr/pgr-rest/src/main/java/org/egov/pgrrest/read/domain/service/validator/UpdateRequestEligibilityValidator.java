package org.egov.pgrrest.read.domain.service.validator;

import org.egov.pgrrest.common.domain.model.AuthenticatedUser;
import org.egov.pgrrest.read.domain.model.ServiceRequest;
import org.egov.pgrrest.read.domain.service.ServiceRequestValidator;
import org.egov.pgrrest.read.domain.service.UpdateServiceRequestEligibilityService;
import org.springframework.stereotype.Service;

@Service
public class UpdateRequestEligibilityValidator implements ServiceRequestValidator {

    private UpdateServiceRequestEligibilityService updateRequestEligibilityService;

    public UpdateRequestEligibilityValidator(UpdateServiceRequestEligibilityService updateRequestEligibilityService) {
        this.updateRequestEligibilityService = updateRequestEligibilityService;
    }

    @Override
    public boolean canValidate(ServiceRequest serviceRequest) {
        return serviceRequest.isModifyServiceRequest();
    }

    @Override
    public void validate(ServiceRequest serviceRequest) {
        final AuthenticatedUser user = serviceRequest.getAuthenticatedUser();
        updateRequestEligibilityService.validate(serviceRequest.getCrn(), serviceRequest.getTenantId(), user);
    }
}
