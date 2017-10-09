package org.egov.tradelicense.persistence.repository.builder;

import org.egov.tradelicense.util.ConstantUtility;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;

/**
 * This Class contains INSERT, UPDATE and SELECT queries for UOM API's
 * 
 * @author Pavan Kumar Kamma
 */
public class UomQueryBuilder {

	private static final String uomTableName = ConstantUtility.UOM_TABLE_NAME;

	public static final String INSERT_UOM_QUERY = "INSERT INTO " + uomTableName
			+ " (tenantId, code, name, active, createdBy, lastModifiedBy, createdTime, lastModifiedTime)"
			+ " VALUES(:tenantId, :code, :name, :active, :createdBy, :lastModifiedBy, :createdTime, :lastModifiedTime)";

	public static final String UPDATE_UOM_QUERY = "UPDATE " + uomTableName
			+ " SET tenantId = :tenantId, code = :code, name = :name, active"
			+ " = :active," + " lastModifiedBy = :lastModifiedBy, lastModifiedTime = :lastModifiedTime"
			+ " WHERE id = :id";

	public static String buildSearchQuery(String tenantId, Integer[] ids, String name, String[] codes, String active,
			Integer pageSize, Integer offSet, MapSqlParameterSource parameters) {

		StringBuffer searchSql = new StringBuffer();
		searchSql.append("select * from " + uomTableName + " where ");
		searchSql.append(" tenantId = :tenantId ");
		parameters.addValue("tenantId", tenantId);

		if (ids != null && ids.length > 0) {

			String searchIds = "";
			int count = 1;
			for (Integer id : ids) {

				if (count < ids.length)
					searchIds = searchIds + id + ",";
				else
					searchIds = searchIds + id;

				count++;
			}
			searchSql.append(" AND id IN (" + searchIds + ") ");
		}

		if (codes != null && codes.length > 0) {

			String searchCodes = "";
			int count = 1;
			for (String code : codes) {

				if (count < codes.length)
					searchCodes = searchCodes + "'" + code.toUpperCase() + "',";
				else
					searchCodes = searchCodes + "'"+code.toUpperCase()+"'";

				count++;
			}
			searchSql.append(" AND upper(code) IN (" + searchCodes + ") ");
		}

		if (name != null && !name.isEmpty()) {
			searchSql.append(" AND name = :name ");
			parameters.addValue("name", name);
		}

		if (active != null) {
			if (active.equalsIgnoreCase("False")) {
				searchSql.append(" AND active = :active ");
				parameters.addValue("active", false);
			} else if (active.equalsIgnoreCase("True")) {
				searchSql.append(" AND active = :active ");
				parameters.addValue("active", true);
			}
		}

		if (pageSize != null) {
			searchSql.append(" limit :limit ");
			parameters.addValue("limit", pageSize);
		}

		if (offSet != null) {
			searchSql.append(" offset :offset ");
			parameters.addValue("offset", offSet);
		}

		return searchSql.toString();
	}
}