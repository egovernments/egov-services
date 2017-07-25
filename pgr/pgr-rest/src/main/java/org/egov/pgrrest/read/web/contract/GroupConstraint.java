package org.egov.pgrrest.read.web.contract;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import org.egov.pgrrest.common.domain.model.GroupConstraintType;

@Getter
@AllArgsConstructor
@Builder
public class GroupConstraint {
    private static final String AT_LEAST_ONE_REQUIRED = "AT_LEAST_ONE_REQUIRED";
    private static final String ALL_REQUIRED = "ALL_REQUIRED";

    private String constraint;
    private String action;
    private String role;

    public GroupConstraint(org.egov.pgrrest.common.domain.model.GroupConstraint groupConstraint) {
        this.action = groupConstraint.getAction().getCode();
        this.role = groupConstraint.getRole();
        this.constraint = toContractConstraint(groupConstraint.getConstraintType());
    }

    private String toContractConstraint(GroupConstraintType constraint) {
        if (constraint == GroupConstraintType.AT_LEAST_ONE_REQUIRED) {
            return AT_LEAST_ONE_REQUIRED;
        } else if(constraint == GroupConstraintType.ALL_REQUIRED) {
            return ALL_REQUIRED;
        }
        return null;
    }
}
