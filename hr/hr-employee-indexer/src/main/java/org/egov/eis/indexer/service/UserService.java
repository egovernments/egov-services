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

@Service
public class UserService {

	@Autowired
	private PropertiesManager propertiesManager;

	@Autowired
	private RestTemplate restTemplate;

	public static final Logger LOGGER = LoggerFactory.getLogger(UserService.class);

	public UserRequest getUser(Long id, EmployeeRequest employeeRequest) {
		UserSearchRequest userSearchRequest = new UserSearchRequest();
		userSearchRequest.setRequestInfo(employeeRequest.getRequestInfo());
		userSearchRequest.setId(Arrays.asList(id));
		userSearchRequest.setPageSize(1);
		userSearchRequest.setTenantId(employeeRequest.getEmployee().getTenantId());
		UserResponse userResponse = null;
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
		return userResponse.getUser().get(0);
	}

}
