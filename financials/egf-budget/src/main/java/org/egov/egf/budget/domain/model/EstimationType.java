package org.egov.egf.budget.domain.model;

import lombok.ToString;

@ToString
public enum EstimationType {
    BE("BudgetEstimate"), RE("RevisionEstimate");

    private final String name;

    private EstimationType(final String s) {
        name = s;
    }

}
