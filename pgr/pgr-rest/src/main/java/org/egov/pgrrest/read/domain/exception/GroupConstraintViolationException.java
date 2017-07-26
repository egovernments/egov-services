package org.egov.pgrrest.read.domain.exception;

import lombok.Getter;

public class GroupConstraintViolationException extends RuntimeException {

    @Getter
    private String groupCode;

    public GroupConstraintViolationException(String groupCode) {
        this.groupCode = groupCode;
    }
}
