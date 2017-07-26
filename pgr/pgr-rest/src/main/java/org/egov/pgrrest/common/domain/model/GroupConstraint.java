package org.egov.pgrrest.common.domain.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
@Builder
public class GroupConstraint {
    private GroupConstraintType constraintType;
    private ServiceStatus action;
    private String role;

    public boolean isMatching(ServiceStatus action, List<String> roleCodes) {
        return this.action == action && roleCodes.contains(role);
    }
}
