package org.egov.user.persistence.repository;

import org.egov.user.domain.model.UserSearch;
import org.egov.user.persistence.entity.Role;
import org.egov.user.persistence.entity.User;
import org.egov.user.persistence.specification.UserSearchSpecificationFactory;
import org.hamcrest.CustomMatcher;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertEquals;
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
    public void test_get_user_by_userName() throws Exception {
        User expectedUser = mock(User.class);

        when(userJpaRepository.findByUsername("userName")).thenReturn(expectedUser);

        User actualUser = userRepository.findByUsername("userName");

        assertThat(expectedUser).isEqualTo(actualUser);
    }

    @Test
    public void test_get_user_by_emailId() throws Exception {
        User expectedUser = mock(User.class);

        when(userJpaRepository.findByEmailId("userName")).thenReturn(expectedUser);

        User actualUser = userRepository.findByEmailId("userName");

        assertThat(expectedUser).isEqualTo(actualUser);
    }

    @Test
    public void test_should_save_entity_user() throws Exception {
        User expectedUser = mock(User.class);
        when(userJpaRepository.save(any(User.class))).thenReturn(expectedUser);
        final HashSet<Role> roles = new HashSet<>();
        final String roleName = "roleName1";
        roles.add(Role.builder().name(roleName).build());
        org.egov.user.domain.model.User domainUser = org.egov.user.domain.model.User.builder()
                .roles(roles)
                .build();
        final Role role = new Role();
        when(roleRepository.findByNameContainingIgnoreCase(roleName)).thenReturn(role);

        User actualUser = userRepository.save(domainUser);

        assertEquals(expectedUser, actualUser);
    }

    @Test
    public void test_should_set_encrypted_password_to_new_user() throws Exception {
        User expectedUser = mock(User.class);
        when(userJpaRepository.save(any(User.class))).thenReturn(expectedUser);
        final HashSet<Role> roles = new HashSet<>();
        final String roleName = "roleName1";
        roles.add(Role.builder().name(roleName).build());
        final String rawPassword = "rawPassword";
        org.egov.user.domain.model.User domainUser = org.egov.user.domain.model.User.builder()
                .roles(roles)
                .password(rawPassword)
                .build();
        final Role role = new Role();
        when(roleRepository.findByNameContainingIgnoreCase(roleName)).thenReturn(role);
        final String expectedEncodedPassword = "encodedPassword";
        when(passwordEncoder.encode(rawPassword)).thenReturn(expectedEncodedPassword);

        userRepository.save(domainUser);

        verify(userJpaRepository).save(argThat(new UserWithPasswordMatcher(expectedEncodedPassword)));
    }

    @Test
    public void test_should_save_new_user_when_enriched_roles() throws Exception {
        User expectedUser = mock(User.class);
        when(userJpaRepository.save(any(User.class))).thenReturn(expectedUser);
        final HashSet<Role> roles = new HashSet<>();
        final String roleName1 = "roleName1";
        final String roleName2 = "roleName2";
        roles.add(Role.builder().name(roleName1).build());
        roles.add(Role.builder().name(roleName2).build());
        org.egov.user.domain.model.User domainUser = org.egov.user.domain.model.User.builder()
                .roles(roles)
                .build();
        final Role role1 = Role.builder().id(1L).build();
        final Role role2 = Role.builder().id(2L).build();
        when(roleRepository.findByNameContainingIgnoreCase(roleName1)).thenReturn(role1);
        when(roleRepository.findByNameContainingIgnoreCase(roleName2)).thenReturn(role2);

        userRepository.save(domainUser);

        final HashSet<Role> expectedRoles = new HashSet<>(Arrays.asList(role1, role2));
        verify(userJpaRepository).save(argThat(new UserWithRolesMatcher(expectedRoles)));
    }

    @Test
    public void test_search_user() throws Exception {
        List<User> expectedList = mock(List.class);
        UserSearch userSearch = mock(UserSearch.class);
        Specification<User> userSpecification = mock(Specification.class);

        when(userSearchSpecificationFactory.getSpecification(userSearch)).thenReturn(userSpecification);
        when(userJpaRepository.findAll(userSpecification)).thenReturn(expectedList);

        List<User> actualList = userRepository.findAll(userSearch);

        assertThat(expectedList).isEqualTo(actualList);
    }

    private class UserWithPasswordMatcher extends CustomMatcher<User> {

        private String expectedPassword;

        UserWithPasswordMatcher(String expectedPassword) {
            super("User password matcher");
            this.expectedPassword = expectedPassword;
        }

        @Override
        public boolean matches(Object o) {
            final User actualUser = (User) o;
            return expectedPassword.equals(actualUser.getPassword());
        }
    }

    private class UserWithRolesMatcher extends CustomMatcher<User> {

        private HashSet<Role> expectedRoles;

        UserWithRolesMatcher(HashSet<Role> expectedRoles) {
            super("User roles matcher");
            this.expectedRoles = expectedRoles;
        }

        @Override
        public boolean matches(Object o) {
            final User acutalUser = (User) o;
            return expectedRoles.equals(acutalUser.getRoles());
        }
    }
}