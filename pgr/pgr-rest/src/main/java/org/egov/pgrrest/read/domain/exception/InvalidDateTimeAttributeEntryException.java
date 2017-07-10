package org.egov.pgrrest.read.domain.exception;

import lombok.Getter;

public class InvalidDateTimeAttributeEntryException extends RuntimeException {
    @Getter
    private String attributeCode;

    public InvalidDateTimeAttributeEntryException(String attributeCode) {
        this.attributeCode = attributeCode;
    }
}

