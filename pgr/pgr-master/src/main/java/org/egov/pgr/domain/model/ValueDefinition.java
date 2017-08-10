package org.egov.pgr.domain.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@AllArgsConstructor
@Builder
public class ValueDefinition {
    private String name;
    private String key;
    private boolean active;
    private String serviceCode;

    private char activeToChar(){
        return active ? 'Y' : 'N';
    }

    public org.egov.pgr.persistence.dto.ValueDefinition toDto(ServiceDefinition serviceDefinition,
                                                              String attributeCode, String tenantId, String serviceCode){
        return org.egov.pgr.persistence.dto.ValueDefinition.builder()
                    .name(name)
                    .key(key)
                    .serviceCode(serviceCode)
                    .tenantId(tenantId)
                    .attributeCode(attributeCode)
                    .createdBy(serviceDefinition.getCreatedBy())
                    .createdDate(serviceDefinition.getCreatedDate())
                    .lastModifiedBy(serviceDefinition.getLastModifiedBy())
                    .lastModifiedDate(serviceDefinition.getLastModifiedDate())
                    .active(activeToChar())
                    .build();
    }
}
