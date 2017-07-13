package org.egov.pgrrest.read.domain.exception;

import lombok.Getter;

public class InvalidAttributeDataTypeException extends RuntimeException {
    private static final String EXCEPTION_MESSAGE = "Invalid data type '%s' for attribute definition '%s'";
    @Getter
    private final String invalidDataType;
    @Getter
    private final String attributeCode;

    public InvalidAttributeDataTypeException(String invalidDataType, String attributeCode) {
        super(String.format(EXCEPTION_MESSAGE, invalidDataType, attributeCode));
        this.invalidDataType = invalidDataType;
        this.attributeCode = attributeCode;
    }
}
