package org.egov.pgr.persistence.repository;

import static org.springframework.util.StringUtils.isEmpty;

import org.egov.pgr.domain.model.AuthenticatedUser;
import org.egov.pgr.web.contract.User;
import org.egov.pgr.web.contract.UserRequest;
import org.egov.pgr.web.contract.UserResponse;
import org.springframework.web.client.RestTemplate;

public class UserRepository {

	private RestTemplate restTemplate;
	private String userHost;
	private String getUserDetailsUrl;
	private String getUserByUserNameUrl;

	public UserRepository(RestTemplate restTemplate, String userHostUrl, String getUserDetailsUrl,
			String getUserByUserNameUrl) {
		this.restTemplate = restTemplate;
		this.userHost = userHostUrl;
		this.getUserDetailsUrl = getUserDetailsUrl;
		this.getUserByUserNameUrl = getUserByUserNameUrl;
	}

	public AuthenticatedUser getUser(String token) {
		if (isEmpty(token)) {
			return AuthenticatedUser.createAnonymousUser();
		}
		return findUser(token);
	}

	public AuthenticatedUser findUser(String token) {
		String url = String.format("%s%s", userHost + getUserDetailsUrl, token);
		return restTemplate.postForObject(url, null, AuthenticatedUser.class);
	}

	public User getUserByUserName(String userName) {
		UserRequest request = new UserRequest();
		request.setUserName(userName);
		String url = userHost + getUserByUserNameUrl;
		return restTemplate.postForObject(url, request, UserResponse.class).getUser().get(0);
	}
}
