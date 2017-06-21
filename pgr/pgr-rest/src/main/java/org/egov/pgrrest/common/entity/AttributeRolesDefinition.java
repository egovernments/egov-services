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
@Table(name = "attribute_role_definition")
public class AttributeRolesDefinition {

    @EmbeddedId
    private AttributeRolesDefinitionKey id;

    public String getAttributeCode() {
        return id.getAttributeCode();
    }

    public org.egov.pgrrest.common.model.AttributeRolesDefinition toDomain() {
        return new org.egov.pgrrest.common.model.AttributeRolesDefinition(id.getName());
    }

}
