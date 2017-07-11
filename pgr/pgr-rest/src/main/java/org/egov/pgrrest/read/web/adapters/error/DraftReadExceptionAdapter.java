package org.egov.pgrrest.read.web.adapters.error;

import org.egov.common.contract.response.Error;
import org.egov.common.contract.response.ErrorField;
import org.egov.common.contract.response.ErrorResponse;

import java.util.Collections;
import java.util.List;


public class DraftReadExceptionAdapter implements ErrorAdapter<Void> {

    private static final int HTTP_CLIENT_ERROR_CODE = 400;
    private static final String DRAFT_READ_EXCEPTION = "DRAFT_READ_EXCEPTION";
    private static final String DRAFT_READ_EXCEPTION_FIELD = "draft";
    private static final String DRAFT_READ__EXCEPTION_MESSAGE =
        "DRAFT READ EXCEPTION";

    @Override
    public ErrorResponse adapt(Void model) {
        return new ErrorResponse(null, getError());
    }

    private Error getError() {
        final List<ErrorField> fields = Collections.singletonList(getErrorField());
        return Error.builder()
            .code(HTTP_CLIENT_ERROR_CODE)
            .message(DRAFT_READ__EXCEPTION_MESSAGE)
            .fields(fields)
            .build();
    }

    private ErrorField getErrorField() {
        return ErrorField.builder()
            .code(DRAFT_READ_EXCEPTION)
            .field(DRAFT_READ_EXCEPTION_FIELD)
            .message(DRAFT_READ_EXCEPTION_FIELD)
            .build();
    }
}
