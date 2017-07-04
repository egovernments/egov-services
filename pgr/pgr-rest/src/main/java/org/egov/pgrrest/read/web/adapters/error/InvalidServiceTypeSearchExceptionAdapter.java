package org.egov.pgrrest.read.web.adapters.error;

import org.egov.common.contract.response.Error;
import org.egov.common.contract.response.ErrorField;
import org.egov.common.contract.response.ErrorResponse;
import org.springframework.http.HttpStatus;

import java.util.ArrayList;
import java.util.List;

public class InvalidServiceTypeSearchExceptionAdapter implements ErrorAdapter<String> {

    private static final String OTP_VALIDATION_NOT_COMPLETE_MESSAGE =
        "Invalid service type search.";
    private static final String ERROR_CODE = "PGR.INVALID_SERVICE_TYPE_SEARCH";
    private static final String FIELD_NAME = "PGR.OTP_VALIDATION_REQUEST_INVALID";

    @Override
    public ErrorResponse adapt(String errorMessage) {
        final Error error = getError(errorMessage);
        return new ErrorResponse(null, error);
    }

    private Error getError(String errorMessage) {
        List<ErrorField> errorFields = getErrorFields(errorMessage);
        return Error.builder()
            .code(HttpStatus.BAD_REQUEST.value())
            .message(OTP_VALIDATION_NOT_COMPLETE_MESSAGE)
            .fields(errorFields)
            .build();
    }

    private List<ErrorField> getErrorFields(String errorMessage) {
        List<ErrorField> errorFields = new ArrayList<>();
        final ErrorField errorField = ErrorField.builder()
            .code(ERROR_CODE)
            .message(errorMessage)
            .field(FIELD_NAME)
            .build();
        errorFields.add(errorField);
        return errorFields;
    }

}
