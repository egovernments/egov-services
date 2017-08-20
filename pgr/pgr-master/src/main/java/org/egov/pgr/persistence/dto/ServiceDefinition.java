package org.egov.pgr.persistence.dto;

import java.util.Date;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Builder
@AllArgsConstructor
@Getter
@Setter
@NoArgsConstructor
public class ServiceDefinition {

    private String code;
    private String tenantId;
    private Long createdBy;
    private Date createdDate;
    private Long lastModifiedBy;
    private Date lastModifiedDate;

    
    public org.egov.pgr.domain.model.ServiceDefinition toDomain(){
        return org.egov.pgr.domain.model.ServiceDefinition.builder()
                .code(code)
                .tenantId(tenantId)
                .createdBy(createdBy)
                .createdDate(createdDate)
                .build();
    }
}
