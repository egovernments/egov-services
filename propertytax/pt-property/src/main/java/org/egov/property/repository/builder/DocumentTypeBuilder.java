package org.egov.property.repository.builder;

import java.util.List;

import org.egov.property.utility.ConstantUtility;

public class DocumentTypeBuilder {
	public static final String INSERT_DOCUMENTTYPE_MASTER_QUERY = "insert into "
			+ ConstantUtility.DOCUMENT_TYPE_TABLE_NAME
			+ " (tenantId,name,code,application,createdby,lastmodifiedby,createdtime,"
			+ "lastmodifiedtime) VALUES (?,?,?,?,?,?,?,?)";

	public static final String UPDATE_DOCUMENTTYPE_MASTER_QUERY = "UPDATE " + ConstantUtility.DOCUMENT_TYPE_TABLE_NAME
			+ " SET tenantId=?,name=?,code=?, application=?,lastmodifiedby=?,lastmodifiedtime=? WHERE id = ?";

	public static final String SELECT_DOCUMENTTYPE_MASTER_CREATETIME = "SELECT createdTime From "
			+ ConstantUtility.DOCUMENT_TYPE_TABLE_NAME + " WHERE id = ?";

	public static String searchDocumentType(String tenantId, String name, String code, String application,
			Integer pageSize, Integer offSet, List<Object> preparedStatementValues) {
		StringBuffer searchSql = new StringBuffer();

		searchSql.append("select * from " + ConstantUtility.DOCUMENT_TYPE_TABLE_NAME + " where");

		if (tenantId != null) {
			searchSql.append(" tenantId = ?");
			preparedStatementValues.add(tenantId);
		}

		if (name != null) {
			searchSql.append(" AND name = ?");
			preparedStatementValues.add(name);
		}

		if (code != null) {
			searchSql.append(" AND code = ?");
			preparedStatementValues.add(code);
		}

		if (application != null) {
			searchSql.append(" AND application = ?");
			preparedStatementValues.add(application);
		}

		if (pageSize == null)
			pageSize = 30;

		searchSql.append(" limit ?");
		preparedStatementValues.add(pageSize);

		if (offSet == null)
			offSet = 0;

		searchSql.append(" offSet ?");
		preparedStatementValues.add(offSet);

		return searchSql.toString();
	}
}
