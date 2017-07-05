package org.egov.pgrrest.read.web.adapters.error;

import org.egov.common.contract.response.Error;
import org.egov.common.contract.response.ErrorField;
import org.egov.common.contract.response.ErrorResponse;

import java.util.Collections;

public class ServiceDefinitionNotFoundExceptionAdapter implements ErrorAdapter<Void> {

    private static final int HTTP_CLIENT_ERROR_CODE = 400;
    private static final String SERVICE_DEFINITION_NOT_FOUND_CODE = "PGR.SERVICE_CODE_NOT_FOUND";
    private static final String SERVICE_CODE_FIELD = "serviceCode";
    private static final String SERVICE_DEFINITION_NOT_FOUND_MESSAGE =
        "Service definition not found for given service code and tenant id";

    @Override
    public ErrorResponse adapt(Void model) {
        return new ErrorResponse(null, getError());
    }

    private Error getError() {
        return Error.builder()
            .code(HTTP_CLIENT_ERROR_CODE)
            .message(SERVICE_DEFINITION_NOT_FOUND_MESSAGE)
            .fields(Collections.singletonList(getErrorField()))
            .build();
    }

    private ErrorField getErrorField() {
        return ErrorField.builder()
            .code(SERVICE_DEFINITION_NOT_FOUND_CODE)
            .field(SERVICE_CODE_FIELD)
            .message(SERVICE_DEFINITION_NOT_FOUND_MESSAGE)
            .build();
    }
}
