package org.egov.pgrrest.read.domain.exception;

import lombok.Getter;

public class InvalidDoubleAttributeEntryException extends RuntimeException {
    @Getter
    private String attributeCode;

    public InvalidDoubleAttributeEntryException(String attributeCode) {
        this.attributeCode = attributeCode;
    }
}
