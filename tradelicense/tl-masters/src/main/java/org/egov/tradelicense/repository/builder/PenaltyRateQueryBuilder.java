package org.egov.tradelicense.repository.builder;

import java.util.List;

/**
 * This Class contains INSERT, UPDATE and SELECT queries for PenaltyRate API's
 * 
 * @author Pavan Kumar Kamma
 */
public class PenaltyRateQueryBuilder {

	public static final String INSERT_PENALTY_RATE_QUERY = "INSERT INTO egtl_mstr_penalty_rate"
			+ " (tenantId, applicationType, fromRange, toRange, rate, createdBy, lastModifiedBy, createdTime, lastModifiedTime)"
			+ " VALUES(?,?,?,?,?,?,?,?,?)";

	public static final String UPDATE_PENALTY_RATE_QUERY = "UPDATE egtl_mstr_penalty_rate"
			+ " SET tenantId = ?, applicationType = ?, fromRange = ?, toRange = ?, rate = ?,"
			+ " lastModifiedBy = ?, lastModifiedTime = ?" + " WHERE id = ?";

	public static String buildSearchQuery(String tenantId, Integer[] ids, String applicationType, Integer pageSize,
			Integer offSet, List<Object> preparedStatementValues) {

		StringBuffer searchSql = new StringBuffer();
		searchSql.append("select * from egtl_mstr_penalty_rate where ");
		searchSql.append(" tenantId = ? ");
		preparedStatementValues.add(tenantId);

		if (applicationType != null && !applicationType.isEmpty()) {
			searchSql.append(" AND applicationType = ? ");
			preparedStatementValues.add(applicationType);
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