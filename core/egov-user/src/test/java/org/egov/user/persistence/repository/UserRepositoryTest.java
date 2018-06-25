package org.egov.user.persistence.repository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.egov.user.domain.exception.InvalidRoleCodeException;
import org.egov.user.domain.model.Address;
import org.egov.user.domain.model.Role;
import org.egov.user.domain.model.User;
import org.egov.user.domain.model.UserSearchCriteria;
import org.egov.user.domain.model.enums.AddressType;
import org.egov.user.domain.model.enums.BloodGroup;
import org.egov.user.domain.model.enums.Gender;
import org.egov.user.repository.builder.UserTypeQueryBuilder;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
public class UserRepositoryTest {

	@Autowired
	private AddressRepository addressRepository;

	@Autowired
	private PasswordEncoder passwordEncoder;

	@Autowired
	private RoleRepository roleRepository;

	@Autowired
	private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

	@Autowired
	private JdbcTemplate jdbcTemplate;

	@Autowired
	private UserTypeQueryBuilder userTypeQueryBuilder;

	private UserRepository userRepository;

	@Before
	public void before() {
		userRepository = new UserRepository(roleRepository, userTypeQueryBuilder, passwordEncoder, addressRepository,
				jdbcTemplate, namedParameterJdbcTemplate);
	}

	@Test
	@Sql(scripts = { "/sql/clearUserRoles.sql","/sql/clearUsers.sql", "/sql/createUsers.sql" })
	public void test_should_return_true_when_user_exists_with_given_user_name_id_and_tenant() {
		boolean isPresent = userRepository.isUserPresent("bigcat399", 2L, "ap.public");

		assertTrue(isPresent);
	}

	@Test
	@Sql(scripts = { "/sql/clearUserRoles.sql","/sql/clearUsers.sql", "/sql/createUsers.sql" })
	public void test_should_return_true_when_user_exists_with_given_user_name_and_tenant() {
		boolean isPresent = userRepository.isUserPresent("bigcat399", "ap.public");

		assertTrue(isPresent);
	}

	@Test
	public void test_should_return_false_when_user_does_not_exist_with_given_user_name_id_and_tenant() {
		boolean isPresent = userRepository.isUserPresent("userName", 1L, "ap.public");

		assertFalse(isPresent);
	}

	@Test
	public void test_should_return_false_when_user_does_not_exist_with_given_user_name_and_tenant() {
		boolean isPresent = userRepository.isUserPresent("userName", "ap.public");

		assertFalse(isPresent);
	}

	@Test
	@Sql(scripts = { "/sql/clearUserRoles.sql","/sql/clearUsers.sql", "/sql/createUsers.sql" })
	public void test_get_user_by_userName() {
		User user = userRepository.findByUsername("bigcat399", "ap.public");
		assertThat(user.getId().equals(1l));
		assertThat(user.getUsername().equals("bigcat399"));
		assertThat(user.getMobileNumber().equals("9731123456"));
		assertThat(user.getEmailId().equals("kay.alexander@example.com"));
		assertThat(user.getTenantId().equals("ap.public"));
	}

	@Test
	@Sql(scripts = { "/sql/clearUserRoles.sql","/sql/clearUsers.sql","/sql/createUsers.sql" })
	public void test_get_user_by_emailId() {
		User user = userRepository.findByEmailId("kay.alexander@example.com", "ap.public");
		assertThat(user.getId().equals(1l));
		assertThat(user.getEmailId().equals("kay.alexander@example.com"));
		assertThat(user.getTenantId().equals("ap.public"));
	}

	@Test
	@Sql(scripts = { "/sql/clearUserRoles.sql", "/sql/clearUsers.sql", "/sql/clearRoles.sql", "/sql/createRoles.sql" })
	public void test_should_save_entity_user() {
		final List<Role> roles = new ArrayList<>();
		final String roleCode = "EMP";
		roles.add(Role.builder().code(roleCode).build());
		User domainUser = User.builder().roles(roles).name("test1").username("TestUserName").password("password")
				.emailId("Test@gmail.com").aadhaarNumber("AadharNumber").mobileNumber("1234567890").active(true)
				.gender(Gender.FEMALE).bloodGroup(BloodGroup.A_NEGATIVE).accountLocked(true).loggedInUserId(10l)
				.createdBy(10l).tenantId("ap.public").build();
		User actualUser = userRepository.create(domainUser);

		assertThat(actualUser != null);
		assertThat(actualUser.getId().equals(1l));
		assertThat(actualUser.getRoles().size() == 1l);
		assertThat(actualUser.getUsername().equals("TestUserName"));
		assertThat(actualUser.getEmailId().equals("Test@gmail.com"));
		assertThat(actualUser.getAadhaarNumber().equals("AadharNumber"));
		assertThat(actualUser.getMobileNumber().equals("1234567890"));
		assertThat(actualUser.getGender().toString().equals("FEMALE"));
		assertThat(actualUser.getCreatedBy().equals(10l));
		assertThat(actualUser.getLastModifiedBy().equals(10l));
		assertThat(actualUser.getTenantId().equals("ap.public"));
	}

	@Test
	@Sql(scripts = { "/sql/clearUserRoles.sql", "/sql/clearUsers.sql", "/sql/clearRoles.sql", "/sql/createRoles.sql",
			"/sql/clearAddresses.sql" })
	public void test_should_save_correspondence_address_on_creating_new_user() {
		Address correspondenceAddress = Address.builder().address("address").type(AddressType.CORRESPONDENCE)
				.addressType("CORRESPONDENCE").city("city").pinCode("123").build();
		final List<Role> roles = new ArrayList<>();
		final String roleCode = "EMP";
		roles.add(Role.builder().code(roleCode).build());
		User domainUser = User.builder().roles(roles)
				.username("TestUserName").password("password").tenantId("ap.public")
				.correspondenceAddress(correspondenceAddress).build();
		User actualUser = userRepository.create(domainUser);

		assertThat(actualUser != null);
		assertThat(actualUser.getId().equals(1l));
		assertThat(actualUser.getRoles().size() == 1l);
		assertThat(actualUser.getUsername().equals("TestUserName"));
		assertThat(actualUser.getTenantId().equals("ap.public"));
		assertThat(actualUser.getCorrespondenceAddress() != null);
		assertThat(actualUser.getCorrespondenceAddress().getAddressType().toString().equals("CORRESPONDENCE"));
		assertThat(actualUser.getCorrespondenceAddress().getCity().equals("city"));
		assertThat(actualUser.getCorrespondenceAddress().getAddress().equals("address"));
		assertThat(actualUser.getCorrespondenceAddress().getPinCode().equals("123"));
	}

	@Test
	@Sql(scripts = { "/sql/clearUserRoles.sql", "/sql/clearUsers.sql", "/sql/clearRoles.sql", "/sql/createRoles.sql",
			"/sql/clearAddresses.sql" })
	public void test_should_save_permanent_address_on_creating_new_user() {
		Address permanentAddress = Address.builder().address("address").type(AddressType.PERMANENT)
				.addressType("PERMANENT").city("city").pinCode("123").build();
		final List<Role> roles = new ArrayList<>();
		final String roleCode = "EMP";
		roles.add(Role.builder().code(roleCode).build());
		User domainUser = User.builder().roles(roles)
				.username("TestUserName").password("password").tenantId("ap.public").permanentAddress(permanentAddress)
				.build();
		User actualUser = userRepository.create(domainUser);

		assertThat(actualUser != null);
		assertThat(actualUser.getId().equals(1l));
		assertThat(actualUser.getRoles().size() == 1l);
		assertThat(actualUser.getUsername().equals("TestUserName"));
		assertThat(actualUser.getTenantId().equals("ap.public"));
		assertThat(actualUser.getPermanentAddress() != null);
		assertThat(actualUser.getPermanentAddress().getAddressType().toString().equals("PERMANENT"));
		assertThat(actualUser.getPermanentAddress().getCity().equals("city"));
		assertThat(actualUser.getPermanentAddress().getAddress().equals("address"));
		assertThat(actualUser.getPermanentAddress().getPinCode().equals("123"));
	}

	@Test(expected = InvalidRoleCodeException.class)
	public void test_should_throw_exception_when_role_does_not_exist_for_given_role_code() {
		final String roleCode = "roleCode1";
		final org.egov.user.domain.model.Role domainRole = org.egov.user.domain.model.Role.builder().name(roleCode)
				.build();
		User domainUser = User.builder()
				.roles(Collections.singletonList(domainRole)).build();
		userRepository.create(domainUser);
	}

	@Test
	@Sql(scripts = { "/sql/clearUserRoles.sql", "/sql/clearUsers.sql", "/sql/clearRoles.sql", "/sql/createRoles.sql",
			"/sql/clearAddresses.sql" })
	public void test_should_set_encrypted_password_to_new_user() {
		final List<org.egov.user.domain.model.Role> roles = new ArrayList<>();
		final String roleCode = "EMP";
		roles.add(org.egov.user.domain.model.Role.builder().code(roleCode).build());
		final String rawPassword = "rawPassword";
		User domainUser = User.builder().roles(roles)
				.username("Test UserName").password(rawPassword).tenantId("ap.public").build();
		User actualUser = userRepository.create(domainUser);
		assertThat(actualUser != null);
		assertThat(actualUser.getId().equals(1l));
		assertThat(actualUser.getPassword().equals("$2a$10$begnxh5azaFpAv0yDe7sQ./uDzp2H4Xy7SrEmY/9JV2qB/cHFha5m"));
	}

	@Test
	@Sql(scripts = { "/sql/clearUserRoles.sql", "/sql/clearUsers.sql", "/sql/clearRoles.sql", "/sql/createRoles.sql",
			"/sql/clearAddresses.sql" })
	public void test_should_save_new_user_when_enriched_roles() {

		final List<org.egov.user.domain.model.Role> roles = new ArrayList<>();
		roles.add(Role.builder().code("EMP").tenantId("ap.public").build());
		roles.add(Role.builder().code("EADMIN").tenantId("ap.public").build());
		User domainUser = User.builder().roles(roles).username("Test UserName").password("pasword")
				.tenantId("ap.public").build();
		User actualUser = userRepository.create(domainUser);
		assertThat(actualUser != null);
		assertThat(actualUser.getId().equals(1l));
		assertThat(actualUser.getRoles().size() == 2);
	}

	@Test
	@Sql(scripts = { "/sql/clearUserRoles.sql", "/sql/clearUsers.sql", "/sql/clearRoles.sql", "/sql/createRoles.sql",
			"/sql/clearAddresses.sql", "/sql/createUsers.sql" })
	public void test_search_user_bytenant() {
		UserSearchCriteria userSearch = UserSearchCriteria.builder().tenantId("ap.public").build();
		List<User> actualList = userRepository.findAll(userSearch);
		assertThat(actualList.size() == 6);
	}
	
	@Test
	@Sql(scripts = { "/sql/clearUserRoles.sql", "/sql/clearUsers.sql", "/sql/clearRoles.sql", "/sql/createRoles.sql",
			"/sql/clearAddresses.sql", "/sql/createUsers.sql" })
	public void test_search_user_byId() {
		
		List<Long> idList = new ArrayList<Long>();
		idList.add(1l);
		idList.add(2l);
		idList.add(3l);
		UserSearchCriteria userSearch = UserSearchCriteria.builder().tenantId("ap.public").id(idList).build();
		List<User> actualList = userRepository.findAll(userSearch);
		assertThat(actualList.size() == 3);
	}
	
	@Test
	@Sql(scripts = { "/sql/clearUserRoles.sql", "/sql/clearUsers.sql", "/sql/clearRoles.sql", "/sql/createRoles.sql",
			"/sql/clearAddresses.sql", "/sql/createUsers.sql" })
	public void test_search_user_byemail() {

		UserSearchCriteria userSearch = UserSearchCriteria.builder().tenantId("ap.public").emailId("kay.alexander@example.com").build();
		List<User> actualList = userRepository.findAll(userSearch);
		assertThat(actualList.size() == 1);
	}
	
	@Test
	@Sql(scripts = { "/sql/clearUserRoles.sql", "/sql/clearUsers.sql", "/sql/clearRoles.sql", "/sql/createRoles.sql",
			"/sql/clearAddresses.sql", "/sql/createUsers.sql" })
	public void test_search_user_byUsername() {

		UserSearchCriteria userSearch = UserSearchCriteria.builder().tenantId("ap.public").userName("bigcat399").build();
		List<User> actualList = userRepository.findAll(userSearch);
		assertThat(actualList.size() == 1);
	}

	@Test
	@Sql(scripts = { "/sql/clearUserRoles.sql", "/sql/clearUsers.sql", "/sql/clearRoles.sql", "/sql/createRoles.sql",
			"/sql/clearAddresses.sql", "/sql/createUsers.sql" })
	public void test_search_user_byName() {

		UserSearchCriteria userSearch = UserSearchCriteria.builder().tenantId("ap.public").name("Kay Alexander").build();
		List<User> actualList = userRepository.findAll(userSearch);
		assertThat(actualList.size() == 1);
	}
	
	@Test
	@Sql(scripts = { "/sql/clearUserRoles.sql", "/sql/clearUsers.sql", "/sql/clearRoles.sql", "/sql/createRoles.sql",
			"/sql/clearAddresses.sql", "/sql/createUsers.sql" })
	public void test_search_user_bymobilenumber() {

		UserSearchCriteria userSearch = UserSearchCriteria.builder().tenantId("ap.public").mobileNumber("9731123456").build();
		List<User> actualList = userRepository.findAll(userSearch);
		assertThat(actualList.size() == 7);
	}
	
	@Test
	@Sql(scripts = { "/sql/clearUserRoles.sql", "/sql/clearUsers.sql", "/sql/clearRoles.sql", "/sql/createRoles.sql",
			"/sql/clearAddresses.sql", "/sql/createUsers.sql" })
	public void test_search_user_byadharnumberumber() {

		UserSearchCriteria userSearch = UserSearchCriteria.builder().tenantId("ap.public").aadhaarNumber("12346789011").build();
		List<User> actualList = userRepository.findAll(userSearch);
		assertThat(actualList.size() == 7);
	}
	
	@Test
	@Sql(scripts = { "/sql/clearUserRoles.sql", "/sql/clearUsers.sql", "/sql/clearRoles.sql", "/sql/createRoles.sql",
			"/sql/clearAddresses.sql", "/sql/createUsers.sql" })
	public void test_search_user_bypan() {

		UserSearchCriteria userSearch = UserSearchCriteria.builder().tenantId("ap.public").pan("ABCDE1234F").build();
		List<User> actualList = userRepository.findAll(userSearch);
		assertThat(actualList.size() == 7);
	}
	
	@Ignore
	@Sql(scripts = { "/sql/clearUserRoles.sql", "/sql/clearUsers.sql", "/sql/clearRoles.sql", "/sql/createRoles.sql",
			"/sql/clearAddresses.sql", "/sql/createUsers.sql" })
	public void test_search_user_bytype() {

		UserSearchCriteria userSearch = UserSearchCriteria.builder().tenantId("ap.public").type("EMPLOYEE").build();
		List<User> actualList = userRepository.findAll(userSearch);
		assertThat(actualList.size() == 7);
	}
	
	@Test
	@Sql(scripts = { "/sql/clearUserRoles.sql", "/sql/clearUsers.sql", "/sql/clearRoles.sql", "/sql/createRoles.sql",
			"/sql/clearAddresses.sql", "/sql/createUsers.sql" })
	public void test_search_user_bytenantid() {

		UserSearchCriteria userSearch = UserSearchCriteria.builder().tenantId("ap.public").pageSize(2).build();
		List<User> actualList = userRepository.findAll(userSearch);
		assertThat(actualList.size() == 2);
	}

	@Ignore
	@Test
	public void test_should_update_entity_user() {
		final List<Role> roles = new ArrayList<>();
		final String roleCode = "EMP";
		roles.add(Role.builder().code(roleCode).build());
		User domainUser = User.builder().roles(roles).name("test1").id(1l).username("TestUserName").password("password")
				.emailId("Test@gmail.com").aadhaarNumber("AadharNumber").mobileNumber("1234567890").active(true)
				.gender(Gender.FEMALE).bloodGroup(BloodGroup.A_NEGATIVE).accountLocked(true).loggedInUserId(10l)
				.createdBy(10l).tenantId("ap.public").build();
		User actualUser = userRepository.update(domainUser);

		assertThat(actualUser != null);
		assertThat(actualUser.getId().equals(1l));
		assertThat(actualUser.getRoles().size() == 1l);
		assertThat(actualUser.getUsername().equals("TestUserName"));
		assertThat(actualUser.getEmailId().equals("Test@gmail.com"));
		assertThat(actualUser.getAadhaarNumber().equals("AadharNumber"));
		assertThat(actualUser.getGender().toString().equals("FEMALE"));
		assertThat(actualUser.getCreatedBy().equals(10l));
		assertThat(actualUser.getLastModifiedBy().equals(10l));
		assertThat(actualUser.getTenantId().equals("ap.public"));
	}
	
	@Ignore
	@Test(expected = InvalidRoleCodeException.class)
	public void test_should_throw_exception_when_updating_user_with_invalid_role_code() {
		final String roleCode = "roleCode1";
		final org.egov.user.domain.model.Role domainRole = org.egov.user.domain.model.Role.builder().name(roleCode)
				.build();
		User domainUser = User.builder()
				.roles(Collections.singletonList(domainRole)).id(1l).tenantId("ap.public").build();
		userRepository.update(domainUser);
	}

	@Test
	public void test_should_return_user() {

		User actualUser = userRepository.getUserById(123L, "tenantId");
		assertThat(actualUser != null);
	}

}
