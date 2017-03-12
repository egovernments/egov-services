package org.egov.user.domain.service;

import org.egov.user.domain.exception.InvalidUserException;
import org.egov.user.domain.exception.OtpValidationPendingException;
import org.egov.user.persistence.entity.Role;
import org.egov.user.domain.model.UserSearch;
import org.egov.user.domain.search.SearchStrategy;
import org.egov.user.domain.search.UserSearchStrategyFactory;
import org.egov.user.persistence.entity.User;
import org.egov.user.persistence.entity.enums.Gender;
import org.egov.user.persistence.entity.enums.UserType;
import org.egov.user.persistence.repository.RoleRepository;
import org.egov.user.persistence.repository.UserRepository;
import org.egov.user.web.contract.RequestInfo;
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
import static org.junit.Assert.fail;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class UserServiceTest {

    @Mock
    UserRepository userRepository;
    @Mock
    RoleRepository roleRepository;
    @Mock
    RequestInfo requestInfo;
    @Mock
    OtpService otpService;
    @Captor
    private ArgumentCaptor<User> userCaptor;

    @Mock
    UserSearchStrategyFactory userSearchStrategyFactory;

    UserService userService;

    private final List<Long> ID = Arrays.asList(1L, 2L);
    private final String EMAIL = "email@gmail.com";
    private final String USER_NAME = "userName";

    @Before
    public void setUp() {
        userService = new UserService(userRepository, roleRepository, otpService, userSearchStrategyFactory);
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
    public void shouldSearchUser() throws Exception {
        UserSearch userSearch = new UserSearch();
        List<User> expectedListOfUsers = getListOfUsers();
        SearchStrategy searchStrategy = mock(SearchStrategy.class);
        when(userSearchStrategyFactory.getSearchStrategy(userSearch)).thenReturn(searchStrategy);
        when(searchStrategy.search(userSearch)).thenReturn(expectedListOfUsers);

        List<User> actualResult = userService.searchUsers(userSearch);

        assertThat(expectedListOfUsers).isEqualTo(actualResult);
    }

    @Test
    public void shouldSaveAValidUser() throws Exception {
        org.egov.user.domain.model.User domainUser = validDomainUser();
        User entityUser = new User().fromDomain(domainUser);
        when(otpService.isOtpValidationComplete(requestInfo, domainUser)).thenReturn(Boolean.TRUE);
        when(userRepository.save(userCaptor.capture())).thenReturn(entityUser);
        User returnedUser = userService.save(requestInfo, domainUser, Boolean.TRUE);

        assertEquals(entityUser, returnedUser);
        assertEquals(entityUser.getUsername(), userCaptor.getValue().getUsername());
        assertEquals(entityUser.getMobileNumber(), userCaptor.getValue().getMobileNumber());
    }

    @Test
    public void testShouldResolveRolesBeforeSavingUser() throws Exception {
        Role mockRole = mock(Role.class);
        org.egov.user.domain.model.User domainUser = validDomainUserWithRole();
        User entityUser = new User().fromDomain(domainUser);
        when(otpService.isOtpValidationComplete(requestInfo, domainUser)).thenReturn(Boolean.TRUE);
        when(userRepository.save(userCaptor.capture())).thenReturn(entityUser);
        when(roleRepository.findByNameContainingIgnoreCase("CITIZEN")).thenReturn(mockRole);
        userService.save(requestInfo, domainUser, Boolean.TRUE);

        assertEquals(mockRole, userCaptor.getValue().getRoles().iterator().next());
    }

    @Test
    public void shouldNotAttemptToResolveRolesWhenNonePresent() throws Exception {
        org.egov.user.domain.model.User domainUser = validDomainUser();
        when(otpService.isOtpValidationComplete(requestInfo, domainUser)).thenReturn(Boolean.TRUE);
        userService.save(requestInfo, domainUser, Boolean.TRUE);

        verify(roleRepository, never()).findByNameContainingIgnoreCase(any(String.class));
    }

    @Test
    public void testShouldEnsureOtpHasBeenValidated() throws Exception {
        org.egov.user.domain.model.User domainUser = validDomainUserWithRole();
        when(otpService.isOtpValidationComplete(requestInfo, domainUser)).thenReturn(Boolean.TRUE);
        userService.save(requestInfo, domainUser, Boolean.TRUE);

        verify(otpService, atLeastOnce()).isOtpValidationComplete(requestInfo, domainUser);
    }

    @Test(expected = OtpValidationPendingException.class)
    public void testIfOtpIsNotValidatedExceptionIsRaised() throws Exception {
        org.egov.user.domain.model.User domainUser = validDomainUserWithRole();
        when(otpService.isOtpValidationComplete(requestInfo, domainUser)).thenReturn(Boolean.FALSE);

        userService.save(requestInfo, domainUser, Boolean.TRUE);
    }

    @Test
    public void testOtpIsNotValidatedWhenEnsureValidationFlagIsValue() throws Exception {
        try {
            org.egov.user.domain.model.User domainUser = validDomainUserWithRole();
            when(otpService.isOtpValidationComplete(requestInfo, domainUser)).thenReturn(Boolean.FALSE);
            userService.save(requestInfo, domainUser, Boolean.FALSE);

            verify(otpService, never()).isOtpValidationComplete(requestInfo, domainUser);
        } catch(OtpValidationPendingException ovpe) {
            fail();
        }
    }

    @Test(expected = InvalidUserException.class)
    public void shouldRaiseExceptionWhenUserIsInvalid() throws Exception {
        org.egov.user.domain.model.User domainUser = org.egov.user.domain.model.User.builder().build();

        userService.save(requestInfo, domainUser, Boolean.TRUE);
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