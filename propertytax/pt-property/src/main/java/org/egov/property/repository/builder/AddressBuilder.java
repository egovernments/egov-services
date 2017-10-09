package org.egov.property.repository.builder;

public class AddressBuilder {

	public static final String INSERT_ADDRESS_QUERY = "INSERT INTO egpt_Address ("
			+ "tenantId, latitude, longitude, addressNumber, addressLine1,"
			+ " addressLine2, landmark, city, pincode, detail,"
			+ "createdBy, lastModifiedBy, createdTime, lastModifiedTime, property, surveyNo, plotNo )"
			+ "VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";

	public static final String ADDRES_BY_PROPERTY_ID_QUERY = "select * from egpt_address where property= ?";

	public static String updatePropertyAddressQuery() {

		StringBuffer addressUpdateSQL = new StringBuffer();

		addressUpdateSQL.append("UPDATE egpt_Address").append(" SET tenantId = ?, latitude = ?, longitude = ?,")
				.append(" addressNumber = ?, addressLine1 = ?, addressLine2 = ?,")
				.append(" landmark = ?, city = ?, pincode = ?, detail = ?, lastModifiedBy = ?,")
				.append(" lastModifiedTime = ?, property= ?, surveyNo =?, plotNo = ?").append(" WHERE id = ?");

		return addressUpdateSQL.toString();
	}

	public static final String AUDIT_DETAILS_FOR_ADDRESS = "select createdBy,lastModifiedBy,createdTime,"
			+ "lastModifiedTime from egpt_address where id= ?";

	public static final String ADDRES_BY_TITLE_TRANSFER_ID_QUERY = "select * from egpt_address where property= ?";

	public static final String MOVE_ADDRESS_TO_HISTORY = "WITH moved_rows AS ( DELETE FROM egpt_address WHERE "
			+ "property=? RETURNING *) INSERT INTO egpt_Address_history SELECT * FROM moved_rows";

}