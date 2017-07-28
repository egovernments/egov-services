package org.egov.pgrrest.read.web.adapters.error;

import org.egov.common.contract.response.Error;
import org.egov.common.contract.response.ErrorField;
import org.egov.common.contract.response.ErrorResponse;

import java.util.Collections;

public class UnknownServiceStatusExceptionAdapter implements ErrorAdapter<String> {

    private static final int HTTP_CLIENT_ERROR_CODE = 400;
    private static final String ERROR_CODE = "PGR.SERVICE_STATUS_UNKNOWN";
    private static final String ERROR_FIELD = "sevaRequest.serviceRequest.attribValues=systemStatus";
    private static final String ERROR_MESSAGE = "Service status: '%s' is unknown";

    @Override
    public ErrorResponse adapt(String unknownStatus) {
        return new ErrorResponse(null, getError(unknownStatus));
    }

    private Error getError(String unknownStatus) {
        final ErrorField field = ErrorField.builder()
            .code(ERROR_CODE)
            .field(ERROR_FIELD)
            .message(String.format(ERROR_MESSAGE, unknownStatus))
            .build();

        return Error.builder()
            .code(HTTP_CLIENT_ERROR_CODE)
            .message(String.format(ERROR_MESSAGE, unknownStatus))
            .fields(Collections.singletonList(field))
            .build();
    }

}
