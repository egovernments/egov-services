package org.egov.tradelicense.persistence.repository.builder;

import java.util.List;

import org.egov.tradelicense.util.ConstantUtility;

/**
 * This Class contains INSERT, UPDATE and SELECT queries for Category API's
 * 
 * @author Pavan Kumar Kamma
 */
public class CategoryQueryBuilder {

	private static final String categoryTableName = ConstantUtility.CATEGORY_TABLE_NAME;
	private static final String categoryDetailTableName = ConstantUtility.CATEGORY_DETAIL_TABLE_NAME;

	public static final String INSERT_CATEGORY_QUERY = "INSERT INTO " + categoryTableName
			+ " (tenantId, name, code, active ,parentId, businessNature, createdBy, lastModifiedBy, createdTime, lastModifiedTime)"
			+ " VALUES(?,?,?,?,?,?,?,?,?,?)";

	public static final String UPDATE_CATEGORY_QUERY = "UPDATE " + categoryTableName
			+ " SET tenantId = ?, name = ?, code = ?, active = ?, parentId = ?, businessNature = ?,"
			+ " lastModifiedBy = ?, lastModifiedTime = ?" + " WHERE id = ?";

	public static final String INSERT_CATEGORY_DETAIL_QUERY = "INSERT INTO " + categoryDetailTableName
			+ " (categoryId, feeType, rateType, uomId, createdBy, lastModifiedBy, createdTime, lastModifiedTime)"
			+ " VALUES(?,?,?,?,?,?,?,?)";

	public static final String UPDATE_CATEGORY_DETAIL_QUERY = "UPDATE " + categoryDetailTableName
			+ " SET categoryId = ?, feeType = ?, rateType = ?," + " uomId = ?,"
			+ " lastModifiedBy = ?, lastModifiedTime = ?" + " WHERE id = ?";

	public static final String buildCategoryDetailSearchQuery(Long categoryId, Integer pageSize, Integer offSet,
			List<Object> preparedStatementValues) {

		StringBuffer searchSql = new StringBuffer();
		searchSql.append("select * from " + categoryDetailTableName + " where ");

		if (categoryId != null) {
			searchSql.append(" categoryId = ? ");
			preparedStatementValues.add(categoryId);
		}

		if (pageSize != null) {
			searchSql.append(" limit ? ");
			preparedStatementValues.add(pageSize);
		}

		if (offSet != null) {
			searchSql.append(" offset ? ");
			preparedStatementValues.add(offSet);
		}

		return searchSql.toString();
	}

	public static String buildSearchQuery(String tenantId, Integer[] ids, String name, String code, String active,
			String type, String businessNature, Integer categoryId, Integer pageSize, Integer offSet, List<Object> preparedStatementValues) {

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
			} else if (ids == null){
				searchSql.append(" AND parentId IS NULL ");
			}
		}
		
		if (businessNature != null && !businessNature.isEmpty()) {
			searchSql.append(" AND businessNature =? ");
			preparedStatementValues.add(businessNature.toUpperCase());
		}
		

		if (active != null) {

			if (active.equalsIgnoreCase("False")) {
				searchSql.append(" AND active =? ");
				preparedStatementValues.add(false);
			}

			else if (active.equalsIgnoreCase("True")) {
				searchSql.append(" AND active =? ");
				preparedStatementValues.add(true);
			}

		}

		if (pageSize != null) {
			searchSql.append(" limit ? ");
			preparedStatementValues.add(pageSize);
		}

		if (offSet != null) {
			searchSql.append(" offset ? ");
			preparedStatementValues.add(offSet);
		}

		return searchSql.toString();
	}
}