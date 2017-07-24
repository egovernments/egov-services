package org.egov.pgrrest.read.domain.exception;

import lombok.Getter;

public class UnknownServiceStatusException extends RuntimeException {

    @Getter
    private String unknownStatus;

    public UnknownServiceStatusException(String unknownStatus) {
        this.unknownStatus = unknownStatus;
    }
}
