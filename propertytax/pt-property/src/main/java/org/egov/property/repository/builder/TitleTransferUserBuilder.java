package org.egov.property.repository.builder;

public class TitleTransferUserBuilder {

	public static final String INSERT_TITLETRANSFERUSER_QUERY = "INSERT INTO egpt_titletransfer_owner ("
			+ "titletransfer,owner,isPrimaryOwner, isSecondaryOwner,ownerShipPercentage, ownerType,"
			+ " createdBy, lastModifiedBy, createdTime," + "lastModifiedTime ) VALUES(?,?,?,?,?,?,?,?,?,?) ";

}
