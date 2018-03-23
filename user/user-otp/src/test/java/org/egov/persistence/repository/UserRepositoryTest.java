package org.egov.persistence.repository;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.egov.domain.model.User;
import org.egov.persistence.contract.UserSearchRequest;
import org.egov.persistence.contract.UserSearchResponseContent;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.client.RestTemplate;

@RunWith(MockitoJUnitRunner.class)
public class UserRepositoryTest {

	private String HOST = "http://localhost:8081/";
	private String SEARCH_USER_URL = "user/_search";

	@InjectMocks
	private UserRepository userRepository;

	@Mock
	private RestTemplate restTemplate;

	@Before
	public void before() {

		ReflectionTestUtils.setField(userRepository, "SEARCH_USER_URL", "user/_search");
		ReflectionTestUtils.setField(userRepository, "HOST", "http://localhost:8081/");
	}

	@Test
	public void test_should_throw_exception_when_user_does_not_exist_for_given_user_name() {
		List<UserSearchResponseContent> list = new ArrayList<UserSearchResponseContent>();
		UserSearchResponseContent searchContent = new UserSearchResponseContent(1l, "test@gmail.com", "123456789");
		list.add(searchContent);
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("user", list);
		UserSearchRequest request = Mockito.mock(UserSearchRequest.class);

		when(restTemplate.postForObject(HOST + SEARCH_USER_URL, request, Map.class)).thenReturn(map);

		User actualUser = userRepository.fetchUser("123456789", "tenantId");

		final User expectedUser = new User(1L, "test@gmail.com", "123456789");

		assertEquals(actualUser, null);
	}

}