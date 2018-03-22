package org.egov.persistence.repository;

import org.egov.Resources;
import org.egov.domain.exception.UserNotFoundException;
import org.egov.domain.model.User;
import org.junit.Before;
import org.junit.Test;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.web.client.RestTemplate;

import static org.junit.Assert.assertEquals;
import static org.springframework.test.web.client.ExpectedCount.once;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.*;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;

public class UserRepositoryTest {

	private static final String HOST = "http://host";
	private static final String SEARCH_USER_URL = "/user/_search";
	private Resources resources = new Resources();
	private MockRestServiceServer server;
	private UserRepository userRepository;

	@Before
	public void before() {
		RestTemplate restTemplate = new RestTemplate();
		userRepository = new UserRepository(restTemplate, HOST, SEARCH_USER_URL);
		server = MockRestServiceServer.bindTo(restTemplate).build();
	}

	@Test
	public void test_should_return_user_for_given_user_name() {
		server.expect(once(), requestTo("http://host/user/_search"))
				.andExpect(method(HttpMethod.POST))
				.andExpect(content().string(resources.getFileContents("userSearchRequest.json")))
				.andRespond(
						withSuccess(resources.getFileContents("userSearchSuccessResponse.json"),
								MediaType.APPLICATION_JSON_UTF8));
		final User expectedUser = new User(1L, "foo@bar.com","123");

		final User actualUser = userRepository.fetchUser("mobileNumber", "tenantId");

		assertEquals(expectedUser, actualUser);
	}

/*	@Test(expected = UserNotFoundException.class)
	public void test_should_throw_exception_when_user_does_not_exist_for_given_user_name() {
		server.expect(once(), requestTo("http://host/user/_search"))
				.andExpect(method(HttpMethod.POST))
				.andExpect(content().string(resources.getFileContents("userSearchRequest.json")))
				.andRespond(
						withSuccess(resources.getFileContents("userSearchEmptyResponse.json"),
								MediaType.APPLICATION_JSON_UTF8));
		userRepository.fetchUser("mobileNumber", "tenantId");
	}*/

}