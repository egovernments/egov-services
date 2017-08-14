package org.egov.pgr.domain.model;

import java.util.List;
import java.util.stream.Collectors;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@AllArgsConstructor
@Builder
public class AttributeDefinition {

    private boolean readOnly;
    private String dataType;
    private boolean required;
    private String dataTypeDescription;
    private Integer order;
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
    

    
    /* field length validation*/
    public boolean isTenantIdLengthMatch(){
        return (tenantId.length() > 0 && tenantId.length() <= 256);
    }
    
    public boolean isCodeLengthMatch(){
        return (code.length() > 0 && code.length() <= 50);
    }

    public boolean isDataTypeLengthMatch(){
        return (code.length() > 0 && code.length() <= 100);
    }
    
    public boolean isDatatypedescriptionLengthMatch(){
        return (code.length() > 0 && code.length() <= 200);
    }
    
    public boolean isDescriptionLengthMatch(){
        return (code.length() > 0 && code.length() <= 300);
    }
    
    public boolean isServicecodeLengthMatch(){
        return (serviceCode.length() > 0 && serviceCode.length() <= 256);
    }
    
    public boolean isUrlLengthMatch(){
        return (url.length() > 0 && url.length() <= 300);
    }
    
    public boolean isGroupCodeLengthMatch(){
        return (groupCode.length() > 0 && groupCode.length() <= 300);
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