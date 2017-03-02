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

import org.egov.eis.model.DepartmentDesignation;
import org.egov.eis.model.Designation;
import org.egov.eis.model.ObjectType;
import org.egov.eis.model.Position;
import org.egov.eis.model.PositionHierarchy;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

@Component
public class PositionHierarchyRowMapper implements RowMapper<PositionHierarchy> {

	@Override
	public PositionHierarchy mapRow(ResultSet rs, int rowNum) throws SQLException {
		PositionHierarchy positionHierarchy = new PositionHierarchy();
		positionHierarchy.setId(rs.getLong("ph_id"));

		Designation fromPositionDesignation = new Designation();
		fromPositionDesignation.setId(rs.getLong("fpDesig_id"));
		fromPositionDesignation.setName(rs.getString("fpDesig_name"));
		fromPositionDesignation.setCode(rs.getString("fpDesig_code"));
		fromPositionDesignation.setChartOfAccounts(rs.getString("fpDesig_chartOfAccounts"));
		fromPositionDesignation.setDescription(rs.getString("fpDesig_description"));
		fromPositionDesignation.setActive(rs.getBoolean("fpDesig_active"));
		fromPositionDesignation.setTenantId(rs.getString("ph_tenantId"));

		DepartmentDesignation fromPositionDepartmentDesignation = new DepartmentDesignation();
		fromPositionDepartmentDesignation.setId(rs.getLong("fpDepDes_id"));
		fromPositionDepartmentDesignation.setDepartmentId(rs.getLong("fpDepDes_departmentId"));
		fromPositionDepartmentDesignation.setDesignation(fromPositionDesignation);
		fromPositionDepartmentDesignation.setTenantId(rs.getString("ph_tenantId"));

		Position fromPosition = new Position();
		fromPosition.setId(rs.getLong("fp_id"));
		fromPosition.setName(rs.getString("fp_name"));
		fromPosition.setDeptdesig(fromPositionDepartmentDesignation);
		fromPosition.setIsPostOutsourced(rs.getBoolean("fp_isPostOutsourced"));
		fromPosition.setActive(rs.getBoolean("fp_active"));
		fromPosition.setTenantId(rs.getString("ph_tenantId"));

		Designation toPositionDesignation = new Designation();
		toPositionDesignation.setId(rs.getLong("tpDesig_id"));
		toPositionDesignation.setName(rs.getString("tpDesig_name"));
		toPositionDesignation.setCode(rs.getString("tpDesig_code"));
		toPositionDesignation.setChartOfAccounts(rs.getString("tpDesig_chartOfAccounts"));
		toPositionDesignation.setDescription(rs.getString("tpDesig_description"));
		toPositionDesignation.setActive(rs.getBoolean("tpDesig_active"));
		toPositionDesignation.setTenantId(rs.getString("ph_tenantId"));

		DepartmentDesignation toPositionDepartmentDesignation = new DepartmentDesignation();
		toPositionDepartmentDesignation.setId(rs.getLong("tpDepDes_id"));
		toPositionDepartmentDesignation.setDepartmentId(rs.getLong("tpDepDes_departmentId"));
		toPositionDepartmentDesignation.setDesignation(toPositionDesignation);
		toPositionDepartmentDesignation.setTenantId(rs.getString("ph_tenantId"));

		Position toPosition = new Position();
		toPosition.setId(rs.getLong("tp_id"));
		toPosition.setName(rs.getString("tp_name"));
		toPosition.setDeptdesig(toPositionDepartmentDesignation);
		toPosition.setIsPostOutsourced(rs.getBoolean("tp_isPostOutsourced"));
		toPosition.setActive(rs.getBoolean("tp_active"));
		toPosition.setTenantId(rs.getString("ph_tenantId"));

		ObjectType objectType = new ObjectType();
		objectType.setId(rs.getLong("ot_id"));
		objectType.setType(rs.getString("ot_type"));
		objectType.setDescription(rs.getString("ot_description"));
		objectType.setLastModifiedDate(rs.getDate("ot_lastModifiedDate"));
		objectType.setTenantId(rs.getString("ph_tenantId"));

		positionHierarchy.setFromPosition(fromPosition);
		positionHierarchy.setToPosition(toPosition);
		positionHierarchy.setObjectType(objectType);
		positionHierarchy.setObjectSubType(rs.getString("ph_objectSubtype"));
		positionHierarchy.setTenantId(rs.getString("ph_tenantId"));
		return positionHierarchy;
	}
}