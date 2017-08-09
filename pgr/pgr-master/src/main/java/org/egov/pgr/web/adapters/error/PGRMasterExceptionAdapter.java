package org.egov.pgr.web.adapters.error;

import org.egov.common.contract.response.Error;
import org.egov.common.contract.response.ErrorField;
import org.egov.common.contract.response.ErrorResponse;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;

public class PGRMasterExceptionAdapter implements ErrorAdapter<HashMap<String, String>> {


    private static final int HTTP_CLIENT_ERROR_CODE = 400;
    private static final String ERROR_CODE = "%s";
    private static final String ERROR_FIELD = "%s";
    private static final String ERROR_MESSAGE = "%s";

    @Override
    public ErrorResponse adapt(HashMap<String, String> hashMap) {
        final Error error = getError(hashMap);
        return new ErrorResponse(null, error);
    }

    private Error getError(HashMap<String, String> hashMap) {
        final List<ErrorField> fields = Collections.singletonList(getErrorField(hashMap));

        return Error.builder()
                .code(HTTP_CLIENT_ERROR_CODE)
                .fields(fields)
                .build();
    }

    private ErrorField getErrorField(HashMap<String, String> hashMap) {
        return ErrorField.builder()
                .code(String.format(ERROR_CODE,hashMap.get("code")))
                .field(String.format(ERROR_FIELD, hashMap.get("field")))
                .message(String.format(ERROR_MESSAGE, hashMap.get("message")))
                .build();
    }
}
