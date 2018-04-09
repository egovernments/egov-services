package org.egov.user.web.adapters.errors;

import java.util.Collections;
import java.util.List;

import org.egov.common.contract.response.Error;
import org.egov.common.contract.response.ErrorField;
import org.egov.common.contract.response.ErrorResponse;
import org.springframework.http.HttpStatus;

public class PasswordMissingErrorHandler implements ErrorAdapter<Void> {

	private static final String PASSWORD_MISSING_CODE = "core-user.PASSWORD_MANDATORY";
	private static final String PASSWORD_FIELD = "password";
	private static final String PASSWORD_MISSING_MESSAGE = "Password is required";

	@Override
	public ErrorResponse adapt(Void model) {
		final Error error = getError();
		return new ErrorResponse(null, error);
	}

	private Error getError() {
		return Error.builder()
				.code(HttpStatus.BAD_REQUEST.value())
				.message(PASSWORD_MISSING_MESSAGE)
				.fields(getErrorField())
				.build();
	}

	private List<ErrorField> getErrorField() {
		return Collections.singletonList(
				ErrorField.builder()
						.message(PASSWORD_MISSING_MESSAGE)
						.code(PASSWORD_MISSING_CODE)
						.field(PASSWORD_FIELD)
						.build()
		);
	}


}