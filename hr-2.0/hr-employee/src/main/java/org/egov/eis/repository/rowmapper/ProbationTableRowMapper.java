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

import static org.springframework.util.ObjectUtils.isEmpty;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.egov.eis.model.Probation;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

@Component
public class ProbationTableRowMapper implements RowMapper<Probation> {

	@Override
	public Probation mapRow(ResultSet rs, int rowNum) throws SQLException, DataAccessException {
		final SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");

		Probation probation = new Probation();

		probation.setId((Long) rs.getObject("id"));
		probation.setDesignation((Long) rs.getObject("designationid"));
		probation.setOrderNo(rs.getString("orderno"));
		probation.setRemarks(rs.getString("remarks"));
		probation.setCreatedBy((Long) rs.getObject("createdby"));
		probation.setLastModifiedBy((Long) rs.getObject("lastmodifiedby"));
		probation.setTenantId(rs.getString("tenantid"));
		try {
			Date date = isEmpty(rs.getDate("declaredon")) ? null : sdf.parse(sdf.format(rs.getDate("declaredon")));
			probation.setDeclaredOn(date);
			date = isEmpty(rs.getDate("orderdate")) ? null : sdf.parse(sdf.format(rs.getDate("orderdate")));
			probation.setOrderDate(date);
			date = isEmpty(rs.getDate("createdDate")) ? null : sdf.parse(sdf.format(rs.getDate("createdDate")));
			probation.setCreatedDate(date);
			date = isEmpty(rs.getDate("lastmodifieddate")) ? null
					: sdf.parse(sdf.format(rs.getDate("lastmodifieddate")));
			probation.setLastModifiedDate(date);
		} catch (ParseException e) {
			e.printStackTrace();
			throw new SQLException("Parse exception while parsing date");
		}

		return probation;
	}
}
