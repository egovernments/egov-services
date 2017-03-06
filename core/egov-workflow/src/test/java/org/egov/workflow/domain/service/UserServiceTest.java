package org.egov.workflow.domain.service;

import org.egov.workflow.persistence.repository.UserRepository;
import org.egov.workflow.web.contract.GetUserByIdResponse;
import org.egov.workflow.web.contract.ResponseInfo;
import org.egov.workflow.web.contract.User;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.Collections;

import static org.junit.Assert.*;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class UserServiceTest {

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