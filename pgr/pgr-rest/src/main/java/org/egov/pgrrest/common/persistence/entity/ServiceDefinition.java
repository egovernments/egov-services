package org.egov.pgrrest.common.persistence.entity;

import lombok.*;
import org.egov.pgrrest.common.domain.model.AttributeDefinition;
import org.egov.pgrrest.common.domain.model.GroupDefinition;

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
    private static final long serialVersionUID = -5016970327424732948L;

    @EmbeddedId
    private ServiceDefinitionKey id;

    public String getCode() {
        return id.getCode();
    }

    public org.egov.pgrrest.common.domain.model.ServiceDefinition toDomain(
        List<AttributeDefinition> domainAttributes,
        List<GroupDefinition> groups) {
        return org.egov.pgrrest.common.domain.model.ServiceDefinition.builder()
            .attributes(domainAttributes)
            .groups(groups)
            .code(id.getCode())
            .tenantId(id.getTenantId())
            .build();
    }
}

