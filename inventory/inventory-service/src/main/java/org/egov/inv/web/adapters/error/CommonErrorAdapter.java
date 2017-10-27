package org.egov.inv.web.adapters.error;

import org.egov.common.contract.response.Error;
import org.egov.common.contract.response.ErrorField;
import org.egov.common.contract.response.ErrorResponse;
import org.springframework.http.HttpStatus;

import java.util.List;

public class CommonErrorAdapter implements ErrorAdapter<List<ErrorField>> {

   

    @Override
    public ErrorResponse adapt(List<ErrorField> errorFields,String requestErrorMsg) {
        final Error error = getError(errorFields, requestErrorMsg);
        return new ErrorResponse(null, error);
    }

    private Error getError(List<ErrorField> errorFields,String requestErrorMsg) {
        return Error.builder()
                .code(HttpStatus.BAD_REQUEST.value())
                .message(requestErrorMsg)
                .fields(errorFields)
                .build();
    }

}
