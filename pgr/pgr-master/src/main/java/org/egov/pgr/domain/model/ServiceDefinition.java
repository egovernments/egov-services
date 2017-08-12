package org.egov.pgr.domain.model;

import lombok.Builder;
import lombok.Getter;

import static org.springframework.util.StringUtils.isEmpty;

import java.util.Date;
import java.util.List;

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

    public org.egov.pgr.persistence.dto.ServiceDefinition toDto(){
        return org.egov.pgr.persistence.dto.ServiceDefinition.builder()
                    .code(code.trim())
                    .tenantId(tenantId)
                    .createdBy(createdBy)
                    .createdDate(createdDate)
                    .build();
    }
    
    public boolean isTenantIdAbsent(){
        return isEmpty(tenantId) || tenantId == null;
    }
    
    public boolean isCodeAbsent(){
        return isEmpty(code) || code == null;
    }
}
