package org.egov.pgrrest.read.domain.service.validator;

import org.egov.pgrrest.common.domain.model.ServiceStatus;
import org.egov.pgrrest.common.domain.model.AttributeDefinition;
import org.egov.pgrrest.common.domain.model.AttributeEntry;
import org.egov.pgrrest.common.domain.model.ServiceDefinition;
import org.egov.pgrrest.read.domain.exception.InvalidIntegerAttributeEntryException;
import org.egov.pgrrest.read.domain.model.ServiceRequest;
import org.springframework.util.CollectionUtils;

import java.util.List;

import static org.apache.commons.lang3.StringUtils.isEmpty;

public class IntegerAttributeValidator implements AttributeValueValidator {

    @Override
    public void validate(ServiceRequest serviceRequest, ServiceDefinition serviceDefinition, ServiceStatus action) {
        final List<AttributeDefinition> nonComputedIntegerAttributes =
            serviceDefinition.getNonComputedIntegerAttributes();

        if (CollectionUtils.isEmpty(nonComputedIntegerAttributes)) {
            return;
        }

        nonComputedIntegerAttributes.forEach(attributeDefinition -> {
            final AttributeEntry matchingIntegerAttributeEntry = serviceRequest
                .getAttributeWithKey(attributeDefinition.getCode());

            if(matchingIntegerAttributeEntry == null || isEmpty(matchingIntegerAttributeEntry.getCode())) {
                return;
            }
            validateInteger(matchingIntegerAttributeEntry);
        });

    }

    private void validateInteger(AttributeEntry attributeEntry) {
        try {
            Integer.parseInt(attributeEntry.getCode());
        } catch (NumberFormatException ex) {
            throw new InvalidIntegerAttributeEntryException(attributeEntry.getKey());
        }
    }
}

