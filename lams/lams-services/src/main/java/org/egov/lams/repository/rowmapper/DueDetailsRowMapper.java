/*
 *    eGov  SmartCity eGovernance suite aims to improve the internal efficiency,transparency,
 *    accountability and the service delivery of the government  organizations.
 *
 *     Copyright (C) 2017  eGovernments Foundation
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
 *            Further, all user interfaces, including but not limited to citizen facing interfaces,
 *            Urban Local Bodies interfaces, dashboards, mobile applications, of the program and any
 *            derived works should carry eGovernments Foundation logo on the top right corner.
 *
 *            For the logo, please refer http://egovernments.org/html/logo/egov_logo.png.
 *            For any further queries on attribution, including queries on brand guidelines,
 *            please contact contact@egovernments.org
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
 *
 */
package org.egov.lams.repository.rowmapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedHashSet;
import java.util.Set;

import org.egov.lams.model.DefaultersInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ResultSetExtractor;

public class DueDetailsRowMapper implements ResultSetExtractor<Set<DefaultersInfo>> {

	public static final Logger logger = LoggerFactory.getLogger(DueDetailsRowMapper.class);

	@Override
	public Set<DefaultersInfo> extractData(ResultSet rs) throws SQLException, DataAccessException {
		Set<DefaultersInfo> defaultersInfos = new LinkedHashSet<>();
		while (rs.next()) {

			try {
				DefaultersInfo defaultersInfo = new DefaultersInfo();
				defaultersInfo.setId((Long) rs.getObject("id"));
				defaultersInfo.setAgreementNumber(rs.getString("agreementnumber"));
				defaultersInfo.setAction(rs.getString("action"));
				defaultersInfo.setStatus(rs.getString("status"));
				defaultersInfo.setPaymentCycle(rs.getString("paymentCycle"));
				defaultersInfo.setSecurityDeposit(rs.getDouble("securitydeposit"));
				defaultersInfo.setAssetCode(rs.getString("assetcode"));
				defaultersInfo.setAssetCategory((Long) rs.getObject("assetcategory"));
				defaultersInfo.setCategoryName(rs.getString("categoryname"));
				defaultersInfo.setElectionward((Long) rs.getObject("electionward"));
				defaultersInfo.setRevenueWard((Long) rs.getObject("ward"));
				defaultersInfo.setStreet((Long) rs.getObject("street"));
				defaultersInfo.setBlock((Long) rs.getObject("block"));
				defaultersInfo.setLocality((Long) rs.getObject("locality"));
				defaultersInfo.setZone((Long) rs.getObject("zone"));

				defaultersInfo.setAllotteeName(rs.getString("allotteename"));
				defaultersInfo.setMobileNumber(rs.getString("allotteemobilenumber"));
				defaultersInfo.setCommencementDate(rs.getTimestamp("commencementdate"));
				defaultersInfo.setExpiryDate(rs.getTimestamp("expirydate"));
				defaultersInfo.setInstallmentFromDate(rs.getTimestamp("fromdate"));
				defaultersInfo.setInstallmentToDate(rs.getTimestamp("todate"));
				defaultersInfo.setTotalAmount(rs.getDouble("amount"));
				defaultersInfo.setTotalCollection(rs.getDouble("collection"));
				defaultersInfo.setTotalBalance(rs.getDouble("balance"));
				defaultersInfo.setInstallment(rs.getString("installment"));
				defaultersInfo.setLastPaid(rs.getTimestamp("lastpaid"));
				defaultersInfo.setTenantId(rs.getString("tenantid"));
				defaultersInfos.add(defaultersInfo);

			} catch (Exception e) {
				logger.info("exception in agreementInfoRowMapper : " + e);
				throw new RuntimeException("error while mapping object from result set : " + e.getCause());
			}
		}
		return defaultersInfos;
	}
}
