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

package org.egov.user.domain.service;

import org.apache.commons.lang3.StringUtils;
import org.egov.user.domain.exception.OtpValidationPendingException;
import org.egov.user.persistence.entity.Role;
import org.egov.user.persistence.entity.User;
import org.egov.user.persistence.repository.RoleRepository;
import org.egov.user.persistence.repository.UserRepository;
import org.egov.user.web.contract.Otp;
import org.egov.user.web.contract.RequestInfo;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class UserService {

    private UserRepository userRepository;
    private RoleRepository roleRepository;
    private OtpService otpService;

    public UserService(UserRepository userRepository, RoleRepository roleRepository, OtpService otpService) {
        this.roleRepository = roleRepository;
        this.userRepository = userRepository;
        this.otpService = otpService;
    }

    public User getUserByUsername(final String userName) {
        return userRepository.findByUsername(userName);
    }

    public User getUserByEmailId(final String emailId) {
        return userRepository.findByEmailId(emailId);
    }

    public List<User> getUsersById(List<Long> ids) {
        return userRepository.findAll(ids);
    }

    public User save(RequestInfo requestInfo, org.egov.user.domain.model.User user, Boolean ensureOtpValidation) {
        user.validate();
        if(ensureOtpValidation && !otpService.isOtpValidationComplete(requestInfo, user)) throw new OtpValidationPendingException(user);
        User userToPersist = new User().fromDomain(user);
        referenceRolesById(userToPersist);
        return userRepository.save(userToPersist);
    }

    private void referenceRolesById(User userToPersist) {
        if(userToPersist.getRoles() == null || userToPersist.getRoles().size() == 0) return;
        Set<Role> enrichedRoles = userToPersist.getRoles()
                .stream()
                .map((role) -> roleRepository.findByName(role.getName()))
                .collect(Collectors.toSet());
        userToPersist.setRoles(enrichedRoles);
    }
}