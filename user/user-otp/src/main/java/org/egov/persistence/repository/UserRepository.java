package org.egov.persistence.repository;

import java.util.Map;

import org.egov.domain.exception.UserNotFoundException;
import org.egov.domain.model.User;
import org.egov.persistence.contract.UserSearchRequest;
import org.egov.persistence.contract.UserSearchResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

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
		ObjectMapper mapper = new ObjectMapper();
		mapper.configure(MapperFeature.ACCEPT_CASE_INSENSITIVE_PROPERTIES, true);
		mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
		mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
		try {
			Object response = restTemplate.postForObject(searchUserUrl, request, Map.class);
			userSearchResponse = mapper.convertValue(response, UserSearchResponse.class);
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
