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

import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;

import org.egov.eis.model.Assignment;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

@Component
public class AssignmentTableRowMapper implements RowMapper<Assignment> {

	@Override
	public Assignment mapRow(ResultSet rs, int rowNum) throws SQLException, DataAccessException {
		final SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");

		Assignment assignment = new Assignment();
		assignment.setId(rs.getLong("id"));
		assignment.setPosition(rs.getLong("positionId"));
		assignment.setFund((rs.getLong("fundId") == 0L ? null : rs.getLong("fundId")));
		assignment.setFunctionary((rs.getLong("functionaryId") == 0L ? null : rs.getLong("functionaryId")));
		assignment.setFunction((rs.getLong("functionId") == 0L ? null : rs.getLong("functionId")));
		assignment.setDepartment(rs.getLong("departmentId"));
		assignment.setDesignation(rs.getLong("designationId"));
		assignment.setIsPrimary(rs.getBoolean("isPrimary"));
		assignment.setGrade((rs.getLong("gradeId") == 0L ? null : rs.getLong("functionId")));
		assignment.setGovtOrderNumber(rs.getString("govtOrderNumber"));
		assignment.setCreatedBy(rs.getLong("createdBy"));
		assignment.setLastModifiedBy(rs.getLong("lastModifiedBy"));
		assignment.setTenantId(rs.getString("tenantId"));
		try {
			assignment.setFromDate(sdf.parse(sdf.format(rs.getDate("a_fromDate"))));
			assignment.setToDate(sdf.parse(sdf.format(rs.getDate("a_toDate"))));
			assignment.setCreatedDate(sdf.parse(sdf.format(rs.getDate("a_createdDate"))));
			assignment.setLastModifiedDate(sdf.parse(sdf.format(rs.getDate("a_lastModifiedDate"))));
		} catch (ParseException e) {
			e.printStackTrace();
			throw new SQLException("Parse exception while parsing date");
		}
		
		return assignment;
	}
}