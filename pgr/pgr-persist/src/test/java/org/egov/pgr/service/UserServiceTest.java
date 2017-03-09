package org.egov.pgr.service;

import org.egov.pgr.contracts.grievance.ResponseInfo;
import org.egov.pgr.contracts.user.GetUserByIdResponse;
import org.egov.pgr.contracts.user.User;
import org.egov.pgr.repository.UserRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.Collections;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class UserServiceTest {

    private static final String HOST = "http://host";

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;

    @Test
    public void getUserById() throws Exception {
        final User user = User.builder()
                .id(67L)
                .build();

        final GetUserByIdResponse expectedUserResponse = GetUserByIdResponse.builder()
                .user(Collections.singletonList(user))
                .responseInfo(new ResponseInfo())
                .build();
        when(userRepository.findUserById(67L)).thenReturn(expectedUserResponse);

        final GetUserByIdResponse actualUser = userService.getUserById(67L);

        assertEquals(expectedUserResponse.getUser().get(0).getId(),
                actualUser.getUser().get(0).getId());
    }
}