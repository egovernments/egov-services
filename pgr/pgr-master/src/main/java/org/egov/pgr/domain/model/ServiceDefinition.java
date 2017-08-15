package org.egov.pgr.domain.model;

import static org.springframework.util.StringUtils.isEmpty;

import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class ServiceDefinition {

    private String code;
    private String tenantId;
    private Long createdBy;
    private Date createdDate;
    private Long lastModifiedBy;
    private Date lastModifiedDate;
    private List<AttributeDefinition> attributes;

    public void setAttributes(List<AttributeDefinition> attributes) {
        this.attributes = attributes;
    }

    public org.egov.pgr.persistence.dto.ServiceDefinition toDto(){
        return org.egov.pgr.persistence.dto.ServiceDefinition.builder()
                    .code(code.trim())
                    .tenantId(tenantId)
                    .createdBy(createdBy)
                    .createdDate(createdDate)
                    .build();
    }
     
    
    public boolean isTenantIdLengthMatch(){
        return (!isEmpty(tenantId) && (tenantId.length() > 0 && tenantId.length() <= 256));
    }
    
    public boolean isCodeLengthMatch(){
        return (!isEmpty(code) && (code.length() > 0 && code.length() <= 20));
    }
    
    public boolean isTenantIdAbsent(){
        return isEmpty(tenantId) || tenantId == null;
    }
    
    public boolean isCodeAbsent(){
        return isEmpty(code) || code == null;
    }
    
    
    public boolean valueDefinMandatoryFieldValidation(ServiceDefinition serviceDefinition) {
		List<String> attributeServiceCodes = serviceDefinition.getAttributes().stream().map(attribute -> attribute.getServiceCode())
		.collect(Collectors.toList());
		List<String> serviceDefinitionCode = Collections.singletonList(code);
		return !attributeServiceCodes.stream().allMatch(serviceDefinitionCode::contains);
				
		
    }
}
