package org.egov.pgrrest.read.web.adapters.error;

import org.egov.common.contract.response.Error;
import org.egov.common.contract.response.ErrorField;
import org.egov.common.contract.response.ErrorResponse;
import org.springframework.http.HttpStatus;

import java.util.ArrayList;
import java.util.List;

public class InvalidcLongAttributeEntryExceptionAdapter implements ErrorAdapter<String> {

    private static final String MESSAGE = "Invalid long data type format.";
    private static final String CODE = "PGR.INVALID_LONG_DATA_TYPE_FORMAT";
    private static final String FIELD_NAME = "serviceRequest.attribValues.name=%s";

    @Override
    public ErrorResponse adapt(String attributeCode) {
        final Error error = getError(attributeCode);
        return new ErrorResponse(null, error);
    }

    private Error getError(String attributeCode) {
        List<ErrorField> errorFields = getErrorFields(attributeCode);
        return Error.builder()
            .code(HttpStatus.BAD_REQUEST.value())
            .message(MESSAGE)
            .fields(errorFields)
            .build();
    }

    private List<ErrorField> getErrorFields(String attributeCode) {
        List<ErrorField> errorFields = new ArrayList<>();
        final ErrorField errorField = ErrorField.builder()
            .code(CODE)
            .message(MESSAGE)
            .field(String.format(FIELD_NAME, attributeCode))
            .build();
        errorFields.add(errorField);
        return errorFields;
    }

}

