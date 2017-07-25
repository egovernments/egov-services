package org.egov.pgrrest.read.domain.service.validator;

import org.egov.pgrrest.common.domain.model.ServiceStatus;
import org.egov.pgrrest.common.domain.model.AttributeDefinition;
import org.egov.pgrrest.common.domain.model.AttributeEntry;
import org.egov.pgrrest.common.domain.model.ServiceDefinition;
import org.egov.pgrrest.read.domain.exception.MandatoryAttributesAbsentException;
import org.egov.pgrrest.read.domain.model.ServiceRequest;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static org.hibernate.internal.util.collections.CollectionHelper.isNotEmpty;

public class MandatoryAttributeValidator implements AttributeValueValidator {

    @Override
    public void validate(ServiceRequest serviceRequest, ServiceDefinition serviceDefinition, ServiceStatus action) {
        final List<AttributeDefinition> mandatoryAttributes =
            serviceDefinition.getMandatoryAttributes(action, serviceRequest.getAuthenticatedUser().getRoleCodes());
        if (CollectionUtils.isEmpty(mandatoryAttributes)) {
            return;
        }
        final Set<String> mandatoryAttributeCodes =
            getMissingMandatoryAttributeCodes(serviceRequest, mandatoryAttributes);

        if (isNotEmpty(mandatoryAttributeCodes)) {
            throw new MandatoryAttributesAbsentException(mandatoryAttributeCodes);
        }
    }

    private Set<String> getMissingMandatoryAttributeCodes(ServiceRequest serviceRequest,
                                                          List<AttributeDefinition> mandatoryAttributes) {
        final Set<String> mandatoryAttributeCodes = getMandatoryAttributeCodes(mandatoryAttributes);
        final Set<String> attributeValueKeys = getAttributeEntryKeys(serviceRequest);
        mandatoryAttributeCodes.removeAll(attributeValueKeys);
        return mandatoryAttributeCodes;
    }

    private Set<String> getAttributeEntryKeys(ServiceRequest serviceRequest) {
        return serviceRequest.getAttributeEntries().stream()
            .map(AttributeEntry::getKey)
            .distinct()
            .collect(Collectors.toSet());
    }

    private Set<String> getMandatoryAttributeCodes(List<AttributeDefinition> mandatoryAttributes) {
        return mandatoryAttributes.stream()
            .map(AttributeDefinition::getCode)
            .collect(Collectors.toSet());
    }
}
