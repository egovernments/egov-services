package org.egov.property.repository.builder;

public class PropertyDetailBuilder {

	public static final String INSERT_PROPERTYDETAILS_QUERY = "INSERT INTO egpt_propertydetails ("
			+ "source, regdDocNo, regdDocDate,reason, status, isVerified,"
			+ "verificationDate, isExempted, exemptionReason, propertyType, category, usage,"
			+ "department, apartment, siteLength, siteBreadth, sitalArea,"
			+ "totalBuiltupArea, undividedShare, noOfFloors, isSuperStructure, landOwner, floorType,"
			+ "woodType, roofType, wallType, stateId, applicationNo, createdBy, lastModifiedBy, "
			+ "createdTime, lastModifiedTime, property, taxCalculations,factors,assessmentDates,builderDetails,bpaNo,bpaDate,subUsage)"
			+ "VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";

	public static final String PROPERTY_DETAIL_BY_PROPERTY_QUERY = " select * from egpt_propertydetails where property = ?";

	public static String updatePropertyDetailQuery() {

		StringBuffer propertyDetailUpdateSQL = new StringBuffer();

		propertyDetailUpdateSQL.append("UPDATE egpt_propertydetails")
				.append(" SET source = ?, regdDocNo = ?, regdDocDate = ?, reason = ?,")
				.append(" status = ?, isVerified = ?, verificationDate = ?, isExempted = ?,")
				.append(" exemptionReason = ?, propertyType = ?, category = ?, usage = ?, department = ?,")
				.append(" apartment = ?, siteLength = ?, siteBreadth = ?, sitalArea = ?, totalBuiltupArea = ?,")
				.append(" undividedShare = ?, noOfFloors = ?, isSuperStructure = ?, landOwner = ?,")
				.append(" floorType = ?, woodType = ?, roofType = ?, wallType = ?, stateId = ?,")
				.append(" applicationNo = ?, lastModifiedBy = ?, lastmodifiedtime = ?, property= ?, taxCalculations = ?,")
				.append(" factors = ?, assessmentDates = ?,builderDetails = ?,bpaNo= ?,bpaDate= ?,subUsage=? WHERE id = ?");

		return propertyDetailUpdateSQL.toString();
	}

	public static final String AUDIT_DETAILS_QUERY = " select createdBy,lastModifiedBy,createdTime,lastModifiedTime from egpt_propertydetails where id= ?";

	public static final String UPDATE_TITLETRANSFERPROPERTYDETAIL_QUERY = "UPDATE egpt_propertydetails"
			+ " SET stateId=?, lastModifiedBy = ?, lastmodifiedtime = ? WHERE id=?";

}
