package org.egov.property.repository;

import org.egov.property.model.UserCreateRequest;
import org.egov.property.model.UserCreateResponse;
import org.egov.tracer.http.LogAwareRestTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class UserRestRepository {

	private LogAwareRestTemplate restTemplate;

	private String url;

	public UserRestRepository(LogAwareRestTemplate restTemplate,
			@Value("${egov.services.egov_user.hostname}") final String userHost,
			@Value("${egov.services.egov_user.basepath}") final String userBasePath,
			@Value("${egov.services.egov_user.createpath}") final String userCreate) {
		this.restTemplate = restTemplate;
		this.url = userHost + userBasePath + userCreate;
	}

	public UserCreateResponse createUser(UserCreateRequest request) throws Exception {

		return restTemplate.postForObject(url, request, UserCreateResponse.class);
	}
}
