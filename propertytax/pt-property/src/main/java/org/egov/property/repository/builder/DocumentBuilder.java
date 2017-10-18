package org.egov.property.repository.builder;

public class DocumentBuilder {

	public static final String INSERT_DOCUMENT_QUERY = "INSERT INTO egpt_document ("
			+ "fileStore, createdBy, lastModifiedBy, createdTime, lastModifiedTime,"
			+ "propertydetails,documenttype) VALUES(?,?,?,?,?,?,?)";

	public static final String DOCUMENT_BY_PROPERTY_DETAILS_QUERY = "select * from egpt_document where"
			+ " propertydetails = ?";

	public static String updateDocumentQuery() {

		StringBuffer documentUpdateSQL = new StringBuffer();

		documentUpdateSQL.append("UPDATE egpt_document").append(" SET  fileStore = ?, lastModifiedBy = ?,")
				.append(" lastModifiedTime = ?, propertydetails = ?,documenttype = ? ").append(" WHERE id = ?");

		return documentUpdateSQL.toString();
	}

	public static final String AUDIT_DETAILS_QUERY = "select createdBy,lastModifiedBy,createdTime,"
			+ "lastModifiedTime from egpt_document where id= ?";

	public static final String DELETE_DOCUMENT_BY_ID = "delete from egpt_document where id = ?";
}
