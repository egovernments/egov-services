package org.egov.pgrrest.common.persistence.entity;

import lombok.*;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "value_definition")
public class ValueDefinition extends AbstractPersistable<ValueDefinitionKey> {
    private static final char YES = 'Y';
    @EmbeddedId
    private ValueDefinitionKey id;

    @Column(name = "servicecode")
    private String serviceCode;

    @Column(name = "name")
    private String name;

    @Column(name = "active")
    private char active;

    public String getAttributeCode() {
        return id.getAttributeCode();
    }

    public org.egov.pgrrest.common.domain.model.ValueDefinition toDomain() {
        return new org.egov.pgrrest.common.domain.model.ValueDefinition(name, id.getKey(), isActive());
    }

    private boolean isActive() {
        return YES == active;
    }
}
