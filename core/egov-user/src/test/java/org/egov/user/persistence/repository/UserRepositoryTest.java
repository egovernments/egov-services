package org.egov.user.persistence.repository;

import org.egov.user.domain.model.User;
import org.egov.user.domain.model.UserSearch;
import org.egov.user.persistence.entity.Role;
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
	private RoleRepository roleRepository;

	@Mock
	private PasswordEncoder passwordEncoder;

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
		when(entityUser.toDomain()).thenReturn(expectedUser);
		when(userJpaRepository.findByUsername("userName")).thenReturn(entityUser);

		User actualUser = userRepository.findByUsername("userName");

		assertThat(expectedUser).isEqualTo(actualUser);
	}

	@Test
	public void test_get_user_by_emailId() {
		User expectedUser = mock(User.class);
		final org.egov.user.persistence.entity.User entityUser = mock(org.egov.user.persistence.entity.User.class);
		when(entityUser.toDomain()).thenReturn(expectedUser);
		when(userJpaRepository.findByEmailId("userName")).thenReturn(entityUser);

		User actualUser = userRepository.findByEmailId("userName");

		assertThat(expectedUser).isEqualTo(actualUser);
	}

	@Test
	public void test_should_save_entity_user() {
		org.egov.user.persistence.entity.User entityUser = mock(org.egov.user.persistence.entity.User.class);
		when(userJpaRepository.save(any(org.egov.user.persistence.entity.User.class))).thenReturn(entityUser);
		final User expectedUser = mock(User.class);
		when(entityUser.toDomain()).thenReturn(expectedUser);
		final List<org.egov.user.domain.model.Role> roles = new ArrayList<>();
		final String roleName = "roleName1";
		roles.add(org.egov.user.domain.model.Role.builder().name(roleName).build());
		org.egov.user.domain.model.User domainUser = org.egov.user.domain.model.User.builder()
				.roles(roles)
				.build();
		final Role role = new Role();
		when(roleRepository.findByTenantIdAndCodeIgnoreCase("ap.public", roleName)).thenReturn(role);

		User actualUser = userRepository.save(domainUser);

		assertEquals(expectedUser, actualUser);
	}

	@Test
	public void test_should_set_encrypted_password_to_new_user() {
		org.egov.user.persistence.entity.User expectedUser = mock(org.egov.user.persistence.entity.User.class);
		when(userJpaRepository.save(any(org.egov.user.persistence.entity.User.class))).thenReturn(expectedUser);
		final List<org.egov.user.domain.model.Role> roles = new ArrayList<>();
		final String roleName = "roleName1";
		roles.add(org.egov.user.domain.model.Role.builder().name(roleName).build());
		final String rawPassword = "rawPassword";
		org.egov.user.domain.model.User domainUser = org.egov.user.domain.model.User.builder()
				.roles(roles)
				.password(rawPassword)
				.build();
		final Role role = new Role();
		when(roleRepository.findByTenantIdAndCodeIgnoreCase("ap.public", roleName)).thenReturn(role);
		final String expectedEncodedPassword = "encodedPassword";
		when(passwordEncoder.encode(rawPassword)).thenReturn(expectedEncodedPassword);

		userRepository.save(domainUser);

		verify(userJpaRepository).save(argThat(new UserWithPasswordMatcher(expectedEncodedPassword)));
	}

	@Test
	public void test_should_save_new_user_when_enriched_roles() {
		org.egov.user.persistence.entity.User expectedUser = mock(org.egov.user.persistence.entity.User.class);
		when(userJpaRepository.save(any(org.egov.user.persistence.entity.User.class))).thenReturn(expectedUser);
		final List<org.egov.user.domain.model.Role> roles = new ArrayList<>();
		final String roleName1 = "roleName1";
		final String roleName2 = "roleName2";
		roles.add(org.egov.user.domain.model.Role.builder().name(roleName1).tenantId("ap.public").build());
		roles.add(org.egov.user.domain.model.Role.builder().name(roleName2).tenantId("ap.public").build());
		org.egov.user.domain.model.User domainUser = org.egov.user.domain.model.User.builder()
				.roles(roles)
				.tenantId("ap.public")
				.build();
		final Role role1 = Role.builder().id(1L).tenantId("ap.public").build();
		final Role role2 = Role.builder().id(2L).tenantId("ap.public").build();
		when(roleRepository.findByTenantIdAndCodeIgnoreCase("ap.public", roleName1)).thenReturn(role1);
		when(roleRepository.findByTenantIdAndCodeIgnoreCase("ap.public", roleName2)).thenReturn(role2);

		userRepository.save(domainUser);

		final HashSet<Role> expectedRoles = new HashSet<>(Arrays.asList(role1, role2));
		verify(userJpaRepository).save(argThat(new UserWithRolesMatcher(expectedRoles)));
	}

	@Test
	public void test_search_user() {
		Page<org.egov.user.persistence.entity.User> page = mock(Page.class);
		org.egov.user.persistence.entity.User mockUserEntity = mock(org.egov.user.persistence.entity.User.class);
		org.egov.user.domain.model.User mockUserModel = mock(org.egov.user.domain.model.User.class);
		when(mockUserEntity.toDomain()).thenReturn(mockUserModel);
		List<org.egov.user.persistence.entity.User> listOfEntities = Collections.singletonList(mockUserEntity);
		List<org.egov.user.domain.model.User> listOfModels = Collections.singletonList(mockUserModel);
		UserSearch userSearch = mock(UserSearch.class);
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
		when(userJpaRepository.findOne(any(Long.class))).thenReturn(entityUser);
		final User expectedUser = mock(User.class);
		when(entityUser.toDomain()).thenReturn(expectedUser);
		final List<org.egov.user.domain.model.Role> roles = new ArrayList<>();
		org.egov.user.domain.model.User domainUser = org.egov.user.domain.model.User.builder()
				.roles(roles)
				.build();

		User actualUser = userRepository.update(1L, domainUser);

		assertEquals(expectedUser, actualUser);
	}

	@Test
	public void test_should_return_user() {
		org.egov.user.persistence.entity.User entityUser = mock(org.egov.user.persistence.entity.User.class);
		when(userJpaRepository.findOne(any(Long.class))).thenReturn(entityUser);
		org.egov.user.persistence.entity.User actualUser = userRepository.getUserById(any(Long.class));

		assertEquals(entityUser, actualUser);
	}
}