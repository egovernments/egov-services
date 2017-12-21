package org.egov.lcms.repository.rowmapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.egov.lcms.config.PropertiesManager;
import org.egov.lcms.models.Agency;
import org.egov.lcms.models.AuditDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;

/** 
* 
* Author		Date			eGov-JIRA ticket	Commit message
* ---------------------------------------------------------------------------
* Veswanth		14th Nov 2107						Initial commit for agency row mapper
* Yosadhara		15th Nov 2107						Added status for agency search
*/
@Component
public class AgencyRowMapper implements RowMapper<Agency> {

	@Autowired
	ObjectMapper objectMapper;

	@Autowired
	PropertiesManager propertiesManager;

	@Override
	public Agency mapRow(ResultSet rs, int rowNum) throws SQLException {

		Agency agency = new Agency();
		agency.setCode(getString(rs.getString("code")));
		agency.setName(getString(rs.getString("name")));
		agency.setAgencyAddress(getString(rs.getString("agencyaddress")));
		agency.setIsIndividual(rs.getBoolean("isindividual"));
		agency.setDateOfEmpanelment(getLong(rs.getLong("dateofempanelment")));
		agency.setStandingCommitteeDecisionDate(getLong(rs.getLong("standingcommitteedecisiondate")));
		agency.setEmpanelmentFromDate(getLong(rs.getLong("empanelmentfromdate")));
		agency.setNewsPaperAdvertismentDate(getLong(rs.getLong("newspaperadvertismentdate")));

		agency.setEmpanelmentToDate(getLong(rs.getLong("empanelmenttodate")));

		agency.setBankName(getString(rs.getString("bankname")));
		agency.setBankBranch(getString(rs.getString("bankbranch")));
		agency.setBankAccountNo(getString(rs.getString("bankaccountno")));

		agency.setIfscCode(getString(rs.getString("ifscCode")));
		agency.setMicr(getString(rs.getString("micr")));
		agency.setIsActive(rs.getBoolean("isactive"));
		agency.setIsTerminate(rs.getBoolean("isterminate"));
		agency.setInActiveDate(getLong(rs.getLong("inactivedate")));
		agency.setTerminationDate(getLong(rs.getLong("terminationdate")));
		agency.setReasonOfTermination(getString(rs.getString("reasonoftermination")));
		agency.setTenantId(getString(rs.getString("tenantid")));
		agency.setStatus(getString(rs.getString("status")));

		AuditDetails auditDetails = new AuditDetails();
		auditDetails.setCreatedBy(rs.getString("createdby"));
		auditDetails.setLastModifiedBy(rs.getString("lastmodifiedby"));
		auditDetails.setCreatedTime(rs.getBigDecimal("createdtime"));
		auditDetails.setLastModifiedTime(rs.getBigDecimal("lastmodifiedtime"));
		agency.setAuditDetails(auditDetails);

		return agency;
	}

	/**
	 * This method will cast the given object to String
	 * 
	 * @param object
	 *            that need to be cast to string
	 * @return {@link String}
	 */
	private String getString(Object object) {
		return object == null ? null : object.toString();
	}

	/**
	 * This method will cast the given object to Long
	 * 
	 * @param object
	 *            that need to be cast to Long
	 * @return {@link Long}
	 */
	private Long getLong(Object object) {
		return object == null ? null : Long.parseLong(object.toString());
	}
}
