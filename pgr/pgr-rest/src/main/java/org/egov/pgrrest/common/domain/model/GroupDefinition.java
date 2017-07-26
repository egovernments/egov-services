package org.egov.pgrrest.common.domain.model;

import lombok.Builder;
import lombok.Getter;

import java.util.List;
import java.util.stream.Collectors;

@Getter
@Builder
public class GroupDefinition {
    private String name;
    private String code;
    private List<GroupConstraint> constraints;

    public List<GroupConstraint> getMatchingConstraints(ServiceStatus action, List<String> roleCodes) {
        return constraints.stream()
            .filter(constraint -> constraint.isMatching(action, roleCodes))
            .collect(Collectors.toList());
    }

}
