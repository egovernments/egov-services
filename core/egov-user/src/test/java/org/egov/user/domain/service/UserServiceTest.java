package org.egov.user.domain.service;

import org.egov.user.domain.exception.DuplicateUserNameException;
import org.egov.user.domain.exception.InvalidUserException;
import org.egov.user.domain.exception.OtpValidationPendingException;
import org.egov.user.domain.model.UserSearch;
import org.egov.user.persistence.entity.Role;
import org.egov.user.persistence.entity.User;
import org.egov.user.domain.model.enums.Gender;
import org.egov.user.domain.model.enums.UserType;
import org.egov.user.persistence.repository.OtpRepository;
import org.egov.user.persistence.repository.UserRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
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
public class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private OtpRepository otpRepository;

    @InjectMocks
    private UserService userService;

    private final List<Long> ID = Arrays.asList(1L, 2L);
    private final String EMAIL = "email@gmail.com";
    private final String USER_NAME = "userName";

    @Test
    public void test_should_get_user_by_email() throws Exception {
        when(userRepository.findByEmailId(EMAIL)).thenReturn(getUserObject());

        User actualUser = userService.getUserByEmailId(EMAIL);

        assertThat(actualUser.getEmailId()).isEqualTo(EMAIL);
    }

    @Test
    public void test_should_get_user_by_username() throws Exception {
        when(userRepository.findByUsername(USER_NAME)).thenReturn(getUserObject());

        User actualUser = userService.getUserByUsername(USER_NAME);

        assertThat(actualUser.getUsername()).isEqualTo(USER_NAME);
    }

    @Test
    public void test_should_search_user() throws Exception {
        UserSearch userSearch = new UserSearch();
        List<org.egov.user.domain.model.User> expectedListOfUsers = mock(List.class);
        when(userRepository.findAll(userSearch)).thenReturn(expectedListOfUsers);

        List<org.egov.user.domain.model.User> actualResult = userService.searchUsers(userSearch);

        assertThat(expectedListOfUsers).isEqualTo(actualResult);
    }

    @Test
    public void test_should_save_a_valid_user() throws Exception {
        org.egov.user.domain.model.User domainUser = validDomainUser();
        when(otpRepository.isOtpValidationComplete(domainUser)).thenReturn(true);
        final User expectedEntityUser = new User();
        when(userRepository.save(domainUser)).thenReturn(expectedEntityUser);

        User returnedUser = userService.save(domainUser, Boolean.TRUE);

        assertEquals(expectedEntityUser, returnedUser);
    }

    @Test(expected = DuplicateUserNameException.class)
    public void test_should_raise_exception_when_duplicate_user_name_exists() throws Exception {
        org.egov.user.domain.model.User domainUser = validDomainUser();
        when(otpRepository.isOtpValidationComplete(domainUser)).thenReturn(true);
        when(userRepository.isUserPresent("supandi_rocks")).thenReturn(true);

        userService.save(domainUser, true);
    }


    @Test(expected = OtpValidationPendingException.class)
    public void test_exception_is_raised_when_otp_validation_fails() throws Exception {
        org.egov.user.domain.model.User domainUser = validDomainUserWithRole();
        when(otpRepository.isOtpValidationComplete(domainUser)).thenReturn(false);

        userService.save(domainUser, true);
    }

    @Test
    public void test_otp_is_not_validated_when_validation_flag_is_false() throws Exception {
        org.egov.user.domain.model.User domainUser = validDomainUserWithRole();
        when(otpRepository.isOtpValidationComplete(domainUser)).thenReturn(false);

        userService.save(domainUser, false);

        verify(otpRepository, never()).isOtpValidationComplete(domainUser);
    }

    @Test(expected = InvalidUserException.class)
    public void test_should_raise_exception_when_user_is_invalid() throws Exception {
        org.egov.user.domain.model.User domainUser = org.egov.user.domain.model.User.builder().build();

        userService.save(domainUser, true);
        verify(userRepository, never()).save(any(org.egov.user.domain.model.User.class));
    }

    private org.egov.user.domain.model.User validDomainUser() {
        return getUserBuilder().build();
    }

    private org.egov.user.domain.model.User validDomainUserWithRole() {
        Role role = new Role();
        role.setName("CITIZEN");
        Set<Role> roles = new HashSet<>();
        roles.add(role);
        return getUserBuilder().build();
    }

    private org.egov.user.domain.model.User.UserBuilder getUserBuilder() {
        return org.egov.user.domain.model.User.builder().username("supandi_rocks")
                .name("Supandi").gender(Gender.MALE).type(UserType.CITIZEN)
                .active(Boolean.TRUE).mobileNumber("9988776655").accountLocked(false);
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