package org.egov.property.repository.builder;

public class VacancyRemissionDocumentBuilder {

	public static final String INSERT_VACANCYREMISSIONDOCUMENT_QUERY = "INSERT INTO egpt_vacancyremission_document ("
			+ "fileStore, createdBy, lastModifiedBy, createdTime, lastModifiedTime,"
			+ "documenttype,vacancyremission) VALUES(?,?,?,?,?,?,?)";

	public static String updateVacancyRemissionDocumentQuery() {

		StringBuffer documentUpdateSQL = new StringBuffer();

		documentUpdateSQL.append("UPDATE egpt_vacancyremission_document")
				.append(" SET  fileStore = ?, lastModifiedBy = ?,")
				.append(" lastModifiedTime = ?,documenttype = ?, vacancyremission  = ? ").append(" WHERE id = ?");

		return documentUpdateSQL.toString();
	}
	
	public static final String GETDOCUMENTIDQUERY = "SELECT id FROM egpt_vacancyremission_document"
			+ " WHERE vacancyremission = ?";
	
	public static final String DELETEDOCUMENTQUERY = "DELETE FROM egpt_vacancyremission_document WHERE id = ?";

	public static final String AUDIT_DETAILS_QUERY = "select createdBy,lastModifiedBy,createdTime,"
			+ "lastModifiedTime from egpt_vacancyremission_document where id= ?";
}
