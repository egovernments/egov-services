package org.pgr.batch.service;

import org.egov.common.contract.request.User;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.pgr.batch.repository.UserRepository;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;

    @Test
    public void getUserByUserName() throws Exception {
        User expectedUser = User.builder()
                .userName("system")
                .build();
        when(userRepository.getUserByUserName("system","tenantId")).thenReturn(expectedUser);

        User user = userService.getUserByUserName("system","tenantId");

        assertEquals(user.getUserName(),expectedUser.getUserName());
    }
}