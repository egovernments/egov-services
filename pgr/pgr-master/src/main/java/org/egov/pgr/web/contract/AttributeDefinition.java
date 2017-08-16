package org.egov.pgr.web.contract;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import org.egov.pgr.domain.model.ValueDefinition;

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
    private Integer order;
    private String serviceCode;
    private boolean active;
    private List<AttributeValueDefinition> attribValues;

    public AttributeDefinition(org.egov.pgr.domain.model.AttributeDefinition attributeDefinition) {
        this.variable = attributeDefinition.isReadOnly();
        this.code = attributeDefinition.getCode();
        this.dataType = attributeDefinition.getDataType();
        this.required = attributeDefinition.isRequired();
        this.dataTypeDescription = attributeDefinition.getDataTypeDescription();
        this.description = attributeDefinition.getDescription();
        this.url = attributeDefinition.getUrl();
        this.groupCode = attributeDefinition.getGroupCode();
        this.order = attributeDefinition.getOrder();
        this.active = attributeDefinition.isActive();
        this.attribValues = mapAttributeValues(attributeDefinition.getValueDefinitions());
    }

    public org.egov.pgr.domain.model.AttributeDefinition toDomain(String tenantId){
        return org.egov.pgr.domain.model.AttributeDefinition.builder()
                    .readOnly(variable)
                    .description(description)
                    .dataTypeDescription(dataTypeDescription)
                    .dataType(dataType)
                    .code(code)
                    .groupCode(groupCode)
                    .required(required)
                    .order(order)
                    .url(url)
                    .serviceCode(serviceCode)
                    .tenantId(tenantId)
                    .valueDefinitions(mapAttribValuesToDomainValues())
                    .build();
    }

    private List<ValueDefinition> mapAttribValuesToDomainValues(){
        return attribValues.stream()
                    .map(AttributeValueDefinition::toDomain)
                    .collect(Collectors.toList());
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