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

package org.egov.wcms.transaction.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.egov.wcms.transaction.config.ConfigurationManager;
import org.egov.wcms.transaction.exception.UserException;
import org.egov.wcms.transaction.model.Connection;
import org.egov.wcms.transaction.model.Role;
import org.egov.wcms.transaction.model.User;
import org.egov.wcms.transaction.validator.RestConnectionService;
import org.egov.wcms.transaction.web.contract.UserRequestInfo;
import org.egov.wcms.transaction.web.contract.UserResponseInfo;
import org.egov.wcms.transaction.web.contract.WaterConnectionGetReq;
import org.egov.wcms.transaction.web.contract.WaterConnectionReq;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class ConnectionUserService {
    @Autowired
    private RestConnectionService restConnectionService;

    @Autowired
    private ConfigurationManager configurationManager;

    public static final String roleCode = "CITIZEN";
    public static final String roleName = "Citizen";

    

    public void createUserId(final WaterConnectionReq waterConnReq) {

        final String searchUrl = restConnectionService.getUserServiceSearchPath();
        final String createUrl = restConnectionService.getUserServiceCreatePath();

        UserResponseInfo userResponse = null;
        final Map<String, Object> userSearchRequestInfo = new HashMap<String, Object>();
        String mobileNumber;
        if (StringUtils.isNotBlank(waterConnReq.getConnection().getConnectionOwner().getMobileNumber()))
            mobileNumber = waterConnReq.getConnection().getConnectionOwner().getMobileNumber();
        else
            mobileNumber = generateUserMobileNumberForUserName(waterConnReq);
        userSearchRequestInfo.put("userName", mobileNumber);
        userSearchRequestInfo.put("type", roleCode);
        userSearchRequestInfo.put("tenantId", waterConnReq.getConnection().getTenantId());
        userSearchRequestInfo.put("RequestInfo", waterConnReq.getRequestInfo());

        log.info("User Service Search URL :: " + searchUrl + " \n userSearchRequestInfo  :: "
                + userSearchRequestInfo);
        if (StringUtils.isNotBlank(waterConnReq.getConnection().getConnectionOwner().getMobileNumber())
                || StringUtils.isNotBlank(waterConnReq.getConnection().getConnectionOwner().getEmailId())
                || StringUtils.isNotBlank(waterConnReq.getConnection().getConnectionOwner().getAadhaarNumber())
                || StringUtils.isNotBlank(waterConnReq.getConnection().getConnectionOwner().getUserName())) {
            userResponse = new RestTemplate().postForObject(searchUrl.toString(), userSearchRequestInfo, UserResponseInfo.class);
            log.info("User Service Search Response :: " + userResponse);

            if (null == userResponse || userResponse.getUser().size() == 0) {
                userSearchRequestInfo.put("name", waterConnReq.getConnection().getConnectionOwner().getName());
                if (StringUtils.isNotBlank(waterConnReq.getConnection().getConnectionOwner().getMobileNumber()))
                    userSearchRequestInfo.put("mobileNumber",
                            waterConnReq.getConnection().getConnectionOwner().getMobileNumber());

                if (StringUtils.isNotBlank(waterConnReq.getConnection().getConnectionOwner().getAadhaarNumber()))
                    userSearchRequestInfo.put("aadharNumber",
                            waterConnReq.getConnection().getConnectionOwner().getAadhaarNumber());

                if (StringUtils.isNotBlank(waterConnReq.getConnection().getConnectionOwner().getEmailId()))
                    userSearchRequestInfo.put("emailId", waterConnReq.getConnection().getConnectionOwner().getEmailId());
                log.info("User Service Search URL with Multiparam :: " + searchUrl + " \n userSearchRequestInfo :: "
                        + userSearchRequestInfo);
                userResponse = new RestTemplate().postForObject(searchUrl.toString(), userSearchRequestInfo,
                        UserResponseInfo.class);
            }
        }
        log.info("User Service Search Response :: " + userResponse);
        if (null == userResponse || userResponse.getUser().size() == 0) {
            final UserRequestInfo userRequestInfo = new UserRequestInfo();
            userRequestInfo.setRequestInfo(waterConnReq.getRequestInfo());
            final User user = buildUserObjectFromConnection(waterConnReq);
            userRequestInfo.setUser(user);
            log.info("User Object to create User : " + userRequestInfo);
            log.info("User Service Create URL :: " + createUrl + " \n userRequestInfo :: "
                    + userRequestInfo);
            UserResponseInfo userCreateResponse = new UserResponseInfo();
            try {
                userCreateResponse = new RestTemplate().postForObject(createUrl.toString(), userRequestInfo,
                        UserResponseInfo.class);
            } catch (final Exception ex) {
                log.error("Exception encountered while creating user ID : " + ex.getMessage());
                throw new UserException("Error in User Creation","Error in User Creation",waterConnReq.getRequestInfo());
                
            }
            if (userCreateResponse != null && userCreateResponse.getUser() != null && !userCreateResponse.getUser().isEmpty()) {
                log.info("User Service Create User Response :: " + userCreateResponse);
                user.setId(userCreateResponse.getUser().get(0).getId());
                waterConnReq.getConnection().getConnectionOwner().setId(userCreateResponse.getUser().get(0).getId());
            }
        }

        if (userResponse != null) {
            log.info("User Response after Create and Search :: " + userResponse);
            if (null != userResponse.getUser() && userResponse.getUser().size() > 0)
                waterConnReq.getConnection().getConnectionOwner().setId(userResponse.getUser().get(0).getId());
        }
    }

    public List<User> searchUserServiceByParams(WaterConnectionGetReq waterConnGetReq) { 
        final String searchUrl = restConnectionService.getUserServiceSearchPath();
        UserResponseInfo userResponse = null;
        final Map<String, Object> userSearchRequestInfo = new HashMap<String, Object>();
        userSearchRequestInfo.put("type", roleCode);
        userSearchRequestInfo.put("tenantId", waterConnGetReq.getTenantId());
        userSearchRequestInfo.put("RequestInfo", restConnectionService.getRequestInfoWrapperWithoutAuth());

        log.info("User Service Search URL :: " + searchUrl + " \n userSearchRequestInfo  :: "
                + userSearchRequestInfo);
        
        if(StringUtils.isNotBlank(waterConnGetReq.getName())) 
                userSearchRequestInfo.put("name", waterConnGetReq.getName());
        if(StringUtils.isNotBlank(waterConnGetReq.getMobileNumber())) 
                userSearchRequestInfo.put("mobileNumber", waterConnGetReq.getMobileNumber());
        if(StringUtils.isNotBlank(waterConnGetReq.getAadhaarNumber())) 
                userSearchRequestInfo.put("aadhaarNumber", waterConnGetReq.getAadhaarNumber());
        userResponse = new RestTemplate().postForObject(searchUrl.toString(), userSearchRequestInfo, UserResponseInfo.class);
        log.info("User Service Search Response :: " + userResponse);
        
        if(null != userResponse && null != userResponse.getUser()) { 
                return userResponse.getUser(); 
        } else {
                return new ArrayList<>(); 
        }
    }
    public String generateUserMobileNumberForUserName(final WaterConnectionReq waterConnectionRequest) {
        return restConnectionService.generateRequestedDocumentNumber(
                waterConnectionRequest.getConnection().getTenantId(), configurationManager.getUserNameService(),
                configurationManager.getUserNameFormat(), waterConnectionRequest.getRequestInfo());
    }

    public User buildUserObjectFromConnection(final WaterConnectionReq waterConnReq) {
        final Connection conn = waterConnReq.getConnection();
        String userName = null;
        if (StringUtils.isNotBlank(waterConnReq.getConnection().getConnectionOwner().getMobileNumber()))
            userName = conn.getConnectionOwner().getMobileNumber();
        else if (userName == null
                && StringUtils.isNotBlank(waterConnReq.getConnection().getConnectionOwner().getAadhaarNumber()))
            userName = conn.getConnectionOwner().getAadhaarNumber();
        else if (userName == null
                && StringUtils.isNotBlank(waterConnReq.getConnection().getConnectionOwner().getEmailId()))
            userName = conn.getConnectionOwner().getEmailId();
        else if (userName == null
                && StringUtils.isNotBlank(waterConnReq.getConnection().getConnectionOwner().getUserName()))
            userName = conn.getConnectionOwner().getUserName();
        else
            userName = restConnectionService.generateRequestedDocumentNumber(waterConnReq.getConnection().getTenantId(),
                    configurationManager.getUserNameService(), configurationManager.getUserNameFormat(),
                    waterConnReq.getRequestInfo());
        final Role role = Role.builder().code(roleCode).name(roleName).build();
        final List<Role> roleList = new ArrayList<>();
        roleList.add(role);
        final User user = new User();
        user.setName(conn.getConnectionOwner().getName());
        user.setMobileNumber(userName);
        user.setUserName(userName);
        user.setActive(true);
        user.setTenantId(conn.getTenantId());
        user.setType(roleCode);
        user.setPassword(configurationManager.getDefaultPassword());
        user.setRoles(roleList);
        if (StringUtils.isNotBlank(conn.getConnectionOwner().getAadhaarNumber()))
            user.setAadhaarNumber(conn.getConnectionOwner().getAadhaarNumber());
        if (StringUtils.isNotBlank(conn.getConnectionOwner().getEmailId()))
            user.setEmailId(conn.getConnectionOwner().getEmailId());
        if (StringUtils.isNotBlank(conn.getAddress().getAddressLine1()))
            user.setPermanentAddress(conn.getAddress().getAddressLine1());
        if (StringUtils.isNotBlank(conn.getAddress().getPinCode()))
            user.setPermanentPinCode(conn.getAddress().getPinCode());
        if (StringUtils.isNotBlank(conn.getAddress().getCity()))
            user.setPermanentCity(conn.getAddress().getCity());
        if (StringUtils.isNotBlank(conn.getHouseNumber())) 
        	user.setCorrespondenceAddress(conn.getHouseNumber());
        if (StringUtils.isNotBlank(conn.getConnectionOwner().getMobileNumber()))
            user.setMobileNumber(conn.getConnectionOwner().getMobileNumber());
        if (StringUtils.isNotBlank(conn.getConnectionOwner().getGender()))
            user.setGender(conn.getConnectionOwner().getGender());

        return user;
    }

   

}
