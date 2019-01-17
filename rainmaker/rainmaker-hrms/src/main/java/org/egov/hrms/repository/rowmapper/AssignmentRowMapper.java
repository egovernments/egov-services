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

import org.egov.hrms.model.Assignment;
import org.egov.hrms.model.HODDepartment;
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
public class AssignmentRowMapper implements ResultSetExtractor<List<Assignment>> {

	@Override
	public List<Assignment> extractData(ResultSet rs) throws SQLException, DataAccessException {
		final SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");

		Map<Long, Assignment> assignmentMap = new HashMap<>();

		while (rs.next()) {
			Long assignmentId = (Long) rs.getObject("a_id");

			Assignment assignment = assignmentMap.get(assignmentId);

			if (assignment == null) {
				assignment = new Assignment();
				assignment.setId((Long) rs.getObject("a_id"));
				assignment.setPosition((Long) rs.getObject("a_positionId"));
				assignment.setFund((Long) rs.getObject("a_fundId"));
				assignment.setFunctionary((Long) rs.getObject("a_functionaryId"));
				assignment.setFunction((Long) rs.getObject("a_functionId"));
                assignment.setDesignation(rs.getString("a_designationId"));
				assignment.setDepartment(rs.getString("a_departmentId"));
				assignment.setIsPrimary((Boolean) rs.getObject("a_isPrimary"));
				assignment.setGrade((Long) rs.getObject("a_gradeId"));
				assignment.setGovtOrderNumber(rs.getString("a_govtOrderNumber"));
				assignment.setCreatedBy((Long) rs.getObject("a_createdBy"));
				assignment.setLastModifiedBy((Long) rs.getObject("a_lastModifiedBy"));
				assignment.setTenantId(rs.getString("a_tenantId"));
				try {
					Date date = isEmpty(rs.getDate("a_fromDate")) ? null
							: sdf.parse(sdf.format(rs.getDate("a_fromDate")));
					assignment.setFromDate(date);
					date = isEmpty(rs.getDate("a_toDate")) ? null : sdf.parse(sdf.format(rs.getDate("a_toDate")));
					assignment.setToDate(date);
					date = isEmpty(rs.getDate("a_createdDate")) ? null
							: sdf.parse(sdf.format(rs.getDate("a_createdDate")));
					assignment.setCreatedDate(date);
					date = isEmpty(rs.getDate("a_lastModifiedDate")) ? null
							: sdf.parse(sdf.format(rs.getDate("a_lastModifiedDate")));
					assignment.setLastModifiedDate(date);
				} catch (ParseException e) {
					e.printStackTrace();
					throw new SQLException("Parse exception while parsing date");
				}

				assignmentMap.put(assignmentId, assignment);
			}

			List<HODDepartment> hodDepartmentsList = assignment.getHod();
			if (hodDepartmentsList == null) {
				hodDepartmentsList = new ArrayList<>();
				assignment.setHod(hodDepartmentsList);
			}
            if (rs.getObject("hod_id") != null) {
				HODDepartment hodDepartment = new HODDepartment();
				hodDepartment.setId((Long) rs.getObject("hod_id"));
				hodDepartment.setDepartment(rs.getString("hod_departmentId"));
				hodDepartment.setTenantId(rs.getString("a_tenantId"));
				hodDepartmentsList.add(hodDepartment);
			}
		}
		return new ArrayList<>(assignmentMap.values());
	}
}