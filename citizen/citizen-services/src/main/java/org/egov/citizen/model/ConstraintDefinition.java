package org.egov.citizen.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@AllArgsConstructor
@Builder
public class ConstraintDefinition {
    private String rule;
    private String value;
}
