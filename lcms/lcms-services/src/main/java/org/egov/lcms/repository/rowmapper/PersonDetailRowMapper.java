package org.egov.lcms.repository.rowmapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.egov.lcms.config.PropertiesManager;
import org.egov.lcms.models.AuditDetails;
import org.egov.lcms.models.PersonDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;

@Component
public class PersonDetailRowMapper implements RowMapper<PersonDetails> {

	@Autowired
	ObjectMapper objectMapper;

	@Autowired
	PropertiesManager propertiesManager;

	@Override
	public PersonDetails mapRow(ResultSet rs, int rowNum) throws SQLException {

		PersonDetails personDetails = new PersonDetails();
		personDetails.setCode(rs.getString("code"));
		personDetails.setName(getString(rs.getString(("name"))));
		personDetails.setAddress(getString(rs.getString("address")));
		personDetails.setAgencyName(getString(rs.getString("agencyname")));
		personDetails.setAgencyCode(getString(rs.getString("agencycode")));
		personDetails.setTitle(getString(rs.getString("title")));
		personDetails.setFirstName(getString(rs.getString("firstname")));
		personDetails.setSecondName(getString(rs.getString("secondname")));
		personDetails.setLastName(getString(rs.getString("lastname")));
		personDetails.setContactNo(getString(rs.getString("contactno")));
		personDetails.setVatTinNo(getString(rs.getString("vattinno")));
		personDetails.setAadhar(getString(rs.getString("aadhar")));
		personDetails.setGender(getString(rs.getString("gender")));
		personDetails.setAge(getString(rs.getString("age")));
		personDetails.setDob(getLong(rs.getLong("dob")));
		personDetails.setMobileNumber(getString(rs.getString("mobilenumber")));
		personDetails.setEmailId(getString(rs.getString("emailid")));
		personDetails.setPan(getString(rs.getString("pan")));
		personDetails.setTenantId(getString(rs.getString("tenantid")));
		
		AuditDetails auditDetails = new AuditDetails();
		auditDetails.setCreatedBy(rs.getString("createdby"));
		auditDetails.setLastModifiedBy(rs.getString("lastmodifiedby"));
		auditDetails.setCreatedTime(rs.getBigDecimal("createdtime"));
		auditDetails.setLastModifiedTime(rs.getBigDecimal("lastmodifiedtime"));
		personDetails.setAuditDetails(auditDetails);

		return personDetails;
	}

	private String getString(Object object) {
		return object == null ? null : object.toString();
	}

	private Long getLong(Object object) {
		return object == null ? null : Long.parseLong(object.toString());
	}
}