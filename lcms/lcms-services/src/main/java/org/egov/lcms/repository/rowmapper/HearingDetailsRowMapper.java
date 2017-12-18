package org.egov.lcms.repository.rowmapper;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.egov.lcms.config.PropertiesManager;
import org.egov.lcms.models.Attender;
import org.egov.lcms.models.AuditDetails;
import org.egov.lcms.models.CaseStatus;
import org.egov.lcms.models.HearingDetails;
import org.egov.tracer.model.CustomException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

@Component
public class HearingDetailsRowMapper implements RowMapper<HearingDetails> {
	@Autowired
	ObjectMapper objectMapper;

	@Autowired
	PropertiesManager propertiesManager;

	@Override
	public HearingDetails mapRow(ResultSet rs, int rowNum) throws SQLException {

		HearingDetails hearingDetails = new HearingDetails();
		hearingDetails.setCode(rs.getString("code"));
		hearingDetails.setCaseJudgeMent(getString(rs.getString("casejudgement")));
		hearingDetails.setCaseFinalDecision(getString(rs.getString("casefinaldecision")));
		hearingDetails.setNextHearingDate(getLong(rs.getLong("nexthearingdate")));
		hearingDetails.setNextHearingTime(getString(rs.getString("nextHearingTime")));
		hearingDetails.setTenantId(getString(rs.getString("tenantid")));
		hearingDetails.setJudgeMentDate(getLong(rs.getLong("judgementdate")));
		hearingDetails.setAdvocateOpinion(getString(rs.getString("advocateopinion")));
		hearingDetails.setFurtherProcessDetails(getString(rs.getString("furtherprocessdetails")));
		hearingDetails.setDarkhasthDueDate(getLong(rs.getLong("darkhasthduedate")));

		AuditDetails auditDetails = new AuditDetails();
		auditDetails.setCreatedBy(rs.getString("createdby"));
		auditDetails.setLastModifiedBy(rs.getString("lastmodifiedby"));
		auditDetails.setCreatedTime(rs.getBigDecimal("createdtime"));
		auditDetails.setLastModifiedTime(rs.getBigDecimal("lastmodifiedtime"));
		hearingDetails.setAuditDetails(auditDetails);

		TypeReference<CaseStatus> caseStatusReference = new TypeReference<CaseStatus>() {
		};
		TypeReference<List<Attender>> attenderReference = new TypeReference<List<Attender>>() {
		};
		try {
			if (rs.getString("casestatus") != null)
				hearingDetails.setCaseStatus(objectMapper.readValue(rs.getString("casestatus"), caseStatusReference));

			if (rs.getString("attendees") != null)
				hearingDetails.setAttendees(objectMapper.readValue(rs.getString("attendees"), attenderReference));

			if (rs.getString("judges") != null)
				hearingDetails.setJudges(objectMapper.readValue(rs.getString("judges"), attenderReference));

		} catch (IOException ex) {
			throw new CustomException(propertiesManager.getHearingDetailsResponseErrorCode(),
					propertiesManager.getHearingDetailsResponseErrorMsg());
		}

		return hearingDetails;
	}

	private String getString(Object object) {
		return object == null ? null : object.toString();
	}

	private Long getLong(Object object) {
		return object == null ? null : Long.parseLong(object.toString());
	}
}
