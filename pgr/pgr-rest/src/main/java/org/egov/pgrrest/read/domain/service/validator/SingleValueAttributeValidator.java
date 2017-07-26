package org.egov.pgrrest.read.domain.service.validator;

import org.egov.pgrrest.common.domain.model.ServiceStatus;
import org.egov.pgrrest.common.domain.model.AttributeDefinition;
import org.egov.pgrrest.common.domain.model.ServiceDefinition;
import org.egov.pgrrest.read.domain.exception.MultipleAttributeValuesReceivedException;
import org.egov.pgrrest.read.domain.model.ServiceRequest;

import java.util.List;

public class SingleValueAttributeValidator implements AttributeValueValidator {

    @Override
    public void validate(ServiceRequest serviceRequest, ServiceDefinition serviceDefinition, ServiceStatus action) {
        final List<AttributeDefinition> singleValueTypeAttributes = serviceDefinition.getSingleValueTypeAttributes();
        singleValueTypeAttributes
            .forEach(attributeDefinition -> validateAttributeEntries(serviceRequest, attributeDefinition));
    }

    private void validateAttributeEntries(ServiceRequest serviceRequest, AttributeDefinition attributeDefinition) {
        final boolean isMultipleEntriesPresent = serviceRequest
            .isMultipleAttributeEntriesPresent(attributeDefinition.getCode());
        if (isMultipleEntriesPresent) {
            throw new MultipleAttributeValuesReceivedException(attributeDefinition.getCode());
        }
    }
}

