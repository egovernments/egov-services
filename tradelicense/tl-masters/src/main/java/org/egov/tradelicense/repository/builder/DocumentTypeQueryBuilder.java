package org.egov.tradelicense.repository.builder;

import java.util.List;

import org.egov.tradelicense.config.PropertiesManager;
import org.egov.tradelicense.utility.ConstantUtility;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * This Class contains INSERT, UPDATE and SELECT queries for DocumentType API's
 * 
 * @author Shubham pratap Singh
 */
public class DocumentTypeQueryBuilder {
	
	@Autowired
	private static PropertiesManager propertiesManager;

	private static final String documentTypeTableName = ConstantUtility.DOCUMENT_TYPE_TABLE_NAME;
	private static final String defaultPageSize = propertiesManager.getDefaultPageSize();
	private static final String defaultOffset = propertiesManager.getDefaultOffset();
	
	public static final String INSERT_DOCUMENT_TYPE_QUERY = "INSERT INTO " + documentTypeTableName
			+ " (tenantId, name, mandatory, enabled, applicationType, createdBy, lastModifiedBy, createdTime, lastModifiedTime)"
			+ " VALUES(?,?,?,?,?,?,?,?,?)";

	public static final String UPDATE_DOCUMENT_TYPE_QUERY = "UPDATE " + documentTypeTableName
			+ " SET tenantId = ?, name = ?, mandatory = ?, enabled = ?, applicationType = ?,"
			+ " lastModifiedBy = ?, lastModifiedTime = ?" + " WHERE id = ?";

	public static String buildSearchQuery(String tenantId, Integer[] ids, String name, Boolean enabled,
			String applicationType, Integer pageSize, Integer offSet, List<Object> preparedStatementValues) {

		StringBuffer searchSql = new StringBuffer();
		searchSql.append("select * from "+ documentTypeTableName +" where ");
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
			searchSql.append(" AND enabled =? ");
			preparedStatementValues.add(enabled);
		}

		if (applicationType != null && !applicationType.isEmpty()) {
			searchSql.append(" AND applicationType =? ");
			preparedStatementValues.add(applicationType);
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