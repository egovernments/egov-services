package org.egov.calculator.repository.builder;

/**
 * 
 * @author Anil Kumar S 
 * This Class will have all the queries which are used in the guidancevalue API's
 *
 */
public class GuidanceValueBuilder {

	public static final String INSERT_GUIDANCEVALUE_QUERY = "INSERT INTO egpt_mstr_guidancevalue ( "
			+ "tenantid, name, boundary, structure, usage, subUsage, occupancy,"
			+ " value, fromdate, todate, createdby, lastmodifiedby, createdtime, "
			+ "lastmodifiedtime ) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?)";

	public static final String UPDATE_GUIDANCEVALUE_QUERY = "UPDATE egpt_mstr_guidancevalue SET tenantid = ?, "
			+ "name = ?, boundary = ?, structure = ?, usage = ?, subUsage = ?, occupancy = ?,"
			+ "value = ?, fromdate = ?, todate = ?,  createdby = ?, lastmodifiedby = ?, createdtime = ?,"
			+ " lastmodifiedtime = ? WHERE id = ? ";

	public static String getGuidanceValueSearchQuery(String tenantId,
			String boundary, String structure, String usage, String subUsage,
			String occupancy, String validDate, String code) {

		StringBuffer searchSql = new StringBuffer();
		searchSql
				.append("SELECT * FROM egpt_mstr_guidancevalue WHERE tenantId = '"
						+ tenantId + "'" + "AND boundary = '" + boundary
						+ "'  OR code = " + code + "AND (fromdate <" + validDate
						+ "< todate) OR structure = '" + structure
						+ "' OR usage = '" + usage + "' " + "OR subusage = '"
						+ subUsage + "' OR occupancy = '" + occupancy + "'");

		return searchSql.toString();

	}

}
