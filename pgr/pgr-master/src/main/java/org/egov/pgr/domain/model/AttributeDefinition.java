package org.egov.pgr.domain.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.List;
import java.util.stream.Collectors;

@Getter
@AllArgsConstructor
@Builder
public class AttributeDefinition {

    private boolean readOnly;
    private String dataType;
    private boolean required;
    private String dataTypeDescription;
    private int order;
    private String description;
    private String code;
    private String url;
    private String groupCode;
    private String tenantId;
    private String serviceCode;
    private List<ValueDefinition> valueDefinitions;

    private char requiredToChar() {
        return required == true ? 'Y' : 'N';
    }

    private char readOnlyToChar() {
        return readOnly == true ? 'Y' : 'N';
    }

    public org.egov.pgr.persistence.dto.AttributeDefinition toDto(ServiceDefinition serviceDefinition){
        return org.egov.pgr.persistence.dto.AttributeDefinition.builder()
                .code(code)
                .tenantId(tenantId)
                .description(description)
                .dataType(dataType)
                .dataTypeDescription(dataTypeDescription)
                .ordernum(order)
                .groupCode(groupCode)
                .serviceCode(serviceCode)
                .url(url)
                .required(requiredToChar())
                .variable(readOnlyToChar())
                .createdBy(serviceDefinition.getCreatedBy())
                .createdDate(serviceDefinition.getCreatedDate())
                .lastModifiedBy(serviceDefinition.getLastModifiedBy())
                .lastModifiedDate(serviceDefinition.getLastModifiedDate())
                .valueDefinitions(mapToValueDefinitionDto(serviceDefinition, code, tenantId, serviceCode))
                .build();
    }

    private List mapToValueDefinitionDto(ServiceDefinition serviceDefinition,
                                         String attributeCode, String tenantId, String serviceCode){
        return valueDefinitions.stream()
                    .map(vd -> vd.toDto(serviceDefinition, attributeCode, tenantId, serviceCode))
                    .collect(Collectors.toList());
    }

}