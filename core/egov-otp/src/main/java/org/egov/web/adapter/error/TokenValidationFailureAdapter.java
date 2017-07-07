package org.egov.web.adapter.error;

import org.egov.domain.model.Token;
import org.egov.web.contract.Error;
import org.egov.web.contract.ErrorResponse;
import org.springframework.http.HttpStatus;

import java.util.Collections;

public class TokenValidationFailureAdapter implements ErrorAdapter<Token> {

    private static final String OTP_VALIDATION_FAILED_MESSAGE = "OTP validation unsuccessful";

    @Override
    public ErrorResponse adapt(Token model) {
        final Error error = Error.builder()
                .code(HttpStatus.BAD_REQUEST.value())
                .message(OTP_VALIDATION_FAILED_MESSAGE)
                .fields(Collections.emptyList())
                .build();
        return new ErrorResponse(null, error);
    }
}
