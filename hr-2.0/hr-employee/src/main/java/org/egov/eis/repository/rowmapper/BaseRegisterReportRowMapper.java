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

package org.egov.eis.repository.rowmapper;

import lombok.Getter;
import lombok.Setter;
import org.egov.eis.model.EmployeeInfo;
import org.egov.eis.model.HODDepartment;
import org.egov.eis.model.enums.MaritalStatus;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

import static org.springframework.util.ObjectUtils.isEmpty;

@Component
public class BaseRegisterReportRowMapper implements ResultSetExtractor<List<EmployeeInfo>> {

    @Override
    public List<EmployeeInfo> extractData(ResultSet rs) throws SQLException, DataAccessException {

        Map<Long, EmpInfo> empInfoMap = getEmpInfoMap(rs);
        List<EmployeeInfo> employeeInfoList = getEmployeeInfoList(empInfoMap);

        return employeeInfoList;
    }

    /**
     * Convert flat Result set data into hierarchical/map structure.
     *
     * @param rs
     * @return
     * @throws SQLException
     */
    private Map<Long, EmpInfo> getEmpInfoMap(ResultSet rs) throws SQLException {
        final SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");

        Map<Long, EmpInfo> empInfoMap = new LinkedHashMap<>();

        while (rs.next()) {
            Long employeeId = (Long) rs.getObject("e_id");

            EmpInfo empInfo = empInfoMap.get(employeeId);

            // populate empInfo fields from result set
            if (empInfo == null) {
                empInfo = new EmpInfo();
                empInfo.setId((Long) rs.getObject("e_id"));
                empInfo.setCode(rs.getString("e_code"));
                empInfo.setEmployeeStatus((Long) rs.getObject("e_employeeStatus"));
                try {
                    Date date = isEmpty(rs.getDate("e_dateOfAppointment")) ? null : sdf.parse(sdf.format(rs.getDate("e_dateOfAppointment")));
                    empInfo.setDateOfAppointment(date);
                    date = isEmpty(rs.getDate("e_dateOfRetirement")) ? null : sdf.parse(sdf.format(rs.getDate("e_dateOfRetirement")));
                    empInfo.setDateOfRetirement(date);
                    date = isEmpty(rs.getDate("e_dateOfJoining")) ? null : sdf.parse(sdf.format(rs.getDate("e_dateOfJoining")));
                    empInfo.setDateOfJoining(date);
                    date = isEmpty(rs.getDate("e_dateOfResignation")) ? null : sdf.parse(sdf.format(rs.getDate("e_dateOfResignation")));
                    empInfo.setDateOfResignation(date);
                    date = isEmpty(rs.getDate("e_dateOfTermination")) ? null : sdf.parse(sdf.format(rs.getDate("e_dateOfTermination")));
                    empInfo.setDateOfTermination(date);
                } catch (ParseException e) {
                    e.printStackTrace();
                    throw new SQLException("Parse exception while parsing date");
                }
                empInfo.setPlaceOfBirth(rs.getString("e_placeOfBirth"));
                MaritalStatus maritalStatus = isEmpty(rs.getString("e_maritalStatus")) ? null : MaritalStatus.fromValue(rs.getString("e_maritalStatus"));
                empInfo.setMaritalStatus(maritalStatus);
                empInfo.setMotherTongue(rs.getLong("e_motherTongueId"));
                empInfo.setPassportNo(rs.getString("e_passportNo"));
                empInfo.setGpfNo(rs.getString("e_gpfNo"));
                empInfo.setRetirementAge(rs.getShort("e_retirementAge"));
                empInfo.setGroup(rs.getLong("e_groupId"));
                empInfo.setRecruitmentMode(rs.getLong("e_recruitmentModeId"));
                empInfo.setRecruitmentType(rs.getLong("e_recruitmentTypeId"));
                empInfo.setRecruitmentQuota(rs.getLong("e_recruitmentQuotaId"));
                empInfo.setEmployeeType((Long) rs.getObject("e_employeeTypeId"));
                empInfo.setBank((Long) rs.getObject("e_bankId"));
                empInfo.setBankBranch((Long) rs.getObject("e_bankBranchId"));
                empInfo.setBankAccount(rs.getString("e_bankAccount"));
                empInfo.setTenantId(rs.getString("e_tenantId"));
                empInfoMap.put(employeeId, empInfo);
            }

            List<Long> languageIds = empInfo.getLanguageIds();
            Long languageId = (Long) rs.getObject("l_languageId");

            if (languageId != null && languageIds != null && !languageIds.contains(languageId))
                languageIds.add(languageId);


        }
        return empInfoMap;
    }

    /**
     * Convert intermediate Map into List of EmployeeInfo
     *
     * @param empInfoMap
     * @return
     */
    private List<EmployeeInfo> getEmployeeInfoList(Map<Long, EmpInfo> empInfoMap) {
        List<EmployeeInfo> employeeInfoList = new ArrayList<>();
        for (Map.Entry<Long, EmpInfo> empInfoEntry : empInfoMap.entrySet()) {
            EmpInfo empInfo = empInfoEntry.getValue();

            EmployeeInfo employeeInfo = EmployeeInfo.builder().id(empInfo.getId()).code(empInfo.getCode())
                    .employeeStatus(empInfo.getEmployeeStatus()).employeeType(empInfo.getEmployeeType()).group(empInfo.getGroup())
                    .maritalStatus(empInfo.getMaritalStatus()).placeOfBirth(empInfo.getPlaceOfBirth())
                    .motherTongue(empInfo.getMotherTongue()).passportNo(empInfo.getPassportNo()).gpfNo(empInfo.getGpfNo())
                    .recruitmentMode(empInfo.getRecruitmentMode()).recruitmentQuota(empInfo.getRecruitmentQuota()).recruitmentType(empInfo.getRecruitmentType())
                    .retirementAge(empInfo.getRetirementAge()).dateOfAppointment(empInfo.getDateOfAppointment()).dateOfJoining(empInfo.getDateOfJoining())
                    .dateOfRetirement(empInfo.getDateOfRetirement()).dateOfResignation(empInfo.getDateOfResignation()).dateOfTermination(empInfo.getDateOfTermination())
                    .bank(empInfo.getBank()).bankBranch(empInfo.getBankBranch()).bankAccount(empInfo.getBankAccount())
                    .tenantId(empInfo.getTenantId()).build();

            employeeInfo.setLanguagesKnown(empInfo.getLanguageIds());

            employeeInfoList.add(employeeInfo);
        }
        return employeeInfoList;
    }

    /**
     * Intermediate class that holds the flat resultset from database in
     * object/hierarchical form. Note that the Map assignments makes it easy for
     * adding unique assignments
     */
    @Getter
    @Setter
    private class EmpInfo {
        private Long id;
        private String code;
        private Long employeeStatus;
        private Long employeeType;
        private Long group;
        private MaritalStatus maritalStatus;
        private String placeOfBirth;
        private Long motherTongue;
        private String passportNo;
        private String gpfNo;
        private Long recruitmentMode;
        private Long recruitmentType;
        private Long recruitmentQuota;
        private Short retirementAge;
        private Date dateOfAppointment;
        private Date dateOfJoining;
        private Date dateOfRetirement;
        private Date dateOfResignation;
        private Date dateOfTermination;
        private List<Long> languageIds = new ArrayList<>();
        private Long bank;
        private Long bankBranch;
        private String bankAccount;
        private String tenantId;
    }


    @Getter
    @Setter
    private class AssignmentInfo {
        private Long id;
        private Long position;
        private Long fund;
        private Long functionary;
        private Long function;
        private Long department;
        private Long designation;
        private Boolean isPrimary;
        private Date fromDate;
        private Date toDate;
        private Long grade;
        private String govtOrderNumber;
        private Long createdBy;
        private Date createdDate;
        private Long lastModifiedBy;
        private Date lastModifiedDate;
        // Key is id for HODDepartment in the hodDeptMap map
        private Map<Long, HODDepartment> hodDeptMap = new LinkedHashMap<>();
    }
}