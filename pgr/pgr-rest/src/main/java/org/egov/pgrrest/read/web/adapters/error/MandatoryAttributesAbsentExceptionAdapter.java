package org.egov.pgrrest.read.web.adapters.error;

import org.egov.common.contract.response.Error;
import org.egov.common.contract.response.ErrorField;
import org.egov.common.contract.response.ErrorResponse;

import java.util.List;
import java.util.stream.Collectors;


public class MandatoryAttributesAbsentExceptionAdapter implements ErrorAdapter<List<String>> {

    private static final int HTTP_CLIENT_ERROR_CODE = 400;
    private static final String ERROR_CODE = "PGR.MANDATORY_ATTRIBUTE_ENTRY_MISSING";
    private static final String ERROR_FIELD = "sevaRequest.serviceRequest.attribValues=%s";
    private static final String ERROR_MESSAGE = "Mandatory attrib value entry is not present";

    @Override
    public ErrorResponse adapt(List<String> missingAttributeCodes) {
        return new ErrorResponse(null, getError(missingAttributeCodes));
    }

    private Error getError(List<String> missingAttributeCodes) {
        final List<ErrorField> fields = missingAttributeCodes.stream()
            .map(this::getErrorField)
            .collect(Collectors.toList());

        return Error.builder()
            .code(HTTP_CLIENT_ERROR_CODE)
            .message(ERROR_MESSAGE)
            .fields(fields)
            .build();
    }

    private ErrorField getErrorField(String fieldCode) {
        return ErrorField.builder()
            .code(ERROR_CODE)
            .field(String.format(ERROR_FIELD, fieldCode))
            .message(ERROR_MESSAGE)
            .build();
    }
}

