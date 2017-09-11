/*
 * eGov suite of products aim to improve the internal efficiency,transparency,
 *    accountability and the service delivery of the government  organizations.
 *
 *     Copyright (C) <2015>  eGovernments Foundation
 *
 *     The updated version of eGov suite of products as by eGovernments Foundation
 *     is available at http://www.egovernments.org
 *
 *     This program is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     any later version.
 *
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 *
 *     You should have received a copy of the GNU General Public License
 *     along with this program. If not, see http://www.gnu.org/licenses/ or
 *     http://www.gnu.org/licenses/gpl.html .
 *
 *     In addition to the terms of the GPL license to be adhered to in using this
 *     program, the following additional terms are to be complied with:
 *
 *         1) All versions of this program, verbatim or modified must carry this
 *            Legal Notice.
 *
 *         2) Any misrepresentation of the origin of the material is prohibited. It
 *            is required that all modified versions of this material be marked in
 *            reasonable ways as different from the original version.
 *
 *         3) This license does not grant any rights to any user of the program
 *            with regards to rights under trademark law for use of the trade names
 *            or trademarks of eGovernments Foundation.
 *
 *   In case of any queries, you can reach eGovernments Foundation at contact@egovernments.org.
 */
package org.egov.wcms.repository.rowmapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.egov.wcms.model.UsageType;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

@Component
public class UsageTypeRowMapper implements RowMapper<UsageType> {

    @Override
    public UsageType mapRow(final ResultSet rs, final int rowNum) throws SQLException {
        final UsageType usageType = new UsageType();
        usageType.setId(rs.getLong("ut_id"));
        usageType.setCode(rs.getString("ut_code"));
        usageType.setActive((Boolean) rs.getObject("ut_active"));
        usageType.setDescription(rs.getString("ut_description"));
        usageType.setName(rs.getString("ut_name"));
        usageType.setParent(rs.getString("ut_parent"));
        usageType.setTenantId(rs.getString("ut_tenantid"));
        usageType.setCreatedBy((Long) rs.getObject("ut_createdby"));
        usageType.setCreatedDate((Long) rs.getObject("ut_createddate"));
        usageType.setLastModifiedBy((Long) rs.getObject("ut_lastmodifiedby"));
        usageType.setLastModifiedDate((Long) rs.getObject("ut_lastmodifieddate"));
        return usageType;
    }

}
