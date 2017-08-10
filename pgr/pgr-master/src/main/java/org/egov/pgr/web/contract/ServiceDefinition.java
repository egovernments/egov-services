package org.egov.pgr.web.contract;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.List;
import java.util.stream.Collectors;

@Getter
@AllArgsConstructor
@Builder
public class ServiceDefinition {
    private String code;
    private String tenantId;
    private List<AttributeDefinition> attributes;


    public List<org.egov.pgr.domain.model.AttributeDefinition> mapToModelAttributes(){
        return attributes.stream()
                .map(attributeDefinition -> attributeDefinition.toDomain(tenantId))
                .collect(Collectors.toList());
    }
}
