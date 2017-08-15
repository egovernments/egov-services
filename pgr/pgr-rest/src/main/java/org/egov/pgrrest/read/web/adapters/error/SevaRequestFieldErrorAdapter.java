package org.egov.pgrrest.read.web.adapters.error;

import org.egov.common.contract.response.Error;
import org.egov.common.contract.response.ErrorField;
import org.egov.common.contract.response.ErrorResponse;
import org.springframework.http.HttpStatus;

import java.util.List;

public class SevaRequestFieldErrorAdapter implements ErrorAdapter<List<ErrorField>> {

    private static final String INVALID_SEVA_REQUEST_MESSAGE = "SevaRequest is invalid";

    @Override
    public ErrorResponse adapt(List<ErrorField> errorFields) {
        final Error error = getError(errorFields);
        return new ErrorResponse(null, error);
    }

    private Error getError(List<ErrorField> errorFields) {
        return Error.builder()
            .code(HttpStatus.BAD_REQUEST.value())
            .message(INVALID_SEVA_REQUEST_MESSAGE)
            .fields(errorFields)
            .build();
    }

}
