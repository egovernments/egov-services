package org.egov.pgrrest.read.web.adapters.error;

import org.egov.common.contract.response.Error;
import org.egov.common.contract.response.ErrorField;
import org.egov.common.contract.response.ErrorResponse;
import org.springframework.http.HttpStatus;

import java.util.ArrayList;
import java.util.List;

public class InvalidOtpValidationRequestExceptionAdapter implements ErrorAdapter<Void> {

    private static final String OTP_VALIDATION_NOT_COMPLETE_MESSAGE =
        "OTP validation request is invalid. Mandatory field 'otpReference' is missing.";
    private static final String MANDATORY_CRN_CODE = "PGR.OTP_VALIDATION_REQUEST_INVALID";
    private static final String FIELD_NAME = "PGR.OTP_VALIDATION_REQUEST_INVALID";

    @Override
    public ErrorResponse adapt(Void model) {
        final Error error = getError();
        return new ErrorResponse(null, error);
    }

    private Error getError() {
        List<ErrorField> errorFields = getErrorFields();
        return Error.builder()
                .code(HttpStatus.BAD_REQUEST.value())
                .message(OTP_VALIDATION_NOT_COMPLETE_MESSAGE)
                .fields(errorFields)
                .build();
    }

    private List<ErrorField> getErrorFields() {
        List<ErrorField> errorFields = new ArrayList<>();
        addCRNValidationErrors(errorFields);
        return errorFields;
    }

    private void addCRNValidationErrors(List<ErrorField> errorFields) {
        final ErrorField errorField = ErrorField.builder()
                .code(MANDATORY_CRN_CODE)
                .message(OTP_VALIDATION_NOT_COMPLETE_MESSAGE)
                .field(FIELD_NAME)
                .build();
        errorFields.add(errorField);
    }

}


