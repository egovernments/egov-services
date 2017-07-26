package org.egov.pgrrest.common.domain.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@AllArgsConstructor
@Builder
public class GroupConstraint {
    private GroupConstraintType constraintType;
    private ServiceStatus action;
    private String role;
}
