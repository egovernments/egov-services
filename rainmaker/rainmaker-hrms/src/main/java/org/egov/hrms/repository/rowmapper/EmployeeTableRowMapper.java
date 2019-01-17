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

package org.egov.hrms.repository.rowmapper;

import org.egov.hrms.model.Employee;
import org.egov.hrms.model.enums.MaritalStatus;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import static org.springframework.util.ObjectUtils.isEmpty;

@Component
public class EmployeeTableRowMapper implements ResultSetExtractor<Employee> {

    @Override
    public Employee extractData(ResultSet rs) throws SQLException, DataAccessException {
        if (!rs.next())
            return null;

        final SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");

        Employee employee = new Employee();
        employee.setId((Long) rs.getObject("id"));
        employee.setCode(rs.getString("code"));
        employee.setEmployeeStatus((Long) rs.getObject("employeeStatus"));
        employee.setRecruitmentMode((Long) rs.getObject("recruitmentmodeid"));
        employee.setRecruitmentType((Long) rs.getObject("recruitmenttypeid"));
        employee.setRecruitmentQuota((Long) rs.getObject("recruitmentquotaid"));
        employee.setRetirementAge(isEmpty(rs.getObject("retirementage")) ? null
                : Short.parseShort((rs.getObject("retirementage").toString())));
        employee.setEmployeeType((Long) rs.getObject("employeetypeid"));
        employee.setMotherTongue((Long) rs.getObject("mothertongueid"));
        employee.setReligion((Long) rs.getObject("religionid"));
        employee.setCommunity((Long) rs.getObject("communityid"));
        employee.setCategory((Long) rs.getObject("categoryid"));
        employee.setPhysicallyDisabled((Boolean) rs.getObject("physicallydisabled"));
        employee.setMedicalReportProduced((Boolean) rs.getObject("medicalReportproduced"));
        employee.setMaritalStatus(
                isEmpty(rs.getString("maritalStatus")) ? null : MaritalStatus.fromValue(rs.getString("maritalStatus")));
        employee.setPassportNo(rs.getString("passportno"));
        employee.setGpfNo(rs.getString("gpfno"));
        employee.setBank((Long) rs.getObject("bankid"));
        employee.setBankBranch((Long) rs.getObject("bankbranchid"));
        employee.setBankAccount(rs.getString("bankaccount"));
        employee.setIfscCode(rs.getString("ifscCode"));
        employee.setGroup((Long) rs.getObject("groupid"));
        employee.setPlaceOfBirth(rs.getString("placeofbirth"));
        employee.setCreatedBy((Long) rs.getObject("createdBy"));
        employee.setLastModifiedBy((Long) rs.getObject("lastModifiedBy"));
        employee.setTenantId(rs.getString("tenantid"));
        try {
            Date date = isEmpty(rs.getDate("dateofappointment")) ? null
                    : sdf.parse(sdf.format(rs.getDate("dateofappointment")));
            employee.setDateOfAppointment(date);
            date = isEmpty(rs.getDate("dateofjoining")) ? null : sdf.parse(sdf.format(rs.getDate("dateofjoining")));
            employee.setDateOfJoining(date);
            date = isEmpty(rs.getDate("dateofretirement")) ? null
                    : sdf.parse(sdf.format(rs.getDate("dateofretirement")));
            employee.setDateOfRetirement(date);
            date = isEmpty(rs.getDate("dateofresignation")) ? null
                    : sdf.parse(sdf.format(rs.getDate("dateofresignation")));
            employee.setDateOfResignation(date);
            date = isEmpty(rs.getDate("dateoftermination")) ? null
                    : sdf.parse(sdf.format(rs.getDate("dateoftermination")));
            employee.setDateOfTermination(date);
            date = isEmpty(rs.getDate("createdDate")) ? null
                    : sdf.parse(sdf.format(rs.getDate("createdDate")));
            employee.setCreatedDate(date);
            date = isEmpty(rs.getDate("lastModifiedDate")) ? null
                    : sdf.parse(sdf.format(rs.getDate("lastModifiedDate")));
            employee.setLastModifiedDate(date);
        } catch (ParseException e) {
            e.printStackTrace();
            throw new SQLException("Parse exception while parsing date");
        }

        return employee;
    }
}
