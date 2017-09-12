package org.egov.tradelicense.persistence.repository.builder;

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
			+ " lastModifiedBy = :lastModifiedBy, lastModifiedTime = :lastModifiedTime"
			+ " WHERE id = :id";

	public static final String buildFeeMatrixDetailSearchQuery(Long feeMatrixId, MapSqlParameterSource parameters) {

		StringBuffer searchSql = new StringBuffer();
		searchSql.append("select * from " + feeMatrixDetailTableName + " where ");
		if (feeMatrixId != null) {
			searchSql.append(" feeMatrixId = :feeMatrixId ");
			parameters.addValue("feeMatrixId",feeMatrixId);
		}

		return searchSql.toString();
	}

	public static String buildSearchQuery(String tenantId, Integer[] ids, Integer categoryId, Integer subCategoryId,
			String financialYear, String applicationType, String businessNature, Integer pageSize, Integer offSet,
			MapSqlParameterSource parameters) {

		StringBuffer searchSql = new StringBuffer();
		searchSql.append("select * from " + feeMatrixTableName + " where ");
		searchSql.append(" tenantId = :tenantId ");
		parameters.addValue("tenantId",tenantId);

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

		if (categoryId != null) {
			searchSql.append(" AND categoryId = :categoryId ");
			parameters.addValue("categoryId", categoryId);
		}

		if (subCategoryId != null) {
			searchSql.append(" AND subCategoryId = :subCategoryId ");
			parameters.addValue("subCategoryId",subCategoryId);
		}

		if (financialYear != null && !financialYear.isEmpty()) {
			searchSql.append(" AND financialYear = :financialYear ");
			parameters.addValue("financialYear",financialYear);
		}

		if (applicationType != null && !applicationType.isEmpty()) {
			searchSql.append(" AND applicationType = :applicationType ");
			parameters.addValue("applicationType",applicationType);
		}

		if (businessNature != null && !businessNature.isEmpty()) {
			searchSql.append(" AND businessNature = :businessNature");
			parameters.addValue("businessNature", businessNature);
		}

		if (pageSize != null) {
			searchSql.append(" limit :limit ");
			parameters.addValue("limit",pageSize);
		}

		if (offSet != null) {
			searchSql.append(" offset :offSet ");
			parameters.addValue("offSet",offSet);
		}

		return searchSql.toString();
	}
}