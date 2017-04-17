package org.egov.user.domain.model;

import org.egov.user.domain.exception.InvalidUserException;
import org.egov.user.domain.model.enums.Gender;
import org.egov.user.domain.model.enums.UserType;
import org.junit.Test;

import java.util.Arrays;
import java.util.Collections;

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
	public void testUserWithEmptyGenderIsInvalid() throws Exception {
		User user = User.builder()
				.username("foolan_devi")
				.name("foolan")
				.mobileNumber("9988776655")
				.active(Boolean.TRUE)
				.type(UserType.CITIZEN)
				.build();

		assertTrue(user.isGenderAbsent());
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
	public void testUserWithAllMandatoryValuesProvidedIsValid() throws Exception {
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
		assertFalse(user.isGenderAbsent());
		assertFalse(user.isActiveIndicatorAbsent());
		assertFalse(user.isNameAbsent());
		assertFalse(user.isMobileNumberAbsent());
		assertFalse(user.isUsernameAbsent());
		assertFalse(user.isTenantIdAbsent());
		assertFalse(user.isRolesAbsent());
	}

}