package org.egov.inv.web.adapters.error;

import org.egov.common.contract.response.Error;
import org.egov.common.contract.response.ErrorField;
import org.egov.common.contract.response.ErrorResponse;
import org.springframework.http.HttpStatus;

import java.util.List;

public class MaterialErrorAdapter implements ErrorAdapter<List<ErrorField>> {

    private static final String INVALID_MATERIAL_REQUEST = "Material Request is invalid";

    @Override
    public ErrorResponse adapt(List<ErrorField> errorFields) {
        final Error error = getError(errorFields);
        return new ErrorResponse(null, error);
    }

    private Error getError(List<ErrorField> errorFields) {
        return Error.builder()
                .code(HttpStatus.BAD_REQUEST.value())
                .message(INVALID_MATERIAL_REQUEST)
                .fields(errorFields)
                .build();
    }

}
