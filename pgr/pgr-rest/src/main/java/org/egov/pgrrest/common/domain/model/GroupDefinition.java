package org.egov.pgrrest.common.domain.model;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class GroupDefinition {
    private String name;
    private String code;
    private List<GroupConstraint> constraints;
}
