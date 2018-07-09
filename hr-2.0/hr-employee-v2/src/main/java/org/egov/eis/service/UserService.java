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

package org.egov.eis.service;

import static org.springframework.util.ObjectUtils.isEmpty;

import java.io.IOException;
import java.text.MessageFormat;
import java.util.List;

import org.egov.common.contract.request.RequestInfo;
import org.egov.eis.config.PropertiesManager;
import org.egov.eis.model.User;
import org.egov.eis.service.exception.UserException;
import org.egov.eis.service.helper.UserSearchURLHelper;
import org.egov.eis.web.contract.EmployeeCriteria;
import org.egov.eis.web.contract.UserGetRequest;
import org.egov.eis.web.contract.UserRequest;
import org.egov.eis.web.contract.UserResponse;
import org.egov.eis.web.controller.EmployeeController;
import org.egov.eis.web.errorhandler.UserErrorResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class UserService {

	private static final Logger LOGGER = LoggerFactory.getLogger(EmployeeController.class);

	@Autowired
	private RestTemplate restTemplate;

	@Autowired
	private UserSearchURLHelper userSearchURLHelper;

	@Autowired
	private PropertiesManager propertiesManager;
	
	@Autowired
	private ObjectMapper objectMapper;

	public List<User> getUsers(EmployeeCriteria employeeCriteria, RequestInfo requestInfo) {
		String url = userSearchURLHelper.searchURL(employeeCriteria.getId(), employeeCriteria.getTenantId());
		LOGGER.debug("\n\n\n\n\n" + "User search url : " + url);

		UserGetRequest userGetRequest = getUserGetRequest(employeeCriteria, requestInfo);
		LOGGER.debug("\nUserGetRequest : " + userGetRequest);

		UserResponse userResponse = null;
		List<User> users = null;
		try {
			userResponse = restTemplate.postForObject(url, userGetRequest, UserResponse.class);
			users = userResponse.getUser();
		} catch (Exception e) {
			LOGGER.debug("\n\nFollowing Exception Occurred While Calling User Service : " + e.getMessage());
			e.printStackTrace();
		}
		
		LOGGER.debug("\n\nUserResponse : " + users);

		return users;
	}

	// FIXME : Fix a common standard for date formats in User Service.
	public UserResponse createUser(UserRequest userRequest) throws UserException {
		String url = propertiesManager.getUsersServiceHostName() + propertiesManager.getUsersServiceUsersBasePath()
				+ propertiesManager.getUsersServiceUsersCreatePath();

		LOGGER.debug("\n\n\n\n\n" + "User create url : " + url);
		LOGGER.debug("\nUserRequest : " + userRequest);

		UserResponse userResponse = null;
		try {
			userResponse = restTemplate.postForObject(url, userRequest, UserResponse.class);
		} catch (HttpClientErrorException e) {
			String errorResponseBody = e.getResponseBodyAsString();
			LOGGER.debug("\n\n" + "Following exception occurred while creating user: " + errorResponseBody);
			UserErrorResponse userErrorResponse = null;
			try {
				userErrorResponse = objectMapper.readValue(errorResponseBody, UserErrorResponse.class);
			} catch (JsonMappingException jme) {
				LOGGER.debug("\nFollowing Exception Occurred While Mapping JSON Response From User Service : "
						+ jme.getMessage());
				jme.printStackTrace();
			} catch (JsonProcessingException jpe) {
				LOGGER.debug("\nFollowing Exception Occurred While Processing JSON Response From User Service : "
						+ jpe.getMessage());
				jpe.printStackTrace();
			} catch (IOException ioe) {
				LOGGER.debug("\nFollowing Exception Occurred Calling User Service : " + ioe.getMessage());
				ioe.printStackTrace();
			}
			throw new UserException(userErrorResponse, userRequest.getRequestInfo());
		} catch (Exception e) {
			LOGGER.debug("\nFollowing Exception Occurred While Calling User Service : " + e.getMessage());
			e.printStackTrace();
			throw new UserException(null, userRequest.getRequestInfo());
		}

		LOGGER.debug("\n\nUserResponse : " + userResponse);

		return userResponse;
	}

	public UserResponse updateUser(Long userId, UserRequest userRequest) throws UserException {
		String url = propertiesManager.getUsersServiceHostName() + propertiesManager.getUsersServiceUsersBasePath()
				+ getUserUpdatePath(userId);

		LOGGER.debug("\n\n\n\n\n" + "User update url : " + url);
		LOGGER.debug("\nUserRequest : " + userRequest);

		UserResponse userResponse = null;
		try {
			userResponse = restTemplate.postForObject(url, userRequest, UserResponse.class);
		} catch (HttpClientErrorException e) {
			String errorResponseBody = e.getResponseBodyAsString();
			LOGGER.debug("\n\n" + "Following exception occurred while updating user: " + errorResponseBody);
			UserErrorResponse userErrorResponse = null;
			try {
				userErrorResponse = objectMapper.readValue(errorResponseBody, UserErrorResponse.class);
			} catch (JsonMappingException jme) {
				LOGGER.debug("\nFollowing Exception Occurred While Mapping JSON Response From User Service : "
						+ jme.getMessage());
				jme.printStackTrace();
			} catch (JsonProcessingException jpe) {
				LOGGER.debug("\nFollowing Exception Occurred While Processing JSON Response From User Service : "
						+ jpe.getMessage());
				jpe.printStackTrace();
			} catch (IOException ioe) {
				LOGGER.debug("\nFollowing Exception Occurred Calling User Service : " + ioe.getMessage());
				ioe.printStackTrace();
			}
			throw new UserException(userErrorResponse, userRequest.getRequestInfo());
		} catch (Exception e) {
			LOGGER.debug("\nFollowing Exception Occurred While Calling User Service : " + e.getMessage());
			e.printStackTrace();
			throw new UserException(null, userRequest.getRequestInfo());
		}

		LOGGER.debug("\n\nUserResponse : " + userResponse);

		return userResponse;
	}

	private String getUserUpdatePath(long id) {
		String path = MessageFormat.format(propertiesManager.getUsersServiceUsersUpdatePath(), Long.toString(id));
		return path;
	}

    public UserGetRequest getUserGetRequest(EmployeeCriteria employeeCriteria, RequestInfo requestInfo) {
		UserGetRequest userGetRequest = new UserGetRequest();

		userGetRequest.setId(employeeCriteria.getId());
		userGetRequest.setRoleCodes(employeeCriteria.getRoleCodes());
		userGetRequest.setTenantId(employeeCriteria.getTenantId());
		userGetRequest.setRequestInfo(requestInfo);
		userGetRequest.setActive(employeeCriteria.getActive());

		if(!isEmpty(employeeCriteria.getUserName())) {
			userGetRequest.setUserName(employeeCriteria.getUserName());
		}
		if(!isEmpty(employeeCriteria.getId())) {
			userGetRequest.setPageSize(employeeCriteria.getId().size());
		}
		if (!isEmpty(userGetRequest.getRoleCodes())) {
			userGetRequest.setUserType("EMPLOYEE");
			userGetRequest.setPageSize(500);
		}

		return userGetRequest;
    }
}