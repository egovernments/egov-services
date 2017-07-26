package org.egov.pgrrest.common.persistence.entity;

import lombok.*;
import org.egov.pgrrest.common.domain.model.GroupConstraintType;
import org.egov.pgrrest.common.domain.model.ServiceStatus;
import org.egov.pgrrest.common.domain.model.exception.UnknownGroupConstraintException;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import java.util.stream.Stream;

import static org.springframework.util.StringUtils.isEmpty;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "group_constraint")
public class GroupConstraint extends AbstractPersistable<Long> {

    private static final long serialVersionUID = -5311591502402318660L;
    @Id
    private Long id;

    @Column(name = "groupcode")
    private String groupCode;

    @Column(name = "tenantid")
    private String tenantId;

    @Column(name = "servicecode")
    private String serviceCode;

    @Column(name = "constrainttype")
    private String constraint;

    @Column(name = "action")
    private String action;

    @Column(name = "role")
    private String role;

    public org.egov.pgrrest.common.domain.model.GroupConstraint toDomain() {
        return org.egov.pgrrest.common.domain.model.GroupConstraint.builder()
            .constraintType(toDomainConstraintType(action))
            .role(role)
            .action(ServiceStatus.parse(action))
            .build();
    }

    private GroupConstraintType toDomainConstraintType(String constraint) {
        if (isEmpty(constraint)) {
            return null;
        }
        return Stream.of(GroupConstraintType.values())
            .filter(c -> c.getCode().equals(constraint))
            .findFirst()
            .orElseThrow(() -> new UnknownGroupConstraintException(constraint));
    }


}
