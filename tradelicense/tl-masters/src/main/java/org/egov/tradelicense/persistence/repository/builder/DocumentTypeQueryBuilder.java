package org.egov.tradelicense.persistence.repository.builder;

import java.util.List;

import org.egov.tradelicense.utility.ConstantUtility;

/**
 * This Class contains INSERT, UPDATE and SELECT queries for DocumentType API's
 * 
 * @author Shubham pratap Singh
 */
public class DocumentTypeQueryBuilder {

	private static final String documentTypeTableName = ConstantUtility.DOCUMENT_TYPE_TABLE_NAME;

	public static final String INSERT_DOCUMENT_TYPE_QUERY = "INSERT INTO " + documentTypeTableName
			+ " (tenantId, name, mandatory, enabled, applicationType, createdBy, lastModifiedBy, createdTime, lastModifiedTime)"
			+ " VALUES(?,?,?,?,?,?,?,?,?)";

	public static final String UPDATE_DOCUMENT_TYPE_QUERY = "UPDATE " + documentTypeTableName
			+ " SET tenantId = ?, name = ?, mandatory = ?, enabled = ?, applicationType = ?,"
			+ " lastModifiedBy = ?, lastModifiedTime = ?" + " WHERE id = ?";

	public static String buildSearchQuery(String tenantId, Integer[] ids, String name, String enabled,
			String applicationType, Integer pageSize, Integer offSet, List<Object> preparedStatementValues) {

		StringBuffer searchSql = new StringBuffer();
		searchSql.append("select * from " + documentTypeTableName + " where ");
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

		if (name != null && !name.isEmpty()) {
			searchSql.append(" AND name =? ");
			preparedStatementValues.add(name);
		}

		if (enabled != null) {
			if (enabled.equalsIgnoreCase("False")) {
				searchSql.append(" AND enabled =? ");
				preparedStatementValues.add(false);
			} else if (enabled.equalsIgnoreCase("True")) {
				searchSql.append(" AND enabled =? ");
				preparedStatementValues.add(true);
			}
		}

		if (applicationType != null && !applicationType.isEmpty()) {
			searchSql.append(" AND applicationType =? ");
			preparedStatementValues.add(applicationType);
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