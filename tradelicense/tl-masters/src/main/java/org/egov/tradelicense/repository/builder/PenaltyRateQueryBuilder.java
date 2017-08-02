package org.egov.tradelicense.repository.builder;

import java.util.List;

import org.egov.tradelicense.config.PropertiesManager;
import org.egov.tradelicense.utility.ConstantUtility;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * This Class contains INSERT, UPDATE and SELECT queries for PenaltyRate API's
 * 
 * @author Pavan Kumar Kamma
 */
public class PenaltyRateQueryBuilder {
	
	@Autowired
	private static PropertiesManager propertiesManager;

	private static final String feeMatrixTableName = ConstantUtility.PENALTY_RATE_TABLE_NAME;
	private static final String defaultPageSize = propertiesManager.getDefaultPageSize();
	private static final String defaultOffset = propertiesManager.getDefaultOffset();

	public static final String INSERT_PENALTY_RATE_QUERY = "INSERT INTO " + feeMatrixTableName
			+ " (tenantId, applicationType, fromRange, toRange, rate, createdBy, lastModifiedBy, createdTime, lastModifiedTime)"
			+ " VALUES(?,?,?,?,?,?,?,?,?)";

	public static final String UPDATE_PENALTY_RATE_QUERY = "UPDATE " + feeMatrixTableName
			+ " SET tenantId = ?, applicationType = ?, fromRange = ?, toRange = ?, rate = ?,"
			+ " lastModifiedBy = ?, lastModifiedTime = ?" + " WHERE id = ?";

	public static String buildSearchQuery(String tenantId, Integer[] ids, String applicationType, Integer pageSize,
			Integer offSet, List<Object> preparedStatementValues) {

		StringBuffer searchSql = new StringBuffer();
		searchSql.append("select * from " + feeMatrixTableName + " where ");
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