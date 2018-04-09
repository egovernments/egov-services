package org.egov.user.persistence.entity;

import org.egov.user.domain.model.enums.BloodGroup;
import org.egov.user.domain.model.enums.Gender;
import org.egov.user.domain.model.enums.GuardianRelation;
import org.egov.user.domain.model.enums.UserType;
import org.junit.Test;

import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;

public class UserTest {

	@Test
	public void test_should_return_last_updated_by_as_null_when_not_present() throws Exception {
		User userEntity = getUserEntity();
		userEntity.setLastModifiedBy(null);

		org.egov.user.domain.model.User userModel = userEntity.toDomain(null, null);

		assertNull(userModel.getLastModifiedBy());
	}

	@Test
	public void test_entity_should_convert_itself_to_domain() throws Exception {
		User userEntity = getUserEntity();

		org.egov.user.domain.model.User userModel = userEntity.toDomain(null, null);

		assertThat(userModel.getId()).isEqualTo(1L);
		assertThat(userModel.getUsername()).isEqualTo(userModel.getUsername());
		assertThat(userModel.getSalutation()).isEqualTo(userModel.getSalutation());
		assertThat(userModel.getName()).isEqualTo(userModel.getName());
		assertThat(userModel.getGender()).isEqualTo(userModel.getGender());
		assertThat(userModel.getMobileNumber()).isEqualTo(userModel.getMobileNumber());
		assertThat(userModel.getEmailId()).isEqualTo(userModel.getEmailId());
		assertThat(userModel.getAltContactNumber()).isEqualTo(userModel.getAltContactNumber());
		assertThat(userModel.getPan()).isEqualTo(userModel.getPan());
		assertThat(userModel.getAadhaarNumber()).isEqualTo(userModel.getAadhaarNumber());
		assertThat(userModel.getActive()).isEqualTo(userModel.getActive());
		assertThat(userModel.getDob()).isEqualTo(userModel.getDob());
		assertThat(userModel.getPasswordExpiryDate()).isEqualTo(userModel.getPasswordExpiryDate());
		assertThat(userModel.getLocale()).isEqualTo(userModel.getLocale());
		assertThat(userModel.getType()).isEqualTo(userModel.getType());
		assertThat(userModel.getAccountLocked()).isEqualTo(userModel.getAccountLocked());
		assertThat(userModel.getRoles()).isEqualTo(userModel.getRoles());
		assertThat(userModel.getGuardian()).isEqualTo(userModel.getGuardian());
		assertThat(userModel.getGuardianRelation()).isEqualTo(userModel.getGuardianRelation());
		assertThat(userModel.getSignature()).isEqualTo(userModel.getSignature());
		assertThat(userModel.getBloodGroup()).isEqualTo(userModel.getBloodGroup());
		assertThat(userModel.getPhoto()).isEqualTo(userModel.getPhoto());
		assertThat(userModel.getIdentificationMark()).isEqualTo(userModel.getIdentificationMark());
		assertThat(userModel.getRoles().size()).isEqualTo(2);
	}

	@Test
	public void test_entity_should_build_itself_from_domain() {
		org.egov.user.domain.model.User domainUser = getUserModel(true, false);
		User entityUser = new User(domainUser);

		assertThat(entityUser.getId().getId()).isEqualTo(domainUser.getId());
		assertThat(entityUser.getName()).isEqualTo(domainUser.getName());
		assertThat(entityUser.getUsername()).isEqualTo(domainUser.getUsername());
		assertThat(entityUser.getTitle()).isEqualTo(domainUser.getTitle());
		assertThat(entityUser.getPassword()).isEqualTo(domainUser.getPassword());
		assertThat(entityUser.getSalutation()).isEqualTo(domainUser.getSalutation());
		assertThat(entityUser.getGuardian()).isEqualTo(domainUser.getGuardian());
		assertThat(entityUser.getGuardianRelation())
				.isEqualTo(org.egov.user.persistence.enums.GuardianRelation.Father);
		assertThat(entityUser.getGender()).isEqualTo(org.egov.user.persistence.enums.Gender.FEMALE);
		assertThat(entityUser.getMobileNumber()).isEqualTo(domainUser.getMobileNumber());
		assertThat(entityUser.getEmailId()).isEqualTo(domainUser.getEmailId());
		assertThat(entityUser.getAltContactNumber()).isEqualTo(domainUser.getAltContactNumber());
		assertThat(entityUser.getPan()).isEqualTo(domainUser.getPan());
		assertThat(entityUser.getAadhaarNumber()).isEqualTo(domainUser.getAadhaarNumber());
		assertThat(entityUser.getActive()).isEqualTo(domainUser.getActive());
		assertThat(entityUser.getDob()).isEqualTo(domainUser.getDob());
		assertThat(entityUser.getPwdExpiryDate()).isEqualTo(domainUser.getPasswordExpiryDate());
		assertThat(entityUser.getLocale()).isEqualTo(domainUser.getLocale());
		assertThat(entityUser.getType()).isEqualTo(org.egov.user.persistence.enums.UserType.CITIZEN);
		assertThat(entityUser.getBloodGroup()).isEqualTo(org.egov.user.persistence.enums.BloodGroup.A_POSITIVE);
		assertThat(entityUser.getIdentificationMark()).isEqualTo(domainUser.getIdentificationMark());
		assertThat(entityUser.getSignature()).isEqualTo(domainUser.getSignature());
		assertThat(entityUser.getPhoto()).isEqualTo(domainUser.getPhoto());
		assertThat(entityUser.getAccountLocked()).isEqualTo(domainUser.getAccountLocked());
		assertThat(entityUser.getLastModifiedDate()).isEqualTo(domainUser.getLastModifiedDate());
		assertThat(entityUser.getCreatedDate()).isEqualTo(domainUser.getCreatedDate());
		assertThat(entityUser.getRoles().size()).isEqualTo(2);
	}

	@Test
	public void test_entity_should_map_boolean_flags_to_false_when_not_specified_in_domain() {
		org.egov.user.domain.model.User domainUser = getUserModel(null, null);

		User entityUser = new User(domainUser);

		assertFalse(entityUser.getActive());
		assertFalse(entityUser.getAccountLocked());
	}

	private org.egov.user.persistence.entity.User getUserEntity() {
		Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("IST"));
		calendar.set(1990, Calendar.JULY, 1);
		Date date = calendar.getTime();

		org.egov.user.persistence.entity.User userEntity = org.egov.user.persistence.entity.User.builder()
				.userKey(new UserKey(1L, "tenant"))
				.username("userName")
				.salutation("salutation")
				.name("name")
				.gender(org.egov.user.persistence.enums.Gender.FEMALE)
				.mobileNumber("mobileNumber1")
				.emailId("email")
				.altContactNumber("mobileNumber2")
				.pan("pan")
				.aadhaarNumber("aadhaarNumber")
//				.addresses(getAddressList())
				.active(true)
				.dob(date)
				.pwdExpiryDate(date)
				.locale("en_IN")
				.type(org.egov.user.persistence.enums.UserType.CITIZEN)
				.accountLocked(false)
				.roles(getListOfRoles())
				.guardian("name of relative")
				.guardianRelation(org.egov.user.persistence.enums.GuardianRelation.Father)
				.signature("7a9d7f12-bdcb-4487-9d43-709838a0ad39")
				.bloodGroup(org.egov.user.persistence.enums.BloodGroup.A_POSITIVE)
				.photo("3b26fb49-e43d-401b-899a-f8f0a1572de0")
				.identificationMark("identification mark")
				.build();

		userEntity.setCreatedBy(1L);
		userEntity.setCreatedDate(date);
		userEntity.setLastModifiedBy(1L);
		userEntity.setLastModifiedDate(date);

		return userEntity;
	}

	private Set<Role> getListOfRoles() {
		org.egov.user.persistence.entity.User user = org.egov.user.persistence.entity.User.builder()
				.userKey(new UserKey(0L, "tenant"))
				.build();
		Calendar calendar = Calendar.getInstance();
		calendar.set(1990, Calendar.JULY, 1);

		org.egov.user.persistence.entity.Role role1 = org.egov.user.persistence.entity.Role.builder()
				.roleKey(new RoleKey(1L, "tenant"))
				.name("name of the role 1")
				.description("description")
				.build();
		role1.setCreatedBy(1L);
		role1.setCreatedDate(calendar.getTime());
		role1.setLastModifiedBy(1L);
		role1.setLastModifiedDate(calendar.getTime());

		org.egov.user.persistence.entity.Role role2 = org.egov.user.persistence.entity.Role.builder()
				.roleKey(new RoleKey(2L, "tenant"))
				.name("name of the role 2")
				.description("description")
				.build();
		role2.setCreatedBy(1L);
		role2.setCreatedDate(calendar.getTime());
		role2.setLastModifiedBy(1L);
		role2.setLastModifiedDate(calendar.getTime());

		return new HashSet<Role>() {{
			add(role1);
			add(role2);
		}};
	}

	private org.egov.user.domain.model.User getUserModel(Boolean isActive, Boolean isAccountLocked) {
		Calendar calendar = Calendar.getInstance();
		calendar.set(1990, Calendar.JULY, 1);
		Date date = calendar.getTime();

		return org.egov.user.domain.model.User.builder()
				.id(1L)
				.username("userName")
				.salutation("salutation")
				.name("name")
				.gender(Gender.FEMALE)
				.mobileNumber("mobileNumber1")
				.emailId("email")
				.altContactNumber("mobileNumber2")
				.pan("pan")
				.aadhaarNumber("aadhaarNumber")
				.active(isActive)
				.dob(date)
				.passwordExpiryDate(date)
				.locale("en_IN")
				.type(UserType.CITIZEN)
				.accountLocked(isAccountLocked)
				.roles(getListOfDomainRoles())
				.guardian("name of relative")
				.guardianRelation(GuardianRelation.Father)
				.signature("7a9d7f12-bdcb-4487-9d43-709838a0ad39")
				.bloodGroup(BloodGroup.A_POSITIVE)
				.photo("3b26fb49-e43d-401b-899a-f8f0a1572de0")
				.identificationMark("identification mark")
				.build();
	}

	private List<org.egov.user.domain.model.Role> getListOfDomainRoles() {
		org.egov.user.domain.model.Role userRole = org.egov.user.domain.model.Role.builder()
				.name("USER")
				.description("Role Description")
				.build();

		org.egov.user.domain.model.Role adminRole = org.egov.user.domain.model.Role.builder()
				.name("ADMIN")
				.description("Role Description")
				.build();

		return Arrays.asList(userRole, adminRole);
	}
}
