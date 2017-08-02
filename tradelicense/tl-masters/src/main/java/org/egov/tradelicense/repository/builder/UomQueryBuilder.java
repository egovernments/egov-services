package org.egov.tradelicense.repository.builder;

import java.util.List;

import org.egov.tradelicense.config.PropertiesManager;
import org.egov.tradelicense.utility.ConstantUtility;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * This Class contains INSERT, UPDATE and SELECT queries for UOM API's
 * 
 * @author Pavan Kumar Kamma
 */
public class UomQueryBuilder {
	
	@Autowired
	private static PropertiesManager propertiesManager;

	private static final String uomTableName = ConstantUtility.UOM_TABLE_NAME;
	private static final String defaultPageSize = propertiesManager.getDefaultPageSize();
	private static final String defaultOffset = propertiesManager.getDefaultOffset();

	public static final String INSERT_UOM_QUERY = "INSERT INTO " + uomTableName
			+ " (tenantId, code, name, active, createdBy, lastModifiedBy, createdTime, lastModifiedTime)"
			+ " VALUES(?,?,?,?,?,?,?,?)";

	public static final String UPDATE_UOM_QUERY = "UPDATE " + uomTableName
			+ " SET tenantId = ?, code = ?, name = ?, active = ?," + " lastModifiedBy = ?, lastModifiedTime = ?"
			+ " WHERE id = ?";

	public static String buildSearchQuery(String tenantId, Integer[] ids, String name, String code, Boolean active,
			Integer pageSize, Integer offSet, List<Object> preparedStatementValues) {

		StringBuffer searchSql = new StringBuffer();
		searchSql.append("select * from " + uomTableName + " where ");
		searchSql.append(" tenantId = ? ");
		preparedStatementValues.add(tenantId);

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

		if (code != null && !code.isEmpty()) {
			searchSql.append(" AND code =? ");
			preparedStatementValues.add(code);
		}

		if (name != null && !name.isEmpty()) {
			searchSql.append(" AND name =? ");
			preparedStatementValues.add(name);
		}

		if (active != null) {
			searchSql.append(" AND active =? ");
			preparedStatementValues.add(active);
		}

		if (pageSize == null) {
			pageSize = Integer.valueOf(defaultPageSize);
		}

		searchSql.append(" limit ? ");
		preparedStatementValues.add(pageSize);

		if (offSet == null) {
			offSet = Integer.valueOf(defaultOffset);
		}

		searchSql.append(" offset ? ");
		preparedStatementValues.add(offSet);

		return searchSql.toString();
	}
}