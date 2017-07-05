package org.egov.property.repository.builder;

public class PropertyBuilder {

	public static final String AUDIT_DETAILS_FOR_PROPERTY_DETAILS = "select createdBy,lastModifiedBy,createdTime,"
			+ "lastModifiedTime from egpt_propertydetails where property= ?";

	public static final String INSERT_PROPERTY_QUERY = "INSERT INTO egpt_property ("
			+ "tenantId, upicNumber, oldUpicNumber, vltUpicNumber,creationReason, assessmentDate,"
			+ " occupancyDate, gisRefNo,isAuthorised, isUnderWorkflow, channel,"
			+ " createdBy,lastModifiedBy, createdTime,lastModifiedTime,demands)" + "VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";

	public static String updatePropertyQuery() {

		StringBuffer propertyUpdateSQL = new StringBuffer();

		propertyUpdateSQL.append("UPDATE egpt_Property")
				.append(" SET tenantId = ? , upicNumber = ?, oldUpicNumber = ?, vltUpicNumber = ?,")
				.append("creationReason = ?, assessmentDate = ?, occupancyDate = ?, gisRefNo = ?,")
				.append(" isAuthorised = ?, isUnderWorkflow = ?, channel = ?,")
				.append(" lastModifiedBy = ?, lastModifiedTime = ?,demands = ?").append(" WHERE id = ? ");

		return propertyUpdateSQL.toString();
	}

}
