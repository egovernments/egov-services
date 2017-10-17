package org.egov.propertyUser.util;

import java.util.List;
import java.util.stream.Collectors;

import org.egov.models.RequestInfo;
import org.egov.models.User;
import org.egov.models.UserResponseInfo;
import org.egov.propertyUser.config.PropertiesManager;
import org.egov.propertyUser.repository.UserRepository;
import org.egov.tracer.http.LogAwareRestTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserUtil {

	private static final Logger logger = LoggerFactory.getLogger(UserUtil.class);

	@Autowired
	PropertiesManager propertiesManager;

	@Autowired
	LogAwareRestTemplate restTemplate;

	@Autowired
	UserRepository userRepository;

	public User getUserId(User user, RequestInfo requestInfo) throws Exception {
		// By default, active should be true.
		user.setActive(true);

		UserResponseInfo userResponse = null;

		// search user

		userResponse = userRepository.getsearchUser(user, requestInfo);

		// create user
		UserResponseInfo userCreateResponse = null;

		if (userResponse == null || userResponse.getUser().size() == 0) {
			userCreateResponse = userRepository.getCreateUser(user, requestInfo);
			user.setId(userCreateResponse.getUser().get(0).getId());
		}
		
		logger.info("User id --------Final---------- " + userResponse.getUser());
		if (userResponse.getUser().size() > 1) {

			List<User> userFromReponse = userResponse.getUser();

			List<User> result = userFromReponse.stream()
					.filter(value -> value.getGender().equalsIgnoreCase(user.getGender())
							&& value.getAltContactNumber().equalsIgnoreCase(user.getAltContactNumber())
							&& value.getPan().equalsIgnoreCase(user.getPan())
							&& value.getPermanentAddress().equalsIgnoreCase(user.getPermanentAddress())
							&& value.getPermanentCity().equalsIgnoreCase(user.getPermanentCity())
							&& value.getPermanentPincode().equalsIgnoreCase(user.getPermanentPincode())
							&& value.getCorrespondenceAddress().equalsIgnoreCase(user.getCorrespondenceAddress())
							&& value.getCorrespondenceCity().equalsIgnoreCase(user.getCorrespondenceCity())
							&& value.getCorrespondencePincode().equalsIgnoreCase(user.getCorrespondencePincode())
							&& value.getLocale().equalsIgnoreCase(user.getLocale())
							&& value.getFatherOrHusbandName().equalsIgnoreCase(user.getFatherOrHusbandName())
							&& value.getBloodGroup().equalsIgnoreCase(user.getBloodGroup())
							&& value.getIdentificationMark().equalsIgnoreCase(user.getIdentificationMark()))
					.collect(Collectors.toList());

			user.setId(result.get(0).getId());
		} else if (userResponse.getUser().size() == 1) {
			user.setId(userResponse.getUser().get(0).getId());
		}

		return user;

	}
}
