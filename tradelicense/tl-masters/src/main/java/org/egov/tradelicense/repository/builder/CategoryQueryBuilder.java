package org.egov.tradelicense.repository.builder;

import java.util.List;

/**
 * This Class contains INSERT, UPDATE and SELECT queries for Category API's
 * 
 * @author Pavan Kumar Kamma
 */
public class CategoryQueryBuilder {

	public static final String INSERT_CATEGORY_QUERY = "INSERT INTO egtl_mstr_category"
			+ " (tenantId, name, code, parentId, businessNature, createdBy, lastModifiedBy, createdTime, lastModifiedTime)"
			+ " VALUES(?,?,?,?,?,?,?,?,?)";

	public static final String UPDATE_CATEGORY_QUERY = "UPDATE egtl_mstr_category"
			+ " SET tenantId = ?, name = ?, code = ?, parentId = ?, businessNature = ?," + " lastModifiedBy = ?, lastModifiedTime = ?"
			+ " WHERE id = ?";

	public static final String INSERT_CATEGORY_DETAIL_QUERY = "INSERT INTO egtl_category_details"
			+ " (categoryId, feeType, rateType, uomId)" + " VALUES(?,?,?,?)";

	public static final String UPDATE_CATEGORY_DETAIL_QUERY = "UPDATE egtl_category_details"
			+ " SET categoryId = ?, feeType = ?, rateType = ?," + " uomId = ?" + " WHERE id = ?";

	public static final String buildCategoryDetailSearchQuery(Long categoryId, Integer pageSize, Integer offSet,
			List<Object> preparedStatementValues) {

		StringBuffer searchSql = new StringBuffer();
		searchSql.append("select * from egtl_category_details where ");
		if (categoryId != null) {
			searchSql.append(" categoryId = ? ");
			preparedStatementValues.add(categoryId);
		}
		if (pageSize == null)
			pageSize = 30;

		searchSql.append(" limit ? ");
		preparedStatementValues.add(pageSize);

		if (offSet == null)
			offSet = 0;

		searchSql.append(" offset ? ");
		preparedStatementValues.add(offSet);

		return searchSql.toString();
	}

	public static String buildSearchQuery(String tenantId, Integer[] ids, String name, String code, Integer pageSize,
			Integer offSet, List<Object> preparedStatementValues) {

		StringBuffer searchSql = new StringBuffer();
		searchSql.append("select * from egtl_mstr_category where ");
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

		if (pageSize == null)
			pageSize = 30;

		searchSql.append(" limit ? ");
		preparedStatementValues.add(pageSize);

		if (offSet == null)
			offSet = 0;

		searchSql.append(" offset ? ");
		preparedStatementValues.add(offSet);

		return searchSql.toString();
	}
}