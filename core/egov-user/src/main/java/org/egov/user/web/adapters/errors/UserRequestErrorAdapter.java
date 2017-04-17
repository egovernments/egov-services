package org.egov.user.web.adapters.errors;

import org.egov.common.contract.response.Error;
import org.egov.common.contract.response.ErrorField;
import org.egov.common.contract.response.ErrorResponse;
import org.egov.user.domain.model.User;
import org.springframework.http.HttpStatus;

import java.util.ArrayList;
import java.util.List;

public class UserRequestErrorAdapter implements ErrorAdapter<User> {

    private static final String INVALID_USER_OBJECT = "Invalid user object";

    private static final String USER_GENDER = "User.gender";
    private static final String GENDER_MISSING_CODE = "core-user.001";
    private static final String GENDER_MISSING_ERROR = "Gender is required";

    private static final String TYPE_MISSING_CODE = "core-user.002";
    private static final String USER_TYPE = "User.type";
    private static final String TYPE_MISSING_ERROR = "Type is required";

    private static final String MOBILE_MISSING_CODE = "core-user.004";
    private static final String USER_MOBILE = "User.mobile";
    private static final String MOBILE_MISSING_ERROR = "Mobile number is required";

    private static final String NAME_MISSING_CODE = "core-user.005";
    private static final String USER_NAME = "User.name";
    private static final String NAME_MISSING_ERROR = "Name is required";

    private static final String USERNAME_MISSING_CODE = "core-user.005";
    private static final String USER_USERNAME = "User.username";
    private static final String USERNAME_MISSING_ERROR = "Username is required";

    public ErrorResponse adapt(User user) {
        final Error error = getError(user);
        return new ErrorResponse(null, error);
    }

    private Error getError(User user) {
        List<ErrorField> errorFields = getErrorFields(user);
        return Error.builder()
                .code(HttpStatus.BAD_REQUEST.value())
                .message(INVALID_USER_OBJECT)
                .fields(errorFields)
                .build();
    }

    private List<ErrorField> getErrorFields(User user) {
        List<ErrorField> errorFields = new ArrayList<>();
        addGenderMissingError(user, errorFields);
        addTypeMissingError(user, errorFields);
        addMobileNumberMissingError(user, errorFields);
        addUsernameMissingError(user, errorFields);
        addNameMissingError(user, errorFields);
        return errorFields;
    }

    private void addNameMissingError(User user, List<ErrorField> errorFields) {
        if (user.isNameAbsent()) {
            errorFields.add(ErrorField.builder()
                    .code(NAME_MISSING_CODE).field(USER_NAME).message(NAME_MISSING_ERROR).build());
        }
    }

    private void addUsernameMissingError(User user, List<ErrorField> errorFields) {
        if (user.isUsernameAbsent()) {
            errorFields.add(ErrorField.builder()
                    .code(USERNAME_MISSING_CODE).field(USER_USERNAME).message(USERNAME_MISSING_ERROR).build());
        }
    }

    private void addTypeMissingError(User user, List<ErrorField> errorFields) {
        if (user.isTypeAbsent()) {
            errorFields.add(ErrorField.builder()
                    .code(TYPE_MISSING_CODE).field(USER_TYPE).message(TYPE_MISSING_ERROR).build());
        }
    }

    private void addGenderMissingError(User user, List<ErrorField> errorFields) {
        if (user.isGenderAbsent()) {
            errorFields.add(ErrorField.builder()
                    .code(GENDER_MISSING_CODE).field(USER_GENDER).message(GENDER_MISSING_ERROR).build());
        }
    }

    private void addMobileNumberMissingError(User user, List<ErrorField> errorFields) {
        if (user.isMobileNumberAbsent()) {
            errorFields.add(ErrorField.builder()
                    .code(MOBILE_MISSING_CODE).field(USER_MOBILE).message(MOBILE_MISSING_ERROR).build());
        }
    }
}

