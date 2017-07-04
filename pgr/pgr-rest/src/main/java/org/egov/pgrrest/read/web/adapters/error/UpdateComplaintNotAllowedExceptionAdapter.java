package org.egov.pgrrest.read.web.adapters.error;

import org.egov.common.contract.response.Error;
import org.egov.common.contract.response.ErrorField;
import org.egov.common.contract.response.ErrorResponse;

import java.util.Collections;

public class UpdateComplaintNotAllowedExceptionAdapter implements ErrorAdapter<Void> {

    private static final int HTTP_CLIENT_ERROR_CODE = 400;
    private static final String UPDATE_COMPLAINT = "PGR.UPDATE_NOT_ALLOWED";
    private static final String UPDATE_COMPLAINT_FIELD = "PGR.UPDATE_NOT_ALLOWED";
    private static final String UPDATE_COMPLAINT_MESSAGE =
        "YOU ARE NOT ALLOWED TO UPDATE THIS COMPLAINT";

    @Override
    public ErrorResponse adapt(Void model) {
        return new ErrorResponse(null, getError());
    }

    private Error getError() {
        return Error.builder()
            .code(HTTP_CLIENT_ERROR_CODE)
            .message(UPDATE_COMPLAINT_MESSAGE)
            .fields(Collections.singletonList(getErrorField()))
            .build();
    }

    private ErrorField getErrorField() {
        return ErrorField.builder()
            .code(UPDATE_COMPLAINT)
            .field(UPDATE_COMPLAINT_FIELD)
            .message(UPDATE_COMPLAINT_MESSAGE)
            .build();
    }
}
