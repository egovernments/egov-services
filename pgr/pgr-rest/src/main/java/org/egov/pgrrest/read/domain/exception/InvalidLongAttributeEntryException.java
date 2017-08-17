package org.egov.pgrrest.read.domain.exception;

import lombok.Getter;

public class InvalidLongAttributeEntryException extends RuntimeException {
    @Getter
    private String attributeCode;

    public InvalidLongAttributeEntryException(String attributeCode) {
        this.attributeCode = attributeCode;
    }
}
