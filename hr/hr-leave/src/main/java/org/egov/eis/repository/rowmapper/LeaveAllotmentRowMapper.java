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

import org.egov.eis.model.LeaveAllotment;
import org.egov.eis.model.LeaveType;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

@Component
public class LeaveAllotmentRowMapper implements RowMapper<LeaveAllotment> {

	@Override
	public LeaveAllotment mapRow(ResultSet rs, int rowNum) throws SQLException {
		LeaveAllotment leaveAllotment = new LeaveAllotment();
		leaveAllotment.setId(rs.getLong("la_id"));
		leaveAllotment.setDesignation(rs.getLong("la_designationId"));

		LeaveType leaveType = new LeaveType();
		leaveType.setId(rs.getLong("lt_id"));
		leaveType.setName(rs.getString("lt_name"));
		leaveType.setDescription(rs.getString("lt_description"));
		leaveType.setHalfdayAllowed(rs.getBoolean("lt_halfdayAllowed"));
		leaveType.setPayEligible(rs.getBoolean("lt_payEligible"));
		leaveType.setAccumulative(rs.getBoolean("lt_accumulative"));
		leaveType.setEncashable(rs.getBoolean("lt_encashable"));
		leaveType.setActive(rs.getBoolean("lt_active"));
		leaveType.setCreatedBy(rs.getLong("lt_createdBy"));
		leaveType.setCreatedDate(rs.getDate("lt_createdDate"));
		leaveType.setLastModifiedBy(rs.getLong("lt_lastModifiedBy"));
		leaveType.setLastModifiedDate(rs.getDate("lt_lastModifiedDate"));
		leaveType.setTenantId(rs.getString("la_tenantId"));

		leaveAllotment.setLeaveType(leaveType);

		leaveAllotment.setNoOfDays(rs.getFloat("la_noOfDays"));
		leaveAllotment.setCreatedBy(rs.getLong("la_createdBy"));
		leaveAllotment.setCreatedDate(rs.getDate("la_createdDate"));
		leaveAllotment.setLastModifiedBy(rs.getLong("la_lastModifiedBy"));
		leaveAllotment.setLastModifiedDate(rs.getDate("la_lastModifiedDate"));
		leaveAllotment.setTenantId(rs.getString("la_tenantId"));
		return leaveAllotment;
	}
}