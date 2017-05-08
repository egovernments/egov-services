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

import java.io.IOException;
import java.text.MessageFormat;
import java.util.List;

import org.egov.eis.config.PropertiesManager;
import org.egov.eis.model.User;
import org.egov.eis.service.helper.UserSearchURLHelper;
import org.egov.eis.web.contract.RequestInfo;
import org.egov.eis.web.contract.UserGetRequest;
import org.egov.eis.web.contract.UserRequest;
import org.egov.eis.web.contract.UserResponse;
import org.egov.eis.web.controller.EmployeeController;
import org.egov.eis.web.errorhandler.ErrorHandler;
import org.egov.eis.web.errorhandler.UserErrorResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
	private UserSearchURLHelper userSearchURLHelper;

	@Autowired
	private PropertiesManager propertiesManager;

	@Autowired
	private ErrorHandler errorHandler;

	public List<User> getUsers(List<Long> ids, String tenantId, RequestInfo requestInfo) {
		String url = userSearchURLHelper.searchURL(ids, tenantId);

		UserGetRequest userGetRequest = new UserGetRequest();
		userGetRequest.setId(ids);
		userGetRequest.setPageSize(ids.size());
		userGetRequest.setRequestInfo(requestInfo);
		userGetRequest.setTenantId(tenantId);

		LOGGER.info("\n\n\n\n\n" + "User search url : " + url);
		LOGGER.debug("UserGetRequest : " + userGetRequest);

		UserResponse userResponse = null;
		List<User> users = null;
		try {
			userResponse = new RestTemplate().postForObject(url, userGetRequest, UserResponse.class);
			users = userResponse.getUser();
		} catch (Exception e) {
			LOGGER.debug("Following Exception Occurred While Calling User Service : " + e.getMessage());
			e.printStackTrace();
		}
		
		LOGGER.info("\n\n\n\n\n" + "User search url : " + url);
		LOGGER.debug("UserResponse : " + users);

		return users;
	}

	// FIXME : User service is expecting & sending dates in multiple formats.
	// Fix a common standard for date formats.
	public ResponseEntity<?> createUser(UserRequest userRequest) {
		String url = propertiesManager.getUsersServiceHostName() + propertiesManager.getUsersServiceUsersBasePath()
				+ propertiesManager.getUsersServiceUsersCreatePath();

		LOGGER.info("\n\n\n\n\n" + "User create url : " + url);
		LOGGER.debug("UserRequest : " + userRequest);

		UserResponse userResponse = null;
		try {
			userResponse = new RestTemplate().postForObject(url, userRequest, UserResponse.class);
		} catch (HttpClientErrorException e) {
			String errorResponseBody = e.getResponseBodyAsString();
			LOGGER.debug("Following exception occurred: " + e.getResponseBodyAsString());
			UserErrorResponse userErrorResponse = null;
			try {
				ObjectMapper mapper = new ObjectMapper();
				userErrorResponse = mapper.readValue(errorResponseBody, UserErrorResponse.class);
			} catch (JsonMappingException jme) {
				LOGGER.debug("Following Exception Occurred While Mapping JSON Response From User Service : "
						+ jme.getMessage());
				jme.printStackTrace();
			} catch (JsonProcessingException jpe) {
				LOGGER.debug("Following Exception Occurred While Processing JSON Response From User Service : "
						+ jpe.getMessage());
				jpe.printStackTrace();
			} catch (IOException ioe) {
				LOGGER.debug("Following Exception Occurred Calling User Service : " + ioe.getMessage());
				ioe.printStackTrace();
			}
			return new ResponseEntity<UserErrorResponse>(userErrorResponse, HttpStatus.BAD_REQUEST);
		} catch (Exception e) {
			LOGGER.debug("Following Exception Occurred While Calling User Service : " + e.getMessage());
			e.printStackTrace();
			return errorHandler.getResponseEntityForUnknownUserDBUpdationError(userRequest.getRequestInfo());
		}
		
		LOGGER.info("\n\n\n\n\n" + "User search url : " + url);
		LOGGER.debug("UserResponse : " + userResponse);

		return new ResponseEntity<UserResponse>(userResponse, HttpStatus.CREATED);
	}

	public ResponseEntity<?> updateUser(Long userId, UserRequest userRequest) {
		String url = propertiesManager.getUsersServiceHostName() + propertiesManager.getUsersServiceUsersBasePath()
				+ getUserUpdatePath(userId);

		LOGGER.info("\n\n\n\n\n" + "User update url : " + url);
		LOGGER.debug("UserRequest : " + userRequest);

		UserResponse userResponse = null;
		try {
			userResponse = new RestTemplate().postForObject(url, userRequest, UserResponse.class);
		} catch (HttpClientErrorException e) {
			String errorResponseBody = e.getResponseBodyAsString();
			LOGGER.debug("Following exception occurred: " + e.getResponseBodyAsString());
			UserErrorResponse userErrorResponse = null;
			try {
				ObjectMapper mapper = new ObjectMapper();
				userErrorResponse = mapper.readValue(errorResponseBody, UserErrorResponse.class);
			} catch (JsonMappingException jme) {
				LOGGER.debug("Following Exception Occurred While Mapping JSON Response From User Service : "
						+ jme.getMessage());
				jme.printStackTrace();
			} catch (JsonProcessingException jpe) {
				LOGGER.debug("Following Exception Occurred While Processing JSON Response From User Service : "
						+ jpe.getMessage());
				jpe.printStackTrace();
			} catch (IOException ioe) {
				LOGGER.debug("Following Exception Occurred Calling User Service : " + ioe.getMessage());
				ioe.printStackTrace();
			}
			return new ResponseEntity<UserErrorResponse>(userErrorResponse, HttpStatus.BAD_REQUEST);
		} catch (Exception e) {
			LOGGER.debug("Following Exception Occurred While Calling User Service : " + e.getMessage());
			e.printStackTrace();
			return errorHandler.getResponseEntityForUnknownUserUpdationError(userRequest.getRequestInfo());
		}
		
		LOGGER.info("\n\n\n\n\n" + "User search url : " + url);
		LOGGER.debug("UserResponse : " + userResponse);

		return new ResponseEntity<UserResponse>(userResponse, HttpStatus.OK);
	}

	private String getUserUpdatePath(long id) {
		String path = MessageFormat.format(propertiesManager.getUsersServiceUsersUpdatePath(), Long.toString(id));
		return path;
	}
}