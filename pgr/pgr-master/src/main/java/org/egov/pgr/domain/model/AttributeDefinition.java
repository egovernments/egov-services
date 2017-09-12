package org.egov.pgr.domain.model;

import static org.springframework.util.StringUtils.isEmpty;

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
    private boolean active;
    private List<ValueDefinition> valueDefinitions;

    private char requiredToChar() {
        return required == true ? 'Y' : 'N';
    }

    private char readOnlyToChar() {
        return readOnly == true ? 'Y' : 'N';
    }

    private char activeToChar(){
        return active ? 'Y' : 'N';
    }

    /* field length validation*/
    public boolean isTenantIdLengthMatch(){
        return (!isEmpty(tenantId) && (tenantId.length() > 0 && tenantId.length() <= 256));
    }
    
    public boolean isCodeLengthMatch(){
        return (!isEmpty(code) && (code.length() > 0 && code.length() <= 50));
    }

    public boolean isDataTypeLengthMatch(){
        return (!isEmpty(dataType) && (dataType.length() > 0 && dataType.length() <= 100));
    }
    
    public boolean isDatatypedescriptionLengthMatch(){
    	if(dataTypeDescription != null){
        return (dataTypeDescription.length() > 0 && dataTypeDescription.length() <= 200);
    	}
    	return true;
    }
    
   public boolean isValueList(){
    	if(!(dataType.equalsIgnoreCase("multivaluelist")|| dataType.equalsIgnoreCase("singlevaluelist")))
	{
    		if(!valueDefinitions.isEmpty())
    		
    		return true;
    	}
    	return false;
    }
    
    public boolean isDescriptionLengthMatch(){
    	if(description != null)
    	{
        return (description.length() > 0 && description.length() <= 300);
    }
    	return true;
	}
    
    public boolean isServicecodeLengthMatch(){
        return (!isEmpty(serviceCode) && (serviceCode.length() > 0 && serviceCode.length() <= 256));
    }
    
    public boolean isUrlLengthMatch(){
    	if(url != null)
    	{
        return (url.length() > 0 && url.length() <= 300);
    	}
    	return true;
    }
    
    public boolean isGroupCodeLengthMatch(){
    	if(groupCode != null)
    	{
    		
        return (groupCode.length() > 0 && groupCode.length() <= 300);
    }
    	return true;
    }
    

    
    /* mandatory validation*/
    public boolean isTenantAbsent()
    {
    	return isEmpty(tenantId) || tenantId == null;  
    }
    
    public boolean isServiceCodeAbsent()
    {
    	return isEmpty(serviceCode) || serviceCode == null;      
    }
    
    public boolean isAttributCodeAbsent()
    {
        return isEmpty(code) || code == null;
    }
    
    public boolean isDatatypeAbsent()
    {
    	return isEmpty(dataType) || dataType == null;      
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
                .active(activeToChar())
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