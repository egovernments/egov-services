package org.egov.pgrrest.common.persistence.entity;

import lombok.*;
import org.egov.pgrrest.common.domain.model.ServiceStatus;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode
@Table(name = "attribute_action_definition")
public class AttributeActionsDefinition extends AbstractPersistable<AttributeActionsDefinitionKey>{

    private static final long serialVersionUID = 2779225241643806540L;
    @EmbeddedId
    private AttributeActionsDefinitionKey id;

    public String getAttributeCode() {
        return id.getAttributeCode();
    }

    public org.egov.pgrrest.common.domain.model.AttributeActionsDefinition toDomain() {
        final ServiceStatus serviceStatus = ServiceStatus.parse(id.getName());
        return new org.egov.pgrrest.common.domain.model.AttributeActionsDefinition(serviceStatus);
    }
}
