package org.egov.pgrrest.common.entity;

import lombok.*;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "attribute_action_definition")
public class AttributeActionsDefinition {

    @EmbeddedId
    private AttributeActionsDefinitionKey id;

    public String getAttributeCode() {
        return id.getAttributeCode();
    }

    public org.egov.pgrrest.common.model.AttributeActionsDefinition toDomain() {
        return new org.egov.pgrrest.common.model.AttributeActionsDefinition(id.getName());
    }
}
