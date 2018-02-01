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

import org.egov.eis.model.LeaveType;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import static org.springframework.util.ObjectUtils.isEmpty;

@Component
public class LeaveTypeRowMapper implements RowMapper<LeaveType> {

	@Override
	public LeaveType mapRow(ResultSet rs, int rowNum) throws SQLException {
		final SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");

		LeaveType leaveType = new LeaveType();
		leaveType.setId(rs.getLong("id"));
		leaveType.setName(rs.getString("name"));
		leaveType.setDescription(rs.getString("description"));
		leaveType.setHalfdayAllowed((Boolean) rs.getObject("halfdayAllowed"));
		leaveType.setPayEligible((Boolean) rs.getObject("payEligible"));
		leaveType.setAccumulative((Boolean) rs.getObject("accumulative"));
		leaveType.setEncashable((Boolean) rs.getObject("encashable"));
		leaveType.setEncloseHoliday((Boolean) rs.getObject("encloseHoliday"));
		leaveType.setIncludePrefixSuffix((Boolean) rs.getObject("includePrefixSuffix"));
		leaveType.setActive((Boolean) rs.getObject("active"));
		leaveType.setCreatedBy((Long) rs.getObject("createdBy"));
		leaveType.setLastModifiedBy((Long) rs.getObject("lastModifiedBy"));
		leaveType.setTenantId(rs.getString("tenantId"));
		try {
			Date date = isEmpty(rs.getDate("createdDate")) ? null : sdf.parse(sdf.format(rs.getDate("createdDate")));
			leaveType.setCreatedDate(date);
			date = isEmpty(rs.getDate("lastModifiedDate")) ? null : sdf.parse(sdf.format(rs.getDate("lastModifiedDate")));
			leaveType.setLastModifiedDate(date);
		} catch (ParseException e) {
			e.printStackTrace();
			throw new SQLException("Parse exception while parsing date");
		}

		return leaveType;
	}
}