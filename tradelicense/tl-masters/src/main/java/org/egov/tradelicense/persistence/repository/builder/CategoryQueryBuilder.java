package org.egov.tradelicense.persistence.repository.builder;

import org.egov.tradelicense.util.ConstantUtility;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;

/**
 * This Class contains INSERT, UPDATE and SELECT queries for Category API's
 * 
 * @author Pavan Kumar Kamma
 */
public class CategoryQueryBuilder {

	private static final String categoryTableName = ConstantUtility.CATEGORY_TABLE_NAME;
	private static final String categoryDetailTableName = ConstantUtility.CATEGORY_DETAIL_TABLE_NAME;

	public static final String INSERT_CATEGORY_QUERY = "INSERT INTO " + categoryTableName
			+ " (tenantId, name, code, active ,parentId, businessNature, validityYears, "
			+ "createdBy, lastModifiedBy, createdTime, lastModifiedTime)"
			+ " VALUES(:tenantId,:name,:code,:active,:parentId,:businessNature,"
			+ ":validityYears,:createdBy,:lastModifiedBy, :createdTime, :lastModifiedTime)";

	public static final String UPDATE_CATEGORY_QUERY = "UPDATE " + categoryTableName
			+ " SET tenantId = :tenantId, name = :name, code = :code, active = :active, parentId = :parentId,"
			+ " businessNature = :businessNature,"
			+ " validityYears = :validityYears , lastModifiedBy = :lastModifiedBy,"
			+ " lastModifiedTime = :lastModifiedTime" + " WHERE id = :id";

	public static final String INSERT_CATEGORY_DETAIL_QUERY = "INSERT INTO " + categoryDetailTableName
			+ " (categoryId, tenantId, feeType, rateType, uomId, createdBy, lastModifiedBy, createdTime, lastModifiedTime)"
			+ " VALUES(:categoryId, :tenantId, :feeType, :rateType, :uomId, :createdBy, :lastModifiedBy, :createdTime, :lastModifiedTime)";

	public static final String UPDATE_CATEGORY_DETAIL_QUERY = "UPDATE " + categoryDetailTableName
			+ " SET categoryId = :categoryId, tenantId = :tenantId, feeType = :feeType, rateType = :rateType,"
			+ " uomId = :uomId," + " lastModifiedBy = :lastModifiedBy, lastModifiedTime = :lastModifiedTime"
			+ " WHERE id = :id";

	public static final String buildCategoryDetailSearchQuery(Long categoryId, Integer pageSize, Integer offSet,
			MapSqlParameterSource parameter) {

		StringBuffer searchSql = new StringBuffer();
		searchSql.append("select cd.*,uom.name as uomName from " + categoryDetailTableName + " cd join "
				+ ConstantUtility.UOM_TABLE_NAME + " uom on( cd.uomId = uom.id) " + " where ");

		if (categoryId != null) {
			searchSql.append(" categoryId = :categoryId ");
			parameter.addValue("categoryId", categoryId);
		}

		if (pageSize != null) {
			searchSql.append(" limit :limit ");
			parameter.addValue("limit", pageSize);
		}

		if (offSet != null) {
			searchSql.append(" offset :offset ");
			parameter.addValue("offset", offSet);
		}

		return searchSql.toString();
	}

	public static String buildSearchQuery(String tenantId, Integer[] ids, String name, String code, String active,
			String type, String businessNature, Integer categoryId, String rateType, String feeType, Integer uomId,
			Integer pageSize, Integer offSet, MapSqlParameterSource parameter) {

		StringBuffer searchSql = new StringBuffer();
		searchSql.append("select * from " + categoryTableName + " where ");
		searchSql.append(" tenantId = :tenantId ");
		parameter.addValue("tenantId", tenantId);

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
			searchSql.append(" AND code = :code ");
			parameter.addValue("code", code.trim());
		}

		if (name != null && !name.isEmpty()) {
			searchSql.append(" AND name = :name ");
			parameter.addValue("name", name.trim());
		}

		if (categoryId != null) {
			searchSql.append(" AND parentId = :parentId ");
			parameter.addValue("parentId", categoryId);
		} else {
			if (type != null && !type.isEmpty() && type.equalsIgnoreCase("SUBCATEGORY")) {
				searchSql.append(" AND parentId IS NOT NULL ");
			} else if (ids == null) {
				searchSql.append(" AND parentId IS NULL ");
			}
		}

		if (businessNature != null && !businessNature.isEmpty()) {
			searchSql.append(" AND businessNature = :businessNature ");
			parameter.addValue("businessNature", businessNature.toUpperCase().trim());
		}

		if (active != null) {

			if (active.equalsIgnoreCase("False")) {
				searchSql.append(" AND active = :active ");
				parameter.addValue("active", false);
			}

			else if (active.equalsIgnoreCase("True")) {
				searchSql.append(" AND active = :active ");
				parameter.addValue("active", true);
			}

		}

		if (rateType != null || feeType != null || uomId != null) {

			StringBuffer subQuery = new StringBuffer();
			subQuery.append(" AND  id in ( SELECT categoryId from " + categoryDetailTableName + " WHERE 1=1   ");

			if (rateType != null) {
				subQuery.append(" AND rateType = :rateType");
				parameter.addValue("rateType", rateType);
			}

			if (feeType != null) {
				subQuery.append(" AND feeType = :feeType");
				parameter.addValue("feeType", feeType);
			}

			if (uomId != null) {
				subQuery.append(" AND uomId = :uomId");
				parameter.addValue("uomId", uomId);
			}
			subQuery.append(")");
			searchSql.append(subQuery);

		}

		if (pageSize != null) {
			searchSql.append(" limit :limit ");
			parameter.addValue("limit", pageSize);
		}

		if (offSet != null) {
			searchSql.append(" offset :offset ");
			parameter.addValue("offset", offSet);
		}

		return searchSql.toString();
	}

	public static String getQueryForParentName(Long parentId, MapSqlParameterSource parameters) {

		parameters.addValue("parentId", parentId);

		return "SELECT name FROM " + ConstantUtility.CATEGORY_TABLE_NAME + " WHERE id = :parentId";

	}

	public static String getQueryCategoryValidation(Long id, Long parentId, String tenantId,
			MapSqlParameterSource parameters) {

		StringBuilder sqlBuilder = new StringBuilder();
		sqlBuilder.append("SELECT count(*) FROM " + categoryTableName);
		sqlBuilder.append(" WHERE tenantId = :tenantId");
		parameters.addValue("tenantId", tenantId);

		if (id != null) {
			sqlBuilder.append(" AND id = :id");
			parameters.addValue("id", id);
		}

		if (parentId != null) {
			sqlBuilder.append(" AND parentId = :parentId");
			parameters.addValue("parentId", parentId);
		}

		return sqlBuilder.toString();
	}

}