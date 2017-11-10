package org.egov.lcms.repository.rowmapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.egov.lcms.config.PropertiesManager;
import org.egov.lcms.models.CaseDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;

@Component
public class CaseDetailsRowMapper implements RowMapper<CaseDetails> {
	@Autowired
	ObjectMapper objectMapper;

	@Autowired
	PropertiesManager propertiesManager;

	@Override
	public CaseDetails mapRow(ResultSet rs, int rowNum) throws SQLException {

		CaseDetails caseDetails = new CaseDetails();
		caseDetails.setSummonReferenceNo(getString(rs.getString("summonreferenceno")));
		caseDetails.setCaseNo(getString(rs.getString("caseno")));
		return caseDetails;
	}

	private String getString(Object object) {
		return object == null ? null : object.toString();
	}
}
