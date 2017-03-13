package org.egov.user.web.adapters.errors;

import org.egov.user.domain.model.UserSearch;
import org.egov.user.web.contract.Error;
import org.egov.user.web.contract.ErrorRes;
import org.egov.user.web.contract.ResponseInfo;
import org.springframework.http.HttpStatus;

public class InvalidUserSearchErrorAdapter implements ErrorAdapter<UserSearch> {
    public static final String INVALID_USER_SEARCH_REQUEST = "Invalid user search request";

    @Override
    public ErrorRes adapt(UserSearch model) {
        Error error = Error.builder()
                .code(HttpStatus.BAD_REQUEST.value())
                .message(INVALID_USER_SEARCH_REQUEST)
                .build();

        ResponseInfo responseInfo = ResponseInfo.builder()
                .status(String.valueOf(HttpStatus.BAD_REQUEST))
                .build();

        return new ErrorRes(responseInfo, error);
    }
}
