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

package org.egov.pgr.rest.web.oauth2.provider;

import java.util.LinkedHashMap;
import java.util.Map;

import org.egov.infra.admin.master.service.CityService;
import org.egov.infra.config.security.authentication.SecureUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.common.DefaultOAuth2AccessToken;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.token.TokenEnhancerChain;
import org.springframework.stereotype.Service;

@Service
public class CustomTokenEnhancer extends TokenEnhancerChain {

    @Autowired
    private CityService cityService;

    @Override
    public OAuth2AccessToken enhance(final OAuth2AccessToken accessToken, final OAuth2Authentication authentication) {
        final DefaultOAuth2AccessToken token = (DefaultOAuth2AccessToken) accessToken;

        SecureUser su = (SecureUser) authentication.getUserAuthentication()
                .getPrincipal();
        final Map<String, Object> info = new LinkedHashMap<String, Object>();
        final Map<String, Object> responseInfo = new LinkedHashMap<String, Object>();
        final Map<String, Object> userInfo = new LinkedHashMap<String, Object>();

        responseInfo.put("api_id", "");
        responseInfo.put("ver", "");
        responseInfo.put("ts", "");
        responseInfo.put("res_msg_id", "");
        responseInfo.put("msg_id", "");
        responseInfo.put("status", "Access Token generated successfully");

        userInfo.put("id", su.getUser().getId());
        userInfo.put("name", su.getUser().getName());
        userInfo.put("username", su.getUser().getUsername());
        userInfo.put("mobile_no", su.getUser().getMobileNumber());
        userInfo.put("email", su.getUser().getEmailId());
        userInfo.put("type", su.getUser().getType());

        info.put("ResponseInfo", responseInfo);
        info.put("User", userInfo);

        info.put("cityLat", cityService.cityDataForKey("citylat") == null ? 0 : cityService.cityDataForKey("citylat"));
        info.put("cityLng", cityService.cityDataForKey("citylng") == null ? 0 : cityService.cityDataForKey("citylng"));

        token.setAdditionalInformation(info);

        return super.enhance(token, authentication);
    }
}