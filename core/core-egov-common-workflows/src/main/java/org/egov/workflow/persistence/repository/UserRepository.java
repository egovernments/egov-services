package org.egov.workflow.persistence.repository;

import static org.springframework.util.StringUtils.isEmpty;

import java.util.Collections;

import org.egov.workflow.domain.model.AuthenticatedUser;
import org.egov.workflow.web.contract.RequestInfo;
import org.egov.workflow.web.contract.UserGetRequest;
import org.egov.workflow.web.contract.UserRequest;
import org.egov.workflow.web.contract.UserResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;
import org.springframework.web.client.RestTemplate;

@Repository
public class UserRepository {

	private RestTemplate restTemplate;
	private String userHost;
	private String userServiceUrl;

	public UserRepository(RestTemplate restTemplate, @Value("${user.service.url}") final String userHostUrl,
			@Value("${egov.services.user_by_id}") final String userServiceUrl) {
		this.restTemplate = restTemplate;
		this.userHost = userHostUrl;
		this.userServiceUrl = userServiceUrl;
	}

	public UserResponse findUserById(Long userId) {
		String url = String.format("%s%s", userHost, userServiceUrl);
		UserRequest userRequest = UserRequest.builder().requestInfo(new RequestInfo())
				.id(Collections.singletonList(userId)).build();

		return restTemplate.postForObject(url, userRequest, UserResponse.class);
	}
	
	public UserResponse findUserByUserNameAndTenantId(final RequestInfo requestInfo) {
                String url = String.format("%s%s", userHost, userServiceUrl);
                UserGetRequest userGetRequest = new UserGetRequest();
                userGetRequest.setRequestInfo(requestInfo);
                userGetRequest.setUserName(requestInfo.getUserInfo().getUserName());
                userGetRequest.setTenantId(requestInfo.getUserInfo().getTenantId());
    
                return restTemplate.postForObject(url, userGetRequest, UserResponse.class);
        }

	
	public AuthenticatedUser getUser(String token) {
		if (isEmpty(token)) {
			return AuthenticatedUser.createAnonymousUser();
		}
		return findUser(token);
	}

	public AuthenticatedUser findUser(String token) {
		String url = String.format("%s%s", userHost + userServiceUrl, token);
		return restTemplate.postForObject(url, null, AuthenticatedUser.class);
	}

	/*public User getUserByUserName(String userName) {
		UserRequest request = new UserRequest();
		request.setUserName(userName);
		String url = userHost + getUserByUserNameUrl;
		return restTemplate.postForObject(url, request, UserResponse.class).getUser().get(0);
	}*/
}
