package org.egov.lams.repository.rowmapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.egov.lams.model.Agreement;
import org.egov.lams.model.Allottee;
import org.egov.lams.model.Asset;
import org.egov.lams.model.RentIncrementType;
import org.egov.lams.model.enums.NatureOfAllotment;
import org.egov.lams.model.enums.PaymentCycle;
import org.egov.lams.model.enums.Status;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ResultSetExtractor;

public class AgreementRowMapper implements ResultSetExtractor<List<Agreement>> {

	public static final Logger logger = LoggerFactory.getLogger(AgreementRowMapper.class);

	@Override
	public List<Agreement> extractData(ResultSet rs) throws SQLException, DataAccessException {

		Map<Long,Agreement> AgreementMap = new HashMap<>();
		logger.info("after creating map object ::: "+AgreementMap.size());
		
		
		while(rs.next()){
			
			Long agreementId = rs.getLong("agreementid");
			System.err.println(agreementId);
			Agreement agreement = AgreementMap.get(agreementId);
			
			if(agreement == null){
				
				agreement = new Agreement();
				agreement.setId(rs.getLong("id"));
				agreement.setAcknowledgementNumber(rs.getString("acknowledgementnumber"));
				agreement.setStateId(rs.getString("stateid"));
				agreement.setGoodWillAmount(rs.getDouble("goodwillamount"));
				//agreement.setCloseDate(rs.getDate("closedate"));
				agreement.setTimePeriod(rs.getLong("timePeriod"));
				agreement.setAgreementDate(rs.getTimestamp("agreement_date"));
				agreement.setAgreementNumber(rs.getString("agreement_no"));
				agreement.setBankGuaranteeAmount(rs.getDouble("bank_guarantee_amount"));
				agreement.setBankGuaranteeDate(rs.getTimestamp("bank_guarantee_date"));
				agreement.setCaseNo(rs.getString("case_no"));
				agreement.setCommencementDate(rs.getTimestamp("commencement_date"));
				agreement.setCouncilDate(rs.getTimestamp("council_date"));
				agreement.setCouncilNumber(rs.getString("council_number"));
				agreement.setExpiryDate(rs.getTimestamp("expiry_date"));
				
				String natureOfAllotment = rs.getString("nature_of_allotment");
				agreement.setNatureOfAllotment(NatureOfAllotment.fromValue(natureOfAllotment));
				agreement.setOrderDate(rs.getTimestamp("order_date"));
				agreement.setOrderDetails(rs.getString("order_details"));
				agreement.setOrderNo(rs.getString("order_no"));
				
				String PaymentCycleValue = rs.getString("payment_cycle");
				agreement.setPaymentCycle(PaymentCycle.fromValue(PaymentCycleValue));
				agreement.setRegistrationFee(rs.getDouble("registration_fee"));
				agreement.setRemarks(rs.getString("remarks"));
				agreement.setRent(rs.getDouble("rent"));
				agreement.setRrReadingNo(rs.getString("rr_reading_no"));
				
				String status = rs.getString("status");
				agreement.setStatus(Status.fromValue(status));
				agreement.setTinNumber(rs.getString("tin_number"));
				agreement.setTenantId(rs.getString("tenant_id"));
				agreement.setTenderDate(rs.getTimestamp("tender_date"));
				agreement.setTenderNumber(rs.getString("tender_number"));
				agreement.setSecurityDeposit(rs.getDouble("security_deposit"));
				agreement.setSecurityDepositDate(rs.getTimestamp("security_deposit_date"));
				agreement.setSolvencyCertificateDate(rs.getTimestamp("solvency_certificate_date"));
				agreement.setSolvencyCertificateNo(rs.getString("solvency_certificate_no"));
				agreement.setTradelicenseNumber(rs.getString("trade_license_number"));
				
				
				RentIncrementType rentIncrementType=new RentIncrementType();
				rentIncrementType.setId(rs.getLong("rent_increment_method"));
				agreement.setRentIncrementMethod(rentIncrementType);

				Allottee allottee = new Allottee();
				allottee.setId(rs.getLong("allottee"));
				agreement.setAllottee(allottee);

				Asset asset = new Asset();
				asset.setId(rs.getLong("asset"));
				agreement.setAsset(asset);
				
				AgreementMap.put(agreementId,agreement);
			}
			
			List<String> demandIdList = agreement.getDemands();
			if(demandIdList == null)
				demandIdList = new ArrayList<>();
			demandIdList.add(rs.getString("demandid"));
			agreement.setDemands(demandIdList);
		}
		logger.info("converting map to list object ::: "+AgreementMap.values());
		return new ArrayList<>(AgreementMap.values());
	}

	

}
