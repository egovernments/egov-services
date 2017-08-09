package org.egov.eis.indexer.service;

import org.egov.boundary.persistence.entity.Boundary;
import org.egov.boundary.persistence.entity.BoundaryType;
import org.egov.boundary.web.contract.BoundaryResponse;
import org.egov.boundary.web.contract.BoundaryTypeResponse;
import org.egov.common.contract.request.RequestInfo;
import org.egov.eis.indexer.config.PropertiesManager;
import org.egov.eis.model.User;
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
import java.util.List;

@Service
public class UserService {

	@Autowired
	private PropertiesManager propertiesManager;

	@Autowired
	private RestTemplate restTemplate;

	public static final Logger LOGGER = LoggerFactory.getLogger(UserService.class);

	public UserRequest getUser(Long id, RequestInfo requestInfo) {
		UserSearchRequest userSearchRequest = UserSearchRequest.builder()
				.requestInfo(requestInfo).id(Arrays.asList(id)).pageSize(1).build();
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
