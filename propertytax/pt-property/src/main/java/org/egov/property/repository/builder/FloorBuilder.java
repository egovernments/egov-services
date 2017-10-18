package org.egov.property.repository.builder;

public class FloorBuilder {

	public static final String INSERT_FLOOR_QUERY = "INSERT INTO egpt_floors ("
			+ "floorNo,createdBy, lastModifiedBy, createdTime, lastModifiedTime, propertydetails)"
			+ "VALUES(?,?,?,?,?,?)";

	public static final String FLOORS_BY_PROPERTY_DETAILS_QUERY = "select * from egpt_floors where propertydetails= ?";

	public static String updateFloorQuery() {

		StringBuffer floorUpdateSQL = new StringBuffer();

		floorUpdateSQL.append("UPDATE egpt_floors").append(" SET floorNo = ?, lastModifiedBy = ?,")
				.append(" lastModifiedTime = ?, propertydetails = ?").append(" WHERE id = ?");

		return floorUpdateSQL.toString();
	}

	public static final String AUDIT_DETAILS_QUERY = "select createdBy,lastModifiedBy,createdTime,"
			+ "lastModifiedTime from egpt_floors where id= ?";

	public static final String MOVE_FLOORS_TO_HISTORY = "WITH moved_rows AS ( DELETE FROM egpt_floors"
			+ " WHERE propertydetails=? RETURNING *) INSERT INTO egpt_floors_history SELECT * FROM moved_rows";

	public static final String DELETE_FLOORS_BY_ID = "delete from egpt_floors where id= ?";

	public static final String DELETE_UNIT_BY_FLOORID = "delete from egpt_unit where floor= ?";

}
