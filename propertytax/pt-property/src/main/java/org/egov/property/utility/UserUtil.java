package org.egov.property.utility;

import org.apache.commons.lang3.RandomStringUtils;
import org.egov.models.RequestInfo;
import org.egov.models.User;
import org.egov.property.config.PropertiesManager;
import org.egov.property.model.UserCreateRequest;
import org.egov.property.model.UserCreateResponse;
import org.egov.property.repository.UserRestRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserUtil {

	@Autowired
	UserRestRepository userRestRepository;

	@Autowired
	PropertiesManager propertiesManager;

	public User createNewUser(User user, RequestInfo requestInfo) throws Exception {
		if (user.getUserName() == null) {
			user.setUserName(getUserName(user));
		}
		if (user.getPassword() == null) {
			user.setPassword(propertiesManager.getDefaultUserPassword());
		}
		if (user.getActive() == null) {
			user.setActive(true);
		}
		UserCreateRequest userCreateRequest = new UserCreateRequest();

		userCreateRequest.setRequestInfo(requestInfo);
		userCreateRequest.setUser(user);

		UserCreateResponse userCreateResponse = userRestRepository.createUser(userCreateRequest);
		user.setId(userCreateResponse.getUser().get(0).getId());

		return user;
	}

	private String getUserName(User user) {

		String userName = user.getUserName();

		if (userName == null) {
			userName = RandomStringUtils.randomAlphanumeric(32);
		}
		return userName;
	}

	public void updateUser(User user, RequestInfo requestInfo) throws Exception {
		UserCreateRequest userCreateRequest = new UserCreateRequest();

		userCreateRequest.setRequestInfo(requestInfo);
		userCreateRequest.setUser(user);
		userRestRepository.updateUser(userCreateRequest);
	}
}
