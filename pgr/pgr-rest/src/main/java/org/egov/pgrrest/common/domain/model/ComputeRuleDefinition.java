package org.egov.pgrrest.common.domain.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@AllArgsConstructor
@Builder
public class ComputeRuleDefinition {
    private String rule;
    private String value;
    private String name;
}
