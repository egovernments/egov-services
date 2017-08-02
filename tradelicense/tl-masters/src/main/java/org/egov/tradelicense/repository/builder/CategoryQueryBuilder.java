package org.egov.tradelicense.repository.builder;

import java.util.List;

import org.egov.tradelicense.config.PropertiesManager;
import org.egov.tradelicense.utility.ConstantUtility;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * This Class contains INSERT, UPDATE and SELECT queries for Category API's
 * 
 * @author Pavan Kumar Kamma
 */
public class CategoryQueryBuilder {

	@Autowired
	private static PropertiesManager propertiesManager;

	private static final String categoryTableName = ConstantUtility.CATEGORY_TABLE_NAME;
	private static final String categoryDetailTableName = ConstantUtility.CATEGORY_DETAIL_TABLE_NAME;
	private static final String defaultPageSize = propertiesManager.getDefaultPageSize();
	private static final String defaultOffset = propertiesManager.getDefaultOffset();

	public static final String INSERT_CATEGORY_QUERY = "INSERT INTO " + categoryTableName
			+ " (tenantId, name, code, parentId, businessNature, createdBy, lastModifiedBy, createdTime, lastModifiedTime)"
			+ " VALUES(?,?,?,?,?,?,?,?,?)";

	public static final String UPDATE_CATEGORY_QUERY = "UPDATE " + categoryTableName
			+ " SET tenantId = ?, name = ?, code = ?, parentId = ?, businessNature = ?,"
			+ " lastModifiedBy = ?, lastModifiedTime = ?" + " WHERE id = ?";

	public static final String INSERT_CATEGORY_DETAIL_QUERY = "INSERT INTO " + categoryDetailTableName
			+ " (categoryId, feeType, rateType, uomId)" + " VALUES(?,?,?,?)";

	public static final String UPDATE_CATEGORY_DETAIL_QUERY = "UPDATE " + categoryDetailTableName
			+ " SET categoryId = ?, feeType = ?, rateType = ?," + " uomId = ?" + " WHERE id = ?";

	public static final String buildCategoryDetailSearchQuery(Long categoryId, Integer pageSize, Integer offSet,
			List<Object> preparedStatementValues) {

		StringBuffer searchSql = new StringBuffer();
		searchSql.append("select * from " + categoryDetailTableName + " where ");
		if (categoryId != null) {
			searchSql.append(" categoryId = ? ");
			preparedStatementValues.add(categoryId);
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

	public static String buildSearchQuery(String tenantId, Integer[] ids, String name, String code, String type,
			Integer categoryId, Integer pageSize, Integer offSet, List<Object> preparedStatementValues) {

		StringBuffer searchSql = new StringBuffer();
		searchSql.append("select * from " + categoryTableName + " where ");
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

		if (categoryId != null) {
			searchSql.append(" AND parentId =? ");
			preparedStatementValues.add(categoryId);
		} else {
			if (type != null && !type.isEmpty() && type.equalsIgnoreCase("SUBCATEGORY")) {
				searchSql.append(" AND parentId IS NOT NULL ");
			} else {
				searchSql.append(" AND parentId IS NULL ");
			}
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