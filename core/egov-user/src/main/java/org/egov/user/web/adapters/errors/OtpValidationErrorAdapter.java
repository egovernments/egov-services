package org.egov.user.web.adapters.errors;

import org.egov.user.domain.model.User;
import org.egov.user.web.contract.Error;
import org.egov.user.web.contract.ErrorField;
import org.egov.user.web.contract.ErrorRes;
import org.springframework.http.HttpStatus;

import java.util.Arrays;
import java.util.List;

public class OtpValidationErrorAdapter implements ErrorAdapter<User> {

    private static final String OTP_FIELD = "User.otpReference";
    private static final String OTP_VALIDATION_CODE = "core-user.006";
    private static final String OTP_VALIDATION_MSG = "Otp validation is pending.";
    private static final String OTP_VALIDATION_PENDING = "Otp validation is pending.";

    public ErrorRes adapt(User user) {
        final Error error = getError(user);
        return new ErrorRes(null, error);
    }

    private Error getError(User user) {
        return Error.builder()
                .code(HttpStatus.BAD_REQUEST.value())
                .message(OTP_VALIDATION_PENDING)
                .fields(getOtpFieldError())
                .build();
    }

    private List<ErrorField> getOtpFieldError() {
        return Arrays.asList(
                ErrorField.builder().
                        field(OTP_FIELD).
                        code(OTP_VALIDATION_CODE).
                        message(OTP_VALIDATION_MSG).build()
        );
    }
}
