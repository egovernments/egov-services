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

import org.egov.eis.model.DepartmentDesignation;
import org.egov.eis.model.Designation;
import org.egov.eis.model.Position;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;

@Component
public class PositionRowMapper implements RowMapper<Position> {

	@Override
	public Position mapRow(ResultSet rs, int rowNum) throws SQLException {
		Position position = new Position();
		position.setId(rs.getLong("p_id"));
		position.setName(rs.getString("p_name"));

		Designation designation = new Designation();
		designation.setId(rs.getLong("des_id"));
		designation.setName(rs.getString("des_name"));
		designation.setCode(rs.getString("des_code"));
		designation.setDescription(rs.getString("des_description"));
		designation.setChartOfAccounts(rs.getString("des_chartOfAccounts"));
		designation.setActive((Boolean) rs.getObject("des_active"));
		designation.setTenantId(rs.getString("p_tenantId"));

		DepartmentDesignation departmentDesignation = new DepartmentDesignation();
		departmentDesignation.setId(rs.getLong("depDes_id"));
		departmentDesignation.setDepartmentId((Long) rs.getObject("depDes_departmentId"));
		departmentDesignation.setDesignation(designation);
		departmentDesignation.setTenantId(rs.getString("p_tenantId"));

		position.setDeptdesig(departmentDesignation);
		position.setIsPostOutsourced((Boolean) rs.getObject("p_isPostOutsourced"));
		position.setActive((Boolean) rs.getObject("p_active"));
		position.setTenantId(rs.getString("p_tenantId"));
		return position;
	}
}