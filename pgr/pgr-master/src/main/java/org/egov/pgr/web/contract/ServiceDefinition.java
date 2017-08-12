package org.egov.pgr.web.contract;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@Builder
@AllArgsConstructor
public class ServiceDefinition {
    private String code;
    private String tenantId;
    private List<AttributeDefinition> attributes;

    public ServiceDefinition(org.egov.pgr.domain.model.ServiceDefinition serviceDefinition, List<org.egov.pgr.domain.model.AttributeDefinition> attributesList) {
        this.code = serviceDefinition.getCode();
        this.tenantId = serviceDefinition.getTenantId();
        this.attributes = mapAttributes(attributesList);
    }

    public List<org.egov.pgr.domain.model.AttributeDefinition> mapToModelAttributes(){
        return attributes.stream()
                .map(attributeDefinition -> attributeDefinition.toDomain(tenantId))
                .collect(Collectors.toList());
    }

    private List<AttributeDefinition> mapAttributes(
            List<org.egov.pgr.domain.model.AttributeDefinition> attributes){
        if (null == attributes) {
            return Collections.emptyList();
        }

        return attributes.stream()
                .map(AttributeDefinition::new)
                .collect(Collectors.toList());
    }
}
