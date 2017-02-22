package org.egov.user.domain.service;

import org.egov.user.persistence.entity.User;
import org.egov.user.persistence.repository.UserRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class UserServiceTest {

    @Mock
    UserRepository userRepository;

    UserService userService;

    private final List<Long> ID = Arrays.asList(1L, 2L);
    private final String EMAIL = "email@gmail.com";
    private final String USER_NAME = "userName";

    @Before
    public void setUp() {
        userService = new UserService(userRepository);
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