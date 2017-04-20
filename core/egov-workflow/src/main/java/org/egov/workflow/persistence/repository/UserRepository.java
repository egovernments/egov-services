package org.egov.workflow.persistence.repository;

import java.util.Collections;

import org.egov.common.contract.request.RequestInfo;
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

	public UserResponse findUserByIdAndTenantId(Long userId,String tenantId) {
		String url = String.format("%s%s", userHost, userServiceUrl);
		UserRequest userRequest = UserRequest.builder().requestInfo(new RequestInfo())
				.id(Collections.singletonList(userId)).tenantId(tenantId).build();

		return restTemplate.postForObject(url, userRequest, UserResponse.class);
	}

}
