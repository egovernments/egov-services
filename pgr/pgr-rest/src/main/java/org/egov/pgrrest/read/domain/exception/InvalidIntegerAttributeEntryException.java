package org.egov.pgrrest.read.domain.exception;

import lombok.Getter;

public class InvalidIntegerAttributeEntryException extends RuntimeException {
    @Getter
    private String attributeCode;

    public InvalidIntegerAttributeEntryException(String attributeCode) {
        this.attributeCode = attributeCode;
    }
}
