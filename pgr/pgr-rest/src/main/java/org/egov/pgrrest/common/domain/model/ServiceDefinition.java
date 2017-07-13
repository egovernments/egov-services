package org.egov.pgrrest.common.domain.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import org.egov.pgrrest.read.domain.model.SevaRequestAction;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.util.CollectionUtils.isEmpty;

@Getter
@AllArgsConstructor
@Builder
public class ServiceDefinition {
    private String code;
    private String tenantId;
    private List<AttributeDefinition> attributes;

    public boolean isComputedFieldsAbsent() {
        return isEmpty(getComputedFields());
    }

    public boolean isAttributesAbsent() {
        return isEmpty(attributes);
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

    public List<AttributeDefinition> getMandatoryAttributes(SevaRequestAction action, List<String> roleCodes) {
        return attributes.stream()
            .filter(attribute -> attribute.isRequired()
                && actionMatches(action, attribute)
                && roleMatches(roleCodes, attribute))
            .collect(Collectors.toList());
    }

    private boolean roleMatches(List<String> roleCodes, AttributeDefinition attribute) {
        return !Collections.disjoint(attribute.getRoleNames(), roleCodes);
    }

    private boolean actionMatches(SevaRequestAction expectedAction, AttributeDefinition attributeDefinition) {
        return attributeDefinition.getActions().stream()
            .anyMatch(a -> expectedAction.getActionName().equalsIgnoreCase(a.getName()));
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

