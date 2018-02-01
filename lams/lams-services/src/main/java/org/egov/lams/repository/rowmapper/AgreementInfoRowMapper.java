package org.egov.lams.repository.rowmapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import org.egov.lams.model.AgreementInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ResultSetExtractor;

public class AgreementInfoRowMapper implements ResultSetExtractor<List<AgreementInfo>> {

	public static final Logger logger = LoggerFactory.getLogger(AgreementInfoRowMapper.class);

	@Override
	public List<AgreementInfo> extractData(ResultSet rs) throws SQLException, DataAccessException {
		List<AgreementInfo> agreementInfos = new ArrayList<>();
		while (rs.next()) {

			try {
				AgreementInfo agreementInfo = new AgreementInfo();
				agreementInfo.setId((Long) rs.getObject("id"));
				agreementInfo.setAgreementNumber(rs.getString("agreementnumber"));
				agreementInfo.setAllotteeName(rs.getString("allotteename"));
				agreementInfo.setDoorno(rs.getString("doorno"));
				agreementInfo.setLocality(rs.getString("locality"));
				agreementInfo.setRevenueward(rs.getString("revenueward"));
				agreementInfo.setElectionward(rs.getString("electionward"));
				agreementInfo.setTradelicenseNumber(rs.getString("tradelicensenumber"));
				agreementInfo.setTenderNumber(rs.getString("tendernumber"));
				agreementInfo.setTenderDate(rs.getTimestamp("tenderdate"));
				agreementInfo.setBankGuaranteeAmount(rs.getDouble("bankguaranteeamount"));
				agreementInfo.setBankGuaranteeDate(rs.getTimestamp("bankguaranteedate"));
				agreementInfo.setSecurityDeposit(rs.getDouble("securitydeposit"));
				agreementInfo.setSecurityDepositDate(rs.getTimestamp("securitydepositdate"));
				agreementInfo.setGoodWillAmount(rs.getDouble("goodwillamount"));
				agreementInfo.setCommencementDate(rs.getTimestamp("commencementdate"));
				agreementInfo.setTimePeriod(Long.parseLong(rs.getString("timeperiod")));
				agreementInfo.setExpiryDate(rs.getTimestamp("expirydate"));
				agreementInfo.setPaymentCycle(rs.getString("paymentcycle"));
				agreementInfo.setDemand(rs.getDouble("rent"));
				agreementInfo.setBalance(rs.getDouble("pendingrent"));
				agreementInfo.setStatus(rs.getString("status"));
				agreementInfo.setSource(rs.getString("source"));
				
				DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");  
				String councilResolutionDate = dateFormat.format(rs.getDate("councilResolutionDate"));
				agreementInfo.setAssetName(rs.getString("assetname"));
				agreementInfo.setMobileNumber(rs.getString("mobilenumber"));
				agreementInfo.setGstin(rs.getString("gstin"));
				agreementInfo.setCouncilResolutionNo(rs.getString("councilResolutionNo"));
				agreementInfo.setCouncilResolutionDate(rs.getDate("councilResolutionDate"));
				agreementInfo.setCouncilResolutionNumberDate(rs.getString("councilResolutionNo").concat("/").concat(councilResolutionDate));
				agreementInfo.setBasisOfAllotment(rs.getString("basisOfAllotment"));
				agreementInfo.setMethodOfRenewal(rs.getString("methodOfRenewal"));
				agreementInfo.setReservationCategory(rs.getString("reservationcategory"));
				agreementInfo.setShopNo(rs.getString("shoporsurveyno"));
				agreementInfo.setAssetArea(rs.getString("assetarea"));
				agreementInfo.setMonthlyRent(rs.getDouble("monthlyrent"));
				agreementInfo.setCollection(rs.getDouble("collection"));
				agreementInfo.setPenalty(rs.getDouble("penalty"));
				

				agreementInfos.add(agreementInfo);

			} catch (Exception e) {
				logger.info("exception in agreementInfoRowMapper : " + e);
				throw new RuntimeException("error while mapping object from result set : " + e.getCause());
			}
		}

		return agreementInfos;
	}
}