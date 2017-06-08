package org.egov.lams.repository.rowmapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.egov.lams.model.Agreement;
import org.egov.lams.model.Allottee;
import org.egov.lams.model.Asset;
import org.egov.lams.model.Cancellation;
import org.egov.lams.model.Renewal;
import org.egov.lams.model.RentIncrementType;
import org.egov.lams.model.enums.Action;
import org.egov.lams.model.enums.NatureOfAllotment;
import org.egov.lams.model.enums.PaymentCycle;
import org.egov.lams.model.enums.ReasonForCancellation;
import org.egov.lams.model.enums.Source;
import org.egov.lams.model.enums.Status;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ResultSetExtractor;

public class AgreementRowMapper implements ResultSetExtractor<List<Agreement>> {

	public static final Logger logger = LoggerFactory.getLogger(AgreementRowMapper.class);

	@Override
	public List<Agreement> extractData(ResultSet rs) throws SQLException, DataAccessException {

		Map<Long, Agreement> AgreementMap = new HashMap<>();
		logger.info("after creating map object ::: " + AgreementMap.size());
		String AgreementIdRsName = "lamsagreementid";

		while (rs.next()) {

			try {

				Long agreementId = (Long) rs.getObject(AgreementIdRsName);
				logger.info("agreementid in row mapper" + agreementId);
				Agreement agreement = AgreementMap.get(agreementId);

				if (agreement == null) {
					
					agreement = new Agreement();

					String action = rs.getString("action");
					agreement.setAction(Action.fromValue(action));
					Object object = getProcessObject(agreement, agreement.getAction(), rs);
					if(object!=null){
					if (object instanceof Cancellation)
						agreement.setCancellation((Cancellation) object);
					else if (object instanceof Renewal)
						agreement.setRenewal((Renewal) object);
					}
					
					agreement.setId((Long) rs.getObject(AgreementIdRsName));
					agreement.setAcknowledgementNumber(rs.getString("acknowledgementnumber"));
					agreement.setStateId(rs.getString("stateid"));
					agreement.setGoodWillAmount(rs.getDouble("goodwillamount"));
					agreement.setTimePeriod((Long) rs.getObject("timePeriod"));
					agreement.setAgreementDate(rs.getTimestamp("agreement_date"));
					agreement.setAgreementNumber(rs.getString("agreement_no"));
					agreement.setBankGuaranteeAmount(rs.getDouble("bank_guarantee_amount"));
					agreement.setBankGuaranteeDate(rs.getTimestamp("bank_guarantee_date"));
					agreement.setCaseNo(rs.getString("case_no"));
					agreement.setCommencementDate(rs.getTimestamp("commencement_date"));
					agreement.setCouncilDate(rs.getTimestamp("council_date"));
					agreement.setCouncilNumber(rs.getString("council_number"));
					agreement.setExpiryDate(rs.getTimestamp("expiry_date"));

					String source = rs.getString("source");
					agreement.setSource(Source.fromValue(source));
					agreement.setCollectedGoodWillAmount(rs.getDouble("collectedGoodWillAmount"));
					agreement.setCollectedSecurityDeposit(rs.getDouble("collectedSecurityDeposit"));

					String natureOfAllotment = rs.getString("nature_of_allotment");
					agreement.setNatureOfAllotment(NatureOfAllotment.fromValue(natureOfAllotment));
					//agreement.setOrderDate(rs.getTimestamp("order_date"));
					agreement.setOrderDetails(rs.getString("order_details"));
					//agreement.setOrderNumber(rs.getString("order_no"));

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

					RentIncrementType rentIncrementType = new RentIncrementType();
					rentIncrementType.setId((Long) rs.getObject("rent_increment_method"));
					agreement.setRentIncrementMethod(rentIncrementType);

					Allottee allottee = new Allottee();
					allottee.setId((Long) rs.getObject("allottee"));
					agreement.setAllottee(allottee);

					Asset asset = new Asset();
					asset.setId((Long) rs.getObject("asset"));
					agreement.setAsset(asset);

					AgreementMap.put(agreementId, agreement);
				}

				List<String> demandIdList = agreement.getDemands();
				String demandId = rs.getString("demandid");
				if (demandId != null) {
					if (demandIdList == null)
						demandIdList = new ArrayList<>();
					demandIdList.add(demandId);
				}
				agreement.setDemands(demandIdList);
			} catch (Exception e) {
				logger.info("exception in agreementRoqwMapper : " + e);
				e.printStackTrace();
				throw new RuntimeException("error while mapping object from reult set : " + e.getCause());
			}
		}
		logger.info("converting map to list object ::: " + AgreementMap.values());
		return new ArrayList<>(AgreementMap.values());
	}
	
	private Object getProcessObject(Agreement agreement, Action action,ResultSet rs) throws SQLException {
		
		
		Date terminationDate=rs.getDate("terminationdate");
		String courtReferenceNumber= rs.getString("courtreferencenumber");
		String orderNumber = rs.getString("order_no");
		Date orderDate = rs.getDate("order_date");
		String reason=rs.getString("reason");
		
		if(action!=null){
		switch (action) {

		case CANCELATION:
					Cancellation cancellation = new Cancellation();
					cancellation.setOrderNumber(orderNumber);
					cancellation.setOrderDate(orderDate);
					cancellation.setReasonForCancellation(ReasonForCancellation.fromValue(reason));
					cancellation.setTerminationDate(terminationDate);
					return cancellation;
		case RENEWAL:
					Renewal renewal = agreement.getRenewal();
					renewal.setRenewalOrderNumber(orderNumber);
					renewal.setRenewalOrderDate(orderDate);
					renewal.setReasonForRenewal(reason);
					return renewal;
		case EVICTION:
			break;
		case CREATE:
			agreement.setOrderNumber(orderNumber);
			agreement.setOrderDate(orderDate);
			return null;
			
		case OBJECTION:
			return null;
		}
		}
		return null;
	}
}
