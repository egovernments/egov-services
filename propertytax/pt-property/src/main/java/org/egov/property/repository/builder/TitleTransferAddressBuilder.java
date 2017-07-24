package org.egov.property.repository.builder;

public class TitleTransferAddressBuilder {

	public static final String INSERT_TITLETRANSERADDRESS_QUERY = "INSERT INTO egpt_titletransfer_address ("
			+ "tenantId, latitude, longitude, addressNumber, addressLine1,"
			+ " addressLine2, landmark, city, pincode, detail,"
			+ "createdBy, lastModifiedBy, createdTime, lastModifiedTime, titletransfer)"
			+ "VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";

}
