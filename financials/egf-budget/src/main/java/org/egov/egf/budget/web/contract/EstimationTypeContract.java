package org.egov.egf.budget.web.contract;

import lombok.ToString;

@ToString
public enum EstimationTypeContract {
    BE("BudgetEstimate"), RE("RevisionEstimate");

    private final String name;

    private EstimationTypeContract(final String s) {
        name = s;
    }

}
