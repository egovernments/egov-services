package org.egov.pgrrest.read.domain.service.validator;

import org.egov.pgrrest.common.domain.model.ServiceStatus;
import org.egov.pgrrest.common.domain.model.AttributeDefinition;
import org.egov.pgrrest.common.domain.model.AttributeEntry;
import org.egov.pgrrest.common.domain.model.ServiceDefinition;
import org.egov.pgrrest.read.domain.exception.InvalidDateAttributeEntryException;
import org.egov.pgrrest.read.domain.model.ServiceRequest;
import org.joda.time.LocalDate;
import org.joda.time.format.DateTimeFormat;
import org.springframework.util.CollectionUtils;

import java.util.List;

import static org.apache.commons.lang3.StringUtils.isEmpty;
import static org.egov.pgrrest.common.domain.model.AttributeDefinition.DATE_FORMAT;

public class DateAttributeValidator implements AttributeValueValidator {

    @Override
    public void validate(ServiceRequest serviceRequest, ServiceDefinition serviceDefinition, ServiceStatus action) {
        final List<AttributeDefinition> nonComputedDateAttributes = serviceDefinition.getNonComputedDateAttributes();

        if (CollectionUtils.isEmpty(nonComputedDateAttributes)) {
            return;
        }

        nonComputedDateAttributes.forEach(attributeDefinition -> {
            final AttributeEntry matchingDateAttributeEntry = serviceRequest
                .getAttributeWithKey(attributeDefinition.getCode());

            if(matchingDateAttributeEntry == null || isEmpty(matchingDateAttributeEntry.getCode())) {
                return;
            }
            validateDateFormat(matchingDateAttributeEntry);
        });

    }

    private void validateDateFormat(AttributeEntry attributeEntry) {
        try {
            LocalDate.parse(attributeEntry.getCode(), DateTimeFormat.forPattern(DATE_FORMAT));
        } catch (IllegalArgumentException ex) {
            throw new InvalidDateAttributeEntryException(attributeEntry.getKey());
        }
    }
}


