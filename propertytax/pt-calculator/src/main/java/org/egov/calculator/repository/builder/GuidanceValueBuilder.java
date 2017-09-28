package org.egov.calculator.repository.builder;

import java.util.List;

import org.egov.calculator.utility.ConstantUtility;

/**
 * 
 * @author Anil Kumar S This Class will have all the queries which are used in
 *         the guidancevalue API's
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

	public static final String BASE_SEARCH_QUERY = "SELECT * FROM " + ConstantUtility.GUIDANCEVALUE_TABLE_NAME
			+ " WHERE tenantId =? "
			+ "AND boundary=? AND to_date(?,'dd/MM/yyyy') BETWEEN fromdate::date AND todate::date";

	public static String getGuidanceValueSearchQuery(String tenantId, String boundary, String structure, String usage,
			String subUsage, String occupancy, String validDate, List<Object> preparedStatementValues) {

		StringBuffer searchSql = new StringBuffer();

		searchSql.append(BASE_SEARCH_QUERY);
		preparedStatementValues.add(tenantId);
		preparedStatementValues.add(boundary);
		preparedStatementValues.add(validDate);

		if (structure != null) {
			if (!structure.isEmpty()) {
				searchSql.append(" AND structure=?");
				preparedStatementValues.add(structure);
			}
		}

		if (usage != null) {
			if (!usage.isEmpty()) {
				searchSql.append(" AND usage=?");
				preparedStatementValues.add(usage);
			}
		}

		if (subUsage != null) {
			if (!subUsage.isEmpty()) {
				searchSql.append(" AND subusage=?");
				preparedStatementValues.add(subUsage);
			}
		}

		if (occupancy != null) {
			if (!occupancy.isEmpty()) {
				searchSql.append(" AND occupancy=?");
				preparedStatementValues.add(occupancy);
			}
		}

		return searchSql.toString();

	}
}
