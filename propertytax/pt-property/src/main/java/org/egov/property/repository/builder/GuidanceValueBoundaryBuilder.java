package org.egov.property.repository.builder;

import java.util.List;

import org.egov.property.utility.ConstantUtility;

public class GuidanceValueBoundaryBuilder {
	public static final String INSERT_GUIDANCEBOUNDARYVALUE_QUERY = "insert into "
			+ ConstantUtility.GUIDANCEVALUEBOUNDARY_TABLE_NAME
			+ " (tenantId,guidancevalueboundary1,guidancevalueboundary2,createdby,lastmodifiedby,createdtime,"
			+ "lastmodifiedtime) VALUES (?,?,?,?,?,?,?)";

	public static final String UPDATE_GUIDANCEBOUNDARYVALUE_QUERY = "UPDATE "
			+ ConstantUtility.GUIDANCEVALUEBOUNDARY_TABLE_NAME
			+ " SET tenantId=?,guidancevalueboundary1=?,guidancevalueboundary2=?,lastmodifiedby=?,lastmodifiedtime=? WHERE id = ?";

	public static String getGuidanceValueBoundarySearchQuery(String tableName, String tenantId,
			String guidanceValueBoundary1, String guidanceValueBoundary2, Integer pageSize, Integer offset,
			List<Object> preparedStatementValues) {

		StringBuffer searchSql = new StringBuffer();

		searchSql.append("select * from " + tableName + " where ");

		searchSql.append("LOWER(tenantId) =LOWER(?) ");
		preparedStatementValues.add(tenantId);

		searchSql.append(" AND LOWER(guidancevalueboundary1) = LOWER(?) ");
		preparedStatementValues.add(guidanceValueBoundary1);

		if (guidanceValueBoundary2 != null && !guidanceValueBoundary2.isEmpty()) {
			searchSql.append(" AND LOWER(guidancevalueboundary2)=LOWER(?)");
			preparedStatementValues.add(guidanceValueBoundary2);
		}

		searchSql.append(" limit ? ");
		preparedStatementValues.add(pageSize);

		searchSql.append(" offset ? ");
		preparedStatementValues.add(offset);

		return searchSql.toString();

	}
}
