package org.egov.property.repository.builder;

public class PropertyTypesBuilder {

	public static final String INSERT_PROPERTYTYPES_QUERY = "INSERT INTO egpt_mstr_propertytype ( "
			+ "tenantid,code,data,createdby,lastModifiedBy, createdTime,lastModifiedtime, parent) "
			+ "VALUES( ?, ?, ?, ?, ?, ?,?,?)";

	public static final String UPDATE_PROPERTYTYPES_QUERY = "UPDATE egpt_mstr_propertytype SET tenantid = "
			+ "? ,code = ?, data=?, lastModifiedBy =? ," + "lastModifiedtime= ?, parent= ? WHERE id = ?";

	public static final String SELECT_PROPERTYTYPES_CREATETIME = "SELECT createdTime  From egpt_mstr_propertytype WHERE id = ?";

}
