package org.egov.pgrrest.read.web.contract;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.List;
import java.util.stream.Collectors;

@Getter
@AllArgsConstructor
@Builder
public class GroupDefinition {
    private String name;
    private String code;
    private List<org.egov.pgrrest.read.web.contract.GroupConstraint> constraints;

    public GroupDefinition(org.egov.pgrrest.common.domain.model.GroupDefinition group) {
        this.name = group.getName();
        this.code = group.getCode();
        this.constraints = toContract(group);
    }

    private List<org.egov.pgrrest.read.web.contract.GroupConstraint> toContract(
        org.egov.pgrrest.common.domain.model.GroupDefinition group) {
        return group.getConstraints().stream()
            .map(org.egov.pgrrest.read.web.contract.GroupConstraint::new)
            .collect(Collectors.toList());
    }

}
