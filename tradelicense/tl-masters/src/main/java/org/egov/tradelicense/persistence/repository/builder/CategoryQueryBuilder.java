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
			+ " (tenantId, name, code, active ,parent, businessNature, validityYears, "
			+ "createdBy, lastModifiedBy, createdTime, lastModifiedTime)"
			+ " VALUES(:tenantId,:name,:code,:active,:parent,:businessNature,"
			+ ":validityYears,:createdBy,:lastModifiedBy, :createdTime, :lastModifiedTime)";

	public static final String UPDATE_CATEGORY_QUERY = "UPDATE " + categoryTableName
			+ " SET tenantId = :tenantId, name = :name, code = :code, active = :active, parent = :parent,"
			+ " businessNature = :businessNature,"
			+ " validityYears = :validityYears , lastModifiedBy = :lastModifiedBy,"
			+ " lastModifiedTime = :lastModifiedTime" + " WHERE id = :id";

	public static final String INSERT_CATEGORY_DETAIL_QUERY = "INSERT INTO " + categoryDetailTableName
			+ " (category, tenantId, feeType, rateType, uom, createdBy, lastModifiedBy, createdTime, lastModifiedTime)"
			+ " VALUES(:category, :tenantId, :feeType, :rateType, :uom, :createdBy, :lastModifiedBy, :createdTime, :lastModifiedTime)";

	public static final String UPDATE_CATEGORY_DETAIL_QUERY = "UPDATE " + categoryDetailTableName
			+ " SET category = :category, tenantId = :tenantId, feeType = :feeType, rateType = :rateType,"
			+ " uom = :uom," + " lastModifiedBy = :lastModifiedBy, lastModifiedTime = :lastModifiedTime"
			+ " WHERE id = :id";

	public static final String buildCategoryDetailSearchQuery(String category, Integer pageSize, Integer offSet,
			MapSqlParameterSource parameter) {

		StringBuffer searchSql = new StringBuffer();
		searchSql.append("select cd.*,uom.name as uomName from " + categoryDetailTableName + " cd join "
				+ ConstantUtility.UOM_TABLE_NAME + " uom on( cd.uom = uom.code and cd.tenantId=uom.tenantId) " + " where ");

		if (category != null) {
			searchSql.append(" category = :category ");
			parameter.addValue("category", category);
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

	public static String buildSearchQuery(String tenantId, Integer[] ids, String[] codes, String name, String active,
			String type, String businessNature, String category, String rateType, String feeType, String uom,
			Integer pageSize, Integer offSet, MapSqlParameterSource parameter) {

		StringBuffer searchSql = new StringBuffer();
		searchSql.append("select * from " + categoryTableName + " where ");
		searchSql.append(" tenantId = :tenantId ");
		parameter.addValue("tenantId", tenantId);

		if (codes != null && codes.length > 0) {

			String searchCodes = "";
			int count = 1;
			for (String code : codes) {

				if (count < codes.length)
					searchCodes = searchCodes + "'" + code.trim().toUpperCase() + "',";
				else
					searchCodes = searchCodes +"'" +code.trim().toUpperCase() +"'";

				count++;
			}
			searchSql.append(" AND upper(code) IN (" + searchCodes + ") ");
		}
		
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


		if (name != null && !name.isEmpty()) {
			searchSql.append(" AND name = :name ");
			parameter.addValue("name", name.trim());
		}

		if (category != null && !category.isEmpty()) {
			searchSql.append(" AND parent = :parent ");
			parameter.addValue("parent", category);
		} else {
			if (type != null && !type.isEmpty() && type.equalsIgnoreCase("SUBCATEGORY")) {
				searchSql.append(" AND parent IS NOT NULL ");
			} else if (ids == null) {
				searchSql.append(" AND parent IS NULL ");
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

		if (rateType != null || feeType != null || uom != null) {

			StringBuffer subQuery = new StringBuffer();
			subQuery.append(" AND  code IN ( SELECT category from " + categoryDetailTableName + " WHERE tenantId ='"+tenantId+"'");

			if (rateType != null) {
				subQuery.append(" AND rateType = :rateType");
				parameter.addValue("rateType", rateType);
			}

			if (feeType != null) {
				subQuery.append(" AND feeType = :feeType");
				parameter.addValue("feeType", feeType);
			}

			if (uom != null) {
				subQuery.append(" AND uom = :uom");
				parameter.addValue("uom", uom);
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

	public static String getQueryForParentName(String parent, MapSqlParameterSource parameters) {

		parameters.addValue("parent", parent);

		return "SELECT name FROM " + ConstantUtility.CATEGORY_TABLE_NAME + " WHERE code = :parent";

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
	
	public static String getQueryCategoryValidationWithCode(String code, String parent, String tenantId,
			MapSqlParameterSource parameters) {

		StringBuilder sqlBuilder = new StringBuilder();
		sqlBuilder.append("SELECT count(*) FROM " + categoryTableName);
		sqlBuilder.append(" WHERE tenantId = :tenantId");
		parameters.addValue("tenantId", tenantId);

		if (code != null) {
			sqlBuilder.append(" AND code = :code");
			parameters.addValue("code", code);
		}

		if (parent != null) {
			sqlBuilder.append(" AND parent = :parent");
			parameters.addValue("parent", parent);
		}

		return sqlBuilder.toString();
	}

}