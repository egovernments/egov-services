package org.egov.property.repository.builder;

public class TitleTransferDocumentBuilder {

	public static final String INSERT_TITLETRANSFERDOCUMENT_QUERY = "INSERT INTO egpt_titletransfer_document ("
			+ "fileStore, createdBy, lastModifiedBy, createdTime, lastModifiedTime,"
			+ "titletransfer,documenttype) VALUES(?,?,?,?,?,?,?)";
}
