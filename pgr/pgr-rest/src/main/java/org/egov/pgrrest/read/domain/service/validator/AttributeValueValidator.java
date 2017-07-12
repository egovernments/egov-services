package org.egov.pgrrest.read.domain.service.validator;

import org.egov.pgrrest.common.domain.model.ServiceDefinition;
import org.egov.pgrrest.read.domain.model.ServiceRequest;
import org.egov.pgrrest.read.domain.model.SevaRequestAction;

public interface AttributeValueValidator {
    void validate(ServiceRequest serviceRequest, ServiceDefinition serviceDefinition, SevaRequestAction action);
}

