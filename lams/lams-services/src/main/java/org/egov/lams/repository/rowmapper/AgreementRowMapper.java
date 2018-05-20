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
import org.egov.lams.model.Eviction;
import org.egov.lams.model.Judgement;
import org.egov.lams.model.Objection;
import org.egov.lams.model.Remission;
import org.egov.lams.model.Renewal;
import org.egov.lams.model.RentIncrementType;
import org.egov.lams.model.enums.Action;
import org.egov.lams.model.enums.BasisOfAllotment;
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
					agreement = getProcessObject(agreement, agreement.getAction(), rs);

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
					agreement.setCreatedDate(rs.getTimestamp("created_date"));
					agreement.setCreatedBy(rs.getString("created_by"));
					agreement.setLastmodifiedDate(rs.getTimestamp("last_modified_date"));
					agreement.setLastmodifiedBy(rs.getString("last_modified_by"));

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
					agreement.setIsAdvancePaid(rs.getBoolean("is_advancepaid"));
					agreement.setAdjustmentStartDate(rs.getTimestamp("adjustment_start_date"));
					agreement.setIsUnderWorkflow(rs.getBoolean("is_under_workflow"));
					agreement.setFirstAllotment(rs.getString("first_allotment"));
					agreement.setGstin(rs.getString("gstin"));
					agreement.setMunicipalOrderNumber(rs.getString("municipal_order_no"));
					agreement.setMunicipalOrderDate(rs.getTimestamp("municipal_order_date"));
					agreement.setGovernmentOrderNumber(rs.getString("govt_order_no"));
					agreement.setGovernmentOrderDate(rs.getTimestamp("govt_order_date"));
					agreement.setRenewalDate(rs.getTimestamp("renewal_date"));
					agreement.setReservationCategory((Long) rs.getObject("res_category"));
					agreement.setOldAgreementNumber(rs.getString("old_agreement_no"));
					agreement.setReferenceNumber(rs.getString("referenceno"));
					agreement.setFloorNumber(rs.getString("floorno"));
					agreement.setParent((Long) rs.getObject("parent"));
					agreement.setTenderOpeningDate(rs.getTimestamp("tenderopeningdate"));
					agreement.setAuctionAmount(rs.getDouble("auctionamount"));
					agreement.setSolvencyAmount(rs.getDouble("solvencyamount"));
					agreement.setIsHistory(rs.getBoolean("ishistory"));
					agreement.setCgst(rs.getDouble("cgst"));
					agreement.setSgst(rs.getDouble("sgst"));
					agreement.setServiceTax(rs.getDouble("servicetax"));
					agreement.setNoticeNumber(rs.getString("noticeno"));

					String baseAllotment = rs.getString("base_allotment");
					agreement.setBasisOfAllotment(BasisOfAllotment.fromValue(baseAllotment));

					RentIncrementType rentIncrementType = new RentIncrementType();
					rentIncrementType.setId((Long) rs.getObject("rent_increment_method"));
					rentIncrementType.setPercentage(rs.getDouble("percentage"));
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
	
	private Agreement getProcessObject(Agreement agreement, Action action, ResultSet rs) throws SQLException {

		String reason = rs.getString("reason");
		String orderNo = rs.getString("order_no");
		Date orderDate = rs.getTimestamp("order_date");

		if (Action.RENEWAL.equals(action)) {
			Renewal renewal = new Renewal();
			renewal.setRenewalOrderNumber(orderNo);
			renewal.setRenewalOrderDate(orderDate);
			renewal.setReasonForRenewal(reason);

			agreement.setRenewal(renewal);
			return agreement;

		} else if (Action.EVICTION.equals(action)) {
			Eviction eviction = new Eviction();
			eviction.setCourtReferenceNumber(rs.getString("courtreferencenumber"));
			eviction.setEvictionProceedingDate(orderDate);
			eviction.setEvictionProceedingNumber(orderNo);
			eviction.setReasonForEviction(reason);

			agreement.setEviction(eviction);
			return agreement;

		} else if (Action.CANCELLATION.equals(action)) {
			Cancellation cancellation = new Cancellation();
			cancellation.setOrderNumber(orderNo);
			cancellation.setOrderDate(orderDate);
			cancellation.setReasonForCancellation(ReasonForCancellation.fromValue(rs.getString("reason")));
			cancellation.setTerminationDate(rs.getTimestamp("terminationdate"));

			agreement.setCancellation(cancellation);
			return agreement;
		} else if (Action.OBJECTION.equals(action)) {
			Objection objection = new Objection();
			objection.setCourtCaseDate(rs.getTimestamp("courtcase_date"));
			objection.setCourtCaseNo(rs.getString("courtcase_no"));
			objection.setCourtFixedRent(rs.getDouble("courtfixed_rent"));
			objection.setEffectiveDate(rs.getTimestamp("effective_date"));

			agreement.setObjection(objection);
			return agreement;

		} else if (Action.JUDGEMENT.equals(action)) {
			Judgement judgement = new Judgement();
			judgement.setJudgementDate(rs.getTimestamp("judgement_date"));
			judgement.setJudgementNo(rs.getString("judgement_no"));
			judgement.setJudgementRent(rs.getDouble("judgement_rent"));
			judgement.setEffectiveDate(rs.getTimestamp("effective_date"));

			agreement.setJudgement(judgement);
			return agreement;
		} else if (Action.REMISSION.equals(action)) {

			Remission remission = new Remission();
			remission.setRemissionFromDate(rs.getTimestamp("remission_from_date"));
			remission.setRemissionToDate(rs.getTimestamp("remission_to_date"));
			remission.setRemissionReason(reason);
			remission.setRemissionOrder(rs.getString("remission_order_no"));
			remission.setRemissionRent(rs.getDouble("remission_fee"));

			agreement.setRemission(remission);
			return agreement;
		} else
			return agreement;
	}
}