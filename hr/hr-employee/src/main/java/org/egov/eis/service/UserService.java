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
import java.util.List;

import org.egov.eis.config.PropertiesManager;
import org.egov.eis.model.User;
import org.egov.eis.service.helper.UserSearchURLHelper;
import org.egov.eis.web.contract.RequestInfo;
import org.egov.eis.web.contract.UserGetRequest;
import org.egov.eis.web.contract.UserRequest;
import org.egov.eis.web.contract.UserResponse;
import org.egov.eis.web.errorhandler.ErrorHandler;
import org.egov.eis.web.errorhandler.UserErrorResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class UserService {

	@Autowired
	private UserSearchURLHelper userSearchURLHelper;
	
	@Autowired
	private PropertiesManager propertiesManager;

	@Autowired
	private ErrorHandler errorHandler;
	
	public List<User> getUsers(List<Long> ids, String tenantId, RequestInfo requestInfo, HttpHeaders headers) {
		String url = userSearchURLHelper.searchURL(ids, tenantId);

		UserGetRequest userGetRequest = new UserGetRequest();
		userGetRequest.setId(ids);
		userGetRequest.setRequestInfo(requestInfo);
		userGetRequest.setTenantId(tenantId);

		String userGetRequestJson = null;
		try {
			ObjectMapper mapper = new ObjectMapper();
			userGetRequestJson = mapper.writeValueAsString(userGetRequest);
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}

		headers.setContentType(MediaType.APPLICATION_JSON);
		// FIXME : Passing auth-token for testing locally. Remove before actual deployment.
		headers.add("auth-token", requestInfo.getAuthToken());
		HttpEntity<String> httpEntityRequest = new HttpEntity<String>(userGetRequestJson, headers);

		UserResponse userResponse = new RestTemplate().postForObject(url, httpEntityRequest, UserResponse.class);

		return userResponse.getUser();
	}

	public ResponseEntity<?> createUser(UserRequest userRequest, HttpHeaders headers) {
		String url = propertiesManager.getUsersServiceHostName() + propertiesManager.getUsersServiceUsersBasePath()
				+ propertiesManager.getUsersServiceUsersCreatePath();

		String userJson = null;
		try {
			ObjectMapper mapper = new ObjectMapper();
			userJson = mapper.writeValueAsString(userRequest);
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}

		System.err.println("userJson : " + userJson);
		headers.setContentType(MediaType.APPLICATION_JSON);
		// FIXME : Passing auth-token for testing locally. Remove before actual deployment.
		headers.add("auth-token", userRequest.getRequestInfo().getAuthToken());
		HttpEntity<String> httpEntityRequest = new HttpEntity<String>(userJson, headers);

		UserResponse userResponse = null;
		try {
			userResponse = new RestTemplate().postForObject(url, httpEntityRequest, UserResponse.class);
		} catch (HttpClientErrorException e) {
			String errorResponseBody = e.getResponseBodyAsString();
			UserErrorResponse userErrorResponse = null;
			try {
				ObjectMapper mapper = new ObjectMapper();
				userErrorResponse = mapper.readValue(errorResponseBody, UserErrorResponse.class);
			} catch (JsonMappingException jme) {
				System.err.println("Following Exception Occurred While Mapping JSON Response From User Service : "
						+ jme.getMessage());
				jme.printStackTrace();
			} catch (JsonProcessingException jpe) {
				System.err.println("Following Exception Occurred While Processing JSON Response From User Service : "
						+ jpe.getMessage());
				jpe.printStackTrace();
			} catch (IOException ioe) {
				System.err.println("Following Exception Occurred Calling User Service : " + ioe.getMessage());
				ioe.printStackTrace();
			}
			return new ResponseEntity<UserErrorResponse>(userErrorResponse, HttpStatus.BAD_REQUEST);
		} catch (Exception e) {
			System.err.println("Exception Occurred While Calling User Service : " + e);
			e.printStackTrace();
			return errorHandler.getResponseEntityForUnexpectedErrors(userRequest.getRequestInfo());
		}
		return new ResponseEntity<UserResponse>(userResponse, HttpStatus.BAD_REQUEST);
	}
}