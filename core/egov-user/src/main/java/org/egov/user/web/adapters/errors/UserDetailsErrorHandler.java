package org.egov.user.web.adapters.errors;

import lombok.Getter;
import org.egov.user.web.contract.Error;
import org.egov.user.web.contract.ErrorField;
import org.egov.user.web.contract.ErrorRes;
import org.springframework.http.HttpStatus;

import java.util.Collections;
import java.util.List;

@Getter
public class UserDetailsErrorHandler extends RuntimeException {

    private static final String USER_DETAILS_NOT_FOUND_CODE = "USER_DETAILS_NOT_FOUND";
    private static final String USER_DETAILS_NOT_FOUND = "Error while fetching user details";

    public ErrorRes adapt() {
        final Error error = getError();
        return new ErrorRes(null, error);
    }

    private Error getError() {
        return Error.builder()
                .code(HttpStatus.BAD_REQUEST.value())
                .message(USER_DETAILS_NOT_FOUND)
                .fields(getAcessTokenFieldError())
                .build();
    }

    private List<ErrorField> getAcessTokenFieldError() {
        return Collections.singletonList(
                ErrorField.builder()
                        .message(USER_DETAILS_NOT_FOUND)
                        .code(USER_DETAILS_NOT_FOUND_CODE)
                        .field("user")
                        .build()
        );
    }

}

