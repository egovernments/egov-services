package org.egov.pgrrest.read.domain.service;

import org.egov.pgrrest.common.domain.model.ServiceDefinition;
import org.egov.pgrrest.read.domain.model.ServiceRequest;

public interface ServiceRequestValidator {
    boolean canValidate(ServiceRequest serviceRequest);
    void validate(ServiceRequest serviceRequest);
}

