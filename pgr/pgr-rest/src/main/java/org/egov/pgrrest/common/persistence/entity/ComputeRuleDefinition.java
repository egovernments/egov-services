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
@Table(name = "cs_compute_rule_definition")
public class ComputeRuleDefinition extends AbstractPersistable<ComputeRuleDefinitionKey> {

    @EmbeddedId
    private ComputeRuleDefinitionKey id;

    @Column(name = "rule")
    private String rule;

    @Column(name = "value")
    private String value;

    public org.egov.pgrrest.common.domain.model.ComputeRuleDefinition toDomain() {
        return org.egov.pgrrest.common.domain.model.ComputeRuleDefinition.builder()
            .rule(rule)
            .value(value)
            .name(id.getName())
            .build();
    }

    public String getAttributeCode() {
        return id.getAttributeCode();
    }
}
