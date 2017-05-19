package org.pgr.batch.repository;

import org.egov.common.contract.request.User;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.web.client.RestTemplate;

import static org.junit.Assert.assertEquals;
import static org.springframework.test.web.client.ExpectedCount.once;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.method;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;

@RunWith(MockitoJUnitRunner.class)
public class UserRepositoryTest {

    private static final String HOST = "http://host";
    private static final String USER_BY_USERNAME_URL = "/user/_search";

    private MockRestServiceServer server;
    private UserRepository userRepository;

    @Before
    public void before() {
        final RestTemplate restTemplate = new RestTemplate();
        userRepository = new UserRepository(restTemplate, HOST, USER_BY_USERNAME_URL);
        server = MockRestServiceServer.bindTo(restTemplate).build();
    }

    @Test
    public void testShouldGetUserByUserName() {
        server.expect(once(), requestTo("http://host/user/_search")).andExpect(method(HttpMethod.POST)).andRespond(
                withSuccess(new Resources().getFileContents("userResponse.json"), MediaType.APPLICATION_JSON_UTF8));

        final User user = userRepository.getUserByUserName("system","tenantId");
        server.verify();
        assertEquals("system", user.getUserName());

    }
}