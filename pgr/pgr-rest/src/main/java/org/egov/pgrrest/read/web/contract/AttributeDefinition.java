package org.egov.pgrrest.read.web.contract;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import org.egov.pgrrest.common.domain.model.ValueDefinition;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@AllArgsConstructor
@Builder
public class AttributeDefinition {
    private boolean variable;
    private String code;
    private String dataType;
    private boolean required;
    private String dataTypeDescription;
    private String description;
    private String url;
    private String groupCode;
    private List<String> roles;
    private List<String> actions;
    private List<ConstraintDefinition> constraints;
    private List<AttributeValueDefinition> attribValues;

    public AttributeDefinition(org.egov.pgrrest.common.domain.model.AttributeDefinition attributeDefinition) {
        this.variable = !attributeDefinition.isReadOnly();
        this.code = attributeDefinition.getCode();
        this.dataType = attributeDefinition.getDataType().getName();
        this.required = attributeDefinition.isRequired();
        this.dataTypeDescription = attributeDefinition.getDataTypeDescription();
        this.description = attributeDefinition.getDescription();
        this.url = attributeDefinition.getUrl();
        this.groupCode = attributeDefinition.getGroupCode();
        this.roles = attributeDefinition.getRoleNames();
        this.actions = attributeDefinition.getActionNames();
        this.attribValues = mapAttributeValues(attributeDefinition.getValues());
    }

    private List<AttributeValueDefinition> mapAttributeValues(List<ValueDefinition> values) {
        if (values == null) {
            return Collections.emptyList();
        }
        return values.stream()
            .map(AttributeValueDefinition::new)
            .collect(Collectors.toList());
    }
}

