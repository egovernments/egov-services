package org.egov.pgrrest.read.domain.service.validator;

import org.egov.pgrrest.common.domain.model.AttributeDefinition;
import org.egov.pgrrest.common.domain.model.AttributeEntry;
import org.egov.pgrrest.common.domain.model.ServiceDefinition;
import org.egov.pgrrest.common.domain.model.ServiceStatus;
import org.egov.pgrrest.read.domain.exception.InvalidDoubleAttributeEntryException;
import org.egov.pgrrest.read.domain.model.ServiceRequest;
import org.springframework.util.CollectionUtils;

import java.util.List;

import static org.apache.commons.lang3.StringUtils.isEmpty;

public class DoubleAttributeValidator implements AttributeValueValidator {

    @Override
    public void validate(ServiceRequest serviceRequest, ServiceDefinition serviceDefinition, ServiceStatus action) {
        final List<AttributeDefinition> nonComputedDoubleAttributes =
            serviceDefinition.getNonComputedDoubleAttributes();

        if (CollectionUtils.isEmpty(nonComputedDoubleAttributes)) {
            return;
        }

        nonComputedDoubleAttributes.forEach(attributeDefinition -> {
            final AttributeEntry matchingDoubleAttributeEntry = serviceRequest
                .getAttributeWithKey(attributeDefinition.getCode());

            if(matchingDoubleAttributeEntry == null || isEmpty(matchingDoubleAttributeEntry.getCode())) {
                return;
            }
            validateDoubleFormat(matchingDoubleAttributeEntry);
        });

    }

    private void validateDoubleFormat(AttributeEntry attributeEntry) {
        try {
            Double.parseDouble(attributeEntry.getCode());
        } catch (NumberFormatException ex) {
            throw new InvalidDoubleAttributeEntryException(attributeEntry.getKey());
        }
    }
}
