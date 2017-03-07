package org.egov.web.adapter.error;

import org.egov.domain.model.Token;
import org.egov.web.contract.Error;
import org.egov.web.contract.ErrorResponse;
import org.springframework.http.HttpStatus;

import java.util.Collections;

public class TokenUpdateErrorAdapter implements ErrorAdapter<Token> {

    private static final String OTP_UPDATE_FAILURE_EXCEPTION = "OTP update unsuccessful";

    @Override
    public ErrorResponse adapt(Token model) {
        final Error error = Error.builder()
                .code(HttpStatus.INTERNAL_SERVER_ERROR.value())
                .message(OTP_UPDATE_FAILURE_EXCEPTION)
                .fields(Collections.emptyList())
                .build();
        return new ErrorResponse(null, error);
    }
}
