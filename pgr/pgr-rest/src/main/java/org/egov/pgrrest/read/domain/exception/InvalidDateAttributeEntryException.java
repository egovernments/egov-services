package org.egov.pgrrest.read.domain.exception;

import lombok.Getter;

public class InvalidDateAttributeEntryException extends RuntimeException {
    @Getter
    private String attributeCode;

    public InvalidDateAttributeEntryException(String attributeCode) {
        this.attributeCode = attributeCode;
    }
}

