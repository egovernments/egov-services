package org.egov.pgrrest.read.web.adapters.error;

import org.egov.common.contract.response.Error;
import org.egov.common.contract.response.ErrorField;
import org.egov.common.contract.response.ErrorResponse;

import java.util.Collections;
import java.util.List;


public class MalformedDraftExceptionAdapter implements ErrorAdapter<Void> {

    private static final int HTTP_CLIENT_ERROR_CODE = 500;
    private static final String MALFORMED_DRAFT_EXCEPTION = "MALFORMED_DRAFT_EXCEPTION";
    private static final String MALFORMED_DRAFT_EXCEPTION_FIELD = "draft";
    private static final String MALFORMED_DRAFT_EXCEPTION_MESSAGE =
        "MALFORMED DRAFT";

    @Override
    public ErrorResponse adapt(Void model) {
        return new ErrorResponse(null,getError());
    }

    private Error getError() {
        final List<ErrorField> fields = Collections.singletonList(getErrorField());
        return Error.builder()
            .code(HTTP_CLIENT_ERROR_CODE)
            .message(MALFORMED_DRAFT_EXCEPTION_MESSAGE)
            .fields(fields)
            .build();
    }

    private ErrorField getErrorField() {
        return ErrorField.builder()
            .code(MALFORMED_DRAFT_EXCEPTION)
            .field(MALFORMED_DRAFT_EXCEPTION_FIELD)
            .message(MALFORMED_DRAFT_EXCEPTION_MESSAGE)
            .build();
    }
}
