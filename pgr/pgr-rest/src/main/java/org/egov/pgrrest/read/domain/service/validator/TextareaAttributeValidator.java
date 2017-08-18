package org.egov.pgrrest.read.domain.service.validator;

import static org.apache.commons.lang3.StringUtils.isEmpty;

import java.util.List;

import org.egov.pgrrest.common.domain.model.AttributeDefinition;
import org.egov.pgrrest.common.domain.model.AttributeEntry;
import org.egov.pgrrest.common.domain.model.ServiceDefinition;
import org.egov.pgrrest.common.domain.model.ServiceStatus;
import org.egov.pgrrest.read.domain.exception.InvalidTextAreaAttributeEntryException;
import org.egov.pgrrest.read.domain.model.ServiceRequest;
import org.springframework.util.CollectionUtils;

public class TextareaAttributeValidator implements AttributeValueValidator {

    @Override
    public void validate(ServiceRequest serviceRequest, ServiceDefinition serviceDefinition, ServiceStatus action) {
        final List<AttributeDefinition> nonComputedTextAreaAttributes =
            serviceDefinition.getNonComputedTextAreaAttributes();

        if (CollectionUtils.isEmpty(nonComputedTextAreaAttributes)) {
            return;
        }

        nonComputedTextAreaAttributes.forEach(attributeDefinition -> {
            final AttributeEntry matchingTextAreaAttributeEntry = serviceRequest
                .getAttributeWithKey(attributeDefinition.getCode());

            if(matchingTextAreaAttributeEntry == null || isEmpty(matchingTextAreaAttributeEntry.getCode())) {
                return;
            }
            validateTextArea(matchingTextAreaAttributeEntry);
        });

    }

    private void validateTextArea(AttributeEntry attributeEntry) {
        try {
            attributeEntry.getCode().toString();
        } catch (Exception ex) {
            throw new InvalidTextAreaAttributeEntryException(attributeEntry.getKey());
        }
    }
}

