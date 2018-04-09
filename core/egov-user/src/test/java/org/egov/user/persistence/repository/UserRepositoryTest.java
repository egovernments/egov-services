package org.egov.user.persistence.repository;

import org.egov.user.domain.exception.InvalidRoleCodeException;
import org.egov.user.domain.model.Address;
import org.egov.user.domain.model.User;
import org.egov.user.domain.model.UserSearchCriteria;
import org.egov.user.persistence.entity.Role;
import org.egov.user.persistence.entity.RoleKey;
import org.egov.user.persistence.entity.UserKey;
import org.egov.user.persistence.specification.UserSearchSpecificationFactory;
import org.hamcrest.CustomMatcher;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.math.BigInteger;
import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.*;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.argThat;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class UserRepositoryTest {

	@Mock
	private UserJpaRepository userJpaRepository;

	@Mock
	private UserSearchSpecificationFactory userSearchSpecificationFactory;

	@Mock
	private RoleJpaRepository roleJpaRepository;

	@Mock
	private AddressRepository addressRepository;

	@Mock
	private PasswordEncoder passwordEncoder;

	@Mock
	private EntityManager entityManager;

	@InjectMocks
	private UserRepository userRepository;

	@Test
	public void test_should_return_true_when_user_exists_with_given_user_name_id_and_tenant() {
		when(userJpaRepository.isUserPresent("userName", 1L, "ap.public")).thenReturn(1L);

		boolean isPresent = userRepository.isUserPresent("userName", 1L, "ap.public");

		assertTrue(isPresent);
	}

	@Test
	public void test_should_return_true_when_user_exists_with_given_user_name_and_tenant() {
		when(userJpaRepository.isUserPresent("userName",  "ap.public")).thenReturn(1L);

		boolean isPresent = userRepository.isUserPresent("userName", "ap.public");

		assertTrue(isPresent);
	}

	@Test
	public void test_should_return_false_when_user_does_not_exist_with_given_user_name_id_and_tenant() {
		when(userJpaRepository.isUserPresent("userName", 1L, "ap.public")).thenReturn(0L);

		boolean isPresent = userRepository.isUserPresent("userName", 1L, "ap.public");

		assertFalse(isPresent);
	}

	@Test
	public void test_should_return_false_when_user_does_not_exist_with_given_user_name_and_tenant() {
		when(userJpaRepository.isUserPresent("userName", "ap.public")).thenReturn(0L);

		boolean isPresent = userRepository.isUserPresent("userName", "ap.public");

		assertFalse(isPresent);
	}

	@Test
	public void test_get_user_by_userName() {
		User expectedUser = mock(User.class);
		final org.egov.user.persistence.entity.User entityUser = mock(org.egov.user.persistence.entity.User.class);
		when(entityUser.toDomain(null, null)).thenReturn(expectedUser);
		when(userJpaRepository.findByUsernameAndUserKeyTenantId("userName", "tenantId"))
				.thenReturn(entityUser);

		User actualUser = userRepository.findByUsername("userName", "tenantId");

		assertThat(expectedUser).isEqualTo(actualUser);
	}

	@Test
	public void test_get_user_by_emailId() {
		User expectedUser = mock(User.class);
		final org.egov.user.persistence.entity.User entityUser = mock(org.egov.user.persistence.entity.User.class);
		when(entityUser.toDomain(null, null)).thenReturn(expectedUser);
		when(userJpaRepository.findByEmailIdAndUserKeyTenantId("userName", "tenantId"))
				.thenReturn(entityUser);

		User actualUser = userRepository.findByEmailId("userName", "tenantId");

		assertThat(expectedUser).isEqualTo(actualUser);
	}

	@Test
	public void test_should_save_entity_user() {
		org.egov.user.persistence.entity.User entityUser = mock(org.egov.user.persistence.entity.User.class);
		when(entityUser.getId()).thenReturn(new UserKey(1L, "ap.public"));
		when(userJpaRepository.save(any(org.egov.user.persistence.entity.User.class))).thenReturn(entityUser);
		final User expectedUser = mock(User.class);
		when(expectedUser.getTenantId()).thenReturn("ap.public");
		when(entityUser.toDomain(null, null)).thenReturn(expectedUser);
		final List<org.egov.user.domain.model.Role> roles = new ArrayList<>();
		final String roleCode = "roleCode1";
		roles.add(org.egov.user.domain.model.Role.builder().code(roleCode).build());
		org.egov.user.domain.model.User domainUser = org.egov.user.domain.model.User.builder()
				.roles(roles)
				.tenantId("ap.public")
				.build();
		final Role role = new Role();
		when(roleJpaRepository.findByRoleKeyTenantIdAndCodeIgnoreCase("ap.public", roleCode)).thenReturn(role);
		mockEntityManager();
		User actualUser = userRepository.create(domainUser);

		assertEquals(expectedUser, actualUser);
	}

	@Test
	public void test_should_save_correspondence_address_on_creating_new_user() {
		org.egov.user.persistence.entity.User entityUser = mock(org.egov.user.persistence.entity.User.class);
		when(entityUser.getId()).thenReturn(new UserKey(1L, "tenant"));
		final String tenantId = "ap.public";
		when(entityUser.getId()).thenReturn(new UserKey(1L, tenantId));
		when(userJpaRepository.save(any(org.egov.user.persistence.entity.User.class))).thenReturn(entityUser);
		final User expectedUser = mock(User.class);
		when(expectedUser.getTenantId()).thenReturn(tenantId);
		when(entityUser.toDomain(null, null)).thenReturn(expectedUser);
		final List<org.egov.user.domain.model.Role> roles = new ArrayList<>();
		final String roleCode = "roleCode1";
		roles.add(org.egov.user.domain.model.Role.builder().code(roleCode).build());
		final Address correspondenceAddress = mock(Address.class);
		org.egov.user.domain.model.User domainUser = org.egov.user.domain.model.User.builder()
				.roles(roles)
				.tenantId(tenantId)
				.correspondenceAddress(correspondenceAddress)
				.build();
		final Role role = new Role();
		when(roleJpaRepository.findByRoleKeyTenantIdAndCodeIgnoreCase(tenantId, roleCode)).thenReturn(role);
		mockEntityManager();

		userRepository.create(domainUser);

		verify(addressRepository).create(correspondenceAddress, 1L, tenantId);
	}

	@Test
	public void test_should_save_permanent_address_on_creating_new_user() {
		org.egov.user.persistence.entity.User entityUser = mock(org.egov.user.persistence.entity.User.class);
		when(entityUser.getId()).thenReturn(new UserKey(1L, "ap.public"));
		final String tenantId = "ap.public";
		when(entityUser.getId()).thenReturn(new UserKey(1L, tenantId));
		when(userJpaRepository.save(any(org.egov.user.persistence.entity.User.class))).thenReturn(entityUser);
		final User expectedUser = mock(User.class);
		when(expectedUser.getTenantId()).thenReturn(tenantId);
		when(entityUser.toDomain(null, null)).thenReturn(expectedUser);
		final List<org.egov.user.domain.model.Role> roles = new ArrayList<>();
		final String roleCode = "roleCode1";
		roles.add(org.egov.user.domain.model.Role.builder().code(roleCode).build());
		final Address permanentAddress = mock(Address.class);
		org.egov.user.domain.model.User domainUser = org.egov.user.domain.model.User.builder()
				.roles(roles)
				.tenantId(tenantId)
				.permanentAddress(permanentAddress)
				.build();
		final Role role = new Role();
		when(roleJpaRepository.findByRoleKeyTenantIdAndCodeIgnoreCase(tenantId, roleCode)).thenReturn(role);
		mockEntityManager();

		userRepository.create(domainUser);

		verify(addressRepository).create(permanentAddress, 1L, tenantId);
	}

	@Test(expected = InvalidRoleCodeException.class)
	public void test_should_throw_exception_when_role_does_not_exist_for_given_role_code() {
		org.egov.user.persistence.entity.User entityUser = mock(org.egov.user.persistence.entity.User.class);
		final User expectedUser = mock(User.class);
		when(entityUser.toDomain(null, null)).thenReturn(expectedUser);
		final String roleCode = "roleCode1";
		final org.egov.user.domain.model.Role domainRole = org.egov.user.domain.model.Role.builder()
						.name(roleCode)
						.build();
		org.egov.user.domain.model.User domainUser = org.egov.user.domain.model.User.builder()
				.roles(Collections.singletonList(domainRole))
				.build();
		when(roleJpaRepository.findByRoleKeyTenantIdAndCodeIgnoreCase("ap.public", roleCode)).thenReturn(null);

		userRepository.create(domainUser);
	}

	@Test
	public void test_should_set_encrypted_password_to_new_user() {
		org.egov.user.persistence.entity.User expectedUser = mock(org.egov.user.persistence.entity.User.class);
		when(expectedUser.getId()).thenReturn(new UserKey(1L, "ap.public"));
		when(userJpaRepository.save(any(org.egov.user.persistence.entity.User.class))).thenReturn(expectedUser);
		final List<org.egov.user.domain.model.Role> roles = new ArrayList<>();
		final String roleCode = "roleCode1";
		roles.add(org.egov.user.domain.model.Role.builder().code(roleCode).build());
		final String rawPassword = "rawPassword";
		org.egov.user.domain.model.User domainUser = org.egov.user.domain.model.User.builder()
				.roles(roles)
				.password(rawPassword)
				.tenantId("ap.public")
				.build();
		final Role role = new Role();
		when(roleJpaRepository.findByRoleKeyTenantIdAndCodeIgnoreCase("ap.public", roleCode))
				.thenReturn(role);
		final String expectedEncodedPassword = "encodedPassword";
		when(passwordEncoder.encode(rawPassword)).thenReturn(expectedEncodedPassword);
		mockEntityManager();

		userRepository.create(domainUser);

		verify(userJpaRepository).save(argThat(new UserWithPasswordMatcher(expectedEncodedPassword)));
	}

	@Test
	public void test_should_save_new_user_when_enriched_roles() {
		org.egov.user.persistence.entity.User expectedUser = mock(org.egov.user.persistence.entity.User.class);
		when(expectedUser.getId()).thenReturn(new UserKey(1L, "ap.public"));

		when(userJpaRepository.save(any(org.egov.user.persistence.entity.User.class))).thenReturn(expectedUser);
		final List<org.egov.user.domain.model.Role> roles = new ArrayList<>();
		final String roleName1 = "roleName1";
		final String roleName2 = "roleName2";
		roles.add(org.egov.user.domain.model.Role.builder().code(roleName1).tenantId("ap.public").build());
		roles.add(org.egov.user.domain.model.Role.builder().code(roleName2).tenantId("ap.public").build());
		org.egov.user.domain.model.User domainUser = org.egov.user.domain.model.User.builder()
				.roles(roles)
				.tenantId("ap.public")
				.build();
		final Role role1 = Role.builder().roleKey(new RoleKey(1L, "ap.public")).build();
		final Role role2 = Role.builder().roleKey(new RoleKey(2L, "ap.public")).build();
		when(roleJpaRepository.findByRoleKeyTenantIdAndCodeIgnoreCase("ap.public", roleName1)).thenReturn(role1);
		when(roleJpaRepository.findByRoleKeyTenantIdAndCodeIgnoreCase("ap.public", roleName2)).thenReturn(role2);
		mockEntityManager();
		userRepository.create(domainUser);

		final HashSet<Role> expectedRoles = new HashSet<>(Arrays.asList(role1, role2));
		verify(userJpaRepository).save(argThat(new UserWithRolesMatcher(expectedRoles)));
	}

	@Test
	public void test_search_user() {
		Page<org.egov.user.persistence.entity.User> page = mock(Page.class);
		org.egov.user.persistence.entity.User mockUserEntity = mock(org.egov.user.persistence.entity.User.class);
		when(mockUserEntity.getId()).thenReturn(new UserKey(1L, "ap.public"));
		org.egov.user.domain.model.User mockUserModel = mock(org.egov.user.domain.model.User.class);
		when(mockUserEntity.toDomain(null, null)).thenReturn(mockUserModel);
		List<org.egov.user.persistence.entity.User> listOfEntities = Collections.singletonList(mockUserEntity);
		List<org.egov.user.domain.model.User> listOfModels = Collections.singletonList(mockUserModel);
		UserSearchCriteria userSearch = mock(UserSearchCriteria.class);
		Specification<org.egov.user.persistence.entity.User> userSpecification = mock(Specification.class);
		when(userSearch.getPageNumber()).thenReturn(1);
		when(userSearch.getPageSize()).thenReturn(20);
		when(userSearch.getSort()).thenReturn(Arrays.asList("name", "userName", "unknownField", "fourthField"));
		Sort sort = new Sort(Sort.Direction.ASC, "name", "username");
		PageRequest pageRequest = new PageRequest(1, 20, sort);
		when(userSearchSpecificationFactory.getSpecification(userSearch)).thenReturn(userSpecification);
		when(userJpaRepository.findAll(userSpecification, pageRequest)).thenReturn(page);
		when(page.getContent()).thenReturn(listOfEntities);

		List<org.egov.user.domain.model.User> actualList = userRepository.findAll(userSearch);

		assertThat(listOfModels).isEqualTo(actualList);
	}

	private class UserWithPasswordMatcher extends CustomMatcher<org.egov.user.persistence.entity.User> {

		private String expectedPassword;

		UserWithPasswordMatcher(String expectedPassword) {
			super("User password matcher");
			this.expectedPassword = expectedPassword;
		}

		@Override
		public boolean matches(Object o) {
			final org.egov.user.persistence.entity.User actualUser = (org.egov.user.persistence.entity.User) o;
			return expectedPassword.equals(actualUser.getPassword());
		}
	}

	private class UserWithRolesMatcher extends CustomMatcher<org.egov.user.persistence.entity.User> {

		private HashSet<Role> expectedRoles;

		UserWithRolesMatcher(HashSet<Role> expectedRoles) {
			super("User roles matcher");
			this.expectedRoles = expectedRoles;
		}

		@Override
		public boolean matches(Object o) {
			final org.egov.user.persistence.entity.User acutalUser = (org.egov.user.persistence.entity.User) o;
			return expectedRoles.equals(acutalUser.getRoles());
		}
	}

	@Test
	public void test_should_update_entity_user() {
		org.egov.user.persistence.entity.User entityUser = mock(org.egov.user.persistence.entity.User.class);
		when(userJpaRepository.save(any(org.egov.user.persistence.entity.User.class))).thenReturn(entityUser);
		when(userJpaRepository.findByUserKeyIdAndUserKeyTenantId(1L, "tenantId")).thenReturn(entityUser);
		final User expectedUser = mock(User.class);
		when(entityUser.toDomain(null, null)).thenReturn(expectedUser);
		final List<org.egov.user.domain.model.Role> roles = new ArrayList<>();
		org.egov.user.domain.model.User domainUser = org.egov.user.domain.model.User.builder()
				.roles(roles)
				.id(1L)
				.tenantId("tenantId")
				.build();

		User actualUser = userRepository.update(domainUser);

		assertEquals(expectedUser, actualUser);
	}

	@Test
	public void test_should_update_addresses_when_updating_user() {
		org.egov.user.persistence.entity.User entityUser = mock(org.egov.user.persistence.entity.User.class);
		when(userJpaRepository.save(any(org.egov.user.persistence.entity.User.class))).thenReturn(entityUser);
		when(userJpaRepository.findByUserKeyIdAndUserKeyTenantId(1L, "tenant")).thenReturn(entityUser);
		final User expectedUser = mock(User.class);
		when(entityUser.toDomain(null, null)).thenReturn(expectedUser);
		org.egov.user.domain.model.User domainUser = mock(User.class);
		final List<Address> expectedAddresses = Collections.singletonList(Address.builder().build());
		when(domainUser.getAddresses()).thenReturn(expectedAddresses);
		when(domainUser.getId()).thenReturn(1L);
		when(domainUser.getTenantId()).thenReturn("tenant");

		userRepository.update(domainUser);

		verify(addressRepository).update(expectedAddresses, 1L, "tenant");
	}

	@Test(expected = InvalidRoleCodeException.class)
	public void test_should_throw_exception_when_updating_user_with_invalid_role_code() {
		org.egov.user.persistence.entity.User entityUser = mock(org.egov.user.persistence.entity.User.class);
		when(entityUser.getId()).thenReturn(new UserKey(1L, "tenantId"));
		when(userJpaRepository.save(any(org.egov.user.persistence.entity.User.class))).thenReturn(entityUser);
		when(userJpaRepository.findByUserKeyIdAndUserKeyTenantId(1L, "tenantId")).thenReturn(entityUser);
		final User expectedUser = mock(User.class);
		when(entityUser.toDomain(null, null)).thenReturn(expectedUser);
		final org.egov.user.domain.model.Role role1 = org.egov.user.domain.model.Role.builder()
				.code("roleCode1")
				.build();
		final List<org.egov.user.domain.model.Role> roles = Collections.singletonList(role1);
		final HashSet<Role> roleSet = new HashSet<>(Collections.singletonList(Role.builder()
				.code("roleCode1")
				.build()));
		when(entityUser.getRoles()).thenReturn(roleSet);
		org.egov.user.domain.model.User domainUser = org.egov.user.domain.model.User.builder()
				.roles(roles)
				.id(1L)
				.tenantId("tenantId")
				.build();
		when(roleJpaRepository.findByRoleKeyTenantIdAndCodeIgnoreCase("tenantId", "roleCode1"))
				.thenReturn(null);

		userRepository.update(domainUser);
	}

	@Test
	public void test_should_return_user() {
		org.egov.user.persistence.entity.User entityUser = mock(org.egov.user.persistence.entity.User.class);
		final User expectedUser = mock(User.class);
		when(entityUser.toDomain(null, null)).thenReturn(expectedUser);
		when(userJpaRepository.findByUserKeyIdAndUserKeyTenantId(123L, "tenantId")).thenReturn(entityUser);

		User actualUser = userRepository.getUserById(123L, "tenantId");

		assertEquals(expectedUser, actualUser);
	}


	private void mockEntityManager() {
		final Query query = mock(Query.class);
		when(query.getSingleResult()).thenReturn(BigInteger.ONE);
		when(entityManager.createNativeQuery(anyString())).thenReturn(query);
	}
}