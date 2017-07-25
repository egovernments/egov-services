package org.egov.pgrrest.read.domain.service.validator;

import org.egov.pgrrest.common.domain.model.AttributeDefinition;
import org.egov.pgrrest.common.domain.model.AttributeEntry;
import org.egov.pgrrest.common.domain.model.ServiceDefinition;
import org.egov.pgrrest.common.domain.model.ServiceStatus;
import org.egov.pgrrest.read.domain.exception.InvalidDateTimeAttributeEntryException;
import org.egov.pgrrest.read.domain.model.ServiceRequest;
import org.joda.time.LocalDateTime;
import org.joda.time.format.DateTimeFormat;
import org.springframework.util.CollectionUtils;

import java.util.List;

import static org.apache.commons.lang3.StringUtils.isEmpty;
import static org.egov.pgrrest.common.domain.model.AttributeDefinition.DATE_TIME_FORMAT;

public class DateTimeAttributeValidator implements AttributeValueValidator {

    @Override
    public void validate(ServiceRequest serviceRequest, ServiceDefinition serviceDefinition, ServiceStatus action) {
        final List<AttributeDefinition> nonComputedDateTimeAttributes = serviceDefinition.
            getNonComputedDateTimeAttributes();

        if (CollectionUtils.isEmpty(nonComputedDateTimeAttributes)) {
            return;
        }

        nonComputedDateTimeAttributes.forEach(attributeDefinition -> {
            final AttributeEntry matchingDateTimeAttributeEntry = serviceRequest
                .getAttributeWithKey(attributeDefinition.getCode());

            if(matchingDateTimeAttributeEntry == null || isEmpty(matchingDateTimeAttributeEntry.getCode())) {
                return;
            }
            validateDateTimeFormat(matchingDateTimeAttributeEntry);
        });

    }

    private void validateDateTimeFormat(AttributeEntry attributeEntry) {
        try {
            LocalDateTime.parse(attributeEntry.getCode(), DateTimeFormat.forPattern(DATE_TIME_FORMAT));
        } catch (IllegalArgumentException ex) {
            throw new InvalidDateTimeAttributeEntryException(attributeEntry.getKey());
        }
    }
}
