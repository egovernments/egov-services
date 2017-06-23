package org.egov.user.persistence.repository;

import org.egov.user.TestConfiguration;
import org.egov.user.domain.model.UserSearchCriteria;
import org.egov.user.persistence.entity.User;
import org.egov.user.persistence.enums.BloodGroup;
import org.egov.user.persistence.enums.Gender;
import org.egov.user.persistence.enums.UserType;
import org.egov.user.persistence.specification.FuzzyNameMatchingSpecification;
import org.egov.user.persistence.specification.MultiFieldsMatchingSpecification;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Arrays;
import java.util.List;

import static java.util.Arrays.asList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

@RunWith(SpringRunner.class)
@DataJpaTest
@Import(TestConfiguration.class)
public class UserJpaRepositoryTest {

	@Autowired
	private UserJpaRepository userJpaRepository;

	@Test
	@Sql(scripts = {"/sql/clearUserRoles.sql",
			"/sql/clearAddresses.sql",
			"/sql/clearRoles.sql",
			"/sql/clearUsers.sql",
			"/sql/createUsers.sql"})
	public void test_should_return_count_of_users_with_given_user_name_and_tenant_and_id_not_matching() {
		Long count = userJpaRepository.isUserPresent("bigcat399", 5L, "ap.public");
		assertEquals(Long.valueOf(1), count);
	}

	@Test
	@Sql(scripts = {"/sql/clearUserRoles.sql",
			"/sql/clearAddresses.sql",
			"/sql/clearRoles.sql",
			"/sql/clearUsers.sql",
			"/sql/createUsers.sql"})
	public void test_should_return_count_of_users_with_given_user_name_and_tenant() {
		Long id = userJpaRepository.isUserPresent("bigcat399", "ap.public");
		assertEquals(Long.valueOf(1), id);
	}

	@Test
	@Sql(scripts = {"/sql/clearUserRoles.sql",
			"/sql/clearAddresses.sql",
			"/sql/clearRoles.sql",
			"/sql/clearUsers.sql",
			"/sql/createUsers.sql"})
	public void test_should_return_zero_count_when_user_does_not_exist_for_given_user_name_and_tenant_not_having_id() {
		Long count = userJpaRepository.isUserPresent("bigcat399", 1L, "ap.public");
		assertEquals(Long.valueOf(0), count);
	}

	@Test
	@Sql(scripts = {"/sql/clearUserRoles.sql",
			"/sql/clearAddresses.sql",
			"/sql/clearRoles.sql",
			"/sql/clearUsers.sql",
			"/sql/createUsers.sql"})
	public void test_should_return_zero_count_when_user_does_not_exist_for_given_user_name_and_tenant() {
		Long count = userJpaRepository.isUserPresent("unknown", "tenantId");
		assertEquals(Long.valueOf(0), count);
	}

	@Test
	@Sql(scripts = {"/sql/clearUserRoles.sql",
			"/sql/clearAddresses.sql",
			"/sql/clearRoles.sql",
			"/sql/clearUsers.sql",
			"/sql/createUsers.sql"})
	public void should_fetch_user_by_name() {
		User user = userJpaRepository.findByUsernameAndUserKeyTenantId("greenfish424", "ap.public");
		assertThat(user.getId().getId()).isEqualTo(2L);
	}

	@Test
	@Sql(scripts = {"/sql/clearUserRoles.sql",
			"/sql/clearAddresses.sql",
			"/sql/clearRoles.sql",
			"/sql/clearUsers.sql",
			"/sql/createUsers.sql"})
	public void should_fetch_user_by_email() {
		User user = userJpaRepository.findByEmailIdAndUserKeyTenantId("email3@gmail.com", "ap.public");
		assertThat(user.getId().getId()).isEqualTo(3L);
	}

	@Test
	@Sql(scripts = {"/sql/clearUserRoles.sql",
			"/sql/clearAddresses.sql",
			"/sql/clearRoles.sql",
			"/sql/clearUsers.sql",
			"/sql/createUsers.sql"})
	public void should_return_active_users_for_given_tenant_and_name_containing() {
		UserSearchCriteria searchCriteria = UserSearchCriteria.builder()
				.name("Ram")
				.tenantId("ap.public")
				.fuzzyLogic(true)
				.active(true)
				.build();
		FuzzyNameMatchingSpecification fuzzyNameMatchingSpecification =
				new FuzzyNameMatchingSpecification(searchCriteria);

		List<User> userList = userJpaRepository.findAll(fuzzyNameMatchingSpecification);

		assertEquals(3, userList.size());
		assertThat(userList.get(0).getId().getId()).isEqualTo(3);
		assertThat(userList.get(1).getId().getId()).isEqualTo(4);
		assertThat(userList.get(2).getId().getId()).isEqualTo(5);
	}

	@Test
	@Sql(scripts = {"/sql/clearUserRoles.sql",
			"/sql/clearAddresses.sql",
			"/sql/clearRoles.sql",
			"/sql/clearUsers.sql",
			"/sql/createUsers.sql"})
	public void should_return_in_active_users_for_given_tenant_and_name_containing() {
		UserSearchCriteria searchCriteria = UserSearchCriteria.builder()
				.name("Ram")
				.tenantId("ap.public")
				.fuzzyLogic(true)
				.active(false)
				.build();
		FuzzyNameMatchingSpecification fuzzyNameMatchingSpecification =
				new FuzzyNameMatchingSpecification(searchCriteria);

		List<User> userList = userJpaRepository.findAll(fuzzyNameMatchingSpecification);

		assertEquals(1, userList.size());
		assertThat(userList.get(0).getId().getId()).isEqualTo(7);
	}

	@Test
	@Sql(scripts = {"/sql/clearUserRoles.sql",
			"/sql/clearAddresses.sql",
			"/sql/clearRoles.sql",
			"/sql/clearUsers.sql",
			"/sql/createUsers.sql"})
	public void should_return_both_active_and_in_active_users_for_given_tenant_and_name_containing() {
		UserSearchCriteria searchCriteria = UserSearchCriteria.builder()
				.name("Ram")
				.tenantId("ap.public")
				.fuzzyLogic(true)
				.active(null)
				.build();
		FuzzyNameMatchingSpecification fuzzyNameMatchingSpecification =
				new FuzzyNameMatchingSpecification(searchCriteria);

		List<User> userList = userJpaRepository.findAll(fuzzyNameMatchingSpecification);

		assertEquals(4, userList.size());
	}

	@Test
	@Sql(scripts = {"/sql/clearUserRoles.sql",
			"/sql/clearAddresses.sql",
			"/sql/clearRoles.sql",
			"/sql/clearUsers.sql",
			"/sql/createUsers.sql"})
	public void multi_field_matching_query_test() {
		UserSearchCriteria userSearch = UserSearchCriteria.builder()
				.name("Sreerama Krishnan")
				.mobileNumber("9731123456")
				.tenantId("ap.public")
				.emailId("email5@gmail.com")
				.pan("ABCDE1234F")
				.aadhaarNumber("12346789011")
				.active(true)
				.build();
		MultiFieldsMatchingSpecification multiFieldsMatchingSpecification =
				new MultiFieldsMatchingSpecification(userSearch);

		List<User> userList = userJpaRepository.findAll(multiFieldsMatchingSpecification);

		assertThat(userList.size()).isEqualTo(1);
		assertThat(userList.get(0).getId().getId()).isEqualTo(5);
	}

	@Test
	@Sql(scripts = {"/sql/clearUserRoles.sql",
			"/sql/clearAddresses.sql",
			"/sql/clearRoles.sql",
			"/sql/clearUsers.sql",
			"/sql/createUsers.sql"})
	public void test_should_find_user_by_id_and_tenant_id() {
		User actualUser = userJpaRepository.findByUserKeyIdAndUserKeyTenantId(1L, "ap.public");

		assertNotNull(actualUser);
	}

	@Test
	@Sql(scripts = {"/sql/clearUserRoles.sql",
			"/sql/clearAddresses.sql",
			"/sql/clearRoles.sql",
			"/sql/clearUsers.sql",
			"/sql/createUsers.sql"})
	public void test_should_return_null_when_user_for_given_id_and_tenant_id_does_not_exist() {
		User actualUser = userJpaRepository.findByUserKeyIdAndUserKeyTenantId(1L, "unknown");

		assertNull(actualUser);
	}

	@Test
	@Sql(scripts = {"/sql/clearUserRoles.sql",
			"/sql/clearAddresses.sql",
			"/sql/clearRoles.sql",
			"/sql/clearUsers.sql",
			"/sql/createUsers.sql"})
	public void multi_field_matching_query_user_id_matching_test() {
		UserSearchCriteria userSearch = UserSearchCriteria.builder()
				.id(asList(1L, 2L))
				.active(true)
				.tenantId("ap.public")
				.build();
		MultiFieldsMatchingSpecification multiFieldsMatchingSpecification =
				new MultiFieldsMatchingSpecification(userSearch);

		List<User> users = userJpaRepository.findAll(multiFieldsMatchingSpecification);

		assertEquals(2, users.size());
		assertThat(users.get(0).getId()).isNotNull();
		assertThat(users.get(0).getGender()).isEqualTo(Gender.FEMALE);
		assertThat(users.get(0).getType()).isEqualTo(UserType.EMPLOYEE);
		assertThat(users.get(0).getBloodGroup()).isEqualTo(BloodGroup.A_POSITIVE);

		assertThat(users.get(1).getId()).isNotNull();
		assertThat(users.get(1).getGender()).isEqualTo(Gender.OTHERS);
		assertThat(users.get(1).getType()).isEqualTo(UserType.CITIZEN);
		assertThat(users.get(1).getBloodGroup()).isEqualTo(BloodGroup.AB_POSITIVE);
	}

	@Test
	@Sql(scripts = {"/sql/clearUserRoles.sql",
			"/sql/clearAddresses.sql",
			"/sql/clearRoles.sql",
			"/sql/clearUsers.sql",
			"/sql/createUsers.sql"})
	public void multi_field_matching_query_should_return_all_active_users_for_given_tenant() {
		UserSearchCriteria userSearch = UserSearchCriteria.builder()
				.active(true)
				.tenantId("ap.public")
				.build();
		MultiFieldsMatchingSpecification multiFieldsMatchingSpecification =
				new MultiFieldsMatchingSpecification(userSearch);

		List<User> users = userJpaRepository.findAll(multiFieldsMatchingSpecification);

		assertEquals(5, users.size());
	}

	@Test
	@Sql(scripts = {"/sql/clearUserRoles.sql",
			"/sql/clearAddresses.sql",
			"/sql/clearRoles.sql",
			"/sql/clearUsers.sql",
			"/sql/createUsers.sql"})
	public void multi_field_matching_query_should_return_all_in_active_users_for_given_tenant() {
		UserSearchCriteria userSearch = UserSearchCriteria.builder()
				.active(false)
				.tenantId("ap.public")
				.build();
		MultiFieldsMatchingSpecification multiFieldsMatchingSpecification =
				new MultiFieldsMatchingSpecification(userSearch);

		List<User> users = userJpaRepository.findAll(multiFieldsMatchingSpecification);

		assertEquals(1, users.size());
	}

	@Test
	@Sql(scripts = {"/sql/clearUserRoles.sql",
			"/sql/clearAddresses.sql",
			"/sql/clearRoles.sql",
			"/sql/clearUsers.sql",
			"/sql/createUsers.sql"})
	public void multi_field_matching_query_should_return_both_active_and_in_active_users_for_given_tenant() {
		UserSearchCriteria userSearch = UserSearchCriteria.builder()
				.active(null)
				.tenantId("ap.public")
				.build();
		MultiFieldsMatchingSpecification multiFieldsMatchingSpecification =
				new MultiFieldsMatchingSpecification(userSearch);

		List<User> users = userJpaRepository.findAll(multiFieldsMatchingSpecification);

		assertEquals(6, users.size());
	}


	@Test
	@Sql(scripts = {"/sql/clearUserRoles.sql",
			"/sql/clearAddresses.sql",
			"/sql/clearRoles.sql",
			"/sql/clearUsers.sql",
			"/sql/createUsers.sql"})
	public void multi_field_matching_negative_test() {
		UserSearchCriteria userSearch = UserSearchCriteria.builder()
				.name("Sreerama Krishnan")
				.mobileNumber("9731123456")
				.emailId("email5@gmail.com")
				.pan("ABCDE1234F")
				.aadhaarNumber("notMatching")
				.active(true)
				.build();
		MultiFieldsMatchingSpecification multiFieldsMatchingSpecification =
				new MultiFieldsMatchingSpecification(userSearch);

		List<User> userList = userJpaRepository.findAll(multiFieldsMatchingSpecification);

		assertThat(userList).isEmpty();
	}

	@Test
	@Sql(scripts = {"/sql/clearUserRoles.sql",
			"/sql/clearAddresses.sql",
			"/sql/clearRoles.sql",
			"/sql/clearUsers.sql",
			"/sql/createUsers.sql"})
	public void multi_field_matching_empty_request_test() {
		UserSearchCriteria userSearch = UserSearchCriteria.builder().active(true).tenantId("ap.public").build();
		MultiFieldsMatchingSpecification multiFieldsMatchingSpecification =
				new MultiFieldsMatchingSpecification(userSearch);

		List<User> userList = userJpaRepository.findAll(multiFieldsMatchingSpecification);

		assertThat(userList).hasSize(5);
	}

	@Test
	@Sql(scripts = {"/sql/clearUserRoles.sql",
			"/sql/clearAddresses.sql",
			"/sql/clearRoles.sql",
			"/sql/clearUsers.sql",
			"/sql/createUsers.sql",
			"/sql/createRoles.sql",
			"/sql/createUserRoles.sql"
	})
	public void multi_field_matching_user_type_test() {
		UserSearchCriteria userSearch = UserSearchCriteria.builder()
				.active(true)
				.tenantId("ap.public")
				.type("EMPLOYEE")
				.roleCodes(Arrays.asList("EMP", "GRO"))
				.build();
		MultiFieldsMatchingSpecification multiFieldsMatchingSpecification =
				new MultiFieldsMatchingSpecification(userSearch);

		List<User> userList = userJpaRepository.findAll(multiFieldsMatchingSpecification);

		assertThat(userList).hasSize(1);
	}
}
