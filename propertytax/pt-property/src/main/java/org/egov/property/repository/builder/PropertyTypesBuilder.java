package org.egov.property.repository.builder;

public class PropertyTypesBuilder {

	public static final String INSERT_PROPERTYTYPES_QUERY = "INSERT INTO egpt_mstr_propertytype ( "
			+ "tenantid,code,data,createdby,lastModifiedBy, createdTime,lastModifiedtime) "
			+ "VALUES( ?, ?, ?, ?, ?, ?,?)";

	public static final String UPDATE_PROPERTYTYPES_QUERY = "UPDATE egpt_mstr_propertytype SET tenantid = "
			+ "? ,code = ?, data=?, lastModifiedBy =? ,"
			+ "lastModifiedtime= ? WHERE id = ?";

}
