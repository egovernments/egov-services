package org.egov.lcms.repository.rowmapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.egov.lcms.config.PropertiesManager;
import org.egov.lcms.models.Advocate;
import org.egov.lcms.models.AdvocateCharge;
import org.egov.lcms.models.AdvocatePayment;
import org.egov.lcms.models.AuditDetails;
import org.egov.lcms.models.CaseStatus;
import org.egov.lcms.models.CaseType;
import org.egov.lcms.models.Document;
import org.egov.tracer.model.CustomException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * 
 * @author Shubham Pratap
 *
 */

@Component
public class AdvocatePaymentRowMapper implements RowMapper<AdvocatePayment> {

	@Autowired
	private ObjectMapper objectMapper;

	@Autowired
	PropertiesManager propertiesManager;

	@Override
	public AdvocatePayment mapRow(ResultSet rs, int rowNum) throws SQLException {
		AuditDetails auditDetails = new AuditDetails();
		AdvocatePayment advocatePayment = new AdvocatePayment();
		advocatePayment.setCode(rs.getString("code"));
		advocatePayment.setDemandDate((Long) rs.getObject("demanddate"));
		advocatePayment.setYear(rs.getString("year"));
		advocatePayment.setAmountClaimed(rs.getDouble("amountclaimed"));
		advocatePayment.setAmountRecived(rs.getDouble("amountrecived"));
		advocatePayment.setAllowance(rs.getDouble("allowance"));
		advocatePayment.setIsPartialPayment(rs.getBoolean("ispartialpayment"));
		advocatePayment.setTenantId(rs.getString("tenantid"));
		auditDetails.setCreatedBy(rs.getString("createdby"));
		auditDetails.setLastModifiedBy(rs.getString("lastModifiedBy"));
		auditDetails.setCreatedTime(rs.getBigDecimal("createdTime"));
		auditDetails.setLastModifiedTime(rs.getBigDecimal("lastModifiedTime"));
		advocatePayment.setAuditDetails(auditDetails);
		advocatePayment.setResolutionDate(rs.getLong("resolutiondate"));
		advocatePayment.setResolutionNo(rs.getString("resolutionno"));
		advocatePayment.setResolutionRemarks(rs.getString("resolutionremarks"));
		advocatePayment.setModeOfPayment(rs.getString("modeofpayment"));
		advocatePayment.setInstrumentNumber(rs.getString("instrumentnumber"));
		advocatePayment.setInstrumentDate(rs.getLong("instrumentdate"));
		advocatePayment.setStateId(rs.getString("stateid"));
		advocatePayment.setVoucherNo(rs.getString("voucherno"));
		advocatePayment.setVoucherDate(rs.getLong("voucherdate"));

		Advocate advocate = new Advocate();
		CaseType caseType = new CaseType();
		CaseStatus caseStatus = new CaseStatus();
		List<AdvocateCharge> advocateCharges = new ArrayList<AdvocateCharge>();

		TypeReference<Advocate> advocateRefObj = new TypeReference<Advocate>() {
		};

		TypeReference<CaseType> caseTypeRefObj = new TypeReference<CaseType>() {
		};

		TypeReference<CaseStatus> caseStatusRefObj = new TypeReference<CaseStatus>() {
		};

		TypeReference<List<AdvocateCharge>> advocateChargesRefObj = new TypeReference<List<AdvocateCharge>>() {
		};

		try {

			if (rs.getString("invoicedoucment") != null) {
				TypeReference<Document> invoiceDocument = new TypeReference<Document>() {
				};
				Document document = new Document();
				document = objectMapper.readValue(rs.getString("invoicedoucment"), invoiceDocument);
				advocatePayment.setInvoiceDoucment(document);
			}

			if (rs.getString("advocate") != null)
				advocate = objectMapper.readValue(rs.getString("advocate"), advocateRefObj);
			if (rs.getString("casetype") != null)
				caseType = objectMapper.readValue(rs.getString("casetype"), caseTypeRefObj);
			if (rs.getString("casestatus") != null)
				caseStatus = objectMapper.readValue(rs.getString("casestatus"), caseStatusRefObj);
			if (rs.getString("advocatecharges") != null)
				advocateCharges = objectMapper.readValue(rs.getString("advocatecharges"), advocateChargesRefObj);

		} catch (Exception ex) {
			throw new CustomException(propertiesManager.getAdvocateDetailsResponseErrorCode(),
					propertiesManager.getAdvocateDetailsResponseErrorMsg());
		}

		advocatePayment.setAdvocate(advocate);
		advocatePayment.setCaseType(caseType);
		advocatePayment.setCaseStatus(caseStatus);
		advocatePayment.setAdvocateCharges(advocateCharges);

		return advocatePayment;
	}
}