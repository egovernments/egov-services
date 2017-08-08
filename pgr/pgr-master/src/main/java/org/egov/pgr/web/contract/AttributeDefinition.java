package org.egov.pgr.web.contract;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

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

    public AttributeDefinition(org.egov.pgr.domain.model.AttributeDefinition attributeDefinition) {
        this.variable = !attributeDefinition.isReadOnly();
        this.code = attributeDefinition.getCode();
        this.dataType = attributeDefinition.getDataType();
        this.required = attributeDefinition.isRequired();
        this.dataTypeDescription = attributeDefinition.getDataTypeDescription();
        this.description = attributeDefinition.getDescription();
        this.url = attributeDefinition.getUrl();
        this.groupCode = attributeDefinition.getGroupCode();
    }
}
