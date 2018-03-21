package org.egov.persistence.repository;

import org.egov.domain.exception.UserNotFoundException;
import org.egov.domain.model.User;
import org.egov.persistence.contract.UserSearchRequest;
import org.egov.persistence.contract.UserSearchResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class UserRepository {
	private final String searchUserUrl;
	private final RestTemplate restTemplate;

	public UserRepository(RestTemplate restTemplate, @Value("${user.host}") String userHost,
			@Value("${search.user.url}") String searchUserUrl) {
		this.restTemplate = restTemplate;
		this.searchUserUrl = userHost + searchUserUrl;
	}

	public User fetchUser(String mobileNumber, String tenantId) {
		final UserSearchRequest request = new UserSearchRequest(mobileNumber, tenantId);

		UserSearchResponse userSearchResponse = null;
		try {
			userSearchResponse = restTemplate.postForObject(searchUserUrl, request, UserSearchResponse.class);
		} catch (Exception e) {
			e.printStackTrace();
		}
		if (null != userSearchResponse && userSearchResponse.isMatchingUserPresent()) {
			return userSearchResponse.toDomainUser();
		} else {
			throw new UserNotFoundException();
		}
	}
}
