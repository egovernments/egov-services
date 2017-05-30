package org.egov.collection.persistence.repository;

import org.egov.collection.domain.model.AuthenticatedUser;
import org.springframework.web.client.RestTemplate;

public class UserRepository {

	private RestTemplate restTemplate;
	private String userServiceUrl;

	public UserRepository(RestTemplate restTemplate, String userServiceUrl) {
		this.restTemplate = restTemplate;
		this.userServiceUrl = userServiceUrl;
	}

	public AuthenticatedUser findUser(String token) {
		String url = String.format("%s%s", userServiceUrl, token);
		return restTemplate.getForObject(url, AuthenticatedUser.class);
	}

}
