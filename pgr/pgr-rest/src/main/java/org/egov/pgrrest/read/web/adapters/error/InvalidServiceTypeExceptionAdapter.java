package org.egov.pgrrest.read.web.adapters.error;

import org.egov.common.contract.response.Error;
import org.egov.common.contract.response.ErrorField;
import org.egov.common.contract.response.ErrorResponse;

import java.util.Collections;
import java.util.List;

public class InvalidServiceTypeExceptionAdapter implements ErrorAdapter<String> {

    private static final int HTTP_CLIENT_ERROR_CODE = 400;
    private static final String INVALID_SERVICE_TYPE_CODE = "PGR.INVALID_SERVICE_TYPE_CODE";
    private static final String SERVICE_CODE_FIELD = "serviceCode";
    private static final String SERVICE_TYPE_NOT_FOUND_MESSAGE =
        "Service code '%s' is invalid for given tenant";

    @Override
    public ErrorResponse adapt(String invalidServiceTypeCode) {
        return new ErrorResponse(null, getError(invalidServiceTypeCode));
    }

    private Error getError(String invalidServiceTypeCode) {
        final List<ErrorField> fields = Collections.singletonList(getErrorField(invalidServiceTypeCode));
        return Error.builder()
            .code(HTTP_CLIENT_ERROR_CODE)
            .message(getMessage(invalidServiceTypeCode))
            .fields(fields)
            .build();
    }

    private String getMessage(String invalidServiceTypeCode) {
        return String.format(SERVICE_TYPE_NOT_FOUND_MESSAGE, invalidServiceTypeCode);
    }

    private ErrorField getErrorField(String invalidServiceTypeCode) {
        return ErrorField.builder()
            .code(INVALID_SERVICE_TYPE_CODE)
            .field(SERVICE_CODE_FIELD)
            .message(getMessage(invalidServiceTypeCode))
            .build();
    }
}
