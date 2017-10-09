package org.egov.tradelicense.persistence.repository.builder;

import org.egov.tl.masters.persistence.entity.FeeMatrixSearchEntity;
import org.egov.tradelicense.util.ConstantUtility;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;

/**
 * This Class contains INSERT, UPDATE and SELECT queries for FeeMatrix API's
 * 
 * @author Pavan Kumar Kamma
 */
public class FeeMatrixQueryBuilder {

	private static final String feeMatrixTableName = ConstantUtility.FEE_MATRIX_TABLE_NAME;
	private static final String feeMatrixDetailTableName = ConstantUtility.FEE_MATRIX_DETAIL_TABLE_NAME;

	public static final String INSERT_FEE_MATRIX_QUERY = "INSERT INTO " + feeMatrixTableName
			+ " (tenantId, applicationType, categoryId, businessNature, subCategoryId, financialYear,"
			+ " effectiveFrom, effectiveTo, createdBy, lastModifiedBy, createdTime, lastModifiedTime)"
			+ " VALUES(:tenantId, :applicationType, :categoryId, :businessNature, :subCategoryId, :financialYear,"
			+ " :effectiveFrom, :effectiveTo, :createdBy, :lastModifiedBy, :createdTime, :lastModifiedTime)";

	public static final String UPDATE_FEE_MATRIX_QUERY = "UPDATE " + feeMatrixTableName
			+ " SET tenantId = :tenantId, applicationType = :applicationType,"
			+ " categoryId = :categoryId, businessNature = :businessNature,"
			+ " subCategoryId = :subCategoryId, financialYear = :financialYear, effectiveFrom = :effectiveFrom,"
			+ " effectiveTo = :effectiveTo, lastModifiedBy = :lastModifiedBy, lastModifiedTime = :lastModifiedTime"
			+ " WHERE id = :id";

	public static final String INSERT_FEE_MATRIX_DETAIL_QUERY = "INSERT INTO " + feeMatrixDetailTableName
			+ " (feeMatrixId, tenantId, uomFrom, uomTo, amount, createdBy, lastModifiedBy, createdTime, lastModifiedTime)"
			+ " VALUES(:feeMatrixId, :tenantId, :uomFrom, :uomTo, :amount, :createdBy, :lastModifiedBy, :createdTime, :lastModifiedTime)";

	public static final String UPDATE_FEE_MATRIX_DETAIL_QUERY = "UPDATE " + feeMatrixDetailTableName
			+ " SET feeMatrixId = :feeMatrixId, tenantId = :tenantId, uomFrom = :uomFrom, uomTo = :uomTo, amount = :amount,"
			+ " lastModifiedBy = :lastModifiedBy, lastModifiedTime = :lastModifiedTime" + " WHERE id = :id";

	public static final String buildFeeMatrixDetailSearchQuery(Long feeMatrixId, MapSqlParameterSource parameters) {

		StringBuffer searchSql = new StringBuffer();
		searchSql.append("select * from " + feeMatrixDetailTableName + " where ");
		if (feeMatrixId != null) {
			searchSql.append(" feeMatrixId = :feeMatrixId ORDER BY uomfrom ASC");
			parameters.addValue("feeMatrixId", feeMatrixId);
		}

		return searchSql.toString();
	}

	public static String buildSearchQuery(FeeMatrixSearchEntity searchEntity, MapSqlParameterSource parameters) {

		StringBuffer searchSql = new StringBuffer();
		searchSql.append("select * from " + feeMatrixTableName + " where ");
		searchSql.append(" tenantId = :tenantId ");
		parameters.addValue("tenantId", searchEntity.getTenantId());

		if (searchEntity.getIds() != null && searchEntity.getIds().length > 0) {

			String searchIds = "";
			int count = 1;
			for (Integer id : searchEntity.getIds()) {

				if (count < searchEntity.getIds().length)
					searchIds = searchIds + id + ",";
				else
					searchIds = searchIds + id;

				count++;
			}
			searchSql.append(" AND id IN (" + searchIds + ") ");
		}

		if (searchEntity.getCategory() != null) {
			searchSql.append(" AND category = :category ");
			parameters.addValue("category", searchEntity.getCategory());
		}

		if (searchEntity.getSubCategory() != null) {
			searchSql.append(" AND subCategory = :subCategory ");
			parameters.addValue("subCategory", searchEntity.getSubCategory());
		}

		if (searchEntity.getFinancialYear() != null && !searchEntity.getFinancialYear().isEmpty()) {
			searchSql.append(" AND financialYear = :financialYear ");
			parameters.addValue("financialYear", searchEntity.getFinancialYear());
		}

		if (searchEntity.getApplicationType() != null && !searchEntity.getApplicationType().isEmpty()) {
			searchSql.append(" AND lower(applicationType) = :applicationType ");
			parameters.addValue("applicationType", searchEntity.getApplicationType().toLowerCase());
		}

		if (searchEntity.getBusinessNature() != null && !searchEntity.getBusinessNature().isEmpty()) {
			searchSql.append(" AND lower(businessNature) = :businessNature");
			parameters.addValue("businessNature", searchEntity.getBusinessNature().toLowerCase());
		}

		if (searchEntity.getFeeType() != null && !searchEntity.getFeeType().isEmpty()) {
			searchSql.append(" AND lower(feeType) = :feeType");
			parameters.addValue("feeType", searchEntity.getFeeType().toLowerCase());
		}

		if (searchEntity.getPageSize() != null) {
			searchSql.append(" limit :limit ");
			parameters.addValue("limit", searchEntity.getPageSize());
		}

		if (searchEntity.getOffSet() != null) {
			searchSql.append(" offset :offSet ");
			parameters.addValue("offSet", searchEntity.getOffSet());
		}

		return searchSql.toString();
	}

	public static String getQueryForIdValidation(Long id, String tenantId, MapSqlParameterSource parameters) {

		StringBuffer sql = new StringBuffer();
		sql.append("SELECT count(*) FROM " + feeMatrixTableName + " WHERE");

		if (id != null) {
			sql.append(" id = :id");
			parameters.addValue("id", id);
		}
		if (tenantId != null) {
			sql.append(" AND tenantId = :tenantId");
			parameters.addValue("tenantId", tenantId);
		}

		return sql.toString();
	}

	public static String getQueryForUniquenessValidation(String tenantId, String applicationType, String feeType,
			String businessNature, String category, String subCategory, String financialYear,
			MapSqlParameterSource parameters) {

		StringBuffer sql = new StringBuffer();

		sql.append("SELECT count(*) FROM " + feeMatrixTableName);
		sql.append(" WHERE tenantid=:tenantId AND feetype =:feeType AND categoryid =:categoryId");
		sql.append(" AND subcategoryid =:subcategoryId AND financialyear=:financialYear");
		parameters.addValue("tenantId", tenantId);
		parameters.addValue("feeType", feeType);
		parameters.addValue("category", category);
		parameters.addValue("subcategory", subCategory);
		parameters.addValue("financialYear", financialYear);

		if (applicationType != null) {
			sql.append(" AND applicationtype =:applicationType");
			parameters.addValue("applicationType", applicationType);
		}
		if (businessNature != null) {
			sql.append(" AND businessnature =:businessNature");
			parameters.addValue("businessNature", businessNature);
		}
		/*
		 * + " AND businessnature = :businessNature" +
		 * " AND feetype = : feeType" + " AND categoryid = :categoryId" +
		 * " AND subcategoryid = :subcategoryId");
		 */

		return sql.toString();
	}

	public static String getQueryForFeeMatrixUomValidation() {
		StringBuffer sql = new StringBuffer();
		sql.append("select count(*) FROM " + feeMatrixDetailTableName + " WHERE uomfrom = :uomFrom"
				+ " AND uomto = :uomTo");

		return sql.toString();
	}

	public static String getNextFeematrixBasedOnEffectiveFrom(FeeMatrixSearchEntity domain) {
		StringBuffer sql = new StringBuffer();

		sql.append("SELECT * FROM " + feeMatrixTableName
				+ " WHERE tenantid=:tenantId AND feetype =:feeType AND category =:category AND subcategory =:subcategory");
		if (domain.getApplicationType() != null) {
			sql.append(" AND lower(applicationtype) = :applicationType");
		}
		if (domain.getBusinessNature() != null) {
			sql.append(" AND lower(businessnature) = :businessNature");
		}
		sql.append(" And effectivefrom>:effectiveFrom order by effectivefrom asc ");
		return sql.toString();
	}

	public static String getPreviousFeematrixBasedOnEffictiveFrom(FeeMatrixSearchEntity domain) {
		StringBuffer sql = new StringBuffer();

		sql.append("SELECT * FROM " + feeMatrixTableName
				+ " WHERE  tenantid=:tenantId AND feetype =:feeType AND category = :category AND subcategory =:subcategory");
		if (domain.getApplicationType() != null) {
			sql.append(" AND applicationtype = :applicationType");
		}
		if (domain.getBusinessNature() != null) {
			sql.append(" AND businessnature = :businessNature");
		}
		sql.append(" And effectivefrom<:effectiveFrom order by effectivefrom desc ");
		return sql.toString();
	}
	
	public static final String buildFeeMatrixSearchQuery(Long id, String tenantId, MapSqlParameterSource parameters) {

		StringBuffer searchSql = new StringBuffer();
		searchSql.append("select * from " + feeMatrixTableName + " where id = :id AND tenantid = :tenantId");
		
		parameters.addValue("id", id);
		parameters.addValue("tenantId", tenantId);

		return searchSql.toString();
	}
	
	public static String getDeleteFeeMatrixDetaisWithIdQuery() {
		StringBuffer sql = new StringBuffer();
		sql.append("DELETE FROM " + feeMatrixDetailTableName + " WHERE id=:id");
		return sql.toString();
	}
}