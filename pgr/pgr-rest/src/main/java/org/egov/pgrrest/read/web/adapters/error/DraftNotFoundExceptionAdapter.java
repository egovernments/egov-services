package org.egov.pgrrest.read.web.adapters.error;

import org.egov.common.contract.response.Error;
import org.egov.common.contract.response.ErrorField;
import org.egov.common.contract.response.ErrorResponse;

import java.util.Collections;
import java.util.List;


public class DraftNotFoundExceptionAdapter implements ErrorAdapter<Void> {

    private static final int HTTP_CLIENT_ERROR_CODE = 500;
    private static final String DRAFT_NOT_FOUND_EXCEPTION = "DRAFT_NOT_FOUND";
    private static final String DRAFT_NOT_FOUND_EXCEPTION_FIELD = "draft";
    private static final String DRAFT_NOT_FOUND_EXCEPTION_MESSAGE =
        "DRAFT NOT FOUND";

    @Override
    public ErrorResponse adapt(Void model) {
        return new ErrorResponse(null, getError());
    }

    private Error getError() {
        final List<ErrorField> fields = Collections.singletonList(getErrorField());
        return Error.builder()
            .code(HTTP_CLIENT_ERROR_CODE)
            .message(DRAFT_NOT_FOUND_EXCEPTION_MESSAGE)
            .fields(fields)
            .build();
    }

    private ErrorField getErrorField() {
        return ErrorField.builder()
            .code(DRAFT_NOT_FOUND_EXCEPTION)
            .field(DRAFT_NOT_FOUND_EXCEPTION_FIELD)
            .message(DRAFT_NOT_FOUND_EXCEPTION_MESSAGE)
            .build();
    }
}
