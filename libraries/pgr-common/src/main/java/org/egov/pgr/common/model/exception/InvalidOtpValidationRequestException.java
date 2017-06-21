package org.egov.pgr.common.model.exception;

import lombok.Getter;
import org.egov.pgr.common.model.OtpValidationRequest;

public class InvalidOtpValidationRequestException extends RuntimeException {

    @Getter
    private OtpValidationRequest request;

    public InvalidOtpValidationRequestException(OtpValidationRequest request) {
        this.request = request;
    }

}
