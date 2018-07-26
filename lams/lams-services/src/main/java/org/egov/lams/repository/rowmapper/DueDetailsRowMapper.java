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
