package org.egov.user.domain.model;

import org.egov.user.domain.exception.InvalidNonLoggedInUserUpdatePasswordRequestException;
import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class NonLoggedInUserUpdatePasswordRequestTest {

	@Test
	public void test_should_not_throw_exception_when_all_mandatory_fields_are_present() {
		final NonLoggedInUserUpdatePasswordRequest request = NonLoggedInUserUpdatePasswordRequest.builder()
				.existingPassword("existingPassword")
				.newPassword("newPassword")
				.mobileNumber("mobileNumber")
				.otpReference("otpReference")
				.build();

		request.validate();

		assertFalse(request.isExistingPasswordAbsent());
		assertFalse(request.isMobileNumberAbsent());
		assertFalse(request.isNewPasswordAbsent());
		assertFalse(request.isExistingPasswordAbsent());
		assertFalse(request.isOtpReferenceAbsent());
	}

	@Test(expected = InvalidNonLoggedInUserUpdatePasswordRequestException.class)
	public void test_should_throw_exception_when_existing_password_is_not_present() {
		final NonLoggedInUserUpdatePasswordRequest request = NonLoggedInUserUpdatePasswordRequest.builder()
				.existingPassword(null)
				.newPassword("newPassword")
				.mobileNumber("mobileNumber")
				.otpReference("otpReference")
				.build();

		request.validate();
	}

	@Test
	public void test_should_return_true_when_existing_password_is_not_present() {
		final NonLoggedInUserUpdatePasswordRequest request = NonLoggedInUserUpdatePasswordRequest.builder()
				.existingPassword(null)
				.newPassword("newPassword")
				.mobileNumber("mobileNumber")
				.otpReference("otpReference")
				.build();

		assertTrue(request.isExistingPasswordAbsent());
	}

	@Test(expected = InvalidNonLoggedInUserUpdatePasswordRequestException.class)
	public void test_should_throw_exception_when_new_password_is_not_present() {
		final NonLoggedInUserUpdatePasswordRequest request = NonLoggedInUserUpdatePasswordRequest.builder()
				.existingPassword("existingPassword")
				.newPassword(null)
				.mobileNumber("mobileNumber")
				.otpReference("otpReference")
				.build();

		request.validate();
	}

	@Test
	public void test_should_return_true_when_new_password_is_not_present() {
		final NonLoggedInUserUpdatePasswordRequest request = NonLoggedInUserUpdatePasswordRequest.builder()
				.existingPassword("existingPassword")
				.newPassword(null)
				.mobileNumber("mobileNumber")
				.otpReference("otpReference")
				.build();

		assertTrue(request.isNewPasswordAbsent());
	}

	@Test(expected = InvalidNonLoggedInUserUpdatePasswordRequestException.class)
	public void test_should_throw_exception_when_mobile_number_is_not_present() {
		final NonLoggedInUserUpdatePasswordRequest request = NonLoggedInUserUpdatePasswordRequest.builder()
				.existingPassword("existingPassword")
				.newPassword("newPassword")
				.mobileNumber(null)
				.otpReference("otpReference")
				.build();

		request.validate();
	}

	@Test
	public void test_should_return_true_when_mobile_number_is_not_present() {
		final NonLoggedInUserUpdatePasswordRequest request = NonLoggedInUserUpdatePasswordRequest.builder()
				.existingPassword("existingPassword")
				.newPassword("newPassword")
				.mobileNumber(null)
				.otpReference("otpReference")
				.build();

		assertTrue(request.isMobileNumberAbsent());
	}

	@Test(expected = InvalidNonLoggedInUserUpdatePasswordRequestException.class)
	public void test_should_throw_exception_when_otp_reference_is_not_present() {
		final NonLoggedInUserUpdatePasswordRequest request = NonLoggedInUserUpdatePasswordRequest.builder()
				.existingPassword("existingPassword")
				.newPassword("newPassword")
				.mobileNumber("mobileNumber")
				.otpReference(null)
				.build();

		request.validate();
	}

	@Test
	public void test_should_return_true_when_otp_reference_is_not_present() {
		final NonLoggedInUserUpdatePasswordRequest request = NonLoggedInUserUpdatePasswordRequest.builder()
				.existingPassword("existingPassword")
				.newPassword("newPassword")
				.mobileNumber("mobileNumber")
				.otpReference(null)
				.build();

		assertTrue(request.isOtpReferenceAbsent());
	}

}