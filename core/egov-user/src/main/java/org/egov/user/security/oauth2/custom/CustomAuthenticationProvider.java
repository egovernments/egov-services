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

package org.egov.user.security.oauth2.custom;

import org.egov.user.domain.model.User;
import org.egov.user.domain.service.UserService;
import org.egov.user.domain.model.SecureUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.common.exceptions.OAuth2Exception;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class CustomAuthenticationProvider implements AuthenticationProvider {

    /**
     * TO-Do:Need to remove this and provide authentication for web, based on
     * authentication_code.
     */

    @Autowired
    private UserService userService;

    @Override
    public Authentication authenticate(Authentication authentication) {

        String userName = authentication.getName();
        String password = authentication.getCredentials().toString();
        User user;
        if (userName.contains("@") && userName.contains(".")) {
            user = userService.getUserByEmailId(userName);
        } else {
            user = userService.getUserByUsername(userName);
        }
        if (user == null) {
            throw new OAuth2Exception("Invalid login credentials");
        }

        BCryptPasswordEncoder bcrypt = new BCryptPasswordEncoder();

        if (bcrypt.matches(password, user.getPassword())) {

            if (!user.getActive()) {
                throw new OAuth2Exception("Please activate your account");
            }
            /**
             * We assume that there will be only one type. If it is multimple
             * then we have change below code Seperate by comma or other and
             * iterate
             */
            List<GrantedAuthority> grantedAuths = new ArrayList<>();
            grantedAuths.add(new SimpleGrantedAuthority("ROLE_" + user.getType()));
            final SecureUser secureUser = new SecureUser(getUser(user));
            return new UsernamePasswordAuthenticationToken(secureUser, password,
                    grantedAuths);
        } else {
            throw new OAuth2Exception("Invalid login credentials");
        }
    }

    private org.egov.user.web.contract.auth.User getUser(User user) {
        return org.egov.user.web.contract.auth.User.builder()
                .id(user.getId())
                .userName(user.getUsername())
                .name(user.getName())
                .mobileNumber(user.getMobileNumber())
                .emailId(user.getEmailId())
                .locale(user.getLocale())
                .active(user.getActive())
                .type(user.getType().name())
                .roles(toAuthRole(user.getRoles()))
                .tenantId(user.getTenantId())
                .build();
    }

    private List<org.egov.user.web.contract.auth.Role> toAuthRole(List<org.egov.user.domain.model.Role> domainRoles) {
        if (domainRoles == null) return new ArrayList<>();
        return domainRoles.stream().map(org.egov.user.web.contract.auth.Role::new)
                .collect(Collectors.toList());
    }

    @Override
    public boolean supports(final Class<?> authentication) {
        return authentication.equals(UsernamePasswordAuthenticationToken.class);
    }

}