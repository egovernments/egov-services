package org.egov.pgrrest.read.domain.exception;

import lombok.Getter;
import org.egov.pgr.common.model.OtpRequest;

public class InvalidOtpRequestException extends RuntimeException {
    @Getter
    private OtpRequest otpRequest;

    public InvalidOtpRequestException(OtpRequest otpRequest) {
        this.otpRequest = otpRequest;
    }
}

