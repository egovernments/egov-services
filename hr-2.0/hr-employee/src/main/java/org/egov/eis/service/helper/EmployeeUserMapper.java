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

package org.egov.eis.service.helper;

import org.egov.eis.model.EmployeeInfo;
import org.egov.eis.model.User;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
public class EmployeeUserMapper {

    public List<EmployeeInfo> mapUsersWithEmployees(List<EmployeeInfo> employeeInfoList, List<User> userInfoList) {
        Map<Long, User> userInfoMap = new HashMap<Long, User>();
        List<EmployeeInfo> finalEmployeeList = new ArrayList<EmployeeInfo>();
        if (userInfoList != null)
            for (User userInfo : userInfoList) {
                userInfoMap.put(userInfo.getId(), userInfo);
            }
        if (employeeInfoList != null)
            for (EmployeeInfo employeeInfo : employeeInfoList) {
                if (userInfoMap.containsKey(employeeInfo.getId())) {
                    User userInfo = userInfoMap.get(employeeInfo.getId());

                    employeeInfo.setSalutation(userInfo.getSalutation());
                    employeeInfo.setName(userInfo.getName());
                    employeeInfo.setUserName(userInfo.getUserName());
                    employeeInfo.setDob(userInfo.getDob());
                    employeeInfo.setGender(userInfo.getGender());
                    employeeInfo.setMobileNumber(userInfo.getMobileNumber());
                    employeeInfo.setEmailId(userInfo.getEmailId());
                    employeeInfo.setPan(userInfo.getPan());
                    employeeInfo.setAadhaarNumber(userInfo.getAadhaarNumber());
                    employeeInfo.setType(userInfo.getType());
                    employeeInfo.setActive(userInfo.getActive());
                    finalEmployeeList.add(employeeInfo);
                }
            }
        return finalEmployeeList;
    }

    public List<EmployeeInfo> mapUsersWithEmployeesForReport(List<EmployeeInfo> employeeInfoList, List<User> userInfoList) {

        Map<Long, User> userInfoMap = new HashMap<Long, User>();
        List<EmployeeInfo> finalEmployeeList = new ArrayList<EmployeeInfo>();
        Date dob = null;
        if (userInfoList != null)
            for (User userInfo : userInfoList) {
                userInfoMap.put(userInfo.getId(), userInfo);
            }

        if (employeeInfoList != null)
            for (EmployeeInfo employeeInfo : employeeInfoList) {
                if (userInfoMap.containsKey(employeeInfo.getId())) {
                    User userInfo = userInfoMap.get(employeeInfo.getId());
                    employeeInfo.setSalutation(userInfo.getSalutation());
                    employeeInfo.setName(userInfo.getName());
                    employeeInfo.setUserName(userInfo.getUserName());
                    employeeInfo.setDob(userInfo.getDob());
                    employeeInfo.setGender(userInfo.getGender());
                    employeeInfo.setMobileNumber(userInfo.getMobileNumber());
                    employeeInfo.setAltContactNumber(userInfo.getAltContactNumber());
                    employeeInfo.setGuardian(userInfo.getFatherOrHusbandName());
                    employeeInfo.setBloodGroup(userInfo.getBloodGroup());
                    employeeInfo.setIdentificationMark(userInfo.getIdentificationMark());
                    employeeInfo.setEmailId(userInfo.getEmailId());
                    employeeInfo.setPan(userInfo.getPan());
                    employeeInfo.setAadhaarNumber(userInfo.getAadhaarNumber());
                    employeeInfo.setPermanentAddress(userInfo.getPermanentAddress());
                    employeeInfo.setPermanentCity(userInfo.getPermanentCity());
                    employeeInfo.setPermanentPinCode(userInfo.getPermanentPinCode());
                    employeeInfo.setCorrespondenceAddress(userInfo.getCorrespondenceAddress());
                    employeeInfo.setCorrespondenceCity(userInfo.getCorrespondenceCity());
                    employeeInfo.setCorrespondencePinCode(userInfo.getCorrespondencePinCode());
                    employeeInfo.setActive(userInfo.getActive());
                    finalEmployeeList.add(employeeInfo);
                }
            }
        return finalEmployeeList;
    }
}