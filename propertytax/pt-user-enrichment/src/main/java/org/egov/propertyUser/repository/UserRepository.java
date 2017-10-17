package org.egov.propertyUser.repository;

import java.util.HashMap;
import java.util.Map;

import org.egov.models.RequestInfo;
import org.egov.models.User;
import org.egov.models.UserResponseInfo;
import org.egov.propertyUser.config.PropertiesManager;
import org.egov.propertyUser.model.UserRequestInfo;
import org.egov.propertyUser.util.UserUtil;
import org.egov.tracer.http.LogAwareRestTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class UserRepository {

	private static final Logger logger = LoggerFactory.getLogger(UserUtil.class);

	@Autowired
	PropertiesManager propertiesManager;

	@Autowired
	LogAwareRestTemplate restTemplate;

	public UserResponseInfo getsearchUser(User user, RequestInfo requestInfo) throws Exception{

		StringBuffer searchUrl = new StringBuffer();
		searchUrl.append(propertiesManager.getUserHostName());
		searchUrl.append(propertiesManager.getUserBasepath());
		searchUrl.append(propertiesManager.getUserSearchpath());

		UserResponseInfo userResponse = null;
		Map<String, Object> userSearchRequestInfo = new HashMap<String, Object>();
		userSearchRequestInfo.put("userName", user.getUserName());
		userSearchRequestInfo.put("type", user.getType());
		userSearchRequestInfo.put("tenantId", user.getTenantId());
		userSearchRequestInfo.put("RequestInfo", requestInfo);
		// search user

		logger.info("UserRepository searchUrl --username-->> " + searchUrl.toString()
				+ " \n userSearchRequestInfo ---->> " + userSearchRequestInfo);

			userResponse = restTemplate.postForObject(searchUrl.toString(), userSearchRequestInfo,
					UserResponseInfo.class);
			logger.info("UserRepository userResponse ---->> " + userResponse);

			if (userResponse == null || userResponse.getUser().size() == 0) {
				userSearchRequestInfo.put("name", user.getName());
				if (user.getMobileNumber() != null) {

					userSearchRequestInfo.put("mobileNumber", user.getMobileNumber());
				}

				if (user.getAadhaarNumber() != null && !user.getAadhaarNumber().isEmpty()) {
					userSearchRequestInfo.put("aadhaarNumber", user.getAadhaarNumber());
				}

				if (user.getEmailId() != null && !user.getEmailId().isEmpty()) {
					userSearchRequestInfo.put("emailId", user.getEmailId());
				}

				// search user
				logger.info("UserRepository searchUrl ---multiparam->> " + searchUrl.toString()
						+ " \n userSearchRequestInfo ---->> " + userSearchRequestInfo);
				userResponse = restTemplate.postForObject(searchUrl.toString(), userSearchRequestInfo,
						UserResponseInfo.class);
				logger.info("UserRepository userResponse ---->> " + userResponse);

			}
		
		return userResponse;
	}

	public UserResponseInfo getCreateUser(User user, RequestInfo requestInfo) throws Exception{

		StringBuffer createUrl = new StringBuffer();
		createUrl.append(propertiesManager.getUserHostName());
		createUrl.append(propertiesManager.getUserBasepath());
		createUrl.append(propertiesManager.getUserCreatepath());

		UserResponseInfo userCreateResponse = null;

		UserRequestInfo userRequestInfo = new UserRequestInfo();
		userRequestInfo.setRequestInfo(requestInfo);
		user.setPassword(propertiesManager.getDefaultPassword());
		userRequestInfo.setUser(user);

		logger.info("UserRepository createUrl ---->> " + createUrl.toString() + " \n userRequestInfo ---->> "
				+ userRequestInfo);

		userCreateResponse = restTemplate.postForObject(createUrl.toString(), userRequestInfo, UserResponseInfo.class);
		logger.info("UserRepository userCreateResponse ---->> " + userCreateResponse);

		return userCreateResponse;

	}
}
