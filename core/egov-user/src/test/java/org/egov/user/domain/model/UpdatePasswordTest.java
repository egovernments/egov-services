package org.egov.user.domain.model;

import org.egov.user.domain.exception.InvalidUpdatePasswordRequestException;
import org.junit.Test;

import static org.junit.Assert.*;

public class UpdatePasswordTest {

	@Test
	public void test_equals_should_return_true_when_both_instances_have_same_field_values() {
		final UpdatePassword updatePassword1 = UpdatePassword.builder()
				.userId(123L)
				.existingPassword("existingPassword")
				.newPassword("newPassword")
				.build();
		final UpdatePassword updatePassword2 = UpdatePassword.builder()
				.userId(123L)
				.existingPassword("existingPassword")
				.newPassword("newPassword")
				.build();

		assertEquals(updatePassword1, updatePassword2);
	}

	@Test
	public void test_hashcode_should_be_same_when_both_instances_have_same_field_values() {
		final UpdatePassword updatePassword1 = UpdatePassword.builder()
				.userId(123L)
				.existingPassword("existingPassword")
				.newPassword("newPassword")
				.build();
		final UpdatePassword updatePassword2 = UpdatePassword.builder()
				.userId(123L)
				.existingPassword("existingPassword")
				.newPassword("newPassword")
				.build();

		assertEquals(updatePassword1.hashCode(), updatePassword2.hashCode());
	}

	@Test
	public void test_equals_should_return_false_when_both_instances_have_different_field_values() {
		final UpdatePassword updatePassword1 = UpdatePassword.builder()
				.userId(124L)
				.existingPassword("oldPassword1")
				.newPassword("newPassword1")
				.build();
		final UpdatePassword updatePassword2 = UpdatePassword.builder()
				.userId(123L)
				.existingPassword("existingPassword")
				.newPassword("newPassword")
				.build();

		assertNotEquals(updatePassword1, updatePassword2);
	}

	@Test
	public void test_hashcode_should_differ_when_both_instances_have_different_field_values() {
		final UpdatePassword updatePassword1 = UpdatePassword.builder()
				.userId(124L)
				.existingPassword("oldPassword1")
				.newPassword("newPassword1")
				.build();
		final UpdatePassword updatePassword2 = UpdatePassword.builder()
				.userId(123L)
				.existingPassword("existingPassword")
				.newPassword("newPassword")
				.build();

		assertNotEquals(updatePassword1.hashCode(), updatePassword2.hashCode());
	}

	@Test
	public void test_validate_should_not_throw_exception_when_all_mandatory_fields_are_present() {
		final UpdatePassword updatePassword = UpdatePassword.builder()
				.existingPassword("existingPassword")
				.newPassword("newPassword")
				.userId(123L)
				.build();

		updatePassword.validate();
	}

	@Test(expected = InvalidUpdatePasswordRequestException.class)
	public void test_validate_should_throw_exception_when_user_id_is_not_present() {
		final UpdatePassword updatePassword = UpdatePassword.builder()
				.existingPassword("existingPassword")
				.newPassword("newPassword")
				.userId(null)
				.build();

		updatePassword.validate();
	}

	@Test(expected = InvalidUpdatePasswordRequestException.class)
	public void test_validate_should_throw_exception_when_old_password_is_not_present() {
		final UpdatePassword updatePassword = UpdatePassword.builder()
				.existingPassword(null)
				.newPassword("newPassword")
				.userId(123L)
				.build();

		updatePassword.validate();
	}

	@Test(expected = InvalidUpdatePasswordRequestException.class)
	public void test_validate_should_throw_exception_when_new_password_is_not_present() {
		final UpdatePassword updatePassword = UpdatePassword.builder()
				.existingPassword("existingPassword")
				.newPassword(null)
				.userId(123L)
				.build();

		updatePassword.validate();
	}

}