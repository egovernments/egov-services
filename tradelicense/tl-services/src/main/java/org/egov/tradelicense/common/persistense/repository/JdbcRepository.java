package org.egov.tradelicense.common.persistense.repository;

import org.egov.tradelicense.persistence.entity.TradeLicenseEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public abstract class JdbcRepository {

	@Autowired
	public JdbcTemplate jdbcTemplate;

	public String getSequence(String seqName) {
		String seqQuery = "select nextval('" + seqName + "')";
		return String.valueOf(jdbcTemplate.queryForObject(seqQuery, Long.class) + 1);
	}

	public TradeLicenseEntity create(TradeLicenseEntity entity) {
		
		final String INSERT_LICENSE_QUERY = "INSERT INTO egtl_license"
		+ "( id, tenantId, applicationType, applicationNumber,"
	    + " licenseNumber, applicationDate, adhaarNumber, mobileNumber , ownerName, fatherSpouseName, emailId,"
	    + " ownerAddress, propertyAssesmentNo, localityId, revenueWardId, tradeAddress, ownerShipType, tradeTitle,"
	    + " tradeType, categoryId, subCategoryId, uomId, quantity, remarks, tradeCommencementDate, agreementDate,"
	    + " agreementNo, isLegacy, active, expiryDate, createdBy, lastModifiedBy, createdTime, lastModifiedTime )"
	    + "  VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
		
		return entity;
	}

}
