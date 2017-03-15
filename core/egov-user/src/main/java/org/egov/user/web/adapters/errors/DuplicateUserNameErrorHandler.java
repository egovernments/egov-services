package org.egov.user.web.adapters.errors;

import org.egov.user.domain.model.User;
import org.egov.user.web.contract.Error;
import org.egov.user.web.contract.ErrorField;
import org.egov.user.web.contract.ErrorRes;
import org.springframework.http.HttpStatus;

import java.util.Collections;
import java.util.List;

public class DuplicateUserNameErrorHandler implements ErrorAdapter<User> {

    private static final String DUPLICATE_USER_NAME_CODE = "USER.USER_NAME_ALREADY_EXISTS";
    private static final String DUPLICATE_USER_NAME_MESSAGE = "User name already exists";
    private static final String USER_NAME_FIELD = "User.userName";

    public ErrorRes adapt(User user) {
        final Error error = getError();
        return new ErrorRes(null, error);
    }

    private Error getError() {
        return Error.builder()
                .code(HttpStatus.BAD_REQUEST.value())
                .message(DUPLICATE_USER_NAME_MESSAGE)
                .fields(getUserNameFieldError())
                .build();
    }

    private List<ErrorField> getUserNameFieldError() {
        return Collections.singletonList(
                ErrorField.builder()
                        .field(USER_NAME_FIELD)
                        .code(DUPLICATE_USER_NAME_CODE)
                        .message(DUPLICATE_USER_NAME_MESSAGE)
                        .build()
        );
    }
}
