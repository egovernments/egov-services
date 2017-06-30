package org.egov.pgrrest.read.web.adapters.error;

import org.egov.pgrrest.read.web.contract.Error;
import org.egov.pgrrest.read.web.contract.ErrorField;
import org.egov.pgrrest.read.web.contract.ErrorResponse;
import org.springframework.http.HttpStatus;

import java.util.ArrayList;
import java.util.List;

public class InvalidAttributeEntryExceptionAdapter implements ErrorAdapter<String> {

    private static final String MESSAGE = "attribValues entry is invalid";
    private static final String CODE = "PGR.INVALID_ATTRIBUTE_VALUE_ENTRY";

    @Override
    public ErrorResponse adapt(String fieldName) {
        final Error error = getError(fieldName);
        return new ErrorResponse(null, error);
    }

    private Error getError(String fieldName) {
        List<ErrorField> errorFields = getErrorFields(fieldName);
        return Error.builder()
            .code(HttpStatus.BAD_REQUEST.value())
            .message(MESSAGE)
            .fields(errorFields)
            .build();
    }

    private List<ErrorField> getErrorFields(String fieldName) {
        List<ErrorField> errorFields = new ArrayList<>();
        final ErrorField errorField = ErrorField.builder()
            .code(CODE)
            .message(MESSAGE)
            .field(fieldName)
            .build();
        errorFields.add(errorField);
        return errorFields;
    }

}
