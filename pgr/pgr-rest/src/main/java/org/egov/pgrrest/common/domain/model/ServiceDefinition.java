package org.egov.pgrrest.common.domain.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.stream.Collectors;

@Getter
@AllArgsConstructor
@Builder
public class ServiceDefinition {
    private String code;
    private String tenantId;
    private List<AttributeDefinition> attributes;

    public boolean isComputedFieldsAbsent() {
        return CollectionUtils.isEmpty(getComputedFields());
    }

    public boolean isAttributesAbsent() {
        return CollectionUtils.isEmpty(attributes);
    }

    public List<AttributeDefinition> getComputedFields() {
        return attributes.stream()
            .filter(AttributeDefinition::isReadOnly)
            .collect(Collectors.toList());
    }

    public List<AttributeDefinition> getNonComputedAttributes() {
        return attributes.stream()
            .filter(attribute -> !attribute.isReadOnly())
            .collect(Collectors.toList());
    }

    public List<AttributeDefinition> getMandatoryAttributes() {
        return attributes.stream()
            .filter(AttributeDefinition::isRequired)
            .collect(Collectors.toList());
    }

    public List<AttributeDefinition> getNonComputedDateAttributes() {
        return getAttributesOfType(AttributeDataType.DATE);
    }

    public List<AttributeDefinition> getNonComputedDateTimeAttributes() {
        return getAttributesOfType(AttributeDataType.DATE_TIME);
    }

    public List<AttributeDefinition> getNonComputedIntegerAttributes() {
        return getAttributesOfType(AttributeDataType.INTEGER);
    }

    public List<AttributeDefinition> getNonComputedDoubleAttributes() {
        return getAttributesOfType(AttributeDataType.DOUBLE);
    }

    public List<AttributeDefinition> getSingleValueTypeAttributes() {
        return attributes.stream()
            .filter(a -> a.getDataType() != AttributeDataType.MULTI_VALUE_LIST)
            .collect(Collectors.toList());
    }

    private List<AttributeDefinition> getAttributesOfType(AttributeDataType attributeType) {
        return getNonComputedAttributes().stream()
            .filter(attribute -> attributeType == attribute.getDataType())
            .collect(Collectors.toList());
    }
}

