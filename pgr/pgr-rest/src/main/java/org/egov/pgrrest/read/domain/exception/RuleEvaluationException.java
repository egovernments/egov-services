package org.egov.pgrrest.read.domain.exception;

import lombok.Getter;
import org.egov.pgrrest.common.domain.model.ComputeRuleDefinition;

public class RuleEvaluationException extends RuntimeException {
    @Getter
    private ComputeRuleDefinition rule;

    public RuleEvaluationException(ComputeRuleDefinition rule) {
        this.rule = rule;
    }
}
