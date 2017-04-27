package org.egov.user.web.adapters.errors;

import org.egov.common.contract.response.Error;
import org.egov.common.contract.response.ErrorField;
import org.egov.common.contract.response.ErrorResponse;
import org.egov.user.domain.model.NonLoggedInUserUpdatePasswordRequest;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class InvalidNonLoggedInUserUpdatePasswordRequestErrorHandlerTest {

	private InvalidNonLoggedInUserUpdatePasswordRequestErrorHandler errorHandler;

	@Before
	public void before() {
		errorHandler = new InvalidNonLoggedInUserUpdatePasswordRequestErrorHandler();
	}

	@Test
	public void test_should_return_error_when_mobile_number_is_not_present() {
		final NonLoggedInUserUpdatePasswordRequest updatePassword = mock(NonLoggedInUserUpdatePasswordRequest.class);
		when(updatePassword.isMobileNumberAbsent()).thenReturn(true);

		final ErrorResponse errorResponse = errorHandler.adapt(updatePassword);

		final Error error = errorResponse.getError();
		final List<ErrorField> fields = error.getFields();
		assertEquals(1, fields.size());
		assertEquals("mobileNumber", fields.get(0).getField());
		assertEquals("Mobile number is mandatory", fields.get(0).getMessage());
		assertEquals("USER.MOBILE_NUMBER_MANDATORY", fields.get(0).getCode());
	}

	@Test
	public void test_should_return_error_when_otp_reference_is_not_present() {
		final NonLoggedInUserUpdatePasswordRequest updatePassword = mock(NonLoggedInUserUpdatePasswordRequest.class);
		when(updatePassword.isOtpReferenceAbsent()).thenReturn(true);

		final ErrorResponse errorResponse = errorHandler.adapt(updatePassword);

		final Error error = errorResponse.getError();
		final List<ErrorField> fields = error.getFields();
		assertEquals(1, fields.size());
		assertEquals("otpReference", fields.get(0).getField());
		assertEquals("OTP reference is mandatory", fields.get(0).getMessage());
		assertEquals("USER.OTP_REFERENCE_MANDATORY", fields.get(0).getCode());
	}

	@Test
	public void test_should_return_error_when_existing_password_is_not_present() {
		final NonLoggedInUserUpdatePasswordRequest updatePassword = mock(NonLoggedInUserUpdatePasswordRequest.class);
		when(updatePassword.isExistingPasswordAbsent()).thenReturn(true);

		final ErrorResponse errorResponse = errorHandler.adapt(updatePassword);

		final Error error = errorResponse.getError();
		final List<ErrorField> fields = error.getFields();
		assertEquals(1, fields.size());
		assertEquals("existingPassword", fields.get(0).getField());
		assertEquals("Existing password is mandatory", fields.get(0).getMessage());
		assertEquals("USER.EXISTING_PASSWORD_MANDATORY", fields.get(0).getCode());
	}

	@Test
	public void test_should_return_error_when_new_password_is_not_present() {
		final NonLoggedInUserUpdatePasswordRequest updatePassword = mock(NonLoggedInUserUpdatePasswordRequest.class);
		when(updatePassword.isNewPasswordAbsent()).thenReturn(true);

		final ErrorResponse errorResponse = errorHandler.adapt(updatePassword);

		final Error error = errorResponse.getError();
		final List<ErrorField> fields = error.getFields();
		assertEquals(1, fields.size());
		assertEquals("newPassword", fields.get(0).getField());
		assertEquals("New password is mandatory", fields.get(0).getMessage());
		assertEquals("USER.NEW_PASSWORD_MANDATORY", fields.get(0).getCode());
	}
}