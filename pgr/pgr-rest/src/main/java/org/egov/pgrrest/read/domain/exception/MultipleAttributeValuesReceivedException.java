package org.egov.pgrrest.read.domain.exception;

import lombok.Getter;

public class MultipleAttributeValuesReceivedException extends RuntimeException {
    @Getter
    private String attributeCode;

    public MultipleAttributeValuesReceivedException(String attributeCode) {
        this.attributeCode = attributeCode;
    }
}
