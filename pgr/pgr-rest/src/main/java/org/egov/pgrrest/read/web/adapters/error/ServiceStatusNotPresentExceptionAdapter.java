package org.egov.pgrrest.read.web.adapters.error;

import org.egov.common.contract.response.Error;
import org.egov.common.contract.response.ErrorField;
import org.egov.common.contract.response.ErrorResponse;

import java.util.Collections;

public class ServiceStatusNotPresentExceptionAdapter implements ErrorAdapter<Void> {

    private static final int HTTP_CLIENT_ERROR_CODE = 400;
    private static final String ERROR_CODE = "PGR.SERVICE_STATUS_MANDATORY";
    private static final String ERROR_FIELD = "sevaRequest.serviceRequest.attribValues=systemStatus";
    private static final String ERROR_MESSAGE = "Service status is mandatory";

    @Override
    public ErrorResponse adapt(Void model) {
        return new ErrorResponse(null, getError());
    }

    private Error getError() {
        final ErrorField field = ErrorField.builder()
            .code(ERROR_CODE)
            .field(ERROR_FIELD)
            .message(ERROR_MESSAGE)
            .build();

        return Error.builder()
            .code(HTTP_CLIENT_ERROR_CODE)
            .message(ERROR_MESSAGE)
            .fields(Collections.singletonList(field))
            .build();
    }

}

