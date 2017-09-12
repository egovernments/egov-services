package org.egov.pgrrest.read.domain.exception;

import lombok.Getter;

public class InvalidTextAreaAttributeEntryException extends RuntimeException {
    @Getter
    private String attributeCode;

    public InvalidTextAreaAttributeEntryException(String attributeCode) {
        this.attributeCode = attributeCode;
    }
}
