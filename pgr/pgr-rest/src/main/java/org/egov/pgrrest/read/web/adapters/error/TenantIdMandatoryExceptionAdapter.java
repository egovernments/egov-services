package org.egov.pgrrest.read.web.adapters.error;

import org.egov.common.contract.response.Error;
import org.egov.common.contract.response.ErrorField;
import org.egov.common.contract.response.ErrorResponse;
import org.springframework.http.HttpStatus;

import java.util.ArrayList;
import java.util.List;

public class TenantIdMandatoryExceptionAdapter implements ErrorAdapter<Void> {

    private static final String MESSAGE = "Tenant id is mandatory";
    private static final String CODE = "PGR.TENANT_ID_MANDATORY";
    private static final String FIELD_NAME = "serviceRequest.tenantId";

    @Override
    public ErrorResponse adapt(Void model) {
        final Error error = getError();
        return new ErrorResponse(null, error);
    }

    private Error getError() {
        List<ErrorField> errorFields = getErrorFields();
        return Error.builder()
            .code(HttpStatus.BAD_REQUEST.value())
            .message(MESSAGE)
            .fields(errorFields)
            .build();
    }

    private List<ErrorField> getErrorFields() {
        List<ErrorField> errorFields = new ArrayList<>();
        final ErrorField errorField = ErrorField.builder()
            .code(CODE)
            .message(MESSAGE)
            .field(FIELD_NAME)
            .build();
        errorFields.add(errorField);
        return errorFields;
    }

}

