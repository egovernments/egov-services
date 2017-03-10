package org.egov.user.domain.service;

import org.egov.user.domain.exception.InvalidUserException;
import org.egov.user.persistence.entity.Role;
import org.egov.user.persistence.entity.User;
import org.egov.user.persistence.entity.enums.Gender;
import org.egov.user.persistence.entity.enums.UserType;
import org.egov.user.persistence.repository.RoleRepository;
import org.egov.user.persistence.repository.UserRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class UserRequestServiceTest {

    @Mock
    UserRepository userRepository;
    @Mock
    RoleRepository roleRepository;
    @Captor
    private ArgumentCaptor<User> userCaptor;

    UserService userService;

    private final List<Long> ID = Arrays.asList(1L, 2L);
    private final String EMAIL = "email@gmail.com";
    private final String USER_NAME = "userName";

    @Before
    public void setUp() {
        userService = new UserService(userRepository, roleRepository);
    }

    @Test
    public void shouldGetUsersById() throws Exception {
        when(userRepository.findAll(ID)).thenReturn(getListOfUsers());

        List<User> user = userService.getUsersById(ID);

        assertThat(user.get(0).getId()).isEqualTo(ID.get(0));
        assertThat(user.get(1).getId()).isEqualTo(ID.get(1));
    }

    @Test
    public void shouldGetUserByEmail() throws Exception {
        when(userRepository.findByEmailId(EMAIL)).thenReturn(getUserObject());

        User actualUser = userService.getUserByEmailId(EMAIL);

        assertThat(actualUser.getEmailId()).isEqualTo(EMAIL);
    }

    @Test
    public void shouldGetUserByUsername() throws Exception {
        when(userRepository.findByUsername(USER_NAME)).thenReturn(getUserObject());

        User actualUser = userService.getUserByUsername(USER_NAME);

        assertThat(actualUser.getUsername()).isEqualTo(USER_NAME);
    }

    @Test
    public void shouldSaveAValidUser() throws Exception {
        org.egov.user.domain.model.User domainUser = validDomainUser();
        User entityUser = new User().fromDomain(domainUser);
        when(userRepository.save(userCaptor.capture())).thenReturn(entityUser);
        User returnedUser = userService.save(domainUser);

        assertEquals(entityUser, returnedUser);
        assertEquals(entityUser.getUsername(), userCaptor.getValue().getUsername());
        assertEquals(entityUser.getMobileNumber(), userCaptor.getValue().getMobileNumber());
    }

    @Test
    public void testShouldResolveRolesBeforeSavingUser() throws Exception {
        Role mockRole = mock(Role.class);
        org.egov.user.domain.model.User domainUser = validDomainUserWithRole();
        User entityUser = new User().fromDomain(domainUser);
        when(userRepository.save(userCaptor.capture())).thenReturn(entityUser);
        when(roleRepository.findByName("CITIZEN")).thenReturn(mockRole);
        userService.save(domainUser);

        assertEquals(mockRole, userCaptor.getValue().getRoles().iterator().next());
    }

    @Test
    public void shouldNotAttemptToResolveRolesWhenNonePresent() throws Exception {
        org.egov.user.domain.model.User domainUser = validDomainUser();
        userService.save(domainUser);

        verify(roleRepository, never()).findByName(any(String.class));
    }

    @Test(expected = InvalidUserException.class)
    public void shouldRaiseExceptionWhenUserIsInvalid() throws Exception {
        org.egov.user.domain.model.User domainUser = org.egov.user.domain.model.User.builder().build();

        userService.save(domainUser);
        verify(userRepository, never()).save(any(User.class));
    }

    private org.egov.user.domain.model.User validDomainUser() {
        return getUserBuilder().build();
    }

    private org.egov.user.domain.model.User validDomainUserWithRole() {
        Role role = new Role();
        role.setName("CITIZEN");
        Set<Role> roles = new HashSet<>();
        roles.add(role);
        return getUserBuilder().roles(roles).build();
    }

    private org.egov.user.domain.model.User.UserBuilder getUserBuilder() {
        return org.egov.user.domain.model.User.builder().username("supandi_rocks")
                .name("Supandi").gender(Gender.MALE).type(UserType.CITIZEN)
                .active(Boolean.TRUE).mobileNumber("9988776655").accountLocked(Boolean.FALSE);
    }

    private List<User> getListOfUsers() {
        return Arrays.asList(
                User.builder()
                        .id(ID.get(0))
                        .emailId(EMAIL)
                        .username(USER_NAME)
                        .build(),

                User.builder()
                        .id(ID.get(1))
                        .emailId(EMAIL)
                        .username(USER_NAME)
                        .build()
        );
    }

    private User getUserObject() {
        return User.builder()
                .id(ID.get(0))
                .emailId(EMAIL)
                .username(USER_NAME)
                .build();
    }
}