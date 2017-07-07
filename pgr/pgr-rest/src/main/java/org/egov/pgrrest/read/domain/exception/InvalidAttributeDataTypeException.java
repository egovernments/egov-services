package org.egov.pgrrest.read.domain.exception;

import lombok.Getter;

public class InvalidAttributeDataTypeException extends RuntimeException {
    @Getter
    private final String invalidDataType;
    @Getter
    private final String attributeCode;

    public InvalidAttributeDataTypeException(String invalidDataType, String attributeCode) {
        this.invalidDataType = invalidDataType;
        this.attributeCode = attributeCode;
    }
}
