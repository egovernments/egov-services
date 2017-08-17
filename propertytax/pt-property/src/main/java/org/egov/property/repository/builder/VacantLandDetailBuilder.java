package org.egov.property.repository.builder;

public class VacantLandDetailBuilder {

	public static final String INSERT_VACANTLANDDETAIL_QUERY = "INSERT INTO egpt_vacantland ("
			+ "surveyNumber,pattaNumber, marketValue,capitalValue,layoutApprovedAuth,layoutPermissionNo, "
			+ "layoutPermissionDate, resdPlotArea,nonResdPlotArea,createdBy, lastModifiedBy, createdTime,"
			+ "lastModifiedTime, property) VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?)";

	public static final String VACANT_LAND_BY_PROPERTY_QUERY = "select * from egpt_vacantland where property = ?";

	public static String updateVacantLandQuery() {
		StringBuffer vacantlandUpdateSQL = new StringBuffer();
		vacantlandUpdateSQL.append("UPDATE egpt_vacantland")
				.append(" SET surveyNumber = ?, pattaNumber = ?, marketValue = ?, capitalValue = ?, layoutApprovedAuth = ?,")
				.append(" layoutPermissionNo = ?, layoutPermissionDate = ?, resdPlotArea = ?, ")
				.append(" nonResdPlotArea = ?, lastModifiedBy = ?, lastModifiedTime = ?, property= ?")
				.append(" WHERE id = ?");

		return vacantlandUpdateSQL.toString();
	}

	public static final String AUDIT_DETAILS_QUERY = "select createdBy,lastModifiedBy,createdTime,"
			+ "lastModifiedTime from egpt_vacantland where id= ?";

}
