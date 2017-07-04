package org.egov.eis.service;

import org.egov.common.contract.request.RequestInfo;
import org.egov.eis.web.contract.UserGetRequest;
import org.egov.eis.web.contract.UserResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;
import org.springframework.web.client.RestTemplate;

@Repository
public class UserService {
	private RestTemplate restTemplate;
	private String userHost;
	private String userServiceUrl;

	public UserService(RestTemplate restTemplate, @Value("${egov.services.users_service.hostname}") final String userHostUrl,
			@Value("${egov.services.users_service.users.searchpath}") final String userServiceUrl) {
		this.restTemplate = restTemplate;
		this.userHost = userHostUrl;
		this.userServiceUrl = userServiceUrl;
	}

	public UserResponse findUserByUserNameAndTenantId(final RequestInfo requestInfo) {
                String url = String.format("%s%s", userHost, userServiceUrl);
				UserGetRequest userGetRequest = new UserGetRequest();
				userGetRequest.setRequestInfo(requestInfo);
				userGetRequest.setUserName(requestInfo.getUserInfo().getUserName());
				userGetRequest.setTenantId(requestInfo.getUserInfo().getTenantId());

                return restTemplate.postForObject(url, userGetRequest, UserResponse.class);
        }
}
