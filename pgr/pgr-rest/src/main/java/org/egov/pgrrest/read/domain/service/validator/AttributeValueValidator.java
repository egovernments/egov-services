package org.egov.pgrrest.read.domain.service.validator;

import org.egov.pgrrest.common.domain.model.ServiceStatus;
import org.egov.pgrrest.common.domain.model.ServiceDefinition;
import org.egov.pgrrest.read.domain.model.ServiceRequest;

public interface AttributeValueValidator {
    void validate(ServiceRequest serviceRequest, ServiceDefinition serviceDefinition, ServiceStatus action);
}

