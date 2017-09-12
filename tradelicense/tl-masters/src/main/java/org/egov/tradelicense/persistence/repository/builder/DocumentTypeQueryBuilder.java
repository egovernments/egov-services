package org.egov.tradelicense.persistence.repository.builder;

import org.egov.tradelicense.util.ConstantUtility;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;

/**
 * This Class contains INSERT, UPDATE and SELECT queries for DocumentType API's
 * 
 * @author Shubham pratap Singh
 */
public class DocumentTypeQueryBuilder {

	private static final String documentTypeTableName = ConstantUtility.DOCUMENT_TYPE_TABLE_NAME;

	public static final String INSERT_DOCUMENT_TYPE_QUERY = "INSERT INTO " + documentTypeTableName
			+ " (tenantId, name, mandatory, enabled, applicationType, createdBy, lastModifiedBy, createdTime, lastModifiedTime)"
			+ " VALUES(:tenantId, :name, :mandatory, :enabled, :applicationType, :createdBy, :lastModifiedBy, :createdTime, :lastModifiedTime)";

	public static final String UPDATE_DOCUMENT_TYPE_QUERY = "UPDATE " + documentTypeTableName
			+ " SET tenantId = :tenantId, name = :name, mandatory = :mandatory, enabled = :enabled, applicationType = :applicationType,"
			+ " lastModifiedBy = :lastModifiedBy, lastModifiedTime = :lastModifiedTime" + " WHERE id = :id";

	public static String buildSearchQuery(String tenantId, Integer[] ids, String name, String enabled,
			String applicationType, Integer pageSize, Integer offSet, MapSqlParameterSource parameters) {

		StringBuffer searchSql = new StringBuffer();
		searchSql.append("select * from " + documentTypeTableName + " where ");
		searchSql.append(" tenantId = :tenantId ");
		parameters.addValue("tenantId", tenantId);
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
			parameters.addValue("name", name);
		}

		if (enabled != null) {
			if (enabled.equalsIgnoreCase("False")) {
				searchSql.append(" AND enabled = :enabled ");
				parameters.addValue("enabled", false);
			} else if (enabled.equalsIgnoreCase("True")) {
				searchSql.append(" AND enabled = :enabled ");
				parameters.addValue("enabled", true);
			}
		}

		if (applicationType != null && !applicationType.isEmpty()) {
			searchSql.append(" AND applicationType = :applicationType ");
			parameters.addValue("applicationType", applicationType);
		}

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
}