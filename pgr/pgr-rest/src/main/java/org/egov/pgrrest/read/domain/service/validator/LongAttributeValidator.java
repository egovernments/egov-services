package org.egov.pgrrest.read.domain.service.validator;

import static org.apache.commons.lang3.StringUtils.isEmpty;

import java.util.List;

import org.egov.pgrrest.common.domain.model.AttributeDefinition;
import org.egov.pgrrest.common.domain.model.AttributeEntry;
import org.egov.pgrrest.common.domain.model.ServiceDefinition;
import org.egov.pgrrest.common.domain.model.ServiceStatus;
import org.egov.pgrrest.read.domain.exception.InvalidLongAttributeEntryException;
import org.egov.pgrrest.read.domain.model.ServiceRequest;
import org.springframework.util.CollectionUtils;

public class LongAttributeValidator implements AttributeValueValidator {

    @Override
    public void validate(ServiceRequest serviceRequest, ServiceDefinition serviceDefinition, ServiceStatus action) {
        final List<AttributeDefinition> nonComputedLongAttributes =
            serviceDefinition.getNonComputedLongAttributes();

        if (CollectionUtils.isEmpty(nonComputedLongAttributes)) {
            return;
        }

        nonComputedLongAttributes.forEach(attributeDefinition -> {
            final AttributeEntry matchingLongAttributeEntry = serviceRequest
                .getAttributeWithKey(attributeDefinition.getCode());

            if(matchingLongAttributeEntry == null || isEmpty(matchingLongAttributeEntry.getCode())) {
                return;
            }
            validateLong(matchingLongAttributeEntry);
        });

    }

    private void validateLong(AttributeEntry attributeEntry) {
        try {
            Long.parseLong(attributeEntry.getCode());
        } catch (NumberFormatException ex) {
            throw new InvalidLongAttributeEntryException(attributeEntry.getKey());
        }
    }
}

