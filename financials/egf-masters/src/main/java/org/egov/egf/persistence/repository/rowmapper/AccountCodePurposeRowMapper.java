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

package org.egov.egf.persistence.repository.rowmapper;

import static org.springframework.util.ObjectUtils.isEmpty;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.egov.egf.persistence.entity.AccountCodePurpose;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

@Component
public class AccountCodePurposeRowMapper implements RowMapper<AccountCodePurpose> {

	@Override
	public AccountCodePurpose mapRow(ResultSet rs, int rowNum) throws SQLException {
		final SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");

		AccountCodePurpose accountCodePurpose = new AccountCodePurpose();
		accountCodePurpose.setId(rs.getLong("acp_id"));
		accountCodePurpose.setName((String) rs.getObject("acp_name"));

		try {
			Date date = isEmpty(rs.getDate("acp_createdDate")) ? null
					: sdf.parse(sdf.format(rs.getDate("acp_createdDate")));
			accountCodePurpose.setCreatedDate(date);
			date = isEmpty(rs.getDate("acp_lastModifiedDate")) ? null
					: sdf.parse(sdf.format(rs.getDate("acp_lastModifiedDate")));
			accountCodePurpose.setLastModifiedDate(date);
		} catch (ParseException e) {
			e.printStackTrace();
			throw new SQLException("Parse exception while parsing date");
		}

		accountCodePurpose.setName(rs.getObject("acp_name").toString());
		accountCodePurpose.setCreatedBy((Long) rs.getObject("acp_createdBy"));
		accountCodePurpose.setLastModifiedBy((Long) rs.getObject("acp_lastModifiedBy"));
		accountCodePurpose.setTenantId(rs.getString("acp_tenantId"));

		return accountCodePurpose;
	}
}