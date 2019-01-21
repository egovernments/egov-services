/*
 * eGov suite of products aim to improve the internal efficiency,transparency,
 * accountability and the service delivery of the government  organizations.
 *
 *  Copyright (C) 2016  eGovernments Foundation
 *
 *  The updated version of eGov suite of products as by eGovernments Foundation
 *  is available at http://www.egovernments.org
 *
 *  This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this program. If not, see http://www.gnu.org/licenses/ or
 *  http://www.gnu.org/licenses/gpl.html .
 *
 *  In addition to the terms of the GPL license to be adhered to in using this
 *  program, the following additional terms are to be complied with:
 *
 *      1) All versions of this program, verbatim or modified must carry this
 *         Legal Notice.
 *
 *      2) Any misrepresentation of the origin of the material is prohibited. It
 *         is required that all modified versions of this material be marked in
 *         reasonable ways as different from the original version.
 *
 *      3) This license does not grant any rights to any user of the program
 *         with regards to rights under trademark law for use of the trade names
 *         or trademarks of eGovernments Foundation.
 *
 *  In case of any queries, you can reach eGovernments Foundation at contact@egovernments.org.
 */

package org.egov.hrms.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.egov.hrms.config.PropertiesManager;
import org.egov.hrms.web.contract.UserRequest;
import org.egov.hrms.web.contract.UserResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.text.MessageFormat;
@Slf4j
@Service
public class UserService {


	@Autowired
	private RestTemplate restTemplate;


	@Autowired
	private PropertiesManager propertiesManager;
	
	@Autowired
	private ObjectMapper objectMapper;

//	public List<User> getUsers(EmployeeCriteria employeeCriteria, RequestInfo requestInfo) {
//		String url = userSearchURLHelper.searchURL(employeeCriteria.getId(), employeeCriteria.getTenantId());
//		LOGGER.debug("\n\n\n\n\n" + "User search url : " + url);
//
//		UserGetRequest userGetRequest = getUserGetRequest(employeeCriteria, requestInfo);
//		LOGGER.debug("\nUserGetRequest : " + userGetRequest);
//
//		UserResponse userResponse = null;
//		List<User> users = null;
//		try {
//			userResponse = restTemplate.postForObject(url, userGetRequest, UserResponse.class);
//			users = userResponse.getUser();
//		} catch (Exception e) {
//			LOGGER.debug("\n\nFollowing Exception Occurred While Calling User Service : " + e.getMessage());
//			e.printStackTrace();
//		}
//
//		LOGGER.debug("\n\nUserResponse : " + users);
//
//		return users;
//	}
//

	public UserResponse createUser(UserRequest userRequest) {
		log.info("Service: Create USer");

		String url = propertiesManager.getUsersServiceHostName() + propertiesManager.getUsersServiceUsersBasePath()
				+ propertiesManager.getUsersServiceUsersCreatePath();

		UserResponse userResponse = restTemplate.postForObject(url,userRequest,UserResponse.class);

		return userResponse;
	}

	public UserResponse updateUser(Long userId, UserRequest userRequest)  {
		log.info("Update");

		String url = propertiesManager.getUsersServiceHostName() + propertiesManager.getUsersServiceUsersBasePath()
				+ getUserUpdatePath(userId);


		UserResponse userResponse = restTemplate.postForObject(url,userRequest,UserResponse.class);


		return userResponse;
	}

	private String getUserUpdatePath(long id) {
		String path = MessageFormat.format(propertiesManager.getUsersServiceUsersUpdatePath(), Long.toString(id));
		return path;
	}

}