package org.egov.pgrrest.common.entity;

import lombok.*;
import org.egov.pgrrest.common.model.AttributeDefinition;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "service_definition")
public class ServiceDefinition extends AbstractPersistable<ServiceDefinitionKey> {
    @EmbeddedId
    private ServiceDefinitionKey id;

    public org.egov.pgrrest.common.model.ServiceDefinition toDomain(List<AttributeDefinition> domainAttributes) {
        return org.egov.pgrrest.common.model.ServiceDefinition.builder()
            .attributes(domainAttributes)
            .code(id.getCode())
            .tenantId(id.getTenantId())
            .build();
    }
}

