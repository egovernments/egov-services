package org.egov.pgrrest.common.persistence.entity;

import lombok.*;

import javax.persistence.Column;
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
@Table(name = "cs_group_definition")
public class GroupDefinition extends AbstractPersistable<GroupDefinitionKey> {

    private static final long serialVersionUID = -1137582812349690198L;

    @EmbeddedId
    private GroupDefinitionKey id;

    @Column(name = "name")
    private String name;

    public String getCode() {
        return id.getCode();
    }

    public org.egov.pgrrest.common.domain.model.GroupDefinition toDomain(
        List<org.egov.pgrrest.common.domain.model.GroupConstraint> groupConstraints) {
        return org.egov.pgrrest.common.domain.model.GroupDefinition.builder()
            .code(getCode())
            .name(name)
            .constraints(groupConstraints)
            .build();
    }

}

