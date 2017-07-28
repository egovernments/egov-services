package org.egov.pgrrest.common.domain.model.exception;

import lombok.Getter;

public class UnknownGroupConstraintException extends RuntimeException {
    @Getter
    private String unknownGroupConstraint;

    public UnknownGroupConstraintException(String unknownGroupConstraint) {
        this.unknownGroupConstraint = unknownGroupConstraint;
    }
}
