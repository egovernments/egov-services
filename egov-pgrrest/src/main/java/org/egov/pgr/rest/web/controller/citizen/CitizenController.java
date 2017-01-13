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

package org.egov.pgr.rest.web.controller.citizen;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.egov.infra.admin.common.service.IdentityRecoveryService;
import org.egov.infra.admin.master.entity.Device;
import org.egov.infra.admin.master.entity.User;
import org.egov.infra.admin.master.repository.DeviceRepository;
import org.egov.infra.admin.master.service.UserService;
import org.egov.infra.utils.FileStoreUtils;
import org.egov.pgr.rest.web.controller.core.PgrRestController;
import org.egov.pgr.rest.web.model.Error;
import org.egov.pgr.rest.web.model.ErrorRes;
import org.egov.pgr.rest.web.model.ResponseInfo;
import org.egov.pgr.rest.web.model.UserRequest;
import org.egov.pgr.rest.web.model.UserResponse;
import org.egov.portal.entity.Citizen;
import org.egov.portal.service.CitizenService;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CitizenController extends PgrRestController {

    private static final Logger LOGGER = Logger.getLogger(CitizenController.class);

    @Autowired
    protected FileStoreUtils fileStoreUtils;

    @Autowired
    private CitizenService citizenService;

    @Autowired
    private DeviceRepository deviceRepository;

    @Autowired
    private UserService userservice;

    @Autowired
    private TokenStore tokenStore;

    @Autowired
    private IdentityRecoveryService identityRecoveryService;

    private ResponseInfo resInfo = null;

    private String msg = null;

    @RequestMapping(value = "/v1/logout", method = RequestMethod.POST)
    public ResponseInfo logout(final HttpServletRequest request, final OAuth2Authentication authentication) throws Exception {
        try {
            final OAuth2AccessToken token = tokenStore.getAccessToken(authentication);
            resInfo = new ResponseInfo("", "",
                    new DateTime().toString(), "uief87324", "", "");
            if (token == null) {
                msg = getMessage("msg.logout.unknown");
                throw new Exception("error");
            }

            tokenStore.removeAccessToken(token);

            resInfo = new ResponseInfo("", "",
                    new DateTime().toString(), "uief87324", "", getMessage("msg.logout.success"));

            return resInfo;
        } catch (final Exception ex) {
            LOGGER.error("EGOV-PGRREST ERROR ", ex);
            msg = getMessage("server.error");
            throw new Exception("error");
        }
    }

    @RequestMapping(value = "/users", method = RequestMethod.POST)
    public UserResponse registerCitizen(@RequestBody final UserRequest request) throws Exception {
        final UserResponse response = new UserResponse();
        try {
            final Citizen citizenCreate = new Citizen();
            citizenCreate.setUsername(request.getUser().getMobileNo());
            citizenCreate.setMobileNumber(request.getUser().getMobileNo());
            citizenCreate.setName(request.getUser().getName());

            if (request.getUser().getEmail() != null && !request.getUser().getEmail().isEmpty())
                citizenCreate.setEmailId(request.getUser().getEmail());

            citizenCreate.setPassword(request.getUser().getPassword());
            Device device = deviceRepository.findByDeviceUId(request.getUser().getDeviceId());
            if (device == null) {
                device = new Device();
                device.setDeviceId(request.getUser().getDeviceId());
                device.setType(request.getUser().getDeviceType());
                device.setOSVersion(request.getUser().getOsVersion());
            }
            resInfo = new ResponseInfo(request.getRequestInfo().getApiId(), request.getRequestInfo().getVer(),
                    new DateTime().toString(), "uief87324", request.getRequestInfo().getMsgId(), "true");
            final User user = userservice.getUserByUsername(citizenCreate.getMobileNumber());

            if (user != null) {
                msg = getMessage("user.register.duplicate.mobileno");
                throw new Exception("error");
            }

            if (citizenCreate.getEmailId() != null && !citizenCreate.getEmailId().isEmpty()) {
                final User getUser = userservice.getUserByEmailId(citizenCreate.getEmailId());
                if (getUser != null) {
                    msg = getMessage("user.register.duplicate.email");
                    throw new Exception("error");
                }
            }

            if (request.getRequestInfo() != null && request.getRequestInfo().getAuthToken() != null &&
                    citizenService.isValidOTP(request.getRequestInfo().getAuthToken(), request.getUser().getMobileNo())) {
                citizenCreate.setActive(true);
                citizenCreate.getDevices().add(device);
                citizenService.create(citizenCreate);
                resInfo.setStatus(getMessage("msg.citizen.reg.success"));
                response.setResponseInfo(resInfo);
                response.setUser(request.getUser());
                response.getUser().setUserName(request.getUser().getMobileNo());
                return response;
            } else {
                msg = getMessage("msg.pwd.otp.invalid");
                throw new Exception("error");
            }

        } catch (final Exception e) {
            LOGGER.error("EGOV-PGRREST ERROR ", e);
            if (msg == null || msg.isEmpty())
                msg = getMessage("server.error");
            throw e;
        }
    }

    @RequestMapping(value = "/users", method = RequestMethod.PUT)
    public UserResponse updatePassword(@RequestBody final UserRequest request) throws Exception {
        final UserResponse response = new UserResponse();

        try {
            final String token = request.getRequestInfo().getAuthToken();
            String newPassword, confirmPassword;
            resInfo = new ResponseInfo(request.getRequestInfo().getApiId(), request.getRequestInfo().getVer(),
                    new DateTime().toString(), "uief87324", request.getRequestInfo().getMsgId(), "true");
            if (StringUtils.isEmpty(request.getUser().getMobileNo())) {
                msg = getMessage("msg.invalid.request");
                throw new Exception("error");
            }

            // for reset password with otp
            if (!StringUtils.isEmpty(token)) {
                newPassword = request.getUser().getNewPassword();
                confirmPassword = request.getUser().getConfirmPassword();

                if (StringUtils.isEmpty(newPassword)) {
                    msg = getMessage("msg.invalid.request");
                    throw new Exception("error");
                } else if (!newPassword.equals(confirmPassword)) {
                    msg = getMessage("msg.pwd.not.match");
                    throw new Exception("error");
                } else if (identityRecoveryService.validateAndResetPassword(token, newPassword)) {
                    resInfo.setStatus(getMessage("msg.pwd.reset.success"));
                    response.setResponseInfo(resInfo);
                    return response;
                } else {
                    msg = getMessage("msg.pwd.otp.invalid");
                    throw new Exception("error");
                }

            } else {
                msg = getMessage("msg.invalid.request");
                throw new Exception("error");
            }

        } catch (final Exception e) {
            LOGGER.error("EGOV-PGRREST ERROR ", e);
            if (msg == null || msg.isEmpty())
                msg = getMessage("server.error");
            throw e;
        }
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorRes> handleError(Exception ex) {
        ErrorRes response = new ErrorRes();
        response.setResposneInfo(resInfo);
        Error error = new Error();
        error.setCode(400);
        error.setDescription(msg);
        response.setError(error);
        return new ResponseEntity<ErrorRes>(response, HttpStatus.BAD_REQUEST);
    }
}