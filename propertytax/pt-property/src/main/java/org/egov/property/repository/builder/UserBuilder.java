package org.egov.property.repository.builder;

public class UserBuilder {

	public static final String INSERT_USER_QUERY = "INSERT INTO egpt_property_owner ("
			+ "property,owner,isPrimaryOwner, isSecondaryOwner,ownerShipPercentage, ownerType,"
			+ "createdBy, lastModifiedBy, createdTime, lastModifiedTime ) VALUES(?,?,?,?,?,?,?,?,?,?) ";

	public static final String PROPERTY_OWNER_BY_PROPERTY_ID_QUERY = "select * from egpt_property_owner where"
			+ " property = ?";

	public static final String PROPERTY_OWNER_BY_USER_ID_QUERY = "select * from egpt_property_owner " + "where user= ?";

	public static String updateOwnerQuery() {

		StringBuffer userUpdateSQL = new StringBuffer();

		userUpdateSQL.append("UPDATE egpt_property_owner").append(" SET property = ?, owner= ?, isPrimaryOwner = ?,")
				.append(" isSecondaryOwner = ?, ownerShipPercentage = ?, ownerType = ?,")
				.append(" lastModifiedBy = ?, lastModifiedTime = ?").append(" WHERE owner= ? and property=?");

		return userUpdateSQL.toString();
	}

	public static final String AUDIT_DETAILS_QUERY = "select createdBy,lastModifiedBy,createdTime,"
			+ "lastModifiedTime from egpt_property_owner where id= ?";

	public static final String DELETE_OWNER = "DELETE from egpt_property_owner where property = ? and owner = ?";

	public static final String MOVE_OWNERS_TO_HISTORY = "WITH moved_rows AS ( DELETE FROM egpt_property_owner WHERE"
			+ " property=? RETURNING *) INSERT INTO egpt_property_owner_history SELECT * FROM moved_rows";

	public static final String DELETE_USER_BY_ID = "delete from egpt_property_owner where property = ? And owner = ?";
}
