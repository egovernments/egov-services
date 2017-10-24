package org.egov.property.repository.builder;

import org.egov.property.utility.ConstantUtility;

public class TaxExemptionDocumentBuilder {

	public static final String INSERT_TAXEXEMPTIONDOCUMENT_QUERY = "INSERT INTO "
			+ ConstantUtility.TAXEXEMPTION_DOCUMENT_TABLE_NAME + " ("
			+ "fileStore, createdBy, lastModifiedBy, createdTime, lastModifiedTime,"
			+ "taxexemption,documenttype) VALUES(?,?,?,?,?,?,?)";

	public static String updateTaxExemptionDocumentQuery() {

		StringBuffer documentUpdateSQL = new StringBuffer();

		documentUpdateSQL.append("UPDATE " + ConstantUtility.TAXEXEMPTION_DOCUMENT_TABLE_NAME + " ")
				.append(" SET  fileStore = ?, lastModifiedBy = ?,")
				.append(" lastModifiedTime = ?, taxexemption = ?,documenttype = ? ").append(" WHERE id = ?");

		return documentUpdateSQL.toString();
	}
	
	public static final String GETDOCUMENTIDQUERY = "SELECT id FROM "+ ConstantUtility.TAXEXEMPTION_DOCUMENT_TABLE_NAME
			+ " WHERE taxexemption = ?";
	
	public static final String DELETEDOCUMENTQUERY = "DELETE FROM "+ ConstantUtility.TAXEXEMPTION_DOCUMENT_TABLE_NAME
			+" WHERE id = ?";

	public static final String AUDIT_DETAILS_QUERY = "select createdBy,lastModifiedBy,createdTime,"
			+" lastModifiedTime from "+ConstantUtility.TAXEXEMPTION_DOCUMENT_TABLE_NAME
			+" where id= ?";
}
