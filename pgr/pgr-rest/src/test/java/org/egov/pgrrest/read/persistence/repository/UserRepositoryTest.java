package org.egov.pgrrest.read.persistence.repository;

import static org.junit.Assert.assertEquals;
import static org.springframework.test.web.client.ExpectedCount.once;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.content;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.method;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;

import org.egov.pgrrest.common.persistence.repository.UserRepository;
import org.egov.pgrrest.common.domain.model.AuthenticatedUser;
import org.egov.pgrrest.common.contract.web.GetUserByIdResponse;
import org.egov.pgrrest.read.web.contract.User;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.web.client.RestTemplate;

@RunWith(MockitoJUnitRunner.class)
public class UserRepositoryTest {

    private static final String HOST = "http://host";
    private static final String USER_DETAILS_BY_ACCESSTOKEN = "/user/_details?access_token=";
    private static final String USER_BY_USERNAME_URL = "/user/_search";

    private MockRestServiceServer server;
    private UserRepository userRepository;

    @Before
    public void before() {
        final RestTemplate restTemplate = new RestTemplate();
        userRepository = new UserRepository(restTemplate, HOST, USER_DETAILS_BY_ACCESSTOKEN, USER_BY_USERNAME_URL);
        server = MockRestServiceServer.bindTo(restTemplate).build();
    }

    @Test
    public void testShouldGetUserByAccessToken() {
        server.expect(once(), requestTo("http://host/user/_details?access_token=access_token"))
                .andExpect(method(HttpMethod.POST))
                .andRespond(withSuccess(new Resources().getFileContents("authenticatedUserResponse.json"),
                        MediaType.APPLICATION_JSON_UTF8));

        final AuthenticatedUser user = userRepository.findUser("access_token");
        server.verify();
        assertEquals(1, user.getId().intValue());

    }

    @Test
    public void testShouldGetUserByUserName() {
        server.expect(once(), requestTo("http://host/user/_search")).andExpect(method(HttpMethod.POST)).andRespond(
                withSuccess(new Resources().getFileContents("userResponse.json"), MediaType.APPLICATION_JSON_UTF8));

        final User user = userRepository.getUserByUserName("user","tenantId");
        server.verify();
        assertEquals(1, user.getId().intValue());

    }

    @Test
    public void testFindUserById() throws Exception {
        server.expect(once(), requestTo("http://host/user/_search")).andExpect(method(HttpMethod.POST))
                .andExpect(content().string(new Resources().getFileContents("searchUserByIdRequest.json")))
                .andRespond(withSuccess(new Resources().getFileContents("searchUserById.json"),
                        MediaType.APPLICATION_JSON_UTF8));

        final GetUserByIdResponse userResponse = userRepository.findUserByIdAndTenantId(67L, "tenantId");
        server.verify();
        assertEquals(1, userResponse.getUser().size());

    }

}
