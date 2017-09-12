package org.egov.eis.indexer.service;

import org.egov.eis.indexer.config.PropertiesManager;
import org.egov.eis.web.contract.EmployeeRequest;
import org.egov.user.web.contract.UserRequest;
import org.egov.user.web.contract.UserResponse;
import org.egov.user.web.contract.UserSearchRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@Service
public class UserService {

	@Autowired
	private PropertiesManager propertiesManager;

	@Autowired
	private RestTemplate restTemplate;

	public static final Logger LOGGER = LoggerFactory.getLogger(UserService.class);

	public List<UserRequest> getUser(List<Long> ids, EmployeeRequest employeeRequest) {
		UserSearchRequest userSearchRequest = getUserSearchRequest(ids, employeeRequest);

		UserResponse userResponse;
		try {
			URI url = new URI(propertiesManager.getEgovUserServiceHost()
					+ propertiesManager.getEgovUserServiceUserBasepath()
					+ propertiesManager.getEgovUserServiceUserSearchPath());
			LOGGER.info(url.toString());
			userResponse = restTemplate.postForObject(url, userSearchRequest, UserResponse.class);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}

		return userResponse.getUser();
	}

	private UserSearchRequest getUserSearchRequest(List<Long> ids, EmployeeRequest employeeRequest) {
		return UserSearchRequest.builder()
				.requestInfo(employeeRequest.getRequestInfo())
				.id(ids)
				.sort(Collections.singletonList("name"))
				.pageSize(ids.size())
				.tenantId(employeeRequest.getEmployee().getTenantId())
				.build();
	}

}
