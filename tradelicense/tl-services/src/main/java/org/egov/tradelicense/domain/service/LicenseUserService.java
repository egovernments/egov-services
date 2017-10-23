/*
 * eGov suite of products aim to improve the internal efficiency,transparency,
 *    accountability and the service delivery of the government  organizations.
 *
 *     Copyright (C) <2015>  eGovernments Foundation
 *
 *     The updated version of eGov suite of products as by eGovernments Foundation
 *     is available at http://www.egovernments.org
 *
 *     This program is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     any later version.
 *
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 *
 *     You should have received a copy of the GNU General Public License
 *     along with this program. If not, see http://www.gnu.org/licenses/ or
 *     http://www.gnu.org/licenses/gpl.html .
 *
 *     In addition to the terms of the GPL license to be adhered to in using this
 *     program, the following additional terms are to be complied with:
 *
 *         1) All versions of this program, verbatim or modified must carry this
 *            Legal Notice.
 *
 *         2) Any misrepresentation of the origin of the material is prohibited. It
 *            is required that all modified versions of this material be marked in
 *            reasonable ways as different from the original version.
 *
 *         3) This license does not grant any rights to any user of the program
 *            with regards to rights under trademark law for use of the trade names
 *            or trademarks of eGovernments Foundation.
 *
 *   In case of any queries, you can reach eGovernments Foundation at contact@egovernments.org.
 */

package org.egov.tradelicense.domain.service;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.egov.tl.commons.web.contract.RequestInfo;
import org.egov.tradelicense.common.config.PropertiesManager;
import org.egov.tradelicense.common.domain.exception.UserException;
import org.egov.tradelicense.domain.model.TradeLicense;
import org.egov.tradelicense.web.contract.Role;
import org.egov.tradelicense.web.contract.User;
import org.egov.tradelicense.web.contract.UserRequestInfo;
import org.egov.tradelicense.web.contract.UserResponseInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class LicenseUserService {
	
	@Autowired
	private PropertiesManager propertiesManager;

	@Autowired
	private IdGenService idGenService;

	public static final String ROLECODE = "CITIZEN";
	public static final String ROLENAME = "Citizen";

	public void createUser(final TradeLicense tradeLicense, final RequestInfo requestInfo) {

		final String createUrl = propertiesManager.getUserServiceHostName()
				+ propertiesManager.getUserServiceCreatePath();

		final UserRequestInfo userRequestInfo = new UserRequestInfo();
		userRequestInfo.setRequestInfo(requestInfo);
		final User user = buildUserObjectFromConnection(tradeLicense, requestInfo);
		userRequestInfo.setUser(user);
		log.info("User Object to create User : " + userRequestInfo);
		log.info("User Service Create URL :: " + createUrl + " \n userRequestInfo :: " + userRequestInfo);
		UserResponseInfo userCreateResponse = new UserResponseInfo();
		try {
			userCreateResponse = new RestTemplate().postForObject(createUrl.toString(), userRequestInfo,
					UserResponseInfo.class);
		} catch (final Exception ex) {
			log.error("Exception encountered while creating user ID : " + ex.getMessage());
			throw new UserException("Error in User Creation", requestInfo);

		}
		if (userCreateResponse != null && userCreateResponse.getUser() != null
				&& !userCreateResponse.getUser().isEmpty()) {
			log.info("User Service Create User Response :: " + userCreateResponse);
			user.setId(userCreateResponse.getUser().get(0).getId());
			tradeLicense.setUserId(userCreateResponse.getUser().get(0).getId());
		}

	}

	private User buildUserObjectFromConnection(final TradeLicense tradeLicense,
			final RequestInfo requestInfo) {
		String userName = idGenService.generate(tradeLicense.getTenantId(), propertiesManager.getUserNameService(),
					propertiesManager.getUserNameFormat(), requestInfo);
		final Role role = Role.builder().code(ROLECODE).name(ROLENAME).build();
		final List<Role> roleList = new ArrayList<>();
		roleList.add(role);
		final User user = new User();
		user.setName(tradeLicense.getOwnerName());
		user.setMobileNumber(userName);
		user.setUserName(userName);
		user.setActive(true);
		user.setTenantId(tradeLicense.getTenantId());
		user.setType(ROLECODE);
		user.setPassword(propertiesManager.getDefaultPassword());
		user.setRoles(roleList);
		if (StringUtils.isNotBlank(tradeLicense.getOwnerAadhaarNumber()))
			user.setAadhaarNumber(tradeLicense.getOwnerAadhaarNumber());
		if (StringUtils.isNotBlank(tradeLicense.getOwnerEmailId()))
			user.setEmailId(tradeLicense.getOwnerEmailId());
		if (StringUtils.isNotBlank(tradeLicense.getOwnerAddress()))
			user.setPermanentAddress(tradeLicense.getOwnerAddress());
		if (StringUtils.isNotBlank(tradeLicense.getOwnerMobileNumber()))
			user.setMobileNumber(tradeLicense.getOwnerMobileNumber());
		if (tradeLicense.getOwnerGender() != null
				&& StringUtils.isNotBlank(tradeLicense.getOwnerGender().toString()))
			user.setGender(tradeLicense.getOwnerGender().toString());

		return user;
	}

}
