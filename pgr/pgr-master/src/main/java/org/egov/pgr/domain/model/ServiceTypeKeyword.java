package org.egov.pgr.domain.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ServiceTypeKeyword {

    private String servicecode;
    private String tenantId;
    private String keyword;

    public org.egov.pgr.persistence.dto.ServiceTypeKeyword toDto(ServiceType serviceType){
        return  org.egov.pgr.persistence.dto.ServiceTypeKeyword.builder()
                    .servicecode(servicecode)
                    .tenantId(tenantId)
                    .keyword(keyword)
                    .createdBy(serviceType.getCreatedBy())
                    .createdDate(serviceType.getCreatedDate())
                    .lastModifiedBy(serviceType.getLastModifiedBy())
                    .lastModifiedDate(serviceType.getLastModifiedDate())
                    .build();
    }
}