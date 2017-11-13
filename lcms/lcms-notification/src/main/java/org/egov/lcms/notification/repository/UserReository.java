package org.egov.lcms.notification.repository;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

import org.egov.common.contract.request.RequestInfo;
import org.egov.lcms.notification.config.PropertiesManager;
import org.egov.lcms.notification.model.UserDetail;
import org.egov.lcms.notification.model.UserSearchRequest;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import lombok.extern.slf4j.Slf4j;

@Repository
@Slf4j
public class UserReository {

	@Autowired
	PropertiesManager propertiesManager;

	public List<UserDetail> getUser(String tenantId, List<String> roleCodes, RequestInfo requestInfo) throws Exception {

		final RestTemplate restTemplate = new RestTemplate();
		List<UserDetail> userDetails = new ArrayList<UserDetail>();

		final StringBuffer userUrl = new StringBuffer();
		userUrl.append(propertiesManager.getUserHostName() + propertiesManager.getUserBasePath());
		userUrl.append(propertiesManager.getUserSearchPath());

		final URI uri = UriComponentsBuilder.fromHttpUrl(userUrl.toString()).build().encode().toUri();

		UserSearchRequest userSearchRequest = new UserSearchRequest();
		userSearchRequest.setRequestInfo(requestInfo);
		userSearchRequest.setTenantId(tenantId);
		userSearchRequest.setRoleCodes(roleCodes);

		log.info("Get User url is " + uri + " user response is : " + userSearchRequest);
		try {

			String response = restTemplate.postForObject(uri, userSearchRequest, String.class);
			JSONParser parser = new JSONParser();
			JSONObject userResponse = (JSONObject) parser.parse(response);
			JSONArray users = (JSONArray) userResponse.get("user");

			for (Object user : users) {

				UserDetail userDetail = new UserDetail();
				JSONObject userObj = (JSONObject) user;

				if (userObj.get("emailId") != null && userObj.get("name") != null
						&& userObj.get("mobileNumber") != null) {

					userDetail.setName(userObj.get("name").toString());
					userDetail.setEmailId(userObj.get("emailId").toString());
					userDetail.setMobileNumber(userObj.get("mobileNumber").toString());
					userDetails.add(userDetail);
				}
			}
		} catch (final HttpClientErrorException exception) {

			log.info("Error occured while fetching User Details!");
			exception.printStackTrace();
		}
		return userDetails;
	}

}