package org.egov.pgrrest.read.web.adapters.error;

import org.egov.common.contract.response.Error;
import org.egov.common.contract.response.ErrorField;
import org.egov.common.contract.response.ErrorResponse;

import java.util.Collections;

public class GroupConstraintViolationExceptionAdapter implements ErrorAdapter<String> {

    private static final int HTTP_CLIENT_ERROR_CODE = 400;
    private static final String ERROR_CODE = "PGR.GROUP_CONSTRAINT_VIOLATED";
    private static final String ERROR_FIELD = "sevaRequest.serviceRequest.attribValues";
    private static final String ERROR_MESSAGE = "One of the constraints for group: '%s' has been violated";

    @Override
    public ErrorResponse adapt(String groupCode) {
        return new ErrorResponse(null, getError(groupCode));
    }

    private Error getError(String groupCode) {
        final ErrorField errorField = ErrorField.builder()
            .code(ERROR_CODE)
            .field(ERROR_FIELD)
            .message(String.format(ERROR_MESSAGE, groupCode))
            .build();

        return Error.builder()
            .code(HTTP_CLIENT_ERROR_CODE)
            .message(String.format(ERROR_MESSAGE, groupCode))
            .fields(Collections.singletonList(errorField))
            .build();
    }

}
