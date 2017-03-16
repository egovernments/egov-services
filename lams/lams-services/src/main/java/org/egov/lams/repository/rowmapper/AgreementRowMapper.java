package org.egov.lams.repository.rowmapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.egov.lams.model.Agreement;
import org.egov.lams.model.Allottee;
import org.egov.lams.model.Asset;
import org.egov.lams.model.RentIncrementType;
import org.egov.lams.model.enums.NatureOfAllotment;
import org.egov.lams.model.enums.PaymentCycle;
import org.egov.lams.model.enums.Status;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.RowMapper;

public class AgreementRowMapper implements RowMapper<Agreement> {

	public static final Logger logger = LoggerFactory.getLogger(AgreementRowMapper.class);

	@Override
	public Agreement mapRow(ResultSet rs, int rowNum) throws SQLException {
		
		Agreement agreement = new Agreement();
		agreement.setId(rs.getLong("id"));
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

		return agreement;
	}

}
