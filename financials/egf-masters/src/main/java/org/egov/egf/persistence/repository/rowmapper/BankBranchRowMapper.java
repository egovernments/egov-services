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

import org.egov.egf.persistence.queue.contract.BankBranchContract;
import org.egov.egf.persistence.queue.contract.BankContract;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

@Component
public class BankBranchRowMapper implements RowMapper<BankBranchContract> {

	@Override
	public BankBranchContract mapRow(ResultSet rs, int rowNum) throws SQLException {
		final SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");

		BankBranchContract bankBranch = new BankBranchContract();
		bankBranch.setId(rs.getLong("bb_id"));

		BankContract bank = new BankContract();
		bank.setId(rs.getLong("b_id"));
		bank.setCode(rs.getString("b_code"));
		bank.setName(rs.getString("b_name"));
		bank.setType(rs.getString("b_type"));
		bank.setDescription(rs.getString("b_description"));
		bank.setActive((Boolean) rs.getObject("b_active"));
		bank.setCreatedBy((Long) rs.getObject("b_createdby"));
		bank.setLastModifiedBy((Long) rs.getObject("b_lastmodifiedby"));
		bank.setTenantId(rs.getString("bb_tenantid"));

		try {
			Date date = isEmpty(rs.getDate("bb_createddate")) ? null
					: sdf.parse(sdf.format(rs.getDate("bb_createddate")));

			bankBranch.setCreatedDate(date);

			date = isEmpty(rs.getDate("b_createddate")) ? null : sdf.parse(sdf.format(rs.getDate("b_createddate")));

			bank.setCreatedDate(date);

			date = isEmpty(rs.getDate("bb_lastmodifieddate")) ? null
					: sdf.parse(sdf.format(rs.getDate("bb_lastmodifieddate")));

			bankBranch.setLastModifiedDate(date);

			date = isEmpty(rs.getDate("b_lastmodifieddate")) ? null
					: sdf.parse(sdf.format(rs.getDate("b_lastmodifieddate")));

			bank.setLastModifiedDate(date);

		} catch (ParseException e) {
			e.printStackTrace();
			throw new SQLException("Parse exception while parsing date");
		}

		bankBranch.setBank(bank);

		bankBranch.setCode(rs.getObject("bb_code").toString());
		bankBranch.setName(rs.getObject("bb_name").toString());
		bankBranch.setAddress(rs.getObject("bb_address") != null ? rs.getObject("bb_address").toString() : "");
		bankBranch.setAddress2(rs.getObject("bb_address2") != null ? rs.getObject("bb_address2").toString() : "");
		bankBranch.setCity(rs.getObject("bb_city") != null ? rs.getObject("bb_city").toString() : "");
		bankBranch.setState(rs.getObject("bb_state") != null ? rs.getObject("bb_state").toString() : "");
		bankBranch.setFax(rs.getObject("bb_fax") != null ? rs.getObject("bb_fax").toString() : "");
		bankBranch.setPincode(rs.getObject("bb_pincode") != null ? rs.getObject("bb_pincode").toString() : "");
		bankBranch.setPhone(rs.getObject("bb_phone") != null ? rs.getObject("bb_phone").toString() : "");
		bankBranch.setContactPerson(
				rs.getObject("bb_contactperson") != null ? rs.getObject("bb_contactperson").toString() : "");
		bankBranch.setActive((Boolean) rs.getObject("bb_active"));
		bankBranch.setDescription(
				rs.getObject("bb_description") != null ? rs.getObject("bb_description").toString() : "");
		bankBranch.setMicr(rs.getObject("bb_micr") != null ? rs.getObject("bb_micr").toString() : "");
		bankBranch.setCreatedBy((Long) rs.getObject("bb_createdby"));
		bankBranch.setLastModifiedBy((Long) rs.getObject("bb_lastmodifiedby"));
		bankBranch.setTenantId(rs.getString("bb_tenantid"));

		return bankBranch;
	}
}