package org.egov.property.repository.builder;

public class BoundaryBuilder {

	public static final String INSERT_BOUNDARY_QUERY = "INSERT INTO egpt_propertylocation ("
			+ "revenueBoundary, locationBoundary, adminBoundary, northBoundedBy,eastBoundedBy, westBoundedBy, "
			+ "southBoundedBy,createdBy, lastModifiedBy, createdTime,lastModifiedTime,"
			+ "property,guidanceValueBoundary) VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?)";

	public static final String PROPERTY_LOCATION_BY_PROPERTY_QUERY = "select * from egpt_propertylocation "
			+ "where property= ?";

	public static String updateBoundaryQuery() {

		StringBuffer boundaryUpdateSQL = new StringBuffer();

		boundaryUpdateSQL.append("UPDATE egpt_propertylocation")
				.append(" SET revenueBoundary = ?, locationBoundary = ?, adminBoundary = ?,")
				.append(" northBoundedBy = ?, eastBoundedBy = ?, westBoundedBy = ?, southBoundedBy = ?,")
				.append(" lastModifiedBy = ?, lastModifiedTime = ?, property= ?,guidanceValueBoundary=?")
				.append(" WHERE id = ?");

		return boundaryUpdateSQL.toString();
	}

	public static final String AUDIT_DETAILS_QUERY = "select createdBy,lastModifiedBy,createdTime,"
			+ "lastModifiedTime from egpt_propertylocation where id= ?";

	public static final String MOVE_BOUNDARY_TO_HISTORY = "WITH moved_rows AS ( DELETE FROM egpt_propertylocation WHERE"
			+ " property=? RETURNING *) INSERT INTO egpt_propertylocation_history SELECT * FROM moved_rows";

}
