package org.egov.pgrrest.read.domain.exception;

import lombok.Getter;

public class InvalidServiceTypeCodeException extends RuntimeException {
    @Getter
    private String invalidServiceTypeCode;

    public InvalidServiceTypeCodeException(String invalidServiceTypeCode) {
        this.invalidServiceTypeCode = invalidServiceTypeCode;
    }
}
