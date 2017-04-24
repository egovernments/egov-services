package org.egov.user.domain.model;

import org.egov.user.domain.exception.InvalidUserException;
import org.egov.user.domain.model.enums.Gender;
import org.egov.user.domain.model.enums.UserType;
import org.junit.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import static org.junit.Assert.*;

public class UserTest {

	@Test(expected = InvalidUserException.class)
	public void testUserWithEmptyNameIsInvalid() throws Exception {
		User user = User.builder()
				.mobileNumber("8899776655")
				.username("foolan_devi")
				.active(Boolean.TRUE)
				.gender(Gender.FEMALE)
				.type(UserType.CITIZEN)
				.build();

		assertTrue(user.isNameAbsent());
		user.validate();
	}

	@Test(expected = InvalidUserException.class)
	public void testUserWithEmptyUserNameIsInvalid() throws Exception {
		User user = User.builder()
				.mobileNumber("8899776655")
				.name("foolan")
				.active(Boolean.TRUE)
				.gender(Gender.FEMALE)
				.type(UserType.CITIZEN)
				.build();

		assertTrue(user.isUsernameAbsent());
		user.validate();
	}

	@Test(expected = InvalidUserException.class)
	public void testUserWithEmptyMobileIsInvalid() throws Exception {
		User user = User.builder()
				.username("foolan_devi")
				.name("foolan")
				.active(Boolean.TRUE)
				.gender(Gender.FEMALE)
				.type(UserType.CITIZEN)
				.build();

		assertTrue(user.isMobileNumberAbsent());
		user.validate();
	}

	@Test(expected = InvalidUserException.class)
	public void testUserWithEmptyTypeIsInvalid() throws Exception {
		User user = User.builder()
				.username("foolan_devi")
				.name("foolan")
				.mobileNumber("9988776655")
				.active(Boolean.TRUE)
				.gender(Gender.FEMALE)
				.build();

		assertTrue(user.isTypeAbsent());
		user.validate();
	}

	@Test(expected = InvalidUserException.class)
	public void test_should_throw_exception_when_tenant_id_is_not_present() {
		User user = User.builder()
				.username("foolan_devi")
				.name("foolan")
				.mobileNumber("9988776655")
				.active(Boolean.TRUE)
				.gender(Gender.FEMALE)
				.type(UserType.CITIZEN)
				.tenantId(null)
				.build();

		assertTrue(user.isTenantIdAbsent());
		user.validate();
	}

	@Test(expected = InvalidUserException.class)
	public void test_should_throw_exception_when_roles_is_not_present() {
		User user = User.builder()
				.username("foolan_devi")
				.name("foolan")
				.mobileNumber("9988776655")
				.active(Boolean.TRUE)
				.gender(Gender.FEMALE)
				.type(UserType.CITIZEN)
				.tenantId("ap.public")
				.roles(null)
				.build();

		assertTrue(user.isRolesAbsent());
		user.validate();
	}

	@Test(expected = InvalidUserException.class)
	public void test_should_throw_exception_when_role_code_is_not_present() {
		final Role role1 = Role.builder().code("roleCode1").build();
		final Role role2 = Role.builder().code(null).build();

		User user = User.builder()
				.username("foolan_devi")
				.name("foolan")
				.mobileNumber("9988776655")
				.active(Boolean.TRUE)
				.gender(Gender.FEMALE)
				.type(UserType.CITIZEN)
				.tenantId("ap.public")
				.roles(Arrays.asList(role1, role2))
				.build();

		assertTrue(user.isRolesAbsent());
		user.validate();
	}

	@Test
	public void test_should_return_true_when_user_id_is_not_present() {
		User user = User.builder()
				.id(null)
				.build();

		assertTrue(user.isIdAbsent());
	}

	@Test
	public void test_should_return_false_when_user_id_is_present() {
		User user = User.builder()
				.id(123L)
				.build();

		assertFalse(user.isIdAbsent());
	}

	@Test(expected = InvalidUserException.class)
	public void test_should_throw_validation_exception_when_otp_reference_is_not_present_and_mandatory_flag_is_enabled() {
		User user = User.builder()
				.otpReference(null)
				.otpValidationMandatory(true)
				.build();

		user.validate();
	}

	@Test
	public void test_should_return_true_when_otp_reference_is_not_present_and_mandatory_flag_is_enabled() {
		User user = User.builder()
				.otpReference(null)
				.otpValidationMandatory(true)
				.build();

		assertTrue(user.isOtpReferenceAbsent());
	}

	@Test
	public void test_should_return_false_when_otp_reference_is_not_present_and_mandatory_flag_is_false() {
		User user = User.builder()
				.otpReference(null)
				.otpValidationMandatory(false)
				.build();

		assertFalse(user.isOtpReferenceAbsent());
	}

	@Test
	public void test_should_return_false_when_otp_reference_is_present() {
		User user = User.builder()
				.otpReference("otpReference")
				.build();

		assertFalse(user.isOtpReferenceAbsent());
	}

	@Test
	public void testUserWithAllMandatoryValuesProvidedIsValid() {
		final Role role1 = Role.builder().code("roleCode1").build();
		User user = User.builder()
				.username("foolan_devi")
				.name("foolan")
				.mobileNumber("9988776655")
				.active(Boolean.TRUE)
				.gender(Gender.FEMALE)
				.type(UserType.CITIZEN)
				.tenantId("ap.public")
				.roles(Collections.singletonList(role1))
				.build();

		user.validate();

		assertFalse(user.isTypeAbsent());
		assertFalse(user.isActiveIndicatorAbsent());
		assertFalse(user.isNameAbsent());
		assertFalse(user.isMobileNumberAbsent());
		assertFalse(user.isUsernameAbsent());
		assertFalse(user.isTenantIdAbsent());
		assertFalse(user.isRolesAbsent());
	}

	@Test
	public void test_should_return_false_when_logged_in_user_is_same_as_user_being_updated() {
		User user = User.builder()
				.id(1L)
				.loggedInUserId(1L)
				.build();

		assertFalse(user.isLoggedInUserDifferentFromUpdatedUser());
	}

	@Test
	public void test_should_return_true_when_logged_in_user_is_different_from_user_being_updated() {
		User user = User.builder()
				.id(1L)
				.loggedInUserId(2L)
				.build();

		assertTrue(user.isLoggedInUserDifferentFromUpdatedUser());
	}

	@Test
	public void test_should_override_to_citizen_role() {
		User user = User.builder()
				.id(1L)
				.type(UserType.CITIZEN)
				.roles(Collections.singletonList(Role.builder().code("ADMIN").build()))
				.build();

		user.setRoleToCitizen();

		assertEquals(UserType.CITIZEN, user.getType());
		final List<Role> roles = user.getRoles();
		assertEquals(1, roles.size());
		assertEquals("CITIZEN", roles.get(0).getCode());
	}

	@Test
	public void test_should_nullify_fields() {
		Role role1 = Role.builder().code("roleCode1").build();
		Role role2 = Role.builder().code("roleCode2").build();
		User user = User.builder()
				.username("userName")
				.mobileNumber("mobileNumber")
				.password("password")
				.pwdExpiryDate(new Date())
				.roles(Arrays.asList(role1, role2))
				.build();

		user.nullifySensitiveFields();

		assertNull(user.getUsername());
		assertNull(user.getMobileNumber());
		assertNull(user.getPassword());
		assertNull(user.getPwdExpiryDate());
		assertNull(user.getRoles());
	}

}