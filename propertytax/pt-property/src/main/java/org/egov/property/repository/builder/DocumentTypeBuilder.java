package org.egov.property.repository.builder;

public class DocumentTypeBuilder {

	public static final String INSERT_DOCUMENTTYPE_QUERY = "INSERT INTO egpt_documenttype ("
			+ "name,application,createdBy,document,lastModifiedBy, createdTime,"
			+ "lastModifiedTime) VALUES(?,?,?,?,?,?,?)";

	public static final String DOCUMENT_TYPE_BY_DOCUMENT = "select * from egpt_documenttype where document= ?";

	public static String updateDocumentTypeQuery() {

		StringBuffer documentTypeUpdateSQL = new StringBuffer();

		documentTypeUpdateSQL.append("UPDATE egpt_documenttype").append(" SET name = ?, application = ?, document= ?,")
				.append(" lastModifiedBy = ?, lastModifiedTime = ?").append(" WHERE id = ?");

		return documentTypeUpdateSQL.toString();
	}

	public static final String AUDIT_DETAILS_QUERY = "select createdBy,lastModifiedBy,createdTime,"
			+ "lastModifiedTime from egpt_documenttype where id= ?";

}
