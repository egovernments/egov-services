package org.egov.pgrrest.read.web.adapters.error;

import org.egov.common.contract.response.Error;
import org.egov.common.contract.response.ErrorField;
import org.egov.common.contract.response.ErrorResponse;

import java.util.Collections;
import java.util.List;


public class ReadExceptionAdapter implements ErrorAdapter<Void> {

    private static final int HTTP_CLIENT_ERROR_CODE = 400;
    private static final String READ_EXCEPTION = "READ_EXCEPTION";
    private static final String READ_EXCEPTION_FIELD = "esJson";
    private static final String READ__EXCEPTION_MESSAGE = "READ EXCEPTION";

    @Override
    public ErrorResponse adapt(Void model) {
        return new ErrorResponse(null, getError());
    }

    private Error getError() {
        final List<ErrorField> fields = Collections.singletonList(getErrorField());
        return Error.builder()
            .code(HTTP_CLIENT_ERROR_CODE)
            .message(READ__EXCEPTION_MESSAGE)
            .fields(fields)
            .build();
    }

    private ErrorField getErrorField() {
        return ErrorField.builder()
            .code(READ_EXCEPTION)
            .field(READ_EXCEPTION_FIELD)
            .message(READ_EXCEPTION_FIELD)
            .build();
    }
}
