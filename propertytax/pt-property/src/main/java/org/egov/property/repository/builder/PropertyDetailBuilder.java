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

	public static final String MOVE_PROPERTY_DETAIL_TO_HISTORY = "WITH moved_rows AS ( DELETE FROM egpt_propertydetails WHERE"
			+ " property=? RETURNING *) INSERT INTO egpt_propertydetails_history SELECT * FROM moved_rows";

	public static final String PROPERTY_DETAIL_ID_BY_PROPERTY_ID = "select id from egpt_propertydetails where property=?";

	public static final String UPDATE_PROPERTYDETAIL_STATUS = "update egpt_propertydetails set status=? where id=?";

	public static final String UPDATE_PROPETY_DETAILS_AFTER_DEMOLITION = "update egpt_propertydetails set propertytype=?"
			+ ",category=?,usage=?,subusage=?,sitalarea=? where id=?";

}
