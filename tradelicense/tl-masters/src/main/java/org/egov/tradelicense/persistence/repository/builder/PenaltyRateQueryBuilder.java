package org.egov.tradelicense.persistence.repository.builder;

import org.egov.tradelicense.util.ConstantUtility;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;

/**
 * This Class contains INSERT, UPDATE and SELECT queries for PenaltyRate API's
 * 
 * @author Pavan Kumar Kamma
 */
public class PenaltyRateQueryBuilder {

	private static final String feeMatrixTableName = ConstantUtility.PENALTY_RATE_TABLE_NAME;

	public static final String INSERT_PENALTY_RATE_QUERY = "INSERT INTO " + feeMatrixTableName
			+ " (tenantId, applicationType, fromRange, toRange, rate, createdBy, lastModifiedBy, createdTime, lastModifiedTime)"
			+ " VALUES(:tenantId, :applicationType, :fromRange, :toRange, :rate, :createdBy, :lastModifiedBy, :createdTime, :lastModifiedTime)";

	public static final String UPDATE_PENALTY_RATE_QUERY = "UPDATE " + feeMatrixTableName
			+ " SET tenantId = :tenantId, applicationType = :applicationType, fromRange = :fromRange, toRange = :toRange, rate = :rate,"
			+ " lastModifiedBy = :lastModifiedBy, lastModifiedTime = :lastModifiedTime" + " WHERE id = :id";

	public static String buildSearchQuery(String tenantId, Integer[] ids, String applicationType, Integer pageSize,
			Integer offSet, MapSqlParameterSource parameters) {

		StringBuffer searchSql = new StringBuffer();
		searchSql.append("select * from " + feeMatrixTableName + " where ");
		searchSql.append(" tenantId = :tenantId ");
		parameters.addValue("tenantId", tenantId);

		if (applicationType != null && !applicationType.isEmpty()) {
			
			searchSql.append(" AND applicationType = :applicationType ");
			parameters.addValue("applicationType", applicationType.toUpperCase());
			
		} else {
			
			searchSql.append(" AND applicationType IS NULL ");
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
          searchSql.append(" ORDER BY fromrange");
          
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

	public static String getQueryToDeletePenalty(Long id, MapSqlParameterSource parameters) {
		parameters.addValue("id", id);
		return "DELETE FROM "+ConstantUtility.PENALTY_RATE_TABLE_NAME+" WHERE id = :id";
	}
}